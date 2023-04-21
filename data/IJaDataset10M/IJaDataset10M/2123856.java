package Bookmark;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.LinkedHashMap;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author sahaqiel
 */
public class MarkTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -7756904906627770508L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (column == 1) {
            LinkedHashMap<TextAttribute, Object> map = new LinkedHashMap<TextAttribute, Object>();
            map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            map.put(TextAttribute.SIZE, 12);
            map.put(TextAttribute.FONT, Font.PLAIN);
            map.put(TextAttribute.FAMILY, Font.SANS_SERIF);
            map.put(TextAttribute.FOREGROUND, Color.blue);
            lbl.setFont(new Font(map));
        } else {
        }
        return lbl;
    }
}
