package de.objectcode.time4u.servernew.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Bodo Junglas
 * @created 7. M�rz 2002
 * @version $Id: PasswordEncoder.java,v 1.1 2003/03/14 23:37:19 junglas Exp $
 */
public class PasswordEncoder {

    private static final SecureRandom g_random = new SecureRandom();

    /**
   * Description of the Method
   * 
   * @param password Description of the Parameter
   * @return Description of the Return Value
   */
    public static String encrypt(final String password) {
        final byte[] salt = new byte[16];
        g_random.nextBytes(salt);
        return encrypt(password, "SSHA", salt);
    }

    /**
   * Description of the Method
   * 
   * @param password Description of the Parameter
   * @param algorithm Description of the Parameter
   * @param salt Description of the Parameter
   * @return Description of the Return Value
   */
    public static String encrypt(final String password, final String algorithm, final byte[] salt) {
        final StringBuffer buffer = new StringBuffer();
        MessageDigest digest = null;
        int size = 0;
        if ("CRYPT".equalsIgnoreCase(algorithm)) {
            throw new InternalError("Not implemented");
        } else if ("SHA".equalsIgnoreCase(algorithm) || "SSHA".equalsIgnoreCase(algorithm)) {
            size = 20;
            if (salt != null && salt.length > 0) {
                buffer.append("{SSHA}");
            } else {
                buffer.append("{SHA}");
            }
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (final NoSuchAlgorithmException e) {
                throw new InternalError("Invalid algorithm");
            }
        } else if ("MD5".equalsIgnoreCase(algorithm) || "SMD5".equalsIgnoreCase(algorithm)) {
            size = 16;
            if (salt != null && salt.length > 0) {
                buffer.append("{SMD5}");
            } else {
                buffer.append("{MD5}");
            }
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (final NoSuchAlgorithmException e) {
                throw new InternalError("Invalid algorithm");
            }
        }
        int outSize = size;
        digest.reset();
        digest.update(password.getBytes());
        if (salt != null && salt.length > 0) {
            digest.update(salt);
            outSize += salt.length;
        }
        final byte[] out = new byte[outSize];
        System.arraycopy(digest.digest(), 0, out, 0, size);
        if (salt != null && salt.length > 0) {
            System.arraycopy(salt, 0, out, size, salt.length);
        }
        buffer.append(Base64.encode(out));
        return buffer.toString();
    }

    /**
   * Description of the Method
   * 
   * @param password Description of the Parameter
   * @param encryptedPassword Description of the Parameter
   * @return Description of the Return Value
   */
    public static boolean verify(final String password, final String encryptedPassword) {
        MessageDigest digest = null;
        int size = 0;
        String base64 = null;
        if (encryptedPassword.regionMatches(true, 0, "{CRYPT}", 0, 7)) {
            throw new InternalError("Not implemented");
        } else if (encryptedPassword.regionMatches(true, 0, "{SHA}", 0, 5)) {
            size = 20;
            base64 = encryptedPassword.substring(5);
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (final NoSuchAlgorithmException e) {
                throw new InternalError("Invalid algorithm");
            }
        } else if (encryptedPassword.regionMatches(true, 0, "{SSHA}", 0, 6)) {
            size = 20;
            base64 = encryptedPassword.substring(6);
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (final NoSuchAlgorithmException e) {
                throw new InternalError("Invalid algorithm");
            }
        } else if (encryptedPassword.regionMatches(true, 0, "{MD5}", 0, 5)) {
            size = 16;
            base64 = encryptedPassword.substring(5);
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (final NoSuchAlgorithmException e) {
                throw new InternalError("Invalid algorithm");
            }
        } else if (encryptedPassword.regionMatches(true, 0, "{SMD5}", 0, 6)) {
            size = 16;
            base64 = encryptedPassword.substring(6);
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (final NoSuchAlgorithmException e) {
                throw new InternalError("Invalid algorithm");
            }
        } else {
            return false;
        }
        final byte[] data = Base64.decode(base64.toCharArray());
        final byte[] orig = new byte[size];
        System.arraycopy(data, 0, orig, 0, size);
        digest.reset();
        digest.update(password.getBytes());
        if (data.length > size) {
            digest.update(data, size, data.length - size);
        }
        return MessageDigest.isEqual(digest.digest(), orig);
    }

    public static void main(final String[] args) {
        System.out.println(PasswordEncoder.encrypt("admin"));
    }
}
