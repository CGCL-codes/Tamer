package org.goniolab.unitcircle.visualizer.inversecircle;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import org.goniolab.lib.graphics.GPoint2D;
import org.goniolab.lib.math.GAngle;
import org.goniolab.unitcircle.Calculator;
import org.goniolab.unitcircle.Delta;
import org.goniolab.unitcircle.Options;
import org.goniolab.unitcircle.visualizer.CanvasPanelCircle;
import org.goniolab.unitcircle.visualizer.PainterMan;

/**
 *
 * @author Patrik Karlsson <patrik@trixon.se>
 */
public class InverseCircleCanvas extends CanvasPanelCircle {

    private Delta delta = new Delta();

    public InverseCircleCanvas() {
        init();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAngleArcs();
    }

    public void setDelta(Delta delta) {
        this.delta = delta;
    }

    @Override
    protected void mouseDragged(MouseEvent evt) {
        Calculator.getInstance().setDeltaValue(getOrigo().screenToRelativeCoordinate(evt.getPoint()), getRadius());
    }

    @Override
    protected void mousePressed(MouseEvent evt) {
        Calculator.getInstance().setDeltaValue(getOrigo().screenToRelativeCoordinate(evt.getPoint()), getRadius());
    }

    @Override
    protected void preferenceChangeColors() {
        setBackgroundColor(Options.getInstance().getColor(Options.ColorPref.BG_INVERSE_CIRCLE));
        setForegroundColor(Options.getInstance().getColor(Options.ColorPref.FG_INVERSE_CIRCLE));
        try {
            circle.setColor(getForegroundColor());
        } catch (NullPointerException e) {
        }
    }

    @Override
    protected void zoomCheckBoxStateChanged(boolean aState) {
        super.zoomCheckBoxStateChanged(aState);
    }

    @Override
    protected void zoomSliderStateChanged(int aValue) {
        super.zoomSliderStateChanged(aValue);
    }

    private void drawAngleArcs() {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setStroke(PEN_NORMAL);
        GAngle phi2 = delta.getPhi2();
        g2.setColor(Options.getInstance().getColor(Options.ColorPref.PHI2));
        PainterMan.drawAngleArc(g2, getOrigo(), phi2, 10);
        GAngle phi1 = delta.getPhi1();
        g2.setColor(Options.getInstance().getColor(Options.ColorPref.PHI1));
        PainterMan.drawAngleArc(g2, getOrigo(), phi1, 20);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (delta.getMode() == Delta.Mode.ARCCOS) {
            drawTrigCos(delta.getDeltaX());
        } else if (delta.getMode() == Delta.Mode.ARCSIN) {
            drawTrigSin(delta.getDeltaY());
        }
        g2.setStroke(PEN_PHI);
        GPoint2D fromPoint2D = new GPoint2D();
        GPoint2D toPoint2D = new GPoint2D();
        fromPoint2D.setLocation(0, 0);
        fromPoint2D.setOffset(getOffset());
        toPoint2D.setOffset(getOffset());
        g2.setColor(Options.getInstance().getColor(Options.ColorPref.PHI2));
        Point.Double phiEnd = getOrigo().polarToRect(phi2.getRad(), getRadius());
        toPoint2D.setLocation(phiEnd.x, phiEnd.y);
        g2.drawLine(fromPoint2D.sX(), fromPoint2D.sY(), toPoint2D.sX(), toPoint2D.sY());
        g2.setColor(Options.getInstance().getColor(Options.ColorPref.PHI1));
        phiEnd = getOrigo().polarToRect(phi1.getRad(), getRadius());
        toPoint2D.setLocation(phiEnd.x, phiEnd.y);
        g2.drawLine(fromPoint2D.sX(), fromPoint2D.sY(), toPoint2D.sX(), toPoint2D.sY());
    }

    private void init() {
        preferenceChangeColors();
    }
}
