package tgreiner.amy.reversi.engine;

import tgreiner.amy.bitboard.BitBoard;
import tgreiner.amy.common.timer.AlgorithmBasedTimer;
import tgreiner.amy.common.timer.LowerBoundTimerAlgorithm;
import tgreiner.amy.reversi.ReversiEngine;
import org.apache.log4j.Logger;

public abstract class AbstractEngine implements ReversiEngine {

    /** The Logger. */
    private static Logger log = Logger.getLogger(AbstractEngine.class);

    /** The board. */
    protected ReversiBoard board;

    /** The driver. */
    private Driver driver;

    /** The timer. */
    protected AlgorithmBasedTimer timer;

    /** The timer algorithm. */
    protected LowerBoundTimerAlgorithm timerAlgorithm;

    /** The remaining time. */
    private long remaining;

    /** @see AbstractEngine#reset */
    public void reset() {
        board = new ReversiBoard();
        timerAlgorithm = new LowerBoundTimerAlgorithm(0);
        timer = new AlgorithmBasedTimer(timerAlgorithm);
        driver = createDriver();
    }

    /**
     * Create a driver.
     *
     * @return the driver
     */
    protected abstract Driver createDriver();

    /** @see ReversiEngine#getMove */
    public int getMove() {
        int numDiscs = BitBoard.countBits(board.getWhite() | board.getBlack());
        int movesRemaining = Math.max(2, 54 - numDiscs);
        movesRemaining = Math.min(22, movesRemaining);
        int timePerMove = (int) (remaining / movesRemaining);
        log.info("Using " + timePerMove + " milliseconds.");
        timerAlgorithm.setDuration(timePerMove);
        return driver.search();
    }

    /** @see ReversiEngine#forceNull */
    public void forceNull() {
        board.doNull();
    }

    /** @see ReversiEngine#forceMove */
    public void forceMove(final int move) {
        board.doMove(move);
    }

    /** @see ReversiEngine#setGameTime */
    public void setGameTime(final long time) {
    }

    /** @see ReversiEngine#setRemainingTime */
    public void setRemainingTime(final long time) {
        remaining = time;
    }
}
