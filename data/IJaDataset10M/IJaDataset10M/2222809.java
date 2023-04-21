package com.jmex.jbullet.nodes;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingCapsule;
import com.jme.bounding.BoundingSphere;
import com.jme.bounding.BoundingVolume;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jmex.jbullet.PhysicsSpace;
import com.jmex.jbullet.collision.CollisionObject;
import com.jmex.jbullet.collision.shapes.BoxCollisionShape;
import com.jmex.jbullet.collision.shapes.CapsuleCollisionShape;
import com.jmex.jbullet.collision.shapes.CollisionShape;
import com.jmex.jbullet.collision.shapes.CollisionShape.ShapeTypes;
import com.jmex.jbullet.collision.shapes.CylinderCollisionShape;
import com.jmex.jbullet.collision.shapes.GImpactCollisionShape;
import com.jmex.jbullet.collision.shapes.MeshCollisionShape;
import com.jmex.jbullet.collision.shapes.SphereCollisionShape;
import com.jmex.jbullet.util.Converter;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * <p>PhysicsNode - Basic jbullet-jme physics object</p>
 * @see com.jmex.jbullet.PhysicsSpace
 * @author normenhansen
 */
public class PhysicsNode extends CollisionObject {

    protected RigidBody rBody;

    private RigidBodyConstructionInfo constructionInfo;

    protected MotionState motionState;

    protected CollisionShape collisionShape;

    protected float mass = 1f;

    private boolean kinematic = false;

    private boolean physicsEnabled = true;

    private final Quaternion tmp_inverseWorldRotation = new Quaternion();

    protected Transform tempTrans = new Transform();

    protected javax.vecmath.Vector3f tempVel = new javax.vecmath.Vector3f();

    protected javax.vecmath.Quat4f tempRot = new javax.vecmath.Quat4f();

    protected com.jme.math.Vector3f tempLocation = new com.jme.math.Vector3f();

    protected com.jme.math.Quaternion tempRotation = new com.jme.math.Quaternion();

    protected com.jme.math.Matrix3f tempMatrix = new com.jme.math.Matrix3f();

    private javax.vecmath.Vector3f localInertia = new javax.vecmath.Vector3f();

    protected Transform motionStateTrans = new Transform();

    private Vector3f continuousForce = new Vector3f();

    private Vector3f continuousForceLocation = new Vector3f();

    private Vector3f continuousTorque = new Vector3f();

    protected GameTaskQueue pQueue = GameTaskQueueManager.getManager().getQueue("jbullet_update");

    private boolean applyForce = false;

    private boolean applyTorque = false;

    public PhysicsNode() {
    }

    /**
     * creates a new PhysicsNode with the supplied child node or geometry and
     * creates the standard sphere collision shape for that PhysicsNode<br>
     * @param child
     */
    public PhysicsNode(Spatial child) {
        this(child, ShapeTypes.SPHERE);
    }

    /**
     * creates a new PhysicsNode with the supplied child node or geometry and
     * also creates a collision shape of the given type for that PhysicsNode
     * @param child
     * @param collisionShapeType
     */
    @Deprecated
    public PhysicsNode(Spatial child, int collisionShapeType) {
        this(child, collisionShapeType, 1.0f);
    }

    /**
     * creates a new PhysicsNode with the supplied child node or geometry and
     * also creates a collision shape of the given type for that PhysicsNode and
     * assigns the given mass.
     * @param child
     * @param collisionShapeType
     * @param mass
     */
    @Deprecated
    public PhysicsNode(Spatial child, int collisionShapeType, float mass) {
        this.attachChild(child);
        this.mass = mass;
        createMotionState();
        createCollisionShape(collisionShapeType);
    }

    /**
     * creates a new PhysicsNode with the supplied child node or geometry and
     * uses the supplied collision shape for that PhysicsNode<br>
     * @param child
     * @param shape
     */
    public PhysicsNode(Spatial child, CollisionShape shape) {
        this(child, shape, 1.0f);
    }

