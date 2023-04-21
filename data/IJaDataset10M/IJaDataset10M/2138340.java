package praktikumid.k11.p09.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Freeline extends Shape {

    private Color color;

    private List<Point> points = new ArrayList<Point>();

    public Freeline(Point from, Color color) {
        this.color = color;
        points.add(from);
    }

    public void addNextPoint(Point nextPoint) {
        points.add(nextPoint);
    }

    @Override
    public void paintShape(Graphics graphics) {
        graphics.setColor(color);
        for (int i = 1; i < points.size(); i++) {
            Point from = points.get(i - 1);
            Point to = points.get(i);
            graphics.drawLine(from.x, from.y, to.x, to.y);
        }
    }
}
