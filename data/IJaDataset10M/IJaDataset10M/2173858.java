package com.genia.toolbox.portlet.editor.gui.view.settings.table;

import java.awt.Component;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import com.genia.toolbox.portlet.editor.model.portlet.impl.HoleModel;

/**
 * The hole table cell editor.
 */
@SuppressWarnings("serial")
public class HoleCellEditor extends AbstractCellEditor implements TableCellEditor {

    /**
   * The current component.
   */
    private Component component = null;

    /**
   * The list of portlets.
   */
    private List<String> portlets = null;

    /**
   * Constructor.
   * 
   * @param portlets
   *          the portlets.
   */
    public HoleCellEditor(List<String> portlets) {
        super();
        this.portlets = portlets;
    }

    /**
   * Handle the display.
   * 
   * @param table
   *          The table.
   * @param value
   *          The displayed value.
   * @param isSelected
   *          Whether the cell is selected.
   * @param row
   *          The row index.
   * @param column
   *          The column index.
   * @return the component.
   */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        HoleTableModel tableModel = (HoleTableModel) table.getModel();
        HoleModel hole = tableModel.getValueAt(row);
        if (column == 0) {
            JLabel label = new JLabel();
            label.setText(" " + hole.getName());
            this.component = label;
        } else {
            JComboBox comboBox = new JComboBox(this.portlets.toArray());
            if (hole.getValue() != null && hole.getValue().trim().length() != 0) {
                comboBox.setSelectedItem(hole.getValue());
            }
            this.component = comboBox;
        }
        return this.component;
    }

    /**
   * Get the cell value.
   * 
   * @return the cell value.
   */
    public Object getCellEditorValue() {
        Object value = null;
        if (this.component != null) {
            if (this.component instanceof JLabel) {
                value = ((JLabel) this.component).getText();
            } else if (this.component instanceof JComboBox) {
                value = ((JComboBox) this.component).getSelectedItem();
            }
        }
        return value;
    }
}
