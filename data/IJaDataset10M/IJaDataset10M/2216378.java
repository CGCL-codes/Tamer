package neon.maps;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class MapUtils {

    /**
	 * Returns a rectangle with the given min/max width and height.
	 * 
	 * @param minW
	 * @param maxW
	 * @param minH
	 * @param maxH
	 * @return	a random Rectangle with its origin a (0,0)
	 */
    public static Rectangle randomRectangle(int minW, int maxW, int minH, int maxH) {
        int w = random(minW, maxW);
        int h = random(minH, maxH);
        return new Rectangle(w, h);
    }

    /**
	 * Returns a square with the given min/max side length.
	 * 
	 * @param minW
	 * @param maxW
	 * @return	a square with its origin at (0,0)
	 */
    public static Rectangle randomSquare(int minW, int maxW) {
        int w = random(minW, maxW);
        return new Rectangle(w, w);
    }

    /**
	 * Returns a rectangle with the given min/max dimensions and a maximum width/height or height/width ratio.
	 * 
	 * @param minW
	 * @param maxW
	 * @param minH
	 * @param maxH
	 * @param ratio
	 * @return	a random Rectangle with its origin at (0,0)
	 */
    public static Rectangle randomRectangle(int minW, int maxW, int minH, int maxH, double ratio) {
        int w = random(minW, maxW);
        int hMin = Math.max(minH, (int) (w / ratio));
        int hMax = Math.min(maxH, (int) (w * ratio));
        int h = random(hMin, hMax);
        return new Rectangle(w, h);
    }

    /**
	 * Generates a random rectangle within the given rectangle.
	 * 
	 * @param minW
	 * @param maxW
	 * @param ratio
	 * @param bounds
	 * @return
	 */
    public static Rectangle randomRectangle(int minW, Rectangle bounds) {
        int w = random(minW, bounds.width - 1);
        int h = random(minW, bounds.height - 1);
        Rectangle rec = new Rectangle(w, h);
        while (!bounds.contains(rec)) {
            rec.x = random(bounds.x, bounds.x + bounds.width - w);
            rec.y = random(bounds.y, bounds.y + bounds.height - h);
        }
        return rec;
    }

    /**
	 * @param min
	 * @param max
	 * @return	a random int between min and max (min and max included)
	 */
    public static int random(int min, int max) {
        return (int) (min + (max - min + 1) * Math.random());
    }

    /**
	 * Returns a random polygon with approximately the given number of vertices. The polygon is 
	 * not guaranteed to be convex, and may intersect itself.
	 * 
	 * @param r			a rectangle
	 * @param corners	the number of vertices
	 * @return	a polygon that is bounded by the given rectangle
	 */
    public static Polygon randomPolygon(Rectangle r, int corners) {
        Rectangle up = new Rectangle(r.x + r.width / 4, r.y, r.width / 2, r.height / 4);
        Rectangle right = new Rectangle(r.x + 3 * r.width / 4, r.y + r.height / 4, r.width / 4, r.height / 2);
        Rectangle down = new Rectangle(r.x + r.width / 4, r.y + 3 * r.height / 4, r.width / 2, r.height / 4);
        Rectangle left = new Rectangle(r.x, r.y + r.height / 4, r.width / 4, r.height / 2);
        int numPoints = corners / 4;
        int[] xPoints = new int[4 * numPoints];
        int[] yPoints = new int[4 * numPoints];
        for (int i = 0; i < numPoints; i++) {
            Point p = randomPoint(up);
            xPoints[i] = p.x;
            yPoints[i] = p.y;
        }
        for (int i = 0; i < numPoints; i++) {
            Point p = randomPoint(right);
            xPoints[numPoints + i] = p.x;
            yPoints[numPoints + i] = p.y;
        }
        for (int i = 0; i < numPoints; i++) {
            Point p = randomPoint(down);
            xPoints[2 * numPoints + i] = p.x;
            yPoints[2 * numPoints + i] = p.y;
        }
        for (int i = 0; i < numPoints; i++) {
            Point p = randomPoint(left);
            xPoints[3 * numPoints + i] = p.x;
            yPoints[3 * numPoints + i] = p.y;
        }
        return new Polygon(xPoints, yPoints, 4 * numPoints);
    }

    /**
	 * @param r
	 * @return	a random point in the given rectangle
	 */
    public static Point randomPoint(Rectangle r) {
        return new Point(random(r.x, r.x + r.width), random(r.y, r.y + r.height));
    }

    /**
	 * Returns a ribbon of width one, running from one side of a rectangle to the opposite one.
	 * 
	 * @param r
	 * @param horizontal
	 * @return	an array of points contained in the ribbon
	 */
    public static Point[] randomRibbon(Rectangle r, boolean horizontal) {
        Point ribbon[];
        if (horizontal) {
            ribbon = new Point[r.width];
            int y = random(r.y, r.y + r.height);
            ribbon[0] = new Point(r.x, y);
            for (int i = 1; i < r.width; i++) {
                y = random(Math.max(r.y, y - 1), Math.min(r.y + r.height, y + 1));
                ribbon[i] = new Point(r.x + i, y);
            }
        } else {
            ribbon = new Point[r.height];
            int x = random(r.x + r.width, r.y);
            ribbon[0] = new Point(x, r.y);
            for (int i = 0; i < r.height; i++) {
                x = random(Math.max(r.x, x - 1), Math.min(r.x + r.width, x + 1));
                ribbon[i] = new Point(x, r.y + i);
            }
        }
        return ribbon;
    }

    public static int amount(boolean[] array, boolean ref) {
        int count = 0;
        for (boolean o : array) {
            if (ref == o) {
                count++;
            }
        }
        return count;
    }

    public static int average(int x1, int x2) {
        return (x1 + x2) / 2;
    }
}
