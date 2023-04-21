package de.jassda.core.registry;

/**
 * Interface Clock
 *
 *
 * @author Mark Broerkens
 * @version %I%, %G%
 */
public interface Clock {

    /**
     * Method init
     *
     *
     * @param timestamp
     *
     */
    public void init(long timestamp);

    /**
     * Method suspend
     *
     *
     * @param timestamp
     *
     */
    public void suspend(long timestamp);

    /**
     * Method resume
     *
     *
     * @param timestamp
     *
     */
    public void resume(long timestamp);

    /**
     * Method setTime
     *
     *
     * @param timestamp
     * @param newTime
     *
     */
    public void setTime(long timestamp, long newTime);

    /**
     * Method getTime
     *
     *
     * @param timestamp
     *
     * @return
     *
     */
    public long getTime(long timestamp);
}
