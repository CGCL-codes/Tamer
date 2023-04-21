package org.fest.swing.testing;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 * Adapted from the <a href="http://java.sun.com/docs/books/tutorial/uiswing/" target="_blank">Swing Tutorial</a>.
 */
public class ColorEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private static final long serialVersionUID = 1L;

    Color currentColor;

    JButton button;

    JColorChooser colorChooser;

    JDialog dialog;

    protected static final String EDIT = "edit";

    public ColorEditor() {
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);
        colorChooser = new JColorChooser();
        dialog = JColorChooser.createDialog(button, "Pick a Color", true, colorChooser, this, null);
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
