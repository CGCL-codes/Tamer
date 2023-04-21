package logviewer;

import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.AbstractAction;

/**
 *  Sets the cursor to the bottom of the currently tailed log.
 *
 * @author    <a href="mailto:mrdon@techie.com">Don Brown</a>
 * @author    <a href="mailto:greghmerrill@yahoo.com">Greg Merrill</a>
 */
class Bottom extends AbstractAction {

    LogViewer app;

    /**
     *  Constructor for the Bottom action
     *
     * @param  app   The main class
     * @param  name  The name of the action
     */
    public Bottom(LogViewer app, String name) {
        super(name);
        this.app = app;
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
        JViewport viewport = fileFollowingPane.getViewport();
        int y = (int) (viewport.getViewSize().getHeight() - viewport.getExtentSize().getHeight());
        Point bottomPosition = new Point(0, y);
        viewport.setViewPosition(bottomPosition);
        viewport.revalidate();
    }
}
