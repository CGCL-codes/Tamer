package jpicedt.widgets;

import javax.swing.*;
import java.awt.*;

/**
 * A customized cell-renderer for the Motif LAF (turns selected item background to white) 
 * @author Sylvain Reynal
 * @since jpicedt 1.2
 * @version $Id: MotifLAFCellRenderer.java,v 1.6 2011/07/23 05:36:06 vincentb1 Exp $
 * <p>
 * @see javax.swing.JList
 */
public class MotifLAFCellRenderer extends JLabel implements ListCellRenderer {

    public MotifLAFCellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setBackground(isSelected ? Color.white : Color.lightGray);
        setIcon((ImageIcon) value);
        return this;
    }
}
