package net.sourceforge.xhsi.model;

public interface AircraftEnvironment {

    /**
	 * @return float - wind speed at the aircrafts location in knots
	 */
    public float wind_speed();

    /**
	 * @return float - wind direction at the aircrafts location in degrees
	 */
    public float wind_direction();
}
