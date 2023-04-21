package org.ikasan.framework.component.transformation;

/**
 * Base TransformationException
 * 
 * @author Ikasan Development Team
 */
public class TransformationException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = 5643215546008399313L;

    /**
     * Constructor
     * 
     * @param cause - The cause
     */
    public TransformationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor
     * 
     * @param message - The exception message
     */
    public TransformationException(String message) {
        super(message);
    }
}
