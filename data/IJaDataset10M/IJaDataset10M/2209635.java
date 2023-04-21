package org.gaugebook.gauges;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.VolatileImage;
import java.text.NumberFormat;
import org.gaugebook.AbstractGauge;
import org.gaugebook.FSXVariable;
import org.gaugebook.GaugeBook;
import org.gaugebook.math.Angle;
import flightsim.simconnect.SimConnectDataType;

public class EICAS737 extends AbstractGauge {

    private FSXVariable totalAirTemperature;

    private FSXVariable varN1left, varN1right;

    private FSXVariable varEGTleft, varEGTright;

    private FSXVariable varN2left, varN2right;

    private FSXVariable varFFleft, varFFright;

    private FSXVariable varOilPressLeft, varOilPressRight;

    private FSXVariable varOilTempLeft, varOilTempRight;

    private FSXVariable varVIBleft, varVIBright;

    private FSXVariable varFuelCTR, varFuel1, varFuel2;

    private FSXVariable varFuelCTRmax, varFuel1max, varFuel2max;

    private FSXVariable varAviCircuitOn;

    private static NumberFormat eicasNF = NumberFormat.getInstance();

    private static final Color clrBlack = new Color(0, 0, 0);

    private static final Color clrWhite = new Color(255, 255, 255);

    private static final Color clrGrey = new Color(99, 99, 99);

    private static final Color clrGreen = new Color(98, 246, 106);

    private static final Color clrPink = new Color(222, 0, 222);

    private static final Color clrRed = new Color(222, 0, 0);

    private static final Color clrCyan = new Color(0, 111, 222);

    private double tat, n1l, n1r, egtl, egtr, n2l, n2r, ffl, ffr, opl, opr, otl, otr, vibl, vibr, fctr, f1, f2, fctrmax, f1max, f2max;

    private boolean aviOn = false, aviOld = false;

    public void init(GaugeBook owner, String title) {
        super.init(owner, title);
        totalAirTemperature = owner.registerFSXVariable("TOTAL AIR TEMPERATURE");
        varN1left = owner.registerFSXVariable("TURB ENG N1:1");
        varN1right = owner.registerFSXVariable("TURB ENG N1:2");
        varEGTleft = owner.registerFSXVariable("ENG EXHAUST GAS TEMPERATURE:1");
        varEGTright = owner.registerFSXVariable("ENG EXHAUST GAS TEMPERATURE:2");
        varN2left = owner.registerFSXVariable("TURB ENG N2:1");
        varN2right = owner.registerFSXVariable("TURB ENG N2:2");
        varFFleft = owner.registerFSXVariable("ENG FUEL FLOW PPH:1");
        varFFright = owner.registerFSXVariable("ENG FUEL FLOW PPH:2");
        varOilPressLeft = owner.registerFSXVariable("ENG OIL PRESSURE:1");
        varOilPressRight = owner.registerFSXVariable("ENG OIL PRESSURE:2");
        varOilTempLeft = owner.registerFSXVariable("ENG OIL TEMPERATURE:1");
        varOilTempRight = owner.registerFSXVariable("ENG OIL TEMPERATURE:2");
        varVIBleft = owner.registerFSXVariable("ENG VIBRATION:1");
        varVIBright = owner.registerFSXVariable("ENG VIBRATION:2");
        varFuelCTR = owner.registerFSXVariable("FUEL TANK CENTER QUANTITY");
        varFuel1 = owner.registerFSXVariable("FUEL TANK LEFT MAIN QUANTITY");
        varFuel2 = owner.registerFSXVariable("FUEL TANK RIGHT MAIN QUANTITY");
        varFuelCTRmax = owner.registerFSXVariable("FUEL TANK CENTER CAPACITY");
        varFuel1max = owner.registerFSXVariable("FUEL TANK LEFT MAIN CAPACITY");
        varFuel2max = owner.registerFSXVariable("FUEL TANK RIGHT MAIN CAPACITY");
        varAviCircuitOn = owner.registerFSXVariable("CIRCUIT AVIONICS ON");
        eicasNF.setMaximumFractionDigits(1);
        eicasNF.setMinimumFractionDigits(1);
        eicasNF.setMaximumIntegerDigits(4);
        eicasNF.setMinimumIntegerDigits(1);
        initdone = true;
    }

