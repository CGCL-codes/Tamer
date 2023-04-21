package de.ipkgatersleben.agbi.uploader.gui.panels.selectedColumns;

import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.collections.list.SetUniqueList;
import de.ipkgatersleben.agbi.uploader.model.database.Column;

class ColumnTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private List<Column> columns = SetUniqueList.decorate(new Vector<Column>());

    private final String[] headers;

    public ColumnTableModel() {
        headers = new String[] { "Table", "Column", "Source" };
    }

    public int getColumnCount() {
        return headers.length;
    }

    public int getRowCount() {
        return columns.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Column column = columns.get(rowIndex);
        switch(columnIndex) {
            case 0:
                if (column.getTable().getSchema() != null) return column.getTable().getSchema() + "." + column.getTable().getName(); else return column.getTable().getName();
            case 1:
                return column.getName();
            case 2:
                return column.getUplDataSource().getShortdesc();
            default:
                return "Error";
        }
    }

    public void setColumns(List<Column> columns) {
        this.columns.clear();
        this.columns.addAll(columns);
        fireTableDataChanged();
    }

    public void addColumns(List<Column> columns) {
        this.columns.addAll(columns);
        fireTableDataChanged();
    }

    public List<Column> getAllColumns() {
        return columns;
    }

    @Override
    public String getColumnName(int column) {
        return headers[column];
    }

    public void removeColumn(Column column) {
        columns.remove(column);
        fireTableDataChanged();
    }

    public void clear() {
        columns.clear();
        fireTableDataChanged();
    }

    public Column getValueAt(int row) {
        return columns.get(row);
    }

    public List<Column> getColumns() {
        return columns;
    }
}
