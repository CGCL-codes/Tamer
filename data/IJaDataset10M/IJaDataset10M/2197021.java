package org.limewire.mojito.util;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.limewire.mojito.settings.KademliaSettings;

/**
 * Miscellaneous utilities for Collections
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * Converts the given Collection to a Set (if it isn't
     * already a Set)
     */
    public static <T> Set<T> toSet(Collection<T> c) {
        if (c instanceof Set) {
            return (Set<T>) c;
        }
        return new LinkedHashSet<T>(c);
    }

    /**
     * Converts the given Collection to a List (if it isn't
     * already a List)
     */
    public static <T> List<T> toList(Collection<T> c) {
        if (c instanceof List) {
            return (List<T>) c;
        }
        return new ArrayList<T>(c);
    }

    /**
     * Returns the given Collection as formatted String
     */
    public static String toString(Collection<?> c) {
        StringBuilder buffer = new StringBuilder();
        Iterator it = c.iterator();
        for (int i = 0; it.hasNext(); i++) {
            buffer.append(i).append(": ").append(it.next()).append('\n');
        }
        if (buffer.length() > 1) {
            buffer.setLength(buffer.length() - 1);
        }
        return buffer.toString();
    }

    /**
     * Returns an iterator that returns up to max number of elements from the
     * Collection
     */
    private static <T> Iterator<T> iterator(final Collection<T> c, final int count) {
        return new Iterator<T>() {

            private final Iterator<T> it = c.iterator();

            private int item = 0;

            public boolean hasNext() {
                if (item >= count) {
                    return false;
                }
                return it.hasNext();
            }

            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                item++;
                return it.next();
            }

            public void remove() {
                it.remove();
                item--;
            }
        };
    }

    /**
     * Returns a sub-view of the given Collection that will only return 
     * the first K elements
     */
    public static <T> Collection<T> getCollection(Collection<T> c) {
        return getCollection(c, KademliaSettings.REPLICATION_PARAMETER.getValue());
    }

    /**
     * Returns a sub-view of the given Collection that will only return 
     * the first count elements
     */
    public static <T> Collection<T> getCollection(final Collection<T> c, final int count) {
        return new AbstractCollection<T>() {

            public Iterator<T> iterator() {
                return CollectionUtils.iterator(c, count);
            }

            public int size() {
                return Math.min(c.size(), count);
            }
        };
    }
}
