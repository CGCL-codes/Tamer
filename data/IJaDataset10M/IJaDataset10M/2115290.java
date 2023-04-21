package simonton.gui.table;

import java.util.*;
import javax.swing.table.*;

public class STableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private final List<String> headers = new ArrayList<String>();

    private final List<STableRow> rows = new ArrayList<STableRow>();

    public void addColumn(String header) {
        headers.add(header);
        fireTableStructureChanged();
    }

    public void addRow(STableRow row) {
        insertRow(getRowCount(), row);
    }

    public void insertRow(int index, STableRow row) {
        row.setModel(this);
        rows.add(index, row);
        updateIndicies(index);
        fireTableRowsInserted(index, index);
    }

    public void removeRow(int index) {
        STableRow row = getRow(index);
        rows.remove(index);
        row.setModel(null);
        row.setIndex(-1);
        row.dispose();
        updateIndicies(index);
        fireTableRowsDeleted(index, index);
    }

    public List<STableRow> getRows() {
        return Collections.unmodifiableList(rows);
    }

    public STableRow getRow(int index) {
        return rows.get(index);
    }

    public int getColumnCount() {
        return headers.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return STableCell.class;
    }

    @Override
    public String getColumnName(int column) {
        return headers.get(column);
    }

    public int getRowCount() {
        return rows.size();
    }

    public STableCell getValueAt(int rowIndex, int columnIndex) {
        return getRow(rowIndex).getCell(columnIndex);
    }

    public void dispose() {
        for (STableRow row : rows) {
            row.dispose();
        }
    }

    void cellChanged(int row, int column) {
        fireTableCellUpdated(row, column);
    }

    private void updateIndicies(int minAffected) {
        for (int i = getRowCount(); --i >= minAffected; ) {
            getRow(i).setIndex(i);
        }
    }
}
