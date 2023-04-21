package com.sun.j3d.utils.scenegraph.io;

/**
 * The named object has not been loaded so it's instance can not be returned
 */
public class ObjectNotLoadedException extends java.lang.Exception {

    /**
 * Creates new <code>ObjectNotLoadedException</code> without detail message.
     */
    public ObjectNotLoadedException() {
    }

    /**
 * Constructs an <code>ObjectNotLoadedException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ObjectNotLoadedException(String msg) {
        super(msg);
    }
}
