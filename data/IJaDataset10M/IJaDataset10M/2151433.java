package west.view;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author armnant
 */
public class ReadOnlyTableModel extends AbstractTableModel {

    public ReadOnlyTableModel(Object data[][], String columnNames[]) {
        this.data = data;
        this.columnNames = columnNames;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public int getRowCount() {
        return data.length;
    }

    public Object getValueAt(int row, int column) {
        return data[row][column];
    }

    @Override
    public Class getColumnClass(int column) {
        return (getValueAt(0, column).getClass());
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    private Object data[][] = null;

    private String columnNames[] = null;
}
