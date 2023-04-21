package filemanager;

import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class SimpleFileRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 3192068598700937119L;

    private final Font DEFAULT_FONT = new Font(Font.DIALOG, Font.PLAIN, 12);

    private final Font SELECTED_FONT = new Font(Font.DIALOG, Font.BOLD, 12);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof ImageIcon) {
            lbl.setText("");
            lbl.setIcon((ImageIcon) value);
            lbl.setSize(160, 160);
            return lbl;
        }
        lbl.setIcon(null);
        if (isSelected) {
            lbl.setFont(SELECTED_FONT);
            return lbl;
        }
        lbl.setFont(DEFAULT_FONT);
        return lbl;
    }
}
