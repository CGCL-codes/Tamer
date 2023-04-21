package spaken.util;

import java.util.Collection;

public class Iterables {

    public static <T> void addAll(Collection<T> collection, Iterable<? extends T> newElements) {
        for (T e : newElements) {
            collection.add(e);
        }
    }
}
