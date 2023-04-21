package ao.chess.v2.engine.mcts.heuristic;

import ao.chess.v2.data.MovePicker;
import ao.chess.v2.engine.mcts.MctsHeuristic;
import ao.chess.v2.state.Move;
import ao.chess.v2.state.State;

/**
 * User: alex
 * Date: 27-Sep-2009
 * Time: 11:54:01 PM
 */
public class MctsCaptureHeuristic implements MctsHeuristic {

    @Override
    public double firstPlayUrgency(int move) {
        return Move.isCapture(move) ? 1.5 + Math.random() / 10000 : 1.0 + Math.random() / 10000;
    }

    @Override
    public int[] orderMoves(State fromState, int[] moves, int nMoves) {
        return MovePicker.pickRandom(nMoves);
    }
}
