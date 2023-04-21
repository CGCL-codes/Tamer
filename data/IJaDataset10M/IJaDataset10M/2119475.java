package org.hsqldb.persist;

import java.io.IOException;
import org.hsqldb.CachedRow;
import org.hsqldb.Error;
import org.hsqldb.ErrorCode;
import org.hsqldb.HsqlException;
import org.hsqldb.Row;
import org.hsqldb.RowAction;
import org.hsqldb.Session;
import org.hsqldb.TableBase;
import org.hsqldb.index.DiskNode;
import org.hsqldb.index.Index;
import org.hsqldb.index.Node;
import org.hsqldb.lib.IntKeyHashMapConcurrent;
import org.hsqldb.lib.LongKeyHashMap;
import org.hsqldb.navigator.RowIterator;
import org.hsqldb.rowio.RowInputInterface;

public class RowStoreHybrid implements PersistentStore {

    final Session session;

    DataFileCacheSession cache;

    final TableBase table;

    final PersistentStoreCollection manager;

    private LongKeyHashMap accessorMap = new LongKeyHashMap();

    private int maxMemoryRowCount;

    private int memoryRowCount;

    private boolean isCached;

    private final boolean isTempTable;

    private IntKeyHashMapConcurrent rowIdMap = new IntKeyHashMapConcurrent();

    int rowIdSequence = 0;

    public RowStoreHybrid(Session session, PersistentStoreCollection manager, TableBase table) {
        this.session = session;
        this.manager = manager;
        this.table = table;
        this.maxMemoryRowCount = session.getResultMemoryRowCount();
        isTempTable = table.getTableType() == TableBase.TEMP_TABLE;
        manager.setStore(table, this);
    }

    public RowStoreHybrid(Session session, PersistentStoreCollection manager, TableBase table, boolean isCached) {
        this.session = session;
        this.manager = manager;
        this.table = table;
        this.maxMemoryRowCount = session.getResultMemoryRowCount();
        isTempTable = table.getTableType() == TableBase.TEMP_TABLE;
        if (isCached) {
            cache = session.sessionData.getResultCache();
            if (cache != null) {
                this.maxMemoryRowCount = Integer.MAX_VALUE;
                this.isCached = true;
                cache.storeCount++;
            }
        }
        manager.setStore(table, this);
    }

    public CachedObject get(int i) {
        try {
            if (isCached) {
                return cache.get(i, this, false);
            } else {
                return (CachedObject) rowIdMap.get(i);
            }
        } catch (HsqlException e) {
            return null;
        }
    }

    public CachedObject getKeep(int i) {
        try {
            if (isCached) {
                return cache.get(i, this, true);
            } else {
                return (CachedObject) rowIdMap.get(i);
            }
        } catch (HsqlException e) {
            return null;
        }
    }

    public int getStorageSize(int i) {
        try {
            if (isCached) {
                return cache.get(i, this, false).getStorageSize();
            } else {
                return 0;
            }
        } catch (HsqlException e) {
            return 0;
        }
    }

    private void add(CachedObject object) throws HsqlException {
        if (isCached) {
            int size = cache.rowOut.getSize((CachedRow) object) + table.getIndexCount() * DiskNode.SIZE_IN_BYTE;
            size = ((size + cache.cachedRowPadding - 1) / cache.cachedRowPadding) * cache.cachedRowPadding;
            object.setStorageSize(size);
            cache.add(object);
        }
    }

    public void restore(CachedObject row) throws HsqlException {
        row.restore();
        if (isCached) {
            cache.restore(row);
        }
    }

    public CachedObject get(RowInputInterface in) {
        try {
            if (isCached) {
                return new CachedRow(table, in);
            }
        } catch (HsqlException e) {
            return null;
        } catch (IOException e1) {
            return null;
        }
        return null;
    }

    public CachedObject getNewCachedObject(Session session, Object object) throws HsqlException {
        if (isCached) {
            Row row = new CachedRow(table, (Object[]) object);
            add(row);
            if (isTempTable) {
                RowAction.addAction(session, RowAction.ACTION_INSERT, table, row);
            }
            return row;
        } else {
            memoryRowCount++;
            if (memoryRowCount > maxMemoryRowCount) {
                changeToDiskTable();
                return getNewCachedObject(session, object);
            }
            Row row = new Row(table, (Object[]) object);
            int id = rowIdSequence++;
            row.setPos(id);
            rowIdMap.put(id, row);
            if (isTempTable) {
                RowAction.addAction(session, RowAction.ACTION_INSERT, table, row);
            }
            return row;
        }
    }

    public void removeAll() {
        accessorMap.clear();
        if (!isCached) {
            rowIdMap.clear();
        }
    }

    public void remove(int i) {
        try {
            if (isCached) {
                cache.remove(i, this);
            } else {
                rowIdMap.remove(i);
            }
        } catch (HsqlException e) {
        }
    }

    public void removePersistence(int i) {
    }

    public void release(int i) {
        if (isCached) {
            cache.release(i);
        }
    }

    public void commit(CachedObject row) {
    }

    public DataFileCache getCache() {
        return cache;
    }

    public void setCache(DataFileCache cache) {
        throw Error.runtimeError(ErrorCode.U_S0500, "");
    }

    public void release() {
        accessorMap.clear();
        if (isCached) {
            cache.storeCount--;
            if (cache.storeCount == 0) {
                cache.clear();
            }
            cache = null;
            isCached = false;
        } else {
            rowIdMap.clear();
        }
        manager.setStore(table, null);
    }

    public Object getAccessor(Object key) {
        Index index = (Index) key;
        Node node = (Node) accessorMap.get(index.getPersistenceId());
        if (node == null) {
            return null;
        }
        if (isCached) {
            Row row = (Row) get(node.getPos());
            node = row.getNode(index.getPosition());
        }
        return node;
    }

    public void setAccessor(Object key, Object accessor) {
        Index index = (Index) key;
        accessorMap.put(index.getPersistenceId(), accessor);
    }

    public void changeToDiskTable() throws HsqlException {
        cache = session.sessionData.getResultCache();
        if (cache != null) {
            RowIterator iterator = table.rowIterator(this);
            accessorMap.clear();
            isCached = true;
            cache.storeCount++;
            while (iterator.hasNext()) {
                Row row = iterator.getNextRow();
                Row newRow = (Row) getNewCachedObject(session, row.getData());
                table.indexRow(this, newRow);
            }
            rowIdMap.clear();
        }
        maxMemoryRowCount = Integer.MAX_VALUE;
    }
}
