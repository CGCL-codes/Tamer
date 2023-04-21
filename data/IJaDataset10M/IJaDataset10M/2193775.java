package co.edu.unal.ungrid.client.util;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;

public class Screen {

    private Screen() {
    }

    public static Dimension getSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static Dimension getActualSize(Window win) {
        assert win != null;
        return getActualSize(win.getGraphicsConfiguration());
    }

    public static Dimension getActualSize(GraphicsConfiguration gc) {
        Insets scrInsets = null;
        try {
            scrInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        } catch (Exception exc) {
            System.out.println("Screen::getActualSize(): " + exc);
        }
        Dimension ss = getSize();
        if (scrInsets != null) {
            ss.height -= scrInsets.bottom;
        }
        return ss;
    }

    public static int getOsToolbarHeight(Window win) {
        assert win != null;
        return getOsToolbarHeight(win.getGraphicsConfiguration());
    }

    public static int getOsToolbarHeight(GraphicsConfiguration gc) {
        Insets scrInsets = null;
        try {
            scrInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        } catch (Exception exc) {
            System.out.println("Screen::getOsToolbarHeight(): " + exc);
        }
        return (scrInsets != null ? scrInsets.bottom : 0);
    }
}
