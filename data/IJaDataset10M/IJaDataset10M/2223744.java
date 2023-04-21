package com.go.trove.util;

import java.util.*;

/******************************************************************************
 * A set implementation that is backed by any map.
 *
 * @author Brian S O'Neill
 * @version
 * <!--$$Revision: 3 $-->, <!--$$JustDate:-->  9/07/00 <!-- $-->
 */
public class MapBackedSet extends AbstractSet implements java.io.Serializable {

    private static final Object PRESENT = new Object();

    protected final Map mMap;

    /**
     * @param map The map to back this set.
     */
    public MapBackedSet(Map map) {
        mMap = map;
    }

    /**
     * Returns an iterator over the elements in this set. The elements
     * are returned in the order determined by the backing map.
     *
     * @return an Iterator over the elements in this set.
     * @see ConcurrentModificationException
     */
    public Iterator iterator() {
        return mMap.keySet().iterator();
    }

    /**
     * Returns the number of elements in this set (its cardinality).
     *
     * @return the number of elements in this set (its cardinality).
     */
    public int size() {
        return mMap.size();
    }

    /**
     * Returns true if this set contains no elements.
     *
     * @return true if this set contains no elements.
     */
    public boolean isEmpty() {
        return mMap.isEmpty();
    }

    /**
     * Returns true if this set contains the specified element.
     *
     * @return true if this set contains the specified element.
     */
    public boolean contains(Object obj) {
        return mMap.containsKey(obj);
    }

    /**
     * Adds the specified element to this set if it is not already
     * present.
     *
     * @param obj element to be added to this set.
     * @return true if the set did not already contain the specified element.
     */
    public boolean add(Object obj) {
        return mMap.put(obj, PRESENT) == null;
    }

    /**
     * Removes the given element from this set if it is present.
     *
     * @param obj object to be removed from this set, if present.
     * @return true if the set contained the specified element.
     */
    public boolean remove(Object obj) {
        return mMap.remove(obj) == PRESENT;
    }

    /**
     * Removes all of the elements from this set.
     */
    public void clear() {
        mMap.clear();
    }
}
