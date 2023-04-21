package net.fortuna.ical4j.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Defines a list of iCalendar components.
 * 
 * @author benf
 */
public class ComponentList implements Serializable {

    private static final long serialVersionUID = 7308557606558767449L;

    private List components;

    /**
	 * Constructor.
	 */
    public ComponentList() {
        components = new ArrayList();
    }

    /**
	 * @see java.util.AbstractCollection#toString()
	 */
    public final String toString() {
        StringBuffer buffer = new StringBuffer();
        for (Iterator i = components.iterator(); i.hasNext(); ) {
            buffer.append(i.next().toString());
        }
        return buffer.toString();
    }

    /**
	 * Returns the first component of specified name.
	 * 
	 * @param aName
	 *            name of component to return
	 * @return a component or null if no matching component found
	 */
    public final Component getComponent(final String aName) {
        for (Iterator i = components.iterator(); i.hasNext(); ) {
            Component c = (Component) i.next();
            if (c.getName().equals(aName)) {
                return c;
            }
        }
        return null;
    }

    /**
	 * Add a component to the list.
	 * 
	 * @param component
	 *            the component to add
	 * @return true
	 * @see List#add(java.lang.Object)
	 */
    public final boolean add(final Component component) {
        return components.add(component);
    }

    /**
	 * @return boolean indicates if the list is empty
	 * @see List#isEmpty()
	 */
    public final boolean isEmpty() {
        return components.isEmpty();
    }

    /**
	 * @return an iterator
	 * @see List#iterator()
	 */
    public final Iterator iterator() {
        return components.iterator();
    }

    /**
	 * Remove a component from the list
	 * 
	 * @param component
	 *            the component to remove
	 * @return true if the list contained the specified component
	 * @see List#remove(java.lang.Object)
	 */
    public final boolean remove(final Component component) {
        return components.remove(component);
    }

    /**
	 * @return the number of components in the list
	 * @see List#size()
	 */
    public final int size() {
        return components.size();
    }

    /**
	 * Provides a list containing all components contained in this component
	 * list.
	 * 
	 * @return a list
	 */
    public final List toList() {
        return new ArrayList(components);
    }
}
