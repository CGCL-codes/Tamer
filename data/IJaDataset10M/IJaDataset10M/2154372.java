package org.formaria.swing.treetable;

import java.util.EventObject;
import javax.swing.CellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

/**
 * A tree node wrapper for an DataModel node
 *
 * <p> Copyright (c) Formaria Ltd., 2008, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * <p> $Revision: 1.5 $</p>
 */
public class AbstractCellEditor implements CellEditor {

    protected EventListenerList listenerList = new EventListenerList();

    public Object getCellEditorValue() {
        return null;
    }

    public boolean isCellEditable(EventObject e) {
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }

    public boolean stopCellEditing() {
        return true;
    }

    public void cancelCellEditing() {
    }

    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    protected void fireEditingStopped() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) ((CellEditorListener) listeners[i + 1]).editingStopped(new ChangeEvent(this));
        }
    }

    protected void fireEditingCanceled() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) ((CellEditorListener) listeners[i + 1]).editingCanceled(new ChangeEvent(this));
        }
    }
}
