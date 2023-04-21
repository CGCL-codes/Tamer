package com.calefay.exodusDefence.weapons;

import com.calefay.effects.ManagedParticleEffect;
import com.calefay.utils.GameWorldInfo;
import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.SharedMesh;
import com.jme.scene.TriMesh;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

public class MissileProjectile extends BaseProjectile {

    private static TriMesh masterMesh = null;

    private static TextureState masterTexture = null;

    protected SharedMesh missileMesh = null;

    protected ManagedParticleEffect thrusterEffect = null;

    protected float missileAcceleration = 0;

    protected float missileMaxSpeed = 0;

    protected float armingDelay = 0;

    protected boolean launched = false;

    public MissileProjectile(String newName, Vector3f pos, Quaternion orientation, float speed, float acceleration, float maxSpeed, float lifeTime, float damage, boolean launch) {
        super(newName, pos, orientation, speed, lifeTime, damage);
        missileAcceleration = acceleration;
        missileMaxSpeed = maxSpeed;
        thrusterEffect = null;
        if (launch) launch(); else launched = false;
        if (masterMesh != null) {
            missileMesh = new SharedMesh(name + "MissileMesh", masterMesh);
            if (masterTexture == null) {
                MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
                ms.setAmbient(ColorRGBA.white);
                ms.setDiffuse(ColorRGBA.white);
                ms.setSpecular(ColorRGBA.white);
                ms.setShininess(40);
                projectileNode.setRenderState(ms);
            } else {
                projectileNode.setRenderState(masterTexture);
            }
            projectileNode.attachChild(missileMesh);
            projectileNode.updateRenderState();
        }
        projectileNode.setModelBound(new BoundingBox());
        projectileNode.updateModelBound();
    }

    public MissileProjectile(String newName, float speed, float acceleration, float maxSpeed, float lifeTime, float damage, boolean launch) {
        this(newName, new Vector3f(), new Quaternion(), speed, acceleration, maxSpeed, lifeTime, damage, launch);
    }

    /** used by the manager class to reset a missile when recycled. */
    protected void reset(String newName, Vector3f pos, Quaternion orientation, float speed, float acceleration, float lifeTime, float damage) {
        super.reset(newName, pos, orientation, speed, damage, lifeTime);
        missileAcceleration = acceleration;
        if (missileMesh != null) {
            missileMesh.setName(name + "MissileMesh");
        }
    }

    public void update(float interpolation) {
        if (active && launched) {
            if (armingDelay > 0) {
                armingDelay -= interpolation;
                if (armingDelay < 0) armingDelay = 0;
            }
            projectileSpeed += missileAcceleration * interpolation;
            if (projectileSpeed > missileMaxSpeed) {
                projectileSpeed = missileMaxSpeed;
            }
            super.update(interpolation);
        }
    }

    public void deactivate() {
        if (!deactivating) {
            if (thrusterEffect != null) {
                Vector3f worldPos = thrusterEffect.getParticleNode().getWorldTranslation();
                thrusterEffect.startDying(2.0f, GameWorldInfo.getGameWorldInfo().getRootNode());
                thrusterEffect.getParticleNode().setLocalTranslation(worldPos);
                thrusterEffect.getParticleNode().updateWorldVectors();
                thrusterEffect = null;
            }
        }
        super.deactivate();
    }

    public void cleanup() {
        super.cleanup();
        if (missileMesh != null) {
            missileMesh.removeFromParent();
            missileMesh = null;
        }
        if (thrusterEffect != null) {
            thrusterEffect.cleanup();
            thrusterEffect = null;
        }
    }

    public void checkHits(float interpolation) {
        if (armingDelay > 0) return;
        if (missileMesh != null) missileMesh.setIsCollidable(false);
        super.checkHits(interpolation);
        if (missileMesh != null) missileMesh.setIsCollidable(true);
    }

    public void addParticleTrail(String effectPath) {
        thrusterEffect = GameWorldInfo.getGameWorldInfo().getParticleManager().getManagedParticleEffect(name + "ThrusterParticles", null, 0.0f, effectPath, projectileNode);
    }

    public void addParticleTrail(ManagedParticleEffect effect) {
        thrusterEffect = effect;
        thrusterEffect.attachTo(getProjectileNode());
    }

    public void setMissileSize(float scaleFactor) {
        missileMesh.setLocalScale(scaleFactor);
        missileMesh.updateModelBound();
    }

    public void setMaxSpeed(float s) {
        missileMaxSpeed = s;
    }

    /** Launches the missile (not needed if launched in the constructor.*/
    public void launch() {
        launched = true;
    }

    public boolean isLaunched() {
        return launched;
    }

    public static void setMissileTexture(TextureState t) {
        masterTexture = t;
    }

    public static void setMissileMesh(TriMesh masterMissileMesh) {
        masterMesh = masterMissileMesh;
        setBounds(new Vector3f(-100f, -100f, -100f), new Vector3f(100f, 100f, 100f));
    }
}
