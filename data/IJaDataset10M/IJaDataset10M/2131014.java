package org.base.apps.util;

/**
 * Marks an object as containing an arbitrary mutable value.
 * 
 * @author Kevan Simpson
 */
public interface Mutable<V> {

    /**
     * Returns the object's <code>Mutable</code> value.
     * @return the object's <code>Mutable</code> value.
     */
    public V getValue();

    /**
     * Sets the object's <code>Mutable</code> value.
     * @param val The value to set.
     */
    public void setValue(V val);
}
