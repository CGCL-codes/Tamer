package com.ezware.oxbow.swingbits.graphics;

import java.util.Collection;
import java.util.TreeSet;

public class CollectionUtils {

    /**
	 * Checks if collection is empty. Null references are considered to be empty collections.
	 * @param c
	 * @return true if collection is empty
	 */
    public static final boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    /**
	 * Tries to sort collection. All elements have to implement Comparable interface and
	 * be mutually comparable 
	 * @param <T>
	 * @param any collection
	 * @return new sorted collection if succeeded otherwise same collection
	 */
    public static <T> Collection<T> trySort(Collection<T> c) {
        try {
            return new TreeSet<T>(c);
        } catch (Throwable ex) {
        }
        return c;
    }
}
