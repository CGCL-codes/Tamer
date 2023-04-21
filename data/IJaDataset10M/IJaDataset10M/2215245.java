package org.dinopolis.gpstool.gpsinput;

import java.awt.Color;
import java.util.List;
import java.util.Vector;

/**
 * An vector based implementation of the
 * {@link org.dinopolis.gpstool.gpsinput.GPSRoute}.
 *
 * @author Christof Dallermassl
 * @version $Revision: 812 $
 */
public class GPSRouteImpl implements GPSRoute {

    protected Vector route_points_ = new Vector();

    protected String identification_ = null;

    protected String comment_ = null;

    protected Color color_;

    protected boolean display_;

    protected boolean minmax_valid_ = false;

    protected double min_latitude_ = 90.0;

    protected double max_latitude_ = -90.0;

    protected double min_longitude_ = 180.0;

    protected double max_longitude_ = -180.0;

    protected double min_altitude_ = Double.MAX_VALUE;

    protected double max_altitude_ = Double.MIN_VALUE;

    /**
   * Default Constructor 
   */
    public GPSRouteImpl() {
    }

    /**
 * Get the identification.
 * @return the identification.
 */
    public String getIdentification() {
        return (identification_);
    }

    /**
 * Set the identification.
 * @param identification The new identification.
 */
    public void setIdentification(String identification) {
        identification_ = identification;
    }

    /**
 * Get the comment.
 * @return the comment or an empty string, if no comment was set.
 */
    public String getComment() {
        return (comment_);
    }

    /**
 * Set the comment.
 * @param comment The comment.
 */
    public void setComment(String comment) {
        comment_ = comment;
    }

    /**
 * Returns true if this track is displayed on the gps device, false otherwise.
 *
 * @return true if this track is displayed on the gps device, false otherwise.
 */
    public boolean isDisplayed() {
        return (display_);
    }

    /**
 * Define whether to display the current track or not.
 *
 * @param display if true, display current track.
 */
    public void setDisplayed(boolean display) {
        display_ = display;
    }

    /**
 * Get the color of the current track.
 */
    public Color getColor() {
        return (color_);
    }

    /**
 * Define the color of the current track. See
 * {@link org.dinopolis.gpstool.gpsinput.garmin.GarminWaypointD108} for colors.
 *
 * @param color Color of the current track.
 */
    public void setColor(Color color) {
        color_ = color;
    }

    /**
 * Get the list of waypoints this route is made of.
 * @return the routepoints.
 */
    public List getWaypoints() {
        return ((List) route_points_.clone());
    }

    /**
 * Set the routepoints.
 * @param routepoints The routepoints.
 */
    public void setWaypoints(List routepoints) {
        route_points_ = new Vector(routepoints);
        minmax_valid_ = false;
    }

    /**
 * Add a route point at the end of the list.
 * @param routepoint The routepoint to add.
 */
    public void addWaypoint(GPSWaypoint routepoint) {
        route_points_.add(routepoint);
        minmax_valid_ = false;
    }

    /**
 * Add a route point at the end of the list.
 * @param position the new position of the routepoint at (0 = add as first point). 
 * @param routepoint The routepoint to add.
 */
    public void addWaypoint(int position, GPSWaypoint routepoint) {
        route_points_.add(position, routepoint);
        minmax_valid_ = false;
    }

    /**
 * Get the routepoint at the given position.
 * @return the routepoint.
 *
 * @throws IndexOutofBoundsException if the index is out of range
 * (index < 0 || index >= size()).
 */
    public GPSWaypoint getWaypoint(int position) throws IndexOutOfBoundsException {
        return ((GPSWaypoint) route_points_.get(position));
    }

    /**
 * Remove the routepoint from the given position.
 *
 * @throws IndexOutofBoundsException if the index is out of range
 * (index < 0 || index >= size()).
 */
    public void removeWaypoint(int position) throws IndexOutOfBoundsException {
        route_points_.remove(position);
        minmax_valid_ = false;
    }

    /**
 * Clears the route (routepoints, identification, comment, etc.).
 */
    public void clear() {
        route_points_.clear();
        identification_ = "";
        comment_ = "";
        min_latitude_ = 90.0;
        max_latitude_ = -90.0;
        min_longitude_ = 180.0;
        max_longitude_ = -180.0;
        min_altitude_ = Double.MAX_VALUE;
        max_altitude_ = Double.MIN_VALUE;
    }

    /**
 * Returns the number of waypoints in this route.
 *
 * @return the number of waypoints in this route.
 */
    public int size() {
        return (route_points_.size());
    }

    /**
 * Returns the minimum latitude (furthest south) covered by this route.
 *
 * @return the minimum latitude covered by this route.
 */
    public double getMinLatitude() {
        if (!minmax_valid_) calculateMinMax();
        return (min_latitude_);
    }

    /**
 * Returns the maximum latitude (furthest north) covered by this route.
 *
 * @return the maximum latitude covered by this route.
 */
    public double getMaxLatitude() {
        if (!minmax_valid_) calculateMinMax();
        return (max_latitude_);
    }

    /**
 * Returns the minimum longitude (furthest west) covered by this route.
 *
 * @return the minimum longitude covered by this route.
 */
    public double getMinLongitude() {
        if (!minmax_valid_) calculateMinMax();
        return (min_longitude_);
    }

    /**
 * Returns the maximum longitude (furthest east) covered by this route.
 *
 * @return the maximum longitude covered by this route.
 */
    public double getMaxLongitude() {
        if (!minmax_valid_) calculateMinMax();
        return (max_longitude_);
    }

    /**
 * Returns the minimum altitude covered by this route.
 *
 * @return the minimum altitude covered by this route.
 */
    public double getMinAltitude() {
        if (!minmax_valid_) calculateMinMax();
        return (min_altitude_);
    }

    /**
 * Returns the maximum altitude covered by this route.
 *
 * @return the maximum altitude covered by this route.
 */
    public double getMaxAltitude() {
        if (!minmax_valid_) calculateMinMax();
        return (max_altitude_);
    }

    /**
 * Finds the min/max lat/long/alt of all registered waypoints.
 */
    protected void calculateMinMax() {
        min_latitude_ = 90.0;
        max_latitude_ = -90.0;
        min_longitude_ = 180.0;
        max_longitude_ = -180.0;
        min_altitude_ = Double.MAX_VALUE;
        max_altitude_ = Double.MIN_VALUE;
        GPSWaypoint waypoint;
        double value;
        for (int index = 0; index < route_points_.size(); index++) {
            waypoint = (GPSWaypoint) route_points_.get(index);
            value = waypoint.getLatitude();
            min_latitude_ = Math.min(value, min_latitude_);
            max_latitude_ = Math.max(value, max_latitude_);
            value = waypoint.getLongitude();
            min_longitude_ = Math.min(value, min_longitude_);
            max_longitude_ = Math.max(value, max_longitude_);
            value = waypoint.getAltitude();
            if (value != Double.NaN) {
                min_altitude_ = Math.min(value, min_altitude_);
                max_altitude_ = Math.max(value, max_altitude_);
            }
        }
        minmax_valid_ = true;
    }

    /**
   * Returns debug info.
   * @see java.lang.Object#toString()
   */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("GPSRouteImpl[identification=").append(identification_).append(",");
        buf.append("route points=").append(route_points_.toString()).append("]");
        return (buf.toString());
    }
}
