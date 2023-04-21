package de.grogra.persistence;

public interface PersistenceOutputListener {

    void objectWritten(Object object);

    void sharedObjectReferenceWritten(Shareable o);
}
