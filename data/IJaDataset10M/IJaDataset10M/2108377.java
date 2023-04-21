package net.sf.dragonchess.game;

import java.util.List;
import net.sf.dragonchess.game.piece.Piece;

/**
 * Stores pieces that have been captured from the game board for both players.
 * 
 * @author Davy Herben
 * @version $Revision: 1.1 $ $Date: 2004/06/01 19:50:39 $
 */
public class CapturedList {

    /** the list of pieces */
    private List list;

    /**
	 * Adds a piece.
	 * 
	 * @param piece the piece to add.
	 */
    public void add(Piece piece) {
        list.add(piece);
    }

    /**
	 * Removes a piece.
	 * 
	 * @param piece the piece to remove
	 */
    public void remove(Piece piece) {
        list.remove(piece);
    }
}
