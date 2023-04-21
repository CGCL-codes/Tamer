package org.jogre.rotary.common;

/**
 * Movement controller for the King piece for the Rotary game.
 *
 * @author  Richard Walter
 * @version Beta 0.3
 */
public class RotaryKingPieceController extends RotaryPieceController {

    /**
	 * Constructor.
	 */
    public RotaryKingPieceController(int typeIndex, String longName, String shortName) {
        super(typeIndex, longName, shortName, new boolean[] { true, false });
    }

    /**
	 * Determine if the given piece can move to the given space on the board.
	 *
	 * @param thePiece     The piece that is moving.
	 * @param direction    The direction that the piece is trying to move.
	 * @param distance     The distance that the piece is trying to move.
	 *
	 * @return true if the piece can move there.
	 */
    public boolean canMove(RotaryPiece thePiece, int direction, int distance) {
        return (distance == 1) && canMove(thePiece, direction);
    }
}
