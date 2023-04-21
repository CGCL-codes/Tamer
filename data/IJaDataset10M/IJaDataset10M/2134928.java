package org.hardtokenmgmt.core.token;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import iaik.pkcs.pkcs11.objects.PrivateKey;

/**
 * This is an adapter class that allows to use token keys as JCA private keys.
 * An application can use this class whereever an interface requires the
 * application to pass an JCA private key; e.g. for signing.
 *
 * @author <a href="mailto:Karl.Scheibelhofer@iaik.at"> Karl Scheibelhofer </a>
 * @version 0.1
 */
public class TokenPrivateKey implements RSAPrivateKey {

    private static final long serialVersionUID = 1L;

    /**
   * The PKCS#11 private key of this object.
   */
    protected PrivateKey tokenPrivateKey_;

    /**
   * Create a new JCA private key that uses the given PKCS#11 private key
   * internally.
   *
   * @param tokenPrivateKey The PKCS#11 private key that this object refers to.
   */
    public TokenPrivateKey(PrivateKey tokenPrivateKey) {
        tokenPrivateKey_ = tokenPrivateKey;
    }

    /**
   * Just returns null.
   *
   * @return null.
   */
    public String getAlgorithm() {
        return null;
    }

    /**
   * Just returns null.
   *
   * @return null.
   */
    public String getFormat() {
        return null;
    }

    /**
   * Just returns null.
   *
   * @return null.
   */
    public byte[] getEncoded() {
        return null;
    }

    /**
   * Returns the PKCS#11 private key object that this object refers to.
   *
   * @return The KCS#11 private key object that this object refers to.
   */
    public PrivateKey getTokenPrivateKey() {
        return tokenPrivateKey_;
    }

    public BigInteger getPrivateExponent() {
        return null;
    }

    public BigInteger getModulus() {
        return null;
    }
}
