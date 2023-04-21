package com.ctrcv.framework.persistence;

import java.io.InputStream;

public abstract class FilePersistence {

    static FilePersistence persister = null;

    static {
        persister = new LocalFilePersistence();
    }

    public static FilePersistence getInstance() {
        return persister;
    }

    public abstract void write(String path, InputStream is) throws PersistenceException;

    public abstract byte[] read(String path) throws PersistenceException;

    public abstract String[] list(String path) throws PersistenceException;

    public abstract void delete(String path) throws PersistenceException;
}
