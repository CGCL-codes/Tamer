package userInterface;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ClickElementTable extends AbstractTableModel {

    protected Vector<String> columnNames;

    protected Vector<Object[]> items;

    @SuppressWarnings("rawtypes")
    protected Comparator comparator = new Comparator<String[]>() {

        @Override
        public int compare(String[] arg0, String[] arg1) {
            return arg0[0].compareTo(arg1[0]);
        }
    };

    public ClickElementTable(Vector<Object[]> items, Vector<String> columnNames) {
        this.columnNames = columnNames;
        this.items = items;
    }

    public void addRow(String tag, String attrName, String attrVal, String text, String xPath, Boolean doClick) {
        items.add(new Object[] { tag, attrName, attrVal, text, xPath, new Boolean(doClick) });
        this.fireTableDataChanged();
    }

    public void removeRow(int index) {
        items.removeElementAt(index);
        this.fireTableDataChanged();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames.get(col);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(String value, int row, int col) {
        (items.get(row))[col] = value;
        fireTableCellUpdated(row, col);
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public Object getValueAt(int vectorIndex, int arrayIndex) {
        return (items.get(vectorIndex))[arrayIndex];
    }

    public Vector<Object[]> getItems() {
        return items;
    }

    public void setItems(Vector<Object[]> items) {
        this.items = items;
        this.fireTableDataChanged();
    }

    @SuppressWarnings("unchecked")
    public void sortItems() {
        Collections.sort(items, comparator);
    }
}
