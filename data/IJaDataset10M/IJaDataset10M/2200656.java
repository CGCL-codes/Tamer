package com.amarphadke.chess.server.rules;

import java.util.List;
import com.amarphadke.chess.server.domain.BoardPositionProvider;
import com.amarphadke.chess.server.domain.Move;
import com.amarphadke.chess.server.domain.Square;
import com.amarphadke.chess.server.domain.enums.Color;
import com.amarphadke.chess.server.domain.enums.MoveType;
import com.amarphadke.chess.server.domain.pieces.AbstractPiece;

public abstract class AbstractRule implements Rule {

    protected boolean isSquareTakeoverPossible(BoardPositionProvider boardPositionProvider, Square square, String color) {
        return isSquareOccupationPossibleByMoveType(boardPositionProvider, square, color, MoveType.TAKEOVER);
    }

    protected boolean isSquareAdvancePossible(BoardPositionProvider boardPositionProvider, Square square, String color) {
        return isSquareOccupationPossibleByMoveType(boardPositionProvider, square, color, MoveType.ADVANCE);
    }

    protected boolean isSquareOccupationPossible(BoardPositionProvider boardPositionProvider, Square square, String color) {
        return isSquareOccupationPossibleByMoveType(boardPositionProvider, square, color, null);
    }

    private boolean isSquareOccupationPossibleByMoveType(BoardPositionProvider boardPositionProvider, Square square, String color, String moveType) {
        boolean occupationPossible = false;
        List<Move> moves = boardPositionProvider.getAllPossibleMoves(color);
        for (Move move : moves) {
            if ((moveType == null || moveType.equals(move.getMoveType())) && square.equals(move.getNewPosition())) {
                occupationPossible = true;
                break;
            }
        }
        return occupationPossible;
    }

    protected List<Square> getInBetweenSquares(BoardPositionProvider boardPositionProvider, Square attackerSquare, Square kingSquare, AbstractPiece attackerPiece) {
        return null;
    }

    protected String getOpponentColor(String color) {
        if (Color.WHITE.equals(color)) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }
}
