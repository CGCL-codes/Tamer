package com.l2jserver.gameserver.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import com.l2jserver.gameserver.model.L2Object;

/**
 * This class is a highly optimized hashtable, where
 * keys are integers. The main goal of this class is to allow
 * concurent read/iterate and write access to this table,
 * plus minimal used memory.
 *
 * This class uses plain array as the table of values, and
 * keys are used to get position in the table. If the position
 * is already busy, we iterate to the next position, unil we
 * find the needed element or null.
 *
 * To iterate over the table (read access) we may simply iterate
 * throgh table array.
 *
 * In case we remove an element from the table, we check - if
 * the next position is null, we reset table's slot to null,
 * otherwice we assign it to a dummy value
 *
 *
 * @author mkizub
 *
 * @param <T> type of values stored in this hashtable
 */
public final class L2ObjectHashMap<T extends L2Object> extends L2ObjectMap<T> {

    private static final boolean TRACE = false;

    private static final boolean DEBUG = false;

    private static final int[] PRIMES = { 5, 7, 11, 17, 23, 29, 37, 47, 59, 71, 89, 107, 131, 163, 197, 239, 293, 353, 431, 521, 631, 761, 919, 1103, 1327, 1597, 1931, 2333, 2801, 3371, 4049, 4861, 5839, 7013, 8419, 10103, 12143, 14591, 17519, 21023, 25229, 30293, 36353, 43627, 52361, 62851, 75431, 90523, 108631, 130363, 156437, 187751, 225307, 270371, 324449, 389357, 467237, 560689, 672827, 807403, 968897, 1162687, 1395263, 1674319, 2009191, 2411033, 2893249, 3471899, 4166287, 4999559, 5999471, 7199369 };

    private T[] _table;

    private int[] _keys;

    private int _count;

    private static int getPrime(int min) {
        for (int i = 0; i < PRIMES.length; i++) {
            if (PRIMES[i] >= min) return PRIMES[i];
        }
        throw new OutOfMemoryError();
    }

    @SuppressWarnings("unchecked")
    public L2ObjectHashMap() {
        int size = PRIMES[0];
        _table = (T[]) new L2Object[size];
        _keys = new int[size];
        if (DEBUG) check();
    }

    @Override
    public int size() {
        return _count;
    }

