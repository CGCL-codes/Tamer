package org.datanucleus.util;

import javax.jdo.datastore.Sequence;

/**
 * Simple implementation of a sequence that counts from 0 and increments
 * by 1 every time. Allocation is not necessary because it increments
 * a long value internally.
 */
public class SimpleSequence implements Sequence {

    String name;

    long current = 0;

    public SimpleSequence(String name) {
        this.name = name;
    }

    /**
     * 
     * @see javax.jdo.datastore.Sequence#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @see javax.jdo.datastore.Sequence#next()
     */
    public Object next() {
        current++;
        return new Long(current);
    }

    /**
     * 
     * @see javax.jdo.datastore.Sequence#nextValue()
     */
    public long nextValue() {
        current++;
        return current;
    }

    /**
     * 
     * @see javax.jdo.datastore.Sequence#allocate(int)
     */
    public void allocate(int arg0) {
    }

    /**
     * 
     * @see javax.jdo.datastore.Sequence#current()
     */
    public Object current() {
        return new Long(current);
    }

    /**
     * 
     * @see javax.jdo.datastore.Sequence#currentValue()
     */
    public long currentValue() {
        return current;
    }
}
