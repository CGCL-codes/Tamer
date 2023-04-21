package view.jscroll.widgets;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

/**
 * This class provides a custom desktop manager for
 * {@link org.jscroll.widgets.RootDesktopPane RootDesktopPane}.
 *
 * @author <a href="mailto:tessier@gabinternet.com">Tom Tessier</a>
 * @version 1.0  9-Aug-2001
 */
public class JScrollDesktopManager extends DefaultDesktopManager {

    private RootDesktopPane desktopPane;

    /**
     *  creates the JScrollDesktopManager
     *
     * @param desktopPane a reference to RootDesktopPane
     */
    public JScrollDesktopManager(RootDesktopPane desktopPane) {
        this.desktopPane = desktopPane;
    }

    /**
     * maximizes the internal frame to the viewport bounds rather
     * than the desktop bounds
     *
     * @param f the internal frame being maximized
     */
    public void maximizeFrame(JInternalFrame f) {
        Rectangle p = desktopPane.getScrollPaneRectangle();
        f.setNormalBounds(f.getBounds());
        setBoundsForFrame(f, p.x, p.y, p.width, p.height);
        try {
            f.setSelected(true);
        } catch (PropertyVetoException pve) {
            System.out.println(pve.getMessage());
        }
        removeIconFor(f);
    }

    /**
     * insures that the associated toolbar and menu buttons of
     * the internal frame are activated as well
     *
     * @param f the internal frame being activated
     */
    public void activateFrame(JInternalFrame f) {
        super.activateFrame(f);
        ((JScrollInternalFrame) f).selectFrameAndAssociatedButtons();
    }

    /**
     * closes the internal frame and removes any associated button
     * and menu components
     *
     * @param f the internal frame being closed
     */
    public void closeFrame(JInternalFrame f) {
        super.closeFrame(f);
        desktopPane.removeAssociatedComponents((JScrollInternalFrame) f);
        desktopPane.resizeDesktop();
    }
}
