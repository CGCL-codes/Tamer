package org.imogene.encryption;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author MEDES-IMPS
 *
 */
public interface EncryptionProvider {

    public String getName();

    public byte[] encrypt(byte[] decrypted);

    public byte[] decrypt(byte[] encrypted);

    public InputStream getDecryptedInputStream(InputStream in);

    public OutputStream getEncryptedOutputStream(OutputStream out);
}
