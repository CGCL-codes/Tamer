package org.ncbi.osidimpl.repository.basic;

public class AssetIterator implements org.osid.repository.AssetIterator {

    private java.util.Iterator iterator = null;

    protected AssetIterator(java.util.Vector vector) {
        this.iterator = vector.iterator();
    }

    public boolean hasNextAsset() throws org.osid.repository.RepositoryException {
        return iterator.hasNext();
    }

    public org.osid.repository.Asset nextAsset() throws org.osid.repository.RepositoryException {
        if (iterator.hasNext()) {
            return (org.osid.repository.Asset) iterator.next();
        } else {
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.NO_MORE_ITERATOR_ELEMENTS);
        }
    }
}
