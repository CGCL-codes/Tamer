package jbotrace.base;

/**
 * <p>Description: DriverCommand holds the commands a driver can
 * give in one timestep.</p>
 */
public class DriverCommand {

    private int gear;

    private double brake;

    private double steeringAngle;

    private double throttle;

    /** Creates a new "empty" DriverCommand */
    public DriverCommand() {
    }

    /** Returns the current acceleration in this command */
    public double getBrake() {
        return brake;
    }

    /** Returns the current wanted steering angle. */
    public double getSteeringAngle() {
        return steeringAngle;
    }

    /** Returns the current acceleration in this command */
    public double getThrottle() {
        return throttle;
    }

    /** Sets the brake */
    public void setBrake(double brake) {
        this.brake = brake;
    }

    /** Sets the wanted(!) angle of the steering. */
    public void setSteeringAngle(double steeringAngle) {
        this.steeringAngle = steeringAngle;
    }

    /** Sets the throttle */
    public void setThrottle(double throttle) {
        this.throttle = throttle;
    }

    /**
	 * @return int
	 */
    public int getGear() {
        return gear;
    }

    /**
	 * Sets the gear.
	 * @param gear The gear to set
	 */
    public void setGear(int gear) {
        this.gear = gear;
    }
}
