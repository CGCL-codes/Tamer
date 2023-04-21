package org.mandarax.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * Iterator for a set of sets. The iterator iterats
 * over all possible combinations.
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 1.2
 */
public class MultipleCollectionIterator implements Iterator {

    private Collection[] colls = null;

    private Iterator[] iterators = null;

    private Object[] lastRecord = null;

    /**
     * Constructor.
     * @param collections java.util.Collection[]
     */
    public MultipleCollectionIterator(Collection[] collections) {
        super();
        colls = collections;
        initialize();
    }

    /**
     * Indicates whether there is a next object.
     * @return boolean
     */
    public boolean hasNext() {
        return lastNonEmptyIteratorIndex() != -1;
    }

    /**
     * Initialize the object.
     */
    private void initialize() {
        iterators = new Iterator[colls.length];
        for (int i = 0; i < colls.length; i++) {
            iterators[i] = colls[i].iterator();
        }
    }

    /**
     * Find the last index of an iterator having a next element.
     * Return -1 if there is no such iterator.
     * @return int
     */
    private int lastNonEmptyIteratorIndex() {
        int index = -1;
        for (int i = 0; i < iterators.length; i++) {
            if (iterators[i].hasNext()) {
                index = i;
            }
        }
        return index;
    }

    /**
     *  Get the next object. The next object is a vector containing exactly one
     *  object from each input collection.
     *  @return java.lang.Object
     */
    public Object next() {
        int index = lastNonEmptyIteratorIndex();
        for (int i = index + 1; i < iterators.length; i++) {
            iterators[i] = colls[i].iterator();
        }
        Object[] next = new Object[iterators.length];
        if (lastRecord == null) {
            for (int i = 0; i < iterators.length; i++) {
                next[i] = iterators[i].next();
            }
        } else {
            for (int i = 0; i < iterators.length; i++) {
                if (i < index) {
                    next[i] = lastRecord[i];
                } else {
                    next[i] = (iterators[i].next());
                }
            }
        }
        lastRecord = next;
        return next;
    }

    /**
     * Remove elements is not supported.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
