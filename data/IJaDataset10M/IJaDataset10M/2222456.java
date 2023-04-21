package mobility;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import org.jgraph.graph.CellView;
import org.jgraph.graph.VertexView;
import com.jgraph.example.GraphEdX;

/**
 * 
 * @author Jaime Jesús Biosca Alarcón
 *
 */
public class UserModel extends MobilityModel {

    private double max;

    private double dx;

    private double dy;

    private double r;

    private int i;

    private int inc;

    private int step;

    private static final String MOVING_CELL = "        Cell 0        ";

    Hashtable<String, Vector<Point>> paths;

    private Point nextMove = new Point();

    public UserModel(GraphEdX graphEd) {
        super(graphEd);
        paths = new Hashtable<String, Vector<Point>>();
        i = 0;
        inc = 10;
        step = 0;
        r = 15;
    }

    /**
	 * Set the initial positions
	 */
    public void setInitialPositions() {
        System.out.println("setInitialPosition");
        CellView[] cellView;
        cellView = graphEd.getGraph().getGraphLayoutCache().getAllViews();
        for (int i = 0; i < cellView.length; i++) {
            if (cellView[i] instanceof VertexView) {
                Boolean bMoves = new Boolean(true);
                if (bMoves.equals(cellView[i].getAllAttributes().get("MOVES"))) {
                    translationMap.put(cellView[i], new Point(0, 0));
                }
            }
        }
    }

    /**
	 * Calculate the next VALID position of nodes taking into account whether partitions are allowed or not and the
	 * visibility distance between nodes  
	 * @return
	 */
    public HashMap<CellView, Point> move() {
        Iterator it = translationMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<CellView, Point> e = (Map.Entry<CellView, Point>) it.next();
            moveMeeting(e.getKey());
            if (nextMove == null) return null;
            e.setValue(nextMove);
        }
        return translationMap;
    }

    private void moveMeeting(CellView cellView) {
        double dx;
        double dy;
        moveUser(cellView);
    }

    private void moveUser(CellView cellView) {
        Vector<Point> points = new Vector<Point>();
        paths = graphEd.getPaths();
        double incX = 0, incY = 0;
        if (cellView.getAllAttributes().get("NAME") != null) {
            points = paths.get(cellView.getAllAttributes().get("NAME"));
            if ((points != null) && (points.size() > 0)) {
                System.out.println("SIZE POINTS (mobility): " + points.size());
                Iterator it = points.iterator();
                if (it.hasNext()) {
                    Point p = (Point) it.next();
                    graphEd.setMovingNode(new Point((int) cellView.getBounds().getX(), (int) cellView.getBounds().getY()));
                    if (((p.getX() - cellView.getBounds().getX()) < 3) && ((p.getY() - cellView.getBounds().getY()) < 3)) {
                        points.remove(p);
                        paths.remove((String) cellView.getAllAttributes().get("NAME"));
                        paths.put((String) cellView.getAllAttributes().get("NAME"), points);
                        graphEd.repaint();
                    } else {
                        System.out.println("NAME (mobility): " + cellView.getAllAttributes().get("NAME"));
                        incX = p.getX() - cellView.getBounds().getX();
                        incY = p.getY() - cellView.getBounds().getY();
                        System.out.println("INICIAL" + incX + " " + incY);
                        if ((incX != 0) && (incY != 0)) {
                            if (incX < incY) {
                                incY = Math.floor(incY / Math.abs(incX));
                                incX = Math.floor(incX / Math.abs(incX));
                            } else if (incX > incY) {
                                incX = Math.floor(incX / Math.abs(incY));
                                incY = Math.floor(incY / Math.abs(incY));
                            } else {
                                incY = Math.floor(incY / Math.abs(incY));
                                incX = Math.floor(incX / Math.abs(incX));
                            }
                        } else {
                            if (incX == 0) {
                                incY = 1;
                            } else if (incY == 0) {
                                incX = 1;
                            }
                        }
                        if (incX > 4) incX = 4; else if (incX < -4) incX = -4;
                        if (incY > 4) incY = 4; else if (incY < -4) incY = -4;
                        System.out.println("INCX, Y " + incX + " " + incY);
                    }
                }
            }
        }
        nextMove.setLocation(incX, incY);
    }

    private void moveCircle() {
        max = 80;
        if (i < max) {
            i++;
            dx = r * Math.cos(2.0 * Math.PI * i / max - Math.PI / 2.0);
            dy = r * Math.sin(2.0 * Math.PI * i / max - Math.PI / 2.0);
            nextMove.setLocation(dx, dy);
        } else {
            i = 0;
            step++;
        }
    }

    private void moveDown() {
        max = 48;
        dx = 0;
        dy = inc;
        if (i < max + 1) {
            i++;
            nextMove.setLocation(dx, dy);
        } else {
            i = 0;
            step++;
        }
    }

    private void moveRight() {
        max = 30;
        dx = inc;
        dy = 0;
        if (i < max + 1) {
            i++;
            nextMove.setLocation(dx, dy);
        } else {
            i = 0;
            step++;
        }
    }

    private void moveLeft() {
        max = 30;
        dx = -inc;
        dy = 0;
        if (i < max + 1) {
            i++;
            nextMove.setLocation(dx, dy);
        } else {
            i = 0;
            step++;
        }
    }
}
