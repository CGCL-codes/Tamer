package uk.ac.lkl.common.util.collections.event;

import uk.ac.lkl.common.util.collections.NotifyingMap;

/**
 * For entryAdded: <oldKey, oldValue, newKey, newValue> = <null, null, K, V> For
 * entryRemoved: <K, V, null, null> For valueChanged: <K, V1, K, V2>
 * 
 * @author $Author: toontalk@gmail.com $
 * @version $Revision: 8138 $
 * @version $Date: 2011-01-20 02:52:10 -0500 (Thu, 20 Jan 2011) $
 * 
 */
public class MapEvent<K, V> extends CollectionEvent<NotifyingMap<K, V>, MapEntry<K, V>> {

    private V newValue;

    public MapEvent(NotifyingMap<K, V> map, K key, V value) {
        this(map, new MapEntry<K, V>(key, value), null);
    }

    public MapEvent(NotifyingMap<K, V> map, K key, V value, V newValue) {
        this(map, new MapEntry<K, V>(key, value), newValue);
    }

    private MapEvent(NotifyingMap<K, V> map, MapEntry<K, V> entry, V newValue) {
        super(map, entry);
        this.newValue = newValue;
    }

    public K getKey() {
        return getElement().getKey();
    }

    public V getValue() {
        return getElement().getValue();
    }

    public V getNewValue() {
        return newValue;
    }

    public NotifyingMap<K, V> getMap() {
        return (NotifyingMap<K, V>) getSource();
    }
}
