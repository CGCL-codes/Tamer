package com.bulletphysics.collision.dispatch;

import com.bulletphysics.collision.broadphase.BroadphaseProxy;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.linearmath.Transform;
import javax.vecmath.Vector3f;

/**
 * CollisionObject can be used to manage collision detection objects.
 * It maintains all information that is needed for a collision detection: {@link CollisionShape},
 * {@link Transform} and {@link BroadphaseProxy AABB proxy}. It can be added to {@link CollisionWorld}.
 * 
 * @author jezek2
 */
public class CollisionObject {

    public static final int ACTIVE_TAG = 1;

    public static final int ISLAND_SLEEPING = 2;

    public static final int WANTS_DEACTIVATION = 3;

    public static final int DISABLE_DEACTIVATION = 4;

    public static final int DISABLE_SIMULATION = 5;

    public Transform worldTransform = new Transform();

    protected final Transform interpolationWorldTransform = new Transform();

    protected final Vector3f interpolationLinearVelocity = new Vector3f();

    protected final Vector3f interpolationAngularVelocity = new Vector3f();

    protected BroadphaseProxy broadphaseHandle;

    protected CollisionShape collisionShape;

    protected CollisionShape rootCollisionShape;

    protected int collisionFlags;

    protected int islandTag1;

    protected int companionId;

    protected int activationState1;

    protected float deactivationTime;

    protected float friction;

    protected float restitution;

    protected Object userObjectPointer;

    protected CollisionObjectType internalType = CollisionObjectType.COLLISION_OBJECT;

    protected float hitFraction;

    protected float ccdSweptSphereRadius;

    protected float ccdSquareMotionThreshold;

    protected boolean checkCollideWith;

    public CollisionObject() {
        this.collisionFlags = CollisionFlags.STATIC_OBJECT;
        this.islandTag1 = -1;
        this.companionId = -1;
        this.activationState1 = 1;
        this.friction = 0.5f;
        this.hitFraction = 1f;
    }

    public boolean checkCollideWithOverride(CollisionObject co) {
        return true;
    }

    public boolean mergesSimulationIslands() {
        return ((collisionFlags & (CollisionFlags.STATIC_OBJECT | CollisionFlags.KINEMATIC_OBJECT | CollisionFlags.NO_CONTACT_RESPONSE)) == 0);
    }

    public boolean isStaticObject() {
        return (collisionFlags & CollisionFlags.STATIC_OBJECT) != 0;
    }

    public boolean isKinematicObject() {
        return (collisionFlags & CollisionFlags.KINEMATIC_OBJECT) != 0;
    }

    public boolean isStaticOrKinematicObject() {
        return (collisionFlags & (CollisionFlags.KINEMATIC_OBJECT | CollisionFlags.STATIC_OBJECT)) != 0;
    }

    public boolean hasContactResponse() {
        return (collisionFlags & CollisionFlags.NO_CONTACT_RESPONSE) == 0;
    }

    public CollisionShape getCollisionShape() {
        return collisionShape;
    }

    public void setCollisionShape(CollisionShape collisionShape) {
        this.collisionShape = collisionShape;
        this.rootCollisionShape = collisionShape;
    }

    public CollisionShape getRootCollisionShape() {
        return rootCollisionShape;
    }

    /**
	 * Avoid using this internal API call.
	 * internalSetTemporaryCollisionShape is used to temporary replace the actual collision shape by a child collision shape.
	 */
    public void internalSetTemporaryCollisionShape(CollisionShape collisionShape) {
        this.collisionShape = collisionShape;
    }

    public int getActivationState() {
        return activationState1;
    }

    public void setActivationState(int newState) {
        if ((activationState1 != DISABLE_DEACTIVATION) && (activationState1 != DISABLE_SIMULATION)) {
            this.activationState1 = newState;
        }
    }

    public float getDeactivationTime() {
        return deactivationTime;
    }

    public void setDeactivationTime(float deactivationTime) {
        this.deactivationTime = deactivationTime;
    }

    public void forceActivationState(int newState) {
        this.activationState1 = newState;
    }

    public void activate() {
        activate(false);
    }

    public void activate(boolean forceActivation) {
        if (forceActivation || (collisionFlags & (CollisionFlags.STATIC_OBJECT | CollisionFlags.KINEMATIC_OBJECT)) == 0) {
            setActivationState(ACTIVE_TAG);
            deactivationTime = 0f;
        }
    }

    public boolean isActive() {
        return ((getActivationState() != ISLAND_SLEEPING) && (getActivationState() != DISABLE_SIMULATION));
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public CollisionObjectType getInternalType() {
        return internalType;
    }

    public Transform getWorldTransform(Transform out) {
        out.set(worldTransform);
        return out;
    }

    public void setWorldTransform(Transform worldTransform) {
        this.worldTransform.set(worldTransform);
    }

    public BroadphaseProxy getBroadphaseHandle() {
        return broadphaseHandle;
    }

    public void setBroadphaseHandle(BroadphaseProxy broadphaseHandle) {
        this.broadphaseHandle = broadphaseHandle;
    }

    public Transform getInterpolationWorldTransform(Transform out) {
        out.set(interpolationWorldTransform);
        return out;
    }

    public void setInterpolationWorldTransform(Transform interpolationWorldTransform) {
        this.interpolationWorldTransform.set(interpolationWorldTransform);
    }

    public Vector3f getInterpolationLinearVelocity(Vector3f out) {
        out.set(interpolationLinearVelocity);
        return out;
    }

    public Vector3f getInterpolationAngularVelocity(Vector3f out) {
        out.set(interpolationAngularVelocity);
        return out;
    }

    public int getIslandTag() {
        return islandTag1;
    }

    public void setIslandTag(int islandTag) {
        this.islandTag1 = islandTag;
    }

    public int getCompanionId() {
        return companionId;
    }

    public void setCompanionId(int companionId) {
        this.companionId = companionId;
    }

    public float getHitFraction() {
        return hitFraction;
    }

    public void setHitFraction(float hitFraction) {
        this.hitFraction = hitFraction;
    }

    public int getCollisionFlags() {
        return collisionFlags;
    }

    public void setCollisionFlags(int collisionFlags) {
        this.collisionFlags = collisionFlags;
    }

    public float getCcdSweptSphereRadius() {
        return ccdSweptSphereRadius;
    }

    public void setCcdSweptSphereRadius(float ccdSweptSphereRadius) {
        this.ccdSweptSphereRadius = ccdSweptSphereRadius;
    }

    public float getCcdSquareMotionThreshold() {
        return ccdSquareMotionThreshold;
    }

    public void setCcdSquareMotionThreshold(float ccdSquareMotionThreshold) {
        this.ccdSquareMotionThreshold = ccdSquareMotionThreshold;
    }

    public Object getUserPointer() {
        return userObjectPointer;
    }

    public void setUserPointer(Object userObjectPointer) {
        this.userObjectPointer = userObjectPointer;
    }

    public boolean checkCollideWith(CollisionObject co) {
        if (checkCollideWith) {
            return checkCollideWithOverride(co);
        }
        return true;
    }
}
