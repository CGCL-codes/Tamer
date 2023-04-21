package org.wct.table;

import org.wct.Table;

/**
 *
 * @author  juliano
 */
public class TableEvent extends java.util.EventObject {

    /** Holds value of property selectedRow. */
    private int selectedRow;

    /** Holds value of property selectedColumn. */
    private int selectedColumn;

    /** Creates a new instance of TableEvent */
    public TableEvent(Table source) {
        super(source);
    }

    /** Getter for property selectedRow.
     * @return Value of property selectedRow.
     */
    public int getSelectedRow() {
        return this.selectedRow;
    }

    /** Setter for property selectedRow.
     * @param selectedRow New value of property selectedRow.
     */
    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    /** Getter for property selectedColumn.
     * @return Value of property selectedColumn.
     */
    public int getSelectedColumn() {
        return this.selectedColumn;
    }

    /** Setter for property selectedColumn.
     * @param selectedColumn New value of property selectedColumn.
     */
    public void setSelectedColumn(int selectedColumn) {
        this.selectedColumn = selectedColumn;
    }
}
