package be.appcraft.cmfc.domain;

public interface MovementState {

    /**
	 * Initiates movement.
	 * @param acceleration acceleration of the object in kilometers per second
	 * @param time time in seconds to move
	 * @param distanceRemaining distance left to objective in kilometers
	 * @return the distance in kilometers traveled during the given amount of time
	 */
    long move(double acceleration, long time, long distanceRemaining);

    double getVelocity();
}
