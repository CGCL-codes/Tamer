package com.osgisamples.congress.persistence;

public class ObjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6732653573945268208L;

    public ObjectNotFoundException(Class clazz, Object object) {
        super("The following object of type (" + clazz + ") cannot be found - " + object);
    }
}
