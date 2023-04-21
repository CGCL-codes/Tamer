package org.primordion.xholon.exception;

/**
 * An exception that occurs while a Xholon application is configuring itself at startup,
 * or during some sort of reconfiguration or change as the model is executing.
 * Typical causes of configuration exceptions include:
 * (1) reading an XML config file.
 * (2) creating a new xholon instance using a factory.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.7.1 (Created on November 12, 2007)
 */
public class XholonConfigurationException extends XholonException {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructs a new exception with null as its detail message.
	 */
    public XholonConfigurationException() {
        super();
    }

    /**
	 * Constructs a new exception with the specified detail message.
	 * @param message - the detail message.
	 * The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
    public XholonConfigurationException(String message) {
        super(message);
    }

    /**
	 * Constructs a new exception with the specified detail message and cause.
	 * @param message - the detail message
	 * (which is saved for later retrieval by the Throwable.getMessage() method).
	 * @param cause - the cause (which is saved for later retrieval by the Throwable.getCause() method).
	 * (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
    public XholonConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Constructs a new exception with the specified cause and a detail message of
	 * (cause==null ? null : cause.toString())
	 * (which typically contains the class and detail message of cause).
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method).
	 * (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
    public XholonConfigurationException(Throwable cause) {
        super(cause);
    }
}
