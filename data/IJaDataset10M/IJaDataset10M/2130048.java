package org.databene.commons.depend;

/**
 * Exception that signals a cyclic dependency.
 * @author Volker Bergmann
 * @since 0.3.04
 */
public class CyclicDependencyException extends RuntimeException {

    private static final long serialVersionUID = 3462929902648133425L;

    public CyclicDependencyException() {
        super();
    }

    public CyclicDependencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public CyclicDependencyException(String message) {
        super(message);
    }

    public CyclicDependencyException(Throwable cause) {
        super(cause);
    }
}
