package org.gaugebook.gauges;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import org.gaugebook.AbstractGauge;
import org.gaugebook.FSXVariable;
import org.gaugebook.GaugeBook;

public class GenericXMLGauge extends AbstractGauge {

    private FSXVariable simVar;

    private double val = 0;

    public GenericXMLGauge() {
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
    }

    public void init(GaugeBook owner, String title) {
        super.init(owner, title);
        super.setTextPosition(0, -.25);
        simVar = owner.registerFSXVariable(new FSXVariable(getProperty("simulationVariable").toString()));
        setText(title);
        setProperty("title", title);
    }

    public void update() {
        if (simVar == null) return;
        if (getProperty("valueFactor") != null) {
            val = (Double) simVar.getValue() * (Double) getProperty("valueFactor");
        } else {
            val = (Double) simVar.getValue();
        }
        super.setValue(val);
        super.update();
    }

    protected void drawNeedle(final double r, final double needleR, final Point2D cp, Graphics2D g2) {
        value = val;
        super.conformProperties();
        super.drawNeedle(r, needleR, cp, g2);
    }

    protected void drawScale(final double r, final Point2D cp, Graphics2D g2) {
        double ominv = minValue;
        double omaxv = maxValue;
        if (getProperty("scaleFactor") != null) {
            minValue = minValue * (Double) getProperty("scaleFactor");
            maxValue = maxValue * (Double) getProperty("scaleFactor");
        }
        String[] args;
        if (getProperty("greenArc") != null) {
            args = ((String) getProperty("greenArc")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(0, 160, 0), r, .86, .94, cp, g2);
        }
        if (getProperty("yellowArc") != null) {
            args = ((String) getProperty("yellowArc")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(160, 160, 0), r, .86, .94, cp, g2);
        }
        if (getProperty("whiteArc") != null) {
            args = ((String) getProperty("whiteArc")).split(",");
            super.drawArc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), new Color(160, 160, 160), r, .90, .94, cp, g2);
        }
        super.drawScale(r, cp, g2);
        if (getProperty("redMarker") != null) {
            args = ((String) getProperty("redMarker")).split(",");
            super.drawMarker(Integer.parseInt(args[0]), new Color(160, 0, 0), 4, r, .82, .92, cp, g2);
        }
        minValue = ominv;
        maxValue = omaxv;
    }
}
