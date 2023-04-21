package hoipolloi;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * GradientPanel is a class with a gradient background.
 * Put your non-opaque objects over it and enjoy.
 *
 * @author Pyrite[1up]
 * @author Brandon Buck
 */
public class GradientPanel extends JPanel {

    private Color startColor;

    private Color endColor;

    private int fillDirection = GradientPanel.LEFT_RIGHT_CENTER;

    private GradientPoints customPoints = null;

    public static final int LEFT_RIGHT_CENTER = 0;

    public static final int TOP_BOTTOM_DIAGONAL = 1;

    public static final int BOTTOM_TOP_DIAGONAL = 2;

    public static final int TOP_BOTTOM_CENTER = 3;

    public static final int CUSTOM_POINTS = 4;

    public GradientPanel() {
        this(Color.BLACK, Color.WHITE);
    }

    /**
     * Constructor supplying a color. Using the default RIGHT_LEFT_CENTER fill
     * option.
     * @param startColor This is the LEFT or TOP color when filling the panel.
     * @param endColor This is the RIGHT or BOTTOM color when filling the panel.
     */
    public GradientPanel(Color startColor, Color endColor) {
        super();
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public GradientPanel(Color startColor, Color endColor, int fillDirection) {
        super();
        this.startColor = startColor;
        this.endColor = endColor;
        if (fillDirection == GradientPanel.CUSTOM_POINTS) this.fillDirection = GradientPanel.LEFT_RIGHT_CENTER; else this.fillDirection = fillDirection;
    }

    public GradientPanel(Color startColor, Color endColor, GradientPoints customPoints) {
        super();
        this.startColor = startColor;
        this.endColor = endColor;
        this.fillDirection = GradientPanel.CUSTOM_POINTS;
        this.customPoints = customPoints;
    }

    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }

    public void setEndColor(Color endColor) {
        this.endColor = endColor;
    }

    public void setFillDirection(int fillDirection) {
        if (fillDirection == GradientPanel.CUSTOM_POINTS) this.fillDirection = GradientPanel.LEFT_RIGHT_CENTER; else this.fillDirection = fillDirection;
    }

    public void setCustomPoints(GradientPoints customPoints) {
        this.customPoints = customPoints;
        this.fillDirection = GradientPanel.CUSTOM_POINTS;
    }

    private Rectangle getFillArea() {
        return new Rectangle(0, 0, this.getWidth(), this.getHeight());
    }

    private GradientPoints getGradientPoints(Rectangle fillArea) {
        double width = fillArea.getWidth();
        double height = fillArea.getHeight();
        GradientPoints fillPoints = new GradientPoints();
        if (this.fillDirection == GradientPanel.TOP_BOTTOM_CENTER) {
            fillPoints.setPointOne(new Point2D.Double((width / 2), 0));
            fillPoints.setPointTwo(new Point2D.Double((width / 2), height));
        } else if (this.fillDirection == GradientPanel.TOP_BOTTOM_DIAGONAL) {
            fillPoints.setPointOne(new Point2D.Double(0, 0));
            fillPoints.setPointTwo(new Point2D.Double(width, height));
        } else if (this.fillDirection == GradientPanel.BOTTOM_TOP_DIAGONAL) {
            fillPoints.setPointOne(new Point2D.Double(0, height));
            fillPoints.setPointTwo(new Point2D.Double(width, 0));
        } else if (this.fillDirection == GradientPanel.LEFT_RIGHT_CENTER) {
            fillPoints.setPointOne(new Point2D.Double(0, (height / 2)));
            fillPoints.setPointTwo(new Point2D.Double(width, (height / 2)));
        } else if (this.fillDirection == GradientPanel.CUSTOM_POINTS) {
            fillPoints = this.customPoints;
        } else {
            fillPoints.setPointOne(new Point2D.Double(0.0, 0.0));
            fillPoints.setPointTwo(new Point2D.Double(0.0, 0.0));
        }
        return fillPoints;
    }

    protected void paintPanel(Graphics2D g2) {
        Rectangle fillArea = this.getFillArea();
        GradientPoints fillPoints = this.getGradientPoints(fillArea);
        GradientPaint gradientPaint = new GradientPaint(fillPoints.getPointOne(), this.startColor, fillPoints.getPointTwo(), this.endColor);
        Paint oldPaint = g2.getPaint();
        g2.setPaint(gradientPaint);
        g2.fill(fillArea);
        g2.setPaint(oldPaint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        paintPanel((Graphics2D) g);
    }

    class GradientPoints {

        private Point2D.Double pointOne;

        private Point2D.Double pointTwo;

        public GradientPoints() {
            pointOne = null;
            pointTwo = null;
        }

        public void setPointOne(Point2D.Double pointOne) {
            this.pointOne = pointOne;
        }

        public void setPointTwo(Point2D.Double pointTwo) {
            this.pointTwo = pointTwo;
        }

        public Point2D.Double getPointOne() {
            return this.pointOne;
        }

        public Point2D.Double getPointTwo() {
            return this.pointTwo;
        }
    }
}
