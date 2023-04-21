package com.xerox.VTM.glyphs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

/**
 * Rectangle implementing basic semantic zooming, in the sense that it has minSize and maxSize attributes controlling its visibility.
 * Cannot be reoriented.
 * @author Emmanuel Pietriga
 *@see com.xerox.VTM.glyphs.VRectangle
 **/
public class SZRectangle extends VRectangle {

    int minSize, maxSize;

    public SZRectangle(int mns, int mxs) {
        super();
        minSize = mns;
        maxSize = mxs;
    }

    /**
     *@param x coordinate in virtual space
     *@param y coordinate in virtual space
     *@param z z-index (pass 0 if you do not use z-ordering)
     *@param w half width in virtual space
     *@param h half height in virtual space
     *@param c fill color
     *@param bc border color
     *@param mns minimum width and height in View below which the glyph is no longer displayed  (in pixels)
     *@param mxs maximum width and height in View above which the glyph is no longer displayed  (in pixels)
     */
    public SZRectangle(long x, long y, int z, long w, long h, Color c, Color bc, int mns, int mxs) {
        super(x, y, z, w, h, c, bc);
        minSize = mns;
        maxSize = mxs;
    }

    public void draw(Graphics2D g, int vW, int vH, int i, Stroke stdS, AffineTransform stdT, int dx, int dy) {
        if ((pc[i].cw >= minSize) && (pc[i].ch >= minSize) && (pc[i].cw <= maxSize) && (pc[i].ch <= maxSize)) {
            if (filled) {
                g.setColor(this.color);
                g.fillRect(dx + pc[i].cx - pc[i].cw, dy + pc[i].cy - pc[i].ch, 2 * pc[i].cw, 2 * pc[i].ch);
            }
            if (paintBorder) {
                g.setColor(borderColor);
                if (stroke != null) {
                    if (((pc[i].cx - pc[i].cw) > 0) || ((pc[i].cy - pc[i].ch) > 0) || ((2 * pc[i].cw - 1) < vW) || ((2 * pc[i].ch - 1) < vH)) {
                        g.setStroke(stroke);
                        g.drawRect(dx + pc[i].cx - pc[i].cw, dy + pc[i].cy - pc[i].ch, 2 * pc[i].cw - 1, 2 * pc[i].ch - 1);
                        g.setStroke(stdS);
                    }
                } else {
                    g.drawRect(dx + pc[i].cx - pc[i].cw, dy + pc[i].cy - pc[i].ch, 2 * pc[i].cw - 1, 2 * pc[i].ch - 1);
                }
            }
        }
    }

    public void drawForLens(Graphics2D g, int vW, int vH, int i, Stroke stdS, AffineTransform stdT, int dx, int dy) {
        if ((pc[i].lcw >= minSize) && (pc[i].lch >= minSize) && (pc[i].lcw <= maxSize) && (pc[i].lch <= maxSize)) {
            if (filled) {
                g.setColor(this.color);
                g.fillRect(dx + pc[i].lcx - pc[i].lcw, dy + pc[i].lcy - pc[i].lch, 2 * pc[i].lcw, 2 * pc[i].lch);
            }
            if (paintBorder) {
                g.setColor(borderColor);
                if (stroke != null) {
                    if (((pc[i].lcx - pc[i].lcw) > 0) || ((dy + pc[i].lcy - pc[i].lch) > 0) || ((2 * pc[i].lcw - 1) < vW) || ((2 * pc[i].lch - 1) < vH)) {
                        g.setStroke(stroke);
                        g.drawRect(dx + pc[i].lcx - pc[i].lcw, dy + pc[i].lcy - pc[i].lch, 2 * pc[i].lcw - 1, 2 * pc[i].lch - 1);
                        g.setStroke(stdS);
                    }
                } else {
                    g.drawRect(dx + pc[i].lcx - pc[i].lcw, dy + pc[i].lcy - pc[i].lch, 2 * pc[i].lcw - 1, 2 * pc[i].lch - 1);
                }
            }
        }
    }

    public Object clone() {
        SZRectangle res = new SZRectangle(vx, vy, 0, vw, vh, color, borderColor, minSize, maxSize);
        res.borderColor = this.borderColor;
        res.mouseInsideColor = this.mouseInsideColor;
        res.bColor = this.bColor;
        return res;
    }
}
