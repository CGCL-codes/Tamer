package pathfinding;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.lang.Math;

/**
 * a class that can calculate a path.
 * @author Robin Malmros
 * @author Oscar Almgren
 */
public class PathFinder {

    /**
     * a list used in the algorithm/process of determining the path.
     */
    private ArrayList open = new ArrayList();

    /**
     * a list used in the process/algorithm of determining the path.
     */
    private ArrayList closed = new ArrayList();

    /**
     * the world we are to find a path in.
     * A gridnet with nodes/squares that are either walkable or not walkable.
     */
    private boolean[][] map;

    /**
     * the starting point
     */
    private Point start;

    /**
     * the end point
     */
    private Point end;

    /**
     * the width of the gridnet we are to walk through
     */
    private int width;

    /**
     * the height of the grid net we are to walk in
     */
    private int height;

    /**
     * Constructing a new pathfinder.
     * @param map the gridnet to find a path in
     * @param start starting point
     * @param end goal point
     */
    public PathFinder(boolean[][] map, Point start, Point end) {
        this.map = map;
        this.start = start;
        this.end = end;
        width = this.map.length;
        height = this.map[0].length;
    }

    /**
     * gets the startpoint
     * @return the start point
     */
    public Point getStartPoint() {
        return this.start;
    }

    /**
     * the list that will be returned containing the path
     */
    public LinkedList thePath = new LinkedList();

    /**
     * calculating the heuristic distance between two points, using the "manhattan method" (only 90 degree turns).
     * @return the heuristic distance
     * @param A point
     * @param B point
     */
    public int calculateH(Point A, Point B) {
        return 10 * (Math.abs((A.x - B.x)) + Math.abs((A.y - B.y)));
    }

    /**
     * removes points that are inbetween other points that are on a straight line.
     * @return a list without "inbetween points"
     * @param longPath list of points
     */
    private LinkedList removeInBetween(LinkedList longPath) {
        LinkedList<Point> newPath = new LinkedList();
        Point last = null;
        Point next = null;
        Point current = null;
        for (int i = 0; i < longPath.size(); i++) {
            current = (Point) longPath.get(i);
            if ((i - 1 >= 0) && (i + 1 < longPath.size())) {
                last = (Point) longPath.get(i - 1);
                next = (Point) longPath.get(i + 1);
                if (!isInBetween(last, current, next)) {
                    newPath.add(current);
                }
            } else {
                newPath.add(current);
            }
        }
        return newPath;
    }

    /**
     * checks if a point is inbetween two other points on a straight line.
     * @return true or false
     * @param last point
     * @param current point
     * @param next point
     */
    private boolean isInBetween(Point last, Point current, Point next) {
        boolean answer = false;
        int lx = last.x;
        int nx = next.x;
        int cx = current.x;
        int ly = last.y;
        int ny = next.y;
        int cy = current.y;
        if ((lx < cx) && (ly < cy) && (nx > cx) && (ny > cy)) {
            answer = true;
        }
        if ((lx == cx) && (ly < cy) && (nx == cx) && (ny > cy)) {
            answer = true;
        }
        if ((lx > cx) && (ly < cy) && (nx < cx) && (ny > cy)) {
            answer = true;
        }
        if ((lx > cx) && (ly == cy) && (nx < cx) && (ny == cy)) {
            answer = true;
        }
        if ((lx > cx) && (ly > cy) && (nx < cx) && (ny < cy)) {
            answer = true;
        }
        if ((lx == cx) && (ly > cy) && (nx == cx) && (ny < cy)) {
            answer = true;
        }
        if ((lx < cx) && (ly > cy) && (nx < cx) && (ny > cy)) {
            answer = true;
        }
        if ((lx < cx) && (ly == cy) && (next.x > cx) && (ny == cy)) {
            answer = true;
        }
        return answer;
    }

    private LinkedList pointsToTravelAndRotate(LinkedList<Point> pointPath) {
        LinkedList<Point> commandPath = new LinkedList();
        pointPath.addFirst(new Point(pointPath.get(0).x, pointPath.get(0).y + 1));
        Line2D L1 = new Line2D.Double();
        Line2D L2 = new Line2D.Double();
        int angle;
        for (int i = 0; i < pointPath.size() - 2; i++) {
            L1 = new Line2D.Double(pointPath.get(i).x, pointPath.get(i).y, pointPath.get(i + 1).x, pointPath.get(i + 1).y);
            L2 = new Line2D.Double(pointPath.get(i + 1).x, pointPath.get(i + 1).y, pointPath.get(i + 2).x, pointPath.get(i + 2).y);
            angle = lineAngle(L2) - lineAngle(L1);
            if (angle > 180) angle -= 360;
            if (angle < -180) angle += 360;
            commandPath.add(new Point(0, angle));
            commandPath.add(new Point(1, 10 * (int) Math.hypot((double) (L2.getX2() - L2.getX1()), (double) (L2.getY2() - L2.getY1()))));
        }
        return commandPath;
    }

    private int lineAngle(Line2D l) {
        final int LESS = -1;
        final int SAME = 0;
        final int MORE = 1;
        int x = (int) Math.signum((double) (l.getX2() - l.getX1()));
        int y = (int) Math.signum((double) (l.getY2() - l.getY1()));
        switch(x) {
            case LESS:
                {
                    switch(y) {
                        case LESS:
                            return 45;
                        case SAME:
                            return 90;
                        case MORE:
                            return 135;
                    }
                }
                break;
            case SAME:
                {
                    switch(y) {
                        case LESS:
                            return 0;
                        case MORE:
                            return 180;
                    }
                }
                break;
            case MORE:
                {
                    switch(y) {
                        case LESS:
                            return -45;
                        case SAME:
                            return -90;
                        case MORE:
                            return -135;
                    }
                }
                break;
        }
        return -1;
    }

