package javaclient3.structures.ranger;

import javaclient3.structures.*;

/**
 * Request/reply: Ranger configuration.
 * Ranger device configuration parameters.
 * PLAYER_RANGER_REQ_GET_CONFIG request obtains the current configuration,
 * while PLAYER_RANGER_REQ_SET_CONFIG (not yet implemented) overwrites it.
 * @author Jorge Santos Simon
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerRangerConf implements PlayerConstants {

    private double min_angle = Double.NaN;

    private double max_angle = Double.NaN;

    private double resolution = Double.NaN;

    private double min_range = Double.NaN;

    private double max_range = Double.NaN;

    private double range_res = Double.NaN;

    private double frequency = Double.NaN;

    /**
     * @return Start angle of scans [rad].
     */
    public double getMin_angle() {
        return this.min_angle;
    }

    /**
     * @param minAngle Start angle of scans [rad].
     */
    public void setMin_angle(double minAngle) {
        this.min_angle = minAngle;
    }

    /**
     * @return End angle of scans [rad].
     */
    public double getMax_angle() {
        return this.max_angle;
    }

    /**
     * @param maxAngle End angle of scans [rad].
     */
    public void setMax_angle(double maxAngle) {
        this.max_angle = maxAngle;
    }

    /**
     * @return Minimum range [m]
     */
    public double getMin_range() {
        return min_range;
    }

    /**
     * @param minRange Minimum range [m]
     */
    public void setMin_range(double minRange) {
        min_range = minRange;
    }

    /**
     * @return Maximum range [m].
     */
    public double getMax_range() {
        return this.max_range;
    }

    /**
     * @param maxRange Maximum range [m].
     */
    public void setMax_range(double maxRange) {
        this.max_range = maxRange;
    }

    /**
     * @return Scanning frequency [Hz].
     */
    public double getFrequency() {
        return this.frequency;
    }

    /**
     * @param frequency Scanning frequency [Hz].
     */
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    /**
     * @return Range resolution [m].
     */
    public double getRange_res() {
        return this.range_res;
    }

    /**
     * @param rangeRes Range resolution [m].
     */
    public void setRange_res(double rangeRes) {
        this.range_res = rangeRes;
    }

    /**
     * @return Scan resolution [rad].
     */
    public double getResolution() {
        return this.resolution;
    }

    /**
     * @param resolution Scan resolution [rad].
     */
    public void setResolution(double resolution) {
        this.resolution = resolution;
    }
}
