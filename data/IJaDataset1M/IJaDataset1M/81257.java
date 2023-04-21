package world.roads;

import geometry.AdjacencyMarker;
import geometry.Point;
import geometry.QuadArea;
import java.awt.Color;

public class Road {

    private boolean connected = false;

    private RoadNode n1, n2;

    private int width = 10;

    public Road(RoadNode n1, RoadNode n2) {
        this.n1 = n1;
        this.n2 = n2;
        n1.addRoad(this);
        n2.addRoad(this);
    }

    public RoadNode getStart() {
        return n1;
    }

    public RoadNode getEnd() {
        return n2;
    }

    public static Color getDrawColor() {
        return Color.DARK_GRAY;
    }

    public static Color getFillColor() {
        return Color.DARK_GRAY;
    }

    public static QuadArea getPolygon(Point p1, Point p2, int streetWidth) {
        Point p3 = new Point(p2);
        Point p4 = new Point(p1);
        double dirRight = Math.atan2(p2.y - p1.y, p2.x - p1.x) - Math.PI * 0.5;
        int dx = (int) (Math.cos(dirRight) * streetWidth / 2);
        int dy = (int) (Math.sin(dirRight) * streetWidth / 2);
        p1.translate(dx, dy);
        p2.translate(dx, dy);
        p3.translate(-dx, -dy);
        p4.translate(-dx, -dy);
        return new QuadArea(p1, p2, p3, p4);
    }

    public QuadArea getPolygon() {
        return getPolygon(new Point(n1.getPoint()), new Point(n2.getPoint()), width);
    }

    public static QuadArea getRightSide(Point refp1, Point refp2, int width) {
        Point p1 = new Point(refp1);
        Point p2 = new Point(refp2);
        Point p3 = new Point(p2);
        Point p4 = new Point(p1);
        double dirRight = Math.atan2(p2.y - p1.y, p2.x - p1.x) - Math.PI * 0.5;
        int dx = (int) (Math.cos(dirRight) * width);
        int dy = (int) (Math.sin(dirRight) * width);
        p1.translate(dx, dy);
        p2.translate(dx, dy);
        return new QuadArea(p1, p2, p3, p4);
    }

    public static QuadArea getLeftSide(Point p1, Point p2, int width) {
        return getRightSide(p2, p1, width);
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public String toString() {
        if (isConnected()) return "Connected road between " + n1 + " <-> " + n2; else return "Unconnected road between " + n1 + " <-> " + n2;
    }

    public AdjacencyMarker getNewsideMarkers() {
        return new AdjacencyMarker(n1.getPoint(), n2.getPoint());
    }

    public int getWidth() {
        return width;
    }
}