    /**
     * calculates a path,using the A* algorithm.
     * @return a list containg points guiding us through the path
     */
    public LinkedList calculatePath() {
        int startH = calculateH(this.start, this.end);
        int startG = 0;
        int startF = startH + startG;
        Square startingSquare = new Square(startF, startG, startH, this.start, this.start);
        open.add(startingSquare);
        while (open.size() > 0 && !IsThisPointInHere(this.end, closed)) {
            Square active = getLowestFSquare(open);
            open.remove(active);
            closed.add(active);
            Point left = new Point(active.currentPos.x - 1, active.currentPos.y);
            Point topLeft = new Point(active.currentPos.x - 1, active.currentPos.y - 1);
            Point top = new Point(active.currentPos.x, active.currentPos.y - 1);
            Point topRight = new Point(active.currentPos.x + 1, active.currentPos.y - 1);
            Point right = new Point(active.currentPos.x + 1, active.currentPos.y);
            Point lowRight = new Point(active.currentPos.x + 1, active.currentPos.y + 1);
            Point low = new Point(active.currentPos.x, active.currentPos.y + 1);
            Point lowLeft = new Point(active.currentPos.x - 1, active.currentPos.y + 1);
            checkAdjecent(active, left, 10);
            checkAdjecent(active, topLeft, 14);
            checkAdjecent(active, top, 10);
            checkAdjecent(active, topRight, 14);
            checkAdjecent(active, right, 10);
            checkAdjecent(active, lowRight, 14);
            checkAdjecent(active, low, 10);
            checkAdjecent(active, lowLeft, 14);
        }
        thePath.addFirst(this.end);
        Point pathPoint = getSquareAtThisPoint(this.end, closed).parent;
        while (pathPoint != start) {
            thePath.addFirst(pathPoint);
            pathPoint = getSquareAtThisPoint(pathPoint, closed).parent;
        }
        System.out.println("This is our path: " + removeInBetween(thePath));
        System.out.println("These are our commands: " + pointsToTravelAndRotate(removeInBetween(thePath)));
        thePath = removeInBetween(thePath);
        return pointsToTravelAndRotate(thePath);
    }

    /**
     * Picks out the Square that has the lowest F-value out of a list of Squares.
     * @param L the list of squares
     * @return the square with lowest f-value
     */
    private Square getLowestFSquare(ArrayList L) {
        Iterator it = L.iterator();
        Square low = new Square(-1, 0, 0, new Point(0, 0), new Point(0, 0));
        while (it.hasNext()) {
            Square tmp = (Square) it.next();
            if (low.F < 0 || tmp.F <= low.F) {
                low = tmp;
            }
        }
        return low;
    }

    /**
     * Picks out the Square that is located at a specific point <CODE>thisOne</CODE>, out of a list of squares.
     * if no such square exists, null is returned.
     * @param thisOne the Point we want to find a matching square to.
     * @param L the list of squares
     * @return the square matching the point, or null
     */
    private Square getSquareAtThisPoint(Point thisOne, ArrayList L) {
        Iterator it = L.iterator();
        Square answ = null;
        while (it.hasNext()) {
            Square tmp = (Square) it.next();
            if (tmp.currentPos.equals(thisOne)) {
                answ = tmp;
            }
        }
        return answ;
    }

    /**
     * checks a list if there is a square matching a specific point in that list.
     * @param thisOne the point we want to find a matching square for.
     * @param L the list of squares to look in
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    private boolean IsThisPointInHere(Point thisOne, ArrayList L) {
        boolean theTruth = false;
        Iterator it = L.iterator();
        while (it.hasNext()) {
            Square tmp = (Square) it.next();
            if (tmp.currentPos.equals(thisOne)) {
                theTruth = true;
            }
        }
        return theTruth;
    }

    /**
     * takes a look at a specific point next to the <CODE>S</CODE> square.
     * A new square is created at that point, and depending on some rules of the
     * A*-algorithm, some actions are taken. I.e. if that square isn't already
     * in the open list, put it there!
     * @param S the active Square
     * @param checking the adjecent point we're intrested in
     * @param walkCost the cost of walking in the direction towards <CODE>checking</CODE>
     */
    private void checkAdjecent(Square S, Point checking, int walkCost) {
        if (checking.x >= 0 && checking.y >= 0 && checking.x < width && checking.y < height && !map[checking.x][checking.y] && !IsThisPointInHere(checking, closed)) {
            int H = calculateH(checking, end);
            int G = S.G + walkCost;
            int F = H + G;
            if (!IsThisPointInHere(checking, open)) {
                Square adjecent = new Square(F, G, H, checking, S.currentPos);
                open.add(adjecent);
            } else {
                Square inOpen = getSquareAtThisPoint(checking, open);
                if (inOpen != null) {
                    if (inOpen.G > (G + walkCost)) {
                        open.remove(inOpen);
                        Square updated = new Square(F, G, H, checking, S.currentPos);
                        open.add(updated);
                    }
                }
            }
        }
    }
}
