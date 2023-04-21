package net.sf.orcc.util;

import java.util.Iterator;

/**
 * This class defines various utility functions that deal with collections.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class CollectionsUtil {

    /**
	 * Returns a string that contains all objects separated with the given
	 * separator.
	 * 
	 * @param objects
	 *            an iterable of objects
	 * @param sep
	 *            a separator string
	 * @return a string that contains all objects separated with the given
	 *         separator
	 */
    public static String toString(Iterable<? extends Object> objects, String sep) {
        StringBuilder builder = new StringBuilder();
        Iterator<? extends Object> it = objects.iterator();
        if (it.hasNext()) {
            builder.append(it.next());
            while (it.hasNext()) {
                builder.append(sep);
                builder.append(it.next());
            }
        }
        return builder.toString();
    }
}
