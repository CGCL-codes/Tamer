package ircam.jmax.toolkit;

import java.awt.*;
import java.util.*;

/**
 * A simple layout to assign proportional space to two components.
 * Create it with a direction (X_AXIS, Y_AXIS) and a ratio.
 * This layout assign a space to the first component
 * equal to ratio*(parent's size)
 * WARNING: this implementation does'nt take care
 * of the parent's insets.
 */
public class ProportionalLayout implements LayoutManager {

    /**
   * Simplest constructor: just specify the VERTICAL or HORIZONTAL direction
   * and the percentage of space of the first component.
   * The second one will receive 1-ratio space  
   */
    public ProportionalLayout(int theType, float theRatio) {
        components = new Component[2];
        itsType = theType;
        itsRatio = theRatio;
    }

    public void addLayoutComponent(String name, Component comp) {
        if (componentIndex == 2) {
            System.err.println("proportional layout error: trying to add more than two components");
        } else components[componentIndex++] = comp;
    }

    public void removeLayoutComponent(Component comp) {
        for (int i = 0; i < 2; i++) {
            if (components[i] == comp) components[i] = null;
            return;
        }
        System.err.println("proportional layout error: trying to remove a not existing component");
    }

    public Dimension preferredLayoutSize(Container parent) {
        int temp;
        int maxWidth;
        int maxHeight;
        maxHeight = components[0].getPreferredSize().height > components[1].getPreferredSize().height ? components[0].getPreferredSize().height : components[1].getPreferredSize().height;
        maxWidth = components[0].getPreferredSize().width > components[1].getPreferredSize().width ? components[0].getPreferredSize().width : components[1].getPreferredSize().width;
        if (itsType == X_AXIS) {
            temp = components[0].getPreferredSize().width + components[1].getPreferredSize().width;
            return new Dimension((int) (temp * itsRatio), maxHeight);
        } else {
            temp = components[0].getPreferredSize().height + components[1].getPreferredSize().height;
            return new Dimension(maxWidth, (int) (temp * itsRatio));
        }
    }

    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    public void layoutContainer(Container parent) {
        if (components[0] == null) return;
        if (itsType == X_AXIS) {
            components[0].setLocation(0, 0);
            components[0].setSize(new Dimension((int) (parent.getSize().width * itsRatio), parent.getSize().height));
            components[1].setLocation(components[0].getSize().width, 0);
            components[1].setSize(new Dimension((int) (parent.getSize().width * (1 - itsRatio)), parent.getSize().height));
        } else {
            components[0].setLocation(0, 0);
            components[0].setSize(new Dimension(parent.getSize().width, (int) (parent.getSize().height * itsRatio)));
            components[1].setLocation(0, components[0].getSize().height);
            components[1].setSize(new Dimension(parent.getSize().width, (int) (parent.getSize().height * (1 - itsRatio))));
        }
    }

    Component components[];

    float itsRatio;

    int componentIndex = 0;

    int itsType;

    public static final int X_AXIS = 0;

    public static final int Y_AXIS = 1;
}
