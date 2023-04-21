package org.gaugebook.gauges;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
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

public class PFD737 extends AbstractGauge {

    private static final long serialVersionUID = 1L;

    private FSXVariable indicatedAirspeed;

    private FSXVariable trueAirspeed;

    private FSXVariable selectedAirspeed;

    private FSXVariable selectedMach;

    private FSXVariable indicatedAltitude;

    private FSXVariable selectedAltitude;

    private FSXVariable kohlsmanMB;

    private FSXVariable kohlsmanHG;

    private FSXVariable varAviCircuitOn;

    private FSXVariable verticalSpeed;

    private FSXVariable bankVar;

    private FSXVariable pitchVar;

    private FSXVariable ballVar;

    private FSXVariable headingIndicator, headingLockDir;

    private FSXVariable vorOBS, vorCDI, vorToFrom, vorGSAngle, vorGSFlag;

    private FSXVariable nav1ident, nav1name;

    private FSXVariable nav2ident, nav2name;

    private FSXVariable nav1obs, nav2obs;

    private FSXVariable hasDME1, hasDME2, navDME1, navDME2;

    private FSXVariable flightDirectorActive;

    private FSXVariable flightDirectorPitch;

    private FSXVariable flightDirectorBank;

    private FSXVariable apHdgLock, apSpdLock, apAltLock;

    private double bank = 0, pitch = 0, fdbank = 0, fdpitch = 0, ias, sas, vs, smach, hi, hld, qnh, ialt, salt, shdg, hdg;

    private boolean aviOn = false, aviOld = false, flightDirOn = false, hdglock = false, spdlock = false, altlock = false;

    private static final Color skyColorAI = new Color(0, 141, 223);

    private static final Color gndColorAI = new Color(140, 74, 16);

    private static final Color clrBlack = new Color(0, 0, 0);

    private static final Color clrWhite = new Color(255, 255, 255);

    private static final Color clrGrey = new Color(75, 75, 75);

    private static final Color clrGreen = new Color(98, 246, 106);

    private static final Color clrPink = new Color(222, 0, 222);

    private static final Color clrRed = new Color(222, 0, 0);

    private static NumberFormat dmeNF = NumberFormat.getInstance();

    private boolean firstCall = true;

    public void init(GaugeBook owner, String title) {
        super.init(owner, title);
        indicatedAirspeed = owner.registerFSXVariable("AIRSPEED INDICATED");
        selectedAirspeed = owner.registerFSXVariable("AUTOPILOT AIRSPEED HOLD VAR");
        selectedMach = owner.registerFSXVariable("AUTOPILOT MACH HOLD VAR");
        trueAirspeed = owner.registerFSXVariable("AIRSPEED TRUE");
        indicatedAltitude = owner.registerFSXVariable("INDICATED ALTITUDE");
        selectedAltitude = owner.registerFSXVariable("AUTOPILOT ALTITUDE LOCK VAR");
        kohlsmanMB = owner.registerFSXVariable("KOHLSMAN SETTING MB");
        kohlsmanHG = owner.registerFSXVariable("KOHLSMAN SETTING HG");
        verticalSpeed = owner.registerFSXVariable("VERTICAL SPEED");
        bankVar = owner.registerFSXVariable("ATTITUDE INDICATOR BANK DEGREES");
        pitchVar = owner.registerFSXVariable("ATTITUDE INDICATOR PITCH DEGREES");
        ballVar = owner.registerFSXVariable("TURN COORDINATOR BALL");
        headingIndicator = owner.registerFSXVariable("HEADING INDICATOR");
        headingLockDir = owner.registerFSXVariable("AUTOPILOT HEADING LOCK DIR");
        vorOBS = owner.registerFSXVariable("NAV OBS:1");
        vorCDI = owner.registerFSXVariable("NAV CDI:1");
        vorToFrom = owner.registerFSXVariable("NAV TOFROM:1");
        vorGSAngle = owner.registerFSXVariable("NAV GLIDE SLOPE ERROR:1");
        vorGSFlag = owner.registerFSXVariable("NAV GS FLAG:1");
        nav1ident = owner.registerFSXVariable("NAV IDENT:1");
        nav1name = owner.registerFSXVariable("NAV NAME:1");
        nav2ident = owner.registerFSXVariable("NAV IDENT:2");
        nav2name = owner.registerFSXVariable("NAV NAME:2");
        nav1obs = owner.registerFSXVariable("NAV OBS:1");
        nav2obs = owner.registerFSXVariable("NAV OBS:2");
        hasDME1 = owner.registerFSXVariable("NAV HAS DME:1");
        hasDME2 = owner.registerFSXVariable("NAV HAS DME:2");
        navDME1 = owner.registerFSXVariable("NAV DME:1");
        navDME2 = owner.registerFSXVariable("NAV DME:2");
        flightDirectorActive = owner.registerFSXVariable("AUTOPILOT FLIGHT DIRECTOR ACTIVE");
        flightDirectorPitch = owner.registerFSXVariable("AUTOPILOT FLIGHT DIRECTOR PITCH");
        flightDirectorBank = owner.registerFSXVariable("AUTOPILOT FLIGHT DIRECTOR BANK");
        apSpdLock = owner.registerFSXVariable("AUTOPILOT AIRSPEED HOLD");
        apHdgLock = owner.registerFSXVariable("AUTOPILOT HEADING LOCK");
        apAltLock = owner.registerFSXVariable("AUTOPILOT ALTITUDE LOCK");
        varAviCircuitOn = owner.registerFSXVariable("CIRCUIT AVIONICS ON");
        dmeNF.setMaximumFractionDigits(1);
        dmeNF.setMinimumFractionDigits(1);
        dmeNF.setMaximumIntegerDigits(2);
        dmeNF.setMinimumIntegerDigits(1);
        initdone = true;
    }

