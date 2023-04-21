package org.melati.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * An <code>Enumeration</code> of Longs.
 */
public class LongEnumeration implements Enumeration {

    private long start, limit, i;

    public LongEnumeration(long start, long limit) {
        this.start = start;
        this.limit = limit;
        this.i = this.start;
    }

    public boolean hasMoreElements() {
        return i < limit;
    }

    public synchronized Object nextElement() throws NoSuchElementException {
        if (i >= limit) throw new NoSuchElementException();
        return new Long(i++);
    }
}
