package net.sf.frozen.utils.cryptography;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for Cryptography.
 * 
 * @author Inácio Ferrarini (inacioferrarini at users.sourceforge.net)
 */
public class Cryptographer {

    /**
	 * Returns a SHA-1 key generated from informed <tt>value</tt>
	 * 
	 * @param value
	 *           the input value for cryptography
	 * @return java.lang.String the SHA-1 key
	 */
    public static String hashSHA1(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(value.getBytes());
            BigInteger hash = new BigInteger(1, digest.digest());
            return hash.toString(16);
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
}
