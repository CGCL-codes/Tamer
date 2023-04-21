package org.hawksee.core.data;

public class Position {

    public int x;

    public int y;

    public Position() {
        this(0, 0);
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position clone() {
        return new Position(this.x, this.y);
    }

    public boolean equals(Position other) {
        return ((this.x == other.x) && (this.y == other.y));
    }
}
