package org.npsnet.v.services.system;

/**
 * Thrown when a module dependency cannot be resolved.
 *
 * @author Andrzej Kapolka
 */
public class UnresolvableModuleDependencyException extends Exception {

    /**
     * Constructor.
     */
    public UnresolvableModuleDependencyException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message the detail message
     */
    public UnresolvableModuleDependencyException(String message) {
        super(message);
    }
}
