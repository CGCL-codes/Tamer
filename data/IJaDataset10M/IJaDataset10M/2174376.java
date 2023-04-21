package net.sourceforge.xconf.toolbox.generic;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Common methods on generic sets.
 *
 * @author Tom Czarniecki
 */
public class SetUtils {

    /**
     * Returns a mutable empty set.
     */
    public static <T> Set<T> empty() {
        return new HashSet<T>();
    }

    /**
     * Returns a mutable ordered empty set.
     */
    public static <T> Set<T> emptyLinked() {
        return new LinkedHashSet<T>();
    }

    /**
     * Returns a mutable sorted empty set.
     */
    public static <T> SortedSet<T> emptySorted() {
        return new TreeSet<T>();
    }

    /**
     * Returns a mutable sorted empty set.
     */
    public static <T> SortedSet<T> emptySorted(Comparator<T> comp) {
        return new TreeSet<T>(comp);
    }

    /**
     * Creates a set from the given collection.
     */
    public static <T> Set<T> toSet(Collection<T> items) {
        return new HashSet<T>(items);
    }

    /**
     * Creates an ordered set from the given collection.
     */
    public static <T> Set<T> toLinkedSet(Collection<T> items) {
        return new LinkedHashSet<T>(items);
    }

    /**
     * Creates a sorted set from the given collection.
     */
    public static <T> SortedSet<T> toSortedSet(Collection<T> items) {
        return new TreeSet<T>(items);
    }

    /**
     * Creates a sorted set from the given collection, sorted by the given comparator.
     */
    public static <T> SortedSet<T> toSortedSet(Collection<T> items, Comparator<T> comp) {
        SortedSet<T> result = emptySorted(comp);
        result.addAll(items);
        return result;
    }

    /**
     * Wraps the given items in a mutable unordered set.
     */
    public static <T> Set<T> toSet(T... items) {
        Set<T> coll = empty();
        Collections.addAll(coll, items);
        return coll;
    }

    /**
     * Wraps the given items in a mutable ordered set.
     */
    public static <T> Set<T> toLinkedSet(T... items) {
        Set<T> coll = emptyLinked();
        Collections.addAll(coll, items);
        return coll;
    }

    /**
     * Wraps the given items in a mutable sorted set.
     */
    public static <T> SortedSet<T> toSortedSet(T... items) {
        SortedSet<T> coll = emptySorted();
        Collections.addAll(coll, items);
        return coll;
    }

    /**
     * Wraps the given items in a mutable sorted set using the given comparator.
     */
    public static <T> SortedSet<T> toSortedSet(Comparator<T> comp, T... items) {
        SortedSet<T> coll = emptySorted(comp);
        Collections.addAll(coll, items);
        return coll;
    }

    /**
     * Execute the closure on each item.
     */
    public static <T> void each(Set<T> items, Closure<T> closure) {
        Iterations.each(items, closure);
    }

    /**
     * Returns a new filtered set.
     */
    public static <T> Set<T> filter(Set<T> items, Filter<T> filter) {
        Set<T> result = empty();
        Iterations.filter(items, filter, result);
        return result;
    }

    /**
     * Returns a new filtered set.
     */
    public static <T> Set<T> filter(Filter<T> filter, T... items) {
        Set<T> result = empty();
        Iterations.filter(result, filter, items);
        return result;
    }

    /**
     * Returns a new converted set.
     */
    public static <From, To> Set<To> convert(Set<From> items, Converter<From, To> converter) {
        Set<To> result = empty();
        Iterations.convert(items, converter, result);
        return result;
    }

    /**
     * Returns a new converted set.
     */
    public static <From, To> Set<To> convert(Converter<From, To> converter, From... items) {
        Set<To> result = empty();
        Iterations.convert(result, converter, items);
        return result;
    }
}
