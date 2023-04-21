package packetsamurai.gui;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * @author Ulysses R. Ribeiro
 *
 */
@SuppressWarnings("serial")
public class IconTableRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

    public IconTableRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        Component c;
        if (value instanceof Component) {
            c = (Component) value;
            if (isSelected) {
                c.setForeground(table.getSelectionForeground());
                c.setBackground(table.getSelectionBackground());
            }
        } else {
            c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        }
        if (isSelected) {
            c.setForeground(table.getSelectionForeground());
            c.setBackground(table.getSelectionBackground());
        }
        return c;
    }
}
