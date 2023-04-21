package tgreiner.amy.common.timer;

/**
 * An implementation of TimeControl which represents a time control of
 * all moves in a given amount of time.
 *
 * @author <a href="mailto:thorsten.greiner@googlemail.com">Thorsten Greiner</a>
 */
public class SuddenDeathTimeControl implements TimeControl {

    /** Constant for microseconds per second. */
    private static final int MICROS_PER_SECOND = 1000;

    /** The number of moves allocated for the rest of the game. */
    private static final int MOVES = 60;

    /** The time. */
    private int time;

    /** The remaining time. */
    private int remainingTime;

    /**
     * Create a SuddenDeathTimeControl.
     *
     * @param theTime the time (in seconds)
     */
    public SuddenDeathTimeControl(final int theTime) {
        this.time = theTime;
        this.remainingTime = MICROS_PER_SECOND * theTime;
    }

    /** @see TimeControl#getSoftLimit */
    public int getSoftLimit() {
        int limit = (MICROS_PER_SECOND * time) / MOVES;
        if (limit > remainingTime) {
            limit = remainingTime / 2;
        }
        return limit;
    }

    /** @see TimeControl#getHardLimit */
    public int getHardLimit() {
        int limit = 4 * getSoftLimit();
        if (limit > remainingTime) {
            limit = (3 * remainingTime) / 4;
        }
        return limit;
    }

    /** @see TimeControl#setRemainingTime */
    public void setRemainingTime(final int remaining) {
        remainingTime = remaining;
    }
}
