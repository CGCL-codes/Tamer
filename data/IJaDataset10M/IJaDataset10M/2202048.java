package org.lokee.punchcard.config.dependency;

import org.lokee.punchcard.config.ConfigException;

public class DependencyException extends ConfigException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5168385864420688261L;

    /**
	 * @param message
	 * @param severity
	 * @param exception
	 */
    public DependencyException(String message, String severity, Exception exception) {
        super(message, severity, exception);
    }

    /**
	 * @param message
	 * @param severity
	 */
    public DependencyException(String message, String severity) {
        super(message, severity);
    }
}
