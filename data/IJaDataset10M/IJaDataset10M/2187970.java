package org.galaxy.gpf.ui;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Icon;

/**
 * A factory for creating Icon objects.
 * 
 * @author Cheng Liang
 * @version 1.0.0
 */
public class IconFactory {

    /** Close icon. */
    private static final Icon close = new CloseIcon();

    /** Restore icon. */
    private static final Icon restore = new RestoreIcon();

    /** Minimum icon. */
    private static final Icon min = new MinIcon();

    /**
	 * Creates a new Icon object.
	 * 
	 * @return the icon
	 */
    public static final Icon createCloseIcon() {
        return close;
    }

    /**
	 * Creates a new Icon object.
	 * 
	 * @return the icon
	 */
    public static final Icon createMinIcon() {
        return min;
    }

    /**
	 * Creates a new Icon object.
	 * 
	 * @return the icon
	 */
    public static final Icon createRestoreIcon() {
        return restore;
    }

    /**
	 * Configure graphics with parameters. First convert <code>Graphics</code>
	 * to <code>Graphics2D</code>, then translate the coordinate origin to
	 * (<code>translate, translate</code>), set stroke width to <code>width</code>
	 * and if <code>anti</code> is <code>true</code>, set the graphics anti-aliasing.
	 * 
	 * @param g the graphics to configure
	 * @param translate the translate value
	 * @param width the width of stroke
	 * @param anti whether anti-aliasing or not
	 * 
	 * @return the graphics configured
	 */
    private static Graphics2D configGraphics(Graphics g, int translate, float width, boolean anti) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(translate, translate);
        g2d.setStroke(new BasicStroke(width));
        if (anti) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        return g2d;
    }

    /**
	 * Restore icon.
	 */
    private static final class RestoreIcon implements Icon {

        /** The height. */
        private static final int HEIGHT = 10;

        /** The width. */
        private static final int WIDTH = 10;

        /** The size of cross icon. */
        private int size = 5;

        @Override
        public int getIconHeight() {
            return HEIGHT;
        }

        @Override
        public int getIconWidth() {
            return WIDTH;
        }

        @Override
        public void paintIcon(Component c, Graphics gr, int x, int y) {
            final int PAD = 3;
            Graphics2D g = configGraphics(gr, PAD, 2.0F, true);
            g.drawRect(0, 0, size, size);
        }
    }

    /**
	 * Minimum icon.
	 */
    private static final class MinIcon implements Icon {

        /** The height. */
        private static final int HEIGHT = 10;

        /** The width. */
        private static final int WIDTH = 10;

        /** The size of cross icon. */
        private int size = 5;

        @Override
        public int getIconHeight() {
            return HEIGHT;
        }

        @Override
        public int getIconWidth() {
            return WIDTH;
        }

        @Override
        public void paintIcon(Component c, Graphics gr, int x, int y) {
            final int PAD = 3;
            Graphics2D g = configGraphics(gr, PAD, 2.0F, true);
            g.drawLine(0, size, size, size);
        }
    }

    /**
	 * Close icon.
	 */
    private static final class CloseIcon implements Icon {

        /** The height. */
        private static final int HEIGHT = 10;

        /** The width. */
        private static final int WIDTH = 10;

        /** The size of cross icon. */
        private int size = 5;

        @Override
        public int getIconHeight() {
            return HEIGHT;
        }

        @Override
        public int getIconWidth() {
            return WIDTH;
        }

        @Override
        public void paintIcon(Component c, Graphics gr, int x, int y) {
            final int PAD = 3;
            Graphics2D g = configGraphics(gr, PAD, 2.0F, true);
            g.drawLine(0, 0, size, size);
            g.drawLine(0, size, size, 0);
        }
    }
}
