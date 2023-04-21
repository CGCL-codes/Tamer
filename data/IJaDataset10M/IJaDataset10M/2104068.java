package net.sourceforge.gpj.cardservices.exceptions;

/**
 * 
 * Exception for errors during applet/package deletion in {@link
 * GlobalPlatformService#deleteAID GlobalPlatformService.deleteAID}.
 */
public class GPDeleteException extends GPException {

    /**
     * 
     * Field to disable the serialVersionUID warning.
     */
    public static final long serialVersionUID = 1L;

    /**
     * 
     * Constructs a new GPDeleteException with the specified detail message.
     * 
     * @param sw
     *            failing response status
     * @param message
     *            the detailed message
     */
    public GPDeleteException(short sw, String message) {
        super(sw, message);
    }

    /**
     * 
     * Constructs a new GPDeleteException with the specified detail message and
     * cause.
     * 
     * @param sw
     *            failing response status
     * @param message
     *            the detailed message
     * @param cause
     *            the cause of this exception or null
     */
    public GPDeleteException(short sw, String message, Throwable cause) {
        super(sw, message, cause);
    }

    /**
     * 
     * Constructs a new GPDeleteException with the specified cause and a detail
     * message of {@code (cause==null ? null : cause.toString())}.
     * 
     * @param cause
     *            the cause of this exception or null
     */
    public GPDeleteException(Throwable cause) {
        super(cause);
    }
}
