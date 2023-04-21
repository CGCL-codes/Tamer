package util;

import java.awt.*;

/**
 * A utility class containing various methods relating to awt components.
 *
 * @author      Jeff Mather
 */
public class ComponentUtil {

    /**
     * Returns the offset x- and y-distances that the given component is
     * from its given ancestor.
     *
     * @param   comp        The component for whose location we are asking.
     * @param   ancestor    The ancestor component considered to be at (0,0)
     *                      for this calculation.
     *
     * @return              The x- and y- offset distances.
     */
    public static Point getLocationRelativeToAncestor(Component comp, Component ancestor) {
        Point location = new Point();
        for (Component c = comp; c != ancestor; c = c.getParent()) {
            Point cLocation = c.getLocation();
            location.translate(cLocation.x, cLocation.y);
        }
        return location;
    }
}
