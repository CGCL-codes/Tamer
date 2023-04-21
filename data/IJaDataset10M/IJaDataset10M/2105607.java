package tgreiner.amy.chess.engine;

import tgreiner.amy.common.engine.IntVector;

/**
 * A utility class to select moves by from/to square.
 *
 * @author <a href="mailto:thorsten.greiner@googlemail.com">Thorsten Greiner</a>
 */
public final class MoveSelector {

    /**
     * This class cannot be instantiated.
     */
    private MoveSelector() {
    }

    /**
     * Select a move via from/to square.
     *
     * @param board the board
     * @param from the from square
     * @param to the to square
     * @return the selected move
     */
    public static int selectMove(final ChessBoard board, final int from, final int to) {
        IntVector moves = new IntVector();
        board.generateLegalMoves(moves);
        for (int i = 0; i < moves.size(); i++) {
            int move = moves.get(i);
            if (Move.getTo(move) == to && Move.getFrom(move) == from) {
                return move;
            }
        }
        return -1;
    }
}
