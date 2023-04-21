package data.geom;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ToroidalMapGeometry extends PlainMapGeometry {

    protected final double hsize;

    public ToroidalMapGeometry(double size) {
        super(size);
        hsize = 0.5 * size;
    }

    @Override
    public double distance(Position pos1, Position pos2) {
        double x = Math.abs(pos1.getX() - pos2.getX());
        double y = Math.abs(pos1.getY() - pos2.getY());
        while (x > hsize) x -= size;
        while (y > hsize) y -= size;
        return Math.hypot(x, y);
    }

    @Override
    public Position relative(Position base, Position pos) {
        double x = pos.getX() - base.getX();
        double y = pos.getY() - base.getY();
        while (x > hsize) x -= size;
        while (x < -hsize) x += size;
        while (y > hsize) y -= size;
        while (y < -hsize) y += size;
        return new Position(x, y);
    }

    @Override
    public Position absolute(Position base, Position rel) {
        double x = base.getX() + rel.getX();
        double y = base.getY() + rel.getY();
        while (x >= size) x -= size;
        while (x < 0.0) x += size;
        while (y >= size) y -= size;
        while (y < 0.0) y += size;
        return new Position(x, y);
    }

    @Override
    public Shape getShape(Position pos, double factor) {
        double s = size * factor;
        double hs = s / 2.0;
        return new Rectangle2D.Double(-hs, -hs, s, s);
    }
}
