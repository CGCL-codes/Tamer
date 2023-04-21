package editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author Martin Demin, Eduard Benes
 */
public final class ObjectPolygon implements Object {

    private LinkedList<Point2D> list;

    private boolean selected;

    int draggedPoint;

    private float scaleX;

    private float scaleY;

    private ObjectInfo info;

    private boolean strict;

    /** Creates a new instance of ObjectPolygon */
    public ObjectPolygon() {
        list = new LinkedList<Point2D>();
        selected = false;
        scaleX = 1;
        scaleY = 1;
        draggedPoint = -1;
        info = null;
        strict = false;
    }

    /**
     * Do strict checking ? If true, the lines cannot intersect
     * @param b true to set strict checking
     */
    public void setStrict(boolean b) {
        strict = b;
    }

    /**
     * Add point to the object, coordinates are in meters
     */
    public void addPoint(Point2D p) {
        list.add(p);
        if (intersects()) list.removeLast();
    }

    /**
     * Redraw this object
     */
    public void draw(Graphics g) {
        Point2D first, prev, cur;
        Iterator<Point2D> i = list.iterator();
        if (list.isEmpty()) return;
        prev = first = i.next();
        if (selected) g.setColor(Color.RED); else g.setColor(Color.WHITE);
        g.drawRect((int) (prev.getX() * scaleX) - 3, (int) (prev.getY() * scaleY) - 3, 6, 6);
        while (i.hasNext()) {
            cur = i.next();
            g.drawLine((int) (prev.getX() * scaleX), (int) (prev.getY() * scaleY), (int) (cur.getX() * scaleX), (int) (cur.getY() * scaleY));
            g.drawRect((int) (cur.getX() * scaleX) - 3, (int) (cur.getY() * scaleY) - 3, 6, 6);
            prev = cur;
        }
        if (prev != first) {
            g.drawLine((int) (prev.getX() * scaleX), (int) (prev.getY() * scaleY), (int) (first.getX() * scaleX), (int) (first.getY() * scaleY));
        }
    }

    /**
     * Get the type of the object
     */
    public int getType() {
        return 0;
    }

    /**
     * Set scaling of the object pixel -> meters transformation
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    /**
     * Unselect the current object
     */
    public void unselect() {
        selected = false;
    }

    /**
     * If the given point is on the object, the object will be selected
     * @return True if the object has been selected
     */
    public boolean doSelected(int x, int y) {
        Iterator<Point2D> i = list.iterator();
        int count = 0;
        double d = 0.0;
        Point2D p;
        while (i.hasNext()) {
            p = i.next();
            d = p.distance(x / scaleX, y / scaleY);
            if (d <= 3) {
                selected = true;
                draggedPoint = count;
                return true;
            }
            count++;
        }
        draggedPoint = -1;
        return false;
    }

    /**
     * 
     * @return Returns true if this object is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Drag the nearby point
     */
    public void doDragged(int x, int y) {
        Point2D p;
        double d;
        if (draggedPoint == -1) {
            Iterator<Point2D> i = list.iterator();
            int count = 0;
            while (i.hasNext()) {
                p = i.next();
                d = p.distance(x / scaleX, y / scaleY);
                if (d <= 3) {
                    draggedPoint = count;
                }
                count++;
            }
        }
        if (draggedPoint != -1) {
            Point2D sav = (Point2D) list.get(draggedPoint).clone();
            list.get(draggedPoint).setLocation(x / scaleX, y / scaleY);
            if (intersects()) {
                list.get(draggedPoint).setLocation(sav);
            }
        }
    }

    private boolean intersects() {
        if (!strict) return false;
        Line2D line1 = new Line2D.Double();
        Line2D line2 = new Line2D.Double();
        Point2D prev1 = list.getLast();
        Point2D prev2 = null;
        for (Point2D elem1 : list) {
            line1.setLine(prev1, elem1);
            prev2 = list.getLast();
            for (Point2D elem2 : list) {
                line2.setLine(prev2, elem2);
                if (prev1 != prev2 && prev1 != elem2 && elem1 != prev2 && elem1 != elem2 && line1.intersectsLine(line2)) return true;
                prev2 = elem2;
            }
            prev1 = elem1;
        }
        return false;
    }

    /**
     * Called when the mouse is released and this object was selected
     */
    public void doReleased() {
        draggedPoint = -1;
    }

    /**
     * Adds point into the object
     */
    public boolean doAddPoint(int x, int y) {
        Point2D first, prev, cur;
        Line2D line = new Line2D.Double();
        double d, d1, d2;
        int count = 0;
        Iterator<Point2D> i = list.iterator();
        if (list.isEmpty()) return false;
        prev = first = i.next();
        while (i.hasNext()) {
            cur = i.next();
            line.setLine(prev.getX(), prev.getY(), cur.getX(), cur.getY());
            d = line.ptSegDist(x / scaleX, y / scaleY);
            d1 = prev.distance(x / scaleX, y / scaleY);
            d2 = cur.distance(x / scaleX, y / scaleY);
            if (d <= 3 && d1 > 3 && d2 > 3) {
                list.add(count + 1, new Point2D.Double(x / scaleX, y / scaleY));
                return true;
            }
            prev = cur;
            count++;
        }
        if (prev != first) {
            line.setLine(prev.getX(), prev.getY(), first.getX(), first.getY());
            d = line.ptSegDist(x / scaleX, y / scaleY);
            d1 = prev.distance(x / scaleX, y / scaleY);
            d2 = first.distance(x / scaleX, y / scaleY);
            if (d <= 3 && d1 > 3 && d2 > 3) {
                list.add(0, new Point2D.Double(x / scaleX, y / scaleY));
                return true;
            }
        }
        return false;
    }

    /**
     * Selected ?
     * @return Returns true if this object is complete and can be added into scene
     */
    public boolean isComplete() {
        if (list.size() > 2) return true;
        return false;
    }

    /**
     * Set this object as selected
     */
    public void select() {
        selected = true;
    }

    /**
     * Adds a point into the object
     * @param i x-axis in pixels of the point to be added
     * @param i0  y-axis in pixels of the point to be added
     */
    public void addPoint(int i, int i0) {
        addPoint(new Point2D.Double(i / scaleX, i0 / scaleY));
    }

    /**
     * Finds out if a point can be added to the object
     * @return true if can add, false otherwise
     */
    public boolean canAdd() {
        return true;
    }

    /**
     * Sets ObjectInfo class
     * @param i ObjectInfo class
     */
    public void setInfo(ObjectInfo i) {
        info = i;
    }

    /**
     * Get info
     * @return Returns reference to ObjectInfo class
     */
    public ObjectInfo getInfo() {
        return info;
    }

    /**
     * Returns String with SDO_GEOM syntax to be inserted into DB
     * @return String with SDO syntax
     */
    public String getGeom() {
        String s = "MDSYS.SDO_GEOMETRY(2003, NULL, NULL, MDSYS.SDO_ELEM_INFO_ARRAY(1,3,1),MDSYS.SDO_ORDINATE_ARRAY(";
        Point2D cur;
        int count = 0;
        Iterator<Point2D> i = list.iterator();
        while (i.hasNext()) {
            cur = i.next();
            if (count != 0) s += ',';
            s += String.valueOf(cur.getX());
            count++;
            s += ',';
            s += String.valueOf(cur.getY());
            count++;
        }
        s += "," + String.valueOf(list.get(0).getX());
        s += "," + String.valueOf(list.get(0).getY());
        s += "))";
        return s;
    }
}
