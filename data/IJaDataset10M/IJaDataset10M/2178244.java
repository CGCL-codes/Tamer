package org.chernovia.games.arcade.loxball;

import java.awt.Color;

public class LoxPlayer extends LoxBall {

    boolean vulnerable = false;

    int hits = 0, goofs = 0, score = 10000;

    public LoxPlayer(int minX, int minY, int maxX, int maxY) {
        super(minX, minY, maxX, maxY, 2);
        setSpeed(8);
        radius = 8;
        setStopped(true);
    }

    @Override
    public Color getColor() {
        if (vulnerable) return Color.WHITE; else return Color.GREEN;
    }

    @Override
    public void setStopped(boolean stop) {
        super.setStopped(stop);
        if (!isStopped()) vulnerable = true;
    }
}
