package org.plazmaforge.framework.client.swing.gui.table;

/**
 * Created on 06.06.2006
 */
public class SortParameter {

    private int columnIndex = -1;

    private boolean ascending;

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public int intAscending() {
        return ascending ? 1 : -1;
    }
}
