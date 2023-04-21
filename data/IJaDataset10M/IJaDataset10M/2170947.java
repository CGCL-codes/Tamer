package dawfiltros;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;
import sun.misc.BASE64Encoder;

/**
 *
 * @author MAR
 */
public class DESEncryptor {

    /**
	 * Encripta los datos pasados por par?metro.
	 *
	 * Genera una clave a partir de la cadena proporcionada, usando MD5.
	 * Realiza la encriptaci?n usando el algoritmo DES.
	 */
    public static byte[] encrypt(String passphrase, byte[] data) throws Exception {
        byte[] dataTemp;
        try {
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passphrase.getBytes());
            DESKeySpec key = new DESKeySpec(md.digest());
            SecretKeySpec DESKey = new SecretKeySpec(key.getKey(), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, DESKey);
            dataTemp = cipher.doFinal(data);
        } catch (Exception e) {
            throw e;
        }
        return dataTemp;
    }

    /**
	 * Desencripta los datos pasados por par?metro.
	 *
	 * Genera una clave a partir de la cadena proporcionada, usando MD5.
	 * Realiza la desencriptaci?n usando el algoritmo DES.
	 */
    public static byte[] decrypt(String passphrase, byte[] data) throws Exception {
        byte[] dataTemp;
        try {
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passphrase.getBytes());
            DESKeySpec key = new DESKeySpec(md.digest());
            SecretKeySpec DESKey = new SecretKeySpec(key.getKey(), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, DESKey);
            dataTemp = cipher.doFinal(data);
        } catch (Exception e) {
            throw e;
        }
        return dataTemp;
    }
}
