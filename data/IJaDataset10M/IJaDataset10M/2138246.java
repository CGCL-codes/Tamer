package org.tolven.security.key;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

/**
 * An interface to define PublicKey/PrivateKey functionality for a user
 * 
 * @author Joseph Isaac
 */
public interface UserKeyRing {

    public UserPrivateKey getUserPrivateKey();

    public void setUserPrivateKey(UserPrivateKey privateKey);

    public UserPublicKey getUserPublicKey();

    public PublicKey getPublicKey() throws GeneralSecurityException;

    public void setPublicKey(PublicKey aPublicKey);

    public PrivateKey getPrivateKey() throws GeneralSecurityException;

    public void setPrivateKey(PrivateKey aPrivateKey);

    public Certificate[] getUserCertificateChain();

    public void setUserCertificateChain(Certificate[] certificateChain);
}
