package com.volantis.synergetics.impl.directory.monitor;

public class InMemoryTimestampPersistenceFactory implements TimestampPersistenceFactory {

    public TimestampPersistence createTimestampPersistence(String dirName, boolean recursive) {
        return new InMemoryTimestampPersistence(dirName, recursive);
    }
}
