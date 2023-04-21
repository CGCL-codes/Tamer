package com.tinywebgears.samples.railnetwork;

import java.util.Set;
import com.tinywebgears.samples.railnetwork.data.NoRouteException;
import com.tinywebgears.samples.railnetwork.data.Path;
import com.tinywebgears.samples.railnetwork.data.Route;

/**
 * NetworkPlanner provides all the methods required to assist finding routes in the underlying rail network. This class
 * also works as a Facade on the {@link com.tinywebgears.samples.railnetwork.RailNetwork} and is the only publicly
 * accessible business interface in this package.
 */
public interface NetworkPlanner {

    /**
     * Checks whether this network planner is initialized properly.
     * 
     * @return True of False
     */
    boolean isInitialized();

    /**
     * Checks a given path to see if such a route exists.
     * 
     * @param path
     *            Path
     * @return Matching route
     */
    Route checkPath(Path path) throws NoRouteException;

    /**
     * Checks a given path to see if such a route exists.
     * 
     * @param pathString
     *            Path string in the form of "Station-Station-...-Station"
     * @return Matching route
     */
    Route checkPath(String pathString) throws NoRouteException;

    /**
     * Returns all the possible routes between two stations shorter having number of stops (all the stations in the
     * route counted except the source) in a given range.
     * 
     * @param source
     *            Source station
     * @param destination
     *            Destination station
     * @param minStops
     *            Minimum number of stops
     * @param maxStops
     *            Maximum number of stops
     * @return Set of all matching routes
     */
    Set<Route> getAllRoutes(String source, String destination, Integer minStops, Integer maxStops);

    /**
     * Returns all the possible routes between two stations shorter (and not equal) than a given threshold.
     * 
     * @param source
     *            Source station
     * @param destination
     *            Destination station
     * @param distanceThreshold
     *            Threshold from which a distance is considered to be too far
     * @return Set of all matching routes
     */
    Set<Route> getAllRoutes(String source, String destination, Integer distanceThreshold);

    /**
     * Returns the shortest route between two stations. If more than one routes are candidates for the shortest, one of
     * them will be returned randomly.
     * 
     * @param source
     *            Source station
     * @param destination
     *            Destination station
     * @return Shortest route
     */
    Route getShortestRoute(String source, String destination);
}
