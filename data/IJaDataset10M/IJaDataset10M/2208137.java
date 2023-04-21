package org.mazix.square;

import org.mazix.item.AbstractItem;

/**
 * This interface defines common method of all squares which can have items.
 * 
 * @author Benjamin Croizet (graffity2199@yahoo.fr)
 * 
 * @since 0.7
 */
public interface WithItem {

    /**
     * Gets the value of item.
     * 
     * @return the value of item, <code>null</code> if no item.
     * @see #setItem(AbstractItem)
     * @since 0.2
     */
    AbstractItem getItem();

    /**
     * Has item ?.
     * 
     * @return <code>true</code> if the square has an item, <code>false</code> otherwise.
     * @see #getItem()
     * @see #setItem(AbstractItem)
     * @since 0.2
     */
    boolean hasItem();

    /**
     * Sets the value of item.
     * 
     * @param value
     *            the new value of item to be set, <code>null</code> if no item.
     * @see #getItem()
     * @since 0.2
     */
    void setItem(AbstractItem value);
}
