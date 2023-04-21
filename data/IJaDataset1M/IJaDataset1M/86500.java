package org.xhtmlrenderer.newtable;

import java.util.ArrayList;
import java.util.List;

/**
 * A row in the table grid.  The list of cells it maintains is always large
 * enough to set a table cell at every position in the row.  If there are no
 * colspans, rowspans, or missing cells, the grid row will exactly correspond
 * to the row in the original markup.  On the other hand, colspans may force
 * spanning cells to be inserted, rowspans will mean cells appear in more than
 * one grid row, and positions may be <code>null</code> if no cell occupies that
 * position in the grid.
 */
public class RowData {

    private List _row = new ArrayList();

    public List getRow() {
        return _row;
    }

    public void extendToColumnCount(int columnCount) {
        while (_row.size() < columnCount) {
            _row.add(null);
        }
    }

    public void splitColumn(int pos) {
        TableCellBox current = (TableCellBox) _row.get(pos);
        _row.add(pos + 1, current == null ? null : TableCellBox.SPANNING_CELL);
    }
}
