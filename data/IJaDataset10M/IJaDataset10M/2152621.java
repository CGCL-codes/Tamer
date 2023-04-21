package org.xlim.sic.ig.windows.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 *
 * @author Tom Wheeler
 */
public class MenuUtils {

    private MenuUtils() {
    }

    /**
     * This method displays the supplied popup menu at the specified coordinates
     * and handles when the menu position might exceed the screen's dimensions.
     * This was copied from org.netbeans.core.windows.frames.DefaultContainerImpl.
     */
    public static void showPopupMenu(JPopupMenu popup, Point p, Component comp) {
        SwingUtilities.convertPointToScreen(p, comp);
        Dimension popupSize = popup.getPreferredSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (p.x + popupSize.width > screenSize.width) {
            p.x = screenSize.width - popupSize.width;
        }
        if (p.y + popupSize.height > screenSize.height) {
            p.y = screenSize.height - popupSize.height;
        }
        SwingUtilities.convertPointFromScreen(p, comp);
        popup.show(comp, p.x, p.y);
    }
}
