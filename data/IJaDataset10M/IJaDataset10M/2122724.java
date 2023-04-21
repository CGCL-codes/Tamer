package com.acarter.scenemonitor.propertydescriptor.editor;

import java.awt.Component;
import java.util.EventObject;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.acarter.scenemonitor.propertydescriptor.propertyobject.MaterialPropertyObject;
import com.jmex.physics.material.Material;

public class MaterialEditor extends JComboBox implements TableCellEditor {

    /**	 */
    private static final long serialVersionUID = 1L;

    /**  */
    protected EventListenerList listenerList = new EventListenerList();

    /**  */
    protected ChangeEvent changeEvent = new ChangeEvent(this);

    /**  */
    protected MaterialPropertyObject value = null;

    /**
	 * 
	 */
    public MaterialEditor() {
        super();
        addItem(Material.DEFAULT);
        addItem(Material.CONCRETE);
        addItem(Material.GHOST);
        addItem(Material.GLASS);
        addItem(Material.GRANITE);
        addItem(Material.ICE);
        addItem(Material.IRON);
        addItem(Material.OSMIUM);
        addItem(Material.PLASTIC);
        addItem(Material.RUBBER);
        addItem(Material.SPONGE);
        addItem(Material.WOOD);
        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                fireEditingStopped();
            }
        });
    }

    /**
	 * 
	 */
    public void addCellEditorListener(CellEditorListener listener) {
        listenerList.add(CellEditorListener.class, listener);
    }

    /**
	 * 
	 */
    public void removeCellEditorListener(CellEditorListener listener) {
        listenerList.remove(CellEditorListener.class, listener);
    }

    /**
	 * 
	 */
    protected void fireEditingStopped() {
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
	 * 
	 */
    protected void fireEditingCanceled() {
        CellEditorListener listener;
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingCanceled(changeEvent);
            }
        }
    }

    /**
	 * 
	 */
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    /**
	 * 
	 */
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    /**
	 * 
	 */
    public boolean isCellEditable(EventObject evt) {
        if (evt instanceof MouseEvent) return ((MouseEvent) evt).getClickCount() >= 2;
        return true;
    }

    /**
	 * 
	 */
    public boolean shouldSelectCell(EventObject event) {
        return true;
    }

    /**
	 * 
	 */
    public Object getCellEditorValue() {
        value.setValue((Material) getSelectedItem());
        return value;
    }

    /**
	 * 
	 */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof MaterialPropertyObject) {
            setSelectedItem(((MaterialPropertyObject) value).getValue());
            this.value = (MaterialPropertyObject) value;
        }
        return this;
    }
}
