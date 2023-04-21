package de.devisnik.web.client;

public class Move implements IMove {

    private final int x;

    private final int y;

    protected Move(int moveX, int moveY) {
        x = moveX;
        y = moveY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
