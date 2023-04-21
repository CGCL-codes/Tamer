package org.lhuillier.pwsafe.io;

/**
 * Thrown when a password decryption fails because the expected HMAC (Hash-based
 * Message Authentication Code) does not match the values that were decrypted.
 * <p>
 * This does not necessarily mean the key is invalid. The database could have
 * been corrupted. In most cases, an invalid key is the cause.
 */
public class DecryptionFailedException extends DbIoException {

    private static final long serialVersionUID = -7514620034696932443L;

    public DecryptionFailedException() {
    }

    public DecryptionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecryptionFailedException(String message) {
        super(message);
    }

    public DecryptionFailedException(Throwable cause) {
        super(cause);
    }
}
