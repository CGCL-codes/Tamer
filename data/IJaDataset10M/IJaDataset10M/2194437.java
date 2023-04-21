package com.anthonyeden.lib.util;

import javax.swing.table.*;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

/** Based on work by Philip Milne. 

    @author Anthony Eden
*/
public class TableMap extends AbstractTableModel implements TableModelListener {

    /** The wrapped TableModel. */
    protected TableModel model;

    /** Get the TableModel which is wrapped by this map.
    
        @return The TableModel
    */
    public TableModel getModel() {
        return model;
    }

    /** Set the TableModel which is wrapped by this map.
    
        @param model The TableModel
    */
    public void setModel(TableModel model) {
        this.model = model;
        model.addTableModelListener(this);
    }

    /** Get the value at the given row and column.
    
        @param aRow The row
        @param aColumn The column
        @return The value
    */
    public Object getValueAt(int aRow, int aColumn) {
        return model.getValueAt(aRow, aColumn);
    }

    /** Set the value at the given row and column.
    
        @param aValue The value
        @param aRow The row
        @param aColumn The column
    */
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        model.setValueAt(aValue, aRow, aColumn);
    }

    /** Get the row count.
    
        @return The row count
    */
    public int getRowCount() {
        return (model == null) ? 0 : model.getRowCount();
    }

    /** Get the column count.
    
        @return The column count
    */
    public int getColumnCount() {
        return (model == null) ? 0 : model.getColumnCount();
    }

    /** Get the column name for the given index.
    
        @param aColumn The column
        @return The column name
    */
    public String getColumnName(int aColumn) {
        return model.getColumnName(aColumn);
    }

    /** Get the column class for the given column index.
    
        @param aColumn The column
        @return The column class
    */
    public Class getColumnClass(int aColumn) {
        return model.getColumnClass(aColumn);
    }

    /** Return true if the cell at the given row and column is editable.
    
        @param row The row
        @param column The column
        @return True if the cell is editable
    */
    public boolean isCellEditable(int row, int column) {
        return model.isCellEditable(row, column);
    }

    /** Fire an event signaling that the table has changed.
    
        @param e The TableModelEvent
    */
    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }
}
