package org.jenetics;

import java.util.Random;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: Arrays.java,v 1.1 2008-03-25 18:31:55 fwilhelm Exp $
 */
final class Arrays {

    private Arrays() {
    }

    static <T> void swap(final T[] array, final int i, final int j) {
        final T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
	 * Randomize the {@code array} with the given {@link Random} object. The used
	 * shuffling algorithm is from D. Knuth TAOCP, Seminumerical Algorithms,
	 * Third edition, page 142, Algorithm S (Selection sampling technique).
	 * 
	 * @param <T> the component type of the array to randomize.
	 * @param random the {@link Random} object to use for randomize.
	 * @param array the {@code array} to randomize.
	 */
    static <T> void randomize(final Random random, final T[] array) {
        for (int j = array.length - 1; j > 0; --j) {
            swap(array, j, random.nextInt(j + 1));
        }
    }
}
