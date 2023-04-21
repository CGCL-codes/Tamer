package com.xBrain.game.gomoku;

import java.io.Serializable;

public final class Point implements Serializable {

    private static final long serialVersionUID = 7957630131081964278L;

    public final int x;

    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "( " + x + " , " + y + " )";
    }

    public int dif() {
        return x - y;
    }
}
