package net.sf.mytoolbox.lang;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Utility class for collections manipulation. <br/>
 * @author ggrussenmeyer
 */
public class CollectionUtils {

    /**
     * Converts the given array of long to a set of Long. <br/>
     * @param array
     * @return
     */
    public static Set<Long> asSet(long[] array) {
        Set<Long> set = new TreeSet<Long>();
        if (array != null) {
            for (Long value : array) {
                set.add(value);
            }
        }
        return set;
    }

    /**
     * Converts the given collection into a set. <br/>
     * @param <T>
     * @param set
     * @return
     */
    public static <T> Set<T> asSet(Collection<T> set) {
        return (set == null ? null : new LinkedHashSet<T>(set));
    }
}
