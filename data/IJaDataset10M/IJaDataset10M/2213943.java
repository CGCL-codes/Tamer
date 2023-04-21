package es.caib.signatura.api;

/** 
 * There isn't private keys to sign.
 * 
 * @author Jesús Reyes
 * @version 1.0
 */
public class SignaturePrivKeyException extends SignatureException {

    public SignaturePrivKeyException() {
        super("There is not private keys to sign.");
    }
}