    /**
     * creates a new PhysicsNode with the supplied child node or geometry and
     * uses the supplied collision shape for that PhysicsNode<br>
     * @param child
     * @param shape
     */
    public PhysicsNode(Spatial child, CollisionShape shape, float mass) {
        this.attachChild(child);
        this.mass = mass;
        this.collisionShape = shape;
        createMotionState();
        rebuildRigidBody();
    }

    protected void createMotionState() {
        motionState = new MotionState() {

            public Transform getWorldTransform(Transform out) {
                if (out == null) out = new Transform();
                tempRotation.set(getWorldRotation());
                Converter.convert(tempRotation, tempRot);
                out.basis.set(tempRot);
                out.origin.set(Converter.convert(getWorldTranslation()));
                return out;
            }

            public void setWorldTransform(Transform worldTrans) {
                motionStateTrans.set(worldTrans);
                applyMotionState();
            }
        };
    }

    private void applyMotionState() {
        Converter.convert(motionStateTrans.origin, tempLocation);
        setWorldTranslation(tempLocation);
        Converter.convert(motionStateTrans.basis, tempMatrix);
        tempRotation.fromRotationMatrix(tempMatrix);
        setWorldRotation(tempRotation);
    }

    /**
     * builds/rebuilds the phyiscs body when parameters have changed
     */
    protected void rebuildRigidBody() {
        boolean removed = false;
        Transform trans = new Transform();
        javax.vecmath.Vector3f vec = new javax.vecmath.Vector3f();
        if (rBody != null) {
            System.out.println("rebuild");
            rBody.getWorldTransform(trans);
            rBody.getAngularVelocity(vec);
            if (rBody.isInWorld()) {
                PhysicsSpace.getPhysicsSpace().remove(this);
                removed = true;
            }
            rBody.destroy();
        }
        preRebuild();
        rBody = new RigidBody(constructionInfo);
        postRebuild();
        if (removed) {
            rBody.setWorldTransform(trans);
            rBody.setAngularVelocity(vec);
            PhysicsSpace.getPhysicsSpace().add(this);
        }
    }

    protected void preRebuild() {
        collisionShape.calculateLocalInertia(mass, localInertia);
        if (constructionInfo == null) constructionInfo = new RigidBodyConstructionInfo(mass, motionState, collisionShape.getCShape(), localInertia); else constructionInfo.collisionShape = collisionShape.getCShape();
    }

    protected void postRebuild() {
        rBody.setUserPointer(this);
        if (mass == 0.0f) {
            rBody.setCollisionFlags(rBody.getCollisionFlags() | CollisionFlags.STATIC_OBJECT);
        } else {
            rBody.setCollisionFlags(rBody.getCollisionFlags() & ~CollisionFlags.STATIC_OBJECT);
        }
    }

    public void setCcdSweptSphereRadius(float radius) {
        rBody.setCcdSweptSphereRadius(radius);
    }

    public void setCcdMotionThreshold(float threshold) {
        rBody.setCcdMotionThreshold(threshold);
    }

    public float getCcdSweptSphereRadius() {
        return rBody.getCcdSweptSphereRadius();
    }

    public float getCcdMotionThreshold() {
        return rBody.getCcdMotionThreshold();
    }

    public float getCcdSquareMotionThreshold() {
        return rBody.getCcdSquareMotionThreshold();
    }

    @Override
    public int attachChild(Spatial child) {
        if (child instanceof PhysicsNode) throw (new IllegalArgumentException("PhysicsNodes cannot have other PhysicsNodes as children!"));
        return super.attachChild(child);
    }

    @Override
    public int attachChildAt(Spatial child, int index) {
        if (child instanceof PhysicsNode) throw (new IllegalArgumentException("PhysicsNodes cannot have other PhysicsNodes as children!"));
        return super.attachChildAt(child, index);
    }

    /**
     * note that getLocalTranslation().set() will not update the physics object position,
     * use setLocalTranslation() instead!
     */
    @Override
    public Vector3f getLocalTranslation() {
        return super.getLocalTranslation();
    }

    /**
     * sets the local translation of this node
     * @param arg0
     */
    @Override
    public void setLocalTranslation(Vector3f arg0) {
        super.setLocalTranslation(arg0);
        applyTranslation();
    }

