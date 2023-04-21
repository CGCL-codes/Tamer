package org.freeworld.prilib.impl.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import org.freeworld.prilib.row.TableRow;
import org.freeworld.prilib.row.TableRowAdapter;
import org.freeworld.prilib.row.TableRowListener;
import org.freeworld.prilib.table.Table;
import org.freeworld.prilib.table.TableReMappable;
import org.freeworld.prilib.table.TableValidationException;

/**
 * <p>The first attempt at filtering row results, this table simply remaps row
 * ids and exports its table interface to only reflect rows that have passed
 * the matching criteria specified.</p>
 * 
 * <p>Since this is still a work in progress, much of this API may come and go
 * with new ideas on how to best optimize this code.</p>
 * 
 * TODO This class is not entirely thread safe
 * 
 * @author dchemko
 */
public class BaseRowFilterTable extends AbstractRowFilterTable implements TableReMappable, TableFilterable {

    private static final long serialVersionUID = 1L;

    private static final int INDEX_CACHE_SIZE_DEFAULT = 32;

    private List<TableRow> viewRowMapping;

    private List<TableRow> modelRowMapping;

    private Cache<TableRow, Integer> viewIndexRowCache = null;

    private Cache<TableRow, Integer> modelIndexRowCache = null;

    private transient TableRowListener filterRowListener;

    private List<Integer[]> addingCache = Collections.synchronizedList(new ArrayList<Integer[]>());

    private int indexCacheSize = INDEX_CACHE_SIZE_DEFAULT;

    public BaseRowFilterTable(Table table) {
        super(table);
    }

    @Override
    public synchronized void setTable(Table table) {
        if (getTable() != null) {
            removeLocalTableRowListener(getFilterRowListener());
            viewRowMapping = null;
            modelRowMapping = null;
        }
        super.setTable(table);
        if (table != null) {
            viewRowMapping = new ArrayList<TableRow>();
            modelRowMapping = new ArrayList<TableRow>();
            TableRow refRow = null;
            for (int i = 0; i < table.getRowCount(); i++) {
                refRow = table.getRow(i);
                modelRowMapping.add(refRow);
                viewRowMapping.add(refRow);
            }
            addLocalTableRowListener(getFilterRowListener());
        }
    }

    public void setIndexCacheSize(int indexCacheSize) {
        this.indexCacheSize = indexCacheSize;
        viewIndexRowCache = null;
        modelIndexRowCache = null;
    }

    public int getIndexCacheSize() {
        return indexCacheSize;
    }

    protected synchronized void filterRow(int modelIndex) {
        if (viewRowMapping == null) return;
        TableRow refRow = getTable().getRow(modelIndex);
        boolean filtered = shouldBeFiltered(refRow);
        int viewRow = getViewRow(refRow);
        if (viewRow == -1 && modelRowMapping.size() > modelIndex) {
            TableRow oldRow = modelRowMapping.get(modelIndex);
            viewRow = getViewRow(oldRow);
            modelRowMapping.set(modelIndex, refRow);
            getModelRowCache().clear();
            if (!filtered && viewRow != -1) {
                viewRowMapping.set(viewRow, refRow);
                getViewRowCache().clear();
                fireRowUpdated(viewRow, oldRow, refRow);
            }
        }
        if (filtered && viewRow != -1) {
            viewRowMapping.remove(viewRow);
            getViewRowCache().clear();
            fireRowDeleted(viewRow, refRow);
        } else if (!filtered && viewRow == -1) {
            int newRow = getNextModelView(refRow);
            viewRowMapping.add(newRow, refRow);
            getViewRowCache().clear();
            fireRowAdded(newRow, refRow);
        }
    }

    @Override
    public void filterRowUnSafe(int index) {
        filterRow(index);
    }

