package gui.scale;

import gui.gauges.AbstractGauge;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Vector;

public class Scale {

    Point2D cp;

    float scaleR;

    int angleStart;

    int angleExtend;

    float minValue;

    float maxValue;

    float minorTickStep;

    float majorTickStep;

    float minorTickStart;

    float minorTickExtend;

    float majorTickStart;

    float majorTickExtend;

    Color minorTickColor;

    Color majorTickColor;

    private int decPlaceCount;

    private Point2D getPoint(Point2D cp, double majorTickR, double currentAngle) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Object valueToString(float maxValue) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private String valueToString(double myv) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Dimension getStringSize(String s, Graphics2D g2) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected enum Direction {

        CLOCKWISE, ANTICLOCKWISE
    }

    ;

    private Direction direction = Direction.CLOCKWISE;

    Vector<ScaleNeedle> needles = new Vector<ScaleNeedle>();

    Vector<ScaleMarker> markers = new Vector<ScaleMarker>();

    Vector<ScaleArc> arcs = new Vector<ScaleArc>();

    AbstractGauge owner;

    public Scale(AbstractGauge owner, Point2D cp, float scaleR, Direction direction) {
        this.owner = owner;
        this.cp = cp;
        this.scaleR = scaleR;
        this.direction = direction;
    }

    public void draw(Graphics2D g2) {
        for (int i = 0; i < arcs.size(); i++) {
            arcs.get(i).draw(g2);
        }
        for (int i = 0; i < markers.size(); i++) {
            markers.get(i).draw(g2);
        }
        double r0 = 55;
        double r1 = 58;
        double majorTickSize = 55;
        double minorTickSize = 55;
        double majorTickR = r1 + 5;
        double minorTickR = majorTickR + (majorTickSize - minorTickSize) / 2.0;
        Stroke oldStroke = g2.getStroke();
        Font oldFont = g2.getFont();
        if (minorTickStep > 0) {
            g2.setColor(minorTickColor);
            g2.setStroke(new BasicStroke(1.f));
            double minorGradSteps = angleExtend * minorTickStep / (maxValue - minValue);
            for (double i = angleStart + angleExtend; i >= angleStart; i -= minorGradSteps) {
                Point2D p0 = getPoint(cp, minorTickR, i);
                Point2D p1 = getPoint(cp, minorTickR + minorTickSize, i);
                g2.draw(new Line2D.Double(p0, p1));
            }
        }
        if (majorTickStep > 0) {
            double markerR = majorTickR + 2 * majorTickSize;
            double l = valueToString((double) maxValue).length();
            if (l > 3 && decPlaceCount > 0) {
                l--;
            }
            double f = l < 4 ? 1.0 : 1.0 - 0.1 * (l - 3);
            double majorFontSize = 18.0;
            Font font = oldFont.deriveFont(majorFontSize < 8.0 ? Font.PLAIN : Font.BOLD, (float) majorFontSize);
            g2.setFont(font);
            g2.setColor(majorTickColor);
            g2.setStroke(new BasicStroke(2.f));
            double stepAngle = angleExtend * majorTickStep / (maxValue - minValue);
            for (double v = minValue; v <= maxValue; v += majorTickStep) {
                double myv = direction == Direction.CLOCKWISE ? v : maxValue - v + minValue;
                double currentAngle = angleStart + angleExtend - stepAngle * (v - minValue) / majorTickStep;
                Point2D p0 = getPoint(cp, majorTickR, currentAngle);
                Point2D p1 = getPoint(cp, majorTickR + majorTickSize, currentAngle);
                g2.draw(new Line2D.Double(p0, p1));
                String s = valueToString(myv);
                Dimension size = getStringSize(s, g2);
                double delta = size.getWidth() / 2;
                double fontR = markerR - 3 * majorTickSize;
                Point2D mc = getPoint(cp, fontR - delta, currentAngle);
                g2.drawString(s, (float) (mc.getX() - size.getWidth() / 2), (float) (mc.getY() + size.getHeight() / 2));
            }
            g2.setStroke(oldStroke);
            g2.setFont(oldFont);
        }
    }

    public void drawNeedles(Graphics2D g2) {
        for (int i = 0; i < needles.size(); i++) {
            needles.get(i).draw(g2);
        }
    }
}
