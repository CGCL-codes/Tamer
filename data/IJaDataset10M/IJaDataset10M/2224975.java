package com.ssx.xml.output.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.ssx.xml.output.Attribute;
import com.ssx.xml.output.AttributeList;

/**
 * A container for a collection of XML attributes.
 */
class AttributeListImpl implements AttributeList {

    private List _store = null;

    /** Create a new, empty, attribute list */
    public AttributeListImpl() {
    }

    /** Add an attribute to the collection.   
     * @param item The attribute values to add to the collection 
     */
    public void add(AttributeImpl item) {
        if (null == item) {
            throw new IllegalArgumentException("Attempt to add null attribute");
        }
        if (null == item.getName()) {
            throw new IllegalArgumentException("A name is required to add an attribute");
        }
        if (null == _store) {
            _store = new LinkedList();
        }
        _store.add(item);
    }

    /** @return An iterator that can be used to access all of the Attributes
     * in the collection. */
    public Iterator iterator() {
        if (null == _store) {
            return new NoRemoveIterator(null);
        }
        return new NoRemoveIterator(_store.iterator());
    }

    /** @return The number of Attributes in the collection. */
    public int size() {
        if (null == _store) {
            return 0;
        }
        return _store.size();
    }

    /** Remove all attributes from the collection */
    public void clear() {
        if (null != _store) {
            _store.clear();
        }
    }

    /** @see AttributeList#getAttribute(int) */
    public Attribute getAttribute(int index) {
        if (null == _store) {
            throw new IndexOutOfBoundsException("No attributes in list");
        }
        return (Attribute) _store.get(index);
    }

    /** Wrap in iterator to disable the remove method */
    private class NoRemoveIterator implements Iterator {

        private final Iterator _base;

        /** @param base The underlying Iterator */
        public NoRemoveIterator(Iterator base) {
            _base = base;
        }

        /** Pass through to the base iterator. 
         * @see java.util.Iterator#hasNext() */
        public boolean hasNext() {
            if (null == _base) {
                return false;
            }
            return _base.hasNext();
        }

        /** Pass through to the base iterator.
         * @see java.util.Iterator#next() */
        public Object next() {
            if (null == _base) {
                return null;
            }
            return _base.next();
        }

        /** @see java.util.Iterator#remove() */
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported by this iterator");
        }
    }
}
