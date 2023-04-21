package yore.boardgames.chess;

import yore.ai.*;
import java.util.*;

public class ChessMove {

    public ChessMove(int oldX, int oldY, int newX, int newY) {
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }

    public String toString() {
        return "(" + oldX() + ", " + oldY() + ") -> (" + newX() + ", " + newY() + ")";
    }

    public int oldX() {
        return oldX;
    }

    public int oldY() {
        return oldY;
    }

    public int newX() {
        return newX;
    }

    public int newY() {
        return newY;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ChessMove)) return false;
        ChessMove m = (ChessMove) o;
        return m.oldX() == oldX && m.oldY() == oldY && m.newX() == newX && m.newY() == newY;
    }

    private int oldX, oldY, newX, newY;
}
