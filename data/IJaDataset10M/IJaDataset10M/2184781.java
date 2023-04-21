package de.syfy.project.engine.utils;

import java.awt.geom.Point2D;

/**
 *
 * @author Timo
 */
public class Polygon {

    private Point2D.Float[] edges;

    private Point2D.Float centroid;

    private float a;

    public Polygon(Point2D.Float[] edges) {
        this.edges = edges;
        calculateArea();
        calculateMid();
    }

    private void calculateArea() {
        float area = 0;
        for (int i = 0; i < edges.length - 1; i++) {
            area += ((edges[i].x * edges[i + 1].y) - (edges[i + 1].x * edges[i].y));
        }
        area /= 2;
        a = Math.abs(area);
    }

    private void calculateMid() {
        float x = 0;
        for (int i = 0; i < edges.length - 1; i++) {
            x += ((edges[i].x + edges[i + 1].x) * (edges[i].x * edges[i + 1].y - edges[i + 1].x * edges[i].y));
        }
        x = Math.abs(x * (1 / (6 * a)));
        float y = 0;
        for (int i = 0; i < edges.length - 1; i++) {
            y += ((edges[i].y + edges[i + 1].y) * (edges[i].x * edges[i + 1].y - edges[i + 1].x * edges[i].y));
        }
        y = Math.abs(y * (1 / (6 * a)));
        centroid = new Point2D.Float(x, y);
    }

    /**
     * Gibt zurück ob der gegebene Punkt innerhalb des Polygons liegt.
     * 
     * http://paulbourke.net/geometry/insidepoly/
     * 
     * @param p
     * @return 
     */
    public boolean isInside(Point2D.Float p) {
        int counter = 0;
        float xinters;
        Point2D.Float p1, p2;
        p1 = edges[0];
        for (int i = 1; i <= edges.length; i++) {
            p2 = edges[i % edges.length];
            if (p.y > Math.min(p1.y, p2.y)) {
                if (p.y <= Math.max(p1.y, p2.y)) {
                    if (p.x <= Math.max(p1.x, p2.x)) {
                        if (p1.y != p2.y) {
                            xinters = (p.y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x;
                            if (p1.x == p2.x || p.x <= xinters) {
                                counter++;
                            }
                        }
                    }
                }
            }
            p1 = p2;
        }
        if (counter % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Gibt den Flächeninhalt des Polygons zurück.
     * 
     * @return 
     */
    public float getArea() {
        return a;
    }

    /**
     * Gibt den Massenschwerpunkt des Polygons zurück.
     * 
     * @return 
     */
    public Point2D.Float getMid() {
        return centroid;
    }
}
