package calclipse.lib.lcd;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import calclipse.lib.lcd.LCD.Segment;

/**
 * A seven-segment numeric indicator.
 * @author T. Sommerland
 */
public class Digit extends Segment {

    private static final long serialVersionUID = 1L;

    private final Point pntLT1 = new Point();

    private final Point pntLT2 = new Point();

    private final Point pntLB1 = new Point();

    private final Point pntLB2 = new Point();

    private final Point pntCT1 = new Point();

    private final Point pntCT2 = new Point();

    private final Point pntCC1 = new Point();

    private final Point pntCC2 = new Point();

    private final Point pntCB1 = new Point();

    private final Point pntCB2 = new Point();

    private final Point pntRT1 = new Point();

    private final Point pntRT2 = new Point();

    private final Point pntRB1 = new Point();

    private final Point pntRB2 = new Point();

    private boolean lt;

    private boolean lb;

    private boolean ct;

    private boolean cc;

    private boolean cb;

    private boolean rt;

    private boolean rb;

    private Dimension digitSize = new Dimension(13, 26);

    private Insets insets = new Insets(2, 2, 2, 2);

    private int thickness = 1;

    private boolean transparent;

    /**
     * Creates a digit with all segments off.
     */
    public Digit() {
    }

    /**
     * Use this constructor to set the individual
     * segments of the digit.
     * @see #setDigit(
     * boolean, boolean, boolean, boolean, boolean, boolean, boolean)
     */
    public Digit(final boolean leftTop, final boolean leftBottom, final boolean centerTop, final boolean centerCenter, final boolean centerBottom, final boolean rightTop, final boolean rightBottom) {
        setSegments(leftTop, leftBottom, centerTop, centerCenter, centerBottom, rightTop, rightBottom);
    }

    /**
     * The argument should be between 0 and 9.
     * @see #setDigit(int)
     */
    public Digit(final int i) {
        setSegments(i);
    }

    private void setSegments(final boolean leftTop, final boolean leftBottom, final boolean centerTop, final boolean centerCenter, final boolean centerBottom, final boolean rightTop, final boolean rightBottom) {
        this.lt = leftTop;
        this.lb = leftBottom;
        this.ct = centerTop;
        this.cc = centerCenter;
        this.cb = centerBottom;
        this.rt = rightTop;
        this.rb = rightBottom;
    }

    private void setSegments(final int digit) {
        switch(digit) {
            case 0:
                setSegments(true, true, true, false, true, true, true);
                break;
            case 1:
                setSegments(false, false, false, false, false, true, true);
                break;
            case 2:
                setSegments(false, true, true, true, true, true, false);
                break;
            case 3:
                setSegments(false, false, true, true, true, true, true);
                break;
            case 4:
                setSegments(true, false, false, true, false, true, true);
                break;
            case 5:
                setSegments(true, false, true, true, true, false, true);
                break;
            case 6:
                setSegments(true, true, true, true, true, false, true);
                break;
            case 7:
                setSegments(false, false, true, false, false, true, true);
                break;
            case 8:
                setSegments(true, true, true, true, true, true, true);
                break;
            case 9:
                setSegments(true, false, true, true, true, true, true);
                break;
            default:
        }
    }

    /**
     * Sets the individual segments of the digit.
     * @param leftTop segment F
     * @param leftBottom segment E
     * @param centerTop segment A
     * @param centerCenter segment G
     * @param centerBottom segment D
     * @param rightTop segment B
     * @param rightBottom segment C
     */
    public void setDigit(final boolean leftTop, final boolean leftBottom, final boolean centerTop, final boolean centerCenter, final boolean centerBottom, final boolean rightTop, final boolean rightBottom) {
        setSegments(leftTop, leftBottom, centerTop, centerCenter, centerBottom, rightTop, rightBottom);
        repaint();
    }

    /**
     * Sets the segments to display a number
     * between 0 and 9.
     */
    public void setDigit(final int digit) {
        setSegments(digit);
        repaint();
    }

    @Override
    public Dimension getSize() {
        return new Dimension(digitSize.width + insets.left + insets.right, digitSize.height + insets.top + insets.bottom);
    }

    @Override
    public void paint(final Graphics g, final Color on, final Color off, final Point p) {
        calcPoints(p);
        boolean b = true;
        final int j = transparent ? 1 : 2;
        g.setColor(on);
        for (int i = 0; i < j; i++) {
            if (lt == b) {
                drawLT(g);
            }
            if (lb == b) {
                drawLB(g);
            }
            if (ct == b) {
                drawCT(g);
            }
            if (cc == b) {
                drawCC(g);
            }
            if (cb == b) {
                drawCB(g);
            }
            if (rt == b) {
                drawRT(g);
            }
            if (rb == b) {
                drawRB(g);
            }
            b = false;
            g.setColor(off);
        }
    }

