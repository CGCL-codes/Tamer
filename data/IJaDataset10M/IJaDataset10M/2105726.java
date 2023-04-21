package com.velocityme.client.gui.node.role;

import com.velocityme.client.gui.VectorTableModel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author  Robert Crida Work
 */
public class PermissionTableModel extends VectorTableModel {

    private boolean m_editable = false;

    /** Creates a new instance of ContactDetailTypeListModel */
    public PermissionTableModel() {
        super(new String[] { "Permission", "Enabled" });
    }

    public Set getAssignedValues() {
        Set results = new HashSet();
        for (int row = 0; row < getRowCount(); row++) {
            if (getValueAt(row, 1).equals(Boolean.TRUE)) results.add(getValueAt(row, 0));
        }
        return results;
    }

    public void setValues(Map p_values) {
        m_dataRows.clear();
        Set permissionSet = p_values.keySet();
        Iterator i = permissionSet.iterator();
        while (i.hasNext()) {
            Object permission = i.next();
            m_dataRows.addElement(new Object[] { permission, p_values.get(permission) });
        }
        fireTableRowsInserted(m_dataRows.size() - 1, m_dataRows.size());
    }

    public void setEditable(boolean editable) {
        m_editable = editable;
    }

    public boolean isCellEditable(int row, int col) {
        return m_editable && col == 1;
    }

    public void setValueAt(Object value, int row, int col) {
        ((Object[]) m_dataRows.get(row))[col] = value;
        fireTableDataChanged();
    }
}
