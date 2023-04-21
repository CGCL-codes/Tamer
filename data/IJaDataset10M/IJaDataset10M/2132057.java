package org.apache.ws.security;

import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.security.auth.callback.Callback;

/**
 */
public class PublicKeyCallback implements Callback {

    private java.security.PublicKey publicKey;

    private boolean verified = false;

    public PublicKeyCallback(java.security.PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void setPublicKey(java.security.PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public java.security.PublicKey getPublicKey() {
        return publicKey;
    }

    public void setVerified(boolean b) {
        verified = b;
    }

    public boolean isVerified() {
        return verified;
    }

    /**
     * Evaluate whether a given public key should be trusted.
     * Essentially, this amounts to checking to see if there is a certificate in the keystore,
     * whose public key matches the transmitted public key.
     */
    public boolean verifyTrust(java.security.KeyStore keyStore) throws WSSecurityException {
        if (publicKey == null || keyStore == null) {
            return false;
        }
        try {
            for (Enumeration e = keyStore.aliases(); e.hasMoreElements(); ) {
                String alias = (String) e.nextElement();
                Certificate[] certs = keyStore.getCertificateChain(alias);
                Certificate cert;
                if (certs == null || certs.length == 0) {
                    cert = keyStore.getCertificate(alias);
                    if (cert == null) {
                        continue;
                    }
                } else {
                    cert = certs[0];
                }
                if (!(cert instanceof X509Certificate)) {
                    continue;
                }
                X509Certificate x509cert = (X509Certificate) cert;
                if (publicKey.equals(x509cert.getPublicKey())) {
                    verified = true;
                    return true;
                }
            }
        } catch (KeyStoreException e) {
            return false;
        }
        return false;
    }
}
