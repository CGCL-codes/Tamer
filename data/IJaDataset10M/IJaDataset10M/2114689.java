package gameinterface.piece;

import java.awt.Point;

public class Move {

    private int pieceCode;

    private Point initialPosition;

    private Point finalPosition;

    public Move(int pieceCode, Point initialPosition, Point finalPosition) {
        this.pieceCode = pieceCode;
        this.initialPosition = initialPosition;
        this.finalPosition = finalPosition;
    }

    public Point getInitialPosition() {
        return initialPosition;
    }

    public Point getFinalPosition() {
        return finalPosition;
    }

    public int getPieceCode() {
        return pieceCode;
    }
}
