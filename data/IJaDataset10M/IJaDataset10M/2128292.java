package com.digitprop.tonicdemo;

import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;

class TonicDemoTableModel implements TableModel {

    /**	Registers the specified TableModelListener with this model */
    public void addTableModelListener(TableModelListener l) {
    }

    /**	Returns the class of the specified column. Returns String for
	 * 	all columns except for the first, for which the class is
	 * 	Boolean.
	 */
    public Class getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return Boolean.class;
            default:
                return String.class;
        }
    }

    /**	Returns the number of columns */
    public int getColumnCount() {
        return 4;
    }

    /**	Returns the name of the specified column */
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return "C";
            case 1:
                return "Description";
            case 2:
                return "Status";
            case 3:
            default:
                return "Location";
        }
    }

    /**	Returns the number of rows */
    public int getRowCount() {
        return 20;
    }

    /**	Returns the cell value at the specified row and column */
    public Object getValueAt(int rowIndex, int columnIndex) {
        String desc[] = { "Replace nuclear fusion cell in flux generator", "Repair core cooling circuit", "Return lightning staff to Moonglow", "Negotiate with brotherhood of steel", "Realign satellite dish", "Prepare for download", "Never buy again from global supermarket chain" };
        String status[] = { "Open", "Closed", "Rejected", "Filed" };
        String loc[] = { "\\data\\storage\\all", "\\data\\enhanced", "\\data", "\\aux\\project", "\\aux\\project\\optional", "\\rec\\music" };
        Random r = new Random(rowIndex);
        for (int i = 0; i < rowIndex; i++) r.nextInt();
        switch(columnIndex) {
            case 0:
                return new Boolean(r.nextBoolean());
            case 1:
                return desc[rowIndex % desc.length];
            case 2:
                return status[r.nextInt(status.length)];
            case 3:
            default:
                return loc[r.nextInt(loc.length)];
        }
    }

    /**	Returns true if the cell at the specified position is
	 * 	editable. With this model, no cells are editable.
	 */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**	Removes the specified TableModelListener from this model. */
    public void removeTableModelListener(TableModelListener l) {
    }

    /**	Sets the value of the cell at the specified position */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }
}
