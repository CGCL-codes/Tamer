package clubmixerlibraryeditor.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Alexander Schindler
 */
public class FileListTableCellRenderer extends DefaultTableCellRenderer {

    public FileListTableCellRenderer() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof ImageIcon) {
            setIcon((Icon) value);
            setText(null);
        } else if (value instanceof String) {
            setText((String) value);
            setIcon(null);
        }
        if ((table.getValueAt(row, 6) != null) && ((String) table.getValueAt(row, 6)).equals("1")) setForeground(Color.orange); else setForeground(Color.gray);
        return this;
    }
}
