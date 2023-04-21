package org.decisiondeck.utils.collections;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import com.google.common.base.Function;

/**
 * @author Sean Patrick Floyd
 * @see <a href="http://stackoverflow.com/questions/3869258/guava-setk-functionk-v-mapk-v">stackoverflow</a>
 * 
 * @param <K>
 *            the type of the keys to use in this map and in the backing set.
 * @param <V>
 *            the type of the values in the map.
 */
public class SetBackedMap<K, V> extends AbstractMap<K, V> {

    private class MapEntry implements Entry<K, V> {

        private final K m_key;

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry<?, ?> e2 = (Entry<?, ?>) obj;
            return (getKey() == null ? e2.getKey() == null : getKey().equals(e2.getKey())) && (getValue() == null ? e2.getValue() == null : getValue().equals(e2.getValue()));
        }

        public MapEntry(final K key) {
            this.m_key = key;
        }

        @Override
        public K getKey() {
            return this.m_key;
        }

        @Override
        public V getValue() {
            V value = SetBackedMap.this.cache.get(this.m_key);
            if (value == null) {
                value = SetBackedMap.this.m_funk.apply(this.m_key);
                SetBackedMap.this.cache.put(this.m_key, value);
            }
            return value;
        }

        @Override
        public V setValue(final V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int hashCode() {
            return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
        }
    }

    private class EntrySet extends AbstractSet<Entry<K, V>> {

        public class EntryIterator implements Iterator<Entry<K, V>> {

            private final Iterator<K> inner;

            public EntryIterator() {
                this.inner = EntrySet.this.m_keys.iterator();
            }

            @Override
            public boolean hasNext() {
                return this.inner.hasNext();
            }

            @Override
            public Map.Entry<K, V> next() {
                final K key = this.inner.next();
                return new MapEntry(key);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        private final Set<K> m_keys;

        public EntrySet(final Set<K> keys) {
            this.m_keys = keys;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return this.m_keys.size();
        }
    }

    private final WeakHashMap<K, V> cache;

    private final Set<Entry<K, V>> entries;

    private final Function<? super K, ? extends V> m_funk;

    public SetBackedMap(Set<K> keys, Function<? super K, ? extends V> funk) {
        this.m_funk = funk;
        this.cache = new WeakHashMap<K, V>();
        this.entries = new EntrySet(keys);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return this.entries;
    }

    public static <K, V> SetBackedMap<K, V> create(Set<K> keys, Function<? super K, ? extends V> funk) {
        return new SetBackedMap<K, V>(keys, funk);
    }
}
