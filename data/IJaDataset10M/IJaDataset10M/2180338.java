package com.sun.pdfview.decrypt;

/**
 * Identifies that the specified encryption mechanism is not
 * supported by this product or platform.
 *
 * @see EncryptionUnsupportedByPlatformException
 * @see EncryptionUnsupportedByProductException
 * @author Luke Kirby
 */
public abstract class UnsupportedEncryptionException extends Exception {

    protected UnsupportedEncryptionException(String message) {
        super(message);
    }

    protected UnsupportedEncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
