package javax.crypto.interfaces;

import java.math.BigInteger;
import java.security.PublicKey;

/**
 * The interface to a Diffie-Hellman public key.
 * 
 * @author Patric Kabus
 * @author Jan Peters
 * @version $Id: DHPublicKey.java 1913 2007-08-08 02:41:53Z jpeters $
 */
public interface DHPublicKey extends DHKey, PublicKey {

    /**
     * Returns the public value, y.
     *
     * @return the public value, y.
     */
    public BigInteger getY();
}
