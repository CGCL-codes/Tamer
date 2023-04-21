package com.groovytagger.ui.frames.model;

import com.groovytagger.mp3.MP3Collection;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class FileTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    JComponent component = new JTextField();

    int rowIndex;

    int colIndex;

    Object oldValue;

    Object newValue;

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int vRowIndex, int vColIndex) {
        this.rowIndex = vRowIndex;
        this.colIndex = vColIndex;
        this.oldValue = value;
        ((JTextField) component).setText((String) value);
        return component;
    }

    public Object getCellEditorValue() {
        this.newValue = ((JTextField) component).getText();
        if (!newValue.equals(oldValue)) {
            MP3Collection.infoBeanConv[rowIndex].updateFileElementAt(colIndex, newValue);
        }
        return newValue;
    }

    public boolean isCellEditable(EventObject evt) {
        if (evt instanceof MouseEvent) {
            if (((MouseEvent) evt).getClickCount() >= 1) {
                return true;
            }
            return false;
        }
        return true;
    }

    public boolean shouldSelectCell(EventObject evt) {
        return true;
    }
}
