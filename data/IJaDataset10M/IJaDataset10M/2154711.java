package polychess.awt;

import polychess.piece.PolyChessPiece;

/**
 * Class to encapsulate the result of a move attempt.
 * @author mac
 */
public class MoveAttemptStatus {

    private final BoardPosition attemptedPosition;

    private final boolean isAllowed;

    private final PolyChessPiece attackedPiece;

    public MoveAttemptStatus(BoardPosition attemptedPosition, boolean isAllowed) {
        this(attemptedPosition, isAllowed, null);
    }

    public MoveAttemptStatus(BoardPosition attemptedPosition, boolean isAllowed, PolyChessPiece attackedPiece) {
        this.attemptedPosition = attemptedPosition;
        this.isAllowed = isAllowed;
        this.attackedPiece = attackedPiece;
        if (isAllowed && attackedPiece != null) throw new IllegalStateException("Cannot attack a piece at a position that can't be reached");
    }

    /**
	 Specifies whether or not the piece can be moved to the specified board position
	 @return true is the piece can be moved to this BoardPosition
	 */
    public boolean isAllowed() {
        return isAllowed;
    }

    /**
	 Specifies whether the current move, if allowed, is an attack on another piece
	 @return true is the piece can be moved to this and this is an attack
	 */
    public boolean isAttack() {
        return attackedPiece != null;
    }

    /**
	 If the current move is an attack move, this will return the piece that currently occupies the specified BoardPosition
	 @return true is the piece can be moved to this and this is an attack
	 */
    public PolyChessPiece getAttackedPiece() {
        return attackedPiece;
    }

    /**
	 The BoardPosition that is being checked as a possible move
	 */
    public BoardPosition getAttemptedPosition() {
        return attemptedPosition;
    }
}
