package org.jarcraft;

/**
 * Indicates that component is not in required state to perform operation.
 *
 * @author Leon van Zantvoort
 */
public class ComponentStateException extends ComponentException {

    private static final long serialVersionUID = 1234636432034623464L;

    public ComponentStateException(String componentName) {
        super(componentName);
    }

    public ComponentStateException(String componentName, String message) {
        super(componentName, message);
    }

    public ComponentStateException(String componentName, Throwable cause) {
        super(componentName, cause);
    }

    public ComponentStateException(String componentName, String message, Throwable cause) {
        super(componentName, message, cause);
    }
}
