package org.argouml.swingext;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;

/**
 * Lays out components in a single row or column starting from any side and aligning  components
 * to eachother.<br />
 * Components can be set to start draw from, LEFTTORIGHT, TOPTOBOTTOM, RIGHTTOLEFT or BOTTOMTOTOP.<br />
 * Components will line up with eachother by edge or follow a common central line.<br />
 * The gap to leave before the first component and the following gaps between each component can
 * be set to be set.
 *
 * @author Bob Tarling
 */
public class SerialLayout extends LineLayout {

    public static final int LEFTTORIGHT = 10;

    public static final int TOPTOBOTTOM = 10;

    public static final int RIGHTTOLEFT = 11;

    public static final int BOTTOMTOTOP = 11;

    public static final String NORTH = "North";

    public static final String SOUTH = "South";

    public static final String EAST = "East";

    public static final String WEST = "West";

    public static final String NORTHEAST = "NorthEast";

    public static final String NORTHWEST = "NorthWest";

    public static final String SOUTHEAST = "SouthEast";

    public static final String SOUTHWEST = "SouthWest";

    public static final int LEFT = 20;

    public static final int RIGHT = 21;

    public static final int TOP = 20;

    public static final int BOTTOM = 21;

    public static final int CENTER = 22;

    String position = WEST;

    int direction = LEFTTORIGHT;

    int alignment = TOP;

    public SerialLayout() {
        this(Horizontal.getInstance(), WEST, LEFTTORIGHT, TOP);
    }

    public SerialLayout(Orientation orientation) {
        this(orientation, WEST, LEFTTORIGHT, TOP);
    }

    public SerialLayout(Orientation orientation, String position) {
        this(orientation, position, LEFTTORIGHT, TOP);
    }

    public SerialLayout(Orientation orientation, String position, int direction) {
        this(orientation, position, direction, TOP);
    }

    public SerialLayout(Orientation orientation, String position, int direction, int alignment) {
        super(orientation);
        this.position = position;
        this.direction = direction;
        this.alignment = alignment;
    }

    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        Point loc;
        if (direction == LEFTTORIGHT) {
            loc = new Point(insets.left, insets.top);
            int nComps = parent.getComponentCount();
            for (int i = 0; i < nComps; i++) {
                Component comp = parent.getComponent(i);
                if (comp != null && comp.isVisible()) {
                    comp.setSize(comp.getPreferredSize());
                    comp.setLocation(loc);
                    loc = orientation.addToPosition(loc, comp);
                }
            }
        } else {
            int lastUsablePosition = orientation.getLastUsablePosition(parent);
            int firstUsableOffset = orientation.getFirstUsableOffset(parent);
            loc = orientation.newPoint(lastUsablePosition, firstUsableOffset);
            int nComps = parent.getComponentCount();
            for (int i = 0; i < nComps; i++) {
                Component comp = parent.getComponent(i);
                if (comp != null && comp.isVisible()) {
                    if (loc == null) System.out.println("null orientation");
                    loc = orientation.subtractFromPosition(loc, comp);
                    comp.setSize(comp.getPreferredSize());
                    comp.setLocation(loc);
                }
            }
        }
    }
}
