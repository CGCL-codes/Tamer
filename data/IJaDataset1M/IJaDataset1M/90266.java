package SplineInterpolation;

import java.awt.*;
import java.util.*;

public class LagrangePanel extends Panel {

    public Hashtable htDr;

    private int W;

    private int H;

    private Dot grabbedDot;

    private int nPts;

    private int yBase = 350;

    private int xBase = 250;

    private double lagpoly[][];

    private double intpoly[];

    private double slopes[];

    private boolean showContrib = true;

    private boolean doSpline = false;

    private boolean doLagrange = true;

    private Color gold = new Color(255, 215, 0);

    private Color steelBlue = new Color(70, 130, 180);

    private Color curveColor[] = { Color.cyan, Color.magenta, Color.yellow, Color.red, Color.green, Color.blue, Color.orange, Color.pink };

    public LagrangePanel(int w, int h) {
        W = w;
        H = h;
        htDr = new Hashtable();
        setBackground(Color.black);
    }

    public Dimension minimumSize() {
        return new Dimension(W, H);
    }

    public Dimension preferredSize() {
        return minimumSize();
    }

    public void showLagrange(boolean b) {
        doLagrange = b;
        repaint();
    }

    public void showContributions(boolean b) {
        showContrib = b;
        repaint();
    }

    public void showSplines(boolean b) {
        doSpline = b;
        repaint();
    }

    public void lagrange(double x[], double y[]) {
        int i, j, jj, k;
        for (j = 0; j < nPts; j++) intpoly[j] = 0.0;
        for (i = 0; i < nPts; i++) {
            lagpoly[i][0] = 1.0;
            double fac = y[i];
            j = 0;
            for (k = 0; k < nPts; k++) {
                if (k == i) continue;
                lagpoly[i][j + 1] = lagpoly[i][j];
                for (jj = j; jj > 0; jj--) lagpoly[i][jj] = lagpoly[i][jj - 1] - lagpoly[i][jj] * x[k];
                lagpoly[i][0] *= -x[k];
                j++;
                fac /= (x[i] - x[k]);
            }
            for (j = 0; j < nPts; j++) lagpoly[i][j] *= fac;
            for (j = 0; j < nPts; j++) intpoly[j] += lagpoly[i][j];
        }
    }

    private double polyEval(double x, double a[], int n) {
        int j;
        double val = a[n];
        for (j = n - 1; j >= 0; j--) val = val * x + a[j];
        return val;
    }

    private double polyEval(double x, double a[][], int i, int n) {
        int j;
        double val = a[i][n];
        for (j = n - 1; j >= 0; j--) val = val * x + a[i][j];
        return val;
    }

    private double A0(double x) {
        return (x - 1.0) * (x - 1.0) * (2 * x + 1);
    }

    private double B0(double x) {
        return (x - 1.0) * (x - 1.0) * (2 * x + 1);
    }

    public static double splineEval(double x, double x0, double x1, double y0, double y1, double s0, double s1) {
        double h = x1 - x0;
        double t = (x - x0) / h;
        double u = 1 - t;
        return u * u * (y0 * (2 * t + 1) + s0 * h * t) + t * t * (y1 * (3 - 2 * t) - s1 * h * u);
    }

    public static void computeSplineSlopes(int n, double x[], double y[], double s[]) {
        int i, j;
        double h[] = new double[n];
        double hinv[] = new double[n];
        double g[] = new double[n];
        double a[] = new double[n + 1];
        double b[] = new double[n + 1];
        double fac;
        for (i = 0; i < n; i++) {
            h[i] = x[i + 1] - x[i];
            hinv[i] = 1.0 / h[i];
            g[i] = 3 * (y[i + 1] - y[i]) * hinv[i] * hinv[i];
        }
        a[0] = 2 * hinv[0];
        b[0] = g[0];
        for (i = 1; i <= n; i++) {
            fac = hinv[i - 1] / a[i - 1];
            a[i] = (2 - fac) * hinv[i - 1];
            b[i] = g[i - 1] - fac * b[i - 1];
            if (i < n) {
                a[i] += 2 * hinv[i];
                b[i] += g[i];
            }
        }
        s[n] = b[n] / a[n];
        for (i = n - 1; i >= 0; i--) s[i] = (b[i] - hinv[i] * s[i + 1]) / a[i];
    }

