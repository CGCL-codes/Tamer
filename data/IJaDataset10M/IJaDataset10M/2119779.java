package eyetrackercalibrator.gui;

import eyetrackercalibrator.gui.util.RotatedEllipse2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import javax.swing.JLabel;

/**
 *
 * @author SQ
 */
public class MarkableJLabel extends JLabel {

    public static final int DEFAULT_CORNER_MARK_LENGTH = 10;

    public static final int DEFAULT_POINT_MARK_LENGTH = 2;

    /**
     * Array of corner points.  Arranges in TopLeft, BottomLeft, BottomRight, TopRight
     */
    static final int TOPLEFT = 0;

    static final int BOTTOMLEFT = 1;

    static final int BOTTOMRIGHT = 2;

    static final int TOPRIGHT = 3;

    private int cornerMarkLength = 10;

    private int pointMarkLength = 2;

    private Point[] greenCorners = new Point[4];

    private Point[] redCorners = new Point[4];

    private Point[] yellowMarkedPoints = null;

    private boolean isReversingYellowMarks = false;

    private Point[] blueMarkedPoints = null;

    private boolean isReversingBlueMarks = false;

    private Point[] greenMarkedPoints = null;

    private boolean isReversingGreenMarks = false;

    private Point[] redMarkedPoints = null;

    private boolean isReversingRedMarks = false;

    private Point[] whiteMarkedPoints = null;

    private boolean isReversingWhiteMarks = false;

    private Color color = Color.GREEN;

    private RotatedEllipse2D greenEllisp = null;

    private RotatedEllipse2D redEllisp = null;

    private void drawRoratedEllisp(RotatedEllipse2D ellisp, AffineTransform oldTransform, Graphics2D g2d) {
        if (ellisp != null) {
            if (ellisp.angle > 0) {
                AffineTransform aff = AffineTransform.getTranslateInstance(oldTransform.getTranslateX(), oldTransform.getTranslateY());
                aff.rotate(ellisp.angle, ellisp.getCenterX(), ellisp.getCenterY());
                g2d.setTransform(aff);
            }
            g2d.draw(ellisp);
            if (ellisp.angle > 0) {
                g2d.setTransform(oldTransform);
            }
        }
    }

    public enum MarkColor {

        GREEN, RED, BLUE, YELLOW, WHITE
    }

    public enum CornerColor {

        GREEN, RED
    }

