package geom;

import java.awt.Graphics2D;
import world.modifier.Drawable;

/**
 * defines a circle
 * @author Jack
 *
 */
public class Circle implements Boundable, Drawable {

    public double[] l;

    public double radius;

    /**
	 * creates a new circle
	 * @param l the location of the center of the circle
	 * @param radius
	 */
    public Circle(double[] l, double radius) {
        this.l = l;
        this.radius = radius;
    }

    /**
	 * tests for intersection
	 * @param c
	 * @return returns true if this circle intersects the passed circle
	 */
    public boolean intersects(Circle c) {
        return getDistance(c) < c.radius + radius;
    }

    /**
	 * determines the distance between this circle and the passed circle
	 * @param c
	 * @return returns the distance between this circle and the passed circle
	 */
    public double getDistance(Circle c) {
        double x = c.l[0] - l[0];
        double y = c.l[1] - l[1];
        return Math.sqrt(x * x + y * y);
    }

    public void draw(Graphics2D g) {
        int x = (int) (l[0] - radius);
        int y = (int) (l[1] - radius);
        g.fillOval(x, y, (int) radius * 2, (int) radius * 2);
    }

    public Rectangle getBounds() {
        return new Rectangle(l[0] - radius, l[1] - radius, radius * 2, radius * 2);
    }
}
