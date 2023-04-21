package checkers3d.presentation;

import java.awt.Point;
import java.util.LinkedList;

/**
 * Provides common functionality required by GUI components. Also includes
 * constants for key codes.
 *
 * @author Ruben Acuna
 */
public class UtilGUI {

    public static final int KEY_BACKSPACE = 8;

    public static final int KEY_RETURN = 10;

    public static final int KEY_ESCAPE = 27;

    public static final int KEY_SPACE = 32;

    /**
     * Computes the location of all components in a list. Computes are arranged
     * in a centered column.
     *
     * @param size The size of the window.
     * @param components        List of components whose position should be calculated.
     */
    public static void computeLayoutCentered(Point size, int axis, LinkedList<IDrawable> components) {
        int totalHeight = 0;
        int bufferY;
        int y;
        for (IDrawable component : components) totalHeight += component.getRenderResource().getHeight();
        bufferY = (size.y - totalHeight) / (components.size() + 1);
        y = bufferY;
        for (IDrawable component : components) {
            int x = axis - component.getRenderResource().getWidth() / 2;
            component.setDrawPosition(new Point(x, y));
            y = y + bufferY + component.getRenderResource().getHeight();
        }
    }

    /**
     * Computes the location of all components in a list. Computes are arranged
     * in a spanning row.
     *
     * @param size The size of the window.
     * @param axis The Y axis value on along which to center.
     * @param components        List of components whose position should be calculated.
     */
    public static void computeLayoutSpanning(Point size, int axis, LinkedList<IDrawable> components) {
        int totalWidth = 0;
        int bufferX;
        int x;
        for (IDrawable component : components) totalWidth += component.getRenderResource().getWidth();
        bufferX = (size.x - totalWidth) / (components.size() + 1);
        x = bufferX;
        for (IDrawable component : components) {
            int y = axis - component.getRenderResource().getHeight() / 2;
            component.setDrawPosition(new Point(x, y));
            x = x + bufferX + component.getRenderResource().getWidth();
        }
    }
}
