package org.apache.velocity.exception;

/**
 * Application-level exception thrown when macro calls within macro calls
 * exceeds the maximum allowed depth. The maximum allowable depth is given
 * in the configuration as velocimacro.max.depth.
 * @since 1.6
 */
public class MacroOverflowException extends VelocityException {

    /**
    * Version Id for serializable
    */
    private static final long serialVersionUID = 7305635093478106342L;

    /**
     * @param exceptionMessage The message to register.
     */
    public MacroOverflowException(final String exceptionMessage) {
        super(exceptionMessage);
    }

    /**
     * @param exceptionMessage The message to register.
     * @param wrapped A throwable object that caused the Exception.
     */
    public MacroOverflowException(final String exceptionMessage, final Throwable wrapped) {
        super(exceptionMessage, wrapped);
    }

    /**
     * @param wrapped A throwable object that caused the Exception.
     */
    public MacroOverflowException(final Throwable wrapped) {
        super(wrapped);
    }
}
