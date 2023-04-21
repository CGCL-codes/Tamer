package javaclient3.structures.bumper;

import javaclient3.structures.*;

/**
 * Data AND Request/reply: bumper geometry
 * To query the geometry of a bumper array, send a null
 * PLAYER_BUMPER_GET_GEOM request.  The response will be in this form.  This
 * message may also be sent as data with the subtype PLAYER_BUMPER_DATA_GEOM
 * (e.g., from a robot whose bumper can move with respect to its body)
 *
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerBumperGeom implements PlayerConstants {

    private PlayerBumperDefine[] bumper_def;

    /**
     * @return  The number of valid bumper definitions.
     */
    public synchronized int getBumper_def_count() {
        return (this.bumper_def == null) ? 0 : this.bumper_def.length;
    }

    /**
     * @return  geometry of each bumper.
     */
    public synchronized PlayerBumperDefine[] getBumper_def() {
        return this.bumper_def;
    }

    /**
     * @param newBumper_def  geometry of each bumper.
     */
    public synchronized void setBumper_def(PlayerBumperDefine[] newBumper_def) {
        this.bumper_def = newBumper_def;
    }
}
