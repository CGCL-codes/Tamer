package de.javagimmicks.collections.event;

import java.util.NavigableMap;
import java.util.NavigableSet;
import de.javagimmicks.collections.transformer.NavigableKeySet;

public abstract class AbstractEventNavigableMap<K, V> extends AbstractEventSortedMap<K, V> implements NavigableMap<K, V> {

    private static final long serialVersionUID = 7570207692375842675L;

    public AbstractEventNavigableMap(NavigableMap<K, V> decorated) {
        super(decorated);
    }

    @Override
    public NavigableMap<K, V> getDecorated() {
        return (NavigableMap<K, V>) super.getDecorated();
    }

    public Entry<K, V> ceilingEntry(K key) {
        return getDecorated().ceilingEntry(key);
    }

    public K ceilingKey(K key) {
        return getDecorated().ceilingKey(key);
    }

    public Entry<K, V> firstEntry() {
        return getDecorated().firstEntry();
    }

    public Entry<K, V> floorEntry(K key) {
        return getDecorated().floorEntry(key);
    }

    public K floorKey(K key) {
        return getDecorated().floorKey(key);
    }

    public Entry<K, V> higherEntry(K key) {
        return getDecorated().higherEntry(key);
    }

    public K higherKey(K key) {
        return getDecorated().higherKey(key);
    }

    public Entry<K, V> lastEntry() {
        return getDecorated().lastEntry();
    }

    public Entry<K, V> lowerEntry(K key) {
        return getDecorated().lowerEntry(key);
    }

    public K lowerKey(K key) {
        return getDecorated().lowerKey(key);
    }

    public Entry<K, V> pollFirstEntry() {
        Entry<K, V> firstEntry = getDecorated().pollFirstEntry();
        if (firstEntry != null) {
            fireEntryRemoved(firstEntry.getKey(), firstEntry.getValue());
        }
        return firstEntry;
    }

    public Entry<K, V> pollLastEntry() {
        Entry<K, V> lastEntry = getDecorated().pollLastEntry();
        if (lastEntry != null) {
            fireEntryRemoved(lastEntry.getKey(), lastEntry.getValue());
        }
        return lastEntry;
    }

    public NavigableSet<K> descendingKeySet() {
        return descendingMap().navigableKeySet();
    }

    public NavigableSet<K> navigableKeySet() {
        return new NavigableKeySet<K, V>(this);
    }

    public NavigableMap<K, V> descendingMap() {
        return new EventSubNavigableMap<K, V>(this, getDecorated().descendingMap());
    }

    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        return new EventSubNavigableMap<K, V>(this, getDecorated().headMap(toKey, inclusive));
    }

    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return new EventSubNavigableMap<K, V>(this, getDecorated().subMap(fromKey, fromInclusive, toKey, toInclusive));
    }

    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return new EventSubNavigableMap<K, V>(this, getDecorated().tailMap(fromKey, inclusive));
    }

    protected static class EventSubNavigableMap<K, V> extends AbstractEventNavigableMap<K, V> {

        private static final long serialVersionUID = 8445308257944385932L;

        protected final AbstractEventNavigableMap<K, V> _parent;

        protected EventSubNavigableMap(AbstractEventNavigableMap<K, V> parent, NavigableMap<K, V> decorated) {
            super(decorated);
            _parent = parent;
        }

        @Override
        protected void fireEntryAdded(K key, V value) {
            _parent.fireEntryAdded(key, value);
        }

        @Override
        protected void fireEntryRemoved(K key, V value) {
            _parent.fireEntryRemoved(key, value);
        }

        @Override
        protected void fireEntryUpdated(K key, V oldValue, V newValue) {
            _parent.fireEntryUpdated(key, oldValue, newValue);
        }
    }
}
