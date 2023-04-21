package org.springframework.beans;

import java.beans.PropertyChangeEvent;
import org.springframework.core.ErrorCoded;

/**
 * Superclass for exceptions related to a property access,
 * such as type mismatch or invocation target exception.
 *
 * @author Rod Johnson
 */
public abstract class PropertyAccessException extends BeansException implements ErrorCoded {

    private final PropertyChangeEvent propertyChangeEvent;

    /**
	 * Create a new PropertyAccessException.
	 * @param propertyChangeEvent the PropertyChangeEvent that resulted in the problem
	 * @param msg the detail message
	 */
    public PropertyAccessException(PropertyChangeEvent propertyChangeEvent, String msg) {
        super(msg);
        this.propertyChangeEvent = propertyChangeEvent;
    }

    /**
	 * Create a new PropertyAccessException.
	 * @param propertyChangeEvent the PropertyChangeEvent that resulted in the problem
	 * @param msg the detail message
	 * @param ex the root cause
	 */
    public PropertyAccessException(PropertyChangeEvent propertyChangeEvent, String msg, Throwable ex) {
        super(msg, ex);
        this.propertyChangeEvent = propertyChangeEvent;
    }

    /**
	 * Return the PropertyChangeEvent that resulted in the problem.
	 */
    public PropertyChangeEvent getPropertyChangeEvent() {
        return propertyChangeEvent;
    }
}
