package JavaTron;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * A class that represents the state that the AudioTron is currently
 * in, e.g. "Playing", "Paused" etc.
 * <p>
 * It is implemented loosely as an enum, there is one and only one
 * instance of an ATState object for each state.  The constructor
 * is private so that it is not possible to create arbitrary instances
 * of ATState.  
 * <p>
 * Because there are only singletons of ATState, they can be compared
 * using a direct comparison for equality (e.g. "==").
 *
 * @author Taylor Gautier
 * @version $Revision: 1.1 $ 
 */
public class ATState {

    private static Hashtable states = new Hashtable();

    public static final ATState UNKNOWN = newState("Unknown");

    public static final ATState STOPPED = newState("Stopped");

    public static final ATState OPENING = newState("Opening");

    public static final ATState CLOSING = newState("Closing");

    public static final ATState CLOSED = newState("Closed");

    public static final ATState BUFFERING = newState("Buffering");

    public static final ATState BUFFERED = newState("Buffered");

    public static final ATState PLAYING = newState("Playing");

    public static final ATState PAUSED = newState("Paused");

    public static final ATState STOPPING = newState("Stopping");

    public static final ATState ERROR = newState("Error");

    private static int count = 0;

    private String state;

    private int value;

    private static Image playingImage, pausedImage, stoppedImage;

    protected void setStateString(String string) {
        state = "Error: " + string;
    }

    static {
        Graphics g;
        BufferedImage img;
        playingImage = img = getNewImage();
        g = img.getGraphics();
        g.setColor(Color.green);
        Polygon p = new Polygon();
        p.addPoint((int) (img.getWidth() * .20), (int) (img.getHeight() * .10));
        p.addPoint((int) (img.getWidth() * .20), (int) (img.getHeight() * .90));
        p.addPoint((int) (img.getWidth() * .82), (int) (img.getHeight() * .50));
        g.fillPolygon(p);
        stoppedImage = img = getNewImage();
        g = img.getGraphics();
        g.setColor(Color.red);
        g.fillOval((int) (img.getWidth() * .20), (int) (img.getHeight() * .20), (int) (img.getWidth() * .60), (int) (img.getHeight() * .60));
        pausedImage = img = getNewImage();
        g = img.getGraphics();
        g.setColor(Color.green);
        g.fillRect((int) (img.getWidth() * .30), (int) (img.getHeight() * .20), (int) (img.getWidth() * .15), (int) (img.getHeight() * .60));
        g.fillRect((int) (img.getWidth() * .55), (int) (img.getHeight() * .20), (int) (img.getWidth() * .15), (int) (img.getHeight() * .60));
    }

    private static BufferedImage getNewImage() {
        BufferedImage image = new BufferedImage(48, 36, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = image.getGraphics();
        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        }
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, image.getHeight(), image.getWidth());
        return image;
    }

    /**
   * Given a string, returns an instance of ATSTate that corresponds
   * to the string.  This string is expected to have been obtained
   * directly from the AudioTron.
   *
   * @param state the string that represents the state
   *
   * @return an ATState Object that represents the state
   */
    public static ATState parse(String state) {
        ATState s = null;
        state = state.trim();
        int index = state.indexOf(' ');
        if (index >= 0) {
            state = state.substring(0, index);
        }
        s = (ATState) states.get(state);
        if (s == null) {
            if (state.equalsIgnoreCase("inactive")) {
                s = STOPPED;
            } else {
                s = UNKNOWN;
            }
        }
        return s;
    }

    @Override
    public String toString() {
        return state;
    }

    /**
   * Returns a unique value for this instance.  Each instance
   * of ATState has a unique value that can be used for comparison
   * purposes.
   */
    public int value() {
        return value;
    }

    /**
   * A convenience function to determine if this state represents
   * a state that is "playing" or one that is "stopped".
   *
   * @return true if the state represents a "playing" state or false
   *         otherwise
   */
    public boolean isPlaying() {
        return (this == ATState.PAUSED || this == ATState.PLAYING || this == ATState.BUFFERING || this == ATState.BUFFERED || this == ATState.OPENING);
    }

    /**
   * Returns an image that represents this state
   */
    public Image getImage() {
        if (isPlaying()) {
            if (this == ATState.PAUSED) {
                return pausedImage;
            }
            return playingImage;
        }
        return stoppedImage;
    }

    private static ATState newState(String state) {
        ATState s = new ATState(state, count++);
        states.put(state, s);
        return s;
    }

    private ATState(String state_, int value_) {
        state = state_;
        value = value_;
    }
}
