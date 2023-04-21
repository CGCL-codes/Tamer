package computational.geometry.voronoidiagram;

import java.awt.geom.QuadCurve2D;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;
import computational.LogManager;
import computational.SilentLogManager;
import computational.geometry.BasicTests;
import computational.geometry.Point;
import computational.geometry.Segment;
import computational.geometry.Turn;
import computational.geometry.voronoidiagram.FortuneEvent.CircleEvent;

public class Fortune extends VoronoiDiagram {

    /**
	 * The final diagram to be computed consists
	 * of segments only, without connectivity
	 * or other information
	 */
    private Collection segments;

    /**
	 * The queue where events are placed and retrieved. 
	 * Events are ordered by their position on the y axis
	 */
    private PriorityQueue queue;

    /**
	 * The Beach-Line is just a collection of vertices
	 * defining the parabolic arcs
	 */
    private Vector beachline;

    /**
	 * Current position of the sweep line
	 */
    private double sweep;

    /**
	 * Machine Precision
	 */
    private final double EPSILON = 0.005;

    /************************************************************
	 *                     CONSTRUCTORS
	 ************************************************************/
    public Fortune() {
        this(Collections.EMPTY_LIST);
    }

    public Fortune(List points) {
        this(points, new SilentLogManager());
    }

    public Fortune(List points, LogManager history) {
        super(points, history);
        segments = new Vector();
        beachline = new Vector();
    }

    /**
	 * The Nodes on the beachline
	 */
    private class Node {

        /**
		 * The focus of this parabolic arc.
		 * Note that multiple nodes can share
		 * the same focus. 
		 */
        public Point focus;

        /**
		 * The CircleEvent that will cause the
		 * disappearance of this parabolic arc
		 */
        public FortuneEvent.CircleEvent event;

        /**
		 * A Node implicitly represents the breakpoint
		 * between the same node and the next one on
		 * the beach line. During the construction
		 * of the diagram, a breakpoint can be connected
		 * to another breakpoint or a fully-formed
		 * vertex in the final Voronoi Diagram (ie a Point) 
		 */
        public Object link;

        public Node(Point p) {
            focus = p;
            event = null;
            link = null;
        }

        public double a, b, c;

        public double sweep;
    }

    /************************************************************
	 *                     CONCRETIZATION
	 ************************************************************/
    @SuppressWarnings("unchecked")
    @Override
    public Collection getVoronoiSegments() {
        if (!isUpdated()) update();
        return segments;
    }

    /**
	 * Fortune's Algorithm Main body
	 * @throws Exception 
	 */
    @SuppressWarnings("unchecked")
    public void update() {
        try {
            segments.clear();
            beachline.clear();
            queue = new PriorityQueue<FortuneEvent>();
            sweep = 0;
            Iterator i = getPoints().iterator();
            while (i.hasNext()) {
                Point p = (Point) i.next();
                FortuneEvent fe = new FortuneEvent.SiteEvent(p);
                if (!queue.contains(fe)) queue.add(fe);
            }
            while (!queue.isEmpty()) {
                FortuneEvent current = (FortuneEvent) queue.remove();
                if (sweep < current.getPriority()) sweep = current.getPriority();
                if (current instanceof FortuneEvent.SiteEvent) {
                    handleSiteEvent((FortuneEvent.SiteEvent) current);
                    current.setBeachLine(plotBeachLine());
                    current.setDiagram(plotPartialDiagram());
                    history.log(current);
                } else if (current instanceof FortuneEvent.CircleEvent) {
                    handleCircleEvent((FortuneEvent.CircleEvent) current);
                    current.setBeachLine(plotBeachLine());
                    current.setDiagram(plotPartialDiagram());
                    history.log(current);
                }
            }
            sweep = 2048;
            segments = plotPartialDiagram();
            setUpdated(true);
        } catch (Exception e) {
            setUpdated(false);
            e.printStackTrace();
        }
    }

