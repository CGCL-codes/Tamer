package org.vikamine.gui.subgroup.editors.zoomtable;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author Tobias Vogele
 */
public class SelectionTableRenderer extends JCheckBox implements TableCellRenderer {

    /**
     * 
     */
    private static final long serialVersionUID = -262548208813878675L;

    private JLabel emptyLabel = new JLabel();

    public SelectionTableRenderer() {
        super();
        setHorizontalAlignment(LEADING);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null) {
            return emptyLabel;
        }
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        Boolean att = (Boolean) value;
        setSelected(att.booleanValue());
        return this;
    }
}
