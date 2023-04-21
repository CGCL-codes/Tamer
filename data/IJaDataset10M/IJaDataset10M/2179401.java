package codec.pkcs7;

import java.security.SignatureException;

/**
 * Thrown when a <code>SignerInfo</code> is not found. This can happen e.g.
 * when a <code>Verifier</code> is initialized with a certificate and
 * <code>SignedData</code> but the <code>SignedData</code> instance does not
 * contain a <code>
 * SignerInfo</code> that matches the subject of the given
 * certificate.
 * 
 * @author Volker Roth
 * @version "$Id: NoSuchSignerException.java,v 1.2 2000/12/06 17:47:32 vroth Exp $"
 */
public class NoSuchSignerException extends SignatureException {

    /**
     * Creates an instance.
     */
    public NoSuchSignerException() {
        super();
    }

    /**
     * Creates an instance with the given message.
     * 
     * @param message
     *                The message.
     */
    public NoSuchSignerException(String message) {
        super(message);
    }
}
