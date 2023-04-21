package com.xerox.VTM.glyphs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import com.xerox.VTM.engine.VirtualSpaceManager;

/**
 * Re-orientable Standalone Text.  This version is less efficient than VText, but it can be reoriented.<br>
 * Font properties are set globally in the view, but can be changed on a per-instance basis using setSpecialFont(Font f).<br>
 * (vx, vy) are the coordinates of the lower-left corner, or lower middle point, or lower-right corner depending on the text anchor (start, middle, end).
 * @author Emmanuel Pietriga
 *@see com.xerox.VTM.glyphs.VText
 *@see com.xerox.VTM.glyphs.LText
 *@see com.xerox.VTM.glyphs.LBText
 *@see net.claribole.zvtm.glyphs.VTextST
 *@see net.claribole.zvtm.glyphs.VTextOrST
 */
public class VTextOr extends VText {

    float vw, vh;

    public VTextOr(String t, float or) {
        super(t);
        orient = or;
    }

    /**
     *@param x coordinate in virtual space
     *@param y coordinate in virtual space
     *@param z z-index (pass 0 if you do not use z-ordering)
     *@param c fill color
     *@param t text string
     *@param or orientation
     */
    public VTextOr(long x, long y, int z, Color c, String t, float or) {
        super(x, y, z, c, t);
        orient = or;
    }

    /**
     *@param x coordinate in virtual space
     *@param y coordinate in virtual space
     *@param z z-index (pass 0 if you do not use z-ordering)
     *@param c fill color
     *@param t text string
     *@param or orientation
     *@param ta text-anchor (for alignment: one of VText.TEXT_ANCHOR_*)
     */
    public VTextOr(long x, long y, int z, Color c, String t, float or, short ta) {
        super(x, y, z, c, t, ta);
        orient = or;
    }

    /**
     *@param x coordinate in virtual space
     *@param y coordinate in virtual space
     *@param z z-index (pass 0 if you do not use z-ordering)
     *@param c fill color
     *@param t text string
     *@param or orientation
     *@param ta text-anchor (for alignment: one of VText.TEXT_ANCHOR_*)
     *@param scale scaleFactor w.r.t original image size
     */
    public VTextOr(long x, long y, int z, Color c, String t, float or, short ta, float scale) {
        super(x, y, z, c, t, ta);
        orient = or;
        scaleFactor = scale;
    }

    public void orientTo(float angle) {
        orient = angle;
        invalidate();
        try {
            vsm.repaintNow();
        } catch (NullPointerException e) {
        }
    }

    public boolean fillsView(long w, long h, int camIndex) {
        return false;
    }

    public void draw(Graphics2D g, int vW, int vH, int i, Stroke stdS, AffineTransform stdT, int dx, int dy) {
        g.setColor(this.color);
        trueCoef = scaleFactor * coef;
        if (trueCoef * fontSize > vsm.getTextDisplayedAsSegCoef() || !zoomSensitive) {
            g.setFont((font != null) ? font : VirtualSpaceManager.getMainFont());
            if (!pc[i].valid) {
                bounds = g.getFontMetrics().getStringBounds(text, g);
                pc[i].cw = (int) Math.abs(Math.round(bounds.getWidth() * scaleFactor));
                pc[i].ch = (int) Math.abs(Math.round(bounds.getHeight() * scaleFactor));
                pc[i].valid = true;
            }
            if (text_anchor == TEXT_ANCHOR_START) {
                at = AffineTransform.getTranslateInstance(dx + pc[i].cx, pc[i].cy);
                if (zoomSensitive) {
                    at.concatenate(AffineTransform.getScaleInstance(trueCoef, trueCoef));
                }
                if (orient != 0) {
                    at.concatenate(AffineTransform.getRotateInstance(-orient));
                }
            } else if (text_anchor == TEXT_ANCHOR_MIDDLE) {
                at = AffineTransform.getTranslateInstance(dx + pc[i].cx, dy + pc[i].cy);
                if (zoomSensitive) {
                    at.concatenate(AffineTransform.getScaleInstance(trueCoef, trueCoef));
                }
                if (orient != 0) {
                    at.concatenate(AffineTransform.getRotateInstance(-orient));
                }
                at.concatenate(AffineTransform.getTranslateInstance(-pc[i].cw / 2.0f / scaleFactor, 0));
            } else {
                at = AffineTransform.getTranslateInstance(dx + pc[i].cx, dy + pc[i].cy);
                if (zoomSensitive) {
                    at.concatenate(AffineTransform.getScaleInstance(trueCoef, trueCoef));
                }
                if (orient != 0) {
                    at.concatenate(AffineTransform.getRotateInstance(-orient));
                }
                at.concatenate(AffineTransform.getTranslateInstance(-pc[i].cw / scaleFactor, 0));
            }
            g.setTransform(at);
            g.drawString(text, 0.0f, 0.0f);
            g.setTransform(stdT);
        } else {
            g.fillRect(dx + pc[i].cx, pc[i].cy, 1, 1);
        }
    }

    public void drawForLens(Graphics2D g, int vW, int vH, int i, Stroke stdS, AffineTransform stdT, int dx, int dy) {
        g.setColor(this.color);
        trueCoef = scaleFactor * coef;
        if (trueCoef * fontSize > vsm.getTextDisplayedAsSegCoef() || !zoomSensitive) {
            g.setFont((font != null) ? font : VirtualSpaceManager.getMainFont());
            if (!pc[i].lvalid) {
                bounds = g.getFontMetrics().getStringBounds(text, g);
                pc[i].lcw = (int) Math.abs(Math.round(bounds.getWidth() * scaleFactor));
                pc[i].lch = (int) Math.abs(Math.round(bounds.getHeight() * scaleFactor));
                pc[i].lvalid = true;
            }
            if (text_anchor == TEXT_ANCHOR_START) {
                at = AffineTransform.getTranslateInstance(dx + pc[i].lcx, pc[i].lcy);
                if (zoomSensitive) {
                    at.concatenate(AffineTransform.getScaleInstance(trueCoef, trueCoef));
                }
                if (orient != 0) {
                    at.concatenate(AffineTransform.getRotateInstance(-orient));
                }
            } else if (text_anchor == TEXT_ANCHOR_MIDDLE) {
                at = AffineTransform.getTranslateInstance(dx + pc[i].lcx, dy + pc[i].lcy);
                if (zoomSensitive) {
                    at.concatenate(AffineTransform.getScaleInstance(trueCoef, trueCoef));
                }
                if (orient != 0) {
                    at.concatenate(AffineTransform.getRotateInstance(-orient));
                }
                at.concatenate(AffineTransform.getTranslateInstance(-pc[i].lcw / 2.0f / scaleFactor, 0));
            } else {
                at = AffineTransform.getTranslateInstance(dx + pc[i].lcx, dy + pc[i].lcy);
                if (zoomSensitive) {
                    at.concatenate(AffineTransform.getScaleInstance(trueCoef, trueCoef));
                }
                if (orient != 0) {
                    at.concatenate(AffineTransform.getRotateInstance(-orient));
                }
                at.concatenate(AffineTransform.getTranslateInstance(-pc[i].lcw / scaleFactor, 0));
            }
            g.setTransform(at);
            g.drawString(text, 0.0f, 0.0f);
            g.setTransform(stdT);
        } else {
            g.fillRect(dx + pc[i].lcx, pc[i].lcy, 1, 1);
        }
    }

    public Object clone() {
        VTextOr res = new VTextOr(vx, vy, 0, color, (new StringBuffer(text)).toString(), orient, text_anchor);
        res.mouseInsideColor = this.mouseInsideColor;
        return res;
    }
}
