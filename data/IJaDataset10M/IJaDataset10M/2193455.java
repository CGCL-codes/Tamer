package org.gaugebook.gauges.aviation.simple;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import org.gaugebook.AbstractSteamGauge;
import org.gaugebook.FSXVariable;
import org.gaugebook.GaugeBook;

/**
 * Fuel Left / Right Gauge
 *
 * @author Michael Nagler
 */
public class FuelLR extends AbstractSteamGauge {

    private FSXVariable varFuelMainLeft, varFuelMainRight;

    private double fml = 0, cfml = 0, fmr = 0, cfmr = 0;

    private final int angleStartLeft = 315, angleExtendLeft = 90, angleStartRight = -225, angleExtendRight = 90;

    private final int minValueLeft = 0, maxValueLeft = 25, minValueRight = 0, maxValueRight = 25;

    public void init(GaugeBook owner, String title) {
        super.init(owner, title);
        this.setMinValue(0);
        this.setMaxValue(30);
        this.setText("Fuel");
        this.setDecPlaceCount(0);
        this.setMajorTickStep(5);
        this.setMinorTickStep(1);
        this.setAngleExtend(angleExtendLeft);
        this.setAngleStart(angleStartLeft);
        this.setValue(0);
        this.setShowValue(false);
        super.setTextPosition(0, -0.6);
        super.setTextHeight(1.4);
        varFuelMainLeft = owner.registerFSXVariable("FUEL TANK LEFT MAIN QUANTITY");
        varFuelMainRight = owner.registerFSXVariable("FUEL TANK RIGHT MAIN QUANTITY");
    }

    public void update() {
        fml = varFuelMainLeft.getDoubleValue();
        fmr = varFuelMainRight.getDoubleValue();
        if (Math.round(cfml) != Math.round(fml) || Math.round(cfmr) != Math.round(fmr)) {
            setValue(cfml = fml);
            cfmr = fmr;
        }
        repaint();
    }

    protected void drawScale(final double r, final Point2D cp, Graphics2D g2) {
        double f = 1;
        Point2D left = new Point2D.Double(cp.getX() - r * f, cp.getY());
        Point2D right = new Point2D.Double(cp.getX() + r * f, cp.getY());
        setAngleExtend(angleExtendRight);
        setAngleStart(angleStartRight);
        setMaxValue(maxValueRight);
        super.drawScale(r, right, g2, Direction.CLOCKWISE);
        setAngleExtend(angleExtendLeft);
        setAngleStart(angleStartLeft);
        setMaxValue(maxValueLeft);
        super.drawScale(r, left, g2, Direction.ANTICLOCKWISE);
    }

    protected void drawNeedle(final double r, double needleR, final Point2D cp, Graphics2D g2) {
        Shape oldClip = g2.getClip();
        double cf = .87;
        Ellipse2D.Double ell = new Ellipse2D.Double(cp.getX() - r * cf, cp.getY() - r * cf, 2 * r * cf, 2 * r * cf);
        g2.setClip(ell);
        double f = 1;
        Point2D left = new Point2D.Double(cp.getX() - r * f, cp.getY());
        Point2D right = new Point2D.Double(cp.getX() + r * f, cp.getY());
        setAngleExtend(angleExtendRight);
        setAngleStart(angleStartRight);
        double v = fmr;
        if (v < minValueRight || v > maxValueRight) {
            v = (v < minValueRight ? minValueRight : maxValueRight);
        }
        super.drawNeedle(r, r * .7, right, g2, v);
        setAngleExtend(angleExtendLeft);
        setAngleStart(angleStartLeft);
        v = fml;
        if (v < minValueLeft || v > maxValueLeft) {
            v = (v < minValueLeft ? minValueLeft : maxValueLeft);
        }
        super.drawNeedle(r, r * .7, left, g2, v, Direction.ANTICLOCKWISE);
        g2.setClip(oldClip);
    }

    protected void drawText(final double r, final Point2D cp, Graphics2D g2) {
        super.drawText(r, cp, g2);
        super.drawText(r, cp, g2, new Point2D.Double(0, .7), 1.1, "QTY");
        super.drawText(r, cp, g2, new Point2D.Double(0, .8), 1, "Gallons");
        super.drawText(r, cp, g2, new Point2D.Double(-.65, .1), 1.2, "Left");
        super.drawText(r, cp, g2, new Point2D.Double(.65, .1), 1.2, "Right");
    }
}
