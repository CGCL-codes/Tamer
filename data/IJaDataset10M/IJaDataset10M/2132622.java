package net.sf.dpdesktop.module.tracking;

import net.sf.dpdesktop.service.container.Container;
import net.sf.dpdesktop.service.log.LogItem;

/**
 *
 * @author Heiner Reinhardt
 */
public interface Tracker {

    public void addListener(TrackingListener l);

    /**
     * Returns the hours of the tracked time.
     * @return Hour
     */
    public int getHours();

    /**
     * Returns the minutes of the tracked time.
     * @return Minute
     */
    public int getMinutes();

    public int getSeconds();

    /**
     * Returns the tracked time.
     * @return time in seconds
     */
    public int getTime();

    /**
     * Return time in format HH:mm:ss
     * @return String
     */
    public String getTimeAsString();

    /**
     * Getter for the current container tracked
     * @return Any container
     */
    public Container getTrackableContainer();

    public boolean isCleared();

    public boolean isInIdle();

    public LogItem createLogItem(String summary, String comment, int complete, int workedH, int workedM, int billableH, int billableM);

    public boolean wasInIdle();

    public boolean isRunning();
}
