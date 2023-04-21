package spc.gaius.actalis.util;

import java.io.InvalidObjectException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.util.encoders.Base64;

public class EncryptUtil {

    private static final SecureRandom random = new SecureRandom();

    public static final String STR_MAGIC = "Salted__";

    private static final String JALG = "DESede/CBC/PKCS5Padding";

    public static String encrypt(String pwd, String val) {
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        Cipher c;
        byte[] vc;
        try {
            c = initCipher(pwd, salt, Cipher.ENCRYPT_MODE);
            vc = c.doFinal(val.getBytes());
        } catch (Exception e) {
            throw new InvalidDataException(e);
        }
        byte[] vcc = new byte[vc.length + salt.length];
        System.arraycopy(salt, 0, vcc, 0, salt.length);
        System.arraycopy(vc, 0, vcc, salt.length, vc.length);
        byte[] vcb = Base64.encode(vcc);
        String ret = STR_MAGIC + new String(vcb);
        return ret;
    }

    public static String decrypt(String pwd, String val) {
        if (!val.startsWith(STR_MAGIC)) {
            return val;
        }
        byte[] sb = Base64.decode(val.substring(STR_MAGIC.length()));
        if (sb.length < 16) {
            return val;
        }
        byte[] salt = new byte[8];
        byte[] bytes = new byte[sb.length - 8];
        for (int i = 0; i < salt.length; i++) {
            salt[i] = sb[i];
        }
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = sb[i + salt.length];
        }
        Cipher c;
        try {
            c = initCipher(pwd, salt, Cipher.DECRYPT_MODE);
            sb = c.doFinal(bytes);
        } catch (Exception e) {
            throw new InvalidDataException(e);
        }
        String result = new String(sb);
        return result;
    }

    private static Cipher initCipher(String pwd, byte[] salt, int mode) throws Exception {
        MD5Digest md = new MD5Digest();
        md.update(pwd.getBytes(), 0, pwd.length());
        md.update(salt, 0, salt.length);
        byte[] dgst = new byte[16];
        md.doFinal(dgst, 0);
        md.reset();
        byte[] dgst1 = new byte[16];
        md.update(dgst, 0, dgst.length);
        md.update(pwd.getBytes(), 0, pwd.length());
        md.update(salt, 0, salt.length);
        md.doFinal(dgst1, 0);
        byte[] k = new byte[24];
        byte[] iv = new byte[8];
        System.arraycopy(dgst, 0, k, 0, 16);
        System.arraycopy(dgst1, 0, k, 16, 8);
        System.arraycopy(dgst1, 8, iv, 0, 8);
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
        SecretKey sk = new javax.crypto.spec.SecretKeySpec(k, "DESede");
        Cipher c = Cipher.getInstance(JALG);
        c.init(mode, sk, paramSpec);
        return c;
    }
}
