package org.globus.gsi.proxy;

import java.security.cert.X509Certificate;
import org.globus.common.ChainedGeneralSecurityException;

/**
 */
public class ProxyPathValidatorException extends ChainedGeneralSecurityException {

    public static final int FAILURE = -1;

    public static final int PROXY_VIOLATION = 1;

    public static final int UNSUPPORTED_EXTENSION = 2;

    public static final int PATH_LENGTH_EXCEEDED = 3;

    public static final int UNKNOWN_CA = 4;

    public static final int UNKNOWN_POLICY = 5;

    public static final int REVOKED = 6;

    public static final int LIMITED_PROXY_ERROR = 7;

    private X509Certificate cert;

    private int errorCode = FAILURE;

    public ProxyPathValidatorException(int errorCode) {
        this(errorCode, null);
    }

    public ProxyPathValidatorException(int errorCode, Throwable root) {
        this(errorCode, "", root);
    }

    public ProxyPathValidatorException(int errorCode, String msg, Throwable root) {
        super(msg, root);
        this.errorCode = errorCode;
    }

    public ProxyPathValidatorException(int errorCode, X509Certificate cert, String msg) {
        super(msg, null);
        this.errorCode = errorCode;
        this.cert = cert;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * Returns the certificate that was being validated when
     * the exception was thrown.
     *
     * @return the <code>Certificate</code> that was being validated when
     * the exception was thrown (or <code>null</code> if not specified)
     */
    public X509Certificate getCertificate() {
        return this.cert;
    }
}
