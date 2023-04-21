package org.zkoss.idom;

/**
 * Represent a class that allows any type of objects, not just String.
 * It is usually implemented by a class that also implements Item.
 * Currently, only Binary implements it.
 *
 * @author tomyeh
 * @see Item
 * @see Group
 * @see Attributable
 * @see Namespaceable
 */
public interface Binable {

    /**
	 * Gets the value of a item that accepts any type as its value.
	 */
    public Object getValue();

    /**
	 * Sets the value of a item that accepts any type as its value.
	 */
    public void setValue(Object value);
}
