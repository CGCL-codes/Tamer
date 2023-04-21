package net.wsnware.gui.models;

import javax.swing.table.AbstractTableModel;
import java.util.Dictionary;
import java.util.Enumeration;

/**
 * TableModel for Dictionary dataset.
 *
 * @author  Alessandro Polo <contact@alessandropolo.name>
 * @version 1.0.0
 * @date    2011-05-17
 */
public class DictionaryTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    protected Dictionary m_hash = null;

    protected boolean showType = true;

    public Dictionary getDictionary() {
        return m_hash;
    }

    public void setDictionary(Dictionary m_hash) {
        this.m_hash = m_hash;
        fireTableRowsUpdated(0, m_hash.size());
    }

    @Override
    public int getColumnCount() {
        return 2 + (showType ? 1 : 0);
    }

    @Override
    public int getRowCount() {
        if (m_hash == null) return 0;
        return m_hash.size();
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) return "Key";
        if (column == 2 || !showType) return "Value";
        return "Type";
    }

    @Override
    public Class getColumnClass(int column) {
        if (column == 0) return String.class;
        if (column == 2 || !showType) return String.class;
        return Class.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) return getKey(rowIndex);
        if (m_hash == null) return null;
        Object val = m_hash.get(getKey(rowIndex));
        if (columnIndex == 1 && showType) {
            if (val == null) return "null";
            return val.getClass().getSimpleName();
        }
        return val;
    }

    private Object getKey(int a_index) {
        if (m_hash == null) return null;
        Object retval = null;
        Enumeration<String> e = m_hash.keys();
        for (int i = 0; i < a_index + 1; i++) {
            retval = e.nextElement();
        }
        return retval;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return (col == (showType ? 2 : 1));
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (col == 0) return;
        if (col == 1 && showType) return;
        if (m_hash == null) return;
        Object key = getKey(row);
        Object oldVal = m_hash.get(key);
        m_hash.put(key, value);
    }
}
