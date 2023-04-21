package es.manuel.maa.util;

import java.security.SecureRandom;

public final class PasswordUtil {

    private static final String SECURE_ALGORITHM = "SHA1PRNG";

    private static SecureRandom SECURE_RANDOM;

    static {
        try {
            SECURE_RANDOM = SecureRandom.getInstance(SECURE_ALGORITHM);
            SECURE_RANDOM.setSeed(System.identityHashCode(System.currentTimeMillis()));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static char[] CHARACTERS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    private PasswordUtil() {
    }

    public static String getPassword(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(CHARACTERS[SECURE_RANDOM.nextInt(CHARACTERS.length)]);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(getPassword(10));
        }
    }
}
