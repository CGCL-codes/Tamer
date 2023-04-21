package org.dlib.gui.treetable;

import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.dlib.gui.treeview.TreeViewNode;

class InternalTableModel implements TableModel {

    private TreeTable treeTable;

    private ArrayList<TableModelListener> alListeners = new ArrayList<TableModelListener>();

    public InternalTableModel(TreeTable tt) {
        treeTable = tt;
    }

    public void addTableModelListener(TableModelListener l) {
        alListeners.add(l);
    }

    public void removeTableModelListener(TableModelListener l) {
        alListeners.remove(l);
    }

    public Class getColumnClass(int index) {
        TreeTableModel model = treeTable.getTreeTableModel();
        if (index != 0) return model.getColumnClass(index - 1);
        return Object.class;
    }

    public boolean isCellEditable(int row, int col) {
        TreeTableModel model = treeTable.getTreeTableModel();
        Object obj = treeTable.getTreeView().getPathForRow(row).getLastPathComponent();
        if (col == 0) return model.isEditable(obj); else return model.isColumnEditable(obj, col - 1);
    }

    public int getColumnCount() {
        TreeTableModel model = treeTable.getTreeTableModel();
        return model.getColumnCount() + 1;
    }

    public String getColumnName(int index) {
        TreeTableModel model = treeTable.getTreeTableModel();
        if (index == 0) return model.getObjectColumnName(); else return model.getColumnName(index - 1);
    }

    public int getRowCount() {
        return treeTable.getTreeView().getRowCount();
    }

    public Object getValueAt(int row, int col) {
        Object object = treeTable.getPathFromRow(row).getLastPathComponent();
        if (col == 0) return object; else {
            TreeTableModel model = treeTable.getTreeTableModel();
            return model.getValueAt(object, col - 1);
        }
    }

    /** This method is called only when data is modified with proper editor not
	  * when data is added/removed
	  */
    public void setValueAt(Object value, int row, int col) {
        if (col == 0) return;
        Object object = treeTable.getPathFromRow(row).getLastPathComponent();
        TreeTableModel model = treeTable.getTreeTableModel();
        model.setValueAt(object, value, col - 1);
        fireTableCellUpdated(row, col);
    }

    private void fireTableCellUpdated(int row, int column) {
        TableModelEvent e = new TableModelEvent(this, row, row, column);
        for (TableModelListener l : alListeners) l.tableChanged(e);
    }

    void fireTableRowInserted(int row) {
        TableModelEvent e = new TableModelEvent(this, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        for (TableModelListener l : alListeners) l.tableChanged(e);
    }

    void fireTableRowUpdated(int row) {
        TableModelEvent e = new TableModelEvent(this, row, row, TableModelEvent.ALL_COLUMNS);
        for (TableModelListener l : alListeners) l.tableChanged(e);
    }

    void fireTableRowDeleted(int row) {
        TableModelEvent e = new TableModelEvent(this, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
        for (TableModelListener l : alListeners) l.tableChanged(e);
    }
}