    protected synchronized void filterRowAdded(int modelIndex) {
        if (viewRowMapping == null) return;
        TableRow refRow = getTable().getRow(modelIndex);
        boolean filtered = shouldBeFiltered(refRow);
        if (!filtered) {
            int newRow = getNextModelView(refRow);
            viewRowMapping.add(newRow, refRow);
            getViewRowCache().clear();
            fireRowAdded(newRow, refRow);
        }
    }

    protected synchronized void filterRowDeleted(int modelIndex, TableRow deletedRow) {
        if (viewRowMapping == null) return;
        boolean filtered = shouldBeFiltered(deletedRow);
        int viewRow = getViewRow(deletedRow);
        if (!filtered && viewRow != -1) {
            viewRowMapping.remove(viewRow);
            getViewRowCache().clear();
            fireRowDeleted(viewRow, deletedRow);
        }
    }

    @Override
    protected boolean shouldBeFiltered(TableRow row) {
        for (TableRowFilter filter : getRowFilters()) if (filter.isFiltered(row)) return true;
        return false;
    }

    @Override
    public synchronized int getRowCount() {
        if (viewRowMapping == null) return getTable().getRowCount();
        return viewRowMapping.size();
    }

    @Override
    public synchronized int getBaseRowFromExposedRowId(int viewRowIndex) {
        return getBaseRowFromExposedRowIdUnsafe(viewRowIndex);
    }

    @Override
    protected int getBaseRowFromExposedRowIdUnsafe(int viewRowIndex) {
        if (viewRowMapping == null) return viewRowIndex;
        return getModelFromView(viewRowIndex);
    }

    @Override
    public synchronized int getExposedRowFromBaseRowId(int modelRowIndex) {
        return getExposedRowFromBaseRowIdUnsafe(modelRowIndex);
    }

    @Override
    protected int getExposedRowFromBaseRowIdUnsafe(int modelRowIndex) {
        if (viewRowMapping == null) return modelRowIndex;
        synchronized (addingCache) {
            for (Integer[] value : addingCache) {
                if (value[0] == modelRowIndex) return value[1];
            }
        }
        if (modelRowIndex > getTable().getRowCount() - 1) return viewRowMapping.size();
        return getViewFromModel(modelRowIndex);
    }

    protected TableRowListener getFilterRowListener() {
        if (filterRowListener == null) {
            filterRowListener = new BaseRowFilterTableRowListener();
        }
        return filterRowListener;
    }

    protected class BaseRowFilterTableRowListener extends TableRowAdapter {

        @Override
        public void rowAdding(Table table, int rowNumber, TableRow row) throws TableValidationException {
            synchronized (BaseRowFilterTable.this) {
                if (!shouldBeFiltered(row)) {
                    int viewIndex = -1;
                    if (table.getRowCount() > rowNumber) {
                        TableRow oldRow = table.getRow(rowNumber);
                        viewIndex = getViewRow(oldRow);
                    }
                    if (viewIndex < 0) viewIndex = getRowCount() > 0 ? getRowCount() - 1 : 0;
                    addingCache.add(new Integer[] { rowNumber, viewIndex });
                    fireRowAdding(viewIndex, row);
                } else {
                }
            }
        }

        @Override
        public void rowAdded(Table table, int rowNumber, TableRow row) {
            synchronized (BaseRowFilterTable.this) {
                if (!shouldBeFiltered(row)) {
                    synchronized (addingCache) {
                        for (int i = 0; i < addingCache.size(); i++) {
                            Integer[] ints = addingCache.get(i);
                            if (ints[0] == rowNumber) {
                                addingCache.remove(i);
                                break;
                            }
                        }
                    }
                }
                modelRowMapping.add(rowNumber, row);
                getModelRowCache().clear();
                filterRowAdded(rowNumber);
            }
        }

        @Override
        public void rowDeleting(Table table, int rowNumber, TableRow row) throws TableValidationException {
            int viewIndex = getViewRow(row);
            if (viewIndex != -1) fireRowDeleting(viewIndex, row);
        }

        @Override
        public void rowDeleted(Table table, int rowNumber, TableRow row) {
            filterRowDeleted(rowNumber, row);
        }

