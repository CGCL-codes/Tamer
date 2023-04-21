package Chess.Pieces;

import java.awt.Color;
import Chess.*;

/**
 *	A class representing a Knight in Chess.
 *
 *	@author		Jeff Chan, James Kuo
 */
public class Knight extends Piece {

    public Knight(Color color) {
        super(color);
    }

    public boolean canAttack(Board board, Square currentSquare, Square occupiedSquare) {
        return validSetupForAttack(board, currentSquare, occupiedSquare) && (differByTwoVerticalAndOneHorizontal(currentSquare, occupiedSquare) || differByOneVerticalAndTwoHorizontal(currentSquare, occupiedSquare));
    }

    public boolean canMove(Board board, Square currentSquare, Square emptySquare) {
        return validSetupForMove(board, currentSquare, emptySquare) && (differByTwoVerticalAndOneHorizontal(currentSquare, emptySquare) || differByOneVerticalAndTwoHorizontal(currentSquare, emptySquare));
    }

    private boolean differByOneVerticalAndTwoHorizontal(Square square1, Square square2) {
        return Math.abs(square1.distanceBetweenRank(square2)) == 2 && Math.abs(square1.distanceBetweenFile(square2)) == 1;
    }

    private boolean differByTwoVerticalAndOneHorizontal(Square square1, Square square2) {
        return Math.abs(square1.distanceBetweenRank(square2)) == 1 && Math.abs(square1.distanceBetweenFile(square2)) == 2;
    }

    public PieceType getPieceType() {
        return PieceType.KNIGHT;
    }
}
