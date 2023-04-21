package org.ascape.view.nonvis;

import java.util.TooManyListenersException;
import org.ascape.model.event.ScapeEvent;

/**
 * A view providing notification when a scape transitions from one state to
 * another. For example, when a scape was paused and is now resumed, an
 * apporiate method will be called. Additionally, this view also guarantees that
 * updateScapeGraphics of the view at least 6 times a second regardless of the
 * nature of the scape notification. This will allow pauses and resumes to be
 * noticed in time to immediatly alert the user, regardless of what is happening
 * with the model.
 * 
 * @author Miles Parker
 * @version 5.0
 * @history 5.0 8/13/02 factored out of ScapeTransitionView (now no graphics)
 * @history 3.0 8/13/02 refcatored out of ControlActionView
 * @since 3.0
 */
public class ScapeStateView extends NonGraphicView {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The last update in millis.
     */
    private long lastUpdateInMillis = 0;

    /**
     * The max millis between updates.
     */
    private long maxMillisBetweenUpdates = 1000 / 6;

    /**
     * Did the scape appear to be running last iteration?.
     */
    private boolean lastScapeAppearsRunning = false;

    /**
     * Did the scape appear to be paused last iteration?.
     */
    protected boolean lastScapeAppearsPaused = false;

    /**
     * Scape now running.
     */
    public void scapeNowRunning() {
    }

    /**
     * Scape now stopped.
     */
    public void scapeNowStopped() {
    }

    /**
     * Scape now paused.
     */
    public void scapeNowPaused() {
    }

    /**
     * Scape now resumed.
     */
    public void scapeNowResumed() {
    }

    /**
     * Scape now steppable.
     */
    public void scapeNowSteppable() {
    }

    /**
     * Environment now scape.
     */
    public void environmentNowScape() {
    }

    /**
     * Environment now no scape.
     */
    public void environmentNowNoScape() {
    }

    /**
     * Called on interation; delays models return by delay slider setting.
     * 
     * @param scapeEvent
     *            the scape event
     * @throws TooManyListenersException
     *             the too many listeners exception
     */
    public void scapeAdded(ScapeEvent scapeEvent) throws TooManyListenersException {
        super.scapeAdded(scapeEvent);
        lastScapeAppearsPaused = !getScape().isPaused();
        lastScapeAppearsRunning = !getScape().isRunning();
        environmentNowScape();
    }

    /**
     * Called on interation; delays models return by delay slider setting.
     * 
     * @param scapeEvent
     *            the scape event
     */
    public void scapeRemoved(ScapeEvent scapeEvent) {
        super.scapeRemoved(scapeEvent);
        scape = null;
        environmentNowNoScape();
    }

    /**
     * Allow for overriding thread implementations.
     */
    public void updateScapeState() {
        updateScapeStateImpl();
    }

    /**
     * Update the components. Ensures that the state of all buttons matchhes the
     * state of the observed scape.
     */
    public synchronized void updateScapeStateImpl() {
        if (scape != null) {
            boolean scapeAppearsRunning = getScape().isRunning();
            boolean scapeAppearsPaused = getScape().isPaused();
            if (scapeAppearsRunning && !lastScapeAppearsRunning) {
                scapeNowRunning();
                if (scapeAppearsPaused) {
                    scapeNowPaused();
                }
            } else if (!scapeAppearsRunning && lastScapeAppearsRunning) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
                scapeNowStopped();
            }
            if (scapeAppearsPaused && !lastScapeAppearsPaused) {
                scapeNowPaused();
            }
            if (!scapeAppearsPaused && lastScapeAppearsPaused && scapeAppearsRunning) {
                scapeNowResumed();
            }
            if (((scapeAppearsPaused && !lastScapeAppearsPaused) && scapeAppearsRunning) || ((scapeAppearsRunning && !lastScapeAppearsRunning) && scapeAppearsPaused)) {
                scapeNowSteppable();
            }
            lastScapeAppearsRunning = scapeAppearsRunning;
            lastScapeAppearsPaused = scapeAppearsPaused;
            lastUpdateInMillis = System.currentTimeMillis();
        }
    }

    /**
     * Returns true if the listener is intended to be used only for the current
     * scape; in this case returns false because control views typically will
     * exist for multiple scapes.
     * 
     * @return true, if is life of scape
     */
    public boolean isLifeOfScape() {
        return false;
    }

    /**
     * Notifies this view that something has happened on the scape. This view
     * then has a chance to update itself, and this super method then notifies
     * the scape that the view itself has been updated. By default, calls the
     * onStart, updateScapeGraphics, or onStop method as appropriate, and then
     * notifies scape.
     * 
     * @param scapeEvent
     *            a scape event update
     */
    public void scapeNotification(ScapeEvent scapeEvent) {
        super.scapeNotification(scapeEvent);
        if (System.currentTimeMillis() - lastUpdateInMillis > maxMillisBetweenUpdates) {
            updateScapeState();
        }
    }

    protected long getLastUpdateInMillis() {
        return lastUpdateInMillis;
    }

    protected void setLastUpdateInMillis(long lastUpdateInMillis) {
        this.lastUpdateInMillis = lastUpdateInMillis;
    }

    protected long getMaxMillisBetweenUpdates() {
        return maxMillisBetweenUpdates;
    }

    protected void setMaxMillisBetweenUpdates(long maxMillisBetweenUpdates) {
        this.maxMillisBetweenUpdates = maxMillisBetweenUpdates;
    }
}
