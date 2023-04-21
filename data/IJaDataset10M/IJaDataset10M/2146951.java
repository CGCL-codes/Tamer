package uchicago.src.guiUtils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

public class ColorCellEditor extends AbstractCellEditor implements TableCellEditor, ChangeListener {

    private ColorChooserPopup chooser;

    JLabel l = new JLabel();

    public ColorCellEditor() {
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        chooser = new ColorChooserPopup();
        chooser.addColorChangeListener(this);
        chooser.setColor((Color) value);
        Rectangle rect = table.getCellRect(row, col, false);
        chooser.show(table, (int) rect.getX(), (int) rect.getY());
        return l;
    }

    public Object getCellEditorValue() {
        return chooser.getColor();
    }

    public void stateChanged(ChangeEvent evt) {
        chooser.setVisible(false);
        stopCellEditing();
        stopCellEditing();
    }
}
