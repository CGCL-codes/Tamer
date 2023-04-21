package org.ws4d.java.security.key;

import org.ws4d.java.logging.Log;

/**
 * Represents a KeyPair, which is stored in the KeyStorage.
 */
public class StoredKeyPair extends AbstractStoredKeyPair {

    /**
	 * Initializes a key pair in the key storage.
	 * 
	 * @param name The of the key pair in the key storage.
	 */
    public StoredKeyPair(String name) {
        super(name);
    }

    /**
	 * Returns the name of the key pair.
	 * 
	 * @return The name of the key pair in the key storage.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Looks up the concrete key pair in the key storage. And returns its
	 * decryption key.
	 */
    public byte[] getDecryptionKey() {
        AbstractKeyPair keyPair = KeyStorage.get(getName());
        if (keyPair != null) return keyPair.getDecryptionKey();
        Log.warn("StoredKeyPair.getDecryptionKey: Named Key couldn't be found in the KeyStorage.");
        return null;
    }

    /**
	 * Looks up the concrete key pair in the key storage. And returns its
	 * encryption key.
	 */
    public byte[] getEncryptionKey() {
        AbstractKeyPair keyPair = KeyStorage.get(getName());
        if (keyPair != null) return keyPair.getEncryptionKey();
        Log.warn("StoredKeyPair.getDecryptionKey: Named Key couldn't be found in the KeyStorage.");
        return null;
    }
}
