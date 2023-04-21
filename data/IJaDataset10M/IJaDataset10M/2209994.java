package gui;

import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
	 * @author turetske
	 *
	 * ClusterTableModel is the table used by ClusterQueryDialog. 	 *
	 */
public class ClusterTableModel extends AbstractTableModel implements TableModelListener {

    private String[] columnNames;

    private ArrayList<ArrayList<Object>> rowData = new ArrayList<ArrayList<Object>>();

    private ArrayList<Object> newColumn = new ArrayList<Object>();

    private ArrayList<Object> row2 = new ArrayList<Object>();

    public int setCount;

    public ClusterTableModel() {
        super();
        setCount = 0;
        addTableModelListener(this);
        columnNames = new String[2];
        columnNames[0] = "#";
        columnNames[1] = "center";
        newColumn.add(new Integer(++setCount));
        newColumn.add(new String(".txt file"));
        rowData.add(newColumn);
        row2.add(new Integer(++setCount));
        row2.add(new String(""));
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
                ArrayList<Object> newRow = new ArrayList<Object>(2);
                newRow.add(new Integer(++setCount));
                newRow.add("");
                rowData.add(newRow);
                fireTableRowsInserted(rowData.size() - 1, rowData.size() - 1);
            }
        }
        if ((e.getLastRow() == rowData.size() - 2) && (e.getType() == TableModelEvent.UPDATE)) {
            ArrayList<Object> lastRow = (ArrayList<Object>) rowData.get(rowData.size() - 1);
            fireTableRowsUpdated(rowData.size() - 1, rowData.size() - 1);
        }
    }
}
