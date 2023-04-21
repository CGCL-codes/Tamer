package org.objectwiz.ui.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.LogFactory;
import org.objectwiz.util.CollectionUtils;

/**
 * A table manager that do not correspond to any real table but does store
 * all the information that it is provided with.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class TableManagerStub implements TableManager {

    private List<String> columnIds;

    private List<String> visibleColumns;

    private Map<String, Integer> columnsWidth = new HashMap();

    public TableManagerStub(TableView tv) {
        this.columnIds = new ArrayList();
        this.visibleColumns = new ArrayList();
        this.columnIds.addAll(Arrays.asList(tv.getColumnsIds()));
        this.visibleColumns.addAll(this.columnIds);
    }

    private void ensureValidColumn(String columnId) {
        if (!columnIds.contains(columnId)) {
            throw new IllegalArgumentException("No such column: " + columnId);
        }
    }

    public void applyStatus(TableManager table) {
        table.setColumnsVisible(true);
        int i = 0;
        for (String id : columnIds) try {
            table.moveColumn(id, i++);
            table.setColumnVisible(id, visibleColumns.contains(id));
            if (columnsWidth.containsKey(id)) {
                table.setColumnWidth(id, columnsWidth.get(id));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error applying status to: " + id, e);
        }
    }

    @Override
    public float getTableWidth() {
        return 100;
    }

    @Override
    public List<String> getColumns(boolean addHidden) {
        return addHidden ? columnIds : new ArrayList(CollectionUtils.retainAll(columnIds, visibleColumns));
    }

    @Override
    public void moveColumn(String columnId, int newIndex) {
        ensureValidColumn(columnId);
        this.columnIds.remove(columnId);
        this.columnIds.add(newIndex, columnId);
    }

    @Override
    public void setColumnVisible(String columnId, boolean visible) {
        ensureValidColumn(columnId);
        if (visible) {
            if (visibleColumns.contains(columnId)) return;
            visibleColumns.add(columnId);
        } else {
            visibleColumns.remove(columnId);
        }
    }

    @Override
    public boolean isColumnVisible(String columnId) {
        if (visibleColumns.contains(columnId)) return true;
        return false;
    }

    @Override
    public void setColumnWidth(String id, int width) {
        columnsWidth.put(id, width);
    }

    @Override
    public int getColumnWidth(String id) {
        ensureValidColumn(id);
        if (columnsWidth.containsKey(id)) {
            return columnsWidth.get(id);
        } else {
            return 100 / columnIds.size();
        }
    }

    @Override
    public void setColumnsVisible(boolean isVisible) {
        this.visibleColumns.clear();
        if (isVisible) this.visibleColumns.addAll(columnIds);
    }
}