    public void update() {
        if (bankVar == null) return;
        ialt = (Double) indicatedAltitude.getValue();
        ias = (Double) indicatedAirspeed.getValue();
        sas = (Double) selectedAirspeed.getValue();
        vs = (Double) verticalSpeed.getValue();
        smach = (Double) selectedMach.getValue();
        hi = (180 / StrictMath.PI) * (Double) headingIndicator.getValue();
        hld = (Double) headingLockDir.getValue();
        salt = (Double) selectedAltitude.getValue();
        pitch = (180 / StrictMath.PI) * (Double) pitchVar.getValue();
        bank = (180 / StrictMath.PI) * (Double) bankVar.getValue();
        fdpitch = (180 / StrictMath.PI) * (Double) flightDirectorPitch.getValue();
        fdbank = (180 / StrictMath.PI) * (Double) flightDirectorBank.getValue();
        qnh = (Double) kohlsmanMB.getValue();
        hdg = (Double) headingIndicator.getValue();
        shdg = (Double) headingLockDir.getValue();
        aviOn = (Double) varAviCircuitOn.getValue() == 1;
        flightDirOn = (Double) flightDirectorActive.getValue() == 1;
        hdglock = (Double) apHdgLock.getValue() == 1;
        spdlock = (Double) apSpdLock.getValue() == 1;
        altlock = (Double) apAltLock.getValue() == 1;
    }

    int lastWidth = 0, lastHeight = 0;

