package javaclient3.structures.pointcloud3d;

import javaclient3.structures.*;

/**
 * Data: Get cloud (PLAYER_POINTCLOUD3D_DATA_STATE).
 * <br>
 * The basic 3dcloudpoint data packet.
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerPointCloud3DData implements PlayerConstants {

    PlayerPointCloud3DElement[] points;

    /**
     * @return Number of 3D points in the cloud.
     */
    public synchronized int getPoints_count() {
        return (this.points == null) ? 0 : this.points.length;
    }

    /**
     * @return 3D points in the cloud.
     */
    public synchronized PlayerPointCloud3DElement[] getPoints() {
        return this.points;
    }

    /**
     * @param newPoints New 3D points in the cloud.
     */
    public synchronized void setPoints(PlayerPointCloud3DElement[] newPoints) {
        this.points = newPoints;
    }
}
