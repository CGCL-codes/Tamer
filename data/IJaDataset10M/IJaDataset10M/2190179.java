package net.sourceforge.plantuml.salt;

public class Position {

    private final int row;

    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public int hashCode() {
        return row * 49 + col;
    }

    @Override
    public boolean equals(Object obj) {
        final Position other = (Position) obj;
        return row == other.row && col == other.col;
    }
}
