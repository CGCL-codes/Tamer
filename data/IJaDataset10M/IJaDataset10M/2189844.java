package gui;

import util.Constants;
import util.Options;

/**
 * A single note of a whole song. A note is one of those falling blobs on the screen.
 */
public abstract class AbstractSongNote implements Constants {

    Options options;

    protected int x;

    protected int velocity;

    public double time;

    protected boolean white;

    protected int keyCode;

    /**
     * Constructor.
     * 
     * @param keycode keycode
     * @param vel velocity
     * @param millis millis
     * @param opt config of the app
     */
    public AbstractSongNote(int keycode, int vel, double millis, Options opt) {
        this.options = opt;
        this.keyCode = keycode;
        this.x = getXPos();
        this.velocity = vel;
        this.time = millis;
        this.white = KEYBOARD_KEY_COLOR[keycode];
    }

    /**
     * Calculates the x-position of a note for the given keycode.
     * 
     * @return Returns the X position of the keyCodes
     * 
     * ToDo:
     * This is too specific for an abstract class and should be handled in the
     * implementation.
     */
    protected int getXPos() {
        int xPos = PIANO_X - PIANO_WHITE_WIDTH;
        for (int i = options.midiKeycodeOffset; i < keyCode; i++) {
            if (KEYBOARD_KEY_COLOR[i]) {
                xPos += PIANO_WHITE_WIDTH + 1;
            }
        }
        if (!KEYBOARD_KEY_COLOR[keyCode]) {
            xPos = xPos - (PIANO_WHITE_WIDTH / 2) + (PIANO_BLACK_WIDTH / 2) - 1;
        }
        return xPos;
    }
}
