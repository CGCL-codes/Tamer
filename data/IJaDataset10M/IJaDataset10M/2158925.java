package org.openscience.cdk.applications.swing;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.openscience.cdk.ChemObject;

/**
 * @cdk.module applications
 * @cdk.require swing
 */
public class ChemObjectPropertyEditorTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -9026379867364393754L;

    private String[] columnNames;

    private Vector names = new Vector();

    private Vector values = new Vector();

    public ChemObjectPropertyEditorTableModel() {
        columnNames = new String[2];
        columnNames[0] = "Name";
        columnNames[1] = "Value";
        insertBlankRow(0);
    }

    public void setValueAt(Object value, int row, int column) {
        if (((String) value).length() == 0) {
            names.removeElementAt(row);
            values.removeElementAt(row);
            fireTableRowsDeleted(row, row);
            if (values.size() < 1) {
                insertBlankRow(row);
            }
        } else if (column == 0) {
            names.setElementAt(value, row);
        } else if (column == 1) {
            values.setElementAt(value, row);
        }
        if (row == values.size() - 1) {
            insertBlankRow(row);
        }
        fireTableCellUpdated(row, column);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return values.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class getColumnClass(int col) {
        return getColumnName(col).getClass();
    }

    public Object getValueAt(int row, int col) {
        if (row >= values.size() || col >= columnNames.length) {
            return null;
        }
        if (col == 0) {
            return names.elementAt(row);
        } else if (col == 1) {
            return values.elementAt(row);
        } else {
            return null;
        }
    }

    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public void setChemObject(ChemObject object) {
        cleanTable();
        Map properties = object.getProperties();
        Iterator iter = properties.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            if (key instanceof String) {
                String keyName = (String) key;
                names.addElement(keyName);
                String value = (String) properties.get(keyName);
                values.addElement(value);
            }
        }
    }

    private void cleanTable() {
        names.clear();
        values.clear();
        fireTableDataChanged();
        insertBlankRow(0);
    }

    private void insertBlankRow(int row) {
        names.addElement("");
        values.addElement("");
        fireTableRowsInserted(row + 1, row + 1);
    }
}
