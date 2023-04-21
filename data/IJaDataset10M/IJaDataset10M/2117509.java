package blue.blueLive;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * @author steven
 */
public class MidiKeyRenderer extends JLabel implements TableCellRenderer {

    /** Creates a new instance of MidiKeyRenderer */
    public MidiKeyRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        String label = "";
        if (value instanceof Integer) {
            int val = ((Integer) value).intValue();
            if (val < 0 || val > 127) {
                label = "";
            } else {
                label = Integer.toString(val);
            }
        }
        this.setText(label);
        return this;
    }
}