    /**
	 * Handles Site Events. These require the beachline to
	 * be updated, new links between breakpoints to be 
	 * constructed and new possible circle events to be
	 * found
	 */
    private void handleSiteEvent(FortuneEvent.SiteEvent current) throws Exception {
        int index;
        if (beachline.size() == 0) {
            beachline.add(new Node(current.getPoint()));
            return;
        }
        if (beachline.size() == 1) index = 0; else index = findArcAbovePoint(current.getX(), current.getY());
        beachline.add(index + 1, new Node(current.getPoint()));
        if (((Node) beachline.get(index)).focus.getY() == current.getY()) ((Node) beachline.get(index)).link = computeFullBreakPoint((Node) beachline.get(index), (Node) beachline.get(index + 1)); else {
            beachline.add(index + 2, new Node(((Node) beachline.get(index)).focus));
            ((Node) beachline.get(index + 2)).link = ((Node) beachline.get(index)).link;
            if (((Node) beachline.get(index + 2)).link instanceof Node) ((Node) ((Node) beachline.get(index + 2)).link).link = beachline.get(index + 2);
            ((Node) beachline.get(index)).link = beachline.get(index + 1);
            ((Node) beachline.get(index + 1)).link = beachline.get(index);
            checkPossibleCircles(current, index + 1);
        }
    }

    /**
	 * Find the index of the node on the beachline
	 * that represents the parabolic arc under which
	 * the point defined by coordinates (x, y) lays
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 * @return the index of the corresponding node on the beachline
	 * @throws Exception
	 */
    private int findArcAbovePoint(double x, double y) throws Exception {
        double break_x = Double.NEGATIVE_INFINITY;
        int i;
        for (i = 0; i < beachline.size() - 1; i++) {
            break_x = computeBreakPoint((Node) beachline.get(i), (Node) beachline.get(i + 1));
            if (x < break_x + EPSILON) break;
        }
        return i;
    }

    /**
	 * Handles Circle Events. These require the beachline
	 * to be deprived of an arc, a new vertex to be added to
	 * the diagram, links to be updated and possible circle 
	 * events to be computed.
	 * 
	 * @param current event
	 * @throws Exception
	 */
    private void handleCircleEvent(FortuneEvent.CircleEvent current) throws Exception {
        int index = beachline.indexOf(current.getNode());
        Point convergence;
        double convergence_x = computeBreakPoint((Node) beachline.get(index), (Node) beachline.get(index + 1));
        double convergence_y = ((Node) beachline.get(index)).a * Math.pow(convergence_x, 2) + ((Node) beachline.get(index)).b * convergence_x + ((Node) beachline.get(index)).c;
        convergence = new Point((int) Math.round(convergence_x), (int) Math.round(convergence_y));
        if (((Node) beachline.get(index - 1)).link instanceof Point) segments.add(new Segment((Point) ((Node) beachline.get(index - 1)).link, convergence)); else if (((Node) beachline.get(index - 1)).link instanceof Node) ((Node) ((Node) beachline.get(index - 1)).link).link = convergence;
        ((Node) beachline.get(index - 1)).link = convergence;
        if (((Node) beachline.get(index)).link instanceof Point) segments.add(new Segment((Point) ((Node) beachline.get(index)).link, convergence)); else if (((Node) beachline.get(index)).link instanceof Node) ((Node) ((Node) beachline.get(index)).link).link = convergence;
        queue.remove(((Node) beachline.get(index)).event);
        beachline.remove(index);
        checkPossibleCircles(current, index);
    }

