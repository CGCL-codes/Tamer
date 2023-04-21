package util;

import java.util.List;
import java.util.Random;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 *
 * Utilities for lists.
 */
public class ListUtil {

    /**
	 * @param <T> the type of the list (implicit).
	 * @param list the list to shuffle.
	 * @param nswaps the number of swaps to perform.
	 */
    public static <T> void shuffle(List<T> list, int nswaps) {
        int size = list.size();
        Random random = new Random();
        for (int i = 0; i < nswaps; i++) {
            int a = random.nextInt(size);
            int b = random.nextInt(size);
            T t = list.get(a);
            list.set(a, list.get(b));
            list.set(b, t);
        }
    }
}
