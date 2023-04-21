package edu.tufts.osidimpl.repository.fedora_2_2;

public class LongValueIterator implements org.osid.shared.LongValueIterator {

    private java.util.Vector vector = new java.util.Vector();

    private int i = 0;

    public LongValueIterator(java.util.Vector vector) throws org.osid.shared.SharedException {
        this.vector = vector;
    }

    public boolean hasNextLongValue() throws org.osid.shared.SharedException {
        return i < vector.size();
    }

    public long nextLongValue() throws org.osid.shared.SharedException {
        if (i < vector.size()) {
            return ((Long) vector.elementAt(i++)).longValue();
        } else {
            throw new org.osid.shared.SharedException(org.osid.shared.SharedException.NO_MORE_ITERATOR_ELEMENTS);
        }
    }
}
