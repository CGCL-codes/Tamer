package org.sourceforge.espro.elicitation;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * An abstract class which extends the {@link ElicitationMethod} in order
 * to make it work with distributions. FunctionalMethods are methods which
 * have an fX() method, where this calculates a function of x.  The abstract
 * class takes care of painting it correctly on the screen.
 *
 * @author (c) 2007 Martin Kaffanke
 * @version 2.0
 */
public abstract class FunctionalMethod extends ElicitationMethod {

    /** The list of points to highlight. */
    protected ArrayList<Integer> highlight = new ArrayList<Integer>();

    /** Holds a List of markers. */
    protected ArrayList<Integer> markers = new ArrayList<Integer>();

    /** holds the points */
    protected double points[][];

    /** Wether the function should be fitted into the functional area. */
    @Setting(name = "Fit", help = "Fit the function into the available space.")
    protected boolean fitFunction = true;

    /** Child classes can set if it want to show the highlights or not. */
    protected boolean showHighlights = true;

    /** Child classes can set if it wants so show markers or not. */
    protected boolean showMarkers = true;

    /**
    * The maximum value of the courve will be stored here when running
    * method {@link #calcFunction()}
    */
    protected double maxValue;

    /** A standard height of the function area. */
    @Setting(name = "Standard Height", help = "The height of the function area.")
    protected double standardHeight = .5;

    /** A standard with of the function area. */
    @Setting(name = "Standard Width", help = "The width of the function area.")
    protected double standardWidth = 1D;

    /** Maximum of possible markers. */
    protected int maxMarkers = 10;

    private final double padding = 20D;

    private double areaHeight;

    private double areaWidth;

    private double areaX;

    private double areaY;

    private double labelHeight;

    private double labelWidth;

    @Setting(name = "Digits", values = { 0, 10, 1 })
    private int digits = 2;

    private int lastHighlightPoint = -1;

    private int maxPointIndex;

    /**
    * The constructor for extensions.
    *
    * @param name The name of the method.
    * @param category The category of the method.
    */
    protected FunctionalMethod(final String name, final String category) {
        super(name, category);
        initialize();
    }

    /**
    * Returns the digits.
    *
    * @return The digits.
    */
    public int getDigits() {
        return digits;
    }

    /**
    * Returns the maxMarkers.
    *
    * @return The maxMarkers.
    */
    public int getMaxMarkers() {
        return maxMarkers;
    }

    /**
    * Returns the index of the point that containts the max value.
    *
    * @return The index.
    */
    public int getMaxPoint() {
        return maxPointIndex;
    }

    /**
    * DOCUMENT ME!
    *
    * @return The standardHeight.
    */
    public double getStandardHeight() {
        return standardHeight;
    }

    /**
    * DOCUMENT ME!
    *
    * @return The standardWidth.
    */
    public double getStandardWidth() {
        return standardWidth;
    }

    /**
    * DOCUMENT ME!
    *
    * @return The fitFunction.
    */
    public boolean isFitFunction() {
        return fitFunction;
    }

    /**
    * Sets the digits
    *
    * @param digits The digits to set.
    */
    public void setDigits(final int digits) {
        if (frozen) {
            return;
        }
        final int old = this.digits;
        this.digits = digits;
        pcs.firePropertyChange("digits", old, this.digits);
    }

    /**
    * DOCUMENT ME!
    *
    * @param fitFunction The fitFunction to set.
    */
    public void setFitFunction(final boolean fitFunction) {
        if (frozen) {
            return;
        }
        final boolean old = this.fitFunction;
        this.fitFunction = fitFunction;
        pcs.firePropertyChange("fitFunction", old, this.fitFunction);
    }

    /**
    * Sets the maxMarkers
    *
    * @param maxMarkers The maxMarkers to set.
    */
    public void setMaxMarkers(final int maxMarkers) {
        if (frozen) {
            return;
        }
        this.maxMarkers = maxMarkers;
    }

    /**
    * DOCUMENT ME!
    *
    * @param standardHeight The standardHeight to set.
    */
    public void setStandardHeight(final double standardHeight) {
        if (frozen) {
            return;
        }
        final double old = this.standardHeight;
        this.standardHeight = standardHeight;
        pcs.firePropertyChange("standardHeight", old, this.standardHeight);
    }

