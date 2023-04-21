package org.crud4j.core;

/**
 * This exception is thrown when the given id is in use by another bean of the
 * same type
 */
public class IdInUseException extends Exception {

    private Object id;

    private Class type;

    public IdInUseException(Object id, Class type) {
        super();
        this.id = id;
        this.type = type;
    }
}
