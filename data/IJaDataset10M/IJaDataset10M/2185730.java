package edu.rice.cs.plt.collect;

import java.util.Iterator;
import edu.rice.cs.plt.tuple.Pair;
import edu.rice.cs.plt.iter.SizedIterable;

/**
 * Maintains an index mapping values of one type to sets of another.  Useful in implementations
 * of Relations.
 */
public interface RelationIndex<K, V> extends SizedIterable<Pair<K, V>> {

    /** Whether the given key-value mapping occurs. */
    public boolean contains(Object key, Object value);

    /**
   * A dynamically-updating view of all keys mapping to at least one value.  Mutation (removal),
   * if supported, will be automatically reflected in the relation being indexed.
   */
    public PredicateSet<K> keys();

    /**
   * A dynamically-updating view of all values matching {@code key}.  May be empty. Mutation,
   * if supported, will be automatically reflected in the relation being indexed.
   */
    public PredicateSet<V> match(K key);

    /** Iterates through all key-value pairs in the index. */
    public Iterator<Pair<K, V>> iterator();

    /**
   * Requests that the index be updated to reflect the addition of the given key/value pair.
   * Assumes the pair is not already present.
   */
    public void added(K key, V value);

    /**
   * Requests that the index be updated to reflect the removal of the given key/value pair.
   * Assumes the pair is present.
   */
    public void removed(K key, V value);

    /**
   * Requests that the index be cleared to reflect the current state of the relation.
   * Assumes the index is non-empty.
   */
    public void cleared();
}
