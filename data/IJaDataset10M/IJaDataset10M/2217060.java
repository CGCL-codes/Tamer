package kiff.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Password Utility functions.
 * @author Adam
 * @version $Id: PasswordUtils.java 62 2008-10-31 04:51:50Z a.ruggles $
 * 
 * Created on Sep 21, 2008 at 1:44:44 PM 
 */
public final class PasswordUtils {

    /**
	 * Encrypts a plain text password.
	 * @param password The plain text password to encrypt.
	 * @return The encrypted value of the password.
	 */
    public static String encrypt(final String password) {
        return DigestUtils.shaHex(password);
    }

    /**
	 * Evaluates the plain text password and seed value and compares it to the encrypted value.
	 * @param plain The plain text password.
	 * @param seed The pseudo-random seed value.
	 * @param encrypted The encrypted password to compare.
	 * @return True if the encrypted password matches the plain text password and seed.
	 */
    public static boolean evaluate(final String plain, final long seed, final String encrypted) {
        if (plain == null || encrypted == null || plain.length() == 0 || encrypted.length() == 0) {
            return false;
        }
        return encrypt(salt(seed) + plain).equals(encrypted);
    }

    /**
	 * Generates an encrypted password from the plain text password and a seed value.
	 * @param plain The plain text password.
	 * @param seed The pseudo-random seed value.
	 * @return an encrypted password.
	 * @throws SecurityException If the plain text password is invalid.
	 */
    public static String generate(final String plain, final long seed) throws SecurityException {
        if (plain == null || plain.length() == 0) {
            throw new SecurityException("invalid plain text password");
        }
        return encrypt(salt(seed) + plain);
    }

    /**
	 * Generates a pseudo-random long value.
	 * @return a pseudo-random long value.
	 */
    public static long randomLong() {
        Random random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            random = new Random();
        }
        return random.nextLong();
    }

    /**
	 * Generates a pseudo-random salt with a seed value.
	 * @param seed A seed value.
	 * @return A pseudo-random string that can be used as salt for password generation.
	 */
    public static String salt(final long seed) {
        Long randomValue = null;
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(seed);
            randomValue = random.nextLong();
        } catch (NoSuchAlgorithmException e) {
            Random random = new Random();
            random.setSeed(seed);
            randomValue = random.nextLong();
        }
        return Long.toHexString(randomValue);
    }

    /**
	 * Converts a String value into a long value, useful as a seed value.
	 * @param value The string value to convert.
	 * @return A long value.
	 */
    public static long stringToLong(final String value) {
        byte[] values = null;
        try {
            values = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            values = value.getBytes();
        }
        long returnValue = 0;
        for (byte v : values) {
            returnValue += (v << 2);
        }
        return returnValue;
    }

    /**
	 * Private Constructor.
	 */
    private PasswordUtils() {
    }
}
