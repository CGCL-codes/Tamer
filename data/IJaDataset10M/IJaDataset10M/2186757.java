package javaclient3.structures.position1d;

import javaclient3.structures.*;

/**
 * Request/reply: Query geometry.
 * To request robot geometry, send a null
 * PLAYER_POSITION1D_GET_GEOM.
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerPosition1dGeom implements PlayerConstants {

    private PlayerPose3d pose;

    private PlayerBbox3d size;

    /**
     * @return  Pose of the robot base, in the robot cs [m, m, rad].
     */
    public synchronized PlayerPose3d getPose() {
        return this.pose;
    }

    /**
     * @param newPose  Pose of the robot base, in the robot cs [m, m, rad].
     */
    public synchronized void setPose(PlayerPose3d newPose) {
        this.pose = newPose;
    }

    /**
     * @return  Dimensions of the base [m, m].
     */
    public synchronized PlayerBbox3d getSize() {
        return this.size;
    }

    /**
     * @param newSize  Dimensions of the base [m, m].
     */
    public synchronized void setSize(PlayerBbox3d newSize) {
        this.size = newSize;
    }
}
