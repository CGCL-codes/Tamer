package net.sourceforge.greenvine.generator.helper.java;

import java.util.Random;

public class RandomHelper {

    private static final Random random = new Random();

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    private static final String NUMBERS = "123456789";

    public static String getRandomNumericString(int length) {
        return generateString(NUMBERS, length);
    }

    public static String getRandomAlphabetString(int length) {
        return generateString(ALPHABET, length);
    }

    public static String getRandomBooleanString() {
        return Boolean.valueOf(random.nextBoolean()).toString();
    }

    private static String generateString(String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }
}
