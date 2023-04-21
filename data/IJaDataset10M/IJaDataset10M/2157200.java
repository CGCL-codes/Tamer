package org.nfcsigning.algorithm;

/**
 *
 * @author Markus Kilås
 */
public class SignatureException extends Exception {

    private Exception causedBy;

    /**
     * Creates a new instance of <code>SignException</code> without detail message.
     */
    public SignatureException() {
    }

    /**
     * Constructs an instance of <code>SignException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SignatureException(String msg) {
        super(msg);
    }

    public SignatureException(Exception causedBy) {
        this.causedBy = causedBy;
    }

    public SignatureException(Exception causedBy, String msg) {
        super(msg);
        this.causedBy = causedBy;
    }

    public Exception getCausedBy() {
        return causedBy;
    }
}
