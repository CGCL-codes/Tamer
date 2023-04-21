package org.nmc.osidimpl.repository.pachytest;

public class RecordIterator implements org.osid.repository.RecordIterator {

    private java.util.Iterator iterator = null;

    public RecordIterator(java.util.Vector vector) throws org.osid.repository.RepositoryException {
        this.iterator = vector.iterator();
    }

    public boolean hasNextRecord() throws org.osid.repository.RepositoryException {
        return iterator.hasNext();
    }

    public org.osid.repository.Record nextRecord() throws org.osid.repository.RepositoryException {
        if (iterator.hasNext()) {
            return (org.osid.repository.Record) iterator.next();
        } else {
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.NO_MORE_ITERATOR_ELEMENTS);
        }
    }
}
