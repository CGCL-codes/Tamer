package tgreiner.amy.chess.engine;

import tgreiner.amy.common.timer.AlgorithmBasedTimer;
import tgreiner.amy.common.timer.FixedTimeTimerAlgorithm;
import tgreiner.amy.common.timer.Timer;

/**
 * A benchmark for speed testing Java implementations and/or CPUs.
 *
 * @author <a href="mailto:thorsten.greiner@googlemail.com">Thorsten Greiner</a>
 */
public class Benchmark {

    /**
     * This class cannot be instantiated.
     */
    private Benchmark() {
    }

    /**
     * The main method.
     *
     * @param args the command line arguments.
     * @throws Exception if an error occurs
     */
    public static void main(final String[] args) throws Exception {
        ChessBoard board = new ChessBoard("r4k2/p3nppp/3q4/2Np1b2/1r1P3P/5QP1/P4PB1/2R1R1K1 w - -");
        TransTable ttable = new TransTableImpl2(14);
        Timer timer = new AlgorithmBasedTimer(new FixedTimeTimerAlgorithm(30 * 1000));
        Driver d = new Driver(board, ttable, timer);
        d.search();
    }
}
