package com.amarphadke.chess.server.domain.pieces;

import java.util.List;
import com.amarphadke.chess.server.domain.BoardPositionProvider;
import com.amarphadke.chess.server.domain.Move;
import com.amarphadke.chess.server.domain.Square;
import com.amarphadke.chess.server.domain.enums.Pieces;

public class AbstractKing extends AbstractPiece {

    public AbstractKing(String color) {
        super(color, Pieces.KING, Integer.MAX_VALUE);
    }

    @Override
    public String toString() {
        return "[" + color + "]King";
    }

    @Override
    protected List<Move> getPossibleMovesIfAlive(Square position, BoardPositionProvider boardPositionProvider) {
        return null;
    }
}
