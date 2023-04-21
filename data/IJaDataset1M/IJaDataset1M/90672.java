package org.apache.batik.util;

/**
 * A set that uses two keys.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @version $Id: DoublyIndexedSet.java 592621 2007-11-07 05:58:12Z cam $
 */
public class DoublyIndexedSet {

    /**
     * The table to store entries.
     */
    protected DoublyIndexedTable table = new DoublyIndexedTable();

    /**
     * Dummy value object for the table.
     */
    protected static Object value = new Object();

    /**
     * Returns the number of entries in the set.
     */
    public int size() {
        return table.size();
    }

    /**
     * Adds an entry to the set.
     */
    public void add(Object o1, Object o2) {
        table.put(o1, o2, value);
    }

    /**
     * Removes an entry from the set.
     */
    public void remove(Object o1, Object o2) {
        table.remove(o1, o2);
    }

    /**
     * Returns whether the given keys are in the set.
     */
    public boolean contains(Object o1, Object o2) {
        return table.get(o1, o2) != null;
    }

    /**
     * Clears the set.
     */
    public void clear() {
        table.clear();
    }
}
