package net.sf.traser.security;

import java.security.KeyStoreException;
import java.security.cert.X509Certificate;

/**
 * This interface defines methods to manage certificate data in a keystore.
 * 
 * @author Marcell Szathmári
 */
public interface CryptoManager {

    /**
     * Saves a certificate in the keystore. 
     * @param cert the certificate to save.
     * @return the alias of the inserted certificate.
     * @throws java.security.KeyStoreException
     */
    String saveCertificate(X509Certificate cert) throws KeyStoreException;
}
