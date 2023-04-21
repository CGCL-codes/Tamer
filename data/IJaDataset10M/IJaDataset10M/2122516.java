package org.jiinxed.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;

/**
 * The <code>EncryptedObject</code> is used to store a {@link Serializable}
 * object in an encrypted form. This class is different from
 * {@link javax.crypto.SealedObject} in that it allows accessing the encrypted
 * object byte buffer.
 * @author S�bastien M�dan
 */
public class EncryptedObject implements Serializable {

    protected EncryptedObject() {
    }

    public EncryptedObject(Serializable serializableToEncrypt, Cipher cipher) throws IOException, GeneralSecurityException {
        ByteArrayOutputStream byteOut = null;
        ObjectOutputStream objectOut = null;
        try {
            byteOut = new ByteArrayOutputStream();
            objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(serializableToEncrypt);
            encryptedObjectArray = cipher.doFinal(byteOut.toByteArray());
            encodedAlgParams = cipher.getParameters().getEncoded();
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, null, e);
                }
            }
            if (byteOut != null) {
                try {
                    byteOut.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, null, e);
                }
            }
        }
    }

    public byte[] getEncryptedObject() {
        return encryptedObjectArray;
    }

    public Object getObject(Key key, String provider) throws IOException, ClassNotFoundException, GeneralSecurityException {
        ByteArrayInputStream byteIn = null;
        ObjectInputStream objectIn = null;
        try {
            final AlgorithmParameters algParams = AlgorithmParameters.getInstance(provider);
            algParams.init(encodedAlgParams);
            final Cipher cipher = Cipher.getInstance(provider);
            cipher.init(Cipher.DECRYPT_MODE, key, algParams);
            final byte[] decryptedObjectArray = cipher.doFinal(encryptedObjectArray);
            byteIn = new ByteArrayInputStream(decryptedObjectArray);
            objectIn = new ObjectInputStream(byteIn);
            return objectIn.readObject();
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, null, e);
                }
            }
            if (byteIn != null) {
                try {
                    byteIn.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, null, e);
                }
            }
        }
    }

    private byte[] encryptedObjectArray;

    private byte[] encodedAlgParams;

    private static transient Logger logger = Logger.getLogger("org.jiinxed.crypto");
}
