package org.gaugebook.gauges;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.VolatileImage;
import java.text.NumberFormat;
import java.util.Locale;
import org.gaugebook.AbstractGauge;
import org.gaugebook.FSXVariable;
import org.gaugebook.GaugeBook;
import org.gaugebook.math.Angle;

public class G1000 extends AbstractGauge {

    private static final Color skyColorAI = new Color(66, 99, 153);

    private static final Color gndColorAI = new Color(74, 68, 43);

    private static final Color clrBlack = new Color(0, 0, 0);

    private static final Color clrWhite = new Color(230, 230, 230);

    private static final Color clrGrey = Color.LIGHT_GRAY;

    private static final Color clrGreen = new Color(108, 168, 107);

    private static final Color clrPink = new Color(168, 108, 167);

    private static final Color clrRed = new Color(222, 0, 0);

    private static final Color clrBlue = new Color(121, 135, 158);

    private FSXVariable varMasterSwitchOn;

    private FSXVariable varCom1Freq;

    private FSXVariable varCom2Freq;

    private FSXVariable varCom1Stby;

    private FSXVariable varCom2Stby;

    private FSXVariable varNav1Freq;

    private FSXVariable varNav2Freq;

    private FSXVariable varNav1Stby;

    private FSXVariable varNav2Stby;

    private FSXVariable varXpdrCode;

    private FSXVariable com1Xmit, com2Xmit, comBoth;

    private FSXVariable indicatedAirspeed;

    private FSXVariable trueAirspeed;

    private FSXVariable selectedAirspeed;

    private FSXVariable selectedMach;

    private FSXVariable indicatedAltitude;

    private FSXVariable selectedAltitude;

    private FSXVariable kohlsmanMB;

    private FSXVariable kohlsmanHG;

    private FSXVariable varAviCircuitOn, varAviMasterOn;

    private FSXVariable verticalSpeed;

    private FSXVariable bankVar;

    private FSXVariable pitchVar;

    private FSXVariable ballVar;

    private FSXVariable headingIndicator, headingLockDir;

    private FSXVariable vorOBS, vorCDI, vorToFrom, vorGSAngle, vorGSFlag;

    private FSXVariable nav1ident, nav1name;

    private FSXVariable nav2ident, nav2name;

    private FSXVariable nav1obs, nav2obs;

    private FSXVariable navHasNav1, navHasNav2, hasDME1, hasDME2, navDME1, navDME2;

    private FSXVariable hsiHasLoc, hsiBearing, hsiBearingValid, hsiCDIValid, hsiCDI, hsiID;

    private FSXVariable vor2Radial;

    private FSXVariable adfFreq, adfRadial, adfSignal, adfIdent;

    private FSXVariable gpsGroundTrueTrack;

    private FSXVariable gpsGroundTrueHeading;

    private FSXVariable gpsGroundMagneticTrack;

    private FSXVariable gpsWPDistance;

    private FSXVariable gpsWPDesiredTrack;

    private FSXVariable gpsWPNextID;

    private FSXVariable gpsApproachType, gpsDrivesNav1;

    private FSXVariable flightDirectorActive;

    private FSXVariable flightDirectorPitch;

    private FSXVariable flightDirectorBank;

    private FSXVariable varLocalTime;

    private FSXVariable varOAT;

    private FSXVariable apHdgLock, apSpdLock, apAltLock;

    private FSXVariable vs0speed, vs1speed, vcspeed, maxmach, asbarberpole;

    private FSXVariable markerI, markerM, markerO;

    private long headingChanged = 0;

    private long obsChanged = 0;

    private int editNav = 1;

    private int editCom = 1;

    private double bank = 0, pitch = 0, fdbank = 0, fdpitch = 0, ias, sas, tas, vs, smach, hi, hld, qnh, ialt, salt, shdg, hdg, obs, cdi, tf, gsa, gsf, vor2r, adfr, com1s, com2s, nav1s, nav2s;

    ;

    double gpsgth, gpswd, gpswdt, gpsgmt, gpsgtt;

    String gpswni;

    private boolean aviOn = false, aviOld = false, flightDirOn = false, hdglock = false, spdlock = false, altlock = false;

    private static NumberFormat dmeNF = NumberFormat.getInstance(Locale.US);

    private static NumberFormat freqNF = NumberFormat.getInstance(Locale.US);

    private static NumberFormat distNF = NumberFormat.getInstance(Locale.US);

    private static final long serialVersionUID = 1L;

    public void init(GaugeBook owner, String title) {
        super.init(owner, title);
        System.out.println("gaugebook size: " + owner.getWidth() + ", " + owner.getHeight());
        varMasterSwitchOn = owner.registerFSXVariable("ELECTRICAL MASTER BATTERY");
        varCom1Freq = owner.registerFSXVariable("COM ACTIVE FREQUENCY:1");
        varCom2Freq = owner.registerFSXVariable("COM ACTIVE FREQUENCY:2");
        varCom1Stby = owner.registerFSXVariable("COM STANDBY FREQUENCY:1");
        varCom2Stby = owner.registerFSXVariable("COM STANDBY FREQUENCY:2");
        varNav1Freq = owner.registerFSXVariable("NAV ACTIVE FREQUENCY:1");
        varNav2Freq = owner.registerFSXVariable("NAV ACTIVE FREQUENCY:2");
        varNav1Stby = owner.registerFSXVariable("NAV STANDBY FREQUENCY:1");
        varNav2Stby = owner.registerFSXVariable("NAV STANDBY FREQUENCY:2");
        com1Xmit = owner.registerFSXVariable("COM TRANSMIT:1");
        com2Xmit = owner.registerFSXVariable("COM TRANSMIT:2");
        comBoth = owner.registerFSXVariable("COM RECIEVE ALL");
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
        vor2Radial = owner.registerFSXVariable("NAV RADIAL:2");
        adfRadial = owner.registerFSXVariable("ADF RADIAL:1");
        varXpdrCode = owner.registerFSXVariable("TRANSPONDER CODE:1");
        varLocalTime = owner.registerFSXVariable("LOCAL TIME");
        varOAT = owner.registerFSXVariable("AMBIENT TEMPERATURE");
        gpsGroundTrueTrack = owner.registerFSXVariable("GPS GROUND TRUE TRACK");
        gpsGroundTrueHeading = owner.registerFSXVariable("GPS GROUND TRUE HEADING");
        gpsGroundMagneticTrack = owner.registerFSXVariable("GPS GROUND MAGNETIC TRACK");
        adfFreq = owner.registerFSXVariable("ADF ACTIVE FREQUENCY:1");
        adfSignal = owner.registerFSXVariable("ADF SIGNAL:1");
        adfIdent = owner.registerFSXVariable("ADF IDENT");
        gpsWPDistance = owner.registerFSXVariable("GPS WP DISTANCE");
        gpsWPDesiredTrack = owner.registerFSXVariable("GPS WP DESIRED TRACK");
        gpsWPNextID = owner.registerFSXVariable("GPS WP NEXT ID");
        gpsApproachType = owner.registerFSXVariable("GPS APPROACH APPROACH TYPE");
        varCom1Freq = owner.registerFSXVariable("COM ACTIVE FREQUENCY:1");
        varCom2Freq = owner.registerFSXVariable("COM ACTIVE FREQUENCY:2");
        varCom1Stby = owner.registerFSXVariable("COM STANDBY FREQUENCY:1");
        varCom2Stby = owner.registerFSXVariable("COM STANDBY FREQUENCY:2");
        varNav1Freq = owner.registerFSXVariable("NAV ACTIVE FREQUENCY:1");
        varNav2Freq = owner.registerFSXVariable("NAV ACTIVE FREQUENCY:2");
        varNav1Stby = owner.registerFSXVariable("NAV STANDBY FREQUENCY:1");
        varNav2Stby = owner.registerFSXVariable("NAV STANDBY FREQUENCY:2");
        navHasNav1 = owner.registerFSXVariable("NAV HAS NAV:1");
        navHasNav2 = owner.registerFSXVariable("NAV HAS NAV:2");
        nav1ident = owner.registerFSXVariable("NAV IDENT:1");
        nav1name = owner.registerFSXVariable("NAV NAME:1");
        nav2ident = owner.registerFSXVariable("NAV IDENT:2");
        nav2name = owner.registerFSXVariable("NAV NAME:2");
        nav1obs = owner.registerFSXVariable("NAV OBS:1");
        nav2obs = owner.registerFSXVariable("NAV OBS:2");
        gpsDrivesNav1 = owner.registerFSXVariable("GPS DRIVES NAV1");
        hsiHasLoc = owner.registerFSXVariable("HSI HAS LOCALIZER");
        hsiBearing = owner.registerFSXVariable("HSI BEARING");
        hsiBearingValid = owner.registerFSXVariable("HSI BEARING VALID");
        hsiCDI = owner.registerFSXVariable("HSI CDI NEEDLE");
        hsiCDIValid = owner.registerFSXVariable("HSI CDI NEEDLE VALID");
        hsiID = owner.registerFSXVariable("HSI STATION IDENT");
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
        vs0speed = owner.registerFSXVariable("DESIGN SPEED VS0");
        vs1speed = owner.registerFSXVariable("DESIGN SPEED VS1");
        vcspeed = owner.registerFSXVariable("DESIGN SPEED VC");
        asbarberpole = owner.registerFSXVariable("AIRSPEED BARBER POLE");
        maxmach = owner.registerFSXVariable("MACH MAX OPERATE");
        varAviMasterOn = owner.registerFSXVariable("AVIONICS MASTER SWITCH");
        varAviCircuitOn = owner.registerFSXVariable("CIRCUIT AVIONICS ON");
        markerI = owner.registerFSXVariable("INNER MARKER");
        markerM = owner.registerFSXVariable("MIDDLE MARKER");
        markerO = owner.registerFSXVariable("OUTER MARKER");
        freqNF.setMaximumFractionDigits(2);
        freqNF.setMinimumFractionDigits(2);
        dmeNF.setMaximumFractionDigits(1);
        dmeNF.setMinimumFractionDigits(1);
        dmeNF.setMaximumIntegerDigits(2);
        dmeNF.setMinimumIntegerDigits(1);
        distNF.setMaximumFractionDigits(1);
        distNF.setMinimumFractionDigits(1);
    }

