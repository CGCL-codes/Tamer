package de.psychomatic.mp3db.utils;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class for grouping Components for "enabled" and "visible"
 * @author Kykal
 */
public class ComponentGroup {

    /**
     * Creates the ComponentGroup
     */
    public ComponentGroup() {
        _comps = new ArrayList();
    }

    /**
     * Sets all components of this group as the given value
     * @param value true, if all elements should be enabled
     */
    public void setEnabled(boolean value) {
        for (Iterator it = _comps.iterator(); it.hasNext(); ((Component) it.next()).setEnabled(value)) ;
    }

    /**
     * Sets all components of this group as the given value
     * @param value true, if all elements should be visible
     */
    public void setVisible(boolean value) {
        for (Iterator it = _comps.iterator(); it.hasNext(); ((Component) it.next()).setVisible(value)) ;
    }

    /**
     * Add a component to the group
     * @param comp Component to add
     */
    public void add(Component comp) {
        _comps.add(comp);
    }

    /**
     * Removes a component from the group
     * @param comp Component to remove
     */
    public void remove(Component comp) {
        _comps.remove(comp);
    }

    /**
     * Lsit of all components
     */
    private List _comps;
}
