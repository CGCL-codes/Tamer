package org.waveprotocol.box.server.util;

import com.google.common.annotations.VisibleForTesting;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Produces sequence of pseudo-random id strings.
 * Thread-safe.
 *
 *
 */
public class RandomBase64Generator {

    /** The 64 valid web-safe characters. */
    @VisibleForTesting
    static final char[] WEB64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();

    private final Random random;

    /**
   * @param random Pseudo-random generator,
   *        use Random for speed, SecureRandom for cryptographic strength.
   */
    public RandomBase64Generator(Random random) {
        this.random = random;
    }

    /**
   * Default constructor using Random.
   */
    public RandomBase64Generator() {
        this(new SecureRandom());
    }

    /**
   * @param length The requested number of random base 64 characters.
   * @return string with length many random base 64 characters.
   */
    public String next(int length) {
        StringBuilder result = new StringBuilder(length);
        int bits = 0;
        int bitCount = 0;
        while (result.length() < length) {
            if (bitCount < 6) {
                bits = random.nextInt();
                bitCount = 32;
            }
            result.append(WEB64_ALPHABET[bits & 0x3F]);
            bits >>= 6;
            bitCount -= 6;
        }
        return result.toString();
    }
}
