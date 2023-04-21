package com.antlersoft.odb.diralloc;

import com.antlersoft.odb.ObjectStoreException;

public class NoSuchClassException extends ObjectStoreException {

    public NoSuchClassException() {
        super();
    }

    public NoSuchClassException(Class notFound) {
        super("Class " + notFound.getName() + " not found");
    }
}
