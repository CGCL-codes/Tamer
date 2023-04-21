package com.daffodilwoods.daffodildb.server.datasystem.interfaces;

import java.util.*;
import com.daffodilwoods.daffodildb.server.datasystem.indexsystem.*;
import com.daffodilwoods.daffodildb.server.datasystem.utility.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.daffodildb.utils.comparator.*;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.database.utility.P;

/**
 *
 * <p>Title: UnionIterator</p>
 * <p>Description:Objective of the union iterator is to manage the operations of both the iterators it has.
 */
public class UnionIterator implements _TableIterator, _UniqueIterator {

    /**
     * Used to maintain the pointer of the UnionIterator
     */
    protected _IndexIterator[] iterators;

    protected Object currentKey;

    protected SuperComparator comparator;

    protected int status;

    protected static final int LENGTH = 2;

    protected boolean recordLoaded;

    /**
     * Constructs the Union iterator with an array of two index iterators and an index table.
     * @param iterators0 is an array of two index iterators one to iterate on the memory table and the other to iterate on file table..
     * @param table0 is an index table that maintains the operations on memory and the file table.
     */
    public UnionIterator(_IndexIterator[] iterators0) {
        iterators = iterators0;
        for (int i = 0; i < LENGTH; i++) {
            if (iterators[i] instanceof _Iterator) new Exception(" IT'S IMPOSSIBLE ").printStackTrace();
        }
        comparator = ((_IndexIteratorInfo) iterators[1]).getComparator();
        status = BEFOREFIRST;
    }

    /**
     * set it's pointer to the first record in the table
     * @return true if it gets the key corresponding to the first record in the table.
     */
    public boolean first() throws DException {
        recordLoaded = false;
        int hasFirst = 0;
        _Key[] keys = new _Key[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            if (iterators[i].first()) {
                hasFirst += 1;
                keys[i] = (_Key) iterators[i].getKey();
            }
        }
        return hasFirst == 0 ? setKeyAndStatus(BEFOREFIRST, null, false) : setKeyAndStatus(VALIDSTATE, new UnionKey(getLowestKey(keys), keys, true), true);
    }

    /**
     * sets the pointer to the last record in the table
     * @return true if it gets the key corresponding to the last record in the table.
     */
    public boolean last() throws DException {
        recordLoaded = false;
        int hasLast = 0;
        _Key[] keys = new _Key[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            if (iterators[i].last()) {
                hasLast += 1;
                keys[i] = (_Key) iterators[i].getKey();
            }
        }
        return hasLast == 0 ? setKeyAndStatus(AFTERLAST, null, false) : setKeyAndStatus(VALIDSTATE, new UnionKey(getHighestKey(keys), keys, false), true);
    }

    /**
     * sets the pointer to the next record in the table
     * @return true if it gets the key corresponding to the next record in the table.
     */
    public boolean next() throws DException {
        if (status != VALIDSTATE) return status == AFTERLAST ? false : first();
        recordLoaded = false;
        UnionKey unionKey = (UnionKey) currentKey;
        BitSet bitSet = unionKey.getBitSet();
        int hasNext = 0;
        _Key[] keys = new _Key[LENGTH];
        boolean direction = unionKey.getDirection();
        for (int i = 0; i < LENGTH; i++) {
            if (bitSet.get(i) || !direction) {
                if (iterators[i].next()) {
                    hasNext++;
                    keys[i] = (_Key) iterators[i].getKey();
                }
            } else {
                hasNext += (keys[i] = (_Key) iterators[i].getKey()) == null ? 0 : 1;
            }
        }
        return hasNext == 0 ? setKeyAndStatus(AFTERLAST, null, false) : setKeyAndStatus(VALIDSTATE, new UnionKey(getLowestKey(keys), keys, true), true);
    }

