package com.ununbium.Scripting.table;

import com.ununbium.Scripting.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.apache.log4j.Logger;

public class PluginTable extends AbstractTableModel {

    private static final Logger logger = Logger.getLogger(PluginTable.class);

    protected String filter = "All";

    protected String bold(String text) {
        return "<html><b>" + text + "</b></html>";
    }

    protected String[] columnNames = { "Enabled", "Name", "Version", "Jar File" };

    protected Object[][] data;

    protected JTable parent;

    private Vector plugins = new Vector();

    public void setFilter(String s) {
        this.filter = s;
        update();
    }

    protected boolean testFilter(String s) {
        if (filter.equals("All") || s.equals(filter)) return true;
        return false;
    }

    public void update() {
        update(plugins);
    }

    public void update(Vector p) {
        try {
            this.plugins = p;
            if (plugins == null || plugins.isEmpty()) {
                data = new Object[0][columnNames.length];
            } else {
                data = new Object[plugins.size()][columnNames.length];
                for (int m = 0; m < plugins.size(); m++) {
                    ScriptPlugin plugin = (ScriptPlugin) plugins.elementAt(m);
                    data[m][0] = (Boolean) new Boolean(plugin.isActive());
                    data[m][1] = (String) new String(plugin.getName());
                    data[m][2] = (String) new String(plugin.getVersion());
                    data[m][3] = (String) new String(plugin.getJarFile());
                }
            }
            fireTableDataChanged();
        } catch (Exception e) {
            logger.error("Error in update: " + e, e);
        }
    }

    public void setParent(JTable table) {
        parent = table;
    }

    public JTable getParent() {
        return parent;
    }

    public int getColumnCount() {
        logger.trace("getColumnCount() returns: " + (columnNames == null ? 0 : columnNames.length));
        return (columnNames == null ? 0 : columnNames.length);
    }

    public int getRowCount() {
        logger.trace("getRowCount() returns: " + (data == null ? 0 : data.length));
        return (data == null ? 0 : data.length);
    }

    @Override
    public String getColumnName(int col) {
        logger.trace("getColumnName( " + col + ") returns: " + columnNames[col]);
        return bold(columnNames[col]);
    }

    public Object getRawValueAt(int row, int col) {
        logger.trace("getRawValueAt( " + row + ", " + col + ") returns: " + data[row][col]);
        return data[row][col];
    }

    public Object getValueAt(int row, int col) {
        logger.trace("getValueAt( " + row + ", " + col + ") returns: " + data[row][col]);
        return data[row][col];
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 0) return true;
        return false;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
        if (col == 0) {
            ScriptPlugin sp = (ScriptPlugin) plugins.elementAt(row);
            sp.setActive((Boolean) value);
        }
    }

    public int getPreferredWidthForColumn(int col) {
        int hw = columnHeaderWidth(col), cw = widestCellInColumn(col);
        logger.trace("Preffered width of column: " + columnNames[col] + " is: " + (hw > cw ? hw : cw));
        return hw > cw ? hw : cw;
    }

    public int columnHeaderWidth(int col) {
        return columnNames[col].length();
    }

    public int widestCellInColumn(int col) {
        int width = 0, maxw = 0;
        for (int r = 0; r < getRowCount(); ++r) {
            width = getRawValueAt(r, col).toString().length() + 2;
            maxw = width > maxw ? width : maxw;
        }
        return maxw * 4;
    }
}
