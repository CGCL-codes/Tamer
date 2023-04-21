package org.gapjump.security.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import org.gapjump.security.keystone.model.CachedEntryData;

/**
 * Providing static utility methods for handling KeyStore data
 * 
 * @author Will Gage <will at gapjump dot oh ahr gee>
 * 
 */
public class KeyStoreUtilities {

    private static final int BLOCK_SIZE = 8;

    /**
	 * Create an empty KeyStore given the provider and algorithm.  KeyStore is ready to be used.
	 * @param provider
	 * @param algorithm
	 * @return
	 * @throws KeyStoreException
	 */
    public static KeyStore createKeyStore(String provider, String algorithm) throws KeyStoreException {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(algorithm, provider);
            ks.load(null);
        } catch (IOException e) {
            throw new KeyStoreException(e);
        } catch (java.security.cert.CertificateException e) {
            throw new KeyStoreException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new KeyStoreException(e);
        } catch (NoSuchProviderException e) {
            throw new KeyStoreException(e);
        }
        return ks;
    }

    /**
	 * Create a KeyStore from a file given the provider, algorithm, fileName and password.  KeyStore is ready to be used when returned.
	 * @param provider
	 * @param algorithm
	 * @param fileName
	 * @param password
	 * @return
	 * @throws KeyStoreException
	 */
    public static KeyStore createKeyStore(String provider, String algorithm, String fileName, String password) throws KeyStoreException {
        KeyStore ks = null;
        File ksFile = null;
        FileInputStream fis = null;
        try {
            ksFile = new File(fileName);
            ks = KeyStore.getInstance(algorithm, provider);
            fis = new FileInputStream(ksFile);
            ks.load(fis, password.toCharArray());
        } catch (IOException e) {
            throw new KeyStoreException(e);
        } catch (java.security.cert.CertificateException e) {
            throw new KeyStoreException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new KeyStoreException(e);
        } catch (NoSuchProviderException e) {
            throw new KeyStoreException(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    fis = null;
                } catch (Exception e) {
                }
            }
        }
        return ks;
    }

    /**
	 * Copy entry data into new KeyStore.Entry object.
	 * 
	 * @param entry
	 * @return new KeyStore.Entry object
	 */
    public static KeyStore.Entry copyEntry(KeyStore.Entry entry) {
        if (entry == null) {
            return null;
        }
        KeyStore.Entry copy = null;
        if (entry instanceof KeyStore.SecretKeyEntry) {
            copy = new KeyStore.SecretKeyEntry(((KeyStore.SecretKeyEntry) entry).getSecretKey());
        } else if (entry instanceof KeyStore.PrivateKeyEntry) {
            KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) entry;
            copy = new KeyStore.PrivateKeyEntry(pkEntry.getPrivateKey(), pkEntry.getCertificateChain());
        } else if (entry instanceof KeyStore.TrustedCertificateEntry) {
            copy = new KeyStore.TrustedCertificateEntry(((KeyStore.TrustedCertificateEntry) entry).getTrustedCertificate());
        } else {
            throw new IllegalArgumentException("Unsupported KeyStore.Entry type");
        }
        return copy;
    }

    /**
	 * Copy parameter data into new KeyStore.ProtectionParameter object.
	 * 
	 * @param param
	 *            must be a KeyStore.ProtectionParameter
	 * @return new KeyStore.ProtectionParameter object
	 */
    public static KeyStore.ProtectionParameter copyProtectionParam(KeyStore.ProtectionParameter param) {
        if (param == null) {
            return null;
        }
        KeyStore.ProtectionParameter copy = null;
        if (param instanceof KeyStore.PasswordProtection) {
            copy = new KeyStore.PasswordProtection(((KeyStore.PasswordProtection) param).getPassword());
        } else {
            throw new IllegalArgumentException("Unsupported KeyStore.ProtectionParameter type");
        }
        return copy;
    }

    /**
	 * Test to see if a given password protects this KeyStore.Entry in the given
	 * keyStore
	 * 
	 * @param keyStore
	 * @param alias
	 * @param passCandidate
	 * @return
	 * @throws KeyStoreException
	 */
    public static boolean isPasswordProtectsEntry(KeyStore keyStore, String alias, char[] passCandidate) throws KeyStoreException {
        boolean gotEntry = false;
        KeyStore.PasswordProtection passParam = (passCandidate != null) ? new KeyStore.PasswordProtection(passCandidate) : null;
        try {
            keyStore.getEntry(alias, passParam);
            gotEntry = true;
        } catch (UnrecoverableEntryException e) {
            gotEntry = false;
        } catch (NoSuchAlgorithmException e) {
            throw new KeyStoreException(e);
        }
        return gotEntry;
    }

    /**
	 * Change the password protecting the KeyStore.Entry stored as alias in
	 * KeyStore keyStore.
	 * 
	 * @param keyStore
	 * @param alias
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * @throws KeyStoreException
	 */
    public static boolean changeEntryPassword(KeyStore keyStore, String alias, char[] oldPassword, char[] newPassword) throws KeyStoreException {
        if (!isPasswordProtectsEntry(keyStore, alias, oldPassword)) {
            return false;
        }
        if (isPasswordEquals(oldPassword, newPassword)) {
            return false;
        }
        KeyStore.PasswordProtection oparam = new KeyStore.PasswordProtection(oldPassword);
        KeyStore.PasswordProtection nparam = new KeyStore.PasswordProtection(newPassword);
        boolean changed = false;
        try {
            KeyStore.Entry entry = keyStore.getEntry(alias, oparam);
            keyStore.setEntry(alias, entry, nparam);
            changed = true;
        } catch (UnrecoverableEntryException e) {
            throw new KeyStoreException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new KeyStoreException(e);
        }
        return changed;
    }

    public static boolean renameEntry(KeyStore keyStore, String oldAlias, String newAlias, char[] password) throws KeyStoreException, UnrecoverableEntryException {
        if (oldAlias.equals(newAlias)) {
            return false;
        }
        if (!keyStore.containsAlias(oldAlias)) {
            throw new IllegalArgumentException("KeyStore does not contain alias " + oldAlias);
        }
        KeyStore.PasswordProtection pparam = password != null ? new KeyStore.PasswordProtection(password) : null;
        try {
            KeyStore.Entry entry = keyStore.getEntry(oldAlias, pparam);
            if (entry instanceof KeyStore.SecretKeyEntry) {
                keyStore.deleteEntry(oldAlias);
                KeyStore.SecretKeyEntry ske = (KeyStore.SecretKeyEntry) entry;
                keyStore.setEntry(newAlias, ske, pparam);
            } else if (entry instanceof KeyStore.PrivateKeyEntry) {
                KeyStore.PrivateKeyEntry pke = (KeyStore.PrivateKeyEntry) entry;
                keyStore.setEntry(newAlias, pke, pparam);
            } else if (entry instanceof KeyStore.TrustedCertificateEntry) {
                KeyStore.TrustedCertificateEntry tce = (KeyStore.TrustedCertificateEntry) entry;
                keyStore.setEntry(newAlias, tce, pparam);
            }
            return true;
        } catch (NoSuchAlgorithmException e) {
            throw new KeyStoreException(e);
        }
    }

    public static int rawEncodedBitLength(byte[] encoded) {
        if (encoded == null || encoded.length == 0) {
            return 0;
        }
        int bitLen = encoded.length * 8;
        return bitLen;
    }

    private static boolean isPasswordEquals(char[] password1, char[] password2) {
        if (password1 == null && password2 == null) {
            return true;
        }
        if (password1 == null || password2 == null) {
            return false;
        }
        if (password1.length != password2.length) {
            return false;
        }
        for (int i = 0; i < password1.length; i++) {
            if (password1[i] != password2[i]) {
                return false;
            }
        }
        return true;
    }
}
