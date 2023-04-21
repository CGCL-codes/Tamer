package net.sourceforge.squirrel_sql.client.gui.db;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import net.sourceforge.squirrel_sql.fw.sql.ISQLDriver;

/**
 * A cell renderer, that shows Drivers that could not be loaded with
 * one icon and those can could be loaded with another icon.
 *
 * @author Henner Zeller
 */
class DriverListCellRenderer extends DefaultListCellRenderer {

    /** Icon for drivers that could be loaded. */
    private final Icon OK_ICON;

    /** Icon for drivers that could not be loaded. */
    private final Icon FAIL_ICON;

    public DriverListCellRenderer(Icon ok, Icon fail) {
        OK_ICON = ok;
        FAIL_ICON = fail;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        final ISQLDriver drv = (ISQLDriver) value;
        if (drv == null || !drv.isJDBCDriverClassLoaded()) {
            setIcon(FAIL_ICON);
        } else {
            setIcon(OK_ICON);
        }
        return this;
    }
}
