package dakside.flexui.tables;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

/**
 * Object array table model<br/>
 * By default, it's a readonly table
 * @author takaji
 */
public class ObjectTableModel<T> extends AbstractTableModel {

    protected ArrayList<ColumnDefinition<T>> columns = new ArrayList<ColumnDefinition<T>>();

    protected ArrayList<T> entries = new ArrayList<T>();

    protected ObjectTableModel() {
    }

    public void add(T entry) {
        if (entry != null) {
            entries.add(entry);
        }
    }

    public void insert(int index, T entry) {
        entries.add(index, entry);
    }

    /**
     * Remove a row
     * @param rowIndex
     */
    public void remove(int rowIndex) {
        entries.remove(rowIndex);
        fireTableDataChanged();
    }

    /**
     * Remove all objects
     */
    public void removeAll() {
        entries.clear();
        fireTableDataChanged();
    }

    public int getRowCount() {
        return entries.size();
    }

    @Override
    public String getColumnName(int index) {
        return columns.get(index).getName();
    }

    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (isCellEditable(rowIndex, columnIndex)) {
            columns.get(rowIndex).setValue(entries.get(rowIndex), aValue);
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount() || columnIndex < 0 || columnIndex >= getColumnCount()) {
            return null;
        }
        T entry = entries.get(rowIndex);
        return columns.get(columnIndex).getValue(entry);
    }

    public T getRowObject(int index) {
        return entries.get(index);
    }

    public ColumnDefinition<T>[] getColumns() {
        return (ColumnDefinition<T>[]) columns.toArray(new Object[columns.size()]);
    }

    public void format(TableColumnModel model) {
        int total = this.getColumnCount();
        for (int i = 0; i < total; i++) {
            model.getColumn(i).setCellRenderer(columns.get(i).getRenderer());
        }
    }

    public T[] getObjectArray(T[] a) {
        return (T[]) this.entries.toArray(a);
    }

    /**
     * Remove all items in current model and add all items from a new set
     * @param entries the new set
     */
    public void reload(T[] entries) {
        removeAll();
        Collections.addAll(this.entries, entries);
    }

    /**
     * Add all items to model
     * @param entries
     */
    public void addAll(T[] entries) {
        Collections.addAll(this.entries, entries);
    }
}
