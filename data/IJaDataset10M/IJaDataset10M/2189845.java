package org.pushingpixels.substance.internal.animation;

import java.awt.Component;
import java.awt.Container;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import javax.swing.*;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.RepeatBehavior;
import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;

/**
 * Tracker for pulsating (default and focused) <code>JButton</code>s. This class
 * is <b>for internal use only</b>.
 * 
 * @author Kirill Grouchnikov
 */
public class RootPaneDefaultButtonTracker extends UIThreadTimelineCallbackAdapter {

    /**
	 * Map (with weakly-referenced keys) of all trackers. For each default
	 * button which has not been claimed by GC, we have a tracker (with
	 * associated <code>Timer</code>).
	 */
    private static WeakHashMap<JButton, RootPaneDefaultButtonTracker> trackers = new WeakHashMap<JButton, RootPaneDefaultButtonTracker>();

    /**
	 * Waek reference to the associated button.
	 */
    private WeakReference<JButton> buttonRef;

    /**
	 * The associated timeline.
	 */
    private Timeline timeline;

    /**
	 * Simple constructor.
	 * 
	 * @param jbutton
	 */
    private RootPaneDefaultButtonTracker(JButton jbutton) {
        this.buttonRef = new WeakReference<JButton>(jbutton);
        this.timeline = new Timeline(jbutton);
        this.timeline.addCallback(this);
        RootPaneDefaultButtonTracker.trackers.put(jbutton, this);
    }

    /**
	 * Recursively checks whether the specified component or one of its inner
	 * components has focus.
	 * 
	 * @param component
	 *            Component to check.
	 * @return <code>true</code> if the specified component or one of its inner
	 *         components has focus, <code>false</code> otherwise.
	 */
    private static boolean isInFocusedWindow(Component component) {
        if (component == null) {
            return false;
        }
        if (component.isFocusOwner()) {
            return true;
        }
        if (component instanceof Container) {
            for (Component comp : ((Container) component).getComponents()) {
                if (isInFocusedWindow(comp)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Recursively checks whether the specified component has visible glass
	 * pane.
	 * 
	 * @param component
	 *            Component to check.
	 * @return <code>true</code> if the specified component has visible glass
	 *         pane, <code>false</code> otherwise.
	 */
    private static boolean hasGlassPane(Component component) {
        if (component == null) {
            return false;
        }
        Component glassPane = null;
        if (component instanceof JDialog) {
            glassPane = ((JDialog) component).getGlassPane();
        }
        if (component instanceof JFrame) {
            glassPane = ((JFrame) component).getGlassPane();
        }
        if ((glassPane != null) && (glassPane.isVisible())) {
            return true;
        }
        return false;
    }

    @Override
    public void onTimelineStateChanged(TimelineState oldState, TimelineState newState, float durationFraction, float timelinePosition) {
        this.onTimelineEvent();
    }

    @Override
    public void onTimelinePulse(float durationFraction, float timelinePosition) {
        this.onTimelineEvent();
    }

    void onTimelineEvent() {
        JButton jButton = this.buttonRef.get();
        if (jButton == null) {
            return;
        }
        if (!jButton.isDisplayable()) {
            timeline.abort();
            return;
        }
        if (RootPaneDefaultButtonTracker.hasGlassPane(jButton.getTopLevelAncestor())) return;
        if (!isPulsating(jButton)) {
            RootPaneDefaultButtonTracker tracker = trackers.get(jButton);
            tracker.stopTimer();
            tracker.buttonRef.clear();
            trackers.remove(jButton);
        } else {
            if (!RootPaneDefaultButtonTracker.isInFocusedWindow(jButton.getTopLevelAncestor())) {
                RootPaneDefaultButtonTracker.update(jButton);
            } else {
                if (!jButton.isEnabled()) {
                    if (timeline.getState() != TimelineState.SUSPENDED) {
                        timeline.suspend();
                    }
                } else {
                    if (timeline.getState() == TimelineState.SUSPENDED) {
                        timeline.resume();
                    }
                }
            }
        }
        if (SubstanceLookAndFeel.isCurrentLookAndFeel()) jButton.repaint();
    }

    /**
	 * Starts the associated timer.
	 */
    private void startTimer() {
        this.timeline.playLoop(RepeatBehavior.REVERSE);
    }

    /**
	 * Stops the associated timer.
	 */
    private void stopTimer() {
        this.timeline.cancel();
    }

    /**
	 * Returns the status of the associated timer.
	 * 
	 * @return <code>true</code> is the associated timer is running,
	 *         <code>false</code> otherwise.
	 */
    private boolean isRunning() {
        TimelineState state = this.timeline.getState();
        return ((state == TimelineState.PLAYING_FORWARD) || (state == TimelineState.PLAYING_REVERSE));
    }

    /**
	 * Updates the state of the specified button which must be a default button
	 * in some window. The button state is determined based on focus ownership.
	 * 
	 * @param jButton
	 *            Button.
	 */
    public static void update(JButton jButton) {
        if (jButton == null) return;
        boolean hasFocus = RootPaneDefaultButtonTracker.isInFocusedWindow(jButton.getTopLevelAncestor());
        RootPaneDefaultButtonTracker tracker = trackers.get(jButton);
        if (!hasFocus) {
            if (tracker == null) {
                return;
            }
        } else {
            if (tracker != null) {
                tracker.startTimer();
                return;
            }
            tracker = new RootPaneDefaultButtonTracker(jButton);
            tracker.startTimer();
            trackers.put(jButton, tracker);
        }
    }

    /**
	 * Retrieves the current cycle count for the specified button.
	 * 
	 * @param jButton
	 *            Button.
	 * @return Current cycle count for the specified button.
	 */
    public static float getTimelinePosition(JButton jButton) {
        RootPaneDefaultButtonTracker tracker = trackers.get(jButton);
        if (tracker == null) return 0;
        return tracker.timeline.getTimelinePosition();
    }

    /**
	 * Retrieves the animation state for the specified button.
	 * 
	 * @param jButton
	 *            Button.
	 * @return <code>true</code> if the specified button is being animated,
	 *         <code>false</code> otherwise.
	 */
    public static boolean isAnimating(JButton jButton) {
        RootPaneDefaultButtonTracker tracker = trackers.get(jButton);
        if (tracker == null) {
            return false;
        }
        return tracker.isRunning();
    }

    /**
	 * Returns memory usage.
	 * 
	 * @return Memory usage string.
	 */
    static String getMemoryUsage() {
        StringBuffer sb = new StringBuffer();
        sb.append("PulseTracker: \n");
        sb.append("\t" + trackers.size() + " trackers");
        return sb.toString();
    }

    /**
	 * Checks whether the specified button is pulsating.
	 * 
	 * @param jButton
	 *            Button.
	 * @return <code>true</code> if the specified button is pulsating,
	 *         <code>false</code> otherwise.
	 */
    public static boolean isPulsating(JButton jButton) {
        boolean isDefault = jButton.isDefaultButton();
        if (isDefault) {
            return true;
        }
        return false;
    }

    /**
	 * Stops all timers.
	 */
    public static void stopAllTimers() {
        for (RootPaneDefaultButtonTracker tracker : trackers.values()) {
            tracker.stopTimer();
        }
    }
}
