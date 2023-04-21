package javaclient3.structures.position2d;

import javaclient3.structures.*;

/**
 * Request/reply: Set position PID parameters.
 *
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerPosition2dPositionPidReq implements PlayerConstants {

    private float kp;

    private float ki;

    private float kd;

    /**
     * @return  PID parameters
     */
    public synchronized float getKp() {
        return this.kp;
    }

    /**
     * @param newKp  PID parameters
     */
    public synchronized void setKp(float newKp) {
        this.kp = newKp;
    }

    /**
     * @return  PID parameters
     */
    public synchronized float getKi() {
        return this.ki;
    }

    /**
     * @param newKi  PID parameters
     */
    public synchronized void setKi(float newKi) {
        this.ki = newKi;
    }

    /**
     * @return  PID parameters
     */
    public synchronized float getKd() {
        return this.kd;
    }

    /**
     * @param newKd  PID parameters
     */
    public synchronized void setKd(float newKd) {
        this.kd = newKd;
    }
}
