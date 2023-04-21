package de.javagimmicks.collections.transformer;

import java.util.NavigableMap;
import java.util.NavigableSet;

class ValueBidiTransformingNavigableMap<K, VF, VT> extends ValueBidiTransformingSortedMap<K, VF, VT> implements NavigableMap<K, VT> {

    /**
    * @deprecated Use TranformerUtils.decorateValueBased() instead
    */
    @Deprecated
    public ValueBidiTransformingNavigableMap(NavigableMap<K, VF> map, BidiTransformer<VF, VT> valueTransformer) {
        super(map, valueTransformer);
    }

    public Entry<K, VT> ceilingEntry(K key) {
        Entry<K, VF> ceilingEntry = getNavigableMap().ceilingEntry(key);
        return ceilingEntry == null ? null : new ValueBidiTransformingEntry<K, VF, VT>(ceilingEntry, getBidiTransformer());
    }

    public K ceilingKey(K key) {
        return getNavigableMap().ceilingKey(key);
    }

    public NavigableSet<K> descendingKeySet() {
        return getNavigableMap().descendingKeySet();
    }

    public NavigableMap<K, VT> descendingMap() {
        return TransformerUtils.decorateValueBased(getNavigableMap().descendingMap(), getBidiTransformer());
    }

    public Entry<K, VT> firstEntry() {
        Entry<K, VF> firstEntry = getNavigableMap().firstEntry();
        return firstEntry == null ? null : new ValueBidiTransformingEntry<K, VF, VT>(firstEntry, getBidiTransformer());
    }

    public Entry<K, VT> floorEntry(K key) {
        Entry<K, VF> floorEntry = getNavigableMap().floorEntry(key);
        return floorEntry == null ? null : new ValueBidiTransformingEntry<K, VF, VT>(floorEntry, getBidiTransformer());
    }

    public K floorKey(K key) {
        return getNavigableMap().floorKey(key);
    }

    public NavigableMap<K, VT> headMap(K toKey, boolean inclusive) {
        return TransformerUtils.decorateValueBased(getNavigableMap().headMap(toKey, inclusive), getBidiTransformer());
    }

    public Entry<K, VT> higherEntry(K key) {
        Entry<K, VF> higherEntry = getNavigableMap().higherEntry(key);
        return higherEntry == null ? null : new ValueBidiTransformingEntry<K, VF, VT>(higherEntry, getBidiTransformer());
    }

    public K higherKey(K key) {
        return getNavigableMap().higherKey(key);
    }

    public Entry<K, VT> lastEntry() {
        Entry<K, VF> lastEntry = getNavigableMap().lastEntry();
        return lastEntry == null ? null : new ValueBidiTransformingEntry<K, VF, VT>(lastEntry, getBidiTransformer());
    }

    public Entry<K, VT> lowerEntry(K key) {
        Entry<K, VF> lowerEntry = getNavigableMap().lowerEntry(key);
        return lowerEntry == null ? null : new ValueBidiTransformingEntry<K, VF, VT>(lowerEntry, getBidiTransformer());
    }

    public K lowerKey(K key) {
        return getNavigableMap().lowerKey(key);
    }

    public NavigableSet<K> navigableKeySet() {
        return getNavigableMap().navigableKeySet();
    }

    public Entry<K, VT> pollFirstEntry() {
        Entry<K, VF> firstEntry = getNavigableMap().pollFirstEntry();
        return firstEntry == null ? null : new ValueBidiTransformingEntry<K, VF, VT>(firstEntry, getBidiTransformer());
    }

    public Entry<K, VT> pollLastEntry() {
        Entry<K, VF> lastEntry = getNavigableMap().pollLastEntry();
        return lastEntry == null ? null : new ValueBidiTransformingEntry<K, VF, VT>(lastEntry, getBidiTransformer());
    }

    public NavigableMap<K, VT> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return TransformerUtils.decorateValueBased(getNavigableMap().subMap(fromKey, fromInclusive, toKey, toInclusive), getBidiTransformer());
    }

    public NavigableMap<K, VT> tailMap(K fromKey, boolean inclusive) {
        return TransformerUtils.decorateValueBased(getNavigableMap().tailMap(fromKey, inclusive), getBidiTransformer());
    }

    protected NavigableMap<K, VF> getNavigableMap() {
        return (NavigableMap<K, VF>) _internalMap;
    }
}
