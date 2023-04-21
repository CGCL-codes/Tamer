package javaclient3.structures.wsn;

import javaclient3.structures.*;

/**
 * Structure describing the WSN node's data packet.
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerWsnNodeData implements PlayerConstants {

    private float light;

    private float mic;

    private float accel_x;

    private float accel_y;

    private float accel_z;

    private float magn_x;

    private float magn_y;

    private float magn_z;

    private float temperature;

    private float battery;

    /**
     * @return The node's light measurement from a light sensor.
     */
    public synchronized float getLight() {
        return this.light;
    }

    /**
     * @param newLight The node's light measurement from a light sensor.
     */
    public synchronized void setLight(float newLight) {
        this.light = newLight;
    }

    /**
     * @return The node's acoustic measurement from a microphone.
     */
    public synchronized float getMic() {
        return this.mic;
    }

    /**
     * @param newMic The node's acoustic measurement from a microphone.
     */
    public synchronized void setMic(float newMic) {
        this.mic = newMic;
    }

    /**
     * @return The node's acceleration on X-axis from an acceleration sensor.
     */
    public synchronized float getAccel_x() {
        return this.accel_x;
    }

    /**
     * @param newAccel_x The node's acceleration on X-axis from an acceleration
     * sensor.
     */
    public synchronized void setAccel_x(float newAccel_x) {
        this.accel_x = newAccel_x;
    }

    /**
     * @return The node's acceleration on Y-axis from an acceleration sensor.
     */
    public synchronized float getAccel_y() {
        return this.accel_y;
    }

    /**
     * @param newAccel_y The node's acceleration on Y-axis from an acceleration
     * sensor.
     */
    public synchronized void setAccel_y(float newAccel_y) {
        this.accel_y = newAccel_y;
    }

    /**
     * @return The node's acceleration on Z-axis from an acceleration sensor.
     */
    public synchronized float getAccel_z() {
        return this.accel_z;
    }

    /**
     * @param newAccel_z The node's acceleration on Z-axis from an acceleration
     * sensor.
     */
    public synchronized void setAccel_z(float newAccel_z) {
        this.accel_z = newAccel_z;
    }

    /**
     * @return The node's magnetic measurement on X-axis from a magnetometer.
     */
    public synchronized float getMagn_x() {
        return this.magn_x;
    }

    /**
     * @param newMagn_x The node's magnetic measurement on X-axis from a
     * magnetometer.
     */
    public synchronized void setMagn_x(float newMagn_x) {
        this.magn_x = newMagn_x;
    }

    /**
     * @return The node's magnetic measurement on Y-axis from a magnetometer.
     */
    public synchronized float getMagn_y() {
        return this.magn_y;
    }

    /**
     * @param newMagn_y The node's magnetic measurement on Y-axis from a
     * magnetometer.
     */
    public synchronized void setMagn_y(float newMagn_y) {
        this.magn_y = newMagn_y;
    }

    /**
     * @return The node's magnetic measurement on Z-axis from a magnetometer.
     */
    public synchronized float getMagn_z() {
        return this.magn_z;
    }

    /**
     * @param newMagn_z The node's magnetic measurement on X-axis from a
     * magnetometer.
     */
    public synchronized void setMagn_z(float newMagn_z) {
        this.magn_z = newMagn_z;
    }

    /**
     * @return The node's temperature measurement from a temperature sensor.
     */
    public synchronized float getTemperature() {
        return this.temperature;
    }

    /**
     * @param newTemperature The node's temperature measurement from a
     * temperature sensor.
     */
    public synchronized void setTemperature(float newTemperature) {
        this.temperature = newTemperature;
    }

    /**
     * @return The node's remaining battery voltage.
     */
    public synchronized float getBattery() {
        return this.battery;
    }

    /**
     * @param newBattery The node's remaining battery voltage.
     */
    public synchronized void setBattery(float newBattery) {
        this.battery = newBattery;
    }
}
