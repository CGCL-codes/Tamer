package net.sf.xmlunit.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class Linqy {

    /**
     * Turns the iterable into a list.
     */
    public static <E> List<E> asList(Iterable<E> i) {
        ArrayList<E> a = new ArrayList<E>();
        for (E e : i) {
            a.add(e);
        }
        return a;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <E> Iterable<E> cast(final Iterable i) {
        return map(i, new Mapper<Object, E>() {

            public E map(Object o) {
                return (E) o;
            }
        });
    }

    public static <E> Iterable<E> singleton(final E single) {
        return new Iterable<E>() {

            public Iterator<E> iterator() {
                return new OnceOnlyIterator<E>(single);
            }
        };
    }

    public static <F, T> Iterable<T> map(final Iterable<F> from, final Mapper<? super F, T> mapper) {
        return new Iterable<T>() {

            public Iterator<T> iterator() {
                return new MappingIterator<F, T>(from.iterator(), mapper);
            }
        };
    }

    public interface Mapper<F, T> {

        T map(F from);
    }

    public static <T> Iterable<T> filter(final Iterable<T> sequence, final Predicate<? super T> filter) {
        return new Iterable<T>() {

            public Iterator<T> iterator() {
                return new FilteringIterator<T>(sequence.iterator(), filter);
            }
        };
    }

    public interface Predicate<T> {

        boolean matches(T toTest);
    }

    @SuppressWarnings("rawtypes")
    public static int count(Iterable seq) {
        int c = 0;
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            c++;
            it.next();
        }
        return c;
    }

    private static class OnceOnlyIterator<E> implements Iterator<E> {

        private final E element;

        private boolean iterated = false;

        private OnceOnlyIterator(E element) {
            this.element = element;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public E next() {
            if (iterated) {
                throw new NoSuchElementException();
            }
            iterated = true;
            return element;
        }

        public boolean hasNext() {
            return !iterated;
        }
    }

    private static class MappingIterator<F, T> implements Iterator<T> {

        private final Iterator<F> i;

        private final Mapper<? super F, T> mapper;

        private MappingIterator(Iterator<F> i, Mapper<? super F, T> mapper) {
            this.i = i;
            this.mapper = mapper;
        }

        public void remove() {
            i.remove();
        }

        public T next() {
            return mapper.map(i.next());
        }

        public boolean hasNext() {
            return i.hasNext();
        }
    }

    private static class FilteringIterator<T> implements Iterator<T> {

        private final Iterator<T> i;

        private final Predicate<? super T> filter;

        private T lookAhead = null;

        private FilteringIterator(Iterator<T> i, Predicate<? super T> filter) {
            this.i = i;
            this.filter = filter;
        }

        public void remove() {
            i.remove();
        }

        public T next() {
            if (lookAhead == null) {
                throw new NoSuchElementException();
            }
            T next = lookAhead;
            lookAhead = null;
            return next;
        }

        public boolean hasNext() {
            while (lookAhead == null && i.hasNext()) {
                T next = i.next();
                if (filter.matches(next)) {
                    lookAhead = next;
                }
            }
            return lookAhead != null;
        }
    }
}
