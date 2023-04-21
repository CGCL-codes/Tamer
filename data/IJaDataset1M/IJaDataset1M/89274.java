package mobiledevtools.keys;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * Midlet that allows a developer to test the key event behaviour of a
 * J2ME-equipped device. Things worth testing include which keys can
 * produce events at the same time, e.g. can a user move and fire at the
 * same time, whether the full set of events is generated for each key,
 * what the key code is for device-specific keys, and what the mapping
 * between standard and game keys are.
 *
 * @author Andrew E Scott
 */
public class KeysMidlet extends MIDlet {

    /** The text to be printed when the Midlet first starts. */
    private static final String instructions = "Press the typical exit key, e.g. hangup, to leave this Midlet.";

    /** The Display used by the Midlet. */
    Display disThis;

    /** The "exit" Command. */
    Command comExit;

    /** The Canvas object that does the bulk of the work in this Midlet. */
    Canvas canKeys;

    /** Set to true after the initial text has been printed. */
    boolean alertShown;

    /**
   * Constructs an instance of the KeysMidlet class. Initialises the
   * KeyStateCanvas object that will print the keypress inform on the screen.
   */
    public KeysMidlet() {
        disThis = Display.getDisplay(this);
        canKeys = new KeyStateCanvas();
        alertShown = false;
    }

    /**
   * Signals the MIDlet that it has entered the Active state. Ensures that
   * the instructional text is shown once at the beginning.
   */
    protected void startApp() {
        if (alertShown) {
            disThis.setCurrent(canKeys);
        } else {
            disThis.setCurrent(new Alert("Keys", instructions, null, AlertType.INFO), canKeys);
            alertShown = true;
        }
    }

    /**
   * Signals the MIDlet to enter the Paused state.
   */
    protected void pauseApp() {
    }

    /**
   * Signals the MIDlet to terminate and enter the Destroyed state.
   *
   * @param fUnconditional if false, can throw MIDletStateChangeException
   */
    protected void destroyApp(boolean fUnconditional) {
        notifyDestroyed();
    }
}
