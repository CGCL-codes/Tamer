package org.dmd.dmw;

import java.util.Iterator;

/**
 * The DmwMVIterator provides a common Iterable interface for primitive multi-valued
 * attributes when accessed via a DmwWrapperBase derived class i.e. anything in a full fledged
 * Java environment. This allows you to use the for(X x: collection) mechanisms.
 * This wrapper is required to provide consistency across the ArrayList, Map and Set
 * collections that are supported by the DmcAttribute.
 * <P>
 * As a note, this mechanism COULD NOT BE USED with objects in a GWT environment
 * since the java.util.Iterable interface is NOT SUPPORTED by the JRE emulation
 * mechanisms. That's why this class is here and not in org.dmd.dmc.
 * @param <CAST> The type of the container that wraps the DMOs in multi-valued attribute
 * @param <DMO>  The type of the Dark Matter Object stored in the attribute.
 */
public class DmwMVIterator<CAST> implements Iterable<CAST>, Iterator<CAST> {

    Iterator<CAST> it;

    protected DmwMVIterator() {
        it = null;
    }

    protected DmwMVIterator(Iterator<CAST> i) {
        it = i;
    }

    public boolean hasNext() {
        if (it == null) return (false);
        return (it.hasNext());
    }

    public CAST getNext() {
        if (it == null) throw (new IllegalStateException("Trying to getNext() on an empty DmwMVIterator"));
        return (it.next());
    }

    @Override
    public Iterator<CAST> iterator() {
        return (this);
    }

    @Override
    public CAST next() {
        return (getNext());
    }

    @Override
    public void remove() {
        throw (new IllegalStateException("Cannot remove from a DmwMVIterator."));
    }
}
