package es.seastorm.stormblade.server.common;

import java.util.Random;

public class Dice {

    private static Random rnd = new Random();

    public static int dice(int max) {
        return rnd.nextInt(max) + 1;
    }

    public static int d10() {
        return rnd.nextInt(10) + 1;
    }
}
