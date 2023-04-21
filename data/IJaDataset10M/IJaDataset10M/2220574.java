package org.jenetics.util;

import java.util.Random;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: RandomUtils.java 1313 2012-02-18 15:19:47Z fwilhelm $
 */
public class RandomUtils {

    private RandomUtils() {
    }

    public static String nextString(final int length) {
        final Random random = RandomRegistry.getRandom();
        final StringBuilder chars = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            chars.append((char) random.nextInt(Short.MAX_VALUE));
        }
        return chars.toString();
    }
}
