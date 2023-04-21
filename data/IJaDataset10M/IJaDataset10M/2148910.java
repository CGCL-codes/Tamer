package org.hsqldb.persist;

import org.hsqldb.Session;
import org.hsqldb.TableBase;
import org.hsqldb.index.Index;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.error.Error;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReadWriteLock;
import org.hsqldb.Row;
import org.hsqldb.RowAVL;
import org.hsqldb.index.NodeAVL;
import org.hsqldb.navigator.RowIterator;

public class RowStoreAVLHybridExtended extends RowStoreAVLHybrid {

    ReadWriteLock lock = new ReentrantReadWriteLock();

    Lock readLock = lock.readLock();

    Lock writeLock = lock.writeLock();

    public RowStoreAVLHybridExtended(Session session, PersistentStoreCollection manager, TableBase table, boolean diskBased) {
        super(session, manager, table, diskBased);
    }

    private RowStoreAVLHybridExtended(Session session, TableBase table) {
        super(session, table);
    }

    public CachedObject getNewCachedObject(Session session, Object object, boolean tx) {
        if (indexList != table.getIndexList()) {
            resetAccessorKeys(table.getIndexList());
        }
        return super.getNewCachedObject(session, object, tx);
    }

    public void indexRow(Session session, Row row) {
        NodeAVL node = ((RowAVL) row).getNode(0);
        int count = 0;
        while (node != null) {
            count++;
            node = node.nNext;
        }
        if (count != indexList.length) {
            resetAccessorKeys(table.getIndexList());
            ((RowAVL) row).setNewNodes(this);
        }
        super.indexRow(session, row);
    }

    public CachedObject getAccessor(Index key) {
        int position = key.getPosition();
        if (position >= accessorList.length || indexList[position] != key) {
            resetAccessorKeys(table.getIndexList());
            return getAccessor(key);
        }
        return accessorList[position];
    }

    public synchronized void resetAccessorKeys(Index[] keys) {
        if (indexList.length == 0 || accessorList[0] == null) {
            indexList = keys;
            accessorList = new CachedObject[indexList.length];
            return;
        }
        if (isCached) {
            resetAccessorKeysForCached();
        }
        super.resetAccessorKeys(keys);
    }

    private void resetAccessorKeysForCached() {
        RowStoreAVLHybrid tempStore = new RowStoreAVLHybridExtended(session, table);
        RowIterator iterator = table.rowIterator(this);
        while (iterator.hasNext()) {
            Row row = iterator.getNextRow();
            Row newRow = (Row) tempStore.getNewCachedObject(session, row.getData(), false);
            tempStore.indexRow(session, newRow);
        }
        indexList = tempStore.indexList;
        accessorList = tempStore.accessorList;
    }

    public void lock() {
        writeLock.lock();
    }

    public void unlock() {
        writeLock.unlock();
    }

    void lockIndexes() {
        for (int i = 0; i < indexList.length; i++) {
        }
    }

    void unlockIndexes(Index[] array) {
        for (int i = 0; i < array.length; i++) {
        }
    }
}
