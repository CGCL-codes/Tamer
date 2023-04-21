package org.statefive.feedstate.feed.view;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.statefive.feedstate.feed.FeedStatus;

/**
 * Controller for stream feeds. Stream feeds can be read, paused or
 * unpaused.
 * 
 * @author rich
 */
public class FeedStreamController extends AbstractFeedController {

    /** Determines the paused status. */
    private boolean paused;

    /** Determines the stopped status. */
    private boolean stopped;

    /** Determines the running status. */
    private boolean running;

    /**
   * Caused when a feed updates it's status. The controller updates itself
   * accordingly, notifying registered listeners.
   * <p>
   * Stream controllers should ensure they are registered as listeners
   * to provide updates to the status of a feed.
   * </p>
   *
   * @param status the updated feed status; must not be {@code null}.
   */
    @Override
    public void feedStatusUpdate(FeedStatus status) {
        if (status.getText().toLowerCase().contains("running")) {
            this.setRunning(true);
            this.setPaused(false);
        } else if (status.getText().toLowerCase().contains("pause")) {
            this.setRunning(false);
            this.setPaused(true);
        }
    }

    /**
   * Determines if this controller's feed is paused.
   *
   * @return {@code true} if the feed is paused; {@code false} otherwise.
   */
    public boolean isPaused() {
        return paused;
    }

    /**
   * Sets the controller's paused state.
   *
   * @param paused {@code true} if the controller's feed is paused,
   * {@code false} otherwise.
   */
    public void setPaused(boolean paused) {
        this.paused = paused;
        this.setChanged();
        this.notifyObservers(null);
    }

    /**
   * Determines if this controller's feed is running.
   *
   * @return {@code true} if the feed is running; {@code false} otherwise.
   */
    public boolean isRunning() {
        return running;
    }

    /**
   * Sets the controller's running state.
   *
   * @param paused {@code true} if the controller's feed is running,
   * {@code false} otherwise.
   */
    public void setRunning(boolean running) {
        this.running = running;
        this.setChanged();
        this.notifyObservers(null);
    }

    /**
   * Provides human readable output.
   *
   * @return debug information.
   */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("Running", this.isRunning()).append("Paused", this.isPaused()).toString();
    }
}
