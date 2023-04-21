package org.gvsig.gui.beans.swing.celleditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import org.gvsig.gui.beans.swing.cellrenderers.BooleanTableCellRenderer;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class BooleanTableCellEditor implements TableCellEditor {

    private BooleanTableCellRenderer renderer;

    private ArrayList listeners = new ArrayList();

    public BooleanTableCellEditor(final JTable ownerTable) {
        renderer = new BooleanTableCellRenderer(true);
    }

    public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, int row, int column) {
        if (value == null) return null;
        JComponent aux = (JComponent) renderer.getTableCellRendererComponent(table, value, isSelected, false, row, column);
        ((JCheckBox) aux.getComponent(0)).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                table.editingStopped(new ChangeEvent(table));
            }
        });
        return aux;
    }

    public void cancelCellEditing() {
        for (int i = 0; i < listeners.size(); i++) {
            CellEditorListener l = (CellEditorListener) listeners.get(i);
            ChangeEvent evt = new ChangeEvent(this);
            l.editingCanceled(evt);
        }
    }

    public boolean stopCellEditing() {
        for (int i = 0; i < listeners.size(); i++) {
            CellEditorListener l = (CellEditorListener) listeners.get(i);
            ChangeEvent evt = new ChangeEvent(this);
            l.editingStopped(evt);
        }
        return true;
    }

    public Object getCellEditorValue() {
        if (renderer != null && renderer.getCheck() != null) return new Boolean(renderer.getCheck().isSelected());
        return null;
    }

    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public void addCellEditorListener(CellEditorListener l) {
        listeners.add(l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        listeners.remove(l);
    }
}
