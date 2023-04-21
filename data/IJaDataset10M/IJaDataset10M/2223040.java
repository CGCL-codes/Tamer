package main;

import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;
import java.util.*;

/**
 * Represents a Tile.
 *
 * @author Adam Koski
 */
public class Tile implements Serializable {

    private GeneralPath path = new GeneralPath();

    private Vector<Point2D> points = new Vector<Point2D>();

    private double x, y, a;

    private Vector<Double> angles;

    private String name;

    private Color color;

    /**
     * Creates a generic tile with the privided atributes.
     *
     * @param a Vector of Double angles.
     * @param c Tile Color.
     * @param n Tile Name.
     */
    public Tile(Vector<Double> a, Color c, String n) {
        angles = a;
        color = c;
        name = n;
    }

    /**
     * Creates a tile at (_x, _y) with the privided
     * tiles atributes.
     *
     * @param t Tile to copy.
     * @param _x X coordinate.
     * @param _y Y coordinate.
     */
    public Tile(Tile t, double _x, double _y) {
        angles = t.getAngles();
        color = t.getColor();
        name = t.getName();
        x = _x;
        y = _y;
        rotate(0);
    }

    /**
     * Returns the GeneralPath of this tile.
     *
     * @return General Path
     */
    public GeneralPath getGeneralPath() {
        return (GeneralPath) path.clone();
    }

    /**
     * Gets the tile's color.
     *
     * @return Color of tile.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the current vertex points of the tile.
     *
     * @return Vector of points.
     */
    public Vector<Point2D> getPoints() {
        return (Vector<Point2D>) points.clone();
    }

    /**
     * Gets the angles between verticies.
     *
     * @return Vector of angles.
     */
    public Vector<Double> getAngles() {
        return angles;
    }

    /**
     * Gets the name of the tile.
     *
     * @return String name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the x coordinate.
     *
     * @return X position.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return Y position.
     */
    public double getY() {
        return y;
    }

    /**
     * Translates this tile by the given vector.
     *
     * @param dx Change in x.
     * @param dy Change in y.
     */
    public void translate(double dx, double dy) {
        x += dx;
        y += dy;
        update();
    }

    /**
     * Scales the tile by the given ratio.
     *
     * @param ratio Double scale factor.
     */
    public void scale(double ratio) {
        x = x * ratio;
        y = y * ratio;
        update();
    }

    /**
     * Rotates the tile by the given angle.
     *
     * @param angle New angle to rotate.
     */
    public void rotate(double angle) {
        a = Math.round((5 * angle) / Math.PI) * Math.PI / 5;
        update();
    }

    private void updatePoints() {
        points = new Vector<Point2D>();
        points.addElement(new Point2D.Double(x, y));
        for (int i = 0; i < angles.size(); i++) {
            points.addElement(new Point2D.Double(points.elementAt(i).getX() + (Preferences.getSideLength() * Math.cos(angles.elementAt(i) + a)), points.elementAt(i).getY() + (Preferences.getSideLength() * Math.sin(angles.elementAt(i) + a))));
        }
    }

    private void updateGeneralPath() {
        path = new GeneralPath();
        path.moveTo(points.elementAt(0).getX(), points.elementAt(0).getY());
        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.elementAt(i).getX(), points.elementAt(i).getY());
        }
        path.closePath();
    }

    private void update() {
        updatePoints();
        updateGeneralPath();
    }
}
