package computational.geometry.convexhull;

import computational.*;
import computational.geometry.*;
import java.util.*;

public abstract class JarvisMarchEvent implements Event {

    public abstract String getType();

    public abstract String getTest();

    /************************************************************
	 *                     BottomFound
	 ************************************************************/
    public static class BottomFound extends JarvisMarchEvent {

        private Point bottom;

        private List points;

        public BottomFound(Point bottom, List points) {
            this.bottom = bottom;
            this.points = new ArrayList(points);
        }

        public Point getBottom() {
            return bottom;
        }

        public List getPoints() {
            return points;
        }

        public String getType() {
            return "Bottom vertex found: " + bottom;
        }

        public String getTest() {
            return null;
        }

        public String toString() {
            return getType();
        }
    }

    /************************************************************
	 *                      VertexFound
	 ************************************************************/
    public static class VertexFound extends JarvisMarchEvent {

        private List vertices;

        public VertexFound(List vertices) {
            this.vertices = new ArrayList(vertices);
        }

        public Point getAdded() {
            return (Point) vertices.get(vertices.size() - 1);
        }

        public List getVertices() {
            return vertices;
        }

        public String getType() {
            return "Vertex Found: " + vertices.get(vertices.size() - 1);
        }

        public String getTest() {
            return null;
        }

        public String toString() {
            return getType();
        }
    }

    /************************************************************
	 *                      PointsCompared
	 ************************************************************/
    public static class PointsCompared extends JarvisMarchEvent {

        private Turn turn;

        private Point chosen;

        public PointsCompared(Turn turn, Point chosen) {
            this.turn = turn;
            this.chosen = (Point) chosen.clone();
        }

        public Turn getTurn() {
            return turn;
        }

        public Point getChosenPoint() {
            return chosen;
        }

        public String getType() {
            return "Points compared" + " [" + turn.getPoints().get(1) + "," + turn.getPoints().get(2) + "]" + " (chosen " + chosen + ")";
        }

        public String getTest() {
            return turn.toString();
        }

        public String toString() {
            return getType();
        }
    }

    /************************************************************
	 *                    ConvexHullFound
	 ************************************************************/
    public static class ConvexHullFound extends JarvisMarchEvent {

        private Polygon ch;

        public ConvexHullFound(Polygon ch) {
            this.ch = (Polygon) ch.clone();
        }

        public Polygon getConvexHull() {
            return ch;
        }

        public String getType() {
            return "Convex Hull found.";
        }

        public String getTest() {
            return null;
        }

        public String toString() {
            return getType();
        }
    }
}
