package org.mozilla.javascript.tools.debugger.downloaded;

import java.awt.Component;
import java.awt.event.*;
import java.awt.AWTEvent;
import javax.swing.*;
import javax.swing.event.*;
import java.util.EventObject;
import java.io.Serializable;

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
            if (listeners[i] == CellEditorListener.class) {
                ((CellEditorListener) listeners[i + 1]).editingStopped(new ChangeEvent(this));
            }
        }
    }

    protected void fireEditingCanceled() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                ((CellEditorListener) listeners[i + 1]).editingCanceled(new ChangeEvent(this));
            }
        }
    }
}
