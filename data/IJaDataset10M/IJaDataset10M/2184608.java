package jshm.wts.gui.editors;

import java.awt.Component;
import java.util.Arrays;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import jshm.Difficulty;

public class DifficultyEditor extends AbstractCellEditor implements TableCellEditor {

    JComboBox combo = new JComboBox(Arrays.copyOf(Difficulty.values(), 4));

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        combo.setSelectedItem(value);
        return combo;
    }

    @Override
    public Object getCellEditorValue() {
        return combo.getSelectedItem();
    }
}
