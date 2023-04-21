package upgmdendro.drawing.drawables;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import upgmdendro.drawing.GraphicParameters;

/**
 *
 * @author psychollek
 */
class DrawableLine implements Drawable {

    private Point a;

    private Point b;

    public DrawableLine(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void paint() {
        int h = GraphicParameters.getHeight();
        Graphics g = GraphicParameters.getGraphics();
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.drawLine(a.x, h - a.y, b.x, h - b.y);
        g.setColor(c);
    }
}
