package org.jsynthlib.utils;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.JFrame;

public class AWTUtils {

    /**
	 * @return rectangle that surrounds all user's monitors
	 */
    public static Rectangle getAllScreenBounds() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        Rectangle allScreenBounds = screens[0].getDefaultConfiguration().getBounds();
        for (int i = 1; i < screens.length; i++) allScreenBounds = union(allScreenBounds, screens[i].getDefaultConfiguration().getBounds());
        return allScreenBounds;
    }

    /**
	 * Ensure frame appears on a visible screen.
	 * 
	 * @param frame
	 */
    public static void moveOnScreen(JFrame frame) {
        Rectangle b = getAllScreenBounds();
        int x = frame.getX();
        int y = frame.getY();
        if (x > b.x + b.width) x = b.x + b.width - frame.getWidth();
        if (y > b.y + b.height) y = b.y + b.height - frame.getHeight();
        if (x < b.x) x = b.x;
        if (y < b.y) y = b.y;
        if (x != frame.getX() || y != frame.getY()) frame.setLocation(x, y);
    }

    /**
	 * Compute the rectangle that covers two other rectangles.
	 * 
	 * @param r1
	 * @param r2
	 * @return
	 */
    public static Rectangle union(Rectangle r1, Rectangle r2) {
        Rectangle ru = new Rectangle();
        ru.x = Math.min(r1.x, r2.x);
        ru.width = Math.max(r1.x + r1.width, r2.x + r2.width) - ru.x;
        ru.y = Math.min(r1.y, r2.y);
        ru.height = Math.max(r1.y + r1.height, r2.y + r2.height) - ru.y;
        return ru;
    }
}
