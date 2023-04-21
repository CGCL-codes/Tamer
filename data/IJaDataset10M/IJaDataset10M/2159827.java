package game.visualizations.graph;

import java.awt.Color;

public class TIntSpot {

    private int x, y, z;

    private Color c;

    public TIntSpot(int x, int y, int z, Color c) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.c = c;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Color getColor() {
        return c;
    }
}
