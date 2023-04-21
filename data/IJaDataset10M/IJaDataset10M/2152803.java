package edu.tufts.osidimpl.repository.freebase;

public class RecordStructureIterator implements org.osid.repository.RecordStructureIterator {

    private java.util.Iterator iterator = null;

    public RecordStructureIterator(java.util.Vector vector) throws org.osid.repository.RepositoryException {
        this.iterator = vector.iterator();
    }

    public boolean hasNextRecordStructure() throws org.osid.repository.RepositoryException {
        return iterator.hasNext();
    }

    public org.osid.repository.RecordStructure nextRecordStructure() throws org.osid.repository.RepositoryException {
        if (iterator.hasNext()) {
            return (org.osid.repository.RecordStructure) iterator.next();
        } else {
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.NO_MORE_ITERATOR_ELEMENTS);
        }
    }
}
