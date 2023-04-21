package graphlab.ui.components.utils;

import java.awt.*;

/**
 * @author rouzbeh ebrahimi
 */
public class GFrameLocationProvider {

    static int coefficent = 16, determinator = 14;

    public static Point getLocation() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Point p = new Point((int) d.getWidth() / coefficent, (int) d.getHeight() / coefficent);
        return p;
    }

    public static Dimension getSize() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dim = new Dimension((int) d.getWidth() * determinator / coefficent, (int) d.getHeight() * determinator / coefficent);
        return dim;
    }

    public static Dimension getPopUpSize() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dim = new Dimension((int) d.getWidth() * 8 / coefficent, (int) d.getHeight() * 8 / coefficent);
        return dim;
    }

    public static Point getPopUpLocation() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Point p = new Point((int) d.getWidth() * 3 / coefficent, (int) d.getHeight() * 3 / coefficent);
        return p;
    }
}
