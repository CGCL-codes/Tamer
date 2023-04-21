package gov.sns.tools.apputils;

import java.awt.*;
import java.util.*;

/**
 * EdgeLayout allows the developer a simpler mechanism for layout of components 
 * than the GridBagLayout without sacrificing powerful layout capability.
 * The developer simply defines the behavior of motion for the component 
 * with respect to its parent and how the component grows.  The motion is 
 * specified by struts between the edges of the the component and the 
 * corresponding edges of the parent container.  For example, if you 
 * use a RIGHT strut, the component is bound to its parent's right edge according 
 * to the initial placement of the component.  That is, the gap between the 
 * right edges of the parent and its container is preserved.  If the component is 
 * bound left and right to its parent and the component is constrained to grow 
 * horizontally, the component will maintain the left and right gaps with its 
 * parent container and grow accordingly.
 *
 * @author  tap
 */
public class EdgeLayout implements LayoutManager {

    public static final int NO_STRUTS = 0;

    public static final int LEFT = 1;

    public static final int RIGHT = 2;

    public static final int TOP = 4;

    public static final int BOTTOM = 8;

    public static final int TOP_LEFT = TOP | LEFT;

    public static final int TOP_BOTTOM = TOP | BOTTOM;

    public static final int TOP_RIGHT = TOP | RIGHT;

    public static final int LEFT_BOTTOM = LEFT | BOTTOM;

    public static final int LEFT_RIGHT = LEFT | RIGHT;

    public static final int BOTTOM_RIGHT = BOTTOM | RIGHT;

    public static final int TOP_LEFT_BOTTOM = TOP | LEFT | BOTTOM;

    public static final int TOP_BOTTOM_RIGHT = TOP | BOTTOM | RIGHT;

    public static final int LEFT_BOTTOM_RIGHT = LEFT | BOTTOM | RIGHT;

    public static final int TOP_LEFT_RIGHT = TOP | LEFT | RIGHT;

    public static final int ALL_SIDES = LEFT | RIGHT | TOP | BOTTOM;

    public static final int NO_GROWTH = 0;

    public static final int GROW_HORIZONTAL = 1;

    public static final int GROW_VERTICAL = 2;

    public static final int GROW_BOTH = GROW_HORIZONTAL | GROW_VERTICAL;

    protected Map<Component, EdgeConstraints> constraintTable;

    /** Creates a new instance of SimpleLayout */
    public EdgeLayout() {
        constraintTable = new Hashtable<Component, EdgeConstraints>();
    }

    /** 
     * Add the component to the layout at the specified coordinates with the specified struts and strut and growth behaviors.
     */
    public void setConstraints(Component component, int topStrut, int leftStrut, int bottomStrut, int rightStrut, int strutBehavior, int growBehavior) {
        EdgeConstraints constraint = new EdgeConstraints();
        constraint.growBehavior = growBehavior;
        constraint.strutBehavior = strutBehavior;
        constraint.topStrut = topStrut;
        constraint.leftStrut = leftStrut;
        constraint.bottomStrut = bottomStrut;
        constraint.rightStrut = rightStrut;
        constraintTable.put(component, constraint);
    }

    /** 
     * Add the component to the layout at the specified coordinates with the specified struts and strut and no growth.
     */
    public void setConstraints(Component component, int topStrut, int leftStrut, int bottomStrut, int rightStrut, int strutBehavior) {
        setConstraints(component, topStrut, leftStrut, bottomStrut, rightStrut, strutBehavior, NO_GROWTH);
    }

    /** 
     * Add the component to the layout at the specified coordinates with the specified strut and growth behaviors.
     * The struts are calculated based on the component's preferred size and the specified container size.
     */
    public void setConstraints(Component component, Dimension containerSize, int x, int y, int strutBehavior, int growBehavior) {
        int bottomStrut = containerSize.height - y - component.getPreferredSize().height;
        int rightStrut = containerSize.width - x - component.getPreferredSize().width;
        setConstraints(component, y, x, bottomStrut, rightStrut, strutBehavior, growBehavior);
    }

    /** 
     * Add the component to the layout at the specified coordinates with the specified strut and growth behaviors.
     * The struts are calculated based on the component's preferred size and the specified container size.
     */
    public void setConstraints(Component component, Dimension containerSize, int x, int y, int strutBehavior) {
        setConstraints(component, containerSize, x, y, strutBehavior, NO_GROWTH);
    }

