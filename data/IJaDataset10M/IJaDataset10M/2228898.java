package org.freeworld.tiler.engine.resources;

public class ResourceNotFoundException extends Exception {

    private static final long serialVersionUID = 7715196532747899831L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Throwable t) {
        super(t);
    }

    public ResourceNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}
