package org.gaugebook.gauges;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import org.gaugebook.AbstractGauge;
import org.gaugebook.FSXVariable;
import org.gaugebook.GaugeBook;

public class GenericDualXMLGauge extends AbstractGauge {

    private FSXVariable simVar, simVarR;

    private int angleStartLeft = 315;

    private int angleExtendLeft = 90, angleStartRight = -225, angleExtendRight = 90;

    private double val = 0, valR = 0;

    public GenericDualXMLGauge() {
        this.setMinValue(0);
        this.setMaxValue(35);
        this.setDecPlaceCount(0);
        this.setMajorTickStep(5);
        this.setMinorTickStep(1);
        this.setAngleExtend(270);
        this.setAngleStart(-45);
        this.setValue(0);
        this.setShowValue(false);
        setProperty("scaleFactor", 1.0);
        setProperty("title", "");
        setProperty("unitTextCenter", "");
        setProperty("unitTextL", "");
        setProperty("unitTextR", "");
        setProperty("titleL", "Left");
        setProperty("titleR", "Right");
    }

    public void init(GaugeBook owner, String title) {
        super.init(owner, title);
        super.setTextPosition(0, -.25);
        simVar = owner.registerFSXVariable(new FSXVariable(getProperty("simulationVariable").toString()));
        simVarR = owner.registerFSXVariable(new FSXVariable(getProperty("simulationVariableR").toString()));
        setText(title);
        setProperty("title", title);
        if (hasProperty("needles") && ((String) getProperty("needles")).equals("centered")) {
            angleStartLeft = -225;
            angleExtendLeft = 90;
            angleStartRight = 315;
            angleExtendRight = 90;
        }
    }

    public void update() {
        if (simVar == null) return;
        if (getProperty("valueFactor") != null) {
            val = (Double) simVar.getValue() * (Double) getProperty("valueFactor");
        } else {
            val = (Double) simVar.getValue();
        }
        if (getProperty("valueFactorR") != null) {
            valR = (Double) simVarR.getValue() * (Double) getProperty("valueFactorR");
        } else {
            valR = (Double) simVarR.getValue();
        }
        if (getProperty("valueAddend") != null) {
            val += (Double) getProperty("valueAddend");
        }
        if (getProperty("valueAddendR") != null) {
            valR += (Double) getProperty("valueAddendR");
        }
        super.setValue(val);
        super.update();
    }

    protected void drawNeedle(final double r, double needleR, final Point2D cp, Graphics2D g2) {
        Shape oldClip = g2.getClip();
        double cf = .87;
        Ellipse2D.Double ell = new Ellipse2D.Double(cp.getX() - r * cf, cp.getY() - r * cf, 2 * r * cf, 2 * r * cf);
        g2.setClip(ell);
        double f = 1;
        boolean centered = hasProperty("needles") ? ((String) getProperty("needles")).equals("centered") : false;
        Point2D left = centered ? cp : new Point2D.Double(cp.getX() - r * f, cp.getY());
        Point2D right = centered ? cp : new Point2D.Double(cp.getX() + r * f, cp.getY());
        setAngleExtend(angleExtendRight);
        setAngleStart(angleStartRight);
        setMinValue((Double) getProperty("minValueR"));
        setMaxValue((Double) getProperty("maxValueR"));
        if (valR < minValue || valR > maxValue) {
            valR = (valR < minValue ? minValue : maxValue);
        }
        super.drawNeedle(r, r * .7, right, g2, valR, centered ? Direction.ANTICLOCKWISE : Direction.CLOCKWISE);
        setAngleExtend(angleExtendLeft);
        setAngleStart(angleStartLeft);
        setMinValue((Double) getProperty("minValue"));
        setMaxValue((Double) getProperty("maxValue"));
        if (val < minValue || val > maxValue) {
            val = (val < minValue ? minValue : maxValue);
        }
        super.drawNeedle(r, r * .7, left, g2, val, centered ? Direction.CLOCKWISE : Direction.ANTICLOCKWISE);
        g2.setClip(oldClip);
    }

