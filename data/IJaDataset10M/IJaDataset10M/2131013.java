package no.ntnu.idi.wall.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An imcomplete (abstract) implementation of WallModel,
 * implements support for a List of WallListeners and
 * a method for notifying them of a change.
 * 
 * @author Hallvard Tr�tteberg
 *
 */
public abstract class AbstractWallModel implements WallModel {

    private List<WallListener> listeners = new ArrayList<WallListener>();

    public void addWallListener(WallListener listener) {
        listeners.add(listener);
    }

    public void removeWallListener(WallListener listener) {
        listeners.remove(listener);
    }

    /**
	 * Notifies the set of listeners of changes to a rectangle of leds.
	 * There is no guarantee that the set of notifications is optimal,
	 * e.g. changes to a rectangle may given in smaller pieces,
	 * even individual leds where both width and height is 1.
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @param width the width of the changed area
	 * @param height the height of the changed area
	 * @param value the new value
	 */
    protected void notifyListeners(int x, int y, int width, int height, byte value) {
        for (WallListener listener : listeners) {
            if (width == 1 && height == 1) {
                listener.ledChanged(x, y, value, this);
            }
            listener.ledsChanged(x, y, width, height, this);
        }
    }

    public AbstractWallModel() {
    }

    /**
	 * A default implementation of setLeds, which uses the setLed method for setting each led.
	 */
    public void setLeds(int x0, int y0, int dx, int dy, byte value) {
        for (int x = x0; x < x0 + dx; x++) {
            for (int y = y0; y < y0 + dy; y++) {
                setLed(x, y, value);
            }
        }
        notifyListeners(x0, y0, dx, dy, value);
    }
}
