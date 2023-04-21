package eu.more.cryptographicservicecore.ciphers;

import java.math.BigInteger;
import java.security.SecureRandom;
import eu.more.cryptographicservicecore.commons.SecurityException;
import eu.more.cryptographicservicecore.hashes.HashCore;

public class PasswordsGenerator {

    private static int passwordLong = 8;

    /**
	 * Generates a random password of 256 bits
	 * 
	 * @return
	 * @throws SecurityException
	 */
    public static String generateRandomPassword() throws SecurityException {
        SecureRandom random = new SecureRandom();
        BigInteger internalInt = new BigInteger(passwordLong, random);
        return HashCore.hash(internalInt);
    }

    /**
	 * Generates a random password of 256 bits based on the given seed.
	 * 
	 * @return
	 * @throws SecurityException
	 */
    public static String generateRandomPassword(String seed) throws SecurityException {
        return HashCore.hash(generateRandomPassword() + seed);
    }
}
