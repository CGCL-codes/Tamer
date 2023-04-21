package net.sf.securejdms.common.core.entities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import net.sf.securejdms.common.core.CommonCore;
import net.sf.securejdms.common.core.extensionpoints.CryptographicException;
import org.apache.log4j.Logger;

public class SecurityTools {

    private static final Logger log = Logger.getLogger(SecurityTools.class);

    /**
	 * Encrypt serializable object into byte array
	 * 
	 * @param serializable object to encrypt
	 * @param keySignature signature of a key to use
	 * @return encrypted serialized <code>serializable</code> object
	 * @throws CryptographicException if a cryptographic operation fails
	 */
    public static byte[] encrypt(Serializable serializable, byte[] keySignature) throws CryptographicException {
        if (serializable == null) {
            return null;
        }
        if (keySignature == null) {
            log.error("Can't encrypt object: keySignature wasn't provided");
            throw new RuntimeException("Can't encrypt object: keySignature wasn't provided");
        }
        if (serializable.getClass().isArray() && serializable.getClass().getComponentType() == Byte.TYPE) {
            return CommonCore.getUnifiedSecurityProviders().encrypt((byte[]) serializable, keySignature);
        }
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
            byte[] data = byteOutputStream.toByteArray();
            return CommonCore.getUnifiedSecurityProviders().encrypt(data, keySignature);
        } catch (IOException e) {
            log.error("Unexpected exception", e);
            return null;
        }
    }

    /**
	 * Decrypt serializable object from <code>cryptedData</code> byte array.
	 * 
	 * @param <T> class object of the expected return type
	 * @param resultType the expected return type
	 * @param encryptedData encrypted serialized <code>serializable</code> object
	 * @param keySignature signature of a key to use
	 * @return object
	 * @throws CryptographicException if key not found or some cryptographic operation fails
	 */
    @SuppressWarnings("unchecked")
    public static <T> T decrypt(Class<T> resultType, byte[] encryptedData, byte[] keySignature) throws CryptographicException {
        if (encryptedData == null) {
            return null;
        }
        if (keySignature == null) {
            log.error("keySignature == null");
            throw new IllegalArgumentException("keySignature == null");
        }
        if (resultType.isArray() && resultType.getComponentType() == Byte.TYPE) {
            T result = (T) CommonCore.getUnifiedSecurityProviders().decrypt(encryptedData, keySignature);
            if (result == null) {
                log.error("Can't find key to decrypt data");
                throw new CryptographicException("Can't find key to decrypt data");
            }
            return result;
        }
        try {
            byte[] data = CommonCore.getUnifiedSecurityProviders().decrypt(encryptedData, keySignature);
            if (data == null) {
                log.error("Can't find key to decrypt data");
                throw new CryptographicException("Can't find key to decrypt data");
            }
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
            Object result = objectInputStream.readObject();
            objectInputStream.close();
            return (T) result;
        } catch (CryptographicException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected exception", e);
            return null;
        }
    }
}
