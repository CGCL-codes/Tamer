package org.dcm4chex.archive.mbean;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision$ $Date$
 * @since Oct 27, 2008
 */
public class CompressionFailedException extends Exception {

    private static final long serialVersionUID = -62831312431840923L;

    public CompressionFailedException() {
    }

    public CompressionFailedException(String message) {
        super(message);
    }

    public CompressionFailedException(Throwable cause) {
        super(cause);
    }

    public CompressionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
