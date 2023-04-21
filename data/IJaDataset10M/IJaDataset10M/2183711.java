package javax.security.cert;

/**
 * <p>Signals a parsing error when decoding a certificate.</p>
 *
 * <p><b>This class is deprecated. It should not be used in new
 * applications.</b></p>
 */
public class CertificateParsingException extends CertificateException {

    public CertificateParsingException() {
        super();
    }

    public CertificateParsingException(String msg) {
        super(msg);
    }
}
