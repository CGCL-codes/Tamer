package au.com.kelpie.fgfp.model;

public class AirportWaypoint extends LocationWaypoint {

    private Airport _airport;

    public AirportWaypoint() {
    }

    public AirportWaypoint(Airport airport) {
        super(airport);
        _airport = airport;
    }

    /**
	 * Return the airport that is the basis of the waypoint.
	 * May be null
	 * @return Airport
	 */
    public Airport getAirport() {
        return _airport;
    }

    /**
	 * Returns the elevation.
	 * @return double
	 */
    public double getElevation() {
        return _airport.getElevation();
    }

    @Override
    public int getType() {
        return TYPE_AIRPORT;
    }

    public void setAirport(Airport airport) {
        _airport = airport;
    }
}
