package org.hsqldb.persist;

import java.io.IOException;
import org.hsqldb.CachedDataRow;
import org.hsqldb.HsqlException;
import org.hsqldb.Table;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.index.Index;
import org.hsqldb.index.Node;
import org.hsqldb.lib.LongKeyHashMap;
import org.hsqldb.Session;
import org.hsqldb.RowAction;
import org.hsqldb.Row;

public class RowStoreText extends RowStoreCached implements PersistentStore {

    public RowStoreText(PersistentStoreCollection manager, Table table) {
        super(manager, null, table);
    }

    void add(CachedObject object) throws HsqlException {
        int size = cache.rowOut.getSize((CachedDataRow) object);
        size = ((size + cache.cachedRowPadding - 1) / cache.cachedRowPadding) * cache.cachedRowPadding;
        object.setStorageSize(size);
        cache.add(object);
    }

    public CachedObject get(RowInputInterface in) {
        try {
            return new CachedDataRow(table, in);
        } catch (HsqlException e) {
            return null;
        } catch (IOException e1) {
            return null;
        }
    }

    public CachedObject getNewCachedObject(Session session, Object object) throws HsqlException {
        Row row = new CachedDataRow(table, (Object[]) object);
        add(row);
        RowAction.addAction(session, RowAction.ACTION_INSERT, table, row);
        return row;
    }

    public void removeAll() {
    }

    public void remove(int i) {
        try {
            if (cache != null) {
                cache.remove(i, this);
            }
        } catch (HsqlException e) {
        }
    }

    public void removePersistence(int i) {
        try {
            if (cache != null) {
                cache.removePersistence(i);
            }
        } catch (HsqlException e) {
        }
    }

    public void release(int i) {
        if (cache != null) {
            cache.release(i);
        }
    }

    public void commit(CachedObject row) {
        try {
            if (cache != null) {
                cache.saveRow(row);
            }
        } catch (HsqlException e1) {
        }
    }

    public void release() {
        accessorMap.clear();
        table.database.logger.closeTextCache((Table) table);
        cache = null;
    }

    public Object getAccessor(Object key) {
        Index index = (Index) key;
        return (Node) accessorMap.get(index.getPersistenceId());
    }
}
