package drk;

import java.util.Random;

public final class RandGen {

    public static Random rand;

    static {
        rand = new Random(System.nanoTime());
    }

    public static double ranged(double l, double u) {
        return l + rand.nextDouble() * (u - l);
    }

    public static float rangef(float l, float u) {
        return l + rand.nextFloat() * (u - l);
    }
}
