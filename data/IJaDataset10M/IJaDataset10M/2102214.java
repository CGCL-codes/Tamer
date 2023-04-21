package org.fpdev.apps.admin.gui.map;

import org.fpdev.util.gui.MapCoordinates;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.fpdev.core.basenet.BaseNetwork;
import org.fpdev.core.basenet.BLink;
import org.fpdev.util.FPUtil;

/**
 *
 * @author demory
 */
public class MapCanvas {

    private Graphics2D g2d_;

    private MapCoordinates coords_;

    private BaseNetwork baseNet_;

    public static final int NODEDISP_HIDE = 1;

    public static final int NODEDISP_STANDARD = 2;

    public static final int NODEDISP_ID = 3;

    public static final int NODEDISP_EDITONLY = 4;

    public static final int NODEDISP_SHPPT = 5;

    public static final int LINKDISP_STANDARD = 1;

    public static final int LINKDISP_ERRNAME = 2;

    public static final int LINKDISP_ERRADDR = 3;

    private int nodeDispMode_, linkDispMode_;

    /** Creates a new instance of MapCanvas */
    public MapCanvas(Graphics2D g2d, MapCoordinates cc, BaseNetwork baseNet) {
        g2d_ = g2d;
        coords_ = cc;
        baseNet_ = baseNet;
        nodeDispMode_ = NODEDISP_EDITONLY;
        linkDispMode_ = LINKDISP_ERRNAME;
    }

    public void drawTextSpecial(String text) {
        g2d_.setColor(Color.BLUE);
        g2d_.setFont(new Font("Dialog", Font.BOLD, 500));
        g2d_.drawChars(text.toCharArray(), 0, text.length(), text.length() == 2 ? 25 : 175, 475);
    }

    public BaseNetwork getBaseNet() {
        return baseNet_;
    }

    public void setBaseNet(BaseNetwork baseNet) {
        baseNet_ = baseNet;
    }

    public Graphics2D getGraphics() {
        return g2d_;
    }

    public void setGraphics(Graphics2D g2d) {
        g2d_ = g2d;
    }

    public MapCoordinates getCC() {
        return coords_;
    }

    public void setColor(Color c) {
        g2d_.setColor(c);
    }

    public void setStroke(Stroke s) {
        g2d_.setStroke(s);
    }

    public void setLineWidth(int w) {
        g2d_.setStroke(new BasicStroke(w));
    }

    public int getNodeDispMode() {
        return nodeDispMode_;
    }

    public void setNodeDispMode(int mode) {
        nodeDispMode_ = mode;
    }

    public int getLinkDispMode() {
        return linkDispMode_;
    }

    public void setLinkDispMode(int mode) {
        linkDispMode_ = mode;
    }

    public boolean getShowNodeIDs() {
        return nodeDispMode_ == NODEDISP_ID;
    }

    public void drawPoint(double x, double y, int r) {
        double gx = coords_.xToScreen(x);
        double gy = coords_.yToScreen(y);
        int gxi = (int) gx;
        int gyi = (int) gy;
        if (g2d_ != null) {
            g2d_.fillOval(gxi - r / 2, gyi - r / 2, r, r);
        }
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        drawLine(x1, y1, x2, y2, false);
    }

    public void drawPolyline(double[] xArrW, double[] yArrW, int numPts) {
        int[] xArrS = new int[numPts];
        int[] yArrS = new int[numPts];
        for (int i = 0; i < numPts; i++) {
            xArrS[i] = coords_.xToScreen(xArrW[i]);
            yArrS[i] = coords_.yToScreen(yArrW[i]);
        }
        if (g2d_ != null) {
            g2d_.drawPolyline(xArrS, yArrS, numPts);
        }
    }

    public void drawLine(double x1, double y1, double x2, double y2, boolean drawArrow) {
        double gx1 = coords_.xToScreen(x1);
        double gy1 = coords_.yToScreen(y1);
        double gx2 = coords_.xToScreen(x2);
        double gy2 = coords_.yToScreen(y2);
        int gx1i = (int) gx1;
        int gy1i = (int) gy1;
        int gx2i = (int) gx2;
        int gy2i = (int) gy2;
        if (g2d_ != null) {
            g2d_.drawLine(gx1i, gy1i, gx2i, gy2i);
        }
        if (drawArrow) {
            double mx = (gx1 + gx2) / 2, my = (gy1 + gy2) / 2;
            double theta = Math.acos((gx2 - gx1) / FPUtil.magnitude(gx1, gy1, gx2, gy2));
            if (gx1 < gx2 && gy2 <= gy1 || gx2 <= gx1 && gy2 < gy1) {
                theta = 2 * Math.PI - theta;
            }
            double sintheta = Math.sin(theta);
            double costheta = Math.cos(theta);
            double len = FPUtil.magnitude(gx1, gy1, gx2, gy2) * .375;
            if (len > 8) {
                len = 8;
            }
            double tx1 = mx + len * costheta, ty1 = my + len * sintheta;
            double txi = mx - len * costheta, tyi = my - len * sintheta;
            double tx2 = txi + (len / 2) * Math.cos(theta + Math.PI / 2), ty2 = tyi + (len / 2) * Math.sin(theta + Math.PI / 2);
            double tx3 = txi + (len / 2) * Math.cos(theta - Math.PI / 2), ty3 = tyi + (len / 2) * Math.sin(theta - Math.PI / 2);
            int[] xArr = { (int) tx1, (int) tx2, (int) tx3 };
            int[] yArr = { (int) ty1, (int) ty2, (int) ty3 };
            g2d_.fillPolygon(xArr, yArr, 3);
        }
    }