    /**
	 * Finds triples of arcs on the beachline defined
	 * by three different sites around the Node given
	 * by index such that the circle passing through 
	 * these three sites is under the beachline, is not
	 * defined in the current event and it is not already
	 * in the event queue
	 * 
	 * @param current 
	 * @param index
	 * @throws Exception
	 */
    private void checkPossibleCircles(FortuneEvent current, int index) throws Exception {
        CircleEvent event = null;
        queue.remove(((Node) beachline.get(index)).event);
        if (index - 1 >= 0) {
            queue.remove(((Node) beachline.get(index - 1)).event);
            if (index - 2 >= 0) {
                event = computeCircleEvent(((Node) beachline.get(index - 2)).focus, ((Node) beachline.get(index - 1)).focus, ((Node) beachline.get(index)).focus);
                if (event != null && event.getPriority() >= sweep - EPSILON && (BasicTests.turns(((Node) beachline.get(index)).focus, ((Node) beachline.get(index - 1)).focus, ((Node) beachline.get(index - 2)).focus) == Turn.LEFT)) {
                    if (!queue.contains(event) && !current.equals(event)) {
                        ((Node) beachline.get(index - 1)).event = event;
                        queue.add(event);
                        event.setNode(((Node) beachline.get(index - 1)));
                    }
                    event = null;
                }
            }
            if (index + 1 < beachline.size()) {
                queue.remove(((Node) beachline.get(index + 1)).event);
                event = computeCircleEvent(((Node) beachline.get(index - 1)).focus, ((Node) beachline.get(index)).focus, ((Node) beachline.get(index + 1)).focus);
                if (event != null && event.getPriority() >= sweep - EPSILON && (BasicTests.turns(((Node) beachline.get(index - 1)).focus, ((Node) beachline.get(index)).focus, ((Node) beachline.get(index + 1)).focus) == Turn.RIGHT)) {
                    if (!queue.contains(event) && !current.equals(event)) {
                        ((Node) beachline.get(index)).event = event;
                        queue.add(event);
                        event.setNode(((Node) beachline.get(index)));
                    }
                    event = null;
                }
            }
        }
        if (index + 2 < beachline.size()) {
            event = computeCircleEvent(((Node) beachline.get(index)).focus, ((Node) beachline.get(index + 1)).focus, ((Node) beachline.get(index + 2)).focus);
            if (event != null && event.getPriority() >= sweep - EPSILON && (BasicTests.turns(((Node) beachline.get(index)).focus, ((Node) ((Node) beachline.get(index + 1))).focus, ((Node) beachline.get(index + 2)).focus) == Turn.RIGHT)) {
                if (!queue.contains(event) && !current.equals(event)) {
                    ((Node) ((Node) beachline.get(index + 1))).event = event;
                    queue.add(event);
                    event.setNode(((Node) ((Node) beachline.get(index + 1))));
                }
                event = null;
            }
        }
    }

    /**
	 * Computes the intersection point of two parabolas (only the x
	 * coordinate)
	 * 
	 * @param l
	 * @param r
	 * @return
	 * @throws Exception 
	 */
    @SuppressWarnings("serial")
    private double computeBreakPoint(Node l, Node r) throws Exception {
        double ys = sweep;
        double la, lb, lc, ra, rb, rc;
        if (l.focus.getY() == sweep && r.focus.getY() == sweep) return (l.focus.getX() + r.focus.getX()) / 2d; else if (l.focus.getY() == sweep) return l.focus.getX(); else if (r.focus.getY() == sweep) return r.focus.getX();
        double a, b, c;
        if (l.sweep != sweep) {
            double lxp = l.focus.getX();
            double lyp = l.focus.getY();
            double lxv, lyv;
            double ld = 2 * (lyp - ys);
            lxv = lxp;
            lyv = (lyp + ys) / 2f;
            la = 1 / ld;
            lb = 2 * (-lxv * la);
            lc = (lxv * lxv) * la + lyv;
            l.sweep = sweep;
            l.a = la;
            l.b = lb;
            l.c = lc;
        } else {
            la = l.a;
            lb = l.b;
            lc = l.c;
        }
        if (r.sweep != sweep) {
            double rxp = r.focus.getX();
            double ryp = r.focus.getY();
            double rxv, ryv;
            double rd = 2 * (ryp - ys);
            rxv = rxp;
            ryv = (ryp + ys) / 2f;
            ra = 1 / rd;
            rb = 2 * (-rxv * ra);
            rc = (rxv * rxv) * ra + ryv;
            r.sweep = sweep;
            r.a = ra;
            r.b = rb;
            r.c = rc;
        } else {
            ra = r.a;
            rb = r.b;
            rc = r.c;
        }
        a = la - ra;
        b = lb - rb;
        c = lc - rc;
        double[] eq = { c, b, a };
        int roots = QuadCurve2D.solveQuadratic(eq);
        if (roots == -1) throw new Exception("Equation is a constant: a" + a + ", b: " + b + ", c: " + c); else if (roots == 0) throw new Exception("(Sweep at " + sweep + " ) Parabolas from " + l.focus.getLabel() + " and " + r.focus.getLabel() + " do not intersect"); else if (roots == 1) return eq[0]; else if (roots == 2) {
            if (l.focus.getY() >= r.focus.getY()) {
                return (eq[0] >= eq[1] ? eq[0] : eq[1]);
            } else {
                return (eq[0] >= eq[1] ? eq[1] : eq[0]);
            }
        } else throw new Exception("More than two roots!!???");
    }