    /**
    * DOCUMENT ME!
    *
    * @param standardWidth The standardWidth to set.
    */
    public void setStandardWidth(final double standardWidth) {
        if (frozen) {
            return;
        }
        final double old = this.standardWidth;
        this.standardWidth = standardWidth;
        pcs.firePropertyChange("standardWidth", old, this.standardWidth);
    }

    /**
    * DOCUMENT ME!
    */
    @Override
    protected void addSelfListeners() {
        super.addSelfListeners();
        addMouseListener(new MouseListener() {

            public void mouseClicked(final MouseEvent arg0) {
            }

            public void mouseEntered(final MouseEvent arg0) {
            }

            public void mouseExited(final MouseEvent arg0) {
                if ((lastHighlightPoint < -1) && showHighlights) {
                    highlight.remove(lastHighlightPoint);
                    lastHighlightPoint = -1;
                    repaint();
                }
            }

            public void mousePressed(final MouseEvent arg0) {
                if (contains(arg0.getPoint()) && showMarkers) {
                    final int id = highlightNearest(arg0.getPoint(), false);
                    final double h[] = points[highlight.get(id)];
                    for (int i = 0; i < markers.size(); i++) {
                        final double m[] = points[markers.get(i)];
                        if (m.equals(h)) {
                            markers.remove(i);
                            repaint();
                        }
                    }
                    markers.add(highlight.get(id));
                    while (markers.size() > maxMarkers) {
                        markers.remove(0);
                    }
                    repaint();
                }
            }

            public void mouseReleased(final MouseEvent arg0) {
            }
        });
        addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(final MouseEvent arg0) {
            }

            public void mouseMoved(final MouseEvent arg0) {
                if (contains(arg0.getPoint()) && showHighlights) {
                    highlightNearest(arg0.getPoint(), false);
                } else {
                }
            }
        });
    }

    /**
    * Calculates the points from the function. This uses your fX()
    * method from the child class.
    */
    protected void calcFunction() {
        final double step = standardWidth / areaWidth;
        points = new double[(int) areaWidth][2];
        maxValue = 0D;
        int i = 0;
        for (double x = 0; x <= standardWidth; x += step) {
            if (i >= (int) areaWidth) {
                break;
            }
            points[i][0] = x;
            points[i][1] = fX(x);
            if (i > 0) {
                maxValue = Math.max(maxValue, points[i][1]);
            } else {
                maxValue = points[i][1];
                maxPointIndex = i;
            }
            i++;
        }
    }

    /**
    * Calculates real area.
    */
    protected void calcReals() {
        final DecimalFormat threeDec = new DecimalFormat("0.000");
        threeDec.setMinimumFractionDigits(digits);
        threeDec.setMaximumFractionDigits(digits);
        final String text = (threeDec.format(new Float(10.11111)));
        final FontRenderContext frc = g2.getFontRenderContext();
        final Font font = g2.getFont();
        final Rectangle2D bounds = font.getStringBounds(text, frc);
        labelWidth = bounds.getWidth() + 11;
        labelHeight = bounds.getHeight() + 11;
        areaX = padding + labelWidth;
        final double maxAreaWidth = getWidth() - padding - areaX;
        areaY = getHeight() - padding - labelHeight;
        final double maxAreaHeight = areaY - padding;
        if ((standardWidth / standardHeight) < (maxAreaWidth / maxAreaHeight)) {
            areaWidth = (standardWidth * maxAreaHeight) / standardHeight;
            areaHeight = maxAreaHeight;
        } else if ((standardWidth / standardHeight) > (maxAreaWidth / maxAreaHeight)) {
            areaHeight = (standardHeight * maxAreaWidth) / standardWidth;
            areaWidth = maxAreaWidth;
        } else {
            areaHeight = maxAreaHeight;
            areaWidth = maxAreaWidth;
        }
        areaX = ((getWidth() - areaWidth - labelWidth) / 2) + labelWidth;
        areaY = getHeight() - ((getHeight() - areaHeight - labelHeight) / 2) - labelHeight;
    }

    /**
    * Removes all markers.
    */
    protected void clearMarkers() {
        markers = new ArrayList<Integer>();
    }

    /**
    * Draws the axis.
    */
    protected void drawAxis() {
        drawStandardLine(0, 0, 0, maxValue);
        drawStandardLine(0, 0, standardWidth, 0);
        drawXDescription();
        drawYDescription();
    }

    /**
    * Draws a filled box arround the given point.
    *
    * @param p The point.
    */
    protected void drawFilledScaledBox(final double p[]) {
        viewBox(p[0], p[1]);
        final double d = graphicsXPoint(p[0]);
        final double e = graphicsYPoint(p[1]);
        g2.fillRect((int) d - 3, (int) e - 3, 6, 6);
    }

    /**
    * Draws the Function into the graph.
    */
    protected void drawFunction() {
        calcFunction();
        for (int j = 0; j < (points.length - 1); j++) {
            drawStandardLine(points[j][0], points[j][1], points[j + 1][0], points[j + 1][1]);
        }
    }

    /**
    * Draws a box arround the given point.
    *
    * @param p The point.
    */
    protected void drawScaledBox(final double p[]) {
        viewBox(p[0], p[1]);
        final double d = graphicsXPoint(p[0]);
        final double e = graphicsYPoint(p[1]);
        g2.drawRect((int) d - 3, (int) e - 3, 6, 6);
    }

    /**
    * Draws a small line from the function.  Given are the real points,
    * but drawn are the calculated points to the graphics area.
    *
    * @param x1 The first x coordinate.
    * @param y1 The first y coodrinate.
    * @param x2 The second x coordinate.
    * @param y2 The second y coordinate.
    */
    protected void drawStandardLine(final double x1, final double y1, final double x2, final double y2) {
        g2.drawLine((int) graphicsXPoint(x1), (int) graphicsYPoint(y1), (int) graphicsXPoint(x2), (int) graphicsYPoint(y2));
    }

    /**
    * Converts a point into the graphics value.  This is equivalent
    * calling graphicsXPoint and graphicsYPoint at a time.
    *
    * @param p The standard point.
    *
    * @return The graphics point.
    */
    protected double[] graphicsPoint(final double p[]) {
        final double np[] = { graphicsXPoint(p[0]), graphicsYPoint(p[1]) };
        return np;
    }

    /**
    * Calculates the graphics x point from a standard x point.
    *
    * @param x The standard x point.
    *
    * @return The grapics x point.
    */
    protected double graphicsXPoint(final double x) {
        return areaX + ((x * areaWidth) / standardWidth);
    }

    /**
    * Calculates the graphics y point from a standard y point.
    *
    * @param y The standard y point.
    *
    * @return The graphics y point.
    */
    protected double graphicsYPoint(final double y) {
        if (fitFunction) {
            return areaY - ((y * areaHeight) / maxValue);
        }
        return areaY - ((y * areaHeight) / standardHeight);
    }

    /**
    * Highlights the point nearest the given point.
    *
    * @param point The coordinates to find the nearest point.
    * @param save true if the point should be saved.
    *
    * @return The index of the highlight list.
    */
    protected int highlightNearest(final Point point, final boolean save) {
        final double p[] = { standardXPoint(point.x), standardYPoint(point.y) };
        final int pointId = nearestPoint(p);
        if ((lastHighlightPoint > -1) && (save != true)) {
            highlight.remove(lastHighlightPoint);
            lastHighlightPoint = -1;
        }
        highlight.add(pointId);
        lastHighlightPoint = highlight.size() - 1;
        repaint();
        return lastHighlightPoint;
    }

    /**
    * 
    * @see org.sourceforge.espro.elicitation.ElicitationMethod#initialize()
    */
    @Override
    protected void initialize() {
        addSelfListeners();
    }

    /**
    * Calculates the nearest point on the courve to the given point.
    *
    * @param p The point.
    *
    * @return The nearest point id of the points array or -1 if there is none.
    */
    protected int nearestPoint(final double p[]) {
        if (points == null) {
            return -1;
        }
        double min = -1;
        int i = 0;
        int nid = 0;
        for (final double point[] : points) {
            final double w = p[0] - point[0];
            final double h = p[1] - point[1];
            final double dist = Math.hypot(w, h);
            final double oldmin = min;
            if (min < 0) {
                min = dist;
            } else {
                min = Math.min(oldmin, dist);
            }
            if (min < oldmin) {
                nid = i;
            }
            i++;
        }
        return nid;
    }

    /**
    * 
    * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
    */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        calcReals();
        drawFunction();
        drawAxis();
        if (showHighlights) {
            drawHighlights();
        }
        if (showMarkers) {
            drawMarkers();
        }
    }

    /**
    * Extensions can change both at once: The width and height of the
    * function area.
    *
    * @param width The width.
    * @param height The heigth.
    */
    protected void setStandardSize(final double width, final double height) {
        if (frozen) {
            return;
        }
        setStandardWidth(width);
        setStandardHeight(height);
    }

    /**
    * Converts a point to the standard values.  This is equivalent to
    * calling standardXPoint and standardYPoint at once.
    *
    * @param p The graphics point.
    *
    * @return The standard point.
    */
    protected double[] standardPoint(final double p[]) {
        final double np[] = { standardXPoint(p[0]), standardYPoint(p[1]) };
        return np;
    }

    /**
    * Calculates the standard x point from a graphics x point.
    *
    * @param x The graphics x point.
    *
    * @return The standard x point.
    */
    protected double standardXPoint(final double x) {
        return ((x - areaX) * standardWidth) / areaWidth;
    }

    /**
    * Calculates the standard y point from a graphics y point.
    *
    * @param y The graphics y point.
    *
    * @return The standard y point.
    */
    protected double standardYPoint(final double y) {
        if (fitFunction) {
            return ((areaY - y) * maxValue) / areaHeight;
        }
        return ((areaY - y) * standardHeight) / areaHeight;
    }

    /**
    * Draws a box on the given position.
    *
    * @param x X-axis value.
    * @param y Y-axis value.
    */
    protected void viewBox(double x, double y) {
        x = graphicsXPoint(x);
        y = graphicsYPoint(y);
        g2.drawRect((int) x - 3, (int) y - 3, 6, 6);
    }

    /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    abstract double fX(double x);

    private void drawHighlights() {
        for (final int p : highlight) {
            drawScaledBox(points[p]);
        }
    }

    private void drawMarkers() {
        for (final int p : markers) {
            drawFilledScaledBox(points[p]);
        }
    }

    private void drawXDescription() {
        final double space = standardWidth / 10;
        for (int i = 0; i < 11; i++) {
            g2.drawLine((int) graphicsXPoint(i * space), (int) graphicsYPoint(0), (int) graphicsXPoint(i * space), (int) graphicsYPoint(0) + 9);
            final DecimalFormat threeDec = new DecimalFormat("0.000");
            threeDec.setMinimumFractionDigits(digits);
            threeDec.setMaximumFractionDigits(digits);
            final String text = (threeDec.format(new Float(i * space)));
            final FontRenderContext frc = g2.getFontRenderContext();
            final Font font = g2.getFont();
            final Rectangle2D bounds = font.getStringBounds(text, frc);
            final int x = (int) (graphicsXPoint(space * i) - (bounds.getWidth() / 2));
            final int y = (int) (areaY + 11 + labelHeight);
            g2.drawString(text, x, y);
        }
    }

    private void drawYDescription() {
        final double space = maxValue / 10;
        for (int i = 0; i < 11; i++) {
            g2.drawLine((int) graphicsXPoint(0), (int) graphicsYPoint(i * space), (int) graphicsXPoint(0) - 9, (int) graphicsYPoint(i * space));
            final DecimalFormat threeDec = new DecimalFormat("0.000");
            threeDec.setMinimumFractionDigits(digits);
            threeDec.setMaximumFractionDigits(digits);
            final String text = (threeDec.format(new Float(i * space)));
            final FontRenderContext frc = g2.getFontRenderContext();
            final Font font = g2.getFont();
            final Rectangle2D bounds = font.getStringBounds(text, frc);
            final int x = (int) (areaX - 11 - bounds.getWidth());
            final int y = (int) (graphicsYPoint(space * i) + (bounds.getHeight() / 2));
            g2.drawString(text, x, y);
        }
    }
}
