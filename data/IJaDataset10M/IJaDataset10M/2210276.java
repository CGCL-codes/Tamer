package mx.com.nyak.base.util;

import java.util.Random;

public class Randomizer {

    public static String generateCode() {
        Random r = new Random();
        return Long.toString(Math.abs(r.nextLong()), 36).toUpperCase();
    }
}
