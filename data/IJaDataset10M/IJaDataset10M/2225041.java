package gui;

import java.util.ArrayList;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;

/**
 * @author andersbe
 *
 */
public class ParTableModel extends AbstractTableModel implements TableModelListener, dataImporters.ParTable {

    private String[] columnNames;

    private ArrayList<ArrayList<Object>> rowData = new ArrayList<ArrayList<Object>>();

    private ArrayList<Object> newColumn = new ArrayList<Object>();

    private ArrayList<Object> row2 = new ArrayList<Object>();

    private boolean showAdvancedOptions;

    public int setCount;

    public ParTableModel(boolean advancedOptions) {
        super();
        this.showAdvancedOptions = advancedOptions;
        setCount = 0;
        addTableModelListener(this);
        int numColumns = 8, nextColumn = 0;
        if (showAdvancedOptions) numColumns = 9;
        columnNames = new String[numColumns];
        columnNames[nextColumn++] = "#";
        columnNames[nextColumn++] = "*.par";
        columnNames[nextColumn++] = "mass cal";
        columnNames[nextColumn++] = "size cal";
        columnNames[nextColumn++] = "Min. Height";
        columnNames[nextColumn++] = "Min. Area";
        columnNames[nextColumn++] = "Min. Rel. Area";
        if (showAdvancedOptions) {
            columnNames[nextColumn++] = "Max. Peak Error";
        }
        columnNames[nextColumn++] = "Autocal";
        newColumn.add(new Integer(++setCount));
        newColumn.add(new String(".par file"));
        newColumn.add(new String(".cal file"));
        newColumn.add(new String(".noz file"));
        newColumn.add(new Integer(0));
        newColumn.add(new Integer(0));
        newColumn.add(new Float(0.0));
        if (showAdvancedOptions) {
            newColumn.add(new Float(0.0));
        }
        newColumn.add(new Boolean(true));
        rowData.add(newColumn);
        row2.add(new Integer(++setCount));
        row2.add(new String(""));
        row2.add(new String(""));
        row2.add(new String(""));
        row2.add(new Integer(0));
        row2.add(new Integer(0));
        row2.add(new Float(0.0));
        if (showAdvancedOptions) {
            row2.add(new Float(0.0));
        }
        row2.add(new Boolean(true));
        rowData.add(row2);
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public int getRowCount() {
        return rowData.size();
    }

    public int getColumnCount() {
        return rowData.get(0).size();
    }

    public Object getValueAt(int row, int col) {
        return rowData.get(row).get(col);
    }

    public boolean isCellEditable(int row, int col) {
        if (col == 0) return false; else return true;
    }

    public void setValueAt(Object value, int row, int col) {
        rowData.get(row).set(col, value);
        fireTableCellUpdated(row, col);
    }

    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public void tableChanged(TableModelEvent e) {
        if ((e.getLastRow() == rowData.size() - 1) && (e.getType() == TableModelEvent.UPDATE) && e.getColumn() == 1) {
            if (!((String) rowData.get(e.getLastRow()).get(1)).equals("")) {
                ArrayList<Object> newRow = new ArrayList<Object>(8);
                int nextCol = 2;
                newRow.add(new Integer(++setCount));
                newRow.add(new String(""));
                newRow.add(((String) rowData.get(rowData.size() - 1).get(nextCol++)));
                newRow.add(((String) rowData.get(rowData.size() - 1).get(nextCol++)));
                newRow.add((Integer) rowData.get(rowData.size() - 1).get(nextCol++));
                newRow.add((Integer) rowData.get(rowData.size() - 1).get(nextCol++));
                newRow.add((Float) rowData.get(rowData.size() - 1).get(nextCol++));
                if (showAdvancedOptions) {
                    newRow.add((Float) rowData.get(rowData.size() - 1).get(nextCol++));
                }
                newRow.add((Boolean) rowData.get(rowData.size() - 1).get(nextCol++));
                rowData.add(newRow);
                fireTableRowsInserted(rowData.size() - 1, rowData.size() - 1);
            }
        }
        if ((e.getLastRow() == rowData.size() - 2) && (e.getType() == TableModelEvent.UPDATE)) {
            ArrayList<Object> lastRow = (ArrayList<Object>) rowData.get(rowData.size() - 1);
            int nextCol = 2;
            lastRow.set(nextCol, (String) rowData.get(rowData.size() - 2).get(nextCol++));
            lastRow.set(nextCol, (String) rowData.get(rowData.size() - 2).get(nextCol++));
            lastRow.set(nextCol, (Integer) rowData.get(rowData.size() - 2).get(nextCol++));
            lastRow.set(nextCol, (Integer) rowData.get(rowData.size() - 2).get(nextCol++));
            lastRow.set(nextCol, (Float) rowData.get(rowData.size() - 2).get(nextCol++));
            if (showAdvancedOptions) {
                lastRow.set(nextCol, (Float) rowData.get(rowData.size() - 2).get(nextCol++));
            }
            lastRow.set(nextCol, (Boolean) rowData.get(rowData.size() - 2).get(nextCol++));
            fireTableRowsUpdated(rowData.size() - 1, rowData.size() - 1);
        }
    }
}