    public void update() {
        try {
            if (shdg != headingLockDir.getDoubleValue()) {
                headingChanged = System.currentTimeMillis();
            }
            if (obs != vorOBS.getDoubleValue()) {
                obsChanged = System.currentTimeMillis();
            }
            if (com1s != varCom1Stby.getDoubleValue()) editCom = 1; else if (com2s != varCom2Stby.getDoubleValue()) editCom = 2;
            if (nav1s != varNav1Stby.getDoubleValue()) editNav = 1; else if (nav2s != varNav2Stby.getDoubleValue()) editNav = 2;
            ialt = (Double) indicatedAltitude.getValue();
            ias = (Double) indicatedAirspeed.getValue();
            sas = (Double) selectedAirspeed.getValue();
            tas = (Double) trueAirspeed.getValue();
            vs = (Double) verticalSpeed.getValue() * 60;
            smach = (Double) selectedMach.getValue();
            hi = (180 / StrictMath.PI) * (Double) headingIndicator.getValue();
            hld = (Double) headingLockDir.getValue();
            salt = (Double) selectedAltitude.getValue();
            pitch = (180 / StrictMath.PI) * (Double) pitchVar.getValue();
            bank = (180 / StrictMath.PI) * (Double) bankVar.getValue();
            fdpitch = (180 / StrictMath.PI) * (Double) flightDirectorPitch.getValue();
            fdbank = (180 / StrictMath.PI) * (Double) flightDirectorBank.getValue();
            qnh = (Double) kohlsmanMB.getValue();
            hdg = hi;
            shdg = (Double) headingLockDir.getValue();
            gpsgtt = 180 / StrictMath.PI * (Double) gpsGroundTrueTrack.getValue();
            gpsgth = 180 / StrictMath.PI * (Double) gpsGroundTrueHeading.getValue();
            gpsgmt = 180 / StrictMath.PI * (Double) gpsGroundMagneticTrack.getValue();
            gpswd = (Double) gpsWPDistance.getValue() / 1852.216;
            gpswdt = 180 / StrictMath.PI * (Double) gpsWPDesiredTrack.getValue();
            gpswni = (String) gpsWPNextID.getValue();
            obs = (Double) vorOBS.getValue();
            cdi = (Double) vorCDI.getValue();
            tf = (Double) vorToFrom.getValue();
            gsa = (Double) vorGSAngle.getValue();
            gsf = (Double) vorGSFlag.getValue();
            adfr = (Double) adfRadial.getValue();
            vor2r = (Double) vor2Radial.getValue();
            com1s = varCom1Stby.getDoubleValue();
            com2s = varCom2Stby.getDoubleValue();
            nav1s = varNav1Stby.getDoubleValue();
            nav2s = varNav2Stby.getDoubleValue();
            aviOn = varMasterSwitchOn.getBooleanValue() && varAviMasterOn.getBooleanValue() && varAviCircuitOn.getBooleanValue();
            flightDirOn = (Double) flightDirectorActive.getValue() == 1;
            hdglock = (Double) apHdgLock.getValue() == 1;
            spdlock = (Double) apSpdLock.getValue() == 1;
            altlock = (Double) apAltLock.getValue() == 1;
        } catch (NullPointerException e) {
        }
    }

    private int lastWidth = 0;

