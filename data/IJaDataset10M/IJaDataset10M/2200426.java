package net.sourceforge.gpj.cardservices.exceptions;

/**
 * 
 * Exception for errors during cap file loading in {@link
 * GlobalPlatformService#loadCapFile GlobalPlatformService.loadCapFile}.
 */
public class GPMakeSelectableException extends GPException {

    /**
     * 
     * Field to disable the serialVersionUID warning.
     */
    public static final long serialVersionUID = 1L;

    /**
     * 
     * Constructs a new GPMakeSelectableException with the specified detail
     * message.
     * 
     * @param sw
     *            failing response status
     * @param message
     *            the detailed message
     */
    public GPMakeSelectableException(short sw, String message) {
        super(sw, message);
    }

    /**
     * 
     * Constructs a new GPMakeSelectableException with the specified detail
     * message and cause.
     * 
     * @param sw
     *            failing response status
     * @param message
     *            the detailed message
     * @param cause
     *            the cause of this exception or null
     */
    public GPMakeSelectableException(short sw, String message, Throwable cause) {
        super(sw, message, cause);
    }

    /**
     * 
     * Constructs a new GPMakeSelectableException with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}.
     * 
     * @param cause
     *            the cause of this exception or null
     */
    public GPMakeSelectableException(Throwable cause) {
        super(cause);
    }
}
