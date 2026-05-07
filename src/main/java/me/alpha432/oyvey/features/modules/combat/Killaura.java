package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;

public class KillAura extends Module {

    private final float range = 4.5f;
    private final int cps = 10;

    private long lastHit;

    public KillAura() {
        super("KillAura", "Automatically attacks entities.", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {

        if (mc.player == null || mc.world == null)
            return;

        Entity target = getTarget();

        if (target == null)
            return;

        faceTarget(target);

        long delay = 1000L / cps;

        if (System.currentTimeMillis() - lastHit >= delay) {

            mc.playerController.attackEntity(mc.player, target);
            mc.player.swingArm(EnumHand.MAIN_HAND);

            lastHit = System.currentTimeMillis();
        }
    }

    private Entity getTarget() {

        Entity closest = null;
        double distance = range;

        for (Entity entity : mc.world.loadedEntityList) {

            if (!(entity instanceof EntityLivingBase))
                continue;

            if (entity == mc.player)
                continue;

            if (entity.isDead)
                continue;

            double dist = mc.player.getDistance(entity);

            if (dist <= distance) {
                distance = dist;
                closest = entity;
            }
        }

        return closest;
    }

    private void faceTarget(Entity entity) {

        double x = entity.posX - mc.player.posX;
        double y = (entity.posY + entity.getEyeHeight()) -
                   (mc.player.posY + mc.player.getEyeHeight());
        double z = entity.posZ - mc.player.posZ;

        double dist = Math.sqrt(x * x + z * z);

        float yaw = (float)(Math.toDegrees(Math.atan2(z, x)) - 90F);
        float pitch = (float)-Math.toDegrees(Math.atan2(y, dist));

        mc.player.rotationYaw = yaw;
        mc.player.rotationPitch = pitch;
    }
}
