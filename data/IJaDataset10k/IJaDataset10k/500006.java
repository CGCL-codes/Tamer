package org.sourceforge.espro.elicitation;

import JSci.maths.statistics.BetaDistribution;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 * The BetaDist method shows a beta distribution to the user and lets them
 * manipulate it by dragging the function courve arround.
 *
 * @author (c) 2007 Martin Kaffanke
 * @version 2.0
 */
public class BetaDist extends FunctionalMethod {

    /**  */
    private static final long serialVersionUID = -5623716906331511044L;

    /**
    * 
    * @see #threePointMethod(Point)
    */
    public static final int THREE_POINT = 0;

    /**
    * 
    * @see #threePointFitMethod(Point)
    */
    public static final int THREE_POINT_FIT = 1;

    /**
    * 
    * @see #distancedTwoPointMethod(Point)
    */
    public static final int DISTANCED_TWO_POINT = 2;

    /**
    * 
    * @see #dynamicPointsMethod(Point)
    */
    public static final int DYNAMIC_POINT = 3;

    /** The p value of the beta distribution function. */
    @Setting(name = "p", help = "The p value of the beta distribution.")
    @Data
    protected double betaP = 2;

    /** The q value of the beta distribution function. */
    @Setting(name = "q", help = "The q value of the beta distribution.")
    @Data
    protected double betaQ = 2;

    private final ArrayList<double[]> threePoints = new ArrayList<double[]>();

    private final int mousePos[] = new int[2];

    private double distance = .4;

    private int fitMethod = 0;

    /**
    * Default constructor.
    */
    public BetaDist() {
        super("Beta Distribution", "Distributions");
    }

    /**
    * Constructor for extensions of this class.
    *
    * @param name The name of the method.
    * @param category The (menu) category of the method.
    */
    protected BetaDist(final String name, final String category) {
        super(name, category);
    }

    /**
    * 
    * @see org.sourceforge.espro.elicitation.FunctionalMethod#fX(double)
    */
    @Override
    public double fX(final double x) {
        if ((x <= 0.0) || (x >= 1.0)) {
            return 0;
        }
        BetaDistribution b;
        b = new BetaDistribution(betaP, betaQ);
        return b.probability(x);
    }

    /**
    * DOCUMENT ME!
    *
    * @return The betaP.
    */
    public double getBetaP() {
        return betaP;
    }

    /**
    * DOCUMENT ME!
    *
    * @return The betaQ.
    */
    public double getBetaQ() {
        return betaQ;
    }

    /**
    * Returns the distance.
    *
    * @return The distance.
    */
    public double getDistance() {
        return distance;
    }

    /**
    * Returns the fitMethod.
    *
    * @return The fitMethod.
    */
    public int getFitMethod() {
        return fitMethod;
    }

    /**
    * DOCUMENT ME!
    *
    * @param betaP The betaP to set.
    */
    public void setBetaP(final double betaP) {
        if (frozen) {
            return;
        }
        final double old = this.betaP;
        this.betaP = betaP;
        pcs.firePropertyChange("betaP", old, this.betaP);
    }

    /**
    * DOCUMENT ME!
    *
    * @param betaQ The betaQ to set.
    */
    public void setBetaQ(final double betaQ) {
        if (frozen) {
            return;
        }
        final double old = this.betaQ;
        this.betaQ = betaQ;
        pcs.firePropertyChange("betaQ", old, this.betaQ);
    }

    /**
    * Sets the distance
    *
    * @param distance The distance to set.
    */
    public void setDistance(final double distance) {
        if (frozen) {
            return;
        }
        this.distance = distance;
    }

    /**
    * Sets the fitMethod
    *
    * @param fitMethod The fitMethod to set.
    */
    public void setFitMethod(final int fitMethod) {
        if (frozen) {
            return;
        }
        final int old = this.fitMethod;
        this.fitMethod = fitMethod;
        pcs.firePropertyChange("fitMethod", old, this.fitMethod);
    }

    /**
    * Calculates the angle of a given point.
    *
    * @param pid The index of the point in the points array.
    *
    * @return the angle in degrees.
    */
    protected double angleOfPoint(final int pid) {
        final double a = points[pid + 1][0] - points[pid - 1][0];
        final double b = points[pid + 1][1] - points[pid - 1][1];
        final double alpha = Math.toDegrees(Math.atan(b / a));
        return alpha;
    }