    /**
     * sets the pointer to the previous record in the table.
     * @return true if it gets the key corresponding to the previous record in the table.
     */
    public boolean previous() throws DException {
        if (status != VALIDSTATE) return status == BEFOREFIRST ? false : last();
        recordLoaded = false;
        UnionKey unionKey = (UnionKey) currentKey;
        BitSet bitSet = unionKey.getBitSet();
        int hasPrevious = 0;
        _Key[] keys = new _Key[LENGTH];
        boolean direction = unionKey.getDirection();
        for (int i = 0; i < LENGTH; i++) {
            if (bitSet.get(i) || direction) {
                if (iterators[i].previous()) {
                    hasPrevious += 1;
                    keys[i] = (_Key) iterators[i].getKey();
                }
            } else {
                keys[i] = (_Key) iterators[i].getKey();
                hasPrevious += keys[i] == null ? 0 : 1;
            }
        }
        return hasPrevious == 0 ? setKeyAndStatus(BEFOREFIRST, null, false) : setKeyAndStatus(VALIDSTATE, new UnionKey(getHighestKey(keys), keys, false), true);
    }

    public Object getKey() throws DException {
        return currentKey;
    }

    /**
     * moves the pointer to the given key in the table
     * @param key Key to which the iterator will move its pointer
     * @return true true if it gets the record corresponding to the given key in the table.
     */
    public void move(Object key) throws DException {
        if (key == null) return;
        recordLoaded = false;
        UnionKey unionKey = (UnionKey) key;
        currentKey = key;
        status = VALIDSTATE;
        Object[] keys = unionKey.getKeys();
        for (int i = 0; i < LENGTH; i++) iterators[i].move(keys[i]);
    }

    /**
     * retreives the value of the record at the current pointer from the table.
     * @param columns array of columns whose value is to be retreived.
     * @return an array of values corresponding to those columns.
     */
    public Object getColumnValues(int[] columns) throws DException {
        if (status != VALIDSTATE) {
            throw new DException("DSE2019", new Object[] { new Integer(status) });
        }
        BitSet bitSet = ((UnionKey) currentKey).getBitSet();
        for (int i = 0; i < LENGTH; i++) if (bitSet.get(i)) return iterators[i].getColumnValues(columns);
        throw new DException("DSE2047", null);
    }

    public void show(boolean flag) throws DException {
        ArrayList list = new ArrayList();
        if (first()) {
            do {
                Object obj = getColumnValues();
                if (flag) ;
                list.add(obj);
            } while (next());
        } else ;
    }

    public _KeyColumnInformation[] getKeyColumnInformations() throws DException {
        try {
            return iterators[1].getKeyColumnInformations();
        } catch (NullPointerException npe) {
            return iterators[0].getKeyColumnInformations();
        }
    }

    public int getBtreeIndex() throws DException {
        return ((IndexTableIterator) iterators[0]).getBtreeIndex();
    }

