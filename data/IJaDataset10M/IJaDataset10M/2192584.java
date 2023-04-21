package net.sf.fysix.leveleditor.tool.table;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

/**
 * @author Markus
 */
public class TableModel extends AbstractTableModel {

    private Vector columns = new Vector();

    private Vector colClass = new Vector();

    private Vector data = new Vector();

    public TableModel() {
    }

    public TableModel(String[] cols) {
        for (int i = 0; cols != null && i < cols.length; i++) {
            columns.addElement(new String(cols[i]));
        }
    }

    public TableModel(String[] cols, Class[] classes) {
        for (int i = 0; cols != null && i < cols.length; i++) {
            columns.addElement(new String(cols[i]));
            colClass.addElement(classes[i]);
        }
    }

    public String getColumnName(int column) {
        return (String) columns.elementAt(column);
    }

    public Class getColumnClass(int column) {
        if (colClass.size() > 0) {
            return (Class) colClass.elementAt(column);
        } else {
            return String.class;
        }
    }

    public int getColumnCount() {
        return columns.size();
    }

    public int getRowCount() {
        return data.size();
    }

    public Object getValueAt(int row, int col) {
        TableEntryInterface te = (TableEntryInterface) data.elementAt(row);
        return te.getValueAt(col);
    }

    public Object getUserObjectAt(int row) {
        TableEntryInterface te = (TableEntryInterface) data.elementAt(row);
        return te.getUserObject();
    }

    public void clearEntries() {
        int lastRow = data.size();
        data.removeAllElements();
        fireTableRowsDeleted(0, (lastRow == 0) ? 0 : lastRow - 1);
    }

    public void addEntry(TableEntryInterface te) {
        int lastRow = data.size();
        data.addElement(te);
        fireTableRowsInserted(lastRow, lastRow);
    }

    public void updateEntry(TableEntryInterface te) {
        int index = findTableEntry(te);
        if (index != -1) {
            data.setElementAt(te, index);
            fireTableRowsUpdated(index, index);
        }
    }

    public void removeEntry(TableEntryInterface te) {
        int index = findTableEntry(te);
        if (index != -1) {
            data.removeElementAt(index);
            fireTableRowsDeleted(index, index);
        }
    }

    public int findTableEntry(TableEntryInterface te) {
        int res = -1;
        for (int i = 0; i < data.size(); i++) {
            TableEntryInterface entry = (TableEntryInterface) data.elementAt(i);
            if (entry.isSameAs(te)) {
                res = i;
                break;
            }
        }
        return res;
    }
}
