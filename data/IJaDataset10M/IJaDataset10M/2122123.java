package org.fudaa.ebli.tableau;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 * @author deniger
 * @version $Id: EbliCellButtonEditor.java,v 1.2 2004-05-05 12:40:05 deniger Exp $
 */
public abstract class EbliCellButtonEditor extends JButton implements TableCellEditor, ActionListener {

    private transient ChangeEvent changeEvent = null;

    private EbliCellDecorator decorator_;

    protected Object value_;

    private boolean doubleClick_;

    public EbliCellButtonEditor(EbliCellDecorator _deco) {
        setOpaque(false);
        setEnabled(true);
        addActionListener(this);
        decorator_ = _deco;
        setBorder(UIManager.getBorder("Label.border"));
        setFont(UIManager.getFont("Table.font"));
        setHorizontalAlignment(0);
    }

    /**
   *
   */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        value_ = value;
        setValue(value);
        if (decorator_ != null) decorator_.decore(this, table, value, row, column);
        return this;
    }

    public void setValue(Object _o) {
        setText(_o.toString());
    }

    /**
   *
   */
    public Object getCellEditorValue() {
        return value_;
    }

    /**
   *
   */
    public boolean isCellEditable(EventObject anEvent) {
        if (!doubleClick_) return true;
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= 2;
        }
        return true;
    }

    /**
   *
   */
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    /**
  *
  */
    public void cancelCellEditing() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                if (changeEvent == null) changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
            }
        }
    }

    /**
   *
   */
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    /**
   *
   */
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    public boolean stopCellEditing() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                if (changeEvent == null) changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
            }
        }
        return false;
    }

    protected abstract void doAction();

    public void actionPerformed(ActionEvent _ae) {
        doAction();
        stopCellEditing();
    }

    /**
   *
   */
    public EbliCellDecorator getDecorator() {
        return decorator_;
    }

    /**
   *
   */
    public void setDecorator(EbliCellDecorator _decorator) {
        decorator_ = _decorator;
    }

    /**
   *
   */
    public boolean isDoubleClick() {
        return doubleClick_;
    }

    /**
   *
   */
    public void setDoubleClick(boolean _b) {
        doubleClick_ = _b;
    }
}
