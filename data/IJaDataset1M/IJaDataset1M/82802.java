package com.calefay.exodusDefence.edAI;

import com.calefay.exodusDefence.entities.EDTurret;
import com.calefay.exodusDefence.radar.EDGenericRadar;
import com.calefay.exodusDefence.weapons.EDProjectileGun;
import com.calefay.utils.GameEntity;
import com.calefay.utils.GameRemoveable;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public class EDTurretSimpleAI implements GameRemoveable, EDAIController {

    private static final float MAXSPIN = FastMath.PI / 2;

    private static final float MAXELEV = FastMath.PI / 2;

    private static final float MINSPIN = 0.001f;

    private static final float MINELEV = 0.001f;

    private static final float SHOOTANGLE = FastMath.PI / 20f;

    private float weaponRange = -1f;

    private EDTurret handledTurret = null;

    private GameEntity target = null;

    private EDGenericRadar targetFinder = null;

    private boolean active = false;

    private boolean suspended = false;

    private boolean firing = false;

    private boolean wantToFire = false;

    private float spinAngle = 0;

    private float elevAngle = 0;

    private float targetRangeSquared = 0;

    public EDTurretSimpleAI(EDTurret t, EDGenericRadar targetFinder) {
        active = true;
        suspended = false;
        handledTurret = t;
        this.targetFinder = targetFinder;
        spinAngle = 0;
        elevAngle = 0;
        firing = false;
        wantToFire = false;
    }

    public void update(float interpolation) {
        if (!active || (handledTurret.isDead())) return;
        if (!handledTurret.isActive()) deactivate();
        wantToFire = false;
        if (suspended) return;
        target = targetFinder.getTarget();
        if (target != null) {
            calculateAngles(interpolation);
            float spinActual = spinAngle;
            float elevActual = elevAngle;
            float ms = MAXSPIN * interpolation;
            float me = MAXELEV * interpolation;
            if (spinActual > ms) spinActual = ms;
            if (spinActual < -ms) spinActual = -ms;
            if (elevActual > me) elevActual = me;
            if (elevActual < -me) elevActual = -me;
            if ((spinActual > MINSPIN) || (spinActual < -MINSPIN)) handledTurret.spin(spinActual);
            if ((elevActual > MINELEV) || (elevActual < -MINELEV)) handledTurret.elevate(elevActual);
            handledTurret.getTurretNode().updateWorldData(interpolation);
            if ((spinAngle > -SHOOTANGLE) && (spinAngle < SHOOTANGLE) && (elevAngle > -SHOOTANGLE) && (elevAngle < SHOOTANGLE)) {
                wantToFire = true;
            } else {
                wantToFire = false;
            }
        }
        if ((weaponRange > 0) && (targetRangeSquared > (weaponRange * weaponRange))) {
            wantToFire = false;
            target = null;
        }
        if (wantToFire && !firing) {
            if (handledTurret.getPrimaryGunBarrel1() != null) handledTurret.getPrimaryGunBarrel1().fire();
            if (handledTurret.getPrimaryGunBarrel2() != null) handledTurret.getPrimaryGunBarrel2().fire();
            firing = true;
        } else if (firing && !wantToFire) {
            if (handledTurret.getPrimaryGunBarrel1() != null) handledTurret.getPrimaryGunBarrel1().stopFiring();
            if (handledTurret.getPrimaryGunBarrel2() != null) handledTurret.getPrimaryGunBarrel2().stopFiring();
            firing = false;
        }
    }

    protected void calculateAngles(float tpf) {
        Quaternion rotWorldtoLocal = handledTurret.getTurretNode().getWorldRotation().inverse();
        Vector3f actualPos = target.getPosition();
        Vector3f targetPos = actualPos;
        if (handledTurret.getPrimaryGunBarrel1() instanceof EDProjectileGun) targetPos = new Vector3f(actualPos.add(target.getVelocity().mult(tpf * 20)));
        Vector3f position = handledTurret.getTurretNode().getWorldTranslation();
        Vector3f offset = new Vector3f();
        targetPos.subtract(position, offset);
        targetRangeSquared = offset.lengthSquared();
        Vector3f direction = new Vector3f(rotWorldtoLocal.mult(new Vector3f(offset.x, 0, offset.z).normalize()));
        spinAngle = Vector3f.UNIT_Z.angleBetween(direction);
        if (direction.x < 0) spinAngle = -spinAngle;
        rotWorldtoLocal = handledTurret.getMountingNode().getWorldRotation().inverse();
        Quaternion q = new Quaternion();
        q.fromAngleNormalAxis(-spinAngle, Vector3f.UNIT_Y);
        direction.set(q.mult(offset.normalize()));
        direction.set(rotWorldtoLocal.mult(direction));
        elevAngle = Vector3f.UNIT_Z.angleBetween(direction) / 2;
        if (direction.y > 0) elevAngle = -elevAngle;
    }

    public void setWeaponRange(float range) {
        weaponRange = range;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
        targetFinder.deactivate();
    }

    /** Suspends the ai. Used for when the turret is under player control.*/
    public void setSuspend(boolean s) {
        suspended = s;
    }

    public void cleanup() {
        targetFinder = null;
        active = false;
        suspended = false;
        handledTurret = null;
        target = null;
        targetFinder = null;
        spinAngle = 0;
        elevAngle = 0;
    }
}
