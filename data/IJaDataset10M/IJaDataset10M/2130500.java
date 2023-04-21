package org.dishevelled.observable.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import org.dishevelled.observable.ObservableCollection;
import org.dishevelled.observable.ObservableList;
import org.dishevelled.observable.ObservableMap;
import org.dishevelled.observable.ObservableQueue;
import org.dishevelled.observable.ObservableSet;
import org.dishevelled.observable.ObservableSortedMap;
import org.dishevelled.observable.ObservableSortedSet;

/**
 * Utility methods for creating observable collection and map
 * interface decorators.
 *
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public final class ObservableUtils {

    /**
     * Private constructor.
     */
    private ObservableUtils() {
    }

    /**
     * Create and return a new simple observable decorator for the specified collection.
     *
     * @param <T> collection element type
     * @param collection collection to decorate, must not be null
     * @return a new simple observable decorator for the specified collection
     */
    public static <T> ObservableCollection<T> observableCollection(final Collection<T> collection) {
        return new ObservableCollectionImpl<T>(collection);
    }

    /**
     * Create and return a new simple observable decorator for the specified list.
     *
     * @param <T> list element type
     * @param list list to decorate, must not be null
     * @return a new simple observable decorator for the specified list
     */
    public static <T> ObservableList<T> observableList(final List<T> list) {
        return new ObservableListImpl<T>(list);
    }

    /**
     * Create and return a new simple observable decorator for the specified map.
     *
     * @param <K> map key type
     * @param <V> map value type
     * @param map map to decorate, must not be null
     * @return a new simple observable decorator for the specified map
     */
    public static <K, V> ObservableMap<K, V> observableMap(final Map<K, V> map) {
        return new ObservableMapImpl<K, V>(map);
    }

    /**
     * Create and return a new simple observable decorator for the specified set.
     *
     * @param <T> queue element type
     * @param queue queue to decorate, must not be null
     * @return a new simple observable decorator for the specified queue
     */
    public static <T> ObservableQueue<T> observableQueue(final Queue<T> queue) {
        return new ObservableQueueImpl<T>(queue);
    }

    /**
     * Create and return a new simple observable decorator for the specified set.
     *
     * @param <T> set element type
     * @param set set to decorate, must not be null
     * @return a new simple observable decorator for the specified set
     */
    public static <T> ObservableSet<T> observableSet(final Set<T> set) {
        return new ObservableSetImpl<T>(set);
    }

    /**
     * Create and return a new simple observable decorator for the specified sorted map.
     *
     * @param <K> sorted map key type
     * @param <V> sorted map value type
     * @param sortedMap sorted map to decorate, must not be null
     * @return a new simple observable decorator for the specified sorted map
     */
    public static <K, V> ObservableSortedMap<K, V> observableSortedMap(final SortedMap<K, V> sortedMap) {
        return new ObservableSortedMapImpl<K, V>(sortedMap);
    }

    /**
     * Create and return a new simple observable decorator for the specified sorted set.
     *
     * @param <T> sorted set element type
     * @param sortedSet sorted set to decorate, must not be null
     * @return a new simple observable decorator for the specified sorted set
     */
    public static <T> ObservableSortedSet<T> observableSortedSet(final SortedSet<T> sortedSet) {
        return new ObservableSortedSetImpl<T>(sortedSet);
    }
}