    private void calcPoints(final Point topLeft) {
        final Rectangle rect = new Rectangle(topLeft.x + insets.left, topLeft.y + insets.top, digitSize.width - 1, digitSize.height - 1);
        final int d = rect.height / 2;
        pntLT1.x = rect.x;
        pntLT1.y = rect.y + 1;
        pntLT2.x = rect.x;
        pntLT2.y = rect.y + d;
        pntLB1.x = pntLT2.x;
        pntLB1.y = pntLT2.y + 2;
        pntLB2.x = pntLB1.x;
        pntLB2.y = rect.y + rect.height - 1;
        pntRT1.x = rect.x + rect.width;
        pntRT1.y = pntLT1.y;
        pntRT2.x = pntRT1.x;
        pntRT2.y = pntLT2.y;
        pntRB1.x = pntRT2.x;
        pntRB1.y = pntLB1.y;
        pntRB2.x = pntRB1.x;
        pntRB2.y = pntLB2.y;
        pntCT1.x = pntLT1.x + 1;
        pntCT1.y = rect.y;
        pntCT2.x = pntRT1.x - 1;
        pntCT2.y = rect.y;
        pntCC1.x = pntLT2.x + 1 + thickness;
        pntCC1.y = pntLT2.y + 1;
        pntCC2.x = pntRT2.x - 1 - thickness;
        pntCC2.y = pntCC1.y;
        pntCB1.x = pntCT1.x;
        pntCB1.y = rect.y + rect.height;
        pntCB2.x = pntCT2.x;
        pntCB2.y = pntCB1.y;
    }

    private void drawLT(final Graphics g) {
        final int w = 2 * thickness;
        for (int i = 0; i < thickness; i++) {
            g.drawLine(pntLT1.x + i, pntLT1.y + i, pntLT2.x + i, pntLT2.y - thickness + i);
            g.drawLine(pntLT1.x + w - i, pntLT1.y + w - i, pntLT2.x + w - i, pntLT2.y - thickness + i);
        }
        g.drawLine(pntLT1.x + thickness, pntLT1.y + thickness, pntLT2.x + thickness, pntLT2.y);
    }

    private void drawLB(final Graphics g) {
        final int w = 2 * thickness;
        for (int i = 0; i < thickness; i++) {
            g.drawLine(pntLB1.x + i, pntLB1.y + thickness - i, pntLB2.x + i, pntLB2.y - i);
            g.drawLine(pntLB1.x + w - i, pntLB1.y + thickness - i, pntLB2.x + w - i, pntLB2.y - w + i);
        }
        g.drawLine(pntLB1.x + thickness, pntLB1.y, pntLB2.x + thickness, pntLB2.y - thickness);
    }

    private void drawCT(final Graphics g) {
        final int w = 2 * thickness;
        for (int i = 1; i <= w; i++) {
            g.drawLine(pntCT1.x + i, pntCT1.y + i, pntCT2.x - i, pntCT2.y + i);
        }
        g.drawLine(pntCT1.x, pntCT1.y, pntCT2.x, pntCT2.y);
    }

    private void drawCC(final Graphics g) {
        for (int i = 1; i <= thickness; i++) {
            g.drawLine(pntCC1.x + i, pntCC1.y - i, pntCC2.x - i, pntCC2.y - i);
            g.drawLine(pntCC1.x + i, pntCC1.y + i, pntCC2.x - i, pntCC2.y + i);
        }
        g.drawLine(pntCC1.x, pntCC1.y, pntCC2.x, pntCC2.y);
    }

    private void drawCB(final Graphics g) {
        final int w = 2 * thickness;
        for (int i = 1; i <= w; i++) {
            g.drawLine(pntCB1.x + i, pntCB1.y - i, pntCB2.x - i, pntCB2.y - i);
        }
        g.drawLine(pntCB1.x, pntCB1.y, pntCB2.x, pntCB2.y);
    }

    private void drawRT(final Graphics g) {
        final int w = 2 * thickness;
        for (int i = 0; i < thickness; i++) {
            g.drawLine(pntRT1.x - i, pntRT1.y + i, pntRT2.x - i, pntRT2.y - thickness + i);
            g.drawLine(pntRT1.x - w + i, pntRT1.y + w - i, pntRT2.x - w + i, pntRT2.y - thickness + i);
        }
        g.drawLine(pntRT1.x - thickness, pntRT1.y + thickness, pntRT2.x - thickness, pntRT2.y);
    }

    private void drawRB(final Graphics g) {
        final int w = 2 * thickness;
        for (int i = 0; i < thickness; i++) {
            g.drawLine(pntRB1.x - i, pntRB1.y + thickness - i, pntRB2.x - i, pntRB2.y - i);
            g.drawLine(pntRB1.x - w + i, pntRB1.y + thickness - i, pntRB2.x - w + i, pntRB2.y - w + i);
        }
        g.drawLine(pntRB1.x - thickness, pntRB1.y, pntRB2.x - thickness, pntRB2.y - thickness);
    }

    public Dimension getDigitSize() {
        return new Dimension(digitSize);
    }

    public void setDigitSize(final Dimension d) {
        digitSize = new Dimension(d);
        refresh();
    }

    public Insets getInsets() {
        return (Insets) insets.clone();
    }

    public void setInsets(final Insets i) {
        insets = (Insets) i.clone();
        refresh();
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(final int i) {
        thickness = i;
        repaint();
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(final boolean b) {
        transparent = b;
        repaint();
    }
}
