package edu.psu.its.lionshare.search.drosid;

public class ObjectIterator implements org.osid.shared.ObjectIterator {

    private java.util.Vector vector = new java.util.Vector();

    private int i = 0;

    public ObjectIterator(java.util.Vector vector) throws org.osid.shared.SharedException {
        this.vector = vector;
    }

    public boolean hasNextObject() throws org.osid.shared.SharedException {
        return i < vector.size();
    }

    public java.io.Serializable nextObject() throws org.osid.shared.SharedException {
        if (i < vector.size()) {
            return (java.io.Serializable) vector.elementAt(i++);
        } else {
            throw new org.osid.shared.SharedException(org.osid.shared.SharedException.NO_MORE_ITERATOR_ELEMENTS);
        }
    }
}
