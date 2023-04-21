package org.jogre.rotary.common;

/**
 * Movement controller for the Scythe piece for the Rotary game.
 *
 * @author  Richard Walter
 * @version Beta 0.3
 */
public class RotaryScythePieceController extends RotaryPieceController {

    /**
	 * Constructor.
	 */
    public RotaryScythePieceController(int typeIndex, String longName, String shortName) {
        super(typeIndex, longName, shortName, new boolean[] { true, false, false, false });
    }

    /**
	 * Return the action that this piece can do after the given move.
	 *
	 * @param result   The move that we want to check what we can do afterwards.
	 *
	 * @return the action that is allowed after the given move.
	 */
    public int getPostMoveAction(RotaryMoveResult result) {
        return RotaryMoveResult.ACTION_ROTATE;
    }
}
