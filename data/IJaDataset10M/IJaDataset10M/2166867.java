package common.body;

import org.jbox2d.dynamics.World;
import org.lwjgl.util.vector.Vector2f;

public final class NullBody implements IBody {

    @Override
    public void addToWorld(final World world) {
    }

    @Override
    public void removeFromWorld(final World world) {
    }

    @Override
    public Vector2f getVelocity() {
        return null;
    }

    @Override
    public IBodyCollisionCallback getCollisionCallback() {
        return null;
    }

    @Override
    public void setCollisionCallback(final IBodyCollisionCallback collisionCallback) {
    }

    @Override
    public float getMass() {
        return 0;
    }

    @Override
    public void setMass(final float mass) {
    }

    @Override
    public Vector2f getPosition() {
        return null;
    }

    @Override
    public void setPosition(final Vector2f position) {
    }

    @Override
    public float getAngle() {
        return 0;
    }

    @Override
    public void setAngle(final float angle) {
    }

    @Override
    public Vector2f getAcceleration() {
        return null;
    }

    @Override
    public float getAngularAcceleration() {
        return 0;
    }

    @Override
    public float getAngularVelocity() {
        return 0;
    }

    @Override
    public float getTorque() {
        return 0;
    }

    @Override
    public Vector2f getVelocityAtPoint(final Vector2f point) {
        return null;
    }

    @Override
    public void clearVelocity() {
    }

    @Override
    public void clearAngularVelocity() {
    }

    @Override
    public void applyForce(final Vector2f force) {
    }

    @Override
    public void applyTorque(final float torque) {
    }

    @Override
    public void applyVelocityChange(final Vector2f velocityChange) {
    }

    @Override
    public void applyAngularVelocityChange(final float velocityChange) {
    }
}
