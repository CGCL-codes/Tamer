package common.graph;

import java.awt.*;

/**
 * User: honza
 * Date: Sep 27, 2006
 * Time: 11:10:06 PM
 */
class GraphGreyMapMark {

    private double x;

    private double y;

    private Color col;

    public GraphGreyMapMark(double x, double y, Color col) {
        this.x = x;
        this.y = y;
        this.col = col;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Color getCol() {
        return col;
    }
}
