package org.jcvi.util;

import java.util.Collection;

/**
 * @author dkatzel
 *
 *
 */
public final class GenericUtil {

    public static <T> T[] toArray(Class<T[]> clazz, Collection<T> collection) {
        @SuppressWarnings("unchecked") T[] array = (T[]) java.lang.reflect.Array.newInstance(clazz.getComponentType(), collection.size());
        int index = 0;
        for (T element : collection) {
            array[index++] = element;
        }
        return array;
    }
}
