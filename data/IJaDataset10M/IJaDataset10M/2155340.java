package org.eclipse.equinox.internal.security.storage;

import org.eclipse.equinox.internal.security.auth.nls.SecAuthMessages;
import org.eclipse.equinox.security.storage.StorageException;

public class CryptoData {

    private static final char MODULE_ID_SEPARATOR = '\t';

    /**
	 * Separates salt from the data; this must not be a valid Base64 character.
	 */
    private static final char SALT_SEPARATOR = ',';

    private final String moduleID;

    private final byte[] salt;

    private final byte[] encryptedData;

    public CryptoData(String moduleID, byte[] salt, byte[] data) {
        this.moduleID = moduleID;
        this.salt = salt;
        this.encryptedData = data;
    }

    public String getModuleID() {
        return moduleID;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getData() {
        return encryptedData;
    }

    public CryptoData(String data) throws StorageException {
        int pos = data.indexOf(MODULE_ID_SEPARATOR);
        String encrypted;
        if (pos == -1) {
            throw new StorageException(StorageException.DECRYPTION_ERROR, SecAuthMessages.invalidEntryFormat);
        } else if (pos == 0) {
            moduleID = null;
            encrypted = data.substring(1);
        } else {
            moduleID = data.substring(0, pos);
            encrypted = data.substring(pos + 1);
        }
        int saltPos = encrypted.indexOf(SALT_SEPARATOR);
        if (saltPos != -1) {
            salt = Base64.decode(encrypted.substring(0, saltPos));
            encryptedData = Base64.decode(encrypted.substring(saltPos + 1));
        } else {
            if (encrypted.length() != 0) throw new StorageException(StorageException.DECRYPTION_ERROR, SecAuthMessages.invalidEntryFormat);
            salt = null;
            encryptedData = null;
        }
    }

    public String toString() {
        StringBuffer encryptedText = (moduleID == null) ? new StringBuffer() : new StringBuffer(moduleID);
        encryptedText.append(MODULE_ID_SEPARATOR);
        if (salt != null) encryptedText.append(Base64.encode(salt));
        if (encryptedData != null) {
            encryptedText.append(SALT_SEPARATOR);
            encryptedText.append(Base64.encode(encryptedData));
        }
        return encryptedText.toString();
    }
}
