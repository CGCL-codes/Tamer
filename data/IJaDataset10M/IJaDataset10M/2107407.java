package model.db.data.event;

import java.awt.Point;
import java.util.ArrayList;

public class PolygonLocation extends Location {

    static final long serialVersionUID = 1234567;

    private ArrayList<Point> points;

    public PolygonLocation() {
        points = new ArrayList<Point>();
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }

    public boolean pointExists(Point point) {
        if (this.points.indexOf(point) > -1) {
            return true;
        }
        return false;
    }

    public void removePoint(Point point) {
        if (this.pointExists(point)) {
            this.points.remove(point);
        }
    }

    protected void doDeepClone() {
    }
}
