package com.editev.chess;

import java.util.Iterator;

/** This class represents a Square on a chess board as a column and row position. * *  @see See the source <a href="Square.java">here</a>. */
public class Square {

    /** The row address of this square in the chess board with row=0 meaning rank 8 in algebraic chess notation. */
    public byte row;

    /** The column address of this square in the chess board with column=0 meaning file a and column=7 meaning file h      *  in algebraic chess notation. */
    public byte column;

    public Square() {
    }

    public Square(byte column, byte row) {
        this.row = row;
        this.column = column;
    }

    public Square(int column, int row) {
        this((byte) column, (byte) row);
    }

    /** Is this Square inside the board? Squares are valid, Moves are legal....     *  @return true if this square is within the confines of the chessboard.      */
    public boolean isWithinBoard() {
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }

    /** @return true if Object is a Square and they have the same row and column elements. */
    public boolean equals(Object object) {
        if (!(object instanceof Square)) return false;
        Square location = (Square) object;
        return row == location.row && column == location.column;
    }

    /** Square in chess notation.      *  @return Classic chess notation for this square      */
    public String toString() {
        return ((char) ('a' + column)) + "" + (8 - row);
    }

    /** @return true if this square is a white square. */
    public final boolean isWhite() {
        return 0 == (1 & column + row);
    }

    /** Copy another Square into this Square. */
    public void copyFrom(Square square) {
        row = square.row;
        column = square.column;
    }

    Iterator foo() {
        return null;
    }
}
