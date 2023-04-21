package org.wct.table;

import org.wct.*;

/**
 * This is the interface implemented by the table cell editors
 * @author  juliano
 * @version 0.1
 */
public interface TableCellEditor {

    /**
     * Returns a component suitable for editing the specified cell
     */
    public Component getTableCellEditorComponent(Table table, Object value, boolean isSelected, int row, int col);

    /**
     * Returns the value of the cell editor
     */
    public Object getCellEditorValue();

    /**
     * Adds a listener to the list thet is notified whenever the editor value gets updated.
     */
    public void addCellEditorListener(javax.swing.event.CellEditorListener l);

    public void removeCellEditorListener(javax.swing.event.CellEditorListener l);
}
