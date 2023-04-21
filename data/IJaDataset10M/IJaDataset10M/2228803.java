package edu.tufts.osidimpl.repository.google.local;

public class PartIterator implements org.osid.repository.PartIterator {

    private java.util.Iterator iterator = null;

    public PartIterator(java.util.Vector vector) throws org.osid.repository.RepositoryException {
        this.iterator = vector.iterator();
    }

    public boolean hasNextPart() throws org.osid.repository.RepositoryException {
        return (this.iterator.hasNext());
    }

    public org.osid.repository.Part nextPart() throws org.osid.repository.RepositoryException {
        try {
            return (org.osid.repository.Part) iterator.next();
        } catch (Throwable t) {
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.NO_MORE_ITERATOR_ELEMENTS);
        }
    }
}
