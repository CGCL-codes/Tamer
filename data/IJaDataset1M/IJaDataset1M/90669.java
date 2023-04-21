package org.statefive.feedstate.ui.swing;

import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.statefive.feedstate.feed.view.View;
import org.statefive.feedstate.feed.view.ViewException;
import org.statefive.feedstate.feed.view.ViewManager;

/**
  Internal frame for displaying a table view with a controller.
  
  @author rich
*/
public class InternalFeedFrame extends JInternalFrame {

    /** The view this this frame contains. */
    private View view;

    /** A set of controls embedded in this view. */
    private FeedStreamControlPanel panelControls;

    /**
   * Creates a new internal frame with the specified view and title.
   *
   * @param view the view that this frame receives updates from.
   *
   * @param title the title of the frame.
   */
    public InternalFeedFrame(View v, String title) {
        super(title, true, true, true, true);
        this.view = v;
        view.addStatusListener(v.getController());
        this.setLayout(new BorderLayout());
        final TableViewPanel tableView = new TableViewPanel(view);
        view.addFeedDataListener(tableView);
        this.add(tableView, BorderLayout.CENTER);
        this.setVisible(true);
        this.addInternalFrameListener(new InternalFeedFrameAdapter());
    }

    /**
   * Gets the view that this frame gets it's data from.
   *
   * @return the view; will never be {@code null}.
   */
    public View getView() {
        return view;
    }

    /**
   * Used to change the selected view for the view manager.
   */
    class InternalFeedFrameAdapter extends InternalFrameAdapter {

        /**
     * Removes this view from the view manager, then closes the view.
     * TODO note that close is called by the view; this is wrong, since
     * clsoing a view removes all status and feed listeners. The problem
     * with this is that if there are still any views open, they
     * will stop receiving information. This needs to be sorted out!
     *
     * @param e the event that was triggered upon the closing of the frame.
     */
        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            ViewManager.getInstance().getHubController().deleteObserver(panelControls);
            try {
                ViewManager.getInstance().removeView(view);
                view.close();
            } catch (ViewException vex) {
                vex.printStackTrace();
            }
        }

        /**
     * On activating a frame, the view manager is notified that this frame
     * is the current view. The control panel for this frame is also
     * added as an observer to the view manager's hub controller,
     * if the control panel exists.
     *
     * @param e the event that was triggered when this frame was selected.
     */
        @Override
        public void internalFrameActivated(InternalFrameEvent e) {
            ViewManager.getInstance().setSelectedView(view);
            if (panelControls != null) {
                ViewManager.getInstance().getHubController().addObserver(panelControls);
            }
        }

        /**
     * Signifies that a frame has been minimised. In this case, the next
     * non-minimised view (if it exists) is set to be the currently selected
     * view.
     *
     * @param e the event that was triggered when this frame was iconified.
     */
        @Override
        public void internalFrameIconified(InternalFrameEvent e) {
            ViewManager.getInstance().setSelectedView(null);
            ViewManager.getInstance().getHubController().deleteObserver(panelControls);
            JInternalFrame frame = getDesktopPane().getSelectedFrame();
            if (frame != null) {
                View view = ((InternalFeedFrame) frame).view;
                ViewManager.getInstance().setSelectedView(view);
            }
        }
    }
}
