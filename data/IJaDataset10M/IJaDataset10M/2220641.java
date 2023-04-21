package com.visitrend.ndvis.table;

import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * 
 * @author John T. Langton - jlangton at visitrend dot com
 * 
 */
public class ColorCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    Color currentColor;

    JButton button;

    JColorChooser colorChooser;

    JDialog dialog;

    protected static final String EDIT = "edit";

    private int clickCount = 2;

    public ColorCellEditor() {
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);
        colorChooser = new JColorChooser();
        dialog = JColorChooser.createDialog(button, "Pick a Color", true, colorChooser, this, null);
    }

    public void setClickCount(int count) {
        clickCount = count;
    }

    public boolean isCellEditable(EventObject evt) {
        if (evt instanceof MouseEvent) {
            return ((MouseEvent) evt).getClickCount() >= clickCount;
        }
        return true;
    }

    /**
	 * Handles events from the editor button and from the dialog's OK button.
	 */
    public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            button.setBackground(currentColor);
            colorChooser.setColor(currentColor);
            dialog.setVisible(true);
            fireEditingStopped();
        } else {
            currentColor = colorChooser.getColor();
        }
    }

    public Object getCellEditorValue() {
        return currentColor;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentColor = (Color) value;
        return button;
    }
}
