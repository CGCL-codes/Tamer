package org.xaware.server.updates;

import java.util.Collection;

public interface IServerUpdater {

    /**
     * Get the period by which this is to use between checks for updates
     * @return long representing milliseconds
     */
    public abstract long getPeriod();

    /**
     * Set the period by which this is to use between checks for updates
     * @param period long value in milliseconds
     */
    public abstract void setPeriod(long period);

    /**
     * Set the udater tasks to be scheduled and manager
     * @param updaters The updaters that will be managed
     */
    public abstract void setUpdaters(Collection<ServerUpdaterTask> updaters);

    /**
     * Called from the ServerUpdaterTask when it becomes active
     * @param task
     */
    public abstract void scheduleUpdater(ServerUpdaterTask task);

    /**
     * Called from ServerUpdaterTask when it is de-activated
     * @param task
     */
    public abstract void unscheduleUpdater(ServerUpdaterTask task);

    /**
     * Call this first and perform any initialization activities
     *
     */
    public abstract void initialize();

    /**
     * Call this once the updaters have been added and you are ready to start checking for updates
     *
     */
    public void start();
}
