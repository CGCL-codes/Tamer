package javaclient3.structures.fiducial;

import javaclient3.structures.*;

/**
 * Request/reply: Get/set sensor field of view.
 * The field of view of the fiducial device can be set using the
 * PLAYER_FIDUCIAL_REQ_SET_FOV request (response will be null), and queried
 * using a null PLAYER_FIDUCIAL_REQ_GET_FOV request.
 *
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerFiducialFov implements PlayerConstants {

    private float min_range;

    private float max_range;

    private float view_angle;

    /**
     * @return  The minimum range of the sensor [m]
     */
    public synchronized float getMin_range() {
        return this.min_range;
    }

    /**
     * @param newMin_range  The minimum range of the sensor [m]
     */
    public synchronized void setMin_range(float newMin_range) {
        this.min_range = newMin_range;
    }

    /**
     * @return  The maximum range of the sensor [m]
     */
    public synchronized float getMax_range() {
        return this.max_range;
    }

    /**
     * @param newMax_range  The maximum range of the sensor [m]
     */
    public synchronized void setMax_range(float newMax_range) {
        this.max_range = newMax_range;
    }

    /**
     * @return  The receptive angle of the sensor [rad].
     */
    public synchronized float getView_angle() {
        return this.view_angle;
    }

    /**
     * @param newView_angle  The receptive angle of the sensor [rad].
     */
    public synchronized void setView_angle(float newView_angle) {
        this.view_angle = newView_angle;
    }
}