    protected void paintComponent(Graphics g) {
        if (!initdone) return;
        if (skipFrame) return;
        skipFrame = true;
        int w = getWidth();
        int h = getHeight();
        GraphicsConfiguration gc = getGraphicsConfiguration();
        if (w != lastWidth || h != lastHeight || vi != null && vi.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
            lastWidth = w;
            lastHeight = h;
            vi = gc.createCompatibleVolatileImage(getWidth(), getHeight());
            do {
                gc = this.getGraphicsConfiguration();
                if (this.vi.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
                    vi = gc.createCompatibleVolatileImage(getWidth(), getHeight());
                }
                Graphics2D g2 = vi.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, (antialias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF));
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

    protected void drawBackground(final double r, final Point2D cp, Graphics2D g2) {
        Point2D p1, p2, p3, p4, p5, p6, p7, p8, p9, p10;
        Polygon pol;
        boolean update = firstCall;
        int w = getWidth();
        int h = getHeight();
        GraphicsConfiguration gc = getGraphicsConfiguration();
        if (update || backVi != null && backVi.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
            backVi = gc.createCompatibleVolatileImage(getWidth(), getHeight());
            do {
                gc = this.getGraphicsConfiguration();
                if (backVi.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
                    backVi = gc.createCompatibleVolatileImage(getWidth(), getHeight());
                }
                Graphics2D bg2 = backVi.createGraphics();
                bg2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, (antialias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF));
                bg2.setClip(0, 0, w, h);
                bg2.setColor(Color.DARK_GRAY);
                bg2.fillRect(0, 0, w, h);
                bg2.setColor(clrBlack);
                bg2.setColor(clrBlack);
                p1 = new Point2D.Double(cp.getX() - r * .98, cp.getY() - r * 0.95);
                RoundRectangle2D.Double rr = new RoundRectangle2D.Double(p1.getX(), p1.getY(), r * 1.9, r * 1.8, r * .1, r * .1);
                bg2.fill(rr);
                if (aviOn) {
                    bg2.setColor(clrGrey);
                    p1 = new Point2D.Double(cp.getX() - r * .55, cp.getY() - r * .9);
                    bg2.fillRect((int) p1.getX(), (int) p1.getY(), (int) (r * .9), (int) (r * .16));
                    bg2.setColor(clrGrey);
                    p1 = new Point2D.Double(cp.getX() - r * .9, cp.getY() - r * .65);
                    bg2.fillRect((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * 1.1));
                    bg2.setColor(skyColorAI);
                    p1 = new Point2D.Double(cp.getX() - r * .55, cp.getY() - r * 0.55);
                    rr = new RoundRectangle2D.Double(p1.getX(), p1.getY(), r * .9, r * .9, r * .1, r * .1);
                    bg2.fill(rr);
                    bg2.setColor(clrGrey);
                    p1 = new Point2D.Double(cp.getX() + r * .45, cp.getY() - r * .65);
                    bg2.fillRect((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * 1.1));
                    p1 = new Point2D.Double(cp.getX() + r * .775, cp.getY() - r * .175);
                    p2 = getPoint(p1, r * .078, 135);
                    p3 = getPoint(p2, r * .32, 90);
                    p4 = getPoint(p3, r * .08, 0);
                    p5 = getPoint(p4, r * .4, 280);
                    p6 = getPoint(p5, r * .125, 270);
                    p7 = getPoint(p6, r * .4, 260);
                    p8 = getPoint(p7, r * .08, 180);
                    p9 = getPoint(p8, r * .32, 90);
                    p10 = getPoint(p9, r * .078, 45);
                    pol = new Polygon();
                    pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
                    pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
                    pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
                    pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
                    pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
                    pol.addPoint((int) Math.round(p6.getX()), (int) Math.round(p6.getY()));
                    pol.addPoint((int) Math.round(p7.getX()), (int) Math.round(p7.getY()));
                    pol.addPoint((int) Math.round(p8.getX()), (int) Math.round(p8.getY()));
                    pol.addPoint((int) Math.round(p9.getX()), (int) Math.round(p9.getY()));
                    pol.addPoint((int) Math.round(p10.getX()), (int) Math.round(p10.getY()));
                    pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
                    bg2.setColor(clrGrey);
                    bg2.fill(pol);
                    p1 = new Point2D.Double(cp.getX() - r * .9, cp.getY() - r * 0.9);
                    Rectangle2D.Double rect = new Rectangle2D.Double(p1.getX(), p1.getY(), r * 1.8, r * 1.7);
                    bg2.setClip(rect);
                    bg2.setColor(clrGrey);
                    Ellipse2D.Double ell = new Ellipse2D.Double(cp.getX() - r * .8, cp.getY() + r * .55, 1.4 * r, 1.4 * r);
                    bg2.fill(ell);
                    bg2.dispose();
                }
            } while (backVi.contentsLost());
        }
        g2.drawImage(backVi, 0, 0, this);
    }

    protected synchronized void drawText(final double r, final Point2D cp, Graphics2D g2) {
    }

    protected void drawValue(final double r, final double needleR, final Point2D cp, Graphics2D g2) {
        Point2D p0, p1, p2, p3, p4, p5, p6, p7;
        Polygon pol;
        Rectangle2D rect;
        Shape oldClip = g2.getClip();
        p1 = new Point2D.Double(cp.getX() - r * .68, cp.getY() - r * .1);
        p2 = getPoint(p1, r * .039, 135);
        p3 = getPoint(p2, r * .05, 90);
        p4 = getPoint(p3, r * .2, 180);
        p5 = getPoint(p4, r * .15, 270);
        p6 = getPoint(p5, r * .2, 0);
        p7 = getPoint(p6, r * .05, 90);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        pol.addPoint((int) Math.round(p6.getX()), (int) Math.round(p6.getY()));
        pol.addPoint((int) Math.round(p7.getX()), (int) Math.round(p7.getY()));
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        g2.setColor(clrBlack);
        g2.fill(pol);
        g2.setColor(clrWhite);
        g2.draw(pol);
        double dy = 1.1 / 120;
        double das = sas - ias;
        das = das < -60 ? -60 : das;
        das = das > 60 ? 60 : das;
        double ys = cp.getY() - r * .1 - r * das * dy;
        p1 = new Point2D.Double(cp.getX() - r * .66, ys);
        p2 = getPoint(p1, r * .025, 45);
        p3 = getPoint(p2, r * .05, 0);
        p4 = getPoint(p3, r * .035, 270);
        p5 = getPoint(p4, r * .05, 180);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        g2.setColor(clrPink);
        g2.draw(pol);
        g2.setColor(clrWhite);
        p1 = new Point2D.Double(cp.getX() - r * .72, cp.getY() - r * .035);
        drawTextRight(r, p1, g2, .9, "" + Math.round(ias));
        g2.setColor(clrPink);
        p1 = new Point2D.Double(cp.getX() - r * .9 + r * .25, cp.getY() - r * .65);
        drawTextRight(r, p1, g2, .8, "" + Math.round(sas));
        g2.setColor(clrGreen);
        if (flightDirOn) {
            p1 = new Point2D.Double(cp.getX() - r * .09, cp.getY() - r * .55);
            drawText(r, p1, g2, .8, "FD");
        }
        if (spdlock) {
            p1 = new Point2D.Double(cp.getX() - r * .36, cp.getY() - r * .8);
            drawText(r, p1, g2, .7, "SPD");
        }
        if (hdglock) {
            p1 = new Point2D.Double(cp.getX() - r * .09, cp.getY() - r * .8);
            drawText(r, p1, g2, .7, "HDG");
        }
        if (altlock) {
            p1 = new Point2D.Double(cp.getX() + r * .18, cp.getY() - r * .8);
            drawText(r, p1, g2, .7, "ALT");
        }
        g2.setColor(clrWhite);
        p1 = new Point2D.Double(cp.getX() - r * .9 + r * .25, cp.getY() + r * .55);
        drawTextRight(r, p1, g2, .8, "" + smach);
        g2.setColor(clrPink);
        p1 = new Point2D.Double(cp.getX() + r * .45 + r * .25, cp.getY() - r * .65);
        drawTextRight(r, p1, g2, .8, "" + Math.round(salt));
        p1 = new Point2D.Double(cp.getX() + r * .35, cp.getY() - r * .65);
        rect = new Rectangle2D.Double((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * 1.1));
        g2.setClip(rect);
        dy = 1.1 / 800;
        das = salt - ialt;
        das = das < -400 ? -400 : das;
        das = das > 400 ? 400 : das;
        ys = cp.getY() - r * .1 - r * das * dy;
        p1 = new Point2D.Double(cp.getX() + r * .45, ys);
        p2 = getPoint(p1, r * .03, 135);
        p3 = getPoint(p2, r * .05, 90);
        p4 = getPoint(p3, r * .1, 0);
        p5 = getPoint(p4, r * .14, 270);
        p6 = getPoint(p5, r * .1, 180);
        p7 = getPoint(p6, r * .05, 90);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        pol.addPoint((int) Math.round(p6.getX()), (int) Math.round(p6.getY()));
        pol.addPoint((int) Math.round(p7.getX()), (int) Math.round(p7.getY()));
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        g2.setColor(clrPink);
        g2.draw(pol);
        g2.setClip(oldClip);
        p1 = new Point2D.Double(cp.getX() + r * .47, cp.getY() - r * .1);
        p2 = getPoint(p1, r * .039, 45);
        p3 = getPoint(p2, r * .05, 90);
        p4 = getPoint(p3, r * .25, 0);
        p5 = getPoint(p4, r * .15, 270);
        p6 = getPoint(p5, r * .25, 180);
        p7 = getPoint(p6, r * .05, 90);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        pol.addPoint((int) Math.round(p6.getX()), (int) Math.round(p6.getY()));
        pol.addPoint((int) Math.round(p7.getX()), (int) Math.round(p7.getY()));
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        g2.setColor(clrBlack);
        g2.fill(pol);
        g2.setColor(clrWhite);
        g2.draw(pol);
        g2.setColor(clrWhite);
        p1 = new Point2D.Double(cp.getX() + r * .74, cp.getY() - r * .045);
        int ia = (int) Math.round(ialt / 20) * 20;
        drawTextRight(r, p1, g2, .65, "" + ia);
        g2.setColor(clrGreen);
        p1 = new Point2D.Double(cp.getX() + r * .45 + r * .25, cp.getY() + r * .55);
        drawTextRight(r, p1, g2, .6, Math.round(qnh) + " mb");
        g2.setColor(clrPink);
        p1 = new Point2D.Double(cp.getX() - r * .14, cp.getY() + r * .81);
        drawTextRight(r, p1, g2, .7, Math.round(shdg) + "H");
        g2.setColor(clrGreen);
        p1 = new Point2D.Double(cp.getX() + r * .14, cp.getY() + r * .81);
        drawTextRight(r, p1, g2, .7, "MAG");
        dy = 1.1 / 800;
        das = salt - ialt;
        das = das < -400 ? -400 : das;
        das = das > 400 ? 400 : das;
        ys = cp.getY() - r * .1 - r * das * dy;
        p1 = new Point2D.Double(cp.getX() - r * .95, cp.getY() - r * 0.92);
        rect = new Rectangle2D.Double(p1.getX(), p1.getY(), r * 1.84, r * 1.72);
        g2.setClip(rect);
        Angle a = new Angle(90 + hi - shdg);
        if (a.getDegrees() < 270 && a.getDegrees() > 135) a.setDegrees(135);
        if (a.getDegrees() > 270 || a.getDegrees() < 45) a.setDegrees(45);
        p0 = new Point2D.Double(cp.getX() - r * .8 + r * 1.4 / 2, cp.getY() + r * .55 + r * 1.4 / 2);
        p1 = getPoint(p0, r * 1.4 / 2, a.getDegrees());
        p2 = getPoint(p1, r * .055, a.getDegreesMinus(30));
        p3 = getPoint(p2, r * .02, a.getDegreesMinus(90));
        p4 = getPoint(p3, r * .045, a.getDegreesMinus(180));
        p5 = getPoint(p1, r * .055, a.getDegreesPlus(30));
        p6 = getPoint(p5, r * .02, a.getDegreesPlus(90));
        p7 = getPoint(p6, r * .045, a.getDegreesPlus(180));
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p7.getX()), (int) Math.round(p7.getY()));
        pol.addPoint((int) Math.round(p6.getX()), (int) Math.round(p6.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        g2.setColor(clrPink);
        g2.draw(pol);
        g2.setColor(clrWhite);
        String s = (String) (nav1ident.getValue());
        if (s.length() > 0) {
            s = s + " / " + Math.round((Double) nav1obs.getValue()) + "�";
        } else {
            s = "" + Math.round((Double) nav1obs.getValue()) + "�";
        }
        p1 = new Point2D.Double(cp.getX() - r * .55, cp.getY() - r * .65);
        drawTextLeft(r, p1, g2, .6, s);
        if ((Double) hasDME1.getValue() == -1.0) {
            p1 = new Point2D.Double(cp.getX() - r * .55, cp.getY() - r * .575);
            drawTextLeft(r, p1, g2, .6, "DME " + dmeNF.format((Double) navDME1.getValue()));
        }
        g2.setClip(oldClip);
    }

    protected synchronized void drawScale(final double r, final Point2D cp, Graphics2D g2) {
        Point2D p0, p1, p2, p3, p4, p5, p6, p7, p8, pc;
        Polygon pol;
        Ellipse2D ell;
        Rectangle2D rect;
        Shape oldClip = g2.getClip();
        Font oldFont = g2.getFont();
        p1 = new Point2D.Double(cp.getX() - r * .9, cp.getY() - r * .65);
        rect = new Rectangle2D.Double((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * 1.1));
        g2.setClip(rect);
        g2.setColor(clrWhite);
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        double dy = 1.1 / 120;
        double ys = cp.getY() - r * .1 + r * ias * dy;
        for (int i = (int) Math.floor(ias / 10) * 10 - 70; i <= Math.floor(ias / 10) * 10 + 70; i += 10) {
            p1 = new Point2D.Double(cp.getX() - r * .66, ys - r * dy * i);
            p2 = new Point2D.Double(cp.getX() - r * .71, ys - r * dy * i);
            if (i < 0) continue;
            g2.draw(new Line2D.Double(p1, p2));
            if (i % 20 == 0) {
                p3 = new Point2D.Double(cp.getX() - r * .73, cp.getY() - r * .06 + r * ias * dy - r * dy * i);
                drawTextRight(r, p3, g2, .5, "" + i);
            }
        }
        double pf = 45;
        double pv = pitch / pf;
        p1 = new Point2D.Double(cp.getX() - r * .55, cp.getY() - r * 0.55);
        RoundRectangle2D rr = new RoundRectangle2D.Double(p1.getX(), p1.getY(), r * .9, r * .9, r * .1, r * .1);
        g2.setClip(rr);
        Angle a = new Angle(-1 * bank);
        Point2D aicp = getPoint(cp, r * .13, 135);
        p1 = getPoint(aicp, r * pv, a.getDegreesPlus(90));
        p1 = getPoint(p1, r * .8, a.getDegrees());
        p2 = getPoint(p1, r * 1.8, a.getDegreesPlus(180));
        p3 = getPoint(p2, r * 180 / pf, a.getDegreesPlus(270));
        p4 = getPoint(p1, r * 180 / pf, a.getDegreesPlus(270));
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        g2.setColor(gndColorAI);
        g2.fill(pol);
        g2.setColor(clrWhite);
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        g2.draw(new Line2D.Double(p1, p2));
        ell = new Ellipse2D.Double(aicp.getX() - r * .35, aicp.getY() - r * .35, r * .7, r * .7);
        g2.setClip(ell);
        g2.setColor(clrWhite);
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        for (int i = -20; i <= 20; i += 10) {
            if (i == 0) continue;
            p1 = getPoint(aicp, r * (pitch + i) / pf, a.getDegreesPlus(90));
            p1 = getPoint(p1, r * .15, a.getDegrees());
            p2 = getPoint(p1, r * .3, a.getDegreesPlus(180));
            g2.draw(new Line2D.Double(p1, p2));
            double majorFontSize = Math.max(6.0, r * .05);
            Font font = oldFont.deriveFont(Font.PLAIN, (float) majorFontSize);
            g2.setFont(font);
            String s = valueToString(i);
            Angle currentAngle = new Angle(-1 * bank);
            TextLayout stl = new TextLayout(s, new Font("Helvetica", 1, (int) Math.round(r * 0.05)), new FontRenderContext(null, true, false));
            AffineTransform tat = new AffineTransform();
            currentAngle = new Angle(360 - currentAngle.getDegrees());
            rect = stl.getBounds();
            tat.translate(aicp.getX() - rect.getWidth() / 2, aicp.getY() + stl.getAscent() / 2.5);
            tat.rotate(Math.toRadians(currentAngle.getDegrees()), rect.getCenterX(), rect.getCenterX());
            tat.translate(r * .2, -r * (pitch + i) / pf);
            Shape ts = stl.getOutline(tat);
            g2.fill(ts);
            tat.translate(-r * .4, 0);
            ts = stl.getOutline(tat);
            g2.fill(ts);
        }
        for (int i = -15; i < 20; i += 10) {
            p1 = getPoint(aicp, r * (pitch + i) / pf, a.getDegreesPlus(90));
            p1 = getPoint(p1, r * .09, a.getDegrees());
            p2 = getPoint(p1, r * .18, a.getDegreesPlus(180));
            g2.draw(new Line2D.Double(p1, p2));
        }
        for (float i = -17.5f; i < 20; i += 5) {
            p1 = getPoint(aicp, r * (pitch + i) / pf, a.getDegreesPlus(90));
            p1 = getPoint(p1, r * .04, a.getDegrees());
            p2 = getPoint(p1, r * .08, a.getDegreesPlus(180));
            g2.draw(new Line2D.Double(p1, p2));
        }
        g2.setClip(rr);
        a = new Angle(-1 * bank);
        p1 = getPoint(aicp, r * .39, a.getDegreesPlus(90));
        p2 = getPoint(p1, r * .055, a.getDegreesPlus(240));
        p3 = getPoint(p1, r * .055, a.getDegreesPlus(300));
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        g2.setColor(skyColorAI);
        g2.fill(pol);
        g2.setColor(clrWhite);
        g2.draw(pol);
        a = new Angle(0);
        p1 = getPoint(aicp, r * .41, a.getDegreesPlus(90));
        p2 = getPoint(p1, r * .05, a.getDegreesPlus(60));
        p3 = getPoint(p1, r * .05, a.getDegreesPlus(120));
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        g2.setColor(clrWhite);
        g2.fill(pol);
        g2.setColor(clrWhite);
        Angle angleCurrent;
        Point2D ap, np;
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        double f = .75;
        for (int a1 = -60; a1 <= 60; a1 += 10) {
            if (a1 == 0 || Math.abs(a1) == 80 || Math.abs(a1) == 70 || Math.abs(a1) == 40 || Math.abs(a1) == 50) continue;
            f = Math.abs(a1) == 10 || Math.abs(a1) == 20 ? .44 : .46;
            angleCurrent = new Angle(a1 + 90);
            ap = getPoint(aicp, r * .41, angleCurrent.getDegrees());
            np = getPoint(aicp, r * f, angleCurrent.getDegrees());
            g2.draw(new Line2D.Double(ap, np));
        }
        g2.setClip(oldClip);
        angleCurrent = new Angle(45 + 90);
        ap = getPoint(aicp, r * .41, angleCurrent.getDegrees());
        np = getPoint(aicp, r * .44, angleCurrent.getDegrees());
        g2.draw(new Line2D.Double(ap, np));
        angleCurrent = new Angle(-45 + 90);
        ap = getPoint(aicp, r * .41, angleCurrent.getDegrees());
        np = getPoint(aicp, r * .44, angleCurrent.getDegrees());
        g2.draw(new Line2D.Double(ap, np));
        rect = new Rectangle2D.Double(aicp.getX() - r * .015, aicp.getY() - r * .015, r * .03, r * .03);
        g2.setColor(clrBlack);
        g2.fill(rect);
        g2.setColor(clrWhite);
        g2.draw(rect);
        p1 = new Point2D.Double(aicp.getX() - r * .16, aicp.getY() - r * .015);
        p2 = new Point2D.Double(aicp.getX() - r * .36, aicp.getY() - r * .015);
        p3 = new Point2D.Double(aicp.getX() - r * .36, aicp.getY() + r * .01);
        p4 = new Point2D.Double(aicp.getX() - r * .19, aicp.getY() + r * .01);
        p5 = new Point2D.Double(aicp.getX() - r * .19, aicp.getY() + r * .030);
        p6 = new Point2D.Double(aicp.getX() - r * .16, aicp.getY() + r * .030);
        p7 = new Point2D.Double(aicp.getX() - r * .16, aicp.getY() - r * .015);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        pol.addPoint((int) Math.round(p6.getX()), (int) Math.round(p6.getY()));
        pol.addPoint((int) Math.round(p7.getX()), (int) Math.round(p7.getY()));
        g2.setColor(clrBlack);
        g2.fill(pol);
        g2.setColor(clrWhite);
        g2.draw(pol);
        p1 = new Point2D.Double(aicp.getX() + r * .16, aicp.getY() - r * .015);
        p2 = new Point2D.Double(aicp.getX() + r * .36, aicp.getY() - r * .015);
        p3 = new Point2D.Double(aicp.getX() + r * .36, aicp.getY() + r * .01);
        p4 = new Point2D.Double(aicp.getX() + r * .19, aicp.getY() + r * .01);
        p5 = new Point2D.Double(aicp.getX() + r * .19, aicp.getY() + r * .030);
        p6 = new Point2D.Double(aicp.getX() + r * .16, aicp.getY() + r * .030);
        p7 = new Point2D.Double(aicp.getX() + r * .16, aicp.getY() - r * .015);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        pol.addPoint((int) Math.round(p6.getX()), (int) Math.round(p6.getY()));
        pol.addPoint((int) Math.round(p7.getX()), (int) Math.round(p7.getY()));
        g2.setColor(clrBlack);
        g2.fill(pol);
        g2.setColor(clrWhite);
        g2.draw(pol);
        if (flightDirOn) {
            g2.setColor(clrPink);
            g2.setStroke(new BasicStroke((int) Math.round(r * 0.01)));
            pf = 270;
            p1 = new Point2D.Double(aicp.getX() - r * .25, aicp.getY() + r * fdpitch / pf);
            p2 = new Point2D.Double(aicp.getX() + r * .25, aicp.getY() + r * fdpitch / pf);
            g2.draw(new Line2D.Double(p1, p2));
            p1 = new Point2D.Double(aicp.getX() + r * -fdbank / pf, aicp.getY() - r * .25);
            p2 = new Point2D.Double(aicp.getX() + r * -fdbank / pf, aicp.getY() + r * .25);
            g2.draw(new Line2D.Double(p1, p2));
        }
        p1 = new Point2D.Double(cp.getX() + r * .45, cp.getY() - r * .65);
        rect = new Rectangle2D.Double((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * 1.1));
        g2.setClip(rect);
        g2.setColor(clrWhite);
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        dy = 1.1 / 800;
        ys = cp.getY() - r * .1 + r * ialt * dy;
        for (int i = (int) Math.floor(ialt / 100) * 100 - 400; i <= Math.floor(ialt / 100) * 100 + 400; i += 100) {
            p1 = new Point2D.Double(cp.getX() + r * .45, ys - r * dy * i);
            p2 = new Point2D.Double(cp.getX() + r * .49, ys - r * dy * i);
            if (i < -1000) continue;
            g2.draw(new Line2D.Double(p1, p2));
            if (i % 200 == 0) {
                p3 = new Point2D.Double(cp.getX() + r * .69, cp.getY() - r * .06 + r * ialt * dy - r * dy * i);
                drawTextRight(r, p3, g2, .5, "" + i);
            }
        }
        p1 = new Point2D.Double(cp.getX() - r * .98, cp.getY() - r * 0.95);
        rect = new Rectangle2D.Double(p1.getX(), p1.getY(), r * 1.9, r * 1.75);
        g2.setClip(rect);
        a = new Angle(0);
        p1 = getPoint(aicp, r * .64, 270);
        p2 = getPoint(p1, r * .05, 60);
        p3 = getPoint(p1, r * .05, 120);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        g2.setColor(clrWhite);
        g2.draw(pol);
        double r0 = proz(r, 63);
        double r1 = proz(r, 65);
        double majorTickSize = proz(r, 3);
        double minorTickSize = proz(r, 2);
        double majorTickR = r1 + 2.5;
        double minorTickR = majorTickR + (majorTickSize - minorTickSize) / 2.0;
        double minValue = 0;
        double maxValue = 359;
        double minorTickStep = 5;
        double majorTickStep = 10;
        double angleStart = 90;
        double angleExtend = 360;
        pc = new Point2D.Double(cp.getX() - r * .1, cp.getY() + r * 1.25);
        if (minorTickStep > 0) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.setStroke(new BasicStroke(1.f));
            double stepAngle = angleExtend * minorTickStep / (maxValue - minValue);
            for (double v = minValue; v <= maxValue; v += minorTickStep) {
                Angle currentAngle = new Angle(angleStart + angleExtend - stepAngle * (v - hi - minValue) / minorTickStep);
                if (currentAngle.getDegrees() < 35 || currentAngle.getDegrees() > 145) continue;
                p0 = getPoint(pc, minorTickR, currentAngle.getDegrees());
                p1 = getPoint(pc, minorTickR + minorTickSize, currentAngle.getDegrees());
                g2.draw(new Line2D.Double(p0, p1));
            }
        }
        if (majorTickStep > 0) {
            double markerR = majorTickR + 2.7 * majorTickSize;
            double l = valueToString(maxValue).length();
            if (l > 3 && decPlaceCount > 0) {
                l--;
            }
            f = l < 4 ? 1.0 : 1.0 - 0.1 * (l - 3);
            double majorFontSize = Math.max(6.0, r * .05 * f);
            g2.setColor(Color.LIGHT_GRAY);
            g2.setStroke(new BasicStroke((int) (r * 0.0075)));
            double stepAngle = angleExtend * majorTickStep / (maxValue - minValue);
            for (double v = minValue; v <= maxValue; v += majorTickStep) {
                Angle currentAngle = new Angle(angleStart + angleExtend - stepAngle * (v - hi - minValue) / majorTickStep);
                if (currentAngle.getDegrees() < 35 || currentAngle.getDegrees() > 145) continue;
                majorFontSize = Math.max(6.0, r * (v % 30 == 0 ? .07 : .05) * f);
                Font font = oldFont.deriveFont(majorFontSize < 8.0 ? Font.PLAIN : Font.BOLD, (float) majorFontSize);
                g2.setFont(font);
                p0 = getPoint(pc, majorTickR, currentAngle.getDegrees());
                p1 = getPoint(pc, majorTickR + majorTickSize, currentAngle.getDegrees());
                g2.draw(new Line2D.Double(p0, p1));
                String s = valueToString(v / 10);
                Dimension size = getStringSize(s, g2);
                double delta = size.getHeight() / 2;
                double fontR = markerR - (v % 30 == 0 ? 3 : 3.25) * majorTickSize;
                Point2D mc = getPoint(pc, fontR - delta, currentAngle.getDegrees());
                g2.drawString(s, (float) (mc.getX() - size.getWidth() / 2), (float) (mc.getY() + size.getHeight() / 2));
            }
        }
        g2.setClip(oldClip);
    }

    protected synchronized void drawNeedle(final double r, double needleR, final Point2D cp, Graphics2D g2) {
    }
}
