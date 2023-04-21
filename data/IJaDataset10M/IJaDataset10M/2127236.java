package basys.client.ui;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * ButtonTableCellRenderer.java
 * 
 * 
 * @author	oalt
 * @version $Id: ButtonTableCellRenderer.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class ButtonTableCellRenderer implements TableCellRenderer {

    /**
	 * 
	 */
    public ButtonTableCellRenderer() {
        super();
    }

    /**
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value.equals("on")) {
        }
        return new JButton("Test");
    }
}
