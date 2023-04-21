package RepeateTables;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author fzelosbi
 */
public class CheckBoxCellEditor extends JCheckBox implements TableCellEditor {

    protected ChangeEvent changeEvent = new ChangeEvent(this);

    protected EventListenerList listenerList = new EventListenerList();

    boolean selected = false;

    boolean cellEnabled = true;

    /**
     * Constructs a new <code>CheckBoxCellEditor</code> object.
     */
    public CheckBoxCellEditor() {
        super();
        this.setSize(10, 10);
        addItemListener(new ItemListener() {

            /**
             * Invoked when item event occurs.
             *
             * @param event <code> ItemEvent</code>
             */
            public void itemStateChanged(ItemEvent event) {
                fireEditingStopped();
            }
        });
    }

    /**
     * Registers the <code>CellEditorListener</code> to listeners list
     *
     * @param l a <code>CellEditorListener</code>
     */
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    /**
     * To cancel the Cell Editing invoked all the actions with this cell will be cancelled
     */
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    /**
     * Returns the cell editor value as <code>Object</code> this will be called when stops the
     * cell editing
     *
     * @return Object
     */
    public Object getCellEditorValue() {
        return new Boolean(this.isSelected());
    }

    /**
     * Returns the Editor Component to be painted on the table for the given row and column with
     * given value.
     *
     * in the method we need to set the value and state of component then return component itself so
     * that it can be painted as it is on the table.
     *
     * @param table <code>JTable</code> is the component where this editor need to be painted.
     * @param value <code>Object</code> is the value for the EditorComponent.
     * @param isSelected boolean
     * @param row int
     * @param column int
     * @return <code>Component</code> is the editor component to be painted on the table.
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        selected = isSelected;
        this.requestFocus();
        if (table.isCellEditable(row, column)) {
            this.setEnabled(true);
        } else {
            this.setEnabled(false);
        }
        if (value.equals(new Boolean(true))) {
            this.setSelected(true);
        } else {
            this.setSelected(false);
        }
        return this;
    }

    /**
     * sets the state of the cell.
     *
     * @param enabled boolean
     */
    public void setCellEnabled(boolean enabled) {
        cellEnabled = enabled;
    }

    /**
     * Determines whether this editor component is Editable or not if the return value is true then
     * it will behave as editable component other wise it can not allow to edit
     *
     * @param anEvent <code>EventObject</code>
     * @return boolean
     */
    public boolean isCellEditable(EventObject anEvent) {
        return cellEnabled;
    }

    /**
     * Remove the <code>CellEditorListener</code> from the listeners list.
     *
     * @param l <code>CellEditorListener</code>
     */
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    /**
     * Returns whether cell should be selectble or not when some event occurs.
     *
     * @param anEvent <code>EventObject</code>
     * @return boolean
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    /**
     * Stops the cell Editing invoked when the focus lost from this editorcomponent and puts the
     * values into model
     *
     * @return boolean
     */
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    /**
     * Invoked when stops the cell editing by removing thefocus from the editor component.
     */
    public void fireEditingStopped() {
        CellEditorListener listener;
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingStopped(changeEvent);
            }
        }
    }

    /**
     * Invoked when stops the cell editing when call cancell cell edit method on perticular cell it
     * will removes all editing values
     */
    public void fireEditingCanceled() {
        CellEditorListener listener;
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingCanceled(changeEvent);
            }
        }
    }
}