    private int lastHeight = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Graphics g) {
        if (skipFrame) return;
        skipFrame = true;
        update();
        int w = getWidth();
        int h = getHeight();
        GraphicsConfiguration gc = getGraphicsConfiguration();
        do {
            gc = getGraphicsConfiguration();
            if (lastWidth != w || lastHeight != h || vi == null || vi.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
                vi = gc.createCompatibleVolatileImage(w, h, gc.getColorModel().getTransparency());
                lastWidth = w;
                lastHeight = h;
            }
            Graphics2D g2 = vi.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, (antialias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF));
            double r = Math.min(w, h) / 2 - 5;
            if (r < 0) r = 100;
            Point2D cp = new Point2D.Double(w / 2, h / 2);
            g2.setClip(0, 0, w, h);
            drawBackground(r, cp, g2);
            if (!initdone) return;
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
        g.drawImage(vi, 0, 0, this);
        skipFrame = false;
    }

    protected void drawBackground(final double r, final Point2D cp, Graphics2D g2) {
        Point2D p1, p2;
        int w = getWidth();
        int h = getHeight();
        GraphicsConfiguration gc = getGraphicsConfiguration();
        if (w != lastWidth || h != lastHeight || backVi == null || backVi.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
            lastWidth = w;
            lastHeight = h;
            backVi = gc.createCompatibleVolatileImage(w, h);
        }
        do {
            gc = this.getGraphicsConfiguration();
            if (backVi.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
                backVi = gc.createCompatibleVolatileImage(w, h);
            }
            Graphics2D bg2 = backVi.createGraphics();
            bg2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, (antialias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF));
            bg2.setClip(0, 0, w, h);
            bg2.setColor(Color.DARK_GRAY);
            bg2.fillRect(0, 0, w, h);
            bg2.setColor(clrBlack);
            p1 = new Point2D.Double(cp.getX() - r * 1.75, cp.getY() - r * 1);
            Rectangle2D.Double rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * 3.5, r * 2);
            bg2.fill(rr);
            bg2.setClip(rr);
            if (aviOn && initdone) {
                bg2.setColor(gndColorAI);
                p1 = new Point2D.Double(cp.getX() - r * 1.4, cp.getY() - r * .85);
                rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * 2.8, r * 1.7);
                bg2.fill(rr);
                bg2.setColor(new Color(200, 204, 208));
                p1 = new Point2D.Double(cp.getX() - r * 1.34, cp.getY() - r * .925);
                drawText(r, p1, bg2, .4, "NAV1");
                p1 = new Point2D.Double(cp.getX() - r * 1.34, cp.getY() - r * .852);
                drawText(r, p1, bg2, .4, "NAV2");
                p1 = new Point2D.Double(cp.getX() + r * 1.39, cp.getY() - r * .925);
                drawTextRight(r, p1, bg2, .4, "COM1");
                p1 = new Point2D.Double(cp.getX() + r * 1.39, cp.getY() - r * .852);
                drawTextRight(r, p1, bg2, .4, "COM2");
                bg2.setColor(new Color(108, 110, 112));
                p1 = new Point2D.Double(cp.getX() - r * 1.4, cp.getY() - r * .85);
                p2 = new Point2D.Double(cp.getX() + r * 1.4, cp.getY() - r * .85);
                bg2.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                p1 = new Point2D.Double(cp.getX() - r * .7, cp.getY() - r * 1);
                p2 = new Point2D.Double(cp.getX() - r * .7, cp.getY() - r * .85);
                bg2.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                p1 = new Point2D.Double(cp.getX() + r * .77, cp.getY() - r * 1);
                p2 = new Point2D.Double(cp.getX() + r * .77, cp.getY() - r * .85);
                bg2.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                p1 = new Point2D.Double(cp.getX() - r * .7, cp.getY() - r * .925);
                p2 = new Point2D.Double(cp.getX() + r * .77, cp.getY() - r * .925);
                bg2.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                bg2.setColor(new Color(200, 204, 208));
                String[] txt = { "WPT", "DIS", "DTK", "TRK" };
                double delta = r * 1.47 / 4;
                for (int i = 0; i < 4; i++) {
                    p1 = new Point2D.Double(cp.getX() - r * .68 + i * delta, cp.getY() - r * .925);
                    drawTextLeft(r, p1, bg2, .45, txt[i]);
                }
                g2.setColor(new Color(150, 150, 150));
                double d = 2.8 / 12;
                p1 = new Point2D.Double(cp.getX() - r * 1.4, cp.getY() + r * .79);
                Rectangle2D rect = new Rectangle2D.Double(p1.getX(), p1.getY(), r * d, r * .06);
                bg2.setColor(Color.BLACK);
                bg2.fill(rect);
                bg2.setColor(Color.LIGHT_GRAY);
                p2 = new Point2D.Double(cp.getX() - r * 1.4 + r * d, cp.getY() + r * .79);
                bg2.draw(new Line2D.Double(p1, p2));
                p1 = new Point2D.Double(cp.getX() - r * 1.4 + r * d, cp.getY() + r * .85);
                bg2.draw(new Line2D.Double(p1, p2));
                p1 = new Point2D.Double(cp.getX() + r * 1.4 - r * d * 3, cp.getY() + r * .79);
                rect = new Rectangle2D.Double(p1.getX(), p1.getY(), r * d * 3, r * .06);
                bg2.setColor(Color.BLACK);
                bg2.fill(rect);
                bg2.setColor(Color.LIGHT_GRAY);
                p2 = new Point2D.Double(cp.getX() + r * 1.4, cp.getY() + r * .79);
                bg2.draw(new Line2D.Double(p1, p2));
                p1 = new Point2D.Double(cp.getX() + r * 1.4 - r * d * 3, cp.getY() + r * .85);
                p2 = new Point2D.Double(cp.getX() + r * 1.4 - r * d * 3, cp.getY() + r * .79);
                bg2.draw(new Line2D.Double(p1, p2));
                p1 = new Point2D.Double(cp.getX() + r * 1.4 - r * d * 1.5, cp.getY() + r * .85);
                p2 = new Point2D.Double(cp.getX() + r * 1.4 - r * d * 1.5, cp.getY() + r * .79);
                bg2.draw(new Line2D.Double(p1, p2));
                p1 = new Point2D.Double(cp.getX() - r * 1.4, cp.getY() + r * .85);
                p2 = new Point2D.Double(cp.getX() + r * 1.4, cp.getY() + r * .85);
                bg2.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                delta = r * 2.8 / 12;
                for (int i = 1; i < 12; i++) {
                    p1 = new Point2D.Double(cp.getX() - r * 1.4 + i * delta, cp.getY() + r * .85);
                    p2 = new Point2D.Double(cp.getX() - r * 1.4 + i * delta, cp.getY() + r * .91);
                    bg2.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                }
            }
            bg2.dispose();
        } while (backVi.contentsLost());
        g2.drawImage(backVi, 0, 0, this);
    }

    protected synchronized void drawText(final double r, final Point2D cp, Graphics2D g2) {
    }

    protected void drawValue(final double r, final double needleR, final Point2D cp, Graphics2D g2) {
        Point2D p0, p1, p2, p3, p4, p5, p6, p7;
        Polygon pol;
        Rectangle2D rect;
        Shape oldClip = g2.getClip();
        Point2D aicp = getPoint(cp, r * .325, 90);
        double heightfactor = .9;
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        p1 = new Point2D.Double(aicp.getX() - r * .65, aicp.getY());
        p2 = getPoint(p1, r * .03, 135);
        p3 = getPoint(p2, r * .03, 90);
        p4 = getPoint(p3, r * .21, 180);
        p5 = getPoint(p4, r * .095, 270);
        p6 = getPoint(p5, r * .21, 0);
        p7 = getPoint(p6, r * .03, 90);
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
        g2.setColor(Color.GRAY);
        g2.draw(pol);
        double dy = heightfactor / 120;
        double das = sas - ias;
        das = das < -60 ? -60 : das;
        das = das > 60 ? 60 : das;
        double ys = aicp.getY() - r * das * dy;
        p1 = new Point2D.Double(aicp.getX() - r * .65, ys);
        p2 = getPoint(p1, r * .035, 45);
        p3 = getPoint(p2, r * .05, 270);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        g2.setColor(Color.LIGHT_GRAY);
        g2.fill(pol);
        g2.setColor(clrWhite);
        p1 = new Point2D.Double(aicp.getX() - r * .7, cp.getY() - r * .27);
        drawTextRight(r, p1, g2, .75, "" + Math.round(ias));
        g2.setColor(clrWhite);
        p1 = new Point2D.Double(aicp.getX() - r * .88, aicp.getY() + r * heightfactor / 1.58);
        drawTextLeft(r, p1, g2, .5, "TAS");
        p1 = new Point2D.Double(aicp.getX() - r * .66, aicp.getY() + r * heightfactor / 1.58);
        drawTextRight(r, p1, g2, .5, "" + Math.round(tas));
        p1 = new Point2D.Double(aicp.getX() + r * .65, aicp.getY());
        p2 = getPoint(p1, r * .03, 45);
        p3 = getPoint(p2, r * .03, 90);
        p4 = getPoint(p3, r * .21, 0);
        p5 = getPoint(p4, r * .095, 270);
        p6 = getPoint(p5, r * .21, 180);
        p7 = getPoint(p6, r * .03, 90);
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
        g2.setColor(Color.GRAY);
        g2.draw(pol);
        g2.setColor(clrWhite);
        p1 = new Point2D.Double(aicp.getX() + r * .88, aicp.getY() + r * .05);
        int ia = (int) Math.round(ialt / 20) * 20;
        drawTextRight(r, p1, g2, .65, "" + ia);
        g2.setColor(clrWhite);
        p1 = new Point2D.Double(cp.getX() + r * .88, cp.getY() + r * .25);
        drawTextRight(r, p1, g2, .5, Math.round(qnh) + " mb");
        g2.setColor(clrWhite);
        p1 = new Point2D.Double(aicp.getX() + r * .78, aicp.getY() - r * .39);
        drawText(r, p1, g2, .5, Math.round(salt) + "");
        Angle a = new Angle(90 + hi - shdg);
        Point2D pc = new Point2D.Double(cp.getX(), cp.getY() + r * .45);
        p0 = new Point2D.Double(pc.getX(), pc.getY());
        p1 = getPoint(p0, r * .35, a.getDegrees());
        p2 = getPoint(p1, r * .04, a.getDegreesMinus(30));
        p3 = getPoint(p2, r * .02, a.getDegreesMinus(90));
        p4 = getPoint(p3, r * .04, a.getDegreesMinus(180));
        p5 = getPoint(p1, r * .04, a.getDegreesPlus(30));
        p6 = getPoint(p5, r * .02, a.getDegreesPlus(90));
        p7 = getPoint(p6, r * .04, a.getDegreesPlus(180));
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p7.getX()), (int) Math.round(p7.getY()));
        pol.addPoint((int) Math.round(p6.getX()), (int) Math.round(p6.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        g2.setColor(new Color(164, 189, 200));
        g2.fill(pol);
        p1 = new Point2D.Double(cp.getX() - r * .075, cp.getY() - r * .01);
        rect = new Rectangle2D.Double(p1.getX(), p1.getY(), r * 0.15, r * .07);
        g2.setColor(Color.BLACK);
        g2.fill(rect);
        g2.setColor(Color.LIGHT_GRAY);
        g2.draw(rect);
        p1 = new Point2D.Double(cp.getX(), cp.getY() + r * .07);
        drawText(r, p1, g2, .6, "" + Math.round(hi) + "�");
        g2.setClip(oldClip);
    }

    protected synchronized void drawScale(final double r, final Point2D cp, Graphics2D g2) {
        Point2D p0, p1, p2, p3, p4, p5, p6, p7, p8, pc;
        Polygon pol;
        Ellipse2D ell;
        Rectangle2D rect;
        Shape oldClip = g2.getClip();
        Font oldFont = g2.getFont();
        Point2D aicp = getPoint(cp, r * .325, 90);
        Point2D hsicp = new Point2D.Double(cp.getX(), cp.getY() + r * .45);
        double pf = 45;
        double pv = pitch / pf;
        p1 = new Point2D.Double(cp.getX() - r * 1.4, cp.getY() - r * 0.85);
        Rectangle2D rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * 2.8, r * 1.7);
        g2.setClip(rr);
        Angle a = new Angle(-1 * bank);
        p1 = getPoint(aicp, r * pv, a.getDegreesPlus(90));
        p1 = getPoint(p1, r * 1.8, a.getDegrees());
        p2 = getPoint(p1, r * 3.6, a.getDegreesPlus(180));
        p3 = getPoint(p2, r * 180 / pf, a.getDegreesPlus(90));
        p4 = getPoint(p1, r * 180 / pf, a.getDegreesPlus(90));
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        g2.setColor(skyColorAI);
        g2.fill(pol);
        g2.setColor(new Color(181, 183, 189));
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        g2.draw(new Line2D.Double(p1, p2));
        ell = new Ellipse2D.Double(aicp.getX() - r * .35, aicp.getY() - r * .35, r * .7, r * .7);
        g2.setClip(ell);
        g2.setColor(clrWhite);
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        double majorFontSize = Math.max(6.0, r * .05);
        Font font = oldFont.deriveFont(Font.PLAIN, (float) majorFontSize);
        g2.setFont(font);
        Angle currentAngle = new Angle(-1 * bank);
        TextLayout stl;
        Shape ts;
        AffineTransform tat = new AffineTransform();
        for (int i = -80; i <= 80; i += 10) {
            if (i == 0) continue;
            p1 = getPoint(aicp, r * (pitch + i) / pf, a.getDegreesPlus(90));
            p1 = getPoint(p1, r * .15, a.getDegrees());
            p2 = getPoint(p1, r * .3, a.getDegreesPlus(180));
            g2.draw(new Line2D.Double(p1, p2));
            String s = valueToString(i);
            currentAngle = new Angle(-1 * bank);
            stl = new TextLayout(s, new Font("Helvetica", 1, (int) Math.round(r * 0.05)), new FontRenderContext(null, true, false));
            tat = new AffineTransform();
            currentAngle = new Angle(360 - currentAngle.getDegrees());
            rect = stl.getBounds();
            tat.translate(aicp.getX() - rect.getWidth() / 2, aicp.getY() + stl.getAscent() / 2.5);
            tat.rotate(Math.toRadians(currentAngle.getDegrees()), rect.getCenterX(), rect.getCenterX());
            tat.translate(r * .2, -r * (pitch + i) / pf);
            ts = stl.getOutline(tat);
            g2.fill(ts);
            tat.translate(-r * .4, 0);
            ts = stl.getOutline(tat);
            g2.fill(ts);
        }
        for (int i = -35; i < 40; i += 10) {
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
        a = new Angle(0);
        p1 = getPoint(aicp, r * .41, a.getDegreesPlus(90));
        p2 = getPoint(p1, r * .055, a.getDegreesPlus(60));
        p3 = getPoint(p1, r * .055, a.getDegreesPlus(120));
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        g2.setColor(clrWhite);
        g2.fill(pol);
        a = new Angle(-bank);
        p1 = getPoint(aicp, r * .39, a.getDegreesPlus(90));
        p2 = getPoint(p1, r * .055, a.getDegreesPlus(240));
        p3 = getPoint(p1, r * .055, a.getDegreesPlus(300));
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        g2.setColor(clrWhite);
        g2.fill(pol);
        double ball = (Double) ballVar.getValue();
        Angle angleCurrent = new Angle(-bank + 90);
        p1 = getPoint(aicp, r * .325, angleCurrent.getDegrees());
        p2 = getPoint(p1, -ball / 1024 * r, angleCurrent.plus(90));
        p3 = getPoint(p2, r * .03, angleCurrent.plus(90));
        p4 = getPoint(p2, r * .03, angleCurrent.plus(270));
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.01)));
        g2.draw(new Line2D.Double(p3, p4));
        g2.setColor(clrWhite);
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
        ap = getPoint(aicp, r * .41, 135);
        np = getPoint(aicp, r * .44, 135);
        g2.draw(new Line2D.Double(ap, np));
        ap = getPoint(aicp, r * .41, 45);
        np = getPoint(aicp, r * .44, 45);
        g2.draw(new Line2D.Double(ap, np));
        Arc2D arc = new Arc2D.Double(new Rectangle2D.Double(aicp.getX() - r * .41, aicp.getY() - r * .41, r * .82, r * .82), 30, 120, Arc2D.OPEN);
        g2.draw(arc);
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
        double heightfactor = .9;
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        double width = r * .035;
        double dy = heightfactor / 120;
        double fps_kts = 0.592483801;
        double ys = aicp.getY() + r * ias * dy;
        g2.setColor(Color.LIGHT_GRAY);
        p1 = new Point2D.Double(aicp.getX() - r * .65 - r * .25, aicp.getY() - r * .4);
        rect = new Rectangle2D.Double((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * heightfactor));
        g2.setClip(rect);
        double h0 = (Double) vs0speed.getValue() * r * fps_kts * dy;
        p1 = new Point2D.Double(aicp.getX() - r * .675, ys - h0);
        rect = new Rectangle2D.Double(p1.getX(), p1.getY(), width, h0);
        g2.setColor(new Color(165, 55, 55));
        g2.fill(rect);
        double h1 = (Double) vs1speed.getValue() * r * fps_kts * dy;
        p1 = new Point2D.Double(aicp.getX() - r * .675, ys - (h0 + h1));
        rect = new Rectangle2D.Double(p1.getX(), p1.getY(), width, h1);
        g2.setColor(new Color(111, 111, 111));
        g2.fill(rect);
        double mach_kts = 666.74;
        double hg = (Double) maxmach.getValue() * mach_kts * r * dy;
        p1 = new Point2D.Double(aicp.getX() - r * .675, ys - (h0 + h1 + hg));
        rect = new Rectangle2D.Double(p1.getX(), p1.getY(), width, hg);
        g2.setColor(new Color(88, 148, 88));
        g2.fill(rect);
        g2.setClip(oldClip);
        double hm = (Double) asbarberpole.getValue() * r * dy;
        p1 = new Point2D.Double(aicp.getX() - r * .695, ys - (h0 + h1 + hg + hm));
        rect = new Rectangle2D.Double(p1.getX(), p1.getY(), width, hm);
        g2.setColor(new Color(165, 55, 55));
        g2.fill(rect);
        g2.setClip(oldClip);
        g2.setColor(Color.LIGHT_GRAY);
        p1 = new Point2D.Double(aicp.getX() - r * .65 - r * .25, aicp.getY() - r * .4);
        rect = new Rectangle2D.Double((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * heightfactor));
        g2.draw(rect);
        g2.setClip(rect);
        p1 = new Point2D.Double(aicp.getX() - r * .65, aicp.getY() - r * .35);
        rect = new Rectangle2D.Double((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * .9));
        for (int i = (int) Math.floor(ias / 10) * 10 - 70; i <= Math.floor(ias / 10) * 10 + 70; i += 10) {
            p1 = new Point2D.Double(aicp.getX() - r * .65, ys - r * dy * i);
            p2 = new Point2D.Double(aicp.getX() - r * (i % 20 == 0 ? .69 : .675), ys - r * dy * i);
            if (i < 0) continue;
            g2.draw(new Line2D.Double(p1, p2));
            if (i % 20 == 0) {
                p3 = new Point2D.Double(aicp.getX() - r * .7, aicp.getY() + r * .045 + r * ias * dy - r * dy * i);
                drawTextRight(r, p3, g2, .6, "" + i);
            }
        }
        g2.setClip(oldClip);
        p1 = new Point2D.Double(aicp.getX() + r * .65, aicp.getY() - r * .4);
        rect = new Rectangle2D.Double((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * heightfactor));
        g2.draw(rect);
        g2.setClip(rect);
        p1 = new Point2D.Double(cp.getX() + r * .88, cp.getY() - r * .75);
        drawTextRight(r, p1, g2, .6, "" + Math.round(salt));
        dy = heightfactor / 800;
        double das = salt - ialt;
        das = das < -400 ? -400 : das;
        das = das > 400 ? 400 : das;
        ys = cp.getY() - r * .3 - r * das * dy;
        p1 = new Point2D.Double(aicp.getX() + r * .66, ys);
        p2 = getPoint(p1, r * .03, 45);
        p3 = getPoint(p2, r * .02, 90);
        p4 = getPoint(p3, r * .05, 180);
        p5 = getPoint(p4, r * .082, 270);
        p6 = getPoint(p5, r * .05, 0);
        p7 = getPoint(p6, r * .02, 90);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        pol.addPoint((int) Math.round(p6.getX()), (int) Math.round(p6.getY()));
        pol.addPoint((int) Math.round(p7.getX()), (int) Math.round(p7.getY()));
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        g2.setColor(new Color(159, 186, 195));
        g2.fill(pol);
        g2.setClip(oldClip);
        pc = new Point2D.Double(cp.getX(), cp.getY() + r * .45);
        g2.setColor(new Color(65, 69, 55));
        ell = new Ellipse2D.Double((int) pc.getX() - r * .35, (int) pc.getY() - r * .35, (int) (r * .7), (int) (r * .7));
        g2.fill(ell);
        double r0 = proz(r, 30);
        double r1 = proz(r, 32);
        double majorTickSize = proz(r, 3);
        double minorTickSize = proz(r, 2);
        double majorTickR = r1 + 2.5;
        double minorTickR = majorTickR + (majorTickSize - minorTickSize) / 2.0;
        double minValue = 0;
        double maxValue = 359;
        double minorTickStep = 5;
        double majorTickStep = 30;
        double angleStart = 90;
        double angleExtend = 360;
        if (minorTickStep > 0) {
            g2.setColor(clrWhite);
            g2.setStroke(new BasicStroke(1.f));
            double stepAngle = angleExtend * minorTickStep / (maxValue - minValue);
            for (double v = minValue; v <= maxValue; v += minorTickStep) {
                currentAngle = new Angle(angleStart + angleExtend - stepAngle * (v - hi - minValue) / minorTickStep);
                p0 = getPoint(pc, minorTickR, currentAngle.getDegrees());
                p1 = getPoint(pc, minorTickR + minorTickSize, currentAngle.getDegrees());
                g2.draw(new Line2D.Double(p0, p1));
            }
        }
        if (majorTickStep > 0) {
            double l = valueToString(maxValue).length();
            if (l > 3 && decPlaceCount > 0) {
                l--;
            }
            f = l < 4 ? 1.0 : 1.0 - 0.1 * (l - 3);
            majorFontSize = Math.max(6.0, r * .05 * f);
            g2.setColor(clrWhite);
            g2.setStroke(new BasicStroke((int) (r * 0.0075)));
            double stepAngle = angleExtend * majorTickStep / (maxValue - minValue);
            for (double v = minValue; v <= maxValue; v += majorTickStep) {
                currentAngle = new Angle(90 - angleStart + angleExtend - stepAngle * (v - hi - minValue) / majorTickStep);
                p0 = getPoint(pc, majorTickR, currentAngle.getDegrees());
                p1 = getPoint(pc, majorTickR + majorTickSize, currentAngle.getDegrees());
                g2.draw(new Line2D.Double(p0, p1));
                String s = valueToString(v / 10);
                switch((int) (v / 10)) {
                    case 0:
                        s = "N";
                        break;
                    case 9:
                        s = "E";
                        break;
                    case 18:
                        s = "S";
                        break;
                    case 27:
                        s = "W";
                        break;
                    default:
                        break;
                }
                stl = new TextLayout(s, new Font("Helvetica", 1, (int) Math.round(r * 0.05)), new FontRenderContext(null, true, false));
                tat = new AffineTransform();
                currentAngle = new Angle(360 - currentAngle.getDegrees() - 135);
                rect = stl.getBounds();
                tat.translate(pc.getX() - rect.getWidth() / 2, pc.getY() - stl.getAscent() / 2);
                tat.rotate(currentAngle.getRadians(), rect.getCenterX(), rect.getCenterX());
                tat.translate(r * .17, r * .17);
                tat.rotate(Math.toRadians(135), rect.getCenterX(), rect.getCenterX());
                ts = stl.getOutline(tat);
                g2.fill(ts);
            }
        }
        g2.setClip(oldClip);
        g2.setStroke(new BasicStroke((int) (r * 0.005)));
        a = new Angle(0);
        p1 = getPoint(cp, r * 0.105, 270);
        p2 = getPoint(p1, r * .05, 60);
        p3 = getPoint(p1, r * .05, 120);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        g2.setColor(clrWhite);
        g2.fill(pol);
        double vsi = vs > 2000 ? 2000 : vs;
        vsi = vsi < -2000 ? -2000 : vsi;
        double vsf = (.225 / 1.5) * (vsi / 1000);
        p1 = new Point2D.Double(aicp.getX() + r * .65, aicp.getY());
        if (vsi > 0) {
            rect = new Rectangle2D.Double(p1.getX(), p1.getY() - vsf * r, r * .02, vsf * r);
        } else {
            rect = new Rectangle2D.Double(p1.getX(), p1.getY() * r, r * .02, -vsf * r);
        }
        g2.setColor(new Color(142, 135, 151));
        g2.fill(rect);
        p1 = new Point2D.Double(aicp.getX() + r * .65, aicp.getY() - r * .4);
        rect = new Rectangle2D.Double((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * heightfactor));
        g2.setClip(rect);
        g2.setColor(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        dy = .9 / 800;
        ys = aicp.getY() + r * ialt * dy;
        for (int i = (int) Math.floor(ialt / 100) * 100 - 400; i <= Math.floor(ialt / 100) * 100 + 400; i += 100) {
            p1 = new Point2D.Double(aicp.getX() + r * .65, ys - r * dy * i);
            p2 = new Point2D.Double(aicp.getX() + r * .69, ys - r * dy * i);
            if (i < -1000) continue;
            g2.draw(new Line2D.Double(p1, p2));
            if (i % 200 == 0) {
                p3 = new Point2D.Double(aicp.getX() + r * .77, aicp.getY() + r * .045 + r * ialt * dy - r * dy * i);
                drawText(r, p3, g2, .5, "" + i);
            }
        }
        g2.setClip(oldClip);
        g2.setColor(Color.LIGHT_GRAY);
        p1 = getPoint(aicp, r * 0.9, 0);
        p2 = getPoint(p1, r * .1, 25);
        p3 = getPoint(p1, r * .1, 335);
        drawLine(p1, p2, g2);
        drawLine(p1, p3, g2);
        p4 = getPoint(p2, r * .3, 90);
        p5 = getPoint(p3, r * .3, 270);
        drawLine(p2, p4, g2);
        drawLine(p3, p5, g2);
        p2 = getPoint(p4, r * .09, 180);
        p3 = getPoint(p5, r * .09, 180);
        drawLine(p2, p4, g2);
        drawLine(p3, p5, g2);
        double h = .225 / 3 * r;
        int len;
        for (int i = 0; i <= 4; i++) {
            if (i == 0) continue;
            if (i % 2 == 0) {
                len = (int) Math.round(r * .04);
                p1 = new Point2D.Double(aicp.getX() + r * .95, aicp.getY() - i * h + r * .0375);
                drawTextLeft(r, p1, g2, .5, "" + (i / 2));
                p1 = new Point2D.Double(aicp.getX() + r * .95, aicp.getY() + i * h + r * .0375);
                drawTextLeft(r, p1, g2, .5, "" + (i / 2));
            } else {
                len = (int) Math.round(r * .02);
            }
            p1 = new Point2D.Double(aicp.getX() + r * .9, aicp.getY() - i * h);
            p2 = new Point2D.Double(aicp.getX() + r * .9 + len, aicp.getY() - i * h);
            drawLine(p1, p2, g2);
            p1 = new Point2D.Double(aicp.getX() + r * .9, aicp.getY() + i * h);
            p2 = new Point2D.Double(aicp.getX() + r * .9 + len, aicp.getY() + i * h);
            drawLine(p1, p2, g2);
        }
        p1 = new Point2D.Double(aicp.getX() + r * .9, aicp.getY() - vsf * r);
        p2 = getPoint(p1, r * .07, 25);
        p3 = getPoint(p2, r * .12, 0);
        p4 = getPoint(p3, r * .059, 270);
        p5 = getPoint(p4, r * .12, 180);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        g2.setColor(Color.black);
        g2.fill(pol);
        g2.setColor(Color.LIGHT_GRAY);
        g2.draw(pol);
        p1 = new Point2D.Double(aicp.getX() + r * 1.08, aicp.getY() - vsf * r + r * .04);
        String s = Math.round(vs * 2 / 100) * 10 / 2 + "0";
        if (s.equals("00")) s = "";
        drawTextRight(r, p1, g2, .5, s);
        if (gsf == 1.0) {
            g2.setColor(Color.LIGHT_GRAY);
            h = r * .225;
            p1 = new Point2D.Double(aicp.getX() + r * .645, aicp.getY() - h);
            p2 = getPoint(p1, r * .075, 180);
            drawLine(p1, p2, g2);
            p3 = getPoint(p2, 2 * h, 270);
            drawLine(p2, p3, g2);
            p4 = getPoint(p3, r * .075, 0);
            drawLine(p3, p4, g2);
            for (int i = -2; i <= 2; i++) {
                if (i == 0) continue;
                p1 = new Point2D.Double(aicp.getX() + r * .595, aicp.getY() - r * .014 - i * h / 2.5);
                ell = new Ellipse2D.Double(p1.getX(), p1.getY(), r * .027, r * .028);
                g2.draw(ell);
            }
            p1 = new Point2D.Double(aicp.getX() + r * .645, aicp.getY());
            p2 = getPoint(p1, r * .075, 180);
            drawLine(p1, p2, g2);
            double gsi = gsa > 1 ? 1 : gsa;
            gsi = gsi < -1 ? -1 : gsi;
            double gsy = r * (.225 / 2.5) * gsi * 2;
            p1 = new Point2D.Double(aicp.getX() + r * .595 + r * .042, aicp.getY() + gsy);
            p2 = getPoint(p1, r * .04, 135);
            p3 = getPoint(p2, r * .04, 135 + 90);
            p4 = getPoint(p3, r * .04, 135 + 180);
            pol = new Polygon();
            pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
            pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
            pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
            pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
            g2.setColor(new Color(113, 163, 104));
            g2.fill(pol);
        }
        if (editNav == 1) {
            p1 = new Point2D.Double(cp.getX() - r * 1.27, cp.getY() - r * .99);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .18, r * .06);
        } else {
            p1 = new Point2D.Double(cp.getX() - r * 1.275, cp.getY() - r * .92);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .2, r * .06);
        }
        g2.setColor(new Color(44, 52, 234));
        g2.draw(rr);
        g2.setColor(clrWhite);
        double freqspace = 1.175 - .92;
        p1 = new Point2D.Double(cp.getX() - r * 1.175, cp.getY() - r * .92);
        drawText(r, p1, g2, .5, "" + freqNF.format(varNav1Stby.getValue()));
        p1 = new Point2D.Double(cp.getX() - r * 1.175, cp.getY() - r * .85);
        drawText(r, p1, g2, .5, "" + freqNF.format(varNav2Stby.getValue()));
        p1 = new Point2D.Double(cp.getX() - r * .92, cp.getY() - r * .92);
        g2.setColor(navHasNav1.getBooleanValue() ? clrGreen : clrWhite);
        drawText(r, p1, g2, .5, "" + freqNF.format(varNav1Freq.getValue()));
        p1 = new Point2D.Double(cp.getX() - r * .92, cp.getY() - r * .85);
        g2.setColor(navHasNav2.getBooleanValue() ? clrGreen : clrWhite);
        drawText(r, p1, g2, .5, "" + freqNF.format(varNav2Freq.getValue()));
        if (editCom == 1) {
            p1 = new Point2D.Double(cp.getX() + r * 1.065, cp.getY() - r * .99);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .18, r * .06);
        } else {
            p1 = new Point2D.Double(cp.getX() + r * 1.065, cp.getY() - r * .92);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .18, r * .06);
        }
        g2.setColor(new Color(44, 52, 234));
        g2.draw(rr);
        g2.setColor(clrWhite);
        double freq = (Double) varCom1Stby.getValue();
        p1 = new Point2D.Double(cp.getX() + r * 1.23, cp.getY() - r * .92);
        drawTextRight(r, p1, g2, .5, "1" + (freqNF.format((float) Integer.parseInt(Integer.toHexString((int) freq)) / 100)));
        freq = (Double) varCom2Stby.getValue();
        p1 = new Point2D.Double(cp.getX() + r * 1.23, cp.getY() - r * .85);
        drawTextRight(r, p1, g2, .5, "1" + (freqNF.format((float) Integer.parseInt(Integer.toHexString((int) freq)) / 100)));
        freq = (Double) varCom1Freq.getValue();
        p1 = new Point2D.Double(cp.getX() + r * (1.23 - freqspace), cp.getY() - r * .92);
        g2.setColor(com1Xmit.getBooleanValue() || comBoth.getBooleanValue() ? clrGreen : clrWhite);
        drawTextRight(r, p1, g2, .5, "1" + (freqNF.format((float) Integer.parseInt(Integer.toHexString((int) freq)) / 100)));
        freq = (Double) varCom2Freq.getValue();
        p1 = new Point2D.Double(cp.getX() + r * (1.23 - freqspace), cp.getY() - r * .85);
        g2.setColor(com2Xmit.getBooleanValue() || comBoth.getBooleanValue() ? clrGreen : clrWhite);
        drawTextRight(r, p1, g2, .5, "1" + (freqNF.format((float) Integer.parseInt(Integer.toHexString((int) freq)) / 100)));
        g2.setColor(clrPink);
        String[] txt = { gpswni, distNF.format(gpswd) + " NM", Math.round(gpswdt) + "�", Math.round(gpsgmt) + "�" };
        double delta = r * 1.47 / 4;
        p1 = new Point2D.Double(cp.getX() - r * .55, cp.getY() - r * .925);
        drawTextLeft(r, p1, g2, .45, txt[0]);
        p1 = new Point2D.Double(cp.getX() - r * .55 + .34 * r, cp.getY() - r * .925);
        drawTextLeft(r, p1, g2, .45, txt[1]);
        p1 = new Point2D.Double(cp.getX() - r * .55 + .73 * r, cp.getY() - r * .925);
        drawTextLeft(r, p1, g2, .45, txt[2]);
        p1 = new Point2D.Double(cp.getX() - r * .55 + 1.1 * r, cp.getY() - r * .925);
        drawTextLeft(r, p1, g2, .45, txt[3]);
        double tw = r * .23;
        if (System.currentTimeMillis() - headingChanged < 3000) {
            p1 = new Point2D.Double(cp.getX() - r * .3 - tw, cp.getY() + r * .055);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), tw, r * .07);
            g2.setColor(clrBlack);
            g2.fill(rr);
            g2.setColor(clrWhite);
            g2.draw(rr);
            p2 = new Point2D.Double(p1.getX() + r * .01, p1.getY() + r * .06);
            drawTextLeft(r, p2, g2, .4, "HDG");
            g2.setColor(clrBlue);
            p2 = new Point2D.Double(p1.getX() - r * .01 + tw, p1.getY() + r * .07);
            drawTextRight(r, p2, g2, .5, Math.round(headingLockDir.getDoubleValue()) + "�");
        }
        if (System.currentTimeMillis() - obsChanged < 3000) {
            p1 = new Point2D.Double(cp.getX() + r * .3, cp.getY() + r * .055);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), tw, r * .07);
            g2.setColor(clrBlack);
            g2.fill(rr);
            g2.setColor(clrWhite);
            g2.draw(rr);
            p2 = new Point2D.Double(p1.getX() + r * .01, p1.getY() + r * .06);
            drawTextLeft(r, p2, g2, .4, "CRS");
            g2.setColor(clrGreen);
            p2 = new Point2D.Double(p1.getX() - r * .01 + tw, p1.getY() + r * .07);
            drawTextRight(r, p2, g2, .5, Math.round(obs) + "�");
        }
        delta = r * 2.8 / 12;
        g2.setColor(Color.LIGHT_GRAY);
        p1 = new Point2D.Double(cp.getX() - r * 1.39, cp.getY() + r * .85);
        drawTextLeft(r, p1, g2, .45, "OAT " + Math.round((Double) varOAT.getValue()) + "�C");
        p1 = new Point2D.Double(cp.getX() + r * 1.41 - 3 * delta, cp.getY() + r * .85);
        drawTextLeft(r, p1, g2, .45, "XPDR");
        double xpdr = (Double) varXpdrCode.getValue();
        p1 = new Point2D.Double(cp.getX() + r * 1.39 - 1.5 * delta, cp.getY() + r * .85);
        g2.setColor(new Color(100, 150, 100));
        drawTextRight(r, p1, g2, .45, lz(Integer.parseInt(Integer.toHexString((int) xpdr)), 4) + "ALT");
        g2.setColor(Color.LIGHT_GRAY);
        p1 = new Point2D.Double(cp.getX() + r * 1.41 - 1.5 * delta, cp.getY() + r * .85);
        double lt = (Double) varLocalTime.getValue();
        int hours = (int) (lt / 3600);
        int minutes = (int) (lt / 60 % 60);
        int seconds = (int) (lt % 60);
        drawTextLeft(r, p1, g2, .45, "LCL  " + lz(hours, 2) + ":" + lz(minutes, 2) + ":" + lz(seconds, 2));
        if (markerI.getBooleanValue() || markerO.getBooleanValue() || markerM.getBooleanValue()) {
            double w = r * .07;
            p1 = new Point2D.Double(aicp.getX() + r * .45, aicp.getY() - r * .4);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), w, w);
            g2.setColor(new Color(197, 199, 123));
            g2.fill(rr);
            g2.setColor(Color.BLACK);
            g2.draw(rr);
            String str = "X";
            str = markerI.getBooleanValue() ? "I" : str;
            str = markerM.getBooleanValue() ? "M" : str;
            str = markerO.getBooleanValue() ? "O" : str;
            p1 = new Point2D.Double(p1.getX() + w / 2, p1.getY() + w);
            g2.setColor(Color.BLACK);
            drawText(r, p1, g2, .5, str);
        }
        Area ar;
        if (navHasNav1.getBooleanValue()) {
            p1 = new Point2D.Double(hsicp.getX() - r * .65, hsicp.getY() - r * 0.055);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .75, r * .2);
            ell = new Ellipse2D.Double((int) hsicp.getX() - r * .4, (int) hsicp.getY() - r * .4, (int) (r * .8), (int) (r * .8));
            ar = new Area(rr);
            ar.subtract(new Area(ell));
            g2.setColor(Color.BLACK);
            g2.fill(ar);
            g2.setColor(Color.LIGHT_GRAY);
            g2.draw(ar);
            g2.setColor(Color.LIGHT_GRAY);
            if ((Double) hasDME1.getValue() == -1.0) {
                p1 = new Point2D.Double(hsicp.getX() - r * .63, hsicp.getY() + r * .02);
                drawTextLeft(r, p1, g2, .5, dmeNF.format((Double) navDME1.getValue()) + " NM");
            }
            boolean gdn1 = (Double) gpsDrivesNav1.getValue() == 1;
            g2.setColor(gdn1 ? new Color(168, 108, 167) : new Color(108, 168, 107));
            p1 = new Point2D.Double(hsicp.getX() - r * .63, hsicp.getY() + r * (.02 + .06));
            drawTextLeft(r, p1, g2, .5, ((Double) gpsDrivesNav1.getValue() == 1 ? hsiID.getStringValue() : "VOR1"));
            g2.setColor(Color.LIGHT_GRAY);
            p1 = new Point2D.Double(hsicp.getX() - r * .63, hsicp.getY() + r * (.02 + .12));
            drawTextLeft(r, p1, g2, .5, (String) (nav1ident.getValue()));
        }
        if (navHasNav2.getBooleanValue()) {
            p1 = new Point2D.Double(hsicp.getX() - r * .65, hsicp.getY() + r * .175);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .65, r * .2);
            ell = new Ellipse2D.Double((int) hsicp.getX() - r * .4, (int) hsicp.getY() - r * .4, (int) (r * .8), (int) (r * .8));
            ar = new Area(rr);
            ar.subtract(new Area(ell));
            g2.setColor(Color.BLACK);
            g2.fill(ar);
            g2.setColor(Color.LIGHT_GRAY);
            g2.draw(ar);
            g2.setColor(Color.LIGHT_GRAY);
            if ((Double) hasDME2.getValue() == -1.0) {
                p1 = new Point2D.Double(hsicp.getX() - r * .63, hsicp.getY() + r * .245);
                drawTextLeft(r, p1, g2, .5, dmeNF.format((Double) navDME2.getValue()) + " NM");
            }
            g2.setColor(clrBlue);
            p1 = new Point2D.Double(hsicp.getX() - r * .63, hsicp.getY() + r * (.245 + .06));
            drawTextLeft(r, p1, g2, .5, (String) (nav2ident.getValue()));
            g2.setColor(clrWhite);
            p1 = new Point2D.Double(hsicp.getX() - r * .63, hsicp.getY() + r * (.245 + .12));
            drawTextLeft(r, p1, g2, .5, "NAV2");
        }
        if (adfSignal.getDoubleValue() > 0.1) {
            p1 = new Point2D.Double(hsicp.getX(), hsicp.getY() + r * .175);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .65, r * .2);
            ell = new Ellipse2D.Double((int) hsicp.getX() - r * .4, (int) hsicp.getY() - r * .4, (int) (r * .8), (int) (r * .8));
            ar = new Area(rr);
            ar.subtract(new Area(ell));
            g2.setColor(Color.BLACK);
            g2.fill(ar);
            g2.setColor(Color.LIGHT_GRAY);
            g2.draw(ar);
            g2.setColor(clrWhite);
            p1 = new Point2D.Double(hsicp.getX() + r * .63, hsicp.getY() + r * .245);
            freq = (Double) adfFreq.getValue();
            drawTextRight(r, p1, g2, .5, "" + lz((float) Integer.parseInt(Integer.toHexString((int) freq)) / 10000, 6));
            g2.setColor(clrBlue);
            p1 = new Point2D.Double(hsicp.getX() + r * .63, hsicp.getY() + r * (.245 + .06));
            drawTextRight(r, p1, g2, .5, adfIdent.getStringValue());
            g2.setColor(clrWhite);
            p1 = new Point2D.Double(hsicp.getX() + r * .63, hsicp.getY() + r * (.245 + .12));
            drawTextRight(r, p1, g2, .5, "ADF1");
        }
        String[] buttons = { "", "INSET", "", "PFD", "", "CDI", "", "XPDR", "IDENT", "TMR/REF", "NRST", "ALERTS", "" };
        g2.setColor(Color.LIGHT_GRAY);
        delta = r * 2.8 / 12;
        for (int i = 1; i < 12; i++) {
            p1 = new Point2D.Double(cp.getX() - r * 1.4 + i * delta + delta / 2, cp.getY() + r * .92);
            drawText(r, p1, g2, .45, buttons[i]);
        }
    }

    protected synchronized void drawNeedle(final double r, double needleR, final Point2D cp, Graphics2D g2) {
        Angle angleCurrent;
        Point2D p1, p2, p3, p4, p5;
        Point2D hsicp = new Point2D.Double(cp.getX(), cp.getY() + r * .45);
        double fo = .5, fi = .18;
        Ellipse2D.Double ello = new Ellipse2D.Double(hsicp.getX() - r * fo, hsicp.getY() - r * fo, 2 * r * fo, 2 * r * fo);
        Ellipse2D.Double elli = new Ellipse2D.Double(hsicp.getX() - r * fi, hsicp.getY() - r * fi, 2 * r * fi, 2 * r * fi);
        if (navHasNav2.getBooleanValue()) {
            angleCurrent = new Angle(270 + hi - vor2Radial.getDoubleValue());
            g2.setColor(new Color(93, 130, 128));
            g2.setStroke(new BasicStroke((float) (r * .006)));
            p1 = getPoint(hsicp, r * fo / 2 + r * .025, angleCurrent.getDegrees());
            p2 = getPoint(p1, r * fo + r * .05, angleCurrent.plus(180));
            g2.draw(new Line2D.Double(p1, p2));
            p2 = getPoint(p1, r * .04, angleCurrent.plus(135));
            g2.draw(new Line2D.Double(p1, p2));
            p2 = getPoint(p1, r * .04, angleCurrent.plus(-135));
            g2.draw(new Line2D.Double(p1, p2));
            p1 = new Point2D.Double(hsicp.getX() - r * fo * .8, hsicp.getY() + r * fo * .6);
            p2 = getPoint(p1, r * fo / 5, 0);
            g2.draw(new Line2D.Double(p1, p2));
            p2 = getPoint(p1, r * .03, 45);
            g2.draw(new Line2D.Double(p1, p2));
            p2 = getPoint(p1, r * .03, 315);
            g2.draw(new Line2D.Double(p1, p2));
        }
        if (adfSignal.getDoubleValue() > 0.1) {
            angleCurrent = new Angle(90 - adfr);
            g2.setColor(new Color(93, 130, 128));
            g2.setStroke(new BasicStroke((float) (r * .006)));
            p1 = getPoint(hsicp, r * fo / 2 + r * .05, angleCurrent.getDegrees());
            p2 = getPoint(p1, r * .025, angleCurrent.plus(135));
            p3 = getPoint(p2, r * fo * 1.05, angleCurrent.plus(180));
            p4 = getPoint(p1, r * .025, angleCurrent.plus(-135));
            p5 = getPoint(p4, r * fo * 1.05, angleCurrent.plus(180));
            g2.draw(new Line2D.Double(p4, p5));
            g2.draw(new Line2D.Double(p2, p3));
            g2.draw(new Line2D.Double(p3, p5));
            p2 = getPoint(hsicp, r * fo / 2 + r * .05, angleCurrent.plus(180));
            p3 = getPoint(p2, r * .05, angleCurrent.getDegrees());
            g2.draw(new Line2D.Double(p2, p3));
            p2 = getPoint(p1, r * .05, angleCurrent.plus(135));
            g2.draw(new Line2D.Double(p1, p2));
            p2 = getPoint(p1, r * .05, angleCurrent.plus(-135));
            g2.draw(new Line2D.Double(p1, p2));
            p1 = new Point2D.Double(hsicp.getX() + r * fo * .8, hsicp.getY() + r * fo * .6);
            p2 = getPoint(p1, r * .015, 225);
            p3 = getPoint(p2, r * fo / 5, 180);
            g2.draw(new Line2D.Double(p2, p3));
            p2 = getPoint(p1, r * .03, 225);
            g2.draw(new Line2D.Double(p1, p2));
            p2 = getPoint(p1, r * .015, 145);
            p3 = getPoint(p2, r * fo / 5.1, 180);
            g2.draw(new Line2D.Double(p2, p3));
            p2 = getPoint(p1, r * .03, 145);
            g2.draw(new Line2D.Double(p1, p2));
        }
        Area area = new Area(ello);
        g2.setClip(area);
        Color clrNeedle = null;
        double deflection = cdi;
        if ((Double) gpsDrivesNav1.getValue() == 1 && (Double) hsiBearingValid.getValue() == 1.0) {
            angleCurrent = new Angle(90 + hi - (Double) hsiBearing.getValue());
            deflection = hsiCDI.getDoubleValue();
            clrNeedle = clrPink;
        } else {
            angleCurrent = new Angle(90 + hi - obs);
            clrNeedle = clrGreen;
        }
        if (clrNeedle != null) {
            g2.setColor(clrNeedle);
            g2.setStroke(new BasicStroke((float) (r * .01)));
            p1 = getPoint(hsicp, r * fo / 2, angleCurrent.getDegrees());
            p2 = getPoint(p1, r * fo, angleCurrent.plus(180));
            g2.draw(new Line2D.Double(p1, p2));
            p2 = getPoint(p1, r * .05, angleCurrent.plus(135));
            g2.draw(new Line2D.Double(p1, p2));
            p2 = getPoint(p1, r * .05, angleCurrent.plus(-135));
            g2.draw(new Line2D.Double(p1, p2));
        }
        g2.setColor(new Color(55, 69, 55));
        g2.fill(elli);
        g2.setColor(clrWhite);
        g2.setStroke(new BasicStroke((float) (r * .005)));
        g2.draw(elli);
        Ellipse2D ell;
        g2.setColor(clrWhite);
        g2.setStroke(new BasicStroke((float) (r * .005)));
        double tickspace = fi * .8 / 2;
        for (double i = -2 * tickspace; i <= tickspace * 2; i += tickspace) {
            if (i == 0) continue;
            p2 = getPoint(hsicp, r * i, angleCurrent.plus(90));
            ell = new Ellipse2D.Double(p2.getX(), p2.getY(), r * 0.01, r * 0.01);
            g2.draw(ell);
        }
        if (clrNeedle != null) {
            g2.setColor(clrNeedle);
            g2.setStroke(new BasicStroke((float) (r * .01)));
            p1 = getPoint(hsicp, r * fi * .8 * deflection / 128, angleCurrent.plus(270));
            p2 = getPoint(p1, r * fi * 1.5 / 2, angleCurrent.getDegrees());
            p1 = getPoint(p2, r * fi * 1.5, angleCurrent.plus(180));
            g2.draw(new Line2D.Double(p1, p2));
        }
    }
}
