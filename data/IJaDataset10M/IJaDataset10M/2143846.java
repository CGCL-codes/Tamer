package javaclient3.structures.limb;

import javaclient3.structures.*;

/**
 * Request/reply: Power.
 * Turn the power to the limb by sending a PLAYER_LIMB_POWER_REQ request. Be
 * careful when turning power on that the limb is not obstructed from its
 * home position in case it moves to it (common behaviour). Null reponse
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerLimbPowerReq implements PlayerConstants {

    private byte value;

    /**
     * @return  Power setting; 0 for off, 1 for on.
     */
    public synchronized byte getValue() {
        return this.value;
    }

    /**
     * @param newValue  Power setting; 0 for off, 1 for on.
     */
    public synchronized void setValue(byte newValue) {
        this.value = newValue;
    }
}