    /**
     * sets the local translation of this node
     */
    @Override
    public void setLocalTranslation(float x, float y, float z) {
        super.setLocalTranslation(x, y, z);
        applyTranslation();
    }

    private void applyTranslation() {
        super.updateGeometricState(0, true);
        tempLocation.set(getWorldTranslation());
        rBody.getWorldTransform(tempTrans);
        Converter.convert(tempLocation, tempTrans.origin);
        rBody.setWorldTransform(tempTrans);
    }

    /**
     * note that getLocalRotation().set() will not update the physics object position,
     * use setLocalRotation() instead!
     */
    @Override
    public Quaternion getLocalRotation() {
        return super.getLocalRotation();
    }

    /**
     * sets the local rotation of this node, the physics object will be updated accordingly
     * in the next global physics update tick
     * @param arg0
     */
    @Override
    public void setLocalRotation(Matrix3f arg0) {
        super.setLocalRotation(arg0);
        applyRotation();
    }

    /**
     * sets the local rotation of this node, the physics object will be updated accordingly
     * in the next global physics update tick
     * @param arg0
     */
    @Override
    public void setLocalRotation(Quaternion arg0) {
        super.setLocalRotation(arg0);
        applyRotation();
    }

    @Override
    public void lookAt(Vector3f position, Vector3f upVector) {
        super.lookAt(position, upVector);
        applyRotation();
    }

    @Override
    public void lookAt(Vector3f position, Vector3f upVector, boolean takeParentInAccount) {
        super.lookAt(position, upVector, takeParentInAccount);
        applyRotation();
    }

    @Override
    public void rotateUpTo(Vector3f newUp) {
        super.rotateUpTo(newUp);
        applyRotation();
    }

    private void applyRotation() {
        super.updateGeometricState(0, true);
        tempRotation.set(getWorldRotation());
        Converter.convert(tempRotation, tempRot);
        rBody.getWorldTransform(tempTrans);
        tempTrans.setRotation(tempRot);
        rBody.setWorldTransform(tempTrans);
    }

    /**
     * computes the local translation from the parameter translation and sets it as new
     * local translation.<br>
     * This should only be called from the physics thread to update the jme spatial
     * @param translation new world translation of this spatial.
     * @return the computed local translation
     */
    protected Vector3f setWorldTranslation(Vector3f translation) {
        Vector3f localTranslation = this.getLocalTranslation();
        if (parent != null) {
            localTranslation.set(translation).subtractLocal(parent.getWorldTranslation());
            localTranslation.divideLocal(parent.getWorldScale());
            tmp_inverseWorldRotation.set(parent.getWorldRotation()).inverseLocal().multLocal(localTranslation);
        } else {
            localTranslation.set(translation);
        }
        return localTranslation;
    }

    /**
     * computes the local rotation from the parameter rot and sets it as new
     * local rotation.<br>
     * This should only be called from the physics thread to update the jme spatial
     * @param rot new world rotation of this spatial.
     * @return the computed local rotation
     */
    protected Quaternion setWorldRotation(Quaternion rot) {
        Quaternion localRotation = getLocalRotation();
        if (parent != null) {
            tmp_inverseWorldRotation.set(parent.getWorldRotation()).inverseLocal().mult(rot, localRotation);
        } else {
            localRotation.set(rot);
        }
        return localRotation;
    }

    @Override
    public void setLocalScale(float localScale) {
        super.setLocalScale(localScale);
        updateWorldBound();
        updateGeometricState(0, true);
        updateWorldBound();
        collisionShape.setScale(getWorldScale());
    }

    @Override
    public void setLocalScale(Vector3f localScale) {
        super.setLocalScale(localScale);
        updateWorldBound();
        updateGeometricState(0, true);
        updateWorldBound();
        collisionShape.setScale(getWorldScale());
    }

