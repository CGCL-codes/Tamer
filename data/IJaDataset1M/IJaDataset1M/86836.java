package antfarm;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.gui.*;
import org.gjt.sp.jedit.io.*;
import org.gjt.sp.util.Log;

public class PropertiesTable extends JTable {

    static final String NAME_HISTORY_MODEL = PropertiesOptionPane.PROPERTY + PropertiesOptionPane.NAME;

    static final String VALUE_HISTORY_MODEL = PropertiesOptionPane.PROPERTY + PropertiesOptionPane.VALUE;

    public PropertiesTable(Properties properties) {
        super(new PropertiesTableModel(properties));
        setUpNameColumn(getColumnModel().getColumn(0));
        setUpValueColumn(getColumnModel().getColumn(1));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public PropertiesTable() {
        this(new Properties());
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        int counter = getRowHeight();
        String name = "";
        String value = "";
        for (int i = 0; i < counter; i++) {
            name = (String) getValueAt(i, 0);
            value = (String) getValueAt(i, 1);
            if (name == null || value == null) continue;
            properties.setProperty(name, value);
        }
        return properties;
    }

    private void setUpNameColumn(TableColumn column) {
        DefaultCellEditor cellEditor = new DefaultCellEditor(new HistoryTextField(NAME_HISTORY_MODEL, true, false));
        cellEditor.setClickCountToStart(1);
        column.setCellEditor(cellEditor);
        column.setPreferredWidth(75);
    }

    private void setUpValueColumn(TableColumn column) {
        DefaultCellEditor cellEditor = new DefaultCellEditor(new HistoryTextField(VALUE_HISTORY_MODEL, true, false));
        cellEditor.setClickCountToStart(1);
        column.setCellEditor(cellEditor);
        column.setPreferredWidth(75);
    }
}

class PropertiesTableModel extends AbstractTableModel {

    private String[] columnNames;

    private Vector names;

    private Vector values;

    public PropertiesTableModel(Properties properties) {
        columnNames = new String[2];
        columnNames[0] = "Name";
        columnNames[1] = "Value";
        names = new Vector(10);
        values = new Vector(10);
        String name = "";
        Enumeration keys = properties.propertyNames();
        while (keys.hasMoreElements()) {
            name = (String) keys.nextElement();
            names.addElement(name);
            values.addElement(properties.getProperty(name));
        }
        names.addElement("");
        values.addElement("");
    }

    public void setValueAt(Object value, int row, int col) {
        if (((String) value).length() == 0) {
            names.removeElementAt(row);
            values.removeElementAt(row);
            fireTableRowsDeleted(row, row);
            if (names.size() < 1) {
                insertBlankRow(row);
            }
        } else {
            if (col == 0) {
                names.setElementAt(value, row);
                HistoryModel.getModel(PropertiesTable.NAME_HISTORY_MODEL).addItem((String) value);
            } else {
                values.setElementAt(value, row);
                HistoryModel.getModel(PropertiesTable.VALUE_HISTORY_MODEL).addItem((String) value);
            }
            if (row == names.size() - 1) {
                insertBlankRow(row);
            }
        }
        fireTableCellUpdated(row, col);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return names.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        if (row >= names.size() || row >= names.size()) return null;
        if (col == 0) {
            return names.elementAt(row);
        }
        return values.elementAt(row);
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    private void insertBlankRow(int row) {
        names.addElement("");
        values.addElement("");
        fireTableRowsInserted(row + 1, row + 1);
    }
}
