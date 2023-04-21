package compoundDB.gui;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class PreviewTableModel extends AbstractTableModel {

    private Object[][] m_data;

    private String[] m_columnNames;

    private boolean m_editable;

    public PreviewTableModel(String[] columnNames, Object[][] data, boolean editable) {
        m_data = data.clone();
        m_columnNames = columnNames.clone();
        m_editable = editable;
    }

    @Override
    public int getColumnCount() {
        return m_columnNames.length;
    }

    @Override
    public int getRowCount() {
        return m_data.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return m_data[rowIndex][columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 1 && m_editable && row != 0 && row != 1;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        m_data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    @Override
    public String getColumnName(int col) {
        return m_columnNames[col];
    }
}
