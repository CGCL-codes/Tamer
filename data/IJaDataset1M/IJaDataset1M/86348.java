package wizworld.navigate.route;

import java.io.Serializable;
import java.util.Vector;

/** Route waypoints object
 * @author (c) Stephen Denham 2002
 * @version 0.1
 */
public final class RouteWaypoints implements Serializable {

    private static final long serialVersionUID = 3395806368130941115L;

    /** Maximum number of waypoints in a route */
    public static final int ROUTE_MAX_WAYPOINTS = RouteStream.ROUTE_FILE_ATTRIBUTES - RouteStream.ROUTE_WAYPOINTS;

    /** Minimum number of waypoints in a route */
    public static final int ROUTE_MIN_WAYPOINTS = 2;

    /** The waypoints */
    private Vector<String> routeWaypoints;

    /** Constructor
   */
    public RouteWaypoints() {
        this.routeWaypoints = new Vector<String>(0);
    }

    /** Get waypoint
   * @param	i	Waypoint index in route 0..n
   * @return	Waypoint name
   * @exception	RouteException	if index is greater than max number of route waypoints
   */
    public String getWaypoint(int i) throws RouteException {
        if (i < ROUTE_MAX_WAYPOINTS) {
            return (String) this.routeWaypoints.elementAt(i);
        } else {
            throw new RouteException(Integer.toString(i) + " > " + Integer.toString(ROUTE_MAX_WAYPOINTS));
        }
    }

    /** Set waypoint
   * @param	waypointName Waypoint name
   * @param	i	Waypoint index in route 0..n
   * @exception	RouteException	if index is greater than max number of route waypoints
   */
    public void setWaypoint(String waypointName, int i) throws RouteException {
        if (i < ROUTE_MAX_WAYPOINTS) {
            boolean isDuplicate = false;
            if (this.getWaypointCount() > 0) {
                if (waypointName.compareTo(this.getWaypoint(this.getWaypointCount() - 1)) == 0) {
                    isDuplicate = true;
                }
            }
            if (isDuplicate == false) {
                if (i >= this.getWaypointCount()) {
                    this.routeWaypoints.addElement(waypointName);
                } else {
                    this.routeWaypoints.setElementAt(waypointName, i);
                }
            }
        } else {
            throw new RouteException(waypointName + Integer.toString(i) + " > " + Integer.toString(ROUTE_MAX_WAYPOINTS));
        }
    }

    /** Delete waypoint
   * @param	i	Waypoint index in route 0..n
   * @exception	RouteException	if index is greater than max number of route waypoints
   */
    public void deleteWaypoint(int i) throws RouteException {
        if (i < ROUTE_MAX_WAYPOINTS && i >= 0) {
            this.routeWaypoints.removeElementAt(i);
        } else {
            throw new RouteException(Integer.toString(i) + " > " + Integer.toString(ROUTE_MAX_WAYPOINTS));
        }
    }

    /** Delete waypoint
   * @param	waypointName	Waypoint name
   * @exception	RouteException	if waypoint not found
   */
    public void deleteWaypoint(String waypointName) throws RouteException {
        int i = this.routeWaypoints.indexOf(waypointName);
        if (i >= 0) {
            this.routeWaypoints.removeElementAt(i);
        } else {
            throw new RouteException(waypointName);
        }
    }

    /** Waypoints in route
   * @return	Number of waypoints in route
   */
    public int getWaypointCount() {
        return this.routeWaypoints.size();
    }

    /** Convert to string
   * @return	Comma delimited string of waypoints
   */
    public String toString() {
        String waypoint = (String) (this.routeWaypoints.elementAt(0));
        String waypoints = waypoint;
        for (int i = 1; i < this.routeWaypoints.size(); i++) {
            waypoint = (String) (this.routeWaypoints.elementAt(i));
            waypoints = waypoints + ", " + waypoint;
        }
        return waypoints;
    }
}
