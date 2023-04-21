package negocio.usuarios;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

public class Encriptador {

    public static String encriptar(String cleartext, String key) throws Exception {
        return crypt(cleartext, key, Cipher.ENCRYPT_MODE);
    }

    public static String desEncriptar(String ciphertext, String key) throws Exception {
        return crypt(ciphertext, key, Cipher.DECRYPT_MODE);
    }

    private static String crypt(String input, String key, int mode) throws Exception {
        Provider sunJce = new com.sun.crypto.provider.SunJCE();
        Security.addProvider(sunJce);
        KeyGenerator kgen = KeyGenerator.getInstance("Blowfish");
        kgen.init(448);
        SecretKey skey = kgen.generateKey();
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        cipher.init(mode, skeySpec);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes());
        CipherOutputStream cos = new CipherOutputStream(bos, cipher);
        int length = 0;
        byte[] buffer = new byte[8192];
        while ((length = bis.read(buffer)) != -1) {
            cos.write(buffer, 0, length);
        }
        bis.close();
        cos.close();
        return bos.toString();
    }
}
