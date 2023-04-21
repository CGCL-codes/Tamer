package com.shimari.profile;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Justin Wells
 * @version 1.0
 */
public final class NullProfiler implements Profiler {

    public static final NullProfiler INSTANCE = new NullProfiler();

    private static final Iterator NULL_ITERATOR = new Iterator() {

        public boolean hasNext() {
            return false;
        }

        public Object next() throws NoSuchElementException {
            throw new NoSuchElementException("No elements");
        }

        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("No remove");
        }
    };

    public Iterator getEvents() throws IllegalStateException {
        return NULL_ITERATOR;
    }

    public void startEvent(String name) {
    }

    public void stopEvent() {
    }

    public void destroy() {
    }

    public String toString() {
        return "NullProfiler()";
    }
}
