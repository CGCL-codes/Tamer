package net.sf.kdgcommons.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *  A <code>Map</code> decorator that will return a default value from {@link #get}
 *  if there is no mapping for the key. There are two ways to use this class: static
 *  and dynamic, and they're very different in how they interact with the underlying
 *  map.
 *  <p>
 *  The static usage returns a single constant value instance, and <em>does not</em>
 *  update the underlying map. This is useful to avoid null checks, for example when
 *  examining a list of parameters.
 *  <p>
 *  The dynamic usage invokes a factory to create new values, and stores those values
 *  in the underlying map. One example of this is a multi-map, where the factory will
 *  create new <code>List</code> or <code>Set</code> instances.
 *  <p>
 *  You can also construct an instance that reverses these usage patterns, and either
 *  returns a static value while updating the underlying map, or returns a dynamic
 *  value and leaves the delegate untouched.
 *  <p>
 *  Note that {@link #get} is the <em>only</em> method overridden by this class. The
 *  "contains" methods would be mostly useless if they considered default values, and
 *  the behavior of iterators would be difficult to define.
 */
public class DefaultMap<K, V> implements Map<K, V> {

    private Map<K, V> _delegate;

    private ValueFactory<V> _valueFactory;

    private boolean _updateMap;

    /**
     *  Base constructor, allowing full configuration.
     *
     *  @param  delegate    The underlying <code>Map</code>.
     *  @param  factory     A factory for new values.
     *  @param  update      Pass <code>true</code> to update the map when a default
     *                      value is returned, <code>false</code> to return the value
     *                      and leave the mapping missing.
     */
    public DefaultMap(Map<K, V> delegate, ValueFactory<V> factory, boolean update) {
        _delegate = delegate;
        _valueFactory = factory;
        _updateMap = update;
    }

    /**
     *  Constructs an instance that returns a static value and does not update
     *  the underlying map.
     *
     *  @param  delegate    The underlying <code>Map</code>.
     *  @param  value       The value to return for missing mappings.
     */
    public DefaultMap(Map<K, V> delegate, V value) {
        this(delegate, new StaticValueFactory<V>(value), false);
    }

    /**
     *  Constructs an instance that returns a dynamic value and will update
     *  the underlying map.
     *
     *  @param  delegate    The underlying <code>Map</code>.
     *  @param  factory     A factory for new values.
     */
    public DefaultMap(Map<K, V> delegate, ValueFactory<V> factory) {
        this(delegate, factory, true);
    }

    /**
     *  This interface defines a factory for default values.
     */
    public interface ValueFactory<T> {

        public T newInstance();
    }

    /**
     *  An implementation of the {@link #ValueFactory} interface for static
     *  values. Exposed in case you want an updating map with a static value
     *  (although why you'd want that, I don't know).
     */
    public static class StaticValueFactory<T> implements ValueFactory<T> {

        private T _value;

        public StaticValueFactory(T value) {
            _value = value;
        }

        public T newInstance() {
            return _value;
        }
    }

    /**
     *  Retrieves an existing mapping, or returns the default value if no
     *  such mapping exists. Depending on configuration, may update the
     *  map with that default value.
     *  <p>
     *  This operation is not atomic. You will need external synchronization
     *  to make it so (or subclass and synchronize).
     */
    public V get(Object key) {
        if (_delegate.containsKey(key)) return _delegate.get(key);
        V value = _valueFactory.newInstance();
        if (_updateMap) _delegate.put((K) key, value);
        return value;
    }

    /**
     *  {@inheritDoc}
     */
    public int size() {
        return _delegate.size();
    }

    /**
     *  {@inheritDoc}
     */
    public boolean isEmpty() {
        return _delegate.isEmpty();
    }

    /**
     *  {@inheritDoc}
     */
    public boolean containsKey(Object key) {
        return _delegate.containsKey(key);
    }

    /**
     *  {@inheritDoc}
     */
    public boolean containsValue(Object value) {
        return _delegate.containsValue(value);
    }

    /**
     *  Stores a new value in the map, replacing any existing value. If there was
     *  an existing mapping for the key, will return the previous value. <em>Will
     *  not<em> return the default value.
     */
    public V put(K key, V value) {
        return _delegate.put(key, value);
    }

    /**
     *  Removes an existing mapping, if there was one, and returns the value of
     *  that mapping. <em>Will not<em> return the default value.
     */
    public V remove(Object key) {
        return _delegate.remove(key);
    }

    /**
     *  {@inheritDoc}
     */
    public void putAll(Map<? extends K, ? extends V> m) {
        _delegate.putAll(m);
    }

    /**
     *  {@inheritDoc}
     */
    public void clear() {
        _delegate.clear();
    }

    /**
     *  {@inheritDoc}
     */
    public Set<K> keySet() {
        return _delegate.keySet();
    }

    /**
     *  {@inheritDoc}
     */
    public Collection<V> values() {
        return _delegate.values();
    }

    /**
     *  {@inheritDoc}
     */
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return _delegate.entrySet();
    }
}
