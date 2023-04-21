package tgreiner.amy.reversi.engine;

import tgreiner.amy.chess.engine.Searcher;
import tgreiner.amy.common.engine.Generator;
import tgreiner.amy.common.engine.NodeType;
import tgreiner.amy.common.timer.TimeOutException;
import tgreiner.amy.common.timer.Timer;
import tgreiner.amy.bitboard.BitBoard;
import org.apache.log4j.Logger;

/**
 * Implements the alpha-beta search.
 *
 * @author Thorsten Greiner
 */
public class AlphaBetaWithProbCut implements Searcher {

    /** The log4j Logger. */
    private static Logger log = Logger.getLogger(AlphaBetaWithProbCut.class);

    /** The board we search. */
    private ReversiBoard board;

    /** The move generators. */
    private Generator[] generators;

    /** The move generators. */
    private Generator[] generatorsDeep;

    /** The timer. */
    private Timer timer;

    private TransTableImpl ttable;

    /** The PVSaver. */
    private PVSaver pvsaver;

    /** The quiescence search. */
    private QuiescenceSearch qsearch;

    /** Number of nodes visited. */
    private int nodes = 0;

    /** The maximum search depth. */
    private static final int MAX_DEPTH = 64;

    /** The prob cut margin. */
    private static final int DEFAULT_MARGIN = 50;

    /** The selective search margin. */
    private int margin = DEFAULT_MARGIN;

    private int marginScale = 50;

    private int depthRed = 4;

    /**
	 * Create an AlphaBeta searcher with prob cuts.
	 *
	 * @param board the board to search.
	 * @param eval the Evaluator
	 * @param pvsaver the principal variation saver.
	 * @param timer the timer.
	 */
    public AlphaBetaWithProbCut(final ReversiBoard board, final Evaluator eval, final TransTableImpl ttable, final PVSaver pvsaver, final Timer timer) {
        this.board = board;
        this.ttable = ttable;
        this.pvsaver = pvsaver;
        this.timer = timer;
        this.qsearch = new QuiescenceSearch(board, eval, pvsaver);
        initGenerators(eval);
    }

    /**
	 * Initialze the move generators.
	 */
    private void initGenerators(Evaluator eval) {
        generators = new Generator[MAX_DEPTH];
        generatorsDeep = new Generator[MAX_DEPTH];
        for (int i = MAX_DEPTH - 1; i >= 0; i--) {
            generators[i] = new MoveGenerator(board, ttable);
            generatorsDeep[i] = new MoveGenerator3(board, ttable, eval);
        }
    }

    /** @see Searcher#getNodes */
    public int getNodes() {
        return nodes + qsearch.getNodes();
    }

    /** @see Searcher#search */
    public int search(final int alpha, final int beta, final int depth, final NodeType nodeType) throws TimeOutException {
        return search(alpha, beta, depth, 1, true);
    }

    /**
	 * Search the board.
	 *
	 * @param alpha the alpha value
	 * @param beta the beta value
	 * @param depth the remaining depth
	 * @param ply the ply
	 * @return the search value
	 * @throws TimeOutException if the timer expires
	 */
    int search(final int alpha, final int beta, final int depth, final int ply, final boolean isSelective) throws TimeOutException {
        if (depth <= 0) {
            return qsearch.search(alpha, beta, ply);
        }
        if (board.isFinalPosition()) {
            int balance = board.getBalance();
            if (!board.isWtm()) {
                balance = -balance;
            }
            return balance * 100;
        }
        nodes++;
        timer.check();
        TTEntry entry = ttable.get(board);
        if (entry != null) {
            if (entry.getDepth() >= depth) {
                if (entry.isExact()) {
                    pvsaver.terminal(ply);
                    return entry.score;
                }
                if (entry.isLower()) {
                    if (entry.score >= beta) {
                        return entry.score;
                    }
                }
                if (entry.isUpper()) {
                    if (entry.score <= alpha) {
                        return entry.score;
                    }
                }
            }
        }
        if (isSelective && depth >= 4) {
            int bound;
            int t = depth - 2 - (depth / depthRed);
            bound = beta + margin + t * marginScale;
            if (search(bound - 1, bound, t, ply, false) >= bound) {
                return beta;
            }
            bound = alpha - margin - t * marginScale;
            if (search(bound, bound + 1, t, ply, false) <= bound) {
                return alpha;
            }
        }
        int best = -30000;
        Generator gen;
        if (depth > 3) {
            gen = generatorsDeep[ply];
        } else {
            gen = generators[ply];
        }
        gen.reset();
        int move;
        int bestMove = -1;
        boolean noLegalMove = true;
        while ((move = gen.nextMove()) != -1) {
            int tmp;
            noLegalMove = false;
            board.doMove(move);
            try {
                if (noLegalMove) {
                    tmp = -search(-beta, -Math.max(alpha, best), depth - 1, ply + 1, false);
                } else {
                    int bound = Math.max(alpha, best);
                    tmp = -search(-bound - 1, -bound, depth - 1, ply + 1, true);
                    if (tmp > bound && tmp < beta) {
                        tmp = -search(-beta, -tmp, depth - 1, ply + 1, false);
                    }
                }
            } finally {
                board.undoMove();
            }
            if (tmp > best) {
                best = tmp;
                bestMove = move;
                if (best >= beta) {
                    gen.failHigh(move, depth);
                    break;
                }
                if (tmp > alpha) {
                    pvsaver.move(ply, bestMove);
                }
            }
        }
        if (depth > 0 && noLegalMove) {
            if (board.wasLastMoveNull()) {
                int balance = board.getBalance();
                if (!board.isWtm()) {
                    balance = -balance;
                }
                if (balance > 0) {
                    return balance * 100;
                } else if (balance < 0) {
                    return balance * 100;
                } else {
                    return 0;
                }
            }
            board.doNull();
            int tmp;
            try {
                tmp = -search(-beta, -Math.max(alpha, best), depth, ply + 1, true);
            } finally {
                board.undoMove();
            }
            if (tmp > best) {
                best = tmp;
                if (tmp > alpha && tmp < beta) {
                    pvsaver.nullMove(ply);
                }
            }
        }
        ttable.store(board, bestMove, depth, best, alpha, beta);
        return best;
    }

    /** @see Searcher#reset */
    public void reset() {
        nodes = 0;
        qsearch.resetStats();
    }

    /**
	 * Set the selective search margin.
	 */
    void setMargin(final int margin) {
        this.margin = margin;
    }

    /**
	 * Setter for marginScale.
	 *
	 * @param newMarginScale new value for marginScale
	 */
    void setMarginScale(int newMarginScale) {
        marginScale = newMarginScale;
    }

    /**
	 * Setter for depthRed.
	 *
	 * @param newDepthRed new value for depthRed
	 */
    void setDepthRed(int newDepthRed) {
        depthRed = newDepthRed;
    }
}