    /**
	 * Computes the intersection point of two parabolas (x and y coordinate)
	 * @param l
	 * @param r
	 * @return
	 * @throws Exception
	 */
    private Point computeFullBreakPoint(Node l, Node r) throws Exception {
        double a, b, c;
        double convergence_x = computeBreakPoint(l, r);
        double convergence_y;
        if (l.focus.getY() == sweep && r.focus.getY() == sweep) convergence_y = Integer.MIN_VALUE; else {
            if (r.focus.getY() == sweep) {
                a = l.a;
                b = l.b;
                c = l.c;
            } else {
                a = r.a;
                b = r.b;
                c = r.c;
            }
            convergence_y = a * Math.pow(convergence_x, 2) + b * convergence_x + c;
        }
        if (convergence_y <= 0d) {
            double lxp = l.focus.getX();
            double lyp = l.focus.getY();
            double rxp = r.focus.getX();
            double ryp = r.focus.getY();
            double midx = (lxp + rxp) / 2d;
            double midy = (lyp + ryp) / 2d;
            double one_over_m = -(ryp - lyp) / (rxp - lxp);
            convergence_y = 0;
            convergence_x = -(midy * one_over_m - midx);
        }
        return new Point((int) Math.round(convergence_x), (int) Math.round(convergence_y));
    }

    /**
	 * Checks if these three points could generate divide by
	 * zero errors in the method computeCircleEvents if used
	 * in a given order.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
    private boolean arePerpendicular(Point a, Point b, Point c) {
        if (b.getX() - a.getX() == 0) return true;
        if (c.getX() - b.getX() == 0) return true;
        if (b.getY() - a.getY() == 0) return true; else return false;
    }

    /**
	 * Computes the circle passing through three distinct points
	 * 
	 * @param _a
	 * @param _b
	 * @param _c
	 * @return
	 * @throws Exception
	 */
    private CircleEvent computeCircleEvent(Point _a, Point _b, Point _c) throws Exception {
        if (_a == _b || _b == _c || _c == _a) return null;
        CircleEvent event;
        Point a, b, c;
        if (!arePerpendicular(_a, _b, _c)) {
            a = _a;
            b = _b;
            c = _c;
        } else if (!arePerpendicular(_a, _c, _b)) {
            a = _a;
            b = _c;
            c = _b;
        } else if (!arePerpendicular(_b, _a, _c)) {
            a = _b;
            b = _a;
            c = _c;
        } else if (!arePerpendicular(_b, _c, _a)) {
            a = _b;
            b = _c;
            c = _a;
        } else if (!arePerpendicular(_c, _b, _a)) {
            a = _c;
            b = _b;
            c = _a;
        } else if (!arePerpendicular(_c, _a, _b)) {
            a = _c;
            b = _a;
            c = _b;
        } else return null;
        float m1 = (b.getY() - a.getY()) / (float) (b.getX() - a.getX());
        float m2 = (c.getY() - b.getY()) / (float) (c.getX() - b.getX());
        float x = (m1 * m2 * (a.getY() - c.getY()) + m2 * (a.getX() + b.getX()) - m1 * (b.getX() + c.getX())) / (2f * (m2 - m1));
        float y = -(1 / m1) * (x - (a.getX() + b.getX()) / 2f) + (a.getY() + b.getY()) / 2f;
        double radius = (Math.sqrt((Math.pow(a.getX() - x, 2)) + (Math.pow(a.getY() - y, 2))));
        event = new CircleEvent(x, y, radius);
        if (_a.getX() <= _b.getX()) {
            if (_b.getX() <= _c.getX()) {
                a = _a;
                b = _b;
                c = _c;
            } else if (_a.getX() <= _c.getX()) {
                a = _a;
                b = _c;
                c = _b;
            } else {
                a = _c;
                b = _a;
                c = _b;
            }
        } else {
            if (_a.getX() <= _c.getX()) {
                a = _b;
                b = _a;
                c = _c;
            } else if (_b.getX() <= _c.getX()) {
                a = _b;
                b = _c;
                c = _a;
            } else {
                a = _c;
                b = _b;
                c = _a;
            }
        }
        event.setPoints(a, b, c);
        return event;
    }

