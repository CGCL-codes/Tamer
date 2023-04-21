package rotorsim.aircraft;

import util.Matrix3f;
import util.Quaternion;
import util.ReadableVector3f;
import util.Vector3f;

public class RSObject {

    private static final RK4Integrator INTEGRATOR = new RK4Integrator();

    protected final Vector3f position;

    protected final Quaternion rotation;

    protected final Vector3f inertia;

    protected final Vector3f linearVelocity;

    protected final Vector3f angularVelocity;

    protected float mass;

    private final Vector3f force;

    private final Vector3f torque;

    public RSObject() {
        position = new Vector3f();
        rotation = new Quaternion();
        inertia = new Vector3f();
        linearVelocity = new Vector3f();
        angularVelocity = new Vector3f();
        force = new Vector3f();
        torque = new Vector3f();
        mass = 1.0f;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getMass() {
        return mass;
    }

    public void setIntertia(ReadableVector3f v) {
        inertia.set(v);
    }

    public void setLinearVelocity(Vector3f v) {
        linearVelocity.set(v);
    }

    public void setAngularVelocity(Vector3f v) {
        angularVelocity.set(v);
    }

    public void setPosition(ReadableVector3f position) {
        this.position.set(position);
    }

    public void setRotation(Quaternion rotation) {
        this.rotation.set(rotation);
    }

    public ReadableVector3f getPosition() {
        return position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public ReadableVector3f getAngularVelocity() {
        return angularVelocity;
    }

    public ReadableVector3f getLinearVelocity() {
        return linearVelocity;
    }

    private static final Vector3f temp = new Vector3f();

    public void applyExternalForce(ReadableVector3f position, ReadableVector3f size) {
        force.add(size);
        if (position != null) {
            position.cross(size, temp);
            applyExternalTorque(temp);
        }
    }

    private static final Vector3f p = new Vector3f();

    private static final Vector3f s = new Vector3f();

    public void applyForce(ReadableVector3f position, ReadableVector3f size) {
        if (position != null) {
            p.set(position);
        }
        s.set(size);
        rotation.rotate(p);
        rotation.rotate(s);
        force.add(s);
        if (position != null) {
            position.cross(size, temp);
            applyTorque(temp);
        }
    }

    public void applyExternalTorque(ReadableVector3f size) {
        torque.add(size);
    }

    public void applyTorque(ReadableVector3f size) {
        s.set(size);
        rotation.rotate(s);
        torque.add(s);
    }

    public void update(float delta) {
        updateLinear(delta);
        updateAngular(delta);
        force.set(0, 0, 0);
        torque.set(0, 0, 0);
    }

    public float getYawRate() {
        float length = angularVelocity.length();
        return angularVelocity.dot(new Vector3f(0, 0, 1)) * length;
    }

    private void updateLinear(float delta) {
        Vector3f accel = new Vector3f();
        accel.set(force);
        accel.add(0, 0, -mass * 9.8f);
        accel.scale(1 / mass);
        linearVelocity.addScaled(accel, delta);
        position.addScaled(linearVelocity, delta);
    }

    private void updateAngular(float delta) {
        Vector3f accel = new Vector3f();
        accel.set(torque.x / inertia.x, torque.y / inertia.y, torque.z / inertia.z);
        angularVelocity.addScaled(accel, delta);
        Quaternion spin = new Quaternion(angularVelocity.x, angularVelocity.y, angularVelocity.z, 0);
        spin.mul(rotation);
        spin.scale(0.5f);
        rotation.addScaled(spin, delta);
        rotation.normalise();
    }

    public String toString() {
        return "Pos = " + position + " Vel = " + linearVelocity;
    }

    private class AccelerationODE implements DiffEquation {

        public float force;

        public void evaluate(ReadableVector3f input, Vector3f output) {
        }
    }
}
