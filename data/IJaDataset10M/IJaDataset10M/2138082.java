package javaclient3.structures.planner;

import javaclient3.structures.*;

/**
 * Request/reply: Get waypoints
 * To retrieve the list of waypoints, send a null
 * PLAYER_PLANNER_REQ_GET_WAYPOINTS request.
 *
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerPlannerWaypointsReq implements PlayerConstants {

    private PlayerPose2d[] waypoints;

    /**
     * @return  Number of waypoints to follow
     */
    public synchronized int getWaypoints_count() {
        return (waypoints == null) ? 0 : this.waypoints.length;
    }

    /**
     * @return  The waypoints
     */
    public synchronized PlayerPose2d[] getWaypoints() {
        return this.waypoints;
    }

    /**
     * @param newWaypoints  The waypoints
     */
    public synchronized void setWaypoints(PlayerPose2d[] newWaypoints) {
        this.waypoints = newWaypoints;
    }
}
