package uebung14.as.aufgabe02;

import java.awt.Point;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;

/**
 * Base class for all path finders.
 * 
 * @author thabo
 */
public abstract class PathFinder extends Observable {

    protected Map map;

    protected Vector<Point> path;

    /**
   * Pathfinder for the given map.
   */
    public PathFinder(Map m) {
        map = m;
        path = new Vector<Point>();
    }

    /**
   * Find the path running from (x0,y0) to (x1,y1). Call notifyObservers at
   * least at the end for the GUI.
   * 
   * @param x0
   *          Start-x.
   * @param y0
   *          Start-y.
   * @param x1
   *          End-x.
   * @param y1
   *          End-y.
   * @return The path.
   */
    public abstract Vector<Point> findPath(int x0, int y0, int x1, int y1);

    public Vector<Point> getPath() {
        return path;
    }

    /**
   * Calculate the total weight of the provided path.
   * 
   * @param p
   *          The path.
   * @return Total weight.
   */
    public double calcTotalWeight(Vector<Point> p) {
        double w = 0;
        if (p.size() > 0) {
            Iterator<Point> it = p.iterator();
            Point pt1, pt2;
            pt2 = (Point) it.next();
            while (it.hasNext()) {
                pt1 = pt2;
                pt2 = (Point) it.next();
                w += map.calcWeight(pt1.x, pt1.y, pt1.x, pt2.x);
            }
        }
        return w;
    }
}
