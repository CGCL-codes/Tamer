package org.sepp.security.interfaces;

import iaik.me.security.CryptoBag;
import org.sepp.config.Configuration;
import org.sepp.datatypes.Credentials;
import org.sepp.exceptions.SecurityServiceException;
import org.sepp.security.SecurityConstants;
import org.sepp.security.datatypes.CertificateBag;
import org.sepp.security.datatypes.KeyBag;

public interface SecurityService {

    public void init(Configuration configuration) throws SecurityServiceException;

    /**
	 * Encrypts the provided data using the provided {@link KeyBag}. The cipher
	 * text is returned as byte array if everything was OK.
	 * 
	 * @param data
	 *            The data which should be encrpyted.
	 * @param key
	 *            The {@link KeyBag} which should be used.
	 * @return The cipher text as byte array.
	 * @throws SecurityServiceException
	 *             Thrown if an error has occured during the encryption process.
	 */
    public byte[] symmetricEncrypt(byte[] data, KeyBag key) throws SecurityServiceException;

    /**
	 * Decrypts the provided cipherText using the specified {@link KeyBag} and
	 * initialization vector. If everything is OK the original data is returned
	 * as byte array.
	 * 
	 * @param cipherText
	 *            The data which should be decrypted.
	 * @param key
	 *            The {@link KeyBag} to use for the decryption.
	 * @param IV
	 *            The initialization vector to use.
	 * @return The original data.
	 * @throws SecurityServiceException
	 *             Thrown if an error has occurred during the decryption
	 *             process.
	 */
    public byte[] symmetricDecrypt(byte[] cipherText, KeyBag key, byte[] IV) throws SecurityServiceException;

    /**
	 * Creates a signature from the provided data using the {@link KeyBag}
	 * stored at the local key store for the specified alias. If no
	 * {@link KeyBag} is stored for the provided alias an exception is thrown.
	 * 
	 * @param data
	 *            The data for which a signature should be created.
	 * @param alias
	 *            The alias for which a private key should be obtained from the
	 *            local key store.
	 * @return The signature as byte array.
	 * @throws SecurityServiceException
	 *             Thrown if an error occurs during the signature creation
	 *             process.
	 */
    public byte[] createSignature(byte[] data, String alias) throws SecurityServiceException;

    /**
	 * Creates a signature from the provided data using the {@link KeyBag}
	 * stored at the local key store for the local peer id. If no {@link KeyBag}
	 * is stored for the provided alias an exception is thrown.
	 * 
	 * @param data
	 *            The data for which a signature should be created.
	 * @return The signature as byte array.
	 * @throws SecurityServiceException
	 *             Thrown if an error occurs during the signature creation
	 *             process.
	 */
    public byte[] createSignature(byte[] data) throws SecurityServiceException;

    /**
	 * Creates a signature from the provided data using the provided private
	 * key. The public key can be provided to directly check if the created
	 * signature can be verified and only return the signature if it was
	 * verifiable. If no public key was provided the created signature is
	 * returned without immediate verification.
	 * 
	 * @param data
	 *            The data for which a signature should be created.
	 * @param privateKey
	 *            The {@link KeyBag} which should be used to create the
	 *            signature.
	 * @param publicKey
	 *            The {@link KeyBag} to verify the created signature.
	 * @return The signature as byte array.
	 * @throws SecurityServiceException
	 *             Thrown if an error occurs during the signature creation
	 *             process.
	 */
    public byte[] createSignature(byte[] data, KeyBag privateKey, KeyBag publicKey) throws SecurityServiceException;

    /**
	 * Verifies the provided signedData byte array. The provided peerId is used
	 * to obtain a public key from the key store which is then used to verify
	 * the signed data with the data which is unsigned. If the signed data could
	 * be verified true is returned otherwise false.
	 * 
	 * @param data
	 *            The original data which was signed.
	 * @param signature
	 *            The signature itself.
	 * @param peerId
	 *            The peerId of the signer.
	 * @return True if the signature could be verified otherwise false.
	 */
    public boolean verifySignature(byte[] data, byte[] signature, String peerId);

    /**
	 * Verifies the provided signedData byte array using the provided public key
	 * with the data which is unsigned. If the signed data could be verified
	 * true is returned otherwise false.
	 * 
	 * @param data
	 *            The original data which was signed.
	 * @param signature
	 *            The signature itself.
	 * @param publicKey
	 *            The public key from the signer.
	 * @return True if the signature could be verified otherwise false.
	 */
    public boolean verifySignature(byte[] data, byte[] signature, KeyBag publicKey);

    /**
	 * Encrypts the provided data using asymmetric encryption. The provided
	 * peerID is used to obtain the public key from the local key store and use
	 * this key for encryption.
	 * 
	 * TODO: Currently there is no possibility to specify the encryption
	 * mechansism since it is predetermined by the key.
	 * 
	 * @param data
	 *            The data which should be encrypted using asymmetric
	 *            cryptography.
	 * @param peerID
	 *            The peerID whose public key should be used for encryption.
	 * @return The encrypted data as byte array.
	 * @throws SecurityServiceException
	 *             Thrown if something went wrong.
	 */
    public byte[] asymmetricEncrypt(byte[] data, String peerID) throws SecurityServiceException;