    public _Record getRecord() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getRecord() not yet implemented.");
    }

    protected BitSet getLowestKey(Object[] values) throws DException {
        BitSet flags = new BitSet();
        if (values[0] == null) {
            flags.set(1);
            return flags;
        } else if (values[1] == null) {
            flags.set(0);
            return flags;
        }
        Object keyValues0 = ((_Key) values[0]).getKeyValue();
        Object keyValues1 = ((_Key) values[1]).getKeyValue();
        int cmp = comparator.compare(keyValues0, keyValues1);
        if (cmp == 0) {
            flags.set(0);
            flags.set(1);
        } else if (cmp < 0) flags.set(0); else flags.set(1);
        return flags;
    }

    protected BitSet getHighestKey(Object[] values) throws DException {
        BitSet flags = new BitSet();
        if (values[0] == null) {
            flags.set(1);
            return flags;
        } else if (values[1] == null) {
            flags.set(0);
            return flags;
        }
        Object keyValues0 = ((_Key) values[0]).getKeyValue();
        Object keyValues1 = ((_Key) values[1]).getKeyValue();
        int cmp = comparator.compare(keyValues0, keyValues1);
        if (cmp == 0) {
            flags.set(0);
            flags.set(1);
        } else if (cmp > 0) flags.set(0); else flags.set(1);
        return flags;
    }

    protected Object getKeyToLocate(Object key, _IndexPredicate[] condition) throws DException {
        Object keyToLocate = null;
        try {
            Object[] kee = (Object[]) key;
            if (kee.length != condition.length) {
                Object[] temp = new Object[condition.length];
                for (int i = 0; i < temp.length; i++) temp[i] = kee[i];
                keyToLocate = temp;
            } else keyToLocate = kee;
        } catch (ClassCastException ex) {
            keyToLocate = key;
        }
        return keyToLocate;
    }

    protected boolean setKeyAndStatus(int num, Object key, boolean flag) {
        status = num;
        currentKey = key;
        return flag;
    }

    protected void setLowestKey() throws DException {
        int length = iterators.length;
        _Key[] keys = new _Key[length];
        for (int i = 0; i < length; i++) {
            keys[i] = iterators[i] != null ? (_Key) iterators[i].getKey() : null;
        }
        BitSet set = getLowestKey(keys);
        currentKey = set == null ? currentKey : new UnionKey(set, keys, true);
    }

    protected void setHighestKey() throws DException {
        int length = iterators.length;
        _Key[] keys = new _Key[length];
        for (int i = 0; i < length; i++) {
            keys[i] = iterators[i] != null ? (_Key) iterators[i].getKey() : null;
        }
        BitSet set = getHighestKey(keys);
        currentKey = set == null ? currentKey : new UnionKey(set, keys, false);
    }

    protected int compareToGetLowest(Object indexKey, Object otherKey) throws DException {
        return indexKey == null ? -1 : otherKey == null ? 1 : comparator.compare(indexKey, otherKey);
    }

    protected int compareToGetHighest(Object indexKey, Object otherKey) throws DException {
        return indexKey == null ? 1 : otherKey == null ? -1 : comparator.compare(indexKey, otherKey);
    }

    public Object getColumnValues(int column) throws com.daffodilwoods.database.resource.DException {
        if (status != VALIDSTATE) throw new DException("DSE2019", new Object[] { new Integer(status) });
        BitSet bitSet = ((UnionKey) currentKey).getBitSet();
        for (int i = 0; i < LENGTH; i++) if (bitSet.get(i)) return iterators[i].getColumnValues(column);
        throw new DException("DSE2047", null);
    }

    public Object getColumnValues() throws com.daffodilwoods.database.resource.DException {
        if (status != VALIDSTATE) throw new DException("DSE2019", new Object[] { new Integer(status) });
        BitSet bitSet = ((UnionKey) currentKey).getBitSet();
        for (int i = 0; i < LENGTH; i++) if (bitSet.get(i)) return iterators[i].getColumnValues();
        throw new DException("DSE2047", null);
    }

    public void moveOnActualKey(Object parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method moveOnActualKey() not yet implemented.");
    }

    public Object getActualKey() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getActualKey() not yet implemented.");
    }

    public _TableCharacteristics getTableCharacteristics() throws DException {
        throw new java.lang.UnsupportedOperationException("Method getTableCharacteristics() not yet implemented.");
    }

    public _IndexInformation[] getUniqueInformation() throws DException {
        throw new java.lang.UnsupportedOperationException("Method getUniqueInformation() not yet implemented.");
    }

    public Object getColumnValues(_Reference reference) throws com.daffodilwoods.database.resource.DException {
        if (status != VALIDSTATE) throw new DException("DSE2019", new Object[] { new Integer(status) });
        BitSet bitSet = ((UnionKey) currentKey).getBitSet();
        for (int i = 0; i < LENGTH; i++) if (bitSet.get(i)) return iterators[i].getColumnValues(reference);
        throw new DException("DSE2047", null);
    }

    public Object getColumnValues(_Reference[] references) throws com.daffodilwoods.database.resource.DException {
        if (status != VALIDSTATE) {
            throw new DException("DSE2019", new Object[] { new Integer(status) });
        }
        BitSet bitSet = ((UnionKey) currentKey).getBitSet();
        for (int i = 0; i < LENGTH; i++) if (bitSet.get(i)) return iterators[i].getColumnValues(references);
        throw new DException("DSE2047", null);
    }

    protected int getColumn(_Reference references, _TableCharacteristics tc) throws DException {
        int column = -1;
        try {
            column = references.getIndex();
        } catch (DException de) {
            if (!de.getDseCode().equalsIgnoreCase("DSE565")) throw de;
            column = tc.getIndexForColumnName(references.getColumn());
            references.setIndex(column);
        }
        return column;
    }

    public byte[] getByteKey() throws DException {
        throw new java.lang.UnsupportedOperationException("Method getByteKey() not yet implemented.");
    }

    public void moveByteKey(byte[] key) throws DException {
        throw new java.lang.UnsupportedOperationException("Method moveByteKey() not yet implemented.");
    }
}