    /**
    * The fixedTwoPointMethod takes the next point on the actual courve
    * to calculate two points next to it with the distance of the give
    * distance, or the last point of the actual courve.
    *
    * @param p The mouse pointer on the scale.
    */
    protected void distancedTwoPointMethod(final Point p) {
        final double p1[] = { standardXPoint(p.x), standardYPoint(p.y) };
        clearMarkers();
        final int nearest = nearestPoint(p1);
        double dist = 0;
        int i = nearest;
        while ((dist < distance) && (i >= 0)) {
            i--;
            dist += Math.hypot(p1[0] - points[i][0], p1[1] - points[i][1]);
        }
        final double p2[] = points[i];
        markers.add(i);
        dist = 0;
        i = nearest;
        while ((dist < distance) && (i < points.length)) {
            i++;
            dist += Math.hypot(p1[0] - points[i][0], p1[1] - points[i][1]);
        }
        final double p3[] = points[i];
        markers.add(i);
        fitCurve(p1, p2, p3);
    }

    /**
    * This finds dynamic points using an algorithm, so the user can
    * intuitivly modify the courve.
    *
    * @param p The mouse pointer position.
    */
    protected void dynamicPointsMethod(final Point p) {
        final double p1[] = { standardXPoint(p.x), standardYPoint(p.x) };
        final int np = nearestPoint(p1);
        int p2i = 0;
        int p3i = 0;
        System.out.println(np);
        if ((np == 0) || (np == (points.length - 1))) {
            return;
        }
        final double alpha = angleOfPoint(np);
        System.out.printf("nearest Point: %d : %.2f\n", np, alpha);
        if (alpha < 15) {
            p2i = 0;
            p3i = getMaxPoint();
        } else if (alpha > 15) {
            p2i = seekPoint(np, 15, 1);
            if (p2i == -1) {
                p2i = points.length - 2;
            }
            p3i = seekPoint(np, 15, -1);
            if (p3i == -1) {
                p3i = 1;
            }
        }
        if (((p2i == 0) && (p3i == 0)) || (p2i == -1) || (p3i == -1)) {
            return;
        }
        fitCurve(p1, points[p2i], points[p3i]);
    }

    /**
    * Fits the given point into the curve, using the given fit method.
    *
    * @param point The mouse pointer position.
    */
    protected void fit(final Point point) {
        switch(fitMethod) {
            case THREE_POINT:
                threePointMethod(point);
                break;
            case THREE_POINT_FIT:
                threePointFitMethod(point);
                break;
            case DISTANCED_TWO_POINT:
                distancedTwoPointMethod(point);
                break;
            case DYNAMIC_POINT:
                dynamicPointsMethod(point);
                break;
            default:
                threePointMethod(point);
        }
    }

    /**
    * Fits the curve so it matches this point. We need three points to
    * calculate the P and Q values for the new Beta Distribution. Note: We
    * need standard points, so if you get it from the mouse, convert it first.
    *
    * @param p1 Point 1.
    * @param p2 Point 2.
    * @param p3 Point 3.
    */
    protected void fitCurve(final double p1[], final double p2[], final double p3[]) {
        final double x1 = p1[0];
        final double y1 = p1[1];
        final double x2 = p2[0];
        final double y2 = p2[1];
        final double x3 = p3[0];
        final double y3 = p3[1];
        System.out.printf("Calculating P and Q for %.3f/%.3f; " + "%.3f/%.3f; %.3f/%.3f.\n", x1, y1, x2, y2, x3, y3);
        double Q = 1 + (((((Math.log(y1) * Math.log(x2)) - (Math.log(y1) * Math.log(x3)) + (Math.log(y2) * Math.log(x3))) - (Math.log(y2) * Math.log(x1)) + (Math.log(y3) * Math.log(x1))) - (Math.log(y3) * Math.log(x2))) / ((((Math.log(1 - x1) * Math.log(x2)) - (Math.log(1 - x1) * Math.log(x3)) + (Math.log(1 - x2) * Math.log(x3))) - (Math.log(1 - x2) * Math.log(x1)) + (Math.log(1 - x3) * Math.log(x1))) - (Math.log(1 - x3) * Math.log(x2))));
        double P = 1 + (((Math.log(y1) - Math.log(y2)) - ((Q - 1) * (Math.log(1 - x1) - Math.log(1 - x2)))) / (Math.log(x1) - Math.log(x2)));
        if (P < 0) {
            P = P * (-1);
        }
        if (Q < 0) {
            Q = Q * (-1);
        }
        if ((P < 1000) && (Q < 1000)) {
            System.out.println("Calced: P: " + P + " Q: " + Q);
            setBetaP(P);
            setBetaQ(Q);
        }
    }

