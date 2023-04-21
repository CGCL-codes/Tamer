package tabdulin.util;

import java.util.Random;

public class RandomGenerator {

    private static Random random = new Random();

    public static int getInt() {
        return random.nextInt();
    }
}
