package ghm.followgui;

import java.awt.event.ActionEvent;

/**
 * Perform an action for debugging.
 * 
 * @author <a href="mailto:greghmerrill@yahoo.com">Greg Merrill</a>
 * @author Murali Krishnan
 */
class Debug extends FollowAppAction {

    Debug(final FollowApp app) {
        super(app, "Debug", "U", "U");
    }

    public void actionPerformed(final ActionEvent e) {
        System.out.println("Debug action.");
    }
}
