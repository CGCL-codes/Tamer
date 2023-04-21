package de.frewert.vboxj.gui.swing;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * <pre>
 * Copyright (C) 2003-2005 Carsten Frewert. All Rights Reserved.
 * 
 * The VBox/J package (de.frewert.vboxj.*) is distributed under
 * the terms of the Artistic license.
 * </pre>
 * @author Carsten Frewert
 * &lt;<a href="mailto:frewert@users.sourceforge.net">
 * frewert@users.sourceforge.net</a>&gt;
 * @version $Revision: 1.4 $
 */
public class DurationCellRenderer extends DefaultTableCellRenderer {

    static final long serialVersionUID = 1832997036209944899L;

    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        if (!(value instanceof Long)) {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        long duration = ((Long) value).longValue();
        int seconds = (int) (duration % 60);
        duration /= 60;
        int minutes = (int) (duration % 60);
        duration /= 60;
        int hours = (int) (duration % 60);
        StringBuffer buf = new StringBuffer(8);
        if (hours > 0) {
            buf.append(hours).append(":");
        }
        buf.append(minutes).append(":");
        if (seconds < 10) {
            buf.append("0");
        }
        buf.append(seconds);
        Component renderer = super.getTableCellRendererComponent(table, buf.toString(), isSelected, hasFocus, row, column);
        if (renderer instanceof JLabel) {
            ((JLabel) renderer).setHorizontalAlignment(RIGHT);
        }
        return renderer;
    }
}
