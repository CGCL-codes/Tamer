package org.wct;

/**
 * This class represents a Flow Layout.
 * A flow Layout puts the components one after another, in the order they are in the container.
 * @author  juliano
 * @version 0.1
 */
public class FlowLayout extends java.lang.Object implements org.wct.LayoutManager {

    /** Creates new FlowLayout */
    public FlowLayout() {
    }

    /**
     * Adds the specified component to this Layout Manager with the specified constraints
 */
    public void addLayoutComponent(Component comp, java.lang.Object constraints) {
    }

    /**
     * Returns the UI class id if this Layout Manager. It is used to lookup a implementation of the layout manager for the
     * current look and feel
 */
    public String getUIClassID() {
        return "FlowLayoutUI";
    }

    /**
     * Removees the specified component from this container
 */
    public void removeLayoutComponent(Component comp) {
    }
}
