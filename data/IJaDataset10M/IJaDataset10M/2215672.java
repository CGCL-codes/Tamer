package edu.tufts.osidimpl.repository.freebase;

public class Properties implements org.osid.shared.Properties {

    public org.osid.shared.ObjectIterator getKeys() throws org.osid.shared.SharedException {
        return new ObjectIterator(new java.util.Vector());
    }

    public java.io.Serializable getProperty(java.io.Serializable key) throws org.osid.shared.SharedException {
        throw new org.osid.shared.SharedException(org.osid.shared.SharedException.UNKNOWN_KEY);
    }

    public org.osid.shared.Type getType() throws org.osid.shared.SharedException {
        return new Type("mit.edu", "properties", "general");
    }
}
