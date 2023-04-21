package javaclient3.structures.position2d;

import javaclient3.structures.*;

/**
 * Request/reply: Set odometry.
 * To set the robot's odometry to a particular state, send a
 * PLAYER_POSITION2D_REQ_SET_ODOM request.  Null response.
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerPosition2dSetOdomReq implements PlayerConstants {

    private PlayerPose2d pose;

    /**
     * @return  (x, y, yaw) [m, m, rad]
     */
    public synchronized PlayerPose2d getPose() {
        return this.pose;
    }

    /**
     * @param newPose  (x, y, yaw) [m, m, rad]
     */
    public synchronized void setPose(PlayerPose2d newPose) {
        this.pose = newPose;
    }
}
