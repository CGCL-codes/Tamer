package org.gaugebook.gauges.aviation.simple;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import org.gaugebook.AbstractSteamGauge;
import org.gaugebook.FSXVariable;
import org.gaugebook.GaugeBook;

/**
 * Oil Temperature and Pressure Gauge
 * 
 * @author Michael Nagler
 */
public class OilTempPress extends AbstractSteamGauge {

    private FSXVariable varOilTemp, varOilPress;

    private double oiltemp = 0, oilpress = 0;

    private final int angleStartLeft = 315, angleExtendLeft = 90, angleStartRight = -225, angleExtendRight = 90;

    private final int minValueLeft = 50, maxValueLeft = 250, minValueRight = 0, maxValueRight = 120;

    private final int minorTickStepLeft = 25, majorTickStepLeft = 50, minorTickStepRight = 10, majorTickStepRight = 40;

    public void init(GaugeBook owner, String title) {
        super.init(owner, title);
        this.setMinValue(0);
        this.setMaxValue(30);
        this.setText("Oil");
        this.setDecPlaceCount(0);
        this.setMajorTickStep(5);
        this.setMinorTickStep(1);
        this.setAngleExtend(angleExtendLeft);
        this.setAngleStart(angleStartLeft);
        this.setValue(0);
        this.setShowValue(false);
        super.setTextPosition(0, -.6);
        super.setTextHeight(1.4);
        varOilTemp = owner.registerFSXVariable("GENERAL ENG OIL TEMPERATURE:1");
        varOilPress = owner.registerFSXVariable("GENERAL ENG OIL PRESSURE:1");
    }

    public void update() {
        oiltemp = varOilTemp.getDoubleValue();
        setValue(oilpress = varOilPress.getDoubleValue());
        repaint();
    }

    @Override
    protected void drawScale(final double r, final Point2D cp, Graphics2D g2) {
        double f = 1;
        Point2D left = new Point2D.Double(cp.getX() - r * f, cp.getY());
        Point2D right = new Point2D.Double(cp.getX() + r * f, cp.getY());
        setAngleExtend(angleExtendLeft);
        setAngleStart(angleStartLeft);
        setMinorTickStep(minorTickStepLeft);
        setMajorTickStep(majorTickStepLeft);
        setMinValue(minValueLeft);
        setMaxValue(maxValueLeft);
        String[] args;
        if (getProperty("greenArcTemp") != null) {
            args = ((String) getProperty("greenArcTemp")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(0, 160, 0), r, .86, .94, left, g2, Direction.ANTICLOCKWISE);
        }
        if (getProperty("yellowArcTemp") != null) {
            args = ((String) getProperty("yellowArcTemp")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(160, 160, 0), r, .86, .94, left, g2, Direction.ANTICLOCKWISE);
        }
        if (getProperty("whiteArcTemp") != null) {
            args = ((String) getProperty("whiteArcTemp")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(160, 160, 160), r, .90, .94, left, g2, Direction.ANTICLOCKWISE);
        }
        super.drawScale(r, left, g2, Direction.ANTICLOCKWISE);
        if (getProperty("redMarkerTemp") != null) {
            args = ((String) getProperty("redMarkerTemp")).split(",");
            super.drawMarker(Integer.parseInt(args[0]), new Color(160, 0, 0), 4, r, .82, .92, left, g2, Direction.ANTICLOCKWISE);
        }
        setAngleExtend(angleExtendRight);
        setAngleStart(angleStartRight);
        setMinorTickStep(minorTickStepRight);
        setMajorTickStep(majorTickStepRight);
        setMinValue(minValueRight);
        setMaxValue(maxValueRight);
        if (getProperty("greenArcPress") != null) {
            args = ((String) getProperty("greenArcPress")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(0, 160, 0), r, .86, .94, right, g2);
        }
        if (getProperty("yellowArcPress") != null) {
            args = ((String) getProperty("yellowArcPress")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(160, 160, 0), r, .86, .94, right, g2);
        }
        if (getProperty("whiteArcPress") != null) {
            args = ((String) getProperty("whiteArcPress")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(160, 160, 160), r, .90, .94, right, g2);
        }
        super.drawScale(r, right, g2, Direction.CLOCKWISE);
        if (getProperty("redMarkerPress") != null) {
            args = ((String) getProperty("redMarkerPress")).split(",");
            super.drawMarker(Integer.parseInt(args[0]), new Color(160, 0, 0), 4, r, .82, .92, right, g2);
        }
    }

    @Override
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
        setMinorTickStep(minorTickStepRight);
        setMajorTickStep(majorTickStepRight);
        setMinValue(minValueRight);
        setMaxValue(maxValueRight);
        double v = oilpress * 0.006944;
        if (v < minValueRight || v > maxValueRight) {
            v = (v < minValueRight ? minValueRight : maxValueRight);
        }
        super.drawNeedle(r, r * .7, right, g2, v);
        setAngleExtend(angleExtendLeft);
        setAngleStart(angleStartLeft);
        setMinorTickStep(minorTickStepLeft);
        setMajorTickStep(majorTickStepLeft);
        setMinValue(minValueLeft);
        setMaxValue(maxValueLeft);
        v = oiltemp - 459.67;
        if (v < minValueLeft || v > maxValueLeft) {
            v = (v < minValueLeft ? minValueLeft : maxValueLeft);
        }
        super.drawNeedle(r, r * .7, left, g2, v, Direction.ANTICLOCKWISE);
        g2.setClip(oldClip);
    }

    protected void drawText(final double r, final Point2D cp, Graphics2D g2) {
        super.drawText(r, cp, g2);
        super.drawText(r, cp, g2, new Point2D.Double(-.65, .05), 1.1, "Temp.");
        super.drawText(r, cp, g2, new Point2D.Double(-.65, .15), 1.1, "�F");
        super.drawText(r, cp, g2, new Point2D.Double(.65, .05), 1.1, "Press.");
        super.drawText(r, cp, g2, new Point2D.Double(.65, .15), 1.1, "psi");
    }
}
