package org.identifylife.descriptlet.store.harvest.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.identifylife.core.utils.CollectionUtils;

/**
 * @author dbarnier
 *
 */
public class MultiHashMap<K, V> implements MultiMap<K, V> {

    private Map<K, Map<K, V>> rowMap = new HashMap<K, Map<K, V>>();

    @Override
    public void clear() {
        List<K> rows = new ArrayList<K>(rowMap.keySet());
        for (K row : rows) {
            rowMap.get(row).clear();
        }
        rowMap.clear();
    }

    @Override
    public boolean contains(K row, K col) {
        if (rowMap.containsKey(row)) {
            return rowMap.get(row).containsKey(col);
        }
        return false;
    }

    @Override
    public V get(K row, K col) {
        if (rowMap.containsKey(row)) {
            return rowMap.get(row).get(col);
        }
        return null;
    }

    @Override
    public Map<K, V> getRow(K row) {
        Map<K, V> result = new HashMap<K, V>();
        if (rowMap.containsKey(row)) {
            result.putAll(rowMap.get(row));
        }
        return result;
    }

    @Override
    public Map<K, V> getCol(K col) {
        Map<K, V> result = new HashMap<K, V>();
        for (K row : rowMap.keySet()) {
            Map<K, V> colMap = rowMap.get(row);
            if (colMap.containsKey(col)) {
                result.put(row, colMap.get(col));
            }
        }
        return result;
    }

    @Override
    public Set<K> colSet(K row) {
        Map<K, V> colMap = rowMap.get(row);
        if (colMap != null) {
            return colMap.keySet();
        }
        return null;
    }

    @Override
    public int colCount(K row) {
        Map<K, V> colMap = rowMap.get(row);
        if (colMap != null) {
            return colMap.size();
        }
        return 0;
    }

    @Override
    public Set<K> rowSet() {
        return rowMap.keySet();
    }

    @Override
    public int rowCount() {
        return rowMap.size();
    }

    @Override
    public Collection<V> colValues(K col) {
        Map<K, V> rowMap = getCol(col);
        if (rowMap != null) {
            return rowMap.values();
        }
        return CollectionUtils.newArrayList();
    }

    @Override
    public Collection<V> rowValues(K row) {
        Map<K, V> colMap = rowMap.get(row);
        if (colMap != null) {
            return colMap.values();
        }
        return CollectionUtils.newArrayList();
    }

    @Override
    public V put(K row, K col, V value) {
        Map<K, V> colMap = rowMap.get(row);
        if (colMap == null) {
            rowMap.put(row, colMap = new HashMap<K, V>());
        }
        return colMap.put(col, value);
    }

    @Override
    public V remove(K row, K col) {
        if (rowMap.containsKey(row)) {
            Map<K, V> colMap = rowMap.get(row);
            if (colMap.containsKey(col)) {
                return colMap.remove(col);
            }
        }
        return null;
    }

    @Override
    public int size() {
        int size = 0;
        for (K row : rowMap.keySet()) {
            size += rowMap.get(row).size();
        }
        return size;
    }
}