    private String doubleFormat(double val, int dec) {
        StringBuffer txt = new StringBuffer();
        if (val < 0.0) {
            txt.append("-");
            val = -val;
        }
        int i, k;
        k = (int) val;
        txt.append("" + k);
        val -= k;
        if (dec > 0) {
            txt.append('.');
        }
        for (i = 0; i < dec; i++) {
            val *= 10;
            k = (int) (val + 0.001);
            txt.append("" + k);
            val -= k;
        }
        return txt.toString();
    }

    private void paintCurves(Graphics g) {
        int i, j;
        String xName, yName, lName;
        String xvs, yvs;
        double x[] = new double[nPts];
        double y[] = new double[nPts];
        double xSorted[] = new double[nPts];
        double ySorted[] = new double[nPts];
        FontMetrics fm = g.getFontMetrics();
        for (i = 0; i < nPts; i++) {
            xName = "x" + i;
            yName = "y" + i;
            lName = "l" + i;
            Drawable xi = getDrawable(xName);
            Drawable yi = getDrawable(yName);
            Drawable li = getDrawable(lName);
            xSorted[i] = x[i] = 0.02 * (xi.getX() - xBase);
            ySorted[i] = y[i] = 0.02 * (xi.getY() - yi.getY());
            g.setColor(curveColor[i % curveColor.length]);
            xvs = doubleFormat(x[i], 2);
            yvs = doubleFormat(y[i], 2);
            int wx = fm.stringWidth(xvs);
            int wy = fm.stringWidth(yvs);
            int h = fm.getHeight();
            g.drawString(xvs, xi.getX() - wx / 2, xi.getY() + h);
            g.drawString(yvs, 2 + li.getX(), li.getY() + h / 2);
        }
        int ix, iy, ixLast, iyLast;
        double rx, ry, rxstep;
        if (doLagrange) {
            lagrange(x, y);
            if (showContrib) {
                for (i = 0; i < nPts; i++) {
                    g.setColor(curveColor[i % curveColor.length]);
                    ixLast = -1;
                    iyLast = -1;
                    for (ix = 0; ix <= 500; ix += 5) {
                        rx = 0.02 * (ix - xBase);
                        ry = polyEval(rx, lagpoly, i, nPts - 1);
                        iy = yBase - (int) (ry * 50.0);
                        if (iy > 10000) iy = 10000;
                        if (iy < -10000) iy = -10000;
                        if (ixLast >= 0) {
                            g.drawLine(ixLast, iyLast, ix, iy);
                        }
                        ixLast = ix;
                        iyLast = iy;
                    }
                }
            }
            g.setColor(Color.white);
            ixLast = -1;
            iyLast = -1;
            for (ix = 0; ix <= 500; ix += 5) {
                rx = 0.02 * (ix - xBase);
                ry = polyEval(rx, intpoly, nPts - 1);
                iy = yBase - (int) (ry * 50.0 + 0.5);
                if (iy > 2000) iy = 2000;
                if (iy < -2000) iy = -2000;
                if (ixLast >= 0) {
                    g.drawLine(ixLast, iyLast, ix, iy);
                }
                ixLast = ix;
                iyLast = iy;
            }
        }
        if (doSpline) {
            double tmp;
            g.setColor(steelBlue);
            for (i = 0; i < nPts; i++) {
                boolean noExch = true;
                for (j = 0; j < nPts - 1; j++) {
                    if (xSorted[j] > xSorted[j + 1]) {
                        tmp = xSorted[j + 1];
                        xSorted[j + 1] = xSorted[j];
                        xSorted[j] = tmp;
                        tmp = ySorted[j + 1];
                        ySorted[j + 1] = ySorted[j];
                        ySorted[j] = tmp;
                        noExch = false;
                    }
                }
                if (noExch) break;
            }
            computeSplineSlopes(nPts - 1, xSorted, ySorted, slopes);
            int seg;
            ixLast = -1;
            iyLast = -1;
            int nDivs = 50;
            for (seg = 0; seg < nPts - 1; seg++) {
                rxstep = (xSorted[seg + 1] - xSorted[seg]) / nDivs;
                for (i = 0; i < nDivs; i++) {
                    rx = xSorted[seg] + i * rxstep;
                    ry = splineEval(rx, xSorted[seg], xSorted[seg + 1], ySorted[seg], ySorted[seg + 1], slopes[seg], slopes[seg + 1]);
                    iy = yBase - (int) (ry * 50.0 + 0.5);
                    ix = xBase + (int) (rx * 50.0 + 0.5);
                    if (iy > 2000) iy = 2000;
                    if (iy < -2000) iy = -2000;
                    if (ixLast >= 0) {
                        g.drawLine(ixLast, iyLast, ix, iy);
                    }
                    ixLast = ix;
                    iyLast = iy;
                }
            }
        }
    }

