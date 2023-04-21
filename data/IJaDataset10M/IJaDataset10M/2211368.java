package org.apache.zookeeper.inspector.encryption;

/**
 *
 */
public class BasicDataEncryptionManager implements DataEncryptionManager {

    public String decryptData(byte[] encrypted) throws Exception {
        return new String(encrypted);
    }

    public byte[] encryptData(String data) throws Exception {
        if (data == null) {
            return new byte[0];
        }
        return data.getBytes();
    }
}
