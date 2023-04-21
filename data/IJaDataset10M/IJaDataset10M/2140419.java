package gnu.ojama.ui;

import java.awt.*;
import javax.swing.*;

/**
 * Default sort icon implementation in the framework.
 * @author Markku Vuorenmaa
 */
public class DefaultSortIcon implements Icon {

    /**
     * Ascending sort icon.
     */
    public static final DefaultSortIcon ASCENDING_ICON;

    /**
     * Ascending sort icon.
     */
    public static final DefaultSortIcon DESCENDING_ICON;

    /**
     * Constant for size of the icon in pixels (width, and height).
     */
    private static final int DEFAULT_SIZE = 10;

    /**
     * Constant for ASCENDING direction.
     */
    public static final int ASCENDING = 1;

    /**
     * Constant for DESCENDING direction.
     */
    public static final int DESCENDING = 2;

    /**
     * Icon direction (ASCENDING or DESCENDING)
     */
    private int myDirection;

    /**
     * Icon size (both width and height in pixels).
     */
    private int mySize;

    /**
     * Icon color.
     */
    private Color myColor;

    /**
     * Default constructor is private, use the public icon constants if you
     * wish to get the icon instance.
     */
    private DefaultSortIcon(int direction) {
        super();
        myDirection = direction;
        mySize = DEFAULT_SIZE;
        myColor = Color.black;
    }

    /**
     * Paints the icon.
     * @param c component
     * @param g graphics context for the component
     * @param x x location in the graphics context
     * @param y y location in the graphics context
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(myColor);
        int iconWidth = mySize - ((mySize + 1) % 2);
        int iconHeight = ((mySize + 1) / 2);
        int yPosition = (mySize - iconHeight) / 2;
        int step = 0;
        int row = 0;
        if (myDirection == ASCENDING) {
            step = -1;
            row = y + yPosition + iconHeight;
        } else {
            step = 1;
            row = y + yPosition;
        }
        int round = 0;
        for (int width = iconWidth; width > 0; width = width - 2) {
            row = row + step;
            g.drawLine(x + round, row, x + round + width - 1, row);
            round++;
        }
    }

    /**
     * Returns the icon height
     * @return height is pixels
     */
    public int getIconHeight() {
        return mySize;
    }

    /**
     * Returns the icon width
     * @return width is pixels
     */
    public int getIconWidth() {
        return mySize;
    }

    static {
        ASCENDING_ICON = new DefaultSortIcon(ASCENDING);
        DESCENDING_ICON = new DefaultSortIcon(DESCENDING);
    }
}
