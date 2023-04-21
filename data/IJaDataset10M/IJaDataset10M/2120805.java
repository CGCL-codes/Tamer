package javaclient3.structures.sound;

import javaclient3.structures.*;

/**
 * Command: play clip (PLAYER_SOUND_CMD_IDX)
 * The sound interface accepts an index of a pre-recorded sound file
 * to play.
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerSoundCmd implements PlayerConstants {

    private int index;

    /**
     * @return  Index of sound to be played.
     */
    public synchronized int getIndex() {
        return this.index;
    }

    /**
     * @param newIndex  Index of sound to be played.
     */
    public synchronized void setIndex(int newIndex) {
        this.index = newIndex;
    }
}
