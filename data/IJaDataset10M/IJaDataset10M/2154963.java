package net.sf.csutils.cis.core;

/**
 * Error message class of the CIS core classes. This exception
 * indicates a configuration problem or another error caused by
 * the user. There is also the {@link CisCoreException}, which
 * is thrown in case of internal problems, like I/O errors.
 */
public class CisCoreErrorMessage extends CisCoreException {

    private static final long serialVersionUID = -7306832911432027139L;

    /**
     * Creates a new instance with the given error message
     * and cause.
     */
    public CisCoreErrorMessage(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }

    /**
     * Creates a new instance with the given error message
     * and no cause.
     */
    public CisCoreErrorMessage(String pMessage) {
        this(pMessage, null);
    }

    /**
     * Creates a new instance with the given cause. The causes
     * error message is used as the created instances error
     * message.
     */
    public CisCoreErrorMessage(Throwable pCause) {
        this(null, pCause);
    }
}