    public void paint(Graphics g) {
        paintCurves(g);
        Enumeration e = htDr.elements();
        while (e.hasMoreElements()) {
            Drawable dr = (Drawable) (e.nextElement());
            dr.paint(g);
        }
    }

    public void addDrawable(String name, Drawable dr) {
        htDr.put(name, dr);
    }

    public Drawable getDrawable(String name) {
        return (Drawable) (htDr.get(name));
    }

    public Dot closestDot(int x, int y) {
        Dot d = null;
        Enumeration e = htDr.elements();
        int minSep = 7;
        while (e.hasMoreElements()) {
            Drawable dr = (Drawable) (e.nextElement());
            if (dr instanceof Dot) {
                Dot a = (Dot) dr;
                int sep = a.separation(x, y);
                if ((sep < 6) && (minSep > sep)) {
                    minSep = sep;
                    d = a;
                }
            }
        }
        return d;
    }

    public boolean handleEvent(Event evt) {
        if (evt.id == Event.MOUSE_DOWN) {
            grabbedDot = closestDot(evt.x, evt.y);
        }
        if (evt.id == Event.MOUSE_DRAG) {
            if (grabbedDot != null) {
                grabbedDot.requestMove(evt.x, evt.y);
                repaint();
            }
        }
        if (evt.id == Event.MOUSE_UP) {
            if (grabbedDot != null) {
                grabbedDot.requestMove(evt.x, evt.y);
                repaint();
            }
        }
        return super.handleEvent(evt);
    }

    public void init(int nPts) {
        init(nPts, false);
    }

    public void init(int nPts, boolean doRandomHeights) {
        htDr.clear();
        this.nPts = nPts;
        Dot xa = new Dot("xa", 0, 350, false, false, null);
        Dot xb = new Dot("xb", 500, 350, false, false, null);
        Line xAxis = new Line("xAxis", xa, xb, Color.gray);
        addDrawable("xAxis", xAxis);
        int i, x, y, xStep;
        x = 50;
        xStep = 400 / (nPts - 1);
        for (i = 0; i < nPts; i++) {
            y = 50;
            if (doRandomHeights) y += (int) (50 * Math.random());
            Color color = curveColor[i % curveColor.length];
            Dot xi = new Dot("x" + i, x, 350, true, false, color);
            addDrawable("x" + i, xi);
            Dot yi = new Dot("y" + i, x, 350 - y, false, true, color);
            yi.xSrc = xi;
            addDrawable("y" + i, yi);
            Line li = new Line("l" + i, xi, yi, color);
            addDrawable("l" + i, li);
            x += xStep;
        }
        lagpoly = new double[nPts][nPts];
        intpoly = new double[nPts];
        slopes = new double[nPts];
        repaint();
    }

    public static void main(String[] args) {
        LagrangePanel p = new LagrangePanel(500, 500);
        p.init(5);
        KFrame fr = new KFrame("Lagrange's Interpolation Formula");
        fr.add("Center", p);
        fr.pack();
        fr.show();
    }
}
