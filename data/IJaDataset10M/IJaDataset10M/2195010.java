package net.sourceforge.strategema.games;

/**
 * A rule that determines where a new piece can be placed on the board.
 * 
 * @author Lizzy
 * 
 */
public interface PiecePlacementRule {

    /**
	 * Tests if a piece can be placed on the desired square.
	 * @param piece The piece to be placed.
	 * @param position The square where the piece will be placed.
	 * @param board The game board.
	 * @param state The current game state.
	 * @return True if the piece can be placed in the specified position, or false if it cannot.
	 */
    public boolean isValid(PieceInPlay piece, Coordinate<?, ?, ?> position, BoardGeometry board, VolatileGameState state);
}