    /**
	 * Encrypts the provided data using asymmetric encryption and the provided
	 * public key.
	 * 
	 * @param data
	 *            The data which should be encrypted using asymmetric
	 *            cryptography.
	 * @param publicKey
	 *            The public key which should be used for encryption.
	 * @return The encrypted data as byte array.
	 * @throws SecurityServiceException
	 *             Thrown if something went wrong.
	 */
    public byte[] asymmetricEncrypt(byte[] data, CertificateBag certificate) throws SecurityServiceException;

    /**
	 * Decrypts the provided data using asymmetric encryption. For decryption
	 * the private key which is associated with the local peer is obtained from
	 * the key store.
	 * 
	 * @param data
	 *            The data which should be decrypted using asymmetric
	 *            cryptography.
	 * @return The decrypted data as byte array.
	 * @throws SecurityServiceException
	 *             Thrown if something went wrong.
	 */
    public byte[] asymmetricDecrypt(byte[] data) throws SecurityServiceException;

    /**
	 * Decrypts the provided data using asymmetric encryption and the provided
	 * private key.
	 * 
	 * @param data
	 *            The data which should be decrypted using asymmetric
	 *            cryptography.
	 * @param privateKey
	 *            The private key which should be used to decrypt the data.
	 * @return The decrypted data as byte array.
	 * @throws SecurityServiceException
	 *             Thrown if something went wrong.
	 */
    public byte[] asymmetricDecrypt(byte[] data, KeyBag privateKey) throws SecurityServiceException;

    /**
	 * Obtains the public key certificate from the local key store for the local
	 * peer in X509 format.
	 * 
	 * @return The {@link CertificateBag} associated with the local peer.
	 * @throws SecurityServiceException
	 *             Thrown if something went wrong.
	 */
    public CertificateBag getLocalPeerCertificate() throws SecurityServiceException;

    /**
	 * Obtains the public key certificate from the local key store for the
	 * specified peer in X509 format.
	 * 
	 * @param peerID
	 *            The peer whose public key certificate should be obtained.
	 * @return The {@link CertificateBag} associated with the specified peer.
	 * @throws SecurityServiceException
	 *             Thrown if no public key certificate could be obtained for the
	 *             specified peer.
	 */
    public CertificateBag getPeerCertificate(String peerID) throws SecurityServiceException;

    /**
	 * Returns the public key from the local key store for the specified peer as
	 * {@link KeyBag}.
	 * 
	 * @param peerID
	 *            The peer whose public key should be obtained.
	 * @return The public key associated with the specified peer.
	 * @throws SecurityServiceException
	 *             Thrown if no public key could be obtained for the specified
	 *             peer.
	 */
    public KeyBag getPublicKey(String peerID) throws SecurityServiceException;

    /**
	 * Tests if the public key certificate associated with the provided peerID
	 * exists in the local key store. If yes true is returned otherwise false.
	 * 
	 * @param peerID
	 *            The peerID whose public key certificate should exist in the
	 *            key store.
	 * @return True if the certificate is available otherwise false.
	 */
    public boolean isCertificateAvailable(String peerID);

    /**
	 * Verifies if the provided certificate is valid at this point in time. This
	 * means that the current time is not before and not after the intended
	 * validity time of the certificate.
	 * 
	 * @param certificate
	 *            The certificate whose validity should be verified.
	 * @return True if the certificate is valid at this time or false if not.
	 */
    public boolean isCertificateValid(CertificateBag certificate);

    /**
	 * Verifies if the provided certificate has been issued and signed from the
	 * trusted CA certificate. The trusted CA certificate must be preinstalled
	 * in the used key store, otherwise this function will fail and raise an
	 * exception.
	 * 
	 * @param certificate
	 *            The certificate which should be verified.
	 * @return True if the certificate is authentic.
	 * @throws SecurityServiceException
	 *             Thrown if something went wrong.
	 */
    public boolean verifyCert(CertificateBag certificate);

    /**
	 * Adds and public key certificate to the local key store using the provided
	 * peerID as alias. Therefore, the public key certificate is associated with
	 * the peer.
	 * 
	 * @param peerID
	 *            The peerID which should be used as alias for the provided
	 *            certificate in the key store.
	 * @param certificate
	 *            The certificate which should be stored in the key store.
	 * @throws SecurityServiceException
	 *             Thrown if something went wrong.
	 */
    public void addPeerCertificateToTrustStore(String peerID, CertificateBag certificate) throws SecurityServiceException;

    /**
	 * Returns the {@link KeyBag} for the provided alias. This method only
	 * returns a valid key if an password is specified in the configuration for
	 * the specified alias and the private key is stored in the keystore.
	 * 
	 * @param alias
	 *            The alias for which the private key should be obtained.
	 * @return The {@link KeyBag} for the provided alias.
	 * @throws SecurityServiceException
	 *             Thrown if something went wrong.
	 */
    public KeyBag getPrivateKey(String alias) throws SecurityServiceException;