    public void setKinematic(boolean kinematic) {
        this.kinematic = kinematic;
        if (kinematic) {
            rBody.setCollisionFlags(rBody.getCollisionFlags() | CollisionFlags.KINEMATIC_OBJECT);
            rBody.setActivationState(com.bulletphysics.collision.dispatch.CollisionObject.DISABLE_DEACTIVATION);
        } else {
            rBody.setCollisionFlags(rBody.getCollisionFlags() & ~CollisionFlags.KINEMATIC_OBJECT);
            rBody.setActivationState(com.bulletphysics.collision.dispatch.CollisionObject.ACTIVE_TAG);
        }
    }

    public boolean isKinematic() {
        return kinematic;
    }

    public float getMass() {
        return mass;
    }

    /**
     * sets the mass of this PhysicsNode, objects with mass=0 are static.
     * @param mass
     */
    public void setMass(float mass) {
        this.mass = mass;
        constructionInfo.mass = mass;
        rebuildRigidBody();
    }

    public void getGravity(Vector3f gravity) {
        rBody.getGravity(tempVel);
        Converter.convert(tempVel, gravity);
    }

    /**
     * set the gravity of this PhysicsNode
     * @param gravity the gravity vector to set
     */
    public void setGravity(Vector3f gravity) {
        rBody.setGravity(Converter.convert(gravity));
    }

    public float getFriction() {
        return rBody.getFriction();
    }

    /**
     * sets the friction of this physics object
     * @param friction the friction of this physics object
     */
    public void setFriction(float friction) {
        constructionInfo.friction = friction;
        rBody.setFriction(friction);
    }

    public void setDamping(float linearDamping, float angularDamping) {
        constructionInfo.linearDamping = linearDamping;
        constructionInfo.angularDamping = angularDamping;
        rBody.setDamping(linearDamping, angularDamping);
    }

    public float getRestitution() {
        return rBody.getRestitution();
    }

    /**
     * the "bouncyness" of the PhysicsNode
     * best performance if restitution=0
     * @param restitution
     */
    public void setRestitution(float restitution) {
        constructionInfo.restitution = restitution;
        rBody.setRestitution(restitution);
    }

    /**
     * get the current angular velocity of this PhysicsNode
     * @return the current linear velocity
     */
    public Vector3f getAngularVelocity() {
        return Converter.convert(rBody.getAngularVelocity(tempVel));
    }

    /**
     * get the current angular velocity of this PhysicsNode
     * @param vec the vector to store the velocity in
     */
    public void getAngularVelocity(Vector3f vec) {
        Converter.convert(rBody.getAngularVelocity(tempVel), vec);
    }

    /**
     * sets the angular velocity of this PhysicsNode
     * @param vec the angular velocity of this PhysicsNode
     */
    public void setAngularVelocity(Vector3f vec) {
        rBody.setAngularVelocity(Converter.convert(vec));
    }

    /**
     * get the current linear velocity of this PhysicsNode
     * @return the current linear velocity
     */
    public Vector3f getLinearVelocity() {
        return Converter.convert(rBody.getLinearVelocity(tempVel));
    }

    /**
     * get the current linear velocity of this PhysicsNode
     * @param vec the vector to store the velocity in
     */
    public void getLinearVelocity(Vector3f vec) {
        Converter.convert(rBody.getLinearVelocity(tempVel), vec);
    }

    /**
     * sets the linear velocity of this PhysicsNode
     * @param vec the linear velocity of this PhysicsNode
     */
    public void setLinearVelocity(Vector3f vec) {
        rBody.setLinearVelocity(Converter.convert(vec));
        rBody.activate();
    }

    /**
     * get the currently applied continuous force
     * @param vec the vector to store the continuous force in
     * @return null if no force is applied
     */
    public Vector3f getContinuousForce(Vector3f vec) {
        if (applyForce) return vec.set(continuousForce); else return null;
    }

    /**
     * get the currently applied continuous force
     * @return null if no force is applied
     */
    public Vector3f getContinuousForce() {
        if (applyForce) return continuousForce; else return null;
    }

    /**
     * get the currently applied continuous force location
     * @return null if no force is applied
     */
    public Vector3f getContinuousForceLocation() {
        if (applyForce) return continuousForceLocation; else return null;
    }

