package tgreiner.amy.common.timer;

/**
 * A time control determines how much time is spent on each move.
 *
 * @author <a href="mailto:thorsten.greiner@googlemail.com">Thorsten Greiner</a>
 */
public interface TimeControl {

    /**
     * Get the soft limit.
     *
     * @return the soft limit
     */
    int getSoftLimit();

    /**
     * Get the hard limit.
     *
     * @return the hard limit.
     */
    int getHardLimit();

    /**
     * Set the remaining time.
     *
     * @param remaining the remaining time
     */
    void setRemainingTime(int remaining);
}