    /**
	 * Returns the {@link KeyBag} stored in the key store under the provided
	 * alias.
	 * 
	 * @param alias
	 *            The alias of the requested {@link KeyBag}.
	 * @return The requested {@link KeyBag}.
	 * @throws SecurityServiceException
	 *             Thrown if an error occurs.
	 */
    public KeyBag getSecretKey(String alias) throws SecurityServiceException;

    /**
	 *
	 */
    public void addSharedKey(String alias, CryptoBag key);

    /**
	 * 
	 */
    public CryptoBag getSharedKey(String alias);

    /**
	 * 
	 */
    public void removeSharedKey(String alias);

    /**
	 * Generates and random number and returns it as byte array.
	 * 
	 * TODO: Currently the size of the random number is fixed to 15 bytes. In
	 * future we should also provide the possibility to use arbitrary length
	 * random numbers and also using a seed for initialization.
	 * 
	 * @return The random number as byte array.
	 */
    public byte[] getRandomBytes();

    /**
	 * Generates and random number and returns it as byte array.
	 * 
	 * @param array
	 *            The byte array to which the random number is written.
	 */
    public void getRandomBytes(byte[] array);

    /**
	 * Generates and random number and returns it as long.
	 * 
	 * @return The random number as long.
	 */
    public long getRandomLong();

    /**
	 * Creates a hash from the provided data using the system standards hash
	 * algorithm. The hash algorithm can be specified through the configuration
	 * file and obtained through {@link SecurityConstants#hashAlgorithm}.
	 * 
	 * @param data
	 *            The data which should be hashed as byte array.
	 * @return The hash of the provided data as byte array.
	 * @throws SecurityServiceException
	 *             Thrown if an error occured.
	 */
    public byte[] createHash(byte[] data) throws SecurityServiceException;

    /**
	 * Creates a MAC for the provided data using the provided key. This method
	 * returns the MAC as byte array.
	 * 
	 * @param data
	 *            The data for which a MAC should be calculated.
	 * @param key
	 *            The key which should be used to calculate the MAC.
	 * @return The calculated MAC as byte array.
	 * @throws SecurityServiceException
	 *             Thrown if no MAC could be created.
	 */
    public byte[] createMAC(byte[] data, KeyBag key) throws SecurityServiceException;

    /**
	 * Verifies the provided MAC using the provided data and key. If the MAC
	 * couldn't be verified false is returned or an exception is thrown. If the
	 * MAC is valid true is returned.
	 * 
	 * @param data
	 *            The data which should be used to verify the provided MAC.
	 * @param mac
	 *            The MAC which should be verified.
	 * @param key
	 *            The key which should be used to verify the provided MAC.
	 * @return True if valid, otherwise false.
	 * @throws SecurityServiceException
	 *             Thrown if an error during the verification process occured.
	 */
    public boolean verifyMAC(byte[] data, byte[] mac, KeyBag key);

    /**
	 * Returns the IV which has been used during the last encryption process.
	 * 
	 * @return The initialization vector as byte array.
	 */
    public byte[] getCurrentIV();

    /**
	 * Creates a key element from the provided raw binary key data and returns
	 * it as {@link KeyBag}. The provided raw binary data must be created
	 * through the {@link #encodeKey(KeyBag)} function.
	 * 
	 * @param key
	 *            The binary data which constitutes a key.
	 * @return The {@link KeyBag} obtained from the provided binary data.
	 */
    public KeyBag createSecretKey(byte[] key);

    /**
	 * Creates a certificate from the provided raw binary certificate data and
	 * returns it as {@link CertificateBag}. The provided raw binary data must
	 * be created through the {@link #encodeCertificate(CertificateBag)}
	 * function.
	 * 
	 * @param certificate
	 *            The binary data which constitutes a certificate.
	 * @return The {@link CertificateBag} obtained from the provided binary
	 *         data.
	 * @throws SecurityServiceException
	 *             Thrown if no certificate could be created.
	 */
    public CertificateBag createCertificate(byte[] certificate) throws SecurityServiceException;

    /**
	 * Creates a {@link KeyBag} which will be used as routing session key. This
	 * key is then used to calculate a MAC for each message sent inside a
	 * specific SEPP network. If the message intended for a specific SEPP
	 * network doesn't contain a MAC created using the routing session key the
	 * message is rejected.
	 * 
	 * @return The created {@link KeyBag}.
	 * @throws SecurityServiceException
	 *             Thrown if no secret key could be created.
	 */
    public KeyBag createSessionKey() throws SecurityServiceException;

    /**
	 * Returns the provided key in its encoded version as byte array. This is
	 * usually used for getting the certificate into a format which can be
	 * transmitted over the network.
	 * 
	 * @param key
	 *            The key which should be encoded.
	 * @return The encoded key as byte array.
	 */
    public byte[] encodeKey(KeyBag key);

    /**
	 * Returns the provided certificate in its encoded version as byte array.
	 * This is usually used for getting the certificate into a format which can
	 * be transmitted over the network.
	 * 
	 * @param certificate
	 *            The certificate which should be encoded.
	 * @return The encoded certificate as byte array.
	 */
    public byte[] encodeCertificate(CertificateBag certificate);
}
