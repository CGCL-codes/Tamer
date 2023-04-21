package at.fhj.itm.business;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.primefaces.model.map.MapModel;
import at.fhj.itm.model.Location;
import at.fhj.itm.model.Point;
import at.fhj.itm.model.Trip;
import at.fhj.itm.model.User;
import at.fhj.itm.model.Waypoint;

public interface ServiceTrip {

    /**
	 * <h1>Insert Trip</h1>
	 * <p>
	 * Inserting a Trip
	 * </p>
	 * <p>
	 * ID = -1 -> Insert
	 * </p>
	 * 
	 * @param departure
	 * @param seats
	 * @param fromLocation 
	 * @param toLocation 
	 * @throws ServiceException
	 * 
	 */
    public abstract void insertTrip(Date departure, int seats, Location fromLocation, Location toLocation, Location... stops) throws ServiceException;

    /**
	 * <h1>Delete Trip</h1>
	 * <p>
	 * Deleting a Trip by ID
	 * </p>
	 * 
	 * @params id
	 * @throws RuntimeException
	 * @throws SQLException
	 */
    public abstract void removeTrip(final String idString);

    public abstract List<Trip> selectAllTrips();

    public List<Trip> searchTrip(String from, String to);

    public abstract List<Waypoint> getWaypointsForTrip(Trip trip);

    public abstract List<Waypoint> getWaypointsForUser(User u);

    public abstract List<Point> getPointsForTrip(Trip t);

    public abstract void bookWaypointForTrip(Waypoint wp, Trip trip);

    /**
	 * Calculates the approximate arrival time bases on the departure and time between the
	 * navigation points.
	 * @param t trips for which the arrival time is calculated
	 * @return calculated arrival time
	 */
    public abstract Date getArivalTime(Trip t);

    public MapModel getMapModelFromTrip(Trip trip);

    public abstract List<Trip> allBookedTripsByUser(int userId);

    public abstract List<Trip> searchTrip(String from, String to, String driver);

    public abstract void removeWaypoint(final String idString);
}