    public void update() {
        if (totalAirTemperature == null) return;
        tat = (Double) totalAirTemperature.getValue();
        n1l = (Double) varN1left.getValue();
        n1r = (Double) varN1right.getValue();
        egtl = (Double) varEGTleft.getValue();
        egtr = (Double) varEGTright.getValue();
        n2l = (Double) varN2left.getValue();
        n2r = (Double) varN2right.getValue();
        ffl = (Double) varFFleft.getValue();
        ffr = (Double) varFFright.getValue();
        opl = (Double) varOilPressLeft.getValue();
        opr = (Double) varOilPressRight.getValue();
        otl = (Double) varOilTempLeft.getValue();
        otr = (Double) varOilTempRight.getValue();
        vibl = (Double) varVIBleft.getValue();
        vibr = (Double) varVIBright.getValue();
        fctr = (Double) varFuelCTR.getValue();
        f1 = (Double) varFuel1.getValue();
        f2 = (Double) varFuel2.getValue();
        fctrmax = (Double) varFuelCTRmax.getValue();
        f1max = (Double) varFuel1max.getValue();
        f2max = (Double) varFuel2max.getValue();
        aviOn = (Double) varAviCircuitOn.getValue() == 1;
    }

    int lastWidth = 0, lastHeight = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Graphics g) {
        if (!initdone) return;
        if (skipFrame) return;
        skipFrame = true;
        int w = getWidth();
        int h = getHeight();
        if (w != lastWidth || h != lastHeight) {
            GraphicsConfiguration gc = getGraphicsConfiguration();
            vi = gc.createCompatibleVolatileImage(getWidth(), getHeight());
            do {
                gc = this.getGraphicsConfiguration();
                if (this.vi.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
                    vi = gc.createCompatibleVolatileImage(getWidth(), getHeight());
                }
                Graphics2D g2 = vi.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, (antialias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF));
                lastWidth = w;
                lastHeight = h;
                double r = Math.min(w, h) / 2 - 5;
                if (r < 0) r = 100;
                Point2D cp = new Point2D.Double(w / 2, h / 2);
                drawBackground(r, cp, g2);
                if (aviOn) {
                    double r0 = proz(r, 55);
                    double r1 = proz(r, 58);
                    double majorTickSize = proz(r, 4);
                    double minorTickSize = proz(r, 2);
                    double majorTickR = r1 + 4;
                    double minorTickR = majorTickR + (majorTickSize - minorTickSize) / 2.0;
                    double needleR = majorTickR + 1.5 * majorTickSize;
                    drawScale(r, cp, g2);
                    drawText(r, cp, g2);
                    drawValue(r, needleR, cp, g2);
                    drawNeedle(r, needleR, cp, g2);
                }
                g2.dispose();
            } while (vi.contentsLost());
        }
        g.drawImage(vi, 0, 0, this);
        skipFrame = false;
    }

    private boolean firstRun = true;

    protected void drawBackground(final double r, final Point2D cp, Graphics2D g2) {
        Point2D p0, p1, p2, p3;
        Rectangle2D rect;
        Polygon pol;
        Ellipse2D ell;
        boolean update = firstRun;
        int w = getWidth();
        int h = getHeight();
        if (update) {
            GraphicsConfiguration gc = getGraphicsConfiguration();
            vi = gc.createCompatibleVolatileImage(getWidth(), getHeight());
            do {
                gc = this.getGraphicsConfiguration();
                if (this.vi.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
                    vi = gc.createCompatibleVolatileImage(getWidth(), getHeight());
                }
                Graphics2D bg2 = vi.createGraphics();
                bg2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, (antialias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF));
                bg2.setClip(0, 0, w, h);
                bg2.setColor(Color.DARK_GRAY);
                bg2.fillRect(0, 0, w, h);
                bg2.setColor(clrBlack);
                bg2.setColor(clrBlack);
                p1 = new Point2D.Double(cp.getX() - r * .98, cp.getY() - r * 0.95);
                RoundRectangle2D.Double rr = new RoundRectangle2D.Double(p1.getX(), p1.getY(), r * 1.9, r * 1.8, r * .1, r * .1);
                bg2.fill(rr);
                if (!aviOn) return;
                Arc2D arc;
                bg2.setColor(clrWhite);
                p1 = new Point2D.Double(cp.getX() - r * .65, cp.getY() - r * .75);
                rect = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .3, r * .1);
                bg2.draw(rect);
                arc = new Arc2D.Double(cp.getX() - r * .85, cp.getY() - r * .85, r * .4, r * .4, 0, -210, Arc2D.OPEN);
                bg2.draw(arc);
                p1 = new Point2D.Double(cp.getX() + r * .0, cp.getY() - r * .75);
                rect = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .3, r * .1);
                bg2.draw(rect);
                arc = new Arc2D.Double(cp.getX() - r * .2, cp.getY() - r * .85, r * .4, r * .4, 0, -210, Arc2D.OPEN);
                bg2.draw(arc);
                p1 = new Point2D.Double(cp.getX() - r * .65, cp.getY() - r * .3);
                rect = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .3, r * .1);
                bg2.draw(rect);
                arc = new Arc2D.Double(cp.getX() - r * .85, cp.getY() - r * .4, r * .4, r * .4, 0, -210, Arc2D.OPEN);
                bg2.draw(arc);
                p1 = new Point2D.Double(cp.getX() + r * .0, cp.getY() - r * .3);
                rect = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .3, r * .1);
                bg2.draw(rect);
                arc = new Arc2D.Double(cp.getX() - r * .2, cp.getY() - r * .4, r * .4, r * .4, 0, -210, Arc2D.OPEN);
                bg2.draw(arc);
                bg2.setColor(clrCyan);
                p1 = new Point2D.Double(cp.getX() - r * .9, cp.getY() - r * 0.83);
                drawTextLeft(r, p1, bg2, .65, "TAT");
                p1 = new Point2D.Double(cp.getX() + r * .7, cp.getY() - r * 0.83);
                drawText(r, p1, bg2, .65, "ENG1");
                p1 = new Point2D.Double(cp.getX() + r * .7, cp.getY() - r * 0.53);
                drawText(r, p1, bg2, .65, "ENG2");
                p1 = new Point2D.Double(cp.getX() - r * .3, cp.getY() - r * .45);
                drawText(r, p1, bg2, .65, "N1");
                p1 = new Point2D.Double(cp.getX() - r * .3, cp.getY() + r * .0);
                drawText(r, p1, bg2, .65, "EGT");
                p1 = new Point2D.Double(cp.getX() - r * .3, cp.getY() + r * 0.32);
                drawText(r, p1, bg2, .65, "N2");
                p1 = new Point2D.Double(cp.getX() - r * .3, cp.getY() + r * 0.43);
                drawText(r, p1, bg2, .65, "FF");
                p1 = new Point2D.Double(cp.getX() - r * .3, cp.getY() + r * 0.54);
                drawText(r, p1, bg2, .65, "OIL PRESS");
                p1 = new Point2D.Double(cp.getX() - r * .3, cp.getY() + r * 0.65);
                drawText(r, p1, bg2, .65, "OIL TEMP");
                p1 = new Point2D.Double(cp.getX() - r * .3, cp.getY() + r * 0.76);
                drawText(r, p1, bg2, .65, "VIB");
                p1 = new Point2D.Double(cp.getX() + r * .7, cp.getY() + r * 0);
                drawText(r, p1, bg2, .65, "FUEL LBS");
                p1 = new Point2D.Double(cp.getX() + r * .7, cp.getY() + r * .2);
                drawText(r, p1, bg2, .65, "CTR");
                p1 = new Point2D.Double(cp.getX() + r * .7, cp.getY() + r * .4);
                drawText(r, p1, bg2, .65, "1");
                p1 = new Point2D.Double(cp.getX() + r * .7, cp.getY() + r * .6);
                drawText(r, p1, bg2, .65, "2");
            } while (backVi.contentsLost());
        }
        g2.drawImage(backVi, 0, 0, this);
    }

    protected synchronized void drawText(final double r, final Point2D cp, Graphics2D g2) {
    }

    protected synchronized void drawScale(final double r, final Point2D cp, Graphics2D g2) {
        Point2D p0, p1, p2, p3, p4, p5, p6, p7, p8, pc;
        Polygon pol;
        Ellipse2D ell;
        Rectangle2D rect;
        Shape oldClip = g2.getClip();
        Font oldFont = g2.getFont();
        g2.setClip(oldClip);
    }

    protected synchronized void drawNeedle(final double r, double needleR, final Point2D cp, Graphics2D g2) {
    }

    protected void drawValue(final double r, final double needleR, final Point2D cp, Graphics2D g2) {
        Point2D p1;
        g2.setColor(clrWhite);
        p1 = new Point2D.Double(cp.getX() - r * .72, cp.getY() - r * .83);
        drawTextLeft(r, p1, g2, .7, Math.round(tat) + "�C");
        p1 = new Point2D.Double(cp.getX() - r * .36, cp.getY() - r * .65);
        drawTextRight(r, p1, g2, .65, eicasNF.format(n1l));
        p1 = new Point2D.Double(cp.getX() + r * .29, cp.getY() - r * .65);
        drawTextRight(r, p1, g2, .65, eicasNF.format(n1r));
        p1 = new Point2D.Double(cp.getX() - r * .36, cp.getY() - r * .2);
        drawTextRight(r, p1, g2, .65, eicasNF.format(egtl));
        p1 = new Point2D.Double(cp.getX() + r * .29, cp.getY() - r * .2);
        drawTextRight(r, p1, g2, .65, eicasNF.format(egtr));
        p1 = new Point2D.Double(cp.getX() - r * .6, cp.getY() + r * .32);
        drawText(r, p1, g2, .7, eicasNF.format(n2l));
        p1 = new Point2D.Double(cp.getX() - r * .0, cp.getY() + r * .32);
        drawText(r, p1, g2, .7, eicasNF.format(n2r));
        p1 = new Point2D.Double(cp.getX() - r * .6, cp.getY() + r * .43);
        drawText(r, p1, g2, .7, eicasNF.format(ffl));
        p1 = new Point2D.Double(cp.getX() - r * .0, cp.getY() + r * .43);
        drawText(r, p1, g2, .7, eicasNF.format(ffr));
        p1 = new Point2D.Double(cp.getX() - r * .6, cp.getY() + r * .54);
        drawText(r, p1, g2, .7, eicasNF.format(opl));
        p1 = new Point2D.Double(cp.getX() - r * .0, cp.getY() + r * .54);
        drawText(r, p1, g2, .7, eicasNF.format(opr));
        p1 = new Point2D.Double(cp.getX() - r * .6, cp.getY() + r * .65);
        drawText(r, p1, g2, .7, eicasNF.format(otl));
        p1 = new Point2D.Double(cp.getX() - r * .0, cp.getY() + r * .65);
        drawText(r, p1, g2, .7, eicasNF.format(otr));
        p1 = new Point2D.Double(cp.getX() - r * .6, cp.getY() + r * .76);
        drawText(r, p1, g2, .7, eicasNF.format(vibl));
        p1 = new Point2D.Double(cp.getX() - r * .0, cp.getY() + r * .76);
        drawText(r, p1, g2, .7, eicasNF.format(vibr));
        p1 = new Point2D.Double(cp.getX() + r * .7, cp.getY() + r * .31);
        drawText(r, p1, g2, .65, "" + Math.round(fctr));
        p1 = new Point2D.Double(cp.getX() + r * .7, cp.getY() + r * .51);
        drawText(r, p1, g2, .65, "" + Math.round(f1));
        p1 = new Point2D.Double(cp.getX() + r * .7, cp.getY() + r * .71);
        drawText(r, p1, g2, .65, "" + Math.round(f2));
    }
}
