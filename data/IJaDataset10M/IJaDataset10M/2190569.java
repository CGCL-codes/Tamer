package nl.beesting.beangenerator.util;

import java.util.Random;

public class RandomUtil {

    static Random random = new Random();

    /**
	 * Instantiates random long between given min and max
	 * 
	 * @param min
	 *            the minimal value (including)
	 * @param max
	 *            the maximal value (excluding)
     * @return
	 */
    public static long initRandomLongBetween(long min, long max) {
        long rl = random.nextLong();
        if (rl >= min && rl < max) {
            return rl;
        } else {
            long between = Math.max(max - min, 1);
            return min + (Math.abs(rl % between));
        }
    }

    /**
	 * Instantiates random long between given min and max
	 * 
	 * @param min
	 *            the minimal value
	 * @param max
	 *            the maximal value
	 * @return the random long created
	 */
    public static int initRandomIntBetween(int min, int max) {
        int ri = random.nextInt(max - min);
        return ri + min;
    }

    /**
	 * Instantiates random double given min and max
	 * 
	 * @param min
	 *            the minimal value
	 * @param max
	 *            the maximal value
	 * @return the random double created
	 */
    public static double initRandomDoubleBetween(double min, double max) {
        double randomDouble = Math.random();
        double r = ((randomDouble) * (max - min));
        return r + min;
    }

    /**
	 * Instantiates random char
	 * 
	 * @todo enable mode generation e.g. alphanumeric / symbolic / etc
	 */
    public static char initRandomChar() {
        long positiveLong = initRandomLongBetween((long) Character.MIN_VALUE, (long) Character.MAX_VALUE);
        char c = (char) positiveLong;
        return c;
    }

    /**
	 * Method initRandomInt.
	 * 
	 * @return int
	 */
    public static int initRandomInt() {
        return random.nextInt();
    }

    /**
	 * Method initRandomLong.
	 * 
	 * @return long
	 */
    public static long initRandomLong() {
        return random.nextLong();
    }

    public static boolean initRandomBoolean() {
        return random.nextBoolean();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(initRandomLongBetween(0, 100));
        }
    }
}
