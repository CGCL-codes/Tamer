package br.ufrgs.f180.elements;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import br.ufrgs.f180.math.Line;
import br.ufrgs.f180.math.Point;
import com.cloudgarden.resource.SWTResourceManager;

public class Wall implements VisualElement {

    private static Logger logger = Logger.getLogger(VisualElement.class);

    public enum CollisionSide {

        NORMAL, REVERSE, BOTH
    }

    ;

    private GameField field;

    private final double y0;

    private final double y1;

    private final double x0;

    private final double x1;

    private final CollisionSide collisionSide;

    public Wall(double x0, double y0, double x1, double y1, CollisionSide collisionSide) {
        this.y0 = y0;
        this.y1 = y1;
        this.x0 = x0;
        this.x1 = x1;
        this.collisionSide = collisionSide;
    }

    @Override
    public void draw(GC gc) {
        Color old = gc.getForeground();
        Color c = SWTResourceManager.getColor(250, 250, 0);
        gc.setForeground(c);
        gc.drawLine(realx(x0), realy(y0), realx(x1), realy(y1));
        gc.setForeground(old);
    }

    @Override
    public int scalex(double x) {
        return field.scalex(x);
    }

    @Override
    public int scaley(double y) {
        return field.scaley(y);
    }

    @Override
    public int realx(double x) {
        return field.realx(x);
    }

    @Override
    public int realy(double y) {
        return field.realy(y);
    }

    public double getY0() {
        return y0;
    }

    public double getY1() {
        return y1;
    }

    public double getX0() {
        return x0;
    }

    public double getX1() {
        return x1;
    }

    public GameField getField() {
        return field;
    }

    public void setField(GameField field) {
        this.field = field;
    }

    public CollisionSide getCollisionSide() {
        return collisionSide;
    }

    /**
	 * Calculates the 2D distance between a point and a line
	 * @param x
	 * @param y
	 * @return
	 */
    public double distanceFrom(Point point) {
        Line l = new Line(new Point(x0, y0), new Point(x1, y1));
        return l.distanceFrom(point);
    }

    public static void main(String[] args) {
        Wall n = new Wall(0, 10, 100, 10, CollisionSide.BOTH);
        Point pt = new Point(5.77, 0);
        Point p = n.perpendicularProjection(pt);
        logger.debug("Distance: " + n.distanceFrom(pt));
        logger.debug("Projection: x = " + p.getX());
        logger.debug("Projection: y = " + p.getY());
    }

    public Point perpendicularProjection(Point pt) {
        Line l = new Line(new Point(x0, y0), new Point(x1, y1));
        return l.perpendicularProjection(pt);
    }
}