    /**
     * apply a continuous force to this PhysicsNode, the force is updated automatically each
     * tick so you only need to set it once and then set it to false to stop applying
     * the force.
     * @param apply true if the force should be applied each physics tick
     * @param force the vector of the force to apply
     */
    public void applyContinuousForce(boolean apply, Vector3f force) {
        if (force != null) continuousForce.set(force);
        continuousForceLocation.set(0, 0, 0);
        if (!applyForce && apply) pQueue.enqueue(doApplyContinuousForce);
        applyForce = apply;
    }

    /**
     * apply a continuous force to this PhysicsNode, the force is updated automatically each
     * tick so you only need to set it once and then set it to false to stop applying
     * the force.
     * @param apply true if the force should be applied each physics tick
     * @param force the offset of the force
     */
    public void applyContinuousForce(boolean apply, Vector3f force, Vector3f location) {
        if (force != null) continuousForce.set(force);
        if (location != null) continuousForceLocation.set(location);
        if (!applyForce && apply) pQueue.enqueue(doApplyContinuousForce);
        applyForce = apply;
    }

    /**
     * use to enable/disable continuous force
     * @param apply set to false to disable
     */
    public void applyContinuousForce(boolean apply) {
        if (!applyForce && apply) pQueue.enqueue(doApplyContinuousForce);
        applyForce = apply;
    }

    private Callable doApplyContinuousForce = new Callable() {

        public Object call() throws Exception {
            rBody.applyForce(Converter.convert(continuousForce), Converter.convert(continuousForceLocation));
            rBody.activate();
            if (applyForce) {
                PhysicsSpace.getPhysicsSpace().reQueue(doApplyContinuousForce);
            }
            return null;
        }
    };

    /**
     * get the currently applied continuous torque
     * @return null if no torque is applied
     */
    public Vector3f getContinuousTorque() {
        if (applyTorque) return continuousTorque; else return null;
    }

    /**
     * get the currently applied continuous torque
     * @param vec the vector to store the continuous torque in
     * @return null if no torque is applied
     */
    public Vector3f getContinuousTorque(Vector3f vec) {
        if (applyTorque) return vec.set(continuousTorque); else return null;
    }

    /**
     * apply a continuous torque to this PhysicsNode. The torque is updated automatically each
     * tick so you only need to set it once and then set it to false to stop applying
     * the torque.
     * @param apply true if the force should be applied each physics tick
     * @param vec the vector of the force to apply
     */
    public void applyContinuousTorque(boolean apply, Vector3f vec) {
        if (vec != null) continuousTorque.set(vec);
        if (!applyTorque && apply) {
            pQueue.enqueue(doApplyContinuousTorque);
        }
        applyTorque = apply;
    }

    /**
     * use to enable/disable continuous torque
     * @param apply set to false to disable
     */
    public void applyContinuousTorque(boolean apply) {
        if (!applyTorque && apply) {
            pQueue.enqueue(doApplyContinuousTorque);
        }
        applyTorque = apply;
    }

    private Callable doApplyContinuousTorque = new Callable() {

        public Object call() throws Exception {
            rBody.applyTorque(Converter.convert(continuousTorque));
            rBody.activate();
            if (applyTorque) {
                PhysicsSpace.getPhysicsSpace().reQueue(doApplyContinuousTorque);
            }
            return null;
        }
    };

    /**
     * apply a force to the PhysicsNode, only applies force in the next physics tick,
     * use applyContinuousForce to apply continuous force
     * @param force the force
     * @param location the location of the force
     */
    public void applyForce(Vector3f force, Vector3f location) {
        rBody.applyForce(Converter.convert(force), Converter.convert(location));
        rBody.activate();
    }

    /**
     * apply a force to the PhysicsNode, only applies force in the next physics tick,
     * use applyContinuousForce to apply continuous force
     * @param force the force
     */
    public void applyCentralForce(Vector3f force) {
        rBody.applyCentralForce(Converter.convert(force));
        rBody.activate();
    }

    /**
     * apply a torque to the PhysicsNode, only applies force in the next physics tick,
     * use applyContinuousTorque to apply continuous torque
     * @param torque the torque
     */
    public void applyTorque(Vector3f torque) {
        rBody.applyTorque(Converter.convert(torque));
        rBody.activate();
    }

