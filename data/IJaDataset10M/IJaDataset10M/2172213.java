package org.peaseplate;

/**
 * An exception thrown by implementations of the {@link Locator} interface.
 * 
 * @author Manfred HANTSCHEL
 */
public class TemplateLocatorException extends TemplateException {

    private static final long serialVersionUID = 1L;

    /**
	 * Create the exception using the specified message
	 * @param message the message
	 */
    public TemplateLocatorException(String message) {
        super(message);
    }

    /**
	 * Create the exception using the specified message and the specified cause for the exception
	 * @param message the message
	 * @param cause the cause for the exception
	 */
    public TemplateLocatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
