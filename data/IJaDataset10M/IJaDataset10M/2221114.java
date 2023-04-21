package issrg.editor2.ifcondition;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * This Table Cell renderer, is used to return any JComponent inside the
 * cells.
 *
 * @author Christian Azzopardi
 */
public class ConditionsTableCellRenderer implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return (JComponent) value;
    }
}
