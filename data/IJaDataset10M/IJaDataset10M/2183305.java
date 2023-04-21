package ru.susu.algebra.util;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.ObjectUtils;
import ru.susu.algebra.pair.Pair;

/**
 * @author akargapolov
 * @since: 05.04.2011
 */
public class SortedArrayMap<K, V> extends AbstractMap<K, V> {

    private static int INITIAL_SIZE = 1;

    private static int INCREMENT = 5;

    private Object[] _keys = new Object[INITIAL_SIZE];

    private Object[] _values = new Object[INITIAL_SIZE];

    private int _size = 0;

    private Comparator _comparator;

    public SortedArrayMap(Comparator comparator) {
        _comparator = comparator;
    }

    @Override
    public int size() {
        return _size;
    }

    @Override
    public V put(K key, V value) {
        if (containsKey(key)) {
            int index = Arrays.binarySearch(_keys, 0, _size, key, _comparator);
            V oldValue = (V) _values[index];
            _values[index] = value;
            return oldValue;
        }
        int index = (Arrays.binarySearch(_keys, 0, _size, key, _comparator) + 1) * (-1);
        if (_size == _keys.length) {
            resize(_size + INCREMENT);
        }
        for (int i = _size; i > index; i--) {
            _keys[i] = _keys[i - 1];
            _values[i] = _values[i - 1];
        }
        _keys[index] = key;
        _values[index] = value;
        _size++;
        return null;
    }

    private void resize(int newLength) {
        _keys = Arrays.copyOf(_keys, newLength);
        _values = Arrays.copyOf(_values, newLength);
    }

    @Override
    public boolean containsKey(Object key) {
        return null != get(key);
    }

    @Override
    public V get(Object key) {
        int index = Arrays.binarySearch(_keys, 0, _size, key, _comparator);
        if (index >= 0 && ObjectUtils.equals(_keys[index], key)) return (V) _values[index];
        return null;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> result = new HashSet<Map.Entry<K, V>>();
        for (int index = 0; index < _size; index++) {
            Pair<K, V> pair = new Pair<K, V>((K) _keys[index], (V) _values[index]);
            result.add(pair);
        }
        return result;
    }

    @Override
    public void clear() {
        _keys = new Object[INITIAL_SIZE];
        _values = new Object[INITIAL_SIZE];
        _size = 0;
    }

    @Override
    public V remove(Object key) {
        V oldValue = get(key);
        if (oldValue != null) {
            int index = Arrays.binarySearch(_keys, 0, _size, key, _comparator);
            for (int i = index; i < _size - 1; i++) {
                _keys[i] = _keys[i + 1];
                _values[i] = _values[i + 1];
            }
            _keys[_size - 1] = _values[_size - 1] = null;
            _size--;
            if (_size < _keys.length - 2 * INCREMENT) resize(_keys.length - INCREMENT);
        }
        return oldValue;
    }
}
