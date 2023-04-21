package net.esle.sinadura.core.exceptions;

import java.security.cert.X509Certificate;

/**
 * @author alfredo
 *
 */
public class ChainValidationException extends Exception {

    private X509Certificate certificate;

    public ChainValidationException(String message) {
        super(message);
    }

    public ChainValidationException(String message, X509Certificate certificate) {
        super(message);
        this.setCertificate(certificate);
    }

    public ChainValidationException(String message, Throwable e) {
        super(message, e);
    }

    public ChainValidationException(String message, Throwable e, X509Certificate certificate) {
        super(message, e);
        this.setCertificate(certificate);
    }

    public void setCertificate(X509Certificate certificate) {
        this.certificate = certificate;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }
}
