package ui.workbook;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

    private TableData tableData;

    private static int serial = 1;

    public TableModel() {
        tableData = new TableData("sheet" + serial++, 40, 40);
    }

    public TableData getTableData() {
        return tableData;
    }

    public int getColumnCount() {
        return tableData.getColumnCount();
    }

    public int getRowCount() {
        return tableData.getRowCount();
    }

    public Object getValueAt(int row, int col) {
        if (tableData.getCellData(row, col) == null) return null; else return tableData.getCellData(row, col).toString();
    }

    public Class getColumnClass(int col) {
        return "".getClass();
    }

    public void setValueAt(Object value, int row, int col) {
        tableData.setCellData(value.toString(), row, col);
        fireTableCellUpdated(row, col);
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }
}
