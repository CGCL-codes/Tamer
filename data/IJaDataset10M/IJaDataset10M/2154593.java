package algorithem;

import java.util.*;

/**
 * The Position class represents a place on the gameboard. It also
 *         can tell me which other positions are related to it, either
 *         horizontally, vertically or in a small 3x3 box.
 */
public final class Position {

    private final int row;

    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int hashCode() {
        return row * 29 + col;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        final Position position = (Position) o;
        return !(col != position.col || row != position.row);
    }

    public String toString() {
        return "(" + row + "," + col + ")";
    }

    public Collection<Position> getRelatedPositions() {
        Collection<Position> result = new HashSet<Position>();
        result.addAll(getHorizontalPositions());
        result.addAll(getVerticalPositions());
        result.addAll(getSmallSquarePositions());
        return result;
    }

    public Collection<Position> getHorizontalPositions() {
        Collection<Position> result = new HashSet<Position>();
        for (int i = 0; i < 9; i++) {
            result.add(new Position(row, i));
        }
        result.remove(this);
        return result;
    }

    public Collection<Position> getVerticalPositions() {
        Collection<Position> result = new HashSet<Position>();
        for (int i = 0; i < 9; i++) {
            result.add(new Position(i, col));
        }
        result.remove(this);
        return result;
    }

    public Collection<Position> getSmallSquarePositions() {
        Collection<Position> result = new HashSet<Position>();
        for (int i = 0; i < 9; i++) {
            int smallSqRow = i / 3 + (row / 3) * 3;
            int smallSqCol = i % 3 + (col / 3) * 3;
            result.add(new Position(smallSqRow, smallSqCol));
        }
        result.remove(this);
        return result;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
