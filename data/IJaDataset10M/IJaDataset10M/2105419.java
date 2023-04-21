package ao.util.math.rand;

import java.util.List;

/**
 * Random number unility class.
 */
public class Rand {

    private static final ThreadLocal<MersenneTwisterFast> RAND = new ThreadLocal<MersenneTwisterFast>() {

        protected synchronized MersenneTwisterFast initialValue() {
            return new MersenneTwisterFast(420L);
        }
    };

    private Rand() {
    }

    public static void randomize() {
        RAND.get().setSeed((long) (Math.random() * Long.MAX_VALUE));
    }

    public static MersenneTwisterFast fastLocal() {
        return RAND.get();
    }

    public static <T> T fromArray(T... arr) {
        if (arr == null || arr.length == 0) return null;
        return arr[Rand.nextInt(arr.length)];
    }

    public static <T> T fromList(List<T> list) {
        if (list == null || list.isEmpty()) return null;
        return list.get(Rand.nextInt(list.size()));
    }

    public static <T> T fromIterable(Iterable<T> list) {
        double max = Double.NEGATIVE_INFINITY;
        T maxItem = null;
        for (T item : list) {
            double score = Math.random();
            if (max < score) {
                max = score;
                maxItem = item;
            }
        }
        return maxItem;
    }

    public static double nextDouble() {
        return RAND.get().nextDouble();
    }

    public static double nextDouble(double upTo) {
        return upTo * RAND.get().nextDouble();
    }

    public static double nextDouble(double from, double upTo) {
        return from + nextDouble(upTo - from);
    }

    public static long nextLong() {
        return RAND.get().nextLong();
    }

    public static int nextInt(int n) {
        return (n > 0) ? RAND.get().nextInt(n) : (n < 0) ? -RAND.get().nextInt(-n) : 0;
    }

    public static int nextInt() {
        return RAND.get().nextInt();
    }

    public static double nextGaussian() {
        return RAND.get().nextGaussian();
    }

    public static float nextFloat() {
        return RAND.get().nextFloat();
    }

    public static void nextBytes(byte[] bytes) {
        RAND.get().nextBytes(bytes);
    }

    public static boolean nextBoolean() {
        return RAND.get().nextBoolean();
    }

    public static boolean nextBoolean(int onceEvery) {
        return RAND.get().nextBoolean(1.0 / (double) onceEvery);
    }

    public static boolean nextBoolean(double probability) {
        return RAND.get().nextBoolean(probability);
    }
}