    @Override
    public boolean isEmpty() {
        return _count == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized void clear() {
        int size = PRIMES[0];
        _table = (T[]) new L2Object[size];
        _keys = new int[size];
        _count = 0;
        if (DEBUG) check();
    }

    private void check() {
        if (DEBUG) {
            int cnt = 0;
            for (int i = 0; i < _table.length; i++) {
                L2Object obj = _table[i];
                if (obj == null) {
                    assert _keys[i] == 0 || _keys[i] == 0x80000000;
                } else {
                    cnt++;
                    assert obj.getObjectId() == (_keys[i] & 0x7FFFFFFF);
                }
            }
            assert cnt == _count;
        }
    }

    @Override
    public synchronized void put(T obj) {
        if (_count >= _table.length / 2) expand();
        final int hashcode = obj.getObjectId();
        assert hashcode > 0;
        int seed = hashcode;
        int incr = 1 + (((seed >> 5) + 1) % (_table.length - 1));
        int ntry = 0;
        int slot = -1;
        do {
            int pos = (seed % _table.length) & 0x7FFFFFFF;
            if (_table[pos] == null) {
                if (slot < 0) slot = pos;
                if (_keys[pos] >= 0) {
                    _keys[slot] = hashcode;
                    _table[slot] = obj;
                    _count++;
                    if (TRACE) System.err.println("ht: put obj id=" + hashcode + " at slot=" + slot);
                    if (DEBUG) check();
                    return;
                }
            } else {
                if (_table[pos] == obj) return;
                assert obj.getObjectId() != _table[pos].getObjectId();
                if (slot >= 0 && _keys[pos] > 0) {
                    _keys[slot] |= hashcode;
                    _table[slot] = obj;
                    _count++;
                    if (TRACE) System.err.println("ht: put obj id=" + hashcode + " at slot=" + slot);
                    if (DEBUG) check();
                    return;
                }
            }
            _keys[pos] |= 0x80000000;
            seed += incr;
        } while (++ntry < _table.length);
        if (DEBUG) check();
        throw new IllegalStateException();
    }

    @Override
    public synchronized void remove(T obj) {
        int hashcode = obj.getObjectId();
        assert hashcode > 0;
        int seed = hashcode;
        int incr = 1 + (((seed >> 5) + 1) % (_table.length - 1));
        int ntry = 0;
        do {
            int pos = (seed % _table.length) & 0x7FFFFFFF;
            if (_table[pos] == obj) {
                _keys[pos] &= 0x80000000;
                _table[pos] = null;
                _count--;
                if (TRACE) System.err.println("ht: remove obj id=" + hashcode + " from slot=" + pos);
                if (DEBUG) check();
                return;
            }
            if (_table[pos] == null && _keys[pos] >= 0) {
                if (DEBUG) check();
                return;
            }
            seed += incr;
        } while (++ntry < _table.length);
        if (DEBUG) check();
        throw new IllegalStateException();
    }

    @Override
    public T get(int id) {
        final int size = _table.length;
        if (id <= 0) return null;
        if (size <= 11) {
            for (int i = 0; i < size; i++) {
                if ((_keys[i] & 0x7FFFFFFF) == id) return _table[i];
            }
            return null;
        }
        int seed = id;
        int incr = 1 + (((seed >> 5) + 1) % (size - 1));
        int ntry = 0;
        do {
            int pos = (seed % size) & 0x7FFFFFFF;
            if ((_keys[pos] & 0x7FFFFFFF) == id) return _table[pos];
            if (_table[pos] == null && _keys[pos] >= 0) {
                return null;
            }
            seed += incr;
        } while (++ntry < size);
        return null;
    }

    @Override
    public boolean contains(T obj) {
        return get(obj.getObjectId()) != null;
    }

    @SuppressWarnings("unchecked")
    private void expand() {
        int newSize = getPrime(_table.length + 1);
        L2Object[] newTable = new L2Object[newSize];
        int[] newKeys = new int[newSize];
        next_entry: for (int i = 0; i < _table.length; i++) {
            L2Object obj = _table[i];
            if (obj == null) continue;
            final int hashcode = _keys[i] & 0x7FFFFFFF;
            assert hashcode == obj.getObjectId();
            int seed = hashcode;
            int incr = 1 + (((seed >> 5) + 1) % (newSize - 1));
            int ntry = 0;
            do {
                int pos = (seed % newSize) & 0x7FFFFFFF;
                if (newTable[pos] == null) {
                    assert newKeys[pos] == 0 && hashcode != 0;
                    newKeys[pos] = hashcode;
                    newTable[pos] = obj;
                    if (TRACE) System.err.println("ht: move obj id=" + hashcode + " from slot=" + i + " to slot=" + pos);
                    continue next_entry;
                }
                newKeys[pos] |= 0x80000000;
                seed += incr;
            } while (++ntry < newSize);
            throw new IllegalStateException();
        }
        _table = (T[]) newTable;
        _keys = newKeys;
        if (DEBUG) check();
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr(_table);
    }

    class Itr implements Iterator<T> {

        private final T[] _array;

        private int _nextIdx;

        private T _nextObj;

        private T _lastRet;

        Itr(T[] pArray) {
            this._array = pArray;
            for (; _nextIdx < _array.length; _nextIdx++) {
                _nextObj = _array[_nextIdx];
                if (_nextObj != null) return;
            }
        }

        public boolean hasNext() {
            return _nextObj != null;
        }

        public T next() {
            if (_nextObj == null) throw new NoSuchElementException();
            _lastRet = _nextObj;
            for (_nextIdx++; _nextIdx < _array.length; _nextIdx++) {
                _nextObj = _array[_nextIdx];
                if (_nextObj != null) break;
            }
            if (_nextIdx >= _array.length) _nextObj = null;
            return _lastRet;
        }

        public void remove() {
            if (_lastRet == null) throw new IllegalStateException();
            L2ObjectHashMap.this.remove(_lastRet);
        }
    }
}
