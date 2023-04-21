package de.javagimmicks.games.sudoku.model;

import java.io.Serializable;

public class Position implements Comparable<Position>, Serializable {

    private static final long serialVersionUID = 4120547373870134497L;

    private final int _row;

    private final int _column;

    public Position(int row, int column) {
        _row = row;
        _column = column;
    }

    public int getRow() {
        return _row;
    }

    public int getColumn() {
        return _column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        Position p = (Position) o;
        return this._row == p._row && this._column == p._column;
    }

    public boolean equals(int row, int column) {
        return _row == row && _column == column;
    }

    @Override
    public int hashCode() {
        return -3424983 + 3 * _row + 7 * _column;
    }

    public int compareTo(Position o) {
        int result = _row - o._row;
        return result != 0 ? result : _column - o._column;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(_row).append("/").append(_column).toString();
    }
}
