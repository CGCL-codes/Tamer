package lug.util;

import java.util.Random;
import org.uncommons.maths.random.MersenneTwisterRNG;

/**
 * @author Luggy
 * Factory class to delivery a random number generator which is better than
 * java.util.Random but uses the same interface.
 */
public class RandomFactory {

    private static String RANDOM_CHARACTERS = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static Random instance = null;

    public static Random getRandom() {
        if (instance == null) {
            instance = new MersenneTwisterRNG();
        }
        return instance;
    }

    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM_CHARACTERS.charAt(getRandom().nextInt(RANDOM_CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String getRandomString() {
        return getRandomString(getRandom().nextInt(15) + 5);
    }
}
