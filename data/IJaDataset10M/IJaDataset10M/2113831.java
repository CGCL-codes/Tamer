package org.hamcrestcollections;

import org.hamcrest.Matcher;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Newman (sam-newman@magpiebrain.com)
 */
public class RejectMatcher {

    /**
     * Returns a list of all the elements in {@code items} that do not match a {@link Matcher}
     *
     * @param items   The items to analyze
     * @param matcher The matcher to run on the items
     * @return A collection of items that don't match the matcher
     */
    public static <T> Iterable<T> reject(Iterable<T> items, Matcher<T> matcher) {
        List<T> matching = new ArrayList<T>();
        for (T item : items) {
            if (!matcher.matches(item)) {
                matching.add(item);
            }
        }
        return matching;
    }
}
