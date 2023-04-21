package com.arykow.applications.ugabe.standalone;

import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

class TriGradientPaintContext implements PaintContext {

    private static final ColorModel cmodel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8 }, false, false, Transparency.OPAQUE, DataBuffer.TYPE_INT);

    private final Point[] point;

    private final Color[] color;

    public TriGradientPaintContext(Point[] points, Color[] colors) {
        if (!(points.length == 3)) throw new Error("Assertion failed: " + "points.length == 3");
        if (!(colors.length == 3)) throw new Error("Assertion failed: " + "colors.length == 3");
        point = points;
        color = colors;
    }

    public void dispose() {
    }

    public ColorModel getColorModel() {
        return cmodel;
    }

    public void transform(AffineTransform xform) {
        double[] dsrc = new double[2];
        double[] ddst = new double[2];
        for (int i = 0; i < 3; ++i) {
            dsrc[0] = point[i].x;
            dsrc[1] = point[i].y;
            xform.transform(dsrc, 0, ddst, 0, 1);
            point[i].x = (int) Math.round(ddst[0]);
            point[i].y = (int) Math.round(ddst[1]);
        }
    }

    public Raster getRaster(int x, int y, int w, int h) {
        WritableRaster wr = cmodel.createCompatibleWritableRaster(w, h);
        int[] c = new int[3];
        double md1, md2, d1, d2, px, py;
        double a1, a2, b1, b2, c1, c2, denom;
        md1 = Point2D.distance((point[0].x), (point[0].y), (point[1].x), (point[1].y));
        md2 = Point2D.distance((point[0].x), (point[0].y), (point[2].x), (point[2].y));
        a1 = (point[1].y) - (point[0].y);
        b1 = (point[0].x) - (point[1].x);
        c1 = (point[1].x) * (point[0].y) - (point[0].x) * (point[1].y);
        a2 = (point[2].y) - (point[0].y);
        b2 = (point[0].x) - (point[2].x);
        denom = a1 * b2 - a2 * b1;
        if (!(denom != 0)) throw new Error("Assertion failed: " + "denom != 0");
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                px = x + i;
                py = y + j;
                c2 = -(py * b2 + px * a2);
                double ix = (b1 * c2 - b2 * c1) / denom;
                double iy = (a2 * c1 - a1 * c2) / denom;
                d1 = Point2D.distance((point[0].x), (point[0].y), ix, iy);
                d2 = Point2D.distance(px, py, ix, iy);
                int w1, w2, w3;
                w2 = (int) Math.round((256.0 * d1) / md1);
                w3 = (int) Math.round((256.0 * d2) / md2);
                w1 = 256 - w2 - w3;
                c[0] = ((color[0].getRed() * w1) + (color[1].getRed() * w2) + (color[2].getRed() * w3)) / 256;
                c[1] = ((color[0].getGreen() * w1) + (color[1].getGreen() * w2) + (color[2].getGreen() * w3)) / 256;
                c[2] = ((color[0].getBlue() * w1) + (color[1].getBlue() * w2) + (color[2].getBlue() * w3)) / 256;
                wr.setPixel(i, j, c);
            }
        }
        return wr;
    }
}

public class TriGradientPaint implements Paint {

    private TriGradientPaintContext pcontext;

    public TriGradientPaint(Point[] points, Color[] colors) {
        if (points.length != 3) throw new IllegalArgumentException("points array must have size 3");
        if (colors.length != 3) throw new IllegalArgumentException("colors array must have size 3");
        pcontext = new TriGradientPaintContext(points, colors);
    }

    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
        pcontext.transform(xform);
        return pcontext;
    }

    public int getTransparency() {
        return OPAQUE;
    }
}
