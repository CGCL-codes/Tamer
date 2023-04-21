package net.sf.dub.miniframework.view.swing;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class StandardTableCellRenderer extends DefaultTableCellRenderer {

    private static Color COLOR_ODD = new Color(238, 242, 255);

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!isSelected) {
            setBackground(row);
        }
        return this;
    }

    public void setBackground(int row) {
        if ((row % 2) != 0) {
            setBackground(COLOR_ODD);
        } else {
            setBackground(Color.WHITE);
        }
    }
}
