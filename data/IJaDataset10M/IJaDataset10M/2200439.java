package de.buelowssiege.jaymail.gui.renderer;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * this is the renderer responsible for rendering some cells in the filtertable
 * 
 * @author Maximilian Schwerin
 * @created December 9, 2002
 */
public class ComponentRenderer extends DefaultTableCellRenderer {

    public ComponentRenderer() {
        super();
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel supercomponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof JComboBox) {
            JComboBox combobox = (JComboBox) value;
            combobox.setBackground(supercomponent.getBackground());
            return (combobox);
        } else if (value instanceof JTextField) {
            JTextField textfield = (JTextField) value;
            textfield.setBackground(supercomponent.getBackground());
            return (textfield);
        } else if (value instanceof JCheckBox) {
            JCheckBox checkbox = (JCheckBox) value;
            checkbox.setBackground(supercomponent.getBackground());
            return (checkbox);
        } else if (value instanceof JButton) {
            JButton button = (JButton) value;
            return (button);
        }
        return (supercomponent);
    }
}
