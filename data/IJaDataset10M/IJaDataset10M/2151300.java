package org.qfirst.batavia.utils;

import org.qfirst.batavia.ui.*;
import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import org.qfirst.i18n.*;

/**
 *  Description of the Class
 *
 * @author     francisdobi
 * @created    May 6, 2004
 */
public class UIUtils {

    /**
	 *Constructor for the UIUtils object
	 */
    private UIUtils() {
    }

    /**
	 *  Description of the Method
	 *
	 * @param  parent        Description of the Parameter
	 * @param  t             Description of the Parameter
	 * @param  message       Description of the Parameter
	 * @param  title         Description of the Parameter
	 * @param  icon          Description of the Parameter
	 * @param  options       Description of the Parameter
	 * @param  defaultValue  Description of the Parameter
	 * @return               Description of the Return Value
	 */
    public static Object showErrorDialog(Component parent, Throwable t, Object message, String title, Icon icon, Object options[], Object defaultValue) {
        ThrowablePanel p = new ThrowablePanel(message, t);
        JOptionPane pane = new JOptionPane(p, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, icon, options, defaultValue);
        JDialog dialog = pane.createDialog(parent, title);
        dialog.setResizable(true);
        dialog.show();
        Object selectedValue = pane.getValue();
        return selectedValue;
    }

    /**
	 *  Description of the Method
	 *
	 * @param  parent   Description of the Parameter
	 * @param  t        Description of the Parameter
	 * @param  message  Description of the Parameter
	 * @param  title    Description of the Parameter
	 * @return          Description of the Return Value
	 */
    public static Object showErrorDialog(Component parent, Throwable t, Object message, String title) {
        return showErrorDialog(parent, t, message, title, null, null, null);
    }

    /**
	 *  Description of the Method
	 *
	 * @param  parent   Description of the Parameter
	 * @param  t        Description of the Parameter
	 * @param  message  Description of the Parameter
	 * @return          Description of the Return Value
	 */
    public static Object showErrorDialog(Component parent, Throwable t, Object message) {
        return showErrorDialog(parent, t, message, Message.getInstance().getMessage("title.error", "ERROR"));
    }

    /**
	 *  Gets the displayingRowCount attribute of the UIUtils class
	 *
	 * @param  table  Description of the Parameter
	 * @return        The displayingRowCount value
	 */
    public static int getDisplayingRowCount(JTable table) {
        Rectangle rect = table.getVisibleRect();
        return table.rowAtPoint(new Point(rect.width - 1, rect.height - 1)) - table.rowAtPoint(new Point(0, 0));
    }

    /**
	 * @param  parentComponent        Description of the Parameter
	 * @return                        The windowForComponent value
	 */
    public static Window getWindowForComponent(Component parentComponent) {
        if (parentComponent == null) {
            return null;
        }
        if (parentComponent instanceof Window) {
            return (Window) parentComponent;
        }
        return getWindowForComponent(parentComponent.getParent());
    }
}
