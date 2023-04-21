package com.velocityme.client.gui.node.taskstatemachine;

import com.velocityme.client.gui.ListAssignPanel;
import com.velocityme.valueobjects.RoleValue;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author  Robert
 */
public class RoleEditor extends JLabel implements TableCellEditor {

    protected OkCancel m_helper;

    protected transient Vector m_listeners;

    protected transient Collection m_originalValues;

    protected transient boolean m_editing;

    protected ListAssignPanel m_listAssignPanel;

    /** Creates a new instance of RoleEditor */
    public RoleEditor(ListAssignPanel p_listAssignPanel) {
        m_listeners = new Vector();
        m_listAssignPanel = p_listAssignPanel;
        m_listAssignPanel.setEnabled(false);
        m_helper = new OkCancel();
    }

    private class OkCancel extends JWindow {

        private javax.swing.JButton jButtonCancel;

        private javax.swing.JButton jButtonOK;

        private javax.swing.JPanel jPanelButtons;

        public OkCancel() {
            jPanelButtons = new javax.swing.JPanel();
            jButtonOK = new javax.swing.JButton();
            jButtonCancel = new javax.swing.JButton();
            jPanelButtons.setLayout(new java.awt.GridLayout());
            jButtonOK.setText("OK");
            jButtonOK.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    stopCellEditing();
                }
            });
            jPanelButtons.add(jButtonOK);
            jButtonCancel.setText("Cancel");
            jButtonCancel.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    cancelCellEditing();
                }
            });
            jPanelButtons.add(jButtonCancel);
            setSize(jPanelButtons.getPreferredSize());
            setContentPane(jPanelButtons);
        }
    }

    public void addCellEditorListener(javax.swing.event.CellEditorListener l) {
        m_listeners.addElement(l);
    }

    public void cancelCellEditing() {
        fireEditingCanceled();
        m_editing = false;
        m_helper.setVisible(false);
        m_listAssignPanel.setEnabled(false);
    }

    public Object getCellEditorValue() {
        return m_listAssignPanel.getAssignedValues();
    }

    public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
        if (value == null) {
            return this;
        }
        if (value instanceof Collection) {
            m_originalValues = (Collection) value;
            m_listAssignPanel.setAssignedValues(m_originalValues);
            setText(m_originalValues.toString());
        }
        table.setRowSelectionInterval(row, row);
        table.setColumnSelectionInterval(column, column);
        m_editing = true;
        Point p = table.getLocationOnScreen();
        Rectangle r = table.getCellRect(row, column, true);
        m_helper.setLocation(r.x + p.x + getWidth() - m_helper.getWidth(), r.y + p.y + getHeight());
        m_helper.setVisible(true);
        m_listAssignPanel.setEnabled(true);
        return this;
    }

    public boolean isCellEditable(java.util.EventObject anEvent) {
        return true;
    }

    public void removeCellEditorListener(javax.swing.event.CellEditorListener l) {
        m_listeners.remove(l);
    }

    public boolean shouldSelectCell(java.util.EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        setText(m_listAssignPanel.getAssignedValues().toString());
        fireEditingStopped();
        m_editing = false;
        m_helper.setVisible(false);
        m_listAssignPanel.setEnabled(false);
        return true;
    }

    protected void fireEditingCanceled() {
        setText(m_originalValues.toString());
        m_listAssignPanel.setAssignedValues(m_originalValues);
        ChangeEvent ce = new ChangeEvent(this);
        for (int i = m_listeners.size() - 1; i >= 0; i--) {
            ((CellEditorListener) m_listeners.elementAt(i)).editingCanceled(ce);
        }
    }

    protected void fireEditingStopped() {
        ChangeEvent ce = new ChangeEvent(this);
        for (int i = m_listeners.size() - 1; i >= 0; i--) {
            ((CellEditorListener) m_listeners.elementAt(i)).editingStopped(ce);
        }
    }
}
