package br.furb.inf.tcc.tankcoders.scene.tank.weapon;

import br.furb.inf.tcc.tankcoders.scene.tank.ITank;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingVolume;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Sphere;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.material.Material;

/**
 * Represents a bullet of the main gun.
 * @author Germano Fronza
 */
public abstract class Bullet extends Node implements Projectile {

    private static final long serialVersionUID = 1L;

    /**
	 * Used to fire a bullet in local game instance.
	 */
    public static final short LOCAL = 1;

    /**
	 * Used to fire a bullet from another game instance.
	 */
    public static final short REMOTE = 2;

    /**
	 * Tank owner of this bullet. 
	 */
    protected ITank owner;

    /**
	 * Dynamic node of the bullet.
	 */
    private DynamicPhysicsNode bulletDyn;

    /**
	 * Visible sphere of the bullet.
	 */
    private Sphere bulletSphere;

    /**
	 * Quantity of frames which this bullet is in the scene.
	 */
    private int framesCount;

    /**
	 * Define if this bullet can damage some tank.
	 * Used with local bullets.
	 */
    private boolean canDamage;

    /**
	 * Creates a new bullet.
	 * @param owner
	 * @param base
	 * @param pSpace
	 * @param absPosition
	 */
    public Bullet(final ITank owner, final Node base, PhysicsSpace pSpace, Vector3f absPosition) {
        super("bulletNode");
        this.owner = owner;
        this.canDamage = true;
        absPosition = getNewRelativePositionByTankMainHeading(base.getLocalTranslation(), absPosition);
        bulletDyn = pSpace.createDynamicNode();
        bulletDyn.setName("bulletDyn");
        bulletDyn.setLocalTranslation(absPosition);
        bulletDyn.getLocalRotation().set(base.getWorldRotation());
        bulletSphere = new Sphere("bulletSphere", 10, 10, 1);
        bulletSphere.setModelBound(new BoundingBox());
        bulletSphere.updateModelBound();
        bulletSphere.setSolidColor(getColor());
        bulletDyn.attachChild(bulletSphere);
        bulletDyn.createSphere("bulletDynSphere");
        bulletDyn.setLocalScale(getBulletScale());
        bulletDyn.generatePhysicsGeometry();
        bulletDyn.setMaterial(Material.RUBBER);
        bulletDyn.setMass(getBulletMass());
        this.attachChild(bulletDyn);
    }

    /**
	 * Gets world bound used to check collisions.
	 */
    public BoundingVolume getSphereWorldBound() {
        return bulletSphere.getWorldBound();
    }

    /**
	 * Gets a new relative position based on tank main gun heading.
	 * @param absPosition
	 * @param relativePosition
	 * @return Vector3f
	 */
    private Vector3f getNewRelativePositionByTankMainHeading(Vector3f absPosition, Vector3f relativePosition) {
        float ownerHeading = owner.getCurrentMainGunHeading();
        float sinAngle = FastMath.sin(ownerHeading);
        float cosAngle = FastMath.cos(ownerHeading);
        float newX = (relativePosition.z * sinAngle) + (relativePosition.x * cosAngle);
        float newZ = (relativePosition.z * cosAngle) + (relativePosition.x * sinAngle);
        relativePosition = new Vector3f(newX, relativePosition.y, newZ);
        absPosition = absPosition.add(relativePosition);
        return absPosition;
    }

    public DynamicPhysicsNode getBulletDyn() {
        return this.bulletDyn;
    }

    public ITank getOwner() {
        return this.owner;
    }

    public Vector3f getLocation() {
        return this.getWorldTranslation();
    }

    public void removeFromScene() {
        removeFromParent();
    }

    public void hitByProjectile(Projectile projectile) {
        removeFromScene();
    }

    /** Scale of the bullet */
    protected abstract float getBulletScale();

    /** Mass of the bullet */
    protected abstract float getBulletMass();

    /** Gets the vector3f based on the current heading of the maingun */
    public Vector3f getVector() {
        float mainGunHeading = owner.getCurrentMainGunHeading();
        float newX = FastMath.cos(mainGunHeading);
        float newZ = FastMath.sin(mainGunHeading);
        float newY = .02f;
        float mainGunZrotAngle = owner.getCurrentMainGunZRotAngle();
        float cosZrot = FastMath.cos(mainGunZrotAngle);
        float sinZrot = FastMath.sin(mainGunZrotAngle);
        newX = (newX * cosZrot) - (newY * sinZrot);
        newY = (newX * sinZrot) + (newY * cosZrot);
        return new Vector3f(newX, newY, newZ);
    }

    public void incFrameCount() {
        framesCount++;
    }

    public boolean isAlive() {
        return (framesCount < 300);
    }

    /** Mult force over getVector() */
    public abstract float getMultForce();

    /** Power of damage. This value is related with ITank.INITIAL_HEALTH */
    public abstract int getPower();

    public ColorRGBA getColor() {
        return ColorRGBA.brown;
    }

    public Sphere getBulletSphere() {
        return bulletSphere;
    }

    public boolean isCanDamage() {
        return canDamage;
    }

    public void setCanDamage(boolean canDamage) {
        this.canDamage = canDamage;
    }
}
