package org.msurtani.ssh4j;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Key;

/**
 * This interface represents an object that handles the exchange of keys.
 * @author  manik
 */
public interface KeyExchanger {

    /**
     * This method starts the key exchange process.
     */
    public void exchangeKeys() throws IOException;

    public byte[] getSessionId();

    public KeyPair getClientKeyPair();

    public PublicKey getHostPublicKey();

    public Key getSharedSecret();
}