    public void drawArrow(BLink link, double maxLen, boolean revDir) {
        this.drawArrow(link, maxLen, revDir, null);
    }

    public void drawArrow(BLink link, double maxLen, boolean revDir, Color color) {
        drawArrow(link, maxLen, revDir, color, 0, null);
    }

    public void drawArrow(BLink link, double maxLen, boolean revDir, Color color, int borderWidth, Color borderColor) {
        double x1 = link.getX1(), y1 = link.getY1(), x2 = link.getX2(), y2 = link.getY2();
        Iterator<Point2D.Double> shpPts = link.getShapePoints();
        if (revDir) {
            x1 = link.getX2();
            y1 = link.getY2();
            x2 = link.getX1();
            y2 = link.getY1();
            List<Point2D.Double> revSP = new LinkedList<Point2D.Double>();
            while (shpPts.hasNext()) {
                revSP.add(0, shpPts.next());
            }
            shpPts = revSP.iterator();
        }
        double gx1 = coords_.xToScreen(x1);
        double gy1 = coords_.yToScreen(y1);
        double gx2 = coords_.xToScreen(x2);
        double gy2 = coords_.yToScreen(y2);
        double len = FPUtil.magnitude(gx1, gy1, gx2, gy2) * .25;
        if (len > maxLen) {
            len = maxLen;
        }
        if (shpPts.hasNext()) {
            double ix1 = x1, iy1 = y1, ix2 = 0, iy2 = 0;
            double fx1 = 0, fy1 = 0, fx2 = 0, fy2 = 0;
            double wlen = link.getLengthFeet(), wlenCovered = 0;
            while (shpPts.hasNext()) {
                java.awt.geom.Point2D.Double shpPt = shpPts.next();
                ix2 = shpPt.getX();
                iy2 = shpPt.getY();
                wlenCovered += FPUtil.magnitude(ix1, iy1, ix2, iy2);
                if (wlenCovered > wlen / 2) {
                    fx1 = ix1;
                    fy1 = iy1;
                    fx2 = ix2;
                    fy2 = iy2;
                    break;
                }
                ix1 = ix2;
                iy1 = iy2;
            }
            if (fx1 == 0) {
                fx1 = ix1;
                fy1 = iy1;
                fx2 = x2;
                fy2 = y2;
            }
            gx1 = coords_.xToScreen(fx1);
            gy1 = coords_.yToScreen(fy1);
            gx2 = coords_.xToScreen(fx2);
            gy2 = coords_.yToScreen(fy2);
        }
        double mx = (gx1 + gx2) / 2, my = (gy1 + gy2) / 2;
        double theta = Math.acos((gx2 - gx1) / FPUtil.magnitude(gx1, gy1, gx2, gy2));
        if (gx1 < gx2 && gy2 <= gy1 || gx2 <= gx1 && gy2 < gy1) {
            theta = 2 * Math.PI - theta;
        }
        double sintheta = Math.sin(theta);
        double costheta = Math.cos(theta);
        double tx1 = mx + len * costheta, ty1 = my + len * sintheta;
        double txi = mx - len * costheta, tyi = my - len * sintheta;
        double width = len * .6;
        double tx2 = txi + (len / 2) * Math.cos(theta + Math.PI / 2), ty2 = tyi + (len / 2) * Math.sin(theta + Math.PI / 2);
        double tx3 = txi + (len / 2) * Math.cos(theta - Math.PI / 2), ty3 = tyi + (len / 2) * Math.sin(theta - Math.PI / 2);
        int[] xArr = { (int) tx1 + 1, (int) tx2 + 1, (int) tx3 + 1 };
        int[] yArr = { (int) ty1 + 1, (int) ty2 + 1, (int) ty3 + 1 };
        if (color != null) {
            g2d_.setColor(color);
        }
        g2d_.fillPolygon(xArr, yArr, 3);
        if (borderWidth > 0) {
            g2d_.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            if (borderColor != null) {
                g2d_.setColor(borderColor);
            }
            g2d_.drawPolygon(xArr, yArr, 3);
        }
    }

    public void drawText(String text, double x, double y, int xo, int yo) {
        drawText(text, 9, x, y, xo, yo);
    }

    public void drawText(String text, int size, double x, double y, int xo, int yo) {
        double gx = coords_.xToScreen(x);
        double gy = coords_.yToScreen(y);
        int gxi = (int) gx;
        int gyi = (int) gy;
        g2d_.setFont(new Font("Dialog", Font.PLAIN, size));
        g2d_.drawChars(text.toCharArray(), 0, text.length(), gxi + xo, gyi + yo);
    }

    public int getMajorCode() {
        int majorCode = 0;
        if (coords_.getResolution() > 100) {
            majorCode = 2;
        } else if (coords_.getResolution() > 70) {
            majorCode = 1;
        }
        return majorCode;
    }
}