        @Override
        public void rowUpdating(Table table, int rowNumber, TableRow oldData, TableRow newData) throws TableValidationException {
            synchronized (BaseRowFilterTable.this) {
                int viewIndex = getViewFromModel(rowNumber);
                if (viewIndex != -1) {
                    fireRowUpdating(viewIndex, oldData, newData);
                }
            }
        }

        @Override
        public void rowUpdated(Table table, int rowNumber, TableRow oldData, TableRow newData) {
            synchronized (BaseRowFilterTable.this) {
                int viewIndex = getViewFromModel(rowNumber);
                if (viewIndex != -1) {
                    viewRowMapping.set(viewIndex, newData);
                    modelRowMapping.set(rowNumber, newData);
                    fireRowUpdated(viewIndex, oldData, newData);
                }
                filterRow(rowNumber);
            }
        }

        @Override
        public void rowReverted(int operation, Table table, int rowNumber, TableRow oldData, TableRow newData, TableValidationException exception) {
            int viewIndex = getViewFromModel(rowNumber);
            switch(operation) {
                case ROW_OPERATION_INSERTED:
                    addingCache.remove(rowNumber);
                    if (viewIndex != -1) fireRowReverted(operation, rowNumber, oldData, newData, exception);
                    break;
                case ROW_OPERATION_UPDATED:
                    if (viewIndex != -1) fireRowReverted(operation, rowNumber, oldData, newData, exception);
                    break;
                case ROW_OPERATION_DELETED:
                    if (viewIndex != -1) fireRowReverted(operation, rowNumber, oldData, newData, exception);
                    break;
            }
        }
    }

    protected synchronized int getViewRow(TableRow row) {
        Cache<TableRow, Integer> cache = getViewRowCache();
        Integer cacheResult = cache.get(row);
        if (cacheResult != null) return cacheResult;
        int retr = viewRowMapping.indexOf(row);
        if (retr > -1) cache.put(row, retr);
        return retr;
    }

    protected synchronized int getModelRow(TableRow row) {
        Cache<TableRow, Integer> cache = getModelRowCache();
        Integer cacheResult = cache.get(row);
        if (cacheResult != null) return cacheResult;
        int retr = modelRowMapping.indexOf(row);
        if (retr > -1) cache.put(row, retr);
        return retr;
    }

    protected synchronized int getViewFromModel(int rowNumber) {
        return getViewRow(modelRowMapping.get(rowNumber));
    }

    protected synchronized int getModelFromView(int rowNumber) {
        if (rowNumber >= viewRowMapping.size()) {
            if (viewRowMapping.size() > 0) return getNextModelView(viewRowMapping.get(viewRowMapping.size() - 1)); else return 0;
        }
        return getModelRow(viewRowMapping.get(rowNumber));
    }

    protected synchronized int getNextModelView(TableRow row) {
        int modelStartRow = modelRowMapping.indexOf(row);
        for (int i = modelStartRow + 1; i < modelRowMapping.size(); i++) {
            int modelRow = getViewRow(modelRowMapping.get(i));
            if (modelRow > -1) return modelRow;
        }
        return viewRowMapping.size();
    }

    private Cache<TableRow, Integer> getViewRowCache() {
        if (viewIndexRowCache == null) {
            viewIndexRowCache = new Cache<TableRow, Integer>(getIndexCacheSize());
        }
        return viewIndexRowCache;
    }

    private Cache<TableRow, Integer> getModelRowCache() {
        if (modelIndexRowCache == null) {
            modelIndexRowCache = new Cache<TableRow, Integer>(getIndexCacheSize());
        }
        return modelIndexRowCache;
    }

    private static class Cache<K, V> extends LinkedHashMap<K, V> {

        private static final long serialVersionUID = 1L;

        private int mMaxEntries;

        public Cache(int maxEntries) {
            super(maxEntries + 1, 1, false);
            mMaxEntries = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
            return size() > mMaxEntries;
        }
    }
}
