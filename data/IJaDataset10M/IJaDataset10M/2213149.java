package tr.swing;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * RadioButtonRenderer

 * @author <a href="mailto:jimoore@netspace.net.au">Jeremy Moore</a>
 */
public class RadioButtonRenderer implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        if (value == null) {
            return null;
        }
        return (Component) value;
    }
}
