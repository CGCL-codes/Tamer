package logviewer;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;

/**
 *  Tails the content of the currently selected log file
 *
 * @author    <a href="mailto:mrdon@techie.com">Don Brown</a>
 * @author    <a href="mailto:greghmerrill@yahoo.com">Greg Merrill</a>
 */
class Tail extends AbstractAction {

    LogViewer app;

    Bottom bottom;

    /**
     *  Constructor for the Tail action
     *
     * @param  app   The main class
     * @param  name  The name of the action
     */
    public Tail(LogViewer app, String name) {
        super(name);
        this.app = app;
        bottom = new Bottom(app, app.getProperty("lastLine.label"));
    }

    /**
     *  Performs the action
     *
     * @param  e  The action event
     */
    public void actionPerformed(ActionEvent e) {
        FileFollowingPane fileFollowingPane = app.getSelectedFileFollowingPane();
        if (fileFollowingPane == null) {
            return;
        }
        fileFollowingPane.toggleAutoPositionCaret();
        bottom.actionPerformed(e);
    }
}
