package org.magiclight.common;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Class ta simplify screen shots.
 *
 * @author Mikael Aronsson
 */
public class Screenshot {

    private static final int imageType = BufferedImage.TYPE_INT_ARGB;

    /**
	 * Takes a screen shot of the entire desktop.
	 * <p>
	 * Any thread may call this method.
	 * <p>
	 * @return a BufferedImage representing the entire screen
	 * @throws AWTException if the platform configuration does not allow low-level input control. This exception is always thrown when GraphicsEnvironment.isHeadless() returns true
	 * @throws SecurityException if createRobot permission is not granted
	 */
    public static BufferedImage take() throws AWTException, SecurityException {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle region = new Rectangle(0, 0, d.width, d.height);
        return take(region);
    }

    /**
	 * Takes a screen shot of the specified region of the desktop.
	 * <p>
	 * Any thread may call this method.
	 * <p>
	 * @param region the Rectangle within the screen that will be captured
	 * @return a BufferedImage representing the specified region within the screen
	 * @throws IllegalArgumentException if region == null; region's width and height are not greater than zero
	 * @throws AWTException if the platform configuration does not allow low-level input control. This exception is always thrown when GraphicsEnvironment.isHeadless() returns true
	 * @throws SecurityException if createRobot permission is not granted
	 */
    public static BufferedImage take(Rectangle region) throws IllegalArgumentException, AWTException, SecurityException {
        return new Robot().createScreenCapture(region);
    }

    /**
	 * Takes a screen shot of that part of the desktop whose area is where component lies.
	 * Any other gui elements in this area, including ones which may lie on top of component,
	 * will be included, since the result always reflects the current desktop view.
	 * <p>
	 * Only {@link EventQueue}'s {@link EventQueue#isDispatchThread dispatch thread} may call this method.
	 * <p>
	 * @param component AWT Component to take a screen shot of
	 * @return a BufferedImage representing component
	 * @throws IllegalArgumentException if component == null; component's width and height are not greater than zero
	 * @throws IllegalStateException if calling thread is not EventQueue's dispatch thread
	 * @throws AWTException if the platform configuration does not allow low-level input control. This exception is always thrown when GraphicsEnvironment.isHeadless() returns true
	 * @throws SecurityException if createRobot permission is not granted
	 */
    public static BufferedImage take(Component component) throws IllegalArgumentException, IllegalStateException, AWTException, SecurityException {
        if (component == null) {
            throw new IllegalArgumentException("component == null");
        }
        if (!EventQueue.isDispatchThread()) {
            throw new IllegalStateException("calling thread (" + Thread.currentThread().toString() + ") is not EventQueue's dispatch thread");
        }
        Rectangle region = component.getBounds();
        region.x = 0;
        region.y = 0;
        return take(component, region);
    }

    /**
	 * Takes a screen shot of that part of the desktop whose area is the region relative to where component lies.
	 * Any other gui elements in this area, including ones which may lie on top of component,
	 * will be included, since the result always reflects the current desktop view.
	 * <p>
	 * Only {@link EventQueue}'s {@link EventQueue#isDispatchThread dispatch thread} may call this method.
	 * <p>
	 * @param component AWT Component to take a screen shot of
	 * @param region the Rectangle <i>relative to</i> component that will be captured
	 * @return a BufferedImage representing component
	 * @throws IllegalArgumentException if component == null; component's width and height are not greater than zero; region == null
	 * @throws IllegalStateException if calling thread is not EventQueue's dispatch thread
	 * @throws AWTException if the platform configuration does not allow low-level input control. This exception is always thrown when GraphicsEnvironment.isHeadless() returns true
	 * @throws SecurityException if createRobot permission is not granted
	 */
    public static BufferedImage take(Component component, Rectangle region) throws IllegalArgumentException, IllegalStateException, AWTException, SecurityException {
        if (component == null) {
            throw new IllegalArgumentException("component == null");
        }
        if (region == null) {
            throw new IllegalArgumentException("region == null");
        }
        if (!EventQueue.isDispatchThread()) {
            throw new IllegalStateException("calling thread (" + Thread.currentThread().toString() + ") is not EventQueue's dispatch thread");
        }
        Point p = new Point(0, 0);
        SwingUtilities.convertPointToScreen(p, component);
        region.x += p.x;
        region.y += p.y;
        return take(region);
    }

    /**
	 * Takes a screen shot of <i>just</i> jcomponent
	 * (no other gui elements will be present in the result).
	 * <p>
	 * Only {@link EventQueue}'s {@link EventQueue#isDispatchThread dispatch thread} may call this method.
	 * <p>
	 * @param jcomponent Swing JComponent to take a screen shot of
	 * @return a BufferedImage representing jcomponent
	 * @throws IllegalArgumentException if jcomponent == null
	 * @throws IllegalStateException if calling thread is not EventQueue's dispatch thread
	 */
    public static BufferedImage take(JComponent jcomponent) throws IllegalArgumentException, IllegalStateException {
        if (jcomponent == null) {
            throw new IllegalArgumentException("jcomponent == null");
        }
        if (!EventQueue.isDispatchThread()) {
            throw new IllegalStateException("calling thread (" + Thread.currentThread().toString() + ") is not EventQueue's dispatch thread");
        }
        Dimension d = jcomponent.getSize();
        Rectangle region = new Rectangle(0, 0, d.width, d.height);
        return take(jcomponent, region);
    }

    /**
	 * Takes a screen shot of <i>just</i> the specified region of jcomponent
	 * (no other gui elements will be present in the result).
	 * <p>
	 * Only {@link EventQueue}'s {@link EventQueue#isDispatchThread dispatch thread} may call this method.
	 * <p>
	 * @param jcomponent Swing JComponent to take a screen shot of
	 * @param region the Rectangle <i>relative to</i> jcomponent that will be captured
	 * @return a BufferedImage representing the region within jcomponent
	 * @throws IllegalArgumentException if jcomponent == null; region == null
	 * @throws IllegalStateException if calling thread is not EventQueue's dispatch thread
	 */
    public static BufferedImage take(JComponent jcomponent, Rectangle region) throws IllegalArgumentException, IllegalStateException {
        if (jcomponent == null) {
            throw new IllegalArgumentException("jcomponent == null");
        }
        if (region == null) {
            throw new IllegalArgumentException("region == null");
        }
        if (!EventQueue.isDispatchThread()) {
            throw new IllegalStateException("calling thread (" + Thread.currentThread().toString() + ") is not EventQueue's dispatch thread");
        }
        boolean opaquenessOriginal = jcomponent.isOpaque();
        Graphics2D g2d = null;
        try {
            jcomponent.setOpaque(true);
            BufferedImage image = new BufferedImage(region.width, region.height, imageType);
            g2d = image.createGraphics();
            g2d.translate(-region.x, -region.y);
            g2d.setClip(region);
            jcomponent.paint(g2d);
            return image;
        } finally {
            jcomponent.setOpaque(opaquenessOriginal);
            if (g2d != null) {
                g2d.dispose();
            }
        }
    }
}
