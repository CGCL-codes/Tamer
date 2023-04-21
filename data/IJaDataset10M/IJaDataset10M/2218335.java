package org.xith3d.scenegraph;

/**
 * The exception used by all of the scene graph objects and methods
 * that throw checked exceptions.
 * 
 * @author Scott Shaver
 */
public class SceneGraphException extends Exception {

    private static final long serialVersionUID = -23991242050571005L;

    /**
     * Constructs a new SceneGraphException with null as its detail message.
     */
    public SceneGraphException() {
        super();
    }

    /**
     * Constructs a new SceneGraphException with the specified detail message.
     */
    public SceneGraphException(String message) {
        super(message);
    }

    /**
     * Constructs a new SceneGraphException with the specified detail message
     * and cause.
     */
    public SceneGraphException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new SceneGraphException with the specified cause
     * and detail message of (cause==null ? null : cause.toString())
     * (which typically contains the class and detail message of cause).
     */
    public SceneGraphException(Throwable cause) {
        super(cause);
    }
}
