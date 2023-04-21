package net.sourceforge.dragonchess.backend;

import net.sourceforge.dragonchess.main.DCConstants;
import net.sourceforge.dragonchess.main.DCMoveList;

/**
 * Represents a Paladin piece on the board
 *
 * <p>A Paladin has the following moves :
 * <ul>
 * <li>on all boards, move and capture to all adjacent squares on the same
 * board
 * <li>move/capt between boards by making an unblockable knight-like move : 2
 * squares in one direction (including up and down) and then 1 in a
 * perpendicular direction.
 * <li>on the middle board, move/capt like a knight
 * </ul>
 * 
 * @author Davy Herben
 * @version 021208
 */
public class DCPaladinPiece extends DCPiece {

    public DCPaladinPiece(int player, DCGameBoard board) {
        super(player, board);
        type = 'P';
    }

    public DCMoveList getValidMoveList() {
        list = new DCMoveList();
        processUnblockableTarget(DCConstants.MOVE, 0, -1, -1);
        processUnblockableTarget(DCConstants.MOVE, 0, -1, 0);
        processUnblockableTarget(DCConstants.MOVE, 0, -1, 1);
        processUnblockableTarget(DCConstants.MOVE, 0, 0, -1);
        processUnblockableTarget(DCConstants.MOVE, 0, 0, 1);
        processUnblockableTarget(DCConstants.MOVE, 0, 1, -1);
        processUnblockableTarget(DCConstants.MOVE, 0, 1, 0);
        processUnblockableTarget(DCConstants.MOVE, 0, 1, 1);
        processUnblockableTarget(DCConstants.CAPT, 0, -1, -1);
        processUnblockableTarget(DCConstants.CAPT, 0, -1, 0);
        processUnblockableTarget(DCConstants.CAPT, 0, -1, 1);
        processUnblockableTarget(DCConstants.CAPT, 0, 0, -1);
        processUnblockableTarget(DCConstants.CAPT, 0, 0, 1);
        processUnblockableTarget(DCConstants.CAPT, 0, 1, -1);
        processUnblockableTarget(DCConstants.CAPT, 0, 1, 0);
        processUnblockableTarget(DCConstants.CAPT, 0, 1, 1);
        if (location.getBoard() == DCConstants.BOARD_MIDDLE) {
            processUnblockableTarget(DCConstants.MOVE, 0, -2, -1);
            processUnblockableTarget(DCConstants.MOVE, 0, -2, +1);
            processUnblockableTarget(DCConstants.MOVE, 0, -1, -2);
            processUnblockableTarget(DCConstants.MOVE, 0, -1, +2);
            processUnblockableTarget(DCConstants.MOVE, 0, 1, -2);
            processUnblockableTarget(DCConstants.MOVE, 0, 1, +2);
            processUnblockableTarget(DCConstants.MOVE, 0, 2, -1);
            processUnblockableTarget(DCConstants.MOVE, 0, 2, +1);
            processUnblockableTarget(DCConstants.CAPT, 0, -2, -1);
            processUnblockableTarget(DCConstants.CAPT, 0, -2, +1);
            processUnblockableTarget(DCConstants.CAPT, 0, -1, -2);
            processUnblockableTarget(DCConstants.CAPT, 0, -1, +2);
            processUnblockableTarget(DCConstants.CAPT, 0, 1, -2);
            processUnblockableTarget(DCConstants.CAPT, 0, 1, +2);
            processUnblockableTarget(DCConstants.CAPT, 0, 2, -1);
            processUnblockableTarget(DCConstants.CAPT, 0, 2, +1);
        }
        if (location.getBoard() != DCConstants.BOARD_TOP) {
            processUnblockableTarget(DCConstants.MOVE, 1, -2, 0);
            processUnblockableTarget(DCConstants.MOVE, 1, 0, -2);
            processUnblockableTarget(DCConstants.MOVE, 1, 0, 2);
            processUnblockableTarget(DCConstants.MOVE, 1, 2, 0);
            processUnblockableTarget(DCConstants.CAPT, 1, -2, 0);
            processUnblockableTarget(DCConstants.CAPT, 1, 0, -2);
            processUnblockableTarget(DCConstants.CAPT, 1, 0, 2);
            processUnblockableTarget(DCConstants.CAPT, 1, 2, 0);
        } else {
            processUnblockableTarget(DCConstants.MOVE, -2, -1, 0);
            processUnblockableTarget(DCConstants.MOVE, -2, 0, -1);
            processUnblockableTarget(DCConstants.MOVE, -2, 0, 1);
            processUnblockableTarget(DCConstants.MOVE, -2, 1, 0);
            processUnblockableTarget(DCConstants.CAPT, -2, -1, 0);
            processUnblockableTarget(DCConstants.CAPT, -2, 0, -1);
            processUnblockableTarget(DCConstants.CAPT, -2, 0, 1);
            processUnblockableTarget(DCConstants.CAPT, -2, 1, 0);
        }
        if (location.getBoard() != DCConstants.BOARD_BOTTOM) {
            processUnblockableTarget(DCConstants.MOVE, -1, -2, 0);
            processUnblockableTarget(DCConstants.MOVE, -1, 0, -2);
            processUnblockableTarget(DCConstants.MOVE, -1, 0, 2);
            processUnblockableTarget(DCConstants.MOVE, -1, 2, 0);
            processUnblockableTarget(DCConstants.CAPT, -1, -2, 0);
            processUnblockableTarget(DCConstants.CAPT, -1, 0, -2);
            processUnblockableTarget(DCConstants.CAPT, -1, 0, 2);
            processUnblockableTarget(DCConstants.CAPT, -1, 2, 0);
        } else {
            processUnblockableTarget(DCConstants.MOVE, +2, -1, 0);
            processUnblockableTarget(DCConstants.MOVE, +2, 0, -1);
            processUnblockableTarget(DCConstants.MOVE, +2, 0, 1);
            processUnblockableTarget(DCConstants.MOVE, +2, 1, 0);
            processUnblockableTarget(DCConstants.CAPT, +2, -1, 0);
            processUnblockableTarget(DCConstants.CAPT, +2, 0, -1);
            processUnblockableTarget(DCConstants.CAPT, +2, 0, 1);
            processUnblockableTarget(DCConstants.CAPT, +2, 1, 0);
        }
        return list;
    }
}