    /** Creates a new instance of MarkableJLabel */
    public MarkableJLabel() {
        cornerMarkLength = DEFAULT_CORNER_MARK_LENGTH;
        pointMarkLength = DEFAULT_POINT_MARK_LENGTH;
        Arrays.fill(greenCorners, null);
        Arrays.fill(redCorners, null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawCorners(g, Color.GREEN, greenCorners);
        drawCorners(g, Color.RED, redCorners);
        int width = getWidth();
        int height = getHeight();
        if (this.isReversingGreenMarks) {
            drawReverseMarks(g, Color.GREEN, greenMarkedPoints, width, height);
        } else {
            drawMarks(g, Color.GREEN, greenMarkedPoints);
        }
        if (this.isReversingRedMarks) {
            drawReverseMarks(g, Color.RED, redMarkedPoints, width, height);
        } else {
            drawMarks(g, Color.RED, redMarkedPoints);
        }
        if (this.isReversingBlueMarks) {
            drawReverseMarks(g, Color.BLUE, blueMarkedPoints, width, height);
        } else {
            drawMarks(g, Color.BLUE, blueMarkedPoints);
        }
        if (this.isReversingYellowMarks) {
            drawReverseMarks(g, Color.YELLOW, yellowMarkedPoints, width, height);
        } else {
            drawMarks(g, Color.YELLOW, yellowMarkedPoints);
        }
        if (this.isReversingYellowMarks) {
            drawReverseMarks(g, Color.WHITE, whiteMarkedPoints, width, height);
        } else {
            drawMarks(g, Color.WHITE, whiteMarkedPoints);
        }
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = g2d.getTransform();
        if (greenEllisp != null) {
            g2d.setColor(Color.GREEN);
            drawRoratedEllisp(greenEllisp, oldTransform, g2d);
        }
        if (redEllisp != null) {
            g2d.setColor(Color.RED);
            drawRoratedEllisp(redEllisp, oldTransform, g2d);
        }
    }

    private void drawCorners(Graphics g, Color color, Point[] points) {
        g.setColor(color);
        if (points[TOPLEFT] != null) {
            g.drawLine(points[TOPLEFT].x, points[TOPLEFT].y, points[TOPLEFT].x, points[TOPLEFT].y + cornerMarkLength);
            g.drawLine(points[TOPLEFT].x, points[TOPLEFT].y, points[TOPLEFT].x + cornerMarkLength, points[TOPLEFT].y);
        }
        if (points[TOPRIGHT] != null) {
            g.drawLine(points[TOPRIGHT].x, points[TOPRIGHT].y, points[TOPRIGHT].x, points[TOPRIGHT].y + cornerMarkLength);
            g.drawLine(points[TOPRIGHT].x, points[TOPRIGHT].y, points[TOPRIGHT].x - cornerMarkLength, points[TOPRIGHT].y);
        }
        if (points[BOTTOMRIGHT] != null) {
            g.drawLine(points[BOTTOMRIGHT].x, points[BOTTOMRIGHT].y, points[BOTTOMRIGHT].x, points[BOTTOMRIGHT].y - cornerMarkLength);
            g.drawLine(points[BOTTOMRIGHT].x, points[BOTTOMRIGHT].y, points[BOTTOMRIGHT].x - cornerMarkLength, points[BOTTOMRIGHT].y);
        }
        if (points[BOTTOMLEFT] != null) {
            g.drawLine(points[BOTTOMLEFT].x, points[BOTTOMLEFT].y, points[BOTTOMLEFT].x, points[BOTTOMLEFT].y - cornerMarkLength);
            g.drawLine(points[BOTTOMLEFT].x, points[BOTTOMLEFT].y, points[BOTTOMLEFT].x + cornerMarkLength, points[BOTTOMLEFT].y);
        }
    }

    private void drawMarks(Graphics g, Color color, Point[] points) {
        g.setColor(color);
        if (points != null) {
            for (int i = 0; i < points.length; i++) {
                if (points[i] != null) {
                    g.drawLine(points[i].x - pointMarkLength, points[i].y, points[i].x + pointMarkLength, points[i].y);
                    g.drawLine(points[i].x, points[i].y - pointMarkLength, points[i].x, points[i].y + pointMarkLength);
                }
            }
        }
    }

    private void drawReverseMarks(Graphics g, Color color, Point[] points, int spaceWidth, int spaceHeight) {
        g.setColor(color);
        if (points != null) {
            for (int i = 0; i < points.length; i++) {
                g.drawLine(0, points[i].y, points[i].x - pointMarkLength - 1, points[i].y);
                g.drawLine(points[i].x + pointMarkLength + 1, points[i].y, spaceWidth, points[i].y);
                g.drawLine(points[i].x, 0, points[i].x, points[i].y - pointMarkLength - 1);
                g.drawLine(points[i].x, points[i].y + pointMarkLength + 1, points[i].x, spaceHeight);
            }
        }
    }

    public Point[] getCorners(MarkColor color) {
        switch(color) {
            case RED:
                return redCorners;
            default:
                return greenCorners;
        }
    }

    public void clearCorners() {
        Arrays.fill(redCorners, null);
        Arrays.fill(greenCorners, null);
    }

    public void clearMarks() {
        this.greenMarkedPoints = null;
        this.redMarkedPoints = null;
    }

    /** Set corner to paint.  If null is given that particular corner won't be shown */
    public void setCorners(Point topleft, Point topright, Point bottomleft, Point bottomright, MarkableJLabel.CornerColor color) {
        Point[] corners = null;
        switch(color) {
            case RED:
                corners = redCorners;
                break;
            default:
                corners = greenCorners;
        }
        corners[TOPLEFT] = topleft;
        corners[TOPRIGHT] = topright;
        corners[BOTTOMLEFT] = bottomleft;
        corners[BOTTOMRIGHT] = bottomright;
    }

    /**
     * Return points which are used to mark specific color (null will give green)
     */
    public Point[] getMarkedPoints(MarkableJLabel.MarkColor color) {
        switch(color) {
            case RED:
                return this.redMarkedPoints;
            case BLUE:
                return this.blueMarkedPoints;
            case YELLOW:
                return this.yellowMarkedPoints;
            case WHITE:
                return this.whiteMarkedPoints;
            default:
                return greenMarkedPoints;
        }
    }

    public void setMarkedPoints(Point[] markedPoints, MarkableJLabel.MarkColor color, boolean isReversed) {
        switch(color) {
            case RED:
                this.redMarkedPoints = markedPoints;
                this.isReversingRedMarks = isReversed;
                break;
            case BLUE:
                this.blueMarkedPoints = markedPoints;
                this.isReversingBlueMarks = isReversed;
                break;
            case YELLOW:
                this.yellowMarkedPoints = markedPoints;
                this.isReversingYellowMarks = isReversed;
                break;
            case WHITE:
                this.whiteMarkedPoints = markedPoints;
                this.isReversingWhiteMarks = isReversed;
                break;
            default:
                this.greenMarkedPoints = markedPoints;
                this.isReversingGreenMarks = isReversed;
        }
    }

    public int getCornerMarkLength() {
        return cornerMarkLength;
    }

    public void setCornerMarkLength(int cornerMarkLength) {
        this.cornerMarkLength = cornerMarkLength;
    }

    public int getPointMarkLength() {
        return pointMarkLength;
    }

    public void setPointMarkLength(int pointMarkLength) {
        this.pointMarkLength = pointMarkLength;
    }

    /**
     * Get ellisp bounding topleft point and size (represented by point)
     */
    public RotatedEllipse2D getGreenEllisp() {
        return greenEllisp;
    }

    /**
     * Set ellisp by specify topleft point and size (represented by point)
     */
    public void setGreenEllisp(RotatedEllipse2D greenEllisp) {
        this.greenEllisp = greenEllisp;
    }

    /**
     * Get ellisp bounding topleft point and size (represented by point)
     */
    public RotatedEllipse2D getRedEllisp() {
        return redEllisp;
    }

    /**
     * Set ellisp by specify topleft point and size (represented by point)
     */
    public void setRedEllisp(RotatedEllipse2D redEllisp) {
        this.redEllisp = redEllisp;
    }
}
