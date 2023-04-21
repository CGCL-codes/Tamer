package neoAtlantis.utilidades.accessController.cipher;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import neoAtlantis.utilidades.accessController.cipher.interfaces.DataCipher;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class CipherMd5Des implements DataCipher {

    /**
     * Versi�n del cifrador
     */
    public static final String VERSION = "1.0";

    private static byte[] SALT_BYTES = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

    private static int ITERATION_COUNT = 19;

    private String llave;

    /**
     * Constructor
     * @param llave Cadena que se utilizara como frase para cifrar y descifrar
     */
    public CipherMd5Des(String llave) {
        this.llave = llave;
    }

    /**
     * Cifra una Cadena
     * @param str Cadena a cifrar
     * @return Cadena cifrada
     * @throws Exception
     */
    public String cifra(String str) throws Exception {
        Cipher ecipher = null;
        KeySpec keySpec = new PBEKeySpec(this.llave.toCharArray(), SALT_BYTES, ITERATION_COUNT);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        ecipher = Cipher.getInstance(key.getAlgorithm());
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT_BYTES, ITERATION_COUNT);
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        byte[] enc = ecipher.doFinal(str.getBytes("UTF8"));
        return new sun.misc.BASE64Encoder().encode(enc);
    }

    /**
     * Descifra una cadena
     * @param str Cadena a descifrar
     * @return Cadena descifrada
     * @throws Exception
     */
    public String descifra(String str) throws Exception {
        Cipher dcipher = null;
        KeySpec keySpec = new PBEKeySpec(this.llave.toCharArray(), SALT_BYTES, ITERATION_COUNT);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        dcipher = Cipher.getInstance(key.getAlgorithm());
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT_BYTES, ITERATION_COUNT);
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
        return new String(dcipher.doFinal(dec), "UTF8");
    }

    /**
     * @param llave the llave to set
     */
    public void setLlave(String llave) {
        this.llave = llave;
    }
}
