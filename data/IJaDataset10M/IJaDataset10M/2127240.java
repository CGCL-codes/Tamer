package org.apache.shindig.gadgets.render;

/**
 * Exceptions thrown during gadget rendering.
 *
 * These execeptions will usually translate directly into an end-user error message, so they should
 * be easily localizable.
 */
public class RenderingException extends Exception {

    public RenderingException(Throwable t) {
        super(t);
    }

    public RenderingException(String message) {
        super(message);
    }

    public RenderingException(String message, Throwable t) {
        super(message, t);
    }
}