    /**
     * apply an impulse to the PhysicsNode
     * @param vec
     * @param vec2
     */
    public void applyImpulse(Vector3f vec, Vector3f vec2) {
        rBody.applyImpulse(Converter.convert(vec), Converter.convert(vec2));
        rBody.activate();
    }

    /**
     * apply a toque impulse to the PhysicsNode
     * @param vec
     */
    public void applyTorqueImpulse(Vector3f vec) {
        rBody.applyTorqueImpulse(Converter.convert(vec));
        rBody.activate();
    }

    public void clearForces() {
        rBody.clearForces();
    }

    /**
     * creates a collisionShape from the BoundingVolume of this node,
     * if no BoundingVolume of the give type exists yet, it will be created.
     * Otherwise the current BoundingVolume will be used.
     * @param type
     */
    public void createCollisionShape(int type) {
        switch(type) {
            case CollisionShape.ShapeTypes.BOX:
                collisionShape = new BoxCollisionShape(this);
                break;
            case CollisionShape.ShapeTypes.SPHERE:
                collisionShape = new SphereCollisionShape(this);
                break;
            case CollisionShape.ShapeTypes.CAPSULE:
                collisionShape = new CapsuleCollisionShape(this);
                break;
            case CollisionShape.ShapeTypes.CYLINDER:
                collisionShape = new CylinderCollisionShape(this);
                break;
            case CollisionShape.ShapeTypes.MESH:
                collisionShape = new MeshCollisionShape(this);
                break;
            case CollisionShape.ShapeTypes.GIMPACT:
                collisionShape = new GImpactCollisionShape(this);
                break;
        }
        rebuildRigidBody();
    }

    /**
     * creates a collisionShape from the current BoundingVolume of this node.
     * If no BoundingVolume of a proper type (box, sphere, cyliner, capsule) exists,
     * a sphere will be created.
     */
    public void createCollisionShape() {
        BoundingVolume bounds = getWorldBound();
        if (bounds instanceof BoundingBox) {
            collisionShape = new BoxCollisionShape(this);
        } else if (bounds instanceof BoundingSphere) {
            collisionShape = new SphereCollisionShape(this);
        } else if (bounds instanceof BoundingCapsule) {
            collisionShape = new CapsuleCollisionShape(this);
        } else {
            createCollisionShape(CollisionShape.ShapeTypes.SPHERE);
        }
        constructionInfo.collisionShape = collisionShape.getCShape();
        rebuildRigidBody();
    }

    /**
     * @return the CollisionShape of this PhysicsNode, to be able to reuse it with
     * other physics nodes (increases performance)
     */
    public CollisionShape getCollisionShape() {
        return collisionShape;
    }

    /**
     * sets a CollisionShape to be used for this PhysicsNode for reusing CollisionShapes
     * @param collisionShape the CollisionShape to set
     */
    public void setCollisionShape(CollisionShape collisionShape) {
        this.collisionShape = collisionShape;
        constructionInfo.collisionShape = collisionShape.getCShape();
        rebuildRigidBody();
    }

    /**
     * reactivates this PhysicsNode when it has been deactivated because it was not moving
     */
    public void activate() {
        rBody.activate();
    }

    public boolean isActive() {
        return rBody.isActive();
    }

    /**
     * sets the sleeping thresholds, these define when the object gets deactivated
     * to save ressources. Low values keep the object active when it barely moves
     * @param linear the linear sleeping threshold
     * @param angular the angular sleeping threshold
     */
    public void setSleepingThresholds(float linear, float angular) {
        rBody.setSleepingThresholds(linear, angular);
    }

    /**
     * used internally
     */
    public RigidBody getRigidBody() {
        return rBody;
    }

    /**
     * destroys this PhysicsNode and removes it from memory
     */
    public void destroy() {
        rBody.destroy();
    }

    @Override
    public void write(JMEExporter e) throws IOException {
        throw (new UnsupportedOperationException("Not implemented yet."));
    }

    @Override
    public void read(JMEImporter e) throws IOException {
        throw (new UnsupportedOperationException("Not implemented yet."));
    }
}
