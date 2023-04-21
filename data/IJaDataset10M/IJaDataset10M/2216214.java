package org.jwatter.model;

/**
 * Thrown when a frame with a given name does not exist in a window or frame.
 */
public class NoSuchFrameException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoSuchFrameException() {
        super();
    }

    public NoSuchFrameException(String message) {
        super(message);
    }

    public NoSuchFrameException(Class<? extends Frame> frameClass, Class<? extends Window> windowClass) {
        super(frameClass.getSimpleName() + " does not exist in " + windowClass.getSimpleName());
    }

    public NoSuchFrameException(String name, Class<? extends Window> windowClass) {
        super("frame " + name + " does not exist in " + windowClass.getSimpleName());
    }
}
