package net.infonode.gui.shaped;

import java.awt.*;
import net.infonode.gui.*;
import net.infonode.gui.shaped.panel.*;
import net.infonode.util.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.5 $
 */
public class ShapedUtil {

    private ShapedUtil() {
    }

    public static Direction getDirection(Component c) {
        return c instanceof ShapedPanel ? ((ShapedPanel) c).getDirection() : Direction.RIGHT;
    }

    public static Insets transformInsets(Component c, Insets insets) {
        return InsetsUtil.rotate(getDirection(c), flipInsets(c, insets));
    }

    public static Insets flipInsets(Component c, Insets i) {
        if (c instanceof ShapedPanel) {
            if (((ShapedPanel) c).isHorizontalFlip()) i = InsetsUtil.flipHorizontal(i);
            if (((ShapedPanel) c).isVerticalFlip()) i = InsetsUtil.flipVertical(i);
        }
        return i;
    }

    public static void rotateCW(Polygon polygon, int height) {
        for (int i = 0; i < polygon.npoints; i++) {
            int tmp = polygon.ypoints[i];
            polygon.ypoints[i] = polygon.xpoints[i];
            polygon.xpoints[i] = height - 1 - tmp;
        }
    }

    public static void rotate(Polygon polygon, Direction d, int width, int height) {
        if (d == Direction.UP) {
            rotateCW(polygon, height);
            rotateCW(polygon, width);
            rotateCW(polygon, height);
        } else if (d == Direction.LEFT) {
            rotateCW(polygon, height);
            rotateCW(polygon, width);
        } else if (d == Direction.DOWN) {
            rotateCW(polygon, height);
        }
    }

    public static int getWidth(Component c, int width, int height) {
        return getDirection(c).isHorizontal() ? width : height;
    }

    public static int getHeight(Component c, int width, int height) {
        return getDirection(c).isHorizontal() ? height : width;
    }
}
