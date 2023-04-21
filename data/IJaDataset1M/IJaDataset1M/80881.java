package hu.arguscab.physic;

/**
 *
 * @author sharp
 */
public class PhysicProperties {

    private final float rocketPower;

    private final float mass;

    private final float maxTurningSpeed;

    private final float turningSpeed;

    private final float maxSpeed;

    PhysicProperties() {
        rocketPower = mass = maxTurningSpeed = turningSpeed = maxSpeed = 0;
    }

    public PhysicProperties(float rocketPower, float mass, float turningAcceleration, float maxTurningSpeed, float maxSpeed) {
        this.rocketPower = rocketPower;
        this.mass = mass;
        this.turningSpeed = turningAcceleration;
        this.maxTurningSpeed = maxTurningSpeed;
        this.maxSpeed = maxSpeed;
    }

    public float getMass() {
        return mass;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getMaxTurningSpeed() {
        return maxTurningSpeed;
    }

    public float getTurningSpeed() {
        return turningSpeed;
    }

    public float getRocketPower() {
        return rocketPower;
    }
}
