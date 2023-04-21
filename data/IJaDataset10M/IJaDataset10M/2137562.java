package org.spockframework.util;

import java.util.*;

public abstract class CollectionUtil {

    public static <E, F> ArrayList<F> filterMap(Collection<E> collection, IFunction<E, F> function) {
        ArrayList<F> result = new ArrayList<F>(collection.size());
        for (E elem : collection) {
            F resultElem = function.apply(elem);
            if (resultElem != null) result.add(resultElem);
        }
        return result;
    }

    /**
   * (Partial) replacement for Arrays.copyOfRange, which is only available in JDK6.
   */
    public static Object[] copyArray(Object[] array, int from, int to) {
        Object[] result = new Object[to - from];
        System.arraycopy(array, from, result, 0, to - from);
        return result;
    }

    @Nullable
    public static <T> T getLastElement(List<T> list) {
        Assert.that(list.size() > 0);
        return list.get(list.size() - 1);
    }

    public static <T> void setLastElement(List<T> list, T elem) {
        Assert.that(list.size() > 0);
        list.set(list.size() - 1, elem);
    }

    public static <T> void addLastElement(List<T> list, T element) {
        list.add(list.size(), element);
    }

    public static <T> Iterable<T> reverse(final List<T> list) {
        return new Iterable<T>() {

            public Iterator<T> iterator() {
                final ListIterator<T> listIterator = list.listIterator();
                return new Iterator<T>() {

                    public boolean hasNext() {
                        return listIterator.hasPrevious();
                    }

                    public T next() {
                        return listIterator.previous();
                    }

                    public void remove() {
                        listIterator.remove();
                    }
                };
            }
        };
    }

    public static <T> Set<T> asSet(T[] values) {
        return new HashSet<T>(Arrays.asList(values));
    }
}
