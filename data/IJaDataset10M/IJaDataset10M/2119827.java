package org.wings.table;

import java.util.Collection;
import org.wings.event.STableColumnModelListener;

/**
 * @see  javax.swing.table.TableColumnModel
 */
public interface STableColumnModel {

    void addColumn(STableColumn aColumn);

    void removeColumn(STableColumn column);

    void moveColumn(int columnIndex, int newIndex);

    void setColumnMargin(int newMargin);

    int getColumnCount();

    Collection getColumns();

    int getColumnIndex(Object columnIdentifier);

    STableColumn getColumn(int columnIndex);

    int getColumnMargin();

    /**
     * @return The total width of this table including the unit
     */
    String getTotalColumnWidth();

    /**
     * Adds a listener for table column model events.
     *
     * @param x  a <code>STableColumnModelListener</code> object
     */
    void addColumnModelListener(STableColumnModelListener x);

    /**
     * Removes a listener for table column model events.
     *
     * @param x  a <code>STableColumnModelListener</code> object
     */
    void removeColumnModelListener(STableColumnModelListener x);
}