    /** 
     * Add the component to the parent at the specified coordinates with the specified strut and growth behaviors.
     * The struts are calculated based on the component's preferred size and the parent's bounds at the time 
     * this method is invoked.
     */
    public void add(Component component, Container parent, int x, int y, int strutBehavior, int growBehavior) {
        parent.add(component);
        setConstraints(component, parent.getPreferredSize(), x, y, strutBehavior, growBehavior);
    }

    /** 
     * Add the component to the parent at the specified coordinates with the specified strut behavior.
     * The growth behavior is set to NO_GROWTH.
     */
    public void add(Component component, Container parent, int x, int y, int strutBehavior) {
        add(component, parent, x, y, strutBehavior, NO_GROWTH);
    }

    /** Implement LayoutManager interface */
    public void addLayoutComponent(String name, Component component) {
    }

    /** Implement LayoutManager interface */
    public void layoutContainer(Container parent) {
        Component[] components = parent.getComponents();
        int count = components.length;
        for (int index = 0; index < count; index++) {
            Component component = components[index];
            layoutComponent(component, parent);
        }
    }

    /** Layout the component in the parent */
    protected void layoutComponent(Component component, Container parent) {
        EdgeConstraints constraint = constraintTable.get(component);
        int width = component.getPreferredSize().width;
        int height = component.getPreferredSize().height;
        if (constraint == null) {
            component.setSize(width, height);
            return;
        }
        int left_side = constraint.leftStrut;
        int top_side = constraint.topStrut;
        if ((constraint.growBehavior & GROW_HORIZONTAL) != 0) {
            if ((constraint.strutBehavior & LEFT_RIGHT) == LEFT_RIGHT) {
                width = parent.getWidth() - constraint.rightStrut - constraint.leftStrut;
            } else {
                int baseParentWidth = constraint.rightStrut + constraint.leftStrut + width;
                width = (width * parent.getWidth()) / baseParentWidth;
            }
        }
        if ((constraint.growBehavior & GROW_VERTICAL) != 0) {
            if ((constraint.strutBehavior & TOP_BOTTOM) == TOP_BOTTOM) {
                height = parent.getHeight() - constraint.bottomStrut - constraint.topStrut;
            } else {
                int baseParentHeight = constraint.bottomStrut + constraint.topStrut + height;
                height = (height * parent.getHeight()) / baseParentHeight;
            }
        }
        if ((constraint.strutBehavior & RIGHT) != 0) {
            left_side = parent.getWidth() - constraint.rightStrut - width;
        }
        if ((constraint.strutBehavior & BOTTOM) != 0) {
            top_side = parent.getHeight() - constraint.bottomStrut - height;
        }
        component.setSize(width, height);
        component.setLocation(left_side, top_side);
    }

    /** Implement LayoutManager interface */
    public Dimension minimumLayoutSize(Container parent) {
        int minWidth = 0;
        int minHeight = 0;
        Component[] components = parent.getComponents();
        int count = components.length;
        for (int index = 0; index < count; index++) {
            Component component = components[index];
            int componentWidth = component.getMinimumSize().width;
            int componentHeight = component.getMinimumSize().height;
            EdgeConstraints constraint = constraintTable.get(component);
            if (constraint != null) {
                componentWidth += constraint.leftStrut + constraint.rightStrut;
                componentHeight += constraint.topStrut + constraint.bottomStrut;
            }
            minWidth = Math.max(minWidth, componentWidth);
            minHeight = Math.max(minHeight, componentHeight);
        }
        return new Dimension(minWidth, minHeight);
    }

    /** Implement LayoutManager interface */
    public Dimension preferredLayoutSize(Container parent) {
        int prefWidth = 0;
        int prefHeight = 0;
        Component[] components = parent.getComponents();
        int count = components.length;
        for (int index = 0; index < count; index++) {
            Component component = components[index];
            int componentWidth = component.getPreferredSize().width;
            int componentHeight = component.getPreferredSize().height;
            EdgeConstraints constraint = constraintTable.get(component);
            if (constraint != null) {
                componentWidth += constraint.leftStrut + constraint.rightStrut;
                componentHeight += constraint.topStrut + constraint.bottomStrut;
            }
            prefWidth = Math.max(prefWidth, componentWidth);
            prefHeight = Math.max(prefHeight, componentHeight);
        }
        return new Dimension(prefWidth, prefHeight);
    }

    /** Implement LayoutManager interface */
    public void removeLayoutComponent(Component component) {
    }
}