    /**
    * 
    * @see org.sourceforge.espro.elicitation.ElicitationMethod#initialize()
    */
    @Override
    protected void initialize() {
        super.initialize();
        setStandardSize(1, .5);
        setMaxMarkers(3);
        showHighlights = false;
        showMarkers = false;
        addMouseListener(new MouseListener() {

            public void mouseClicked(final MouseEvent arg0) {
            }

            public void mouseEntered(final MouseEvent arg0) {
            }

            public void mouseExited(final MouseEvent arg0) {
            }

            public void mousePressed(final MouseEvent arg0) {
                fit(arg0.getPoint());
            }

            public void mouseReleased(final MouseEvent arg0) {
            }
        });
        addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(final MouseEvent arg0) {
                fit(arg0.getPoint());
            }

            public void mouseMoved(final MouseEvent arg0) {
            }
        });
    }

    /**
    * 
    * @see org.sourceforge.espro.elicitation.FunctionalMethod#paintComponent(java.awt.Graphics)
    */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        drawThreePoints();
    }

    /**
    * Seeks a point which reaches the angle. If the point in pid has a
    * higher angle it searches the next point with the lower angle, and it
    * search the higher angle if the pid has a higher angle.
    *
    * @param pid The point to start from.
    * @param alpha The angle searched.
    * @param direction The direction (1 = forward, -1 = backwards)
    *
    * @return The index of the point from the points array or -1 if there
    *         couldn't be found a point (in the given direction).
    *
    * @throws IllegalArgumentException DOCUMENT ME!
    */
    protected int seekPoint(final int pid, final double alpha, final int direction) {
        if ((direction != 1) && (direction != -1)) {
            throw new IllegalArgumentException("direction can only be 1 or -1.");
        }
        double beta = angleOfPoint(pid);
        if (beta < alpha) {
            int pos = pid;
            while (beta < alpha) {
                pos = pos + direction;
                if ((pos == 0) || (pos == (points.length - 1))) {
                    return -1;
                }
                beta = angleOfPoint(pos);
            }
            System.out.printf("Found point %d, angle: %.2f\n", pos, beta);
            return pos;
        } else if (beta > alpha) {
            int pos = pid;
            while (beta > alpha) {
                pos = pos + direction;
                if ((pos == 0) || (pos == (points.length - 1))) {
                    return -1;
                }
                beta = angleOfPoint(pos);
            }
            System.out.printf("Found point %d, angle: %.2f\n", pos, beta);
            return pos;
        }
        return -1;
    }

    /**
    * This is a three Point Method where the points are fitted into the
    * courve.
    *
    * @param point The mouse pointer position.
    *
    * @see #threePointMethod
    */
    protected void threePointFitMethod(final Point point) {
        threePointMethod(point);
        if (threePoints.size() > 2) {
            for (int i = 0; i < 3; i++) {
                final double p[] = threePoints.get(i);
                final double pi[] = standardPoint(p);
                final double pii[] = graphicsPoint(points[nearestPoint(pi)]);
                p[0] = pii[0];
                p[1] = pii[1];
            }
            repaint();
        }
    }

    /**
    * Takes three cursur inputs and draws on this. The 4th and more
    * points will redraw the neares point of the 3 given before.
    *
    * @param point A Point from the mouse.
    */
    protected void threePointMethod(final Point point) {
        final double p[] = { point.x, point.y };
        if (threePoints.size() > 2) {
            final double p1[] = threePoints.get(0);
            final double p2[] = threePoints.get(1);
            final double p3[] = threePoints.get(2);
            final double d1 = dist(p1, p);
            final double d2 = dist(p2, p);
            final double d3 = dist(p3, p);
            if (d1 > d2) {
                if (d2 > d3) {
                    threePoints.remove(2);
                } else {
                    threePoints.remove(1);
                }
            } else {
                if (d1 > d3) {
                    threePoints.remove(2);
                } else {
                    threePoints.remove(0);
                }
            }
        }
        for (final double tp[] : threePoints) {
            if ((tp[0] == p[0]) && (tp[1] == p[1])) {
                return;
            }
        }
        threePoints.add(p);
        if (threePoints.size() < 3) {
            repaint();
        }
        if (threePoints.size() > 2) {
            fitCurve(standardPoint(threePoints.get(0)), standardPoint(threePoints.get(1)), standardPoint(threePoints.get(2)));
        }
    }

    /**
    * This watches the mouse where it is and if the button is pressed
    * this recalculates the beta curve.
    *
    * @param x The x position of the mouse.
    * @param y The y position of the mouse.
    */
    protected void trackMouse(final int x, final int y) {
        final int old[] = mousePos;
        mousePos[0] = x;
        mousePos[1] = y;
        if (contains(x, y)) {
        } else {
        }
        pcs.firePropertyChange("mousePos", old, mousePos);
    }

    private void clearPoints() {
        threePoints.clear();
    }

    private double dist(final double p1[], final double p2[]) {
        final double x = p1[0] - p2[0];
        final double y = p1[1] - p2[1];
        return Math.sqrt((x * x) + (y * y));
    }

    private void drawThreePoints() {
        for (final double p[] : threePoints) {
            drawFilledScaledBox(standardPoint(p));
        }
    }
}
