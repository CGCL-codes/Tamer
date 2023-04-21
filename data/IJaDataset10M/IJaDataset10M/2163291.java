package org.gaugebook.gauges.aviation.simple;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import org.gaugebook.AbstractSteamGauge;
import org.gaugebook.FSXVariable;
import org.gaugebook.GaugeBook;
import org.gaugebook.math.Angle;

/**
 * Turn Coordinator
 * 
 * Dev version 
 * 
 * @author Michael Nagler
 *
 */
public class TurnCoordinator extends AbstractSteamGauge {

    private FSXVariable bankVar;

    private FSXVariable ballVar;

    private double bank = 0, ball = 0;

    public void init(GaugeBook owner, String title) {
        super.init(owner, title);
        this.setMinValue(-90);
        this.setMaxValue(90);
        this.setText("TURN COORDINATOR");
        this.setDecPlaceCount(0);
        this.setMajorTickStep(30);
        this.setMinorTickStep(10);
        this.setAngleExtend(180);
        this.setAngleStart(0);
        this.setValue(0);
        super.setTextPosition(0, -0.45);
        bankVar = owner.registerFSXVariable("DELTA HEADING RATE");
        ballVar = owner.registerFSXVariable("TURN COORDINATOR BALL");
    }

    public void update() {
        bank = (180 / StrictMath.PI) * -2 * Math.PI * bankVar.getDoubleValue();
        ball = ballVar.getDoubleValue();
        super.setValue(bank);
        repaint();
    }

    @Override
    protected void drawScale(final double r, final Point2D cp, Graphics2D g2) {
        Angle angleCurrent;
        Point2D ap, np;
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        for (int a = -105; a <= 105; a += 15) {
            if (!(Math.abs(a) == 105 || Math.abs(a) == 90)) continue;
            angleCurrent = new Angle(90 + a);
            ap = getPoint(cp, r * .75, angleCurrent.getDegrees());
            np = getPoint(cp, r * .92, angleCurrent.getDegrees());
            g2.draw(new Line2D.Double(ap, np));
        }
    }

    @Override
    protected void drawNeedle(final double r, double needleR, final Point2D cp, Graphics2D g2) {
        double r0 = proz(r, 65);
        double r1 = proz(r, 70);
        r0 = proz(r, 7.6);
        r1 = proz(r, 9.5);
        Ellipse2D ell_0 = new Ellipse2D.Double();
        ell_0.setFrameFromCenter(cp, new Point2D.Double(cp.getX() - r0, cp.getY() - r0));
        Ellipse2D ell_1 = new Ellipse2D.Double();
        ell_1.setFrameFromCenter(cp, new Point2D.Double(cp.getX() - r1, cp.getY() - r1));
        double bank = this.bank;
        bank = this.bank > 90 ? 180 - bank : bank;
        bank = bank < -90 ? -180 - bank : bank;
        bank /= 2;
        Point2D pointLeft, pointRight, np;
        Angle angleCurrent = new Angle(bank + 90);
        g2.setColor(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke((int) (r * .04)));
        pointLeft = getPoint(cp, needleR * .8, bank);
        pointRight = getPoint(cp, needleR * -.8, bank);
        g2.draw(new Line2D.Double(pointLeft, pointRight));
        g2.setStroke(new BasicStroke((int) (r * .03)));
        np = getPoint(cp, r * .12, angleCurrent.getDegrees());
        pointLeft = getPoint(np, needleR * .3, bank);
        pointRight = getPoint(np, needleR * -.3, bank);
        g2.draw(new Line2D.Double(pointLeft, pointRight));
        pointLeft = getPoint(cp, r * .24, angleCurrent.getDegrees());
        g2.draw(new Line2D.Double(pointLeft, np));
        np = getPoint(cp, r * .05, angleCurrent.getDegrees());
        ell_1.setFrameFromCenter(np, new Point2D.Double(np.getX() - r * .1, np.getY() - r * .1));
        g2.fill(ell_1);
    }

    @Override
    protected void drawValue(final double r, final double needleR, final Point2D cp, Graphics2D g2) {
        if (showValue) {
            g2.setColor(valueBackgroundColor);
            double rv = proz(r, 40);
            Point2D p = getPoint(cp, rv, angleStart + angleExtend + (360 - angleExtend) / 2);
            double h = proz(r, 16);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, (float) (h * 0.7)));
            Dimension size = new Dimension((int) (r * .9), (int) (r * .12));
            double w = size.getWidth();
            double corner = Math.min(10, Math.max(h, w) / 4.0);
            RoundRectangle2D cvArea = new RoundRectangle2D.Double(p.getX() - w / 2, p.getY() - h / 2, w, h, corner, corner);
            g2.fill(cvArea);
            Stroke old = g2.getStroke();
            g2.setColor(scaleLineColor);
            g2.setStroke(new BasicStroke(h < 20 ? 1f : (h < 40 ? 2f : 3f)));
            g2.draw(cvArea);
            g2.setStroke(old);
            g2.setColor(valueColor);
            double br = r * .14;
            double x0 = cvArea.getCenterX() - br / 2;
            double y0 = cvArea.getCenterY() - br / 2;
            Ellipse2D.Double ell = new Ellipse2D.Double(x0 + (ball * size.getWidth() / 306), y0, r * .15, r * .15);
            g2.setColor(Color.LIGHT_GRAY);
            g2.fill(ell);
            g2.draw(new Line2D.Double(new Point2D.Double(x0, y0), new Point2D.Double(x0, y0 + br)));
            g2.draw(new Line2D.Double(new Point2D.Double(x0 + br, y0), new Point2D.Double(x0 + br, y0 + br)));
        }
    }

    protected void drawText(final double r, final Point2D cp, Graphics2D g2) {
        super.drawText(r, cp, g2);
        super.drawText(r, cp, g2, new Point2D.Double(0, .65), 1, "2 MIN");
        super.drawText(r, cp, g2, new Point2D.Double(0, .75), .8, "NO PITCH");
        super.drawText(r, cp, g2, new Point2D.Double(0, .85), .8, "INFORMATION");
    }
}
