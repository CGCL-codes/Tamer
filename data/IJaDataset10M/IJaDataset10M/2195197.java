package br.furb.inf.tcc.tankcoders.scene.tank.weapon.maingun.bullet;

import br.furb.inf.tcc.tankcoders.scene.tank.ITank;
import br.furb.inf.tcc.tankcoders.scene.tank.m1abrams.M1AbramsTank;
import br.furb.inf.tcc.tankcoders.scene.tank.weapon.Bullet;
import com.jme.math.Vector3f;

/**
 * Represents a bullet of the M1Abrams main gun.
 * @author Germano Fronza
 */
public class M1AbramsMainGunBullet extends Bullet {

    private static final long serialVersionUID = 1L;

    public M1AbramsMainGunBullet(ITank owner, Vector3f relativePosition) {
        super(owner, ((M1AbramsTank) owner).getMainGun().getMainNode(), ((M1AbramsTank) owner).getPhysicsSpace(), relativePosition);
    }

    protected float getBulletMass() {
        return 1;
    }

    protected float getBulletScale() {
        return 0.3f;
    }

    public float getMultForce() {
        return 40000;
    }

    public int getPower() {
        return 35;
    }
}
