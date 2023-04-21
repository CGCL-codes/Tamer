package net.sourceforge.yagurashogi.client.core.pieces;

import net.sourceforge.yagurashogi.client.gui.Piece;

public class Bishop extends Piece {

    public Bishop(int side) {
        this.code = "KA";
        this.isPromoted = false;
        this.canPromote = true;
        this.side = side;
        this.jumpOver = false;
        boolean[][] movesTmp = { { false, false, false, false, false }, { false, false, false, false, false }, { false, false, false, false, false }, { false, false, false, false, false }, { false, false, false, false, false } };
        this.moves = movesTmp;
        boolean[] directionsTmp = { false, true, false, true, false, true, false, true };
        this.directions = directionsTmp;
    }

    public Piece getPromotedPiece() {
        return new Horse(side);
    }

    public Piece getDemotedPiece() {
        return this;
    }
}
