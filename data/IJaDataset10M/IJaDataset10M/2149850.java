package javaclient3.structures.health;

import javaclient3.structures.*;

/**
 * Structure describing the CPU load.
 * @author Jorge Santos Simon
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerHealthCpu implements PlayerConstants {

    private float idle;

    private float system;

    private float user;

    /**
     * @return The idle CPU load
     */
    public float getIdle() {
        return idle;
    }

    /**
     * @param newIdle The idle CPU load
     */
    public void setIdle(float newIdle) {
        this.idle = newIdle;
    }

    /**
     * @return The system CPU load
     */
    public float getSystem() {
        return system;
    }

    /**
     * @param newSystem The system CPU load
     */
    public void setSystem(float newSystem) {
        this.system = newSystem;
    }

    /**
     * @return The user CPU load
     */
    public float getUser() {
        return user;
    }

    /**
     * @param newUser The user CPU load
     */
    public void setUser(float newUser) {
        this.user = newUser;
    }
}
