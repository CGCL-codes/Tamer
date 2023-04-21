package org.nmc.osidimpl.repository.pachytest;

public class PropertiesIterator implements org.osid.shared.PropertiesIterator {

    private java.util.Iterator iterator = null;

    public PropertiesIterator(java.util.Vector vector) throws org.osid.shared.SharedException {
        this.iterator = vector.iterator();
    }

    public boolean hasNextProperties() throws org.osid.shared.SharedException {
        return iterator.hasNext();
    }

    public org.osid.shared.Properties nextProperties() throws org.osid.shared.SharedException {
        if (iterator.hasNext()) {
            return (org.osid.shared.Properties) iterator.next();
        } else {
            throw new org.osid.shared.SharedException(org.osid.shared.SharedException.NO_MORE_ITERATOR_ELEMENTS);
        }
    }
}
