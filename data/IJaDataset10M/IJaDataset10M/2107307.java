package shag.activity;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * This is a view on all currently running ActivityTasks.  This was
 * created to be a similar facility to the Progress Tab in the Eclipse
 * IDE.
 * 
 * @author   zac
 * @version  $Revision: 1.6 $ $Date: 2005/06/17 21:10:51 $
 */
public class ActivityView extends JPanel implements ActivityManagerListener {

    public ActivityView() {
        _activities = new HashMap<ActivityTask<?>, ActivityProgressPanel>();
        _tasks = new ScrollableActivitiesPanel();
        _activityGBC = new GridBagConstraints();
        layoutComponents();
        getInitialData();
    }

    /**
     * Lays out the panel's user interface.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        _tasks.setLayout(new GridBagLayout());
        _activityGBC.weighty = 0;
        _activityGBC.weightx = 1;
        _activityGBC.gridwidth = GridBagConstraints.REMAINDER;
        _activityGBC.fill = GridBagConstraints.HORIZONTAL;
        JScrollPane sp = new JScrollPane(_tasks);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(sp, BorderLayout.CENTER);
    }

    private void getInitialData() {
        for (ActivityTask<?> at : ActivityManager.getInstance().getActivities()) {
            addActivity(at);
        }
    }

    /**
	 * @see java.awt.Component#addNotify()
	 */
    public void addNotify() {
        super.addNotify();
        ActivityManager.getInstance().addActivityManagerListener(this);
        getInitialData();
    }

    /**
	 * @see java.awt.Component#removeNotify()
	 */
    public void removeNotify() {
        super.removeNotify();
        ActivityManager.getInstance().removeActivityManagerListener(this);
    }

    /**
	 * @see shag.activity.ActivityManagerListener#addActivity(shag.activity.ActivityTask)
	 */
    public void addActivity(final ActivityTask<?> at) {
        if (!_activities.containsKey(at)) {
            final ActivityProgressPanel app = new ActivityProgressPanel(at);
            _tasks.add(app, _activityGBC);
            _activities.put(at, app);
            revalidate();
            repaint();
        }
    }

    /**
     * @see shag.activity.ActivityManagerListener#removeActivity(shag.activity.ActivityTask)
     */
    public void removeActivity(final ActivityTask<?> at) {
        if (_activities.containsKey(at)) {
            final ActivityProgressPanel app = _activities.remove(at);
            _tasks.remove(app);
            revalidate();
            repaint();
        }
    }

    private class ScrollableActivitiesPanel extends JPanel implements Scrollable {

        /**
         * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
         */
        public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(ActivityProgressPanel.WIDTH, ActivityProgressPanel.HEIGHT * 5);
        }

        /**
         * @see javax.swing.Scrollable#getScrollableUnitIncrement(java.awt.Rectangle, int, int)
         */
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            if (orientation == SwingConstants.VERTICAL) {
                int offset = visibleRect.y % ActivityProgressPanel.HEIGHT;
                return (offset == 0) ? ActivityProgressPanel.HEIGHT : (direction < 0) ? offset : ActivityProgressPanel.HEIGHT - offset;
            }
            return 1;
        }

        /**
         * @see javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle, int, int)
         */
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            if (orientation == SwingConstants.VERTICAL) {
                return visibleRect.height - ActivityProgressPanel.HEIGHT;
            }
            return ActivityProgressPanel.WIDTH;
        }

        /**
         * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
         */
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        /**
         * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
         */
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    private final Map<ActivityTask<?>, ActivityProgressPanel> _activities;

    private final ScrollableActivitiesPanel _tasks;

    private final GridBagConstraints _activityGBC;
}
