package org.ajax4jsf.portlet.context;

import java.util.Enumeration;
import java.util.Iterator;

class EnumerationIterator implements Iterator {

    public EnumerationIterator(Enumeration enumeration) {
        this.enumeration = enumeration;
    }

    private Enumeration enumeration;

    public boolean hasNext() {
        return this.enumeration.hasMoreElements();
    }

    public Object next() {
        return this.enumeration.nextElement();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
