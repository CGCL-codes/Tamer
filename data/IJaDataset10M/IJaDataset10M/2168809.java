package net.pandoragames.far.ui.swing.component;

import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for subwindowss. 
 * @author Olivier Wehner at 31.03.2008
 * <!-- copyright note --> 
 */
public class SubWindow extends JDialog {

    protected Log logger;

    /**
	 * Always specify owner and title. The window will be created non-modal.
	 * @param owner top level window
	 * @param title for this window
	 */
    public SubWindow(JFrame owner, String title) {
        super(owner, title, false);
        logger = LogFactory.getLog(this.getClass());
    }

    /**
	 * Constructor for modal subwindows (modal windows stay on top).
	 * @param owner top level window
	 * @param title for this window
	 * @param modal set to true for a modal window.
	 */
    public SubWindow(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        logger = LogFactory.getLog(this.getClass());
    }

    /**
	 * Positions this FileWindow on the screen. The position is choosen with respect to
	 * the specified screen center, the position of the owner and the position of the last 
	 * visible subwindow of the owner, if any.
	 * @param screenCenter coordinates of the screen center
	 */
    protected void placeOnScreen(Point screenCenter) {
        Window parent = getOwner();
        Point indent = parent.getLocationOnScreen();
        Point leftUpper = new Point(200, 50);
        Window[] peers = parent.getOwnedWindows();
        if (peers.length > 1) {
            int count = peers.length - 2;
            Window sister = null;
            do {
                sister = peers[count];
                count--;
            } while ((count >= 0) && ((sister == null) || (!sister.isShowing())));
            if ((sister != null) && (sister.isShowing())) {
                try {
                    leftUpper = sister.getLocationOnScreen();
                    indent = new Point(25, 25);
                } catch (IllegalComponentStateException icsx) {
                    leftUpper = new Point(screenCenter);
                }
            }
        }
        if (leftUpper == null) {
            logger.warn("Location was null, using screen center");
            leftUpper = new Point(screenCenter);
        }
        if (leftUpper.x > screenCenter.x) indent.x = -indent.x;
        if (leftUpper.y > screenCenter.y) indent.y = -indent.y;
        setLocation(leftUpper.x + indent.x, leftUpper.y + indent.y);
    }
}
