package org.berlin.tron.gl.game;

public class Point extends Move {

    public Point(final int x, final int y) {
        super(x, y);
    }

    public String toString() {
        return "${Move.Point x=" + this.getX() + " y=" + this.getY() + " [" + this.getMoveTimeMs() + "]}";
    }
}
