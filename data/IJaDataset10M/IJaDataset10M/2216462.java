package danlib;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Tool {

    public static boolean randomBool() {
        if (Math.random() > .5) return true; else return false;
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
        }
    }

    public static double dist(DVec2 a, DVec2 b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public static int dist(IVec2 a, IVec2 b) {
        return (int) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public static double dist(double x, double y, DVec2 b) {
        return Math.sqrt(Math.pow(x - b.x, 2) + Math.pow(y - b.y, 2));
    }

    public static double dist(double ax, double ay, double bx, double by) {
        return Math.sqrt(Math.pow(ax - bx, 2) + Math.pow(ay - by, 2));
    }

    public static double torusDist(double x1, double y1, double x2, double y2, double X, double Y) {
        return Math.sqrt(Math.pow(Math.min(Math.abs(x1 - x2), X - Math.abs(x1 - x2)), 2) + Math.pow(Math.min(Math.abs(y1 - y2), Y - Math.abs(y1 - y2)), 2));
    }

    public static double torusAngle(double x1, double y1, double x2, double y2, double X, double Y) {
        double d1 = dist(x1, y1, x2, y2);
        double d2 = dist(x1, y1, x2 - X, y2);
        double d3 = dist(x1, y1, x2, y2 - Y);
        double d4 = dist(x1, y1, x2 + X, y2);
        double d5 = dist(x1, y1, x2, y2 + Y);
        double closest = Math.min(Math.min(Math.min(Math.min(d1, d2), d3), d4), d5);
        if (d1 == closest) return Math.atan2((y1 - y2), (x1 - x2));
        if (d2 == closest) return Math.atan2((y1 - y2), (x1 - (x2 - X)));
        if (d3 == closest) return Math.atan2((y1 - (y2 - Y)), (x1 - x2));
        if (d4 == closest) return Math.atan2((y1 - y2), (x1 - (x2 + X))); else return Math.atan2((y1 - (y2 + Y)), (x1 - x2));
    }

    public static DVec2 lineLine(DVec2 v1, DVec2 v2, DVec2 v3, DVec2 v4) {
        double denom = ((v4.y - v3.y) * (v2.x - v1.x)) - ((v4.x - v3.x) * (v2.y - v1.y));
        double numerator = ((v4.x - v3.x) * (v1.y - v3.y)) - ((v4.y - v3.y) * (v1.x - v3.x));
        double numerator2 = ((v2.x - v1.x) * (v1.y - v3.y)) - ((v2.y - v1.y) * (v1.x - v3.x));
        if (denom == 0.0f) {
            if (numerator == 0.0f && numerator2 == 0.0f) {
                return null;
            }
            return null;
        }
        double ua = numerator / denom;
        double ub = numerator2 / denom;
        if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
            return add(v1, mul(sub(v2, v1), ua));
        } else return null;
    }

    public static DVec2 add(DVec2 a, DVec2 b) {
        return new DVec2(a.x + b.x, a.y + b.y);
    }

    public static DVec2 sub(DVec2 a, DVec2 b) {
        return new DVec2(a.x - b.x, a.y - b.y);
    }

    public static DVec2 mul(DVec2 a, double b) {
        return new DVec2(a.x * b, a.y * b);
    }

    public static double wrap(double n, double b, double t) {
        if (n < b) n = t;
        if (n > t) n = b;
        return n;
    }

    public static int bound(int n, int b, int t) {
        int nn = n;
        if (nn < b) nn = b;
        if (nn > t) nn = t;
        return nn;
    }

    public static double bound(double n, double b, double t) {
        double nn = n;
        if (nn < b) nn = b;
        if (nn > t) nn = t;
        return nn;
    }

    public static double getAngle(DVec2 p1, DVec2 p2) {
        return Math.atan2((p1.y - p2.y), (p1.x - p2.x));
    }

    public static double getAngle(double p1x, double p1y, double p2x, double p2y) {
        return Math.atan2((p1y - p2y), (p1x - p2x));
    }

    public static double cot(double t) {
        return Math.tan(Math.PI * .5 - t);
    }

    public static void AAlias(Graphics2D gr) {
        RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        gr.setRenderingHints(renderHints);
    }

    public static void setAlpha(float a, Graphics2D g2t) {
        int type = AlphaComposite.SRC_OVER;
        AlphaComposite composite = AlphaComposite.getInstance(type, a);
        g2t.setComposite(composite);
    }

    public static BufferedImage optimizeImage(Image imga) {
        BufferedImage img = null;
        if (img instanceof BufferedImage) {
            img = (BufferedImage) imga;
        } else {
            img = new BufferedImage(imga.getWidth(null), imga.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.drawImage(imga, 0, 0, null);
            g2.dispose();
        }
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        boolean istransparent = img.getColorModel().hasAlpha();
        BufferedImage img2 = gc.createCompatibleImage(img.getWidth(), img.getHeight(), istransparent ? Transparency.BITMASK : Transparency.OPAQUE);
        Graphics2D g = img2.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return img2;
    }
}
