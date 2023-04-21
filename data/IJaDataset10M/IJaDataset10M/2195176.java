package players.team5.GameState;

import java.awt.Color;
import java.awt.Point;

public class Piece {

    public int pieceCode;

    public Color pieceColor;

    public Point piecePos;

    public Boolean pieceIdentified;

    public Boolean pieceMoved;

    public Piece(int Pcode, Color Pcolor, Point Ppos, Boolean Piden) {
        pieceCode = Pcode;
        pieceColor = Pcolor;
        piecePos = Ppos;
        pieceIdentified = Piden;
    }

    public int getPieceCode() {
        return pieceCode;
    }

    public Color getPieceColor() {
        return pieceColor;
    }

    public Point getPiecePos() {
        return piecePos;
    }

    public Boolean getPieceMoved() {
        return pieceMoved;
    }

    public Boolean isIdentified() {
        return pieceIdentified;
    }

    public void setPieceCode(int Pcode) {
        pieceCode = Pcode;
    }

    public void setPieceColor(Color Pcolor) {
        pieceColor = Pcolor;
    }

    public void setPiecePos(Point Ppos) {
        piecePos = Ppos;
    }

    public void setPieceIdenified(Boolean Piden) {
        pieceIdentified = Piden;
    }

    public void setPieceMoved(Boolean set) {
        pieceMoved = set;
    }
}
