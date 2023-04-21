package org.gruposp2p.dnie.utils;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * @author Alejandro D. Garin <agarin@gmail.com>
 */
public class DESEncryptImpl implements HasEncryption {

    private String key = "";

    private static String algoritmo = "DES";

    private static String cipheralg = "DES/ECB/PKCS5Padding";

    public DESEncryptImpl(String key) {
        this.key = key;
    }

    public String encrypt(String source) {
        try {
            Key key = getKey();
            Cipher desCipher = Cipher.getInstance(cipheralg);
            desCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = source.getBytes();
            byte[] ciphertext = desCipher.doFinal(cleartext);
            return getString(ciphertext);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String generateKey() {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(algoritmo);
            SecretKey desKey = keygen.generateKey();
            byte[] bytes = desKey.getEncoded();
            setKey(getString(bytes));
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String source) {
        try {
            Key key = getKey();
            Cipher desCipher = Cipher.getInstance(cipheralg);
            byte[] ciphertext = getBytes(source);
            desCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cleartext = desCipher.doFinal(ciphertext);
            return new String(cleartext);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String showKey() {
        return key;
    }

    public Key getKey() {
        try {
            byte[] bytes = getBytes(key);
            DESKeySpec pass = new DESKeySpec(bytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(algoritmo);
            SecretKey s = skf.generateSecret(pass);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
   * Returns true if the specified text is encrypted, false otherwise
   */
    public boolean isEncrypted(String text) {
        if (text.indexOf('-') == -1) {
            return false;
        }
        StringTokenizer st = new StringTokenizer(text, "-", false);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.length() > 3) {
                return false;
            }
            for (int i = 0; i < token.length(); i++) {
                if (!Character.isDigit(token.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    private String getString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            sb.append((int) (0x00FF & b));
            if (i + 1 < bytes.length) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    private byte[] getBytes(String str) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StringTokenizer st = new StringTokenizer(str, "-", false);
        while (st.hasMoreTokens()) {
            int i = Integer.parseInt(st.nextToken());
            bos.write((byte) i);
        }
        return bos.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public void showProviders() {
        try {
            Provider[] providers = Security.getProviders();
            for (int i = 0; i < providers.length; i++) {
                System.out.println("Provider: " + providers[i].getName() + ", " + providers[i].getInfo());
                for (Iterator itr = providers[i].keySet().iterator(); itr.hasNext(); ) {
                    String key = (String) itr.next();
                    String value = (String) providers[i].get(key);
                    System.out.println("\t" + key + " = " + value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