    protected void drawScale(final double r, final Point2D cp, Graphics2D g2) {
        double f = 1;
        boolean centered = hasProperty("needles") ? ((String) getProperty("needles")).equals("centered") : false;
        Point2D lcp = centered ? cp : new Point2D.Double(cp.getX() - r * f, cp.getY());
        Point2D rcp = centered ? cp : new Point2D.Double(cp.getX() + r * f, cp.getY());
        setMinValue((Double) getProperty("minValueR"));
        setMaxValue((Double) getProperty("maxValueR"));
        angleStart = angleStartRight;
        angleExtend = angleExtendRight;
        if (getProperty("scaleFactorR") != null) {
            minValue = minValue * (Double) getProperty("scaleFactorR");
            maxValue = maxValue * (Double) getProperty("scaleFactorR");
        }
        String[] args;
        if (getProperty("greenArcR") != null) {
            args = ((String) getProperty("greenArcR")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(0, 160, 0), r, .86, .94, rcp, g2, centered ? Direction.ANTICLOCKWISE : Direction.CLOCKWISE);
        }
        if (getProperty("yellowArcR") != null) {
            args = ((String) getProperty("yellowArcR")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(160, 160, 0), r, .86, .94, rcp, g2, centered ? Direction.ANTICLOCKWISE : Direction.CLOCKWISE);
        }
        if (getProperty("whiteArcR") != null) {
            args = ((String) getProperty("whiteArcR")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(160, 160, 160), r, .90, .94, rcp, g2, centered ? Direction.ANTICLOCKWISE : Direction.CLOCKWISE);
        }
        super.drawScale(r, rcp, g2, centered ? Direction.ANTICLOCKWISE : Direction.CLOCKWISE);
        if (getProperty("redMarkerR") != null) {
            args = ((String) getProperty("redMarker")).split(",");
            super.drawMarker(Integer.parseInt(args[0]), new Color(160, 0, 0), 4, r, .82, .92, rcp, g2, centered ? Direction.ANTICLOCKWISE : Direction.CLOCKWISE);
        }
        setMinValue((Double) getProperty("minValue"));
        setMaxValue((Double) getProperty("maxValue"));
        angleStart = angleStartLeft;
        angleExtend = angleExtendLeft;
        if (getProperty("scaleFactor") != null) {
            minValue = minValue * (Double) getProperty("scaleFactor");
            maxValue = maxValue * (Double) getProperty("scaleFactor");
        }
        if (getProperty("greenArc") != null) {
            args = ((String) getProperty("greenArc")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(0, 160, 0), r, .86, .94, lcp, g2, centered ? Direction.CLOCKWISE : Direction.ANTICLOCKWISE);
        }
        if (getProperty("yellowArc") != null) {
            args = ((String) getProperty("yellowArc")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(160, 160, 0), r, .86, .94, lcp, g2, centered ? Direction.CLOCKWISE : Direction.ANTICLOCKWISE);
        }
        if (getProperty("whiteArc") != null) {
            args = ((String) getProperty("whiteArc")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(160, 160, 160), r, .90, .94, lcp, g2, centered ? Direction.CLOCKWISE : Direction.ANTICLOCKWISE);
        }
        super.drawScale(r, lcp, g2, centered ? Direction.CLOCKWISE : Direction.ANTICLOCKWISE);
        if (getProperty("redMarker") != null) {
            args = ((String) getProperty("redMarker")).split(",");
            super.drawMarker(Integer.parseInt(args[0]), new Color(160, 0, 0), 4, r, .82, .92, lcp, g2, centered ? Direction.CLOCKWISE : Direction.ANTICLOCKWISE);
        }
    }

    protected void drawText(final double r, final Point2D cp, Graphics2D g2) {
        boolean centered = hasProperty("needles") ? ((String) getProperty("needles")).equals("centered") : false;
        super.drawText(r, cp, g2, new Point2D.Double(0, -.7), 1.1, (String) getProperty("title"));
        super.drawText(r, cp, g2, new Point2D.Double(0, .8), 1, (String) getProperty("unitTextCenter"));
        super.drawText(r, cp, g2, new Point2D.Double(centered ? -.35 : -.65, .1), 1.2, (String) getProperty("titleL"));
        super.drawText(r, cp, g2, new Point2D.Double(centered ? .35 : .65, .1), 1.2, (String) getProperty("titleR"));
    }
}
