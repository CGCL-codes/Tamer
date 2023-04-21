package com.c5corp.c5gps;

import java.util.*;

/**
* A Route is a container class for a list of RoutePoint
* objects. A list of RoutePoints is typically stored in a file; this object
* represents such a file. The file has header data
* that applies to all of the RoutePoints it contains, but for RoutePoints
* this data is added as local fields to individual RoutePoint objects.
* These lists are conceptually "ordered" data. Another important matter to
* note is that all of the C5UTM GpsDataFileType classes and the data classes they
* encapsulate are intended for use with a relational
* database application. There are fields in typical GPS data
* that are ignored by these classes, and often, floating point
* type numbers are rounded to intergers.
* @author  Brett Stalbaum
* @version 2.0
* @since 2.0
* @see RoutePoint
* @see com.c5corp.c5gps.filters.CsvRouteListFilter
*/
public class Route extends GpsPointCollection {

    private int pointer = -1;

    private boolean hasMoreElements = false;

    private Vector<RoutePoint> routePoints;

    private String routeName = null;

    /**
	 * @param routePoints a Vector<RoutePoint> of RoutePoint objects
	 * @param name the name of the Route
	 */
    public Route(Vector<RoutePoint> routePoints, String name) {
        if (routePoints.size() > 0) {
            this.routePoints = routePoints;
            hasMoreElements = true;
            pointer = 0;
            routeName = routePoints.get(0).getWayPointName() + "-" + routePoints.get(routePoints.size() - 1).getWayPointName();
            ;
        }
    }

    /** implements java.util.Enumeration
	 * @see java.util.Enumeration
	 * @return true if has more elements
	 */
    public boolean hasMoreElements() {
        return hasMoreElements;
    }

    /** implements java.util.Enumeration
	 * @see java.util.Enumeration
	 * @return the next RoutePoint
	 */
    public RoutePoint nextElement() throws NoSuchElementException {
        if (!hasMoreElements) {
            throw new NoSuchElementException("The Route has no more elements");
        }
        RoutePoint temp = routePoints.get(pointer);
        pointer++;
        if (pointer == routePoints.size()) {
            hasMoreElements = false;
        }
        return temp;
    }

    /** resets the java.util.Enumeration to the first element */
    public void reset() {
        pointer = 0;
        hasMoreElements = true;
    }

    /** returns the name of this route
	* @return the route name, which is derived from its RoutePoints
	*/
    public String getRouteName() {
        return routeName;
    }

    /** returns the number of elements (RoutePoints) in this Route.
	 * @return the number of route points in this object
	 */
    public int size() {
        return routePoints.size();
    }

    /**
	* Compares the another Route to this to see if they are
	* equivalent. The number of RoutePoints must be the same, and each
	* routepoint must contain the same planametric coordinates.
	* @param route the Route to compare
	* @return true if the routes are equal
	*/
    public boolean equals(Route route) {
        if (routePoints.size() != route.size()) return false;
        route.reset();
        for (int i = 0; i < routePoints.size(); i++) {
            RoutePoint myRoutePoint = routePoints.get(i);
            RoutePoint otherRoutePoint = route.nextElement();
            if (!myRoutePoint.equals(otherRoutePoint)) {
                route.reset();
                return false;
            }
        }
        return true;
    }

    /** Returns a Route object that is a copy of this
	 * @return a Route object that is a copy of this
	 */
    public Route getCopy() {
        Vector<RoutePoint> vec = new Vector<RoutePoint>(100);
        Collections.copy(vec, routePoints);
        return new Route(vec, routeName);
    }

    /**
	* <p>override toString()</p>
	*/
    public String toString() {
        return "Route Name: " + routeName + " contains " + routePoints.size() + " RoutePoints.";
    }
}
