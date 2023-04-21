package ao.chess.v2.engine.mcts.player;

import ao.chess.v1.util.Io;
import ao.chess.v2.engine.Player;
import ao.chess.v2.engine.endgame.tablebase.DeepOracle;
import ao.chess.v2.engine.endgame.tablebase.DeepOutcome;
import ao.chess.v2.engine.mcts.*;
import ao.chess.v2.engine.mcts.message.MctsAction;
import ao.chess.v2.state.Move;
import ao.chess.v2.state.State;
import ao.util.math.rand.Rand;
import ao.util.time.Sched;
import it.unimi.dsi.fastutil.longs.LongLists;

/**
 * User: alex
 * Date: 27-Sep-2009
 * Time: 12:52:02 PM
 */
public class MctsPlayer implements Player {

    private final MctsNode.Factory nodes;

    private final MctsValue.Factory values;

    private final MctsRollout rollouts;

    private final MctsSelector sellectors;

    private final MctsHeuristic heuristics;

    private final MctsScheduler.Factory schedulers;

    private final TranspositionTable transTable;

    private State prevState = null;

    private MctsNode prevPlay = null;

    public <V extends MctsValue<V>> MctsPlayer(MctsNode.Factory<V> nodeFactory, MctsValue.Factory<V> valueFactory, MctsRollout rollOutInstance, MctsSelector<V> selectorInstance, MctsHeuristic heuristicInstance, TranspositionTable<V> transpositionTable, MctsScheduler.Factory schedulerFactory) {
        nodes = nodeFactory;
        values = valueFactory;
        rollouts = rollOutInstance;
        sellectors = selectorInstance;
        heuristics = heuristicInstance;
        transTable = transpositionTable;
        schedulers = schedulerFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int move(State position, int timeLeft, int timePerMove, int timeIncrement) {
        int oracleAction = oracleAction(position);
        if (oracleAction != -1) {
            Sched.sleep(2500);
            return oracleAction;
        }
        MctsNode root = null;
        if (prevState != null && prevPlay != null) {
            root = prevPlay.childMatching(action(prevState, position));
        }
        if (root == null) {
            root = nodes.newNode(position, values);
            transTable.retain(LongLists.EMPTY_LIST);
        } else {
            Io.display("Recycling " + root);
        }
        MctsScheduler scheduler = schedulers.newScheduler(timeLeft, timePerMove, timeIncrement);
        int count = 0;
        long lastReport = System.currentTimeMillis();
        while (scheduler.shouldContinue()) {
            root.runTrajectory(position, values, rollouts, transTable, heuristics);
            if (count++ != 0 && count % 10000 == 0) {
                long timer = System.currentTimeMillis() - lastReport;
                long before = System.currentTimeMillis();
                Io.display(root);
                Io.display(root.bestMove(sellectors).information());
                Io.display("took " + timer + " | " + (System.currentTimeMillis() - before));
                lastReport = System.currentTimeMillis();
            }
        }
        MctsAction act = root.bestMove(sellectors);
        if (act == null) return -1;
        prevPlay = act.node();
        prevState = position.prototype();
        Move.apply(act.action(), prevState);
        return act.action();
    }

    private int oracleAction(State from) {
        if (from.pieceCount() > 5) return -1;
        boolean canDraw = false;
        int bestOutcome = 0;
        int bestMove = -1;
        for (int legalMove : from.legalMoves()) {
            Move.apply(legalMove, from);
            DeepOutcome outcome = DeepOracle.INSTANCE.see(from);
            Move.unApply(legalMove, from);
            if (outcome == null || outcome.isDraw()) {
                canDraw = true;
                continue;
            }
            if (outcome.outcome().winner() == from.nextToAct()) {
                if (bestOutcome <= 0 || bestOutcome > outcome.plyDistance() || (bestOutcome == outcome.plyDistance() && Rand.nextBoolean())) {
                    Io.display(outcome.outcome() + " in " + outcome.plyDistance() + " with " + Move.toString(legalMove));
                    bestOutcome = outcome.plyDistance();
                    bestMove = legalMove;
                }
            } else if (!canDraw && bestOutcome <= 0 && bestOutcome > -outcome.plyDistance()) {
                Io.display(outcome.outcome() + " in " + outcome.plyDistance() + " with " + Move.toString(legalMove));
                bestOutcome = -outcome.plyDistance();
                bestMove = legalMove;
            }
        }
        return (bestOutcome <= 0 && canDraw) ? -1 : bestMove;
    }

    private int action(State from, State to) {
        int[] moves = new int[Move.MAX_PER_PLY];
        int nMoves = from.moves(moves);
        for (int i = 0; i < nMoves; i++) {
            int move = Move.apply(moves[i], from);
            if (from.equals(to)) {
                Move.unApply(move, from);
                return move;
            }
            Move.unApply(move, from);
        }
        return -1;
    }
}