    /************************************************************
	 *                  PLOTTING METHODS
	 ************************************************************/
    protected List<java.awt.Shape> plotBeachLine() {
        Vector<java.awt.Shape> vector = new Vector<java.awt.Shape>();
        Iterator<Node> i = beachline.iterator();
        Node l, r = null;
        while (i.hasNext()) {
            l = r;
            r = i.next();
            vector.add(plotParabola(r, sweep));
            try {
                if (l != null) {
                    Point bp = computeFullBreakPoint(l, r);
                    vector.add(new java.awt.geom.Rectangle2D.Float(bp.getX() - 1, bp.getY() - 1, 3, 3));
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(12);
            }
        }
        return vector;
    }

    private List<Segment> plotPartialDiagram() {
        List<Segment> list = new Vector<Segment>();
        list.addAll(segments);
        Vector<Node> visited = new Vector<Node>();
        Iterator<Node> i = beachline.iterator();
        Node l, r = null;
        while (i.hasNext()) {
            l = r;
            r = i.next();
            try {
                if (l != null) {
                    if (!visited.contains(l) && l.link != null) {
                        Point bpl = computeFullBreakPoint(l, r);
                        Point bpr = null;
                        if (l.link instanceof Node) {
                            int k = beachline.indexOf(l.link);
                            if (k != -1) bpr = computeFullBreakPoint((Node) beachline.get(k), (Node) beachline.get(k + 1));
                        } else if (l.link instanceof Point) {
                            bpr = (Point) l.link;
                        }
                        if (bpr != null) list.add(new Segment(bpl, bpr));
                        visited.add(l);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(12);
            }
        }
        return list;
    }

    protected java.awt.Shape plotParabola(Node n, double directrix) {
        if (n.focus.getY() == directrix) return new java.awt.geom.Line2D.Double(n.focus.getX(), directrix, n.focus.getX(), 0);
        QuadCurve2D curve = new QuadCurve2D.Float();
        double a, b, c;
        double xp = n.focus.getX();
        double yp = n.focus.getY();
        double ys = directrix;
        double xv, yv;
        double d = yp - ys;
        xv = xp;
        yv = (yp + ys) / 2f;
        a = 1 / (2 * d);
        b = -xv / d;
        c = (xv * xv) / (2 * d) + yv;
        double[] eq = { c, b, a };
        int roots = QuadCurve2D.solveQuadratic(eq);
        if (roots == 0) return null; else if (roots == 2) {
            double x_ground = eq[0];
            double m = 2 * a * x_ground + b;
            double q = -m * x_ground;
            double ctrly = m * n.focus.getX() + q;
            curve.setCurve(eq[0], 0, n.focus.getX(), ctrly, eq[1], 0);
            return curve;
        } else return null;
    }
}
