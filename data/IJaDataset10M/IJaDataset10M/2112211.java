package org.happy.collections.maps.decorators.data.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import org.happy.collections.maps.DataMap_1x2;
import org.happy.collections.maps.DataMap_1x2.DataAdapter;
import org.happy.commons.patterns.Cacheable_1x2;
import org.happy.commons.util.comparators.Comparator_1x0;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

/**
 * decorates DataAdapter Decorator with Cache functionality.
 * null values for keys are not allowed!
 * @author Andreas Hollmann
 *
 * @param <K> type of key, can't be null!
 * @param <V> type of values
 */
public class CacheDataAdapter_1x2<K, V> extends DataAdapterDecorator_1x2<K, V> implements Cacheable_1x2 {

    /**
	 * decorates CacheAdapter
	 * @param decorated
	 * @return
	 */
    public static <K, V> CacheDataAdapter_1x2<K, V> of(DataMap_1x2.DataAdapter<K, V> decorated) {
        return new CacheDataAdapter_1x2<K, V>(decorated);
    }

    /**
	 * caches the data according to the sequence it is stored in the decorated DataAdapter class
	 */
    private SortedMap<Integer, Entry<K, V>> cacheMapSequence;

    /**
	 * caches keys and values thus they can be fast accesed 
	 */
    private Map<K, V> cacheMap;

    private Integer size = null;

    public CacheDataAdapter_1x2(DataAdapter<K, V> decorated) {
        super(decorated);
        this.cacheMapSequence = new TreeMap<Integer, Entry<K, V>>(new Comparator_1x0<Integer>());
        this.cacheMap = new HashMap<K, V>();
    }

    @Override
    public boolean remove(Object key) {
        this.refresh();
        return super.remove(key);
    }

    @Override
    public Collection<Entry<K, V>> getBetween(final int startIndex, final int endIndex) {
        final int elementsNumber = endIndex - startIndex;
        final SortedMap<Integer, Entry<K, V>> subMap = cacheMapSequence.subMap(startIndex, endIndex);
        if (elementsNumber == subMap.size()) {
            return ImmutableSet.copyOf(subMap.values());
        }
        final Collection<Entry<K, V>> col = super.getBetween(startIndex, endIndex);
        int index = startIndex;
        for (final Entry<K, V> entry : col) {
            cacheMapSequence.put(index, entry);
            cacheMap.put(entry.getKey(), entry.getValue());
            index++;
        }
        Preconditions.checkState((index - startIndex) == col.size(), "Internal Error -> Not all elements  were added to the cacheMap!");
        return col;
    }

    @Override
    public int size() {
        if (this.size == null) {
            this.size = super.size();
        }
        return this.size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        Preconditions.checkNotNull((K) key);
        if (this.cacheMap.containsKey(key)) {
            return this.cacheMap.get(key);
        }
        V value = super.get(key);
        this.cacheMap.put((K) key, value);
        return value;
    }

    @Override
    public void clear() {
        this.refresh();
        super.clear();
    }

    @Override
    public V put(K key, V value) {
        this.refresh();
        return super.put(key, value);
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        if (this.cacheMap.containsKey(key)) {
            return true;
        }
        return super.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        if (this.cacheMap.containsValue(value)) {
            return true;
        }
        return super.containsValue(value);
    }

    @Override
    public boolean refresh() {
        this.cacheMapSequence.clear();
        this.cacheMap.clear();
        this.size = null;
        return true;
    }
}
