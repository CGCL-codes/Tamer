package com.xerox.VTM.glyphs;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

/**
 * Translucent Circle. This version is less efficient than VCircle, but it can be made translucent.
 * @author Emmanuel Pietriga
 *@see com.xerox.VTM.glyphs.VCircle
 *@see com.xerox.VTM.glyphs.VEllipse
 *@see com.xerox.VTM.glyphs.VEllipseST
 **/
public class VCircleST extends VCircle implements Translucent {

    AlphaComposite acST;

    float alpha = 0.5f;

    public VCircleST() {
        super();
        acST = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    }

    /**
     *@param x coordinate in virtual space
     *@param y coordinate in virtual space
     *@param z z-index (pass 0 if you do not use z-ordering)
     *@param r radius in virtual space
     *@param c fill color
     */
    public VCircleST(long x, long y, int z, long r, Color c) {
        super(x, y, z, r, c);
        acST = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    }

    /**
     *@param x coordinate in virtual space
     *@param y coordinate in virtual space
     *@param z z-index (pass 0 if you do not use z-ordering)
     *@param r radius in virtual space
     *@param c fill color
     *@param bc fill color
     *@param a in [0;1.0]. 0 is fully transparent, 1 is opaque
     */
    public VCircleST(long x, long y, int z, long r, Color c, Color bc, float a) {
        super(x, y, z, r, c, bc);
        alpha = a;
        acST = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    }

    public void setTranslucencyValue(float a) {
        alpha = a;
        acST = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        try {
            vsm.repaintNow();
        } catch (NullPointerException e) {
        }
    }

    public float getTranslucencyValue() {
        return alpha;
    }

    public boolean fillsView(long w, long h, int camIndex) {
        if ((alpha == 1.0) && (Math.sqrt(Math.pow(w - pc[camIndex].cx, 2) + Math.pow(h - pc[camIndex].cy, 2)) <= pc[camIndex].cr) && (Math.sqrt(Math.pow(pc[camIndex].cx, 2) + Math.pow(h - pc[camIndex].cy, 2)) <= pc[camIndex].cr) && (Math.sqrt(Math.pow(w - pc[camIndex].cx, 2) + Math.pow(pc[camIndex].cy, 2)) <= pc[camIndex].cr) && (Math.sqrt(Math.pow(pc[camIndex].cx, 2) + Math.pow(pc[camIndex].cy, 2)) <= pc[camIndex].cr)) {
            return true;
        } else {
            return false;
        }
    }

    public void draw(Graphics2D g, int vW, int vH, int i, Stroke stdS, AffineTransform stdT, int dx, int dy) {
        if (alpha == 0) {
            return;
        }
        if (pc[i].cr > 1) {
            if (alpha < 1.0f) {
                g.setComposite(acST);
                if (filled) {
                    g.setColor(this.color);
                    g.fillOval(dx + pc[i].cx - pc[i].cr, dy + pc[i].cy - pc[i].cr, 2 * pc[i].cr, 2 * pc[i].cr);
                }
                if (paintBorder) {
                    g.setColor(borderColor);
                    if (stroke != null) {
                        g.setStroke(stroke);
                        g.drawOval(dx + pc[i].cx - pc[i].cr, dy + pc[i].cy - pc[i].cr, 2 * pc[i].cr, 2 * pc[i].cr);
                        g.setStroke(stdS);
                    } else {
                        g.drawOval(dx + pc[i].cx - pc[i].cr, dy + pc[i].cy - pc[i].cr, 2 * pc[i].cr, 2 * pc[i].cr);
                    }
                }
                g.setComposite(acO);
            } else {
                if (filled) {
                    g.setColor(this.color);
                    g.fillOval(dx + pc[i].cx - pc[i].cr, dy + pc[i].cy - pc[i].cr, 2 * pc[i].cr, 2 * pc[i].cr);
                }
                if (paintBorder) {
                    g.setColor(borderColor);
                    if (stroke != null) {
                        g.setStroke(stroke);
                        g.drawOval(dx + pc[i].cx - pc[i].cr, dy + pc[i].cy - pc[i].cr, 2 * pc[i].cr, 2 * pc[i].cr);
                        g.setStroke(stdS);
                    } else {
                        g.drawOval(dx + pc[i].cx - pc[i].cr, dy + pc[i].cy - pc[i].cr, 2 * pc[i].cr, 2 * pc[i].cr);
                    }
                }
            }
        } else {
            g.setColor(this.color);
            if (alpha < 1.0f) {
                g.setComposite(acST);
                g.fillRect(dx + pc[i].cx, dy + pc[i].cy, 1, 1);
                g.setComposite(acO);
            } else {
                g.fillRect(dx + pc[i].cx, dy + pc[i].cy, 1, 1);
            }
        }
    }

    public void drawForLens(Graphics2D g, int vW, int vH, int i, Stroke stdS, AffineTransform stdT, int dx, int dy) {
        if (alpha == 0) {
            return;
        }
        if (pc[i].lcr > 1) {
            if (alpha < 1.0f) {
                g.setComposite(acST);
                if (filled) {
                    g.setColor(this.color);
                    g.fillOval(dx + pc[i].lcx - pc[i].lcr, dy + pc[i].lcy - pc[i].lcr, 2 * pc[i].lcr, 2 * pc[i].lcr);
                }
                if (paintBorder) {
                    g.setColor(borderColor);
                    if (stroke != null) {
                        g.setStroke(stroke);
                        g.drawOval(dx + pc[i].lcx - pc[i].lcr, dy + pc[i].lcy - pc[i].lcr, 2 * pc[i].lcr, 2 * pc[i].lcr);
                        g.setStroke(stdS);
                    } else {
                        g.drawOval(dx + pc[i].lcx - pc[i].lcr, dy + pc[i].lcy - pc[i].lcr, 2 * pc[i].lcr, 2 * pc[i].lcr);
                    }
                }
                g.setComposite(acO);
            } else {
                if (filled) {
                    g.setColor(this.color);
                    g.fillOval(dx + pc[i].lcx - pc[i].lcr, dy + pc[i].lcy - pc[i].lcr, 2 * pc[i].lcr, 2 * pc[i].lcr);
                }
                if (paintBorder) {
                    g.setColor(borderColor);
                    if (stroke != null) {
                        g.setStroke(stroke);
                        g.drawOval(dx + pc[i].lcx - pc[i].lcr, dy + pc[i].lcy - pc[i].lcr, 2 * pc[i].lcr, 2 * pc[i].lcr);
                        g.setStroke(stdS);
                    } else {
                        g.drawOval(dx + pc[i].lcx - pc[i].lcr, dy + pc[i].lcy - pc[i].lcr, 2 * pc[i].lcr, 2 * pc[i].lcr);
                    }
                }
            }
        } else {
            g.setColor(this.color);
            if (alpha < 1.0f) {
                g.setComposite(acST);
                g.fillRect(dx + pc[i].lcx, dy + pc[i].lcy, 1, 1);
                g.setComposite(acO);
            } else {
                g.fillRect(dx + pc[i].lcx, dy + pc[i].lcy, 1, 1);
            }
        }
    }

    public Object clone() {
        VCircleST res = new VCircleST(vx, vy, 0, vr, color, borderColor, alpha);
        res.mouseInsideColor = this.mouseInsideColor;
        return res;
    }
}
