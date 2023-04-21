package jaque;

import jaque.expressions.Functions;
import jaque.functions.Comparator;
import jaque.functions.Function;
import jaque.functions.math.BinaryOperator;
import jaque.misc.BaseIterator;
import static jaque.Query.*;
import static jaque.expressions.Functions.*;
import java.util.*;

/**
 * Provides a standard set of business rules to be applied with {@link Query}
 * class. The rules are immutable and thus can be instantiated once and used
 * everywhere. The rules are designed to be combined to create more
 * sophisticated ones, i.e. {@code or(greaterThan(...), lessThan(...))}.
 * 
 * @author <a href="mailto://object_streaming@googlegroups.com">Konstantin
 *         Triger</a>
 */
public final class Operation {

    private Operation() {
    }

    /**
	 * Determines whether all objects in a sequence satisfy a boolean condition.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @param predicate
	 *            a function representing a boolean condition.
	 * @return true if predicate returned true for all the objects in the
	 *         sequence or if the sequence is empty; otherwise false.
	 */
    public static <T> boolean all(final Function<Boolean, ? super T> predicate, final Iterable<T> source) {
        try {
            for (T t : source) if (!predicate.invoke(t)) return false;
            return true;
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * Determines whether the object sequence is empty.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @return true if the sequence does not contain objects; otherwise false.
	 */
    public static <T> boolean isEmpty(final Iterable<T> source) {
        return !source.iterator().hasNext();
    }

    /**
	 * Determines whether any object in a sequence satisfies a boolean
	 * condition.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @param predicate
	 *            a function representing a boolean condition.
	 * @return true if predicate returned true for any object in the sequence;
	 *         otherwise false.
	 */
    public static <T> boolean any(final Function<Boolean, ? super T> predicate, final Iterable<T> source) {
        return !isEmpty(where(predicate, source));
    }

    /**
	 * Counts objects in a sequence.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @return Integer representing the number of objects in the sequence.
	 */
    public static <T> int count(final Iterable<T> source) {
        int c = 0;
        for (Iterator<T> iter = source.iterator(); iter.hasNext(); iter.next(), c++) ;
        return c;
    }

    /**
	 * Counts objects satisfying a boolean condition in a sequence.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @param predicate
	 *            a function representing a boolean condition.
	 * @return Integer representing the number of objects satisfying a boolean
	 *         condition in the sequence.
	 */
    public static <T> int count(final Function<Boolean, ? super T> predicate, final Iterable<T> source) {
        return count(where(predicate, source));
    }

    /**
	 * Counts objects in a sequence.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @return Long representing the number of objects in the sequence.
	 */
    public static <T> long longCount(final Iterable<T> source) {
        long c = 0;
        for (Iterator<T> iter = source.iterator(); iter.hasNext(); iter.next(), c++) ;
        return c;
    }

    /**
	 * Counts objects satisfying a boolean condition in a sequence.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @param predicate
	 *            a function representing a boolean condition.
	 * @return Long representing the number of objects satisfying a boolean
	 *         condition in the sequence.
	 */
    public static <T> long longCount(final Function<Boolean, ? super T> predicate, final Iterable<T> source) {
        return longCount(where(predicate, source));
    }

    /**
	 * Returns an object at specified position in a sequence.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @param position
	 *            position of object in the sequence.
	 * @return an object at specified position in a sequence.
	 */
    public static <T> T elementAt(int position, final Iterable<T> source) {
        if (source instanceof List) return ((List<T>) source).get(position);
        if (position < 0) throw new IndexOutOfBoundsException();
        Iterator<T> iter = source.iterator();
        T t;
        do {
            if (!iter.hasNext()) throw new IndexOutOfBoundsException();
            t = iter.next();
        } while (position-- > 0);
        return t;
    }

    /**
	 * Returns an object at specified position in a sequence satisfying a
	 * boolean condition.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @param position
	 *            position of object in the sequence.
	 * @param predicate
	 *            a function representing a boolean condition.
	 * @return an object at specified position in a sequence satisfying a
	 *         boolean condition.
	 */
    public static <T> T elementAt(int position, final Function<Boolean, ? super T> predicate, final Iterable<T> source) {
        return elementAt(position, where(predicate, source));
    }

    /**
	 * Returns the first object in the sequence.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @return a first object.
	 */
    public static <T> T first(final Iterable<T> source) {
        return elementAt(0, source);
    }

    /**
	 * Returns the first object in the sequence satisfying a boolean condition.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @param predicate
	 *            a function representing a boolean condition.
	 * @return a first object satisfying a boolean condition.
	 */
    public static <T> T first(final Function<Boolean, ? super T> predicate, final Iterable<T> source) {
        return first(where(predicate, source));
    }

    /**
	 * Returns the only element of a sequence, and throws an exception if there
	 * is not exactly one element in the sequence.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @return a first object.
	 */
    public static <T> T single(final Iterable<T> source) {
        return internalSingle(source, true);
    }

    /**
	 * Returns the only element of a sequence, null if the source contains no
	 * elements, and throws an exception if there is not exactly one element in
	 * the sequence.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @return a first object, or null if the source contains no elements.
	 */
    public static <T> T singleOrNull(final Iterable<T> source) {
        return internalSingle(source, false);
    }

    private static <T> T internalSingle(final Iterable<T> source, boolean throwOnEmpty) {
        if (source instanceof List) {
            List<T> l = (List<T>) source;
            int size = l.size();
            if (size == 0) {
                if (throwOnEmpty) throw new IndexOutOfBoundsException();
                return null;
            }
            if (size == 1) return l.get(0);
        } else {
            Iterator<T> iter = source.iterator();
            if (!iter.hasNext()) {
                if (throwOnEmpty) throw new IndexOutOfBoundsException();
                return null;
            }
            T r = iter.next();
            if (!iter.hasNext()) return r;
        }
        throw new IllegalArgumentException("More than one element.");
    }

    /**
	 * Returns the first object in the sequence satisfying a boolean condition.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @param predicate
	 *            a function representing a boolean condition.
	 * @return a first object satisfying a boolean condition.
	 */
    public static <T> T single(final Function<Boolean, ? super T> predicate, final Iterable<T> source) {
        return single(where(predicate, source));
    }

    /**
	 * Returns the only element of a sequence, null if the source contains no
	 * elements, and throws an exception if there is not exactly one element in
	 * the sequence.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @param predicate
	 *            a function representing a boolean condition.
	 * @return a first object satisfying a boolean condition, or null if the
	 *         source contains no elements.
	 */
    public static <T> T singleOrNull(final Function<Boolean, ? super T> predicate, final Iterable<T> source) {
        return singleOrNull(where(predicate, source));
    }

    /**
	 * Returns the first object in the source, or null if the sequence contains
	 * no elements.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @return a first object in the source, or null if the sequence contains no
	 *         elements.
	 */
    public static <T> T firstOrNull(final Iterable<T> source) {
        if (source instanceof List) {
            List<T> l = (List<T>) source;
            return l.size() > 0 ? l.get(0) : null;
        }
        Iterator<T> iter = source.iterator();
        if (!iter.hasNext()) return null;
        return iter.next();
    }

    /**
	 * Returns the first object in the source satisfying a boolean condition, or
	 * null if the sequence contains no elements.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @param predicate
	 *            a function representing a boolean condition.
	 * @return a first object in the source satisfying a boolean condition, or
	 *         null if the sequence contains no elements.
	 */
    public static <T> T firstOrNull(final Function<Boolean, ? super T> predicate, final Iterable<T> source) {
        return firstOrNull(where(predicate, source));
    }

    /**
	 * Returns the last object in the sequence.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @return a last object.
	 */
    public static <T> T last(final Iterable<T> source) {
        if (source instanceof List) {
            List<T> gsource = (List<T>) source;
            return gsource.get(gsource.size() - 1);
        }
        Iterator<T> iter = source.iterator();
        if (!iter.hasNext()) throw new IndexOutOfBoundsException();
        T r;
        do {
            r = iter.next();
        } while (iter.hasNext());
        return r;
    }

    /**
	 * Returns the last object in the sequence satisfying a boolean condition.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @param predicate
	 *            a function representing a boolean condition.
	 * @return a last object satisfying a boolean condition.
	 */
    public static <T> T last(final Function<Boolean, ? super T> predicate, final Iterable<T> source) {
        return last(where(predicate, source));
    }

    /**
	 * Returns the last object in the source, or null if the source contains no
	 * elements.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @return a last object in the source, or null if the source contains no
	 *         elements.
	 */
    public static <T> T lastOrNull(final Iterable<T> source) {
        if (source instanceof List) {
            List<T> l = (List<T>) source;
            return l.size() > 0 ? l.get(l.size() - 1) : null;
        }
        Iterator<T> iter = source.iterator();
        if (!iter.hasNext()) return null;
        T r;
        do {
            r = iter.next();
        } while (iter.hasNext());
        return r;
    }

    /**
	 * Returns the last object in the source satisfying a boolean condition, or
	 * null if the source contains no elements.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @param predicate
	 *            a function representing a boolean condition.
	 * @return a last object in the source satisfying a boolean condition, or
	 *         null if the source contains no elements.
	 */
    public static <T> T lastOrNull(final Function<Boolean, ? super T> predicate, final Iterable<T> source) {
        return lastOrNull(where(predicate, source));
    }

    /**
	 * Creates a {@code List<T>} from an {@code Iterable<T>}.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @return A {@code List<T>} that contains elements from the input sequence.
	 */
    public static <T> List<T> toList(final Iterable<T> source) {
        if (source instanceof List) return (List<T>) source;
        ArrayList<T> list = new ArrayList<T>();
        for (T t : source) list.add(t);
        return list;
    }

    /**
	 * Determines whether a sequence contains a specified element.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param value
	 *            The value to locate in the sequence.
	 * @param source
	 *            the input object sequence.
	 * @return true if the source sequence contains an element that has the
	 *         specified value; otherwise, false.
	 */
    public static <T extends Comparable<? super T>> boolean contains(final T value, final Iterable<T> source) {
        return contains(value, Comparator.<T>getComparableDefault(), source);
    }

    /**
	 * Determines whether a sequence contains a specified element.
	 * 
	 * @param <T>
	 *            the type of the objects in the sequence.
	 * @param value
	 *            The value to locate in the sequence.
	 * @param comparator
	 *            comparator is used to compare values.
	 * @param source
	 *            the input object sequence.
	 * @return true if the source sequence contains an element that has the
	 *         specified value; otherwise, false.
	 */
    public static <T> boolean contains(final T value, final java.util.Comparator<? super T> comparator, final Iterable<T> source) {
        for (T t : source) if (comparator.compare(t, value) == 0) return true;
        return false;
    }

    /**
	 * Returns a maximum value in a sequence of numeric objects.
	 * 
	 * @param <T>
	 *            type of the objects in the input sequence.
	 * @param source
	 *            the input object sequence.
	 * @return the maximum value.
	 */
    public static <T extends Comparable<? super T>> T max(final Iterable<T> source) {
        return max(Functions.<T>self(), Comparator.<T>getComparableDefault(), source);
    }

    /**
	 * Computes the maximum value in a sequence of objects by transforming each
	 * element to numeric value. *
	 * 
	 * {@code Object.equals()}
	 * 
	 * is used to compare values.
	 * 
	 * @param <T>
	 *            type of the objects in the input sequence.
	 * @param source
	 *            the input object sequence.
	 * @param selector
	 *            a transform function to apply to each input object.
	 * @return the maximum value.
	 */
    public static <T, Key extends Comparable<? super Key>> T max(final Function<Key, ? super T> selector, final Iterable<T> source) {
        return max(selector, Comparator.<Key>getComparableDefault(), source);
    }

    /**
	 * Computes the maximum value in a sequence of objects by transforming each
	 * element to numeric value. A specified comparator is used to compare
	 * values.
	 * 
	 * @param <T>
	 *            type of the objects in the input sequence.
	 * @param source
	 *            the input object sequence.
	 * @param selector
	 *            a transform function to apply to each input object.
	 * @param comparator
	 *            comparator is used to compare values.
	 * @return the maximum value.
	 */
    public static <T, Key> T max(final Function<Key, ? super T> selector, final java.util.Comparator<? super Key> comparator, final Iterable<T> source) {
        return last(aggregate(conditional(Functions.<T, Key>greaterThanOrEqual(selector, selector, comparator)), source));
    }

    /**
	 * Returns a minimum value in a sequence of numeric objects.
	 * 
	 * @param <T>
	 *            type of the objects in the input sequence.
	 * @param source
	 *            the input object sequence.
	 * @return the minimum value.
	 */
    public static <T extends Comparable<? super T>> T min(final Iterable<T> source) {
        return min(Functions.<T>self(), Comparator.<T>getComparableDefault(), source);
    }

    /**
	 * Computes the minimum value in a sequence of objects by transforming each
	 * element to numeric value.
	 * 
	 * {@code Object.equals()}
	 * 
	 * is used to compare values.
	 * 
	 * @param <T>
	 *            type of the objects in the input sequence.
	 * @param source
	 *            the input object sequence.
	 * @param selector
	 *            a transform function to apply to each input object.
	 * @return the minimum value.
	 */
    public static <T, Key extends Comparable<? super Key>> T min(final Function<Key, ? super T> selector, final Iterable<T> source) {
        return min(selector, Comparator.<Key>getComparableDefault(), source);
    }

    /**
	 * Computes the minimum value in a sequence of objects by transforming each
	 * element to numeric value. A specified comparator is used to compare
	 * values.
	 * 
	 * @param <T>
	 *            type of the objects in the input sequence.
	 * @param source
	 *            the input object sequence.
	 * @param selector
	 *            a transform function to apply to each input object.
	 * @param comparator
	 *            comparator is used to compare values.
	 * @return the minimum value.
	 */
    public static <T, Key> T min(final Function<Key, ? super T> selector, final java.util.Comparator<? super Key> comparator, final Iterable<T> source) {
        return last(aggregate(conditional(Functions.<T, Key>lessThanOrEqual(selector, selector, comparator)), source));
    }

    /**
	 * Computes the sum of a sequence of objects by transforming each element to
	 * numeric value.
	 * 
	 * @param <T>
	 *            type of the objects in the input sequence.
	 * @param source
	 *            the input object sequence.
	 * @param selector
	 *            a transform function to apply to each input object.
	 * @return the sum value.
	 */
    public static <T, Result extends Number> Result sum(final Function<Result, ? super T> selector, final Iterable<T> source) {
        return last(aggregateSum(selector, source));
    }

    /**
	 * Returns the sum of a sequence of numeric objects.
	 * 
	 * @param <T>
	 *            type of the objects in the input sequence.
	 * @param source
	 *            the input sequence.
	 * @return the sum value.
	 */
    public static <T extends Number> T sum(final Iterable<T> source) {
        return sum(Functions.<T>self(), source);
    }

    private static <T, Result extends Number> Iterable<Result> aggregateSum(final Function<Result, ? super T> selector, final Iterable<T> source) {
        return Query.aggregate(Functions.<Result, T>add(selector, selector), source);
    }

    /**
	 * Computes the average of a sequence of objects by applying a transform to
	 * numeric to each element.
	 * 
	 * @param <T>
	 *            type of the objects in the input sequence.
	 * @param source
	 *            the input object sequence.
	 * @param selector
	 *            a transform function to apply to each input object.
	 * @return the average.
	 */
    public static <T, E extends Number> Number average(final Function<E, ? super T> selector, final Iterable<T> source) {
        final Iterable<E> aggregate = aggregateSum(selector, source);
        return last(new Iterable<Number>() {

            public final Iterator<Number> iterator() {
                return averageIterator(aggregate);
            }
        });
    }

    /**
	 * Computes the average of a sequence of numeric objects.
	 * 
	 * @param <T>
	 *            type of the objects in the input sequence.
	 * @param source
	 *            the input object sequence.
	 * @return the average.
	 */
    public static <T extends Number> Number average(final Iterable<T> source) {
        return average(Functions.<T>self(), source);
    }

    private static <E extends Number> Iterator<Number> averageIterator(final Iterable<E> aggregate) {
        return new BaseIterator<Number>() {

            private final Iterator<E> _iter = aggregate.iterator();

            long _count;

            public final boolean hasNext() {
                while (_iter.hasNext()) {
                    _count++;
                    return yield(_iter.next());
                }
                return yieldBreak;
            }

            @Override
            public final Number next() {
                return BinaryOperator.Divide.eval(super.next(), _count);
            }
        };
    }
}
