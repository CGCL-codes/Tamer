package org.progeeks.util;

import java.util.*;

/**
 *  Iterator implementation that contains a single object and
 *  returns it at the first call to next().  This is lighter weight
 *  than creating a singleton set of some other thing just to get
 *  an iterator.
 *
 *  @version   $Revision: 1.1 $
 *  @author    Paul Speed
 */
public class SingletonIterator implements Iterator {

    private Object obj;

    private boolean hasNext;

    public SingletonIterator(Object obj) {
        this.obj = obj;
        this.hasNext = true;
    }

    public boolean hasNext() {
        return (hasNext);
    }

    public Object next() {
        if (!hasNext) throw new NoSuchElementException("next() passed end of iterator.");
        hasNext = false;
        Object o = obj;
        obj = null;
        return (o);
    }

    public void remove() {
        throw new UnsupportedOperationException("remove() not supported by this iterator.");
    }
}
