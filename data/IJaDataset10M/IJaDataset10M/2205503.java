package com.nexirius.framework.datamodel;

import java.util.Iterator;

/**
 * This class is an iterator for DataModelVector.
 *
 * @author Marcel Baumann
 */
public class DataModelEnumeration {

    public int size = 0;

    private Iterator iterator = null;

    /**
     * Creates a pseudo enumeration containing no elements
     */
    public DataModelEnumeration() {
    }

    /**
     * Creates an enumeration
     *
     * @param size The number of elements in the list
     * @param e The actual iterator
     */
    public DataModelEnumeration(int size, Iterator e) {
        this.size = size;
        this.iterator = e;
    }

    /**
     * This method returns false if there are no elements in the list or if the
     * last element was reached.
     */
    public boolean hasMore() {
        if (iterator != null) {
            return iterator.hasNext();
        }
        return false;
    }

    /**
     * Returns the next element from the list (only if hasMore() is true).
     */
    public DataModel next() {
        if (iterator != null) {
            return (DataModel) iterator.next();
        }
        return null;
    }

    public void removeActualItem() {
        iterator.remove();
    }
}
