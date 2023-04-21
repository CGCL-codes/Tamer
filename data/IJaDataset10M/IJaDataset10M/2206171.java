package net.claribole.zvtm.glyphs;

import com.xerox.VTM.glyphs.Translucent;
import com.xerox.VTM.glyphs.VTextOr;
import com.xerox.VTM.engine.VirtualSpaceManager;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

/**
 * Translucent Re-orientable Standalone Text. This version is less efficient than all others, but it can be reoriented and made translucent.<br>
 * Font properties are set globally in the view, but can be changed on a per-instance basis using setSpecialFont(Font f).<br>
 * (vx, vy) are the coordinates of the lower-left corner, or lower middle point, or lower-right corner depending on the text anchor (start, middle, end).
 * @author Emmanuel Pietriga
 *@see com.xerox.VTM.glyphs.VText
 *@see com.xerox.VTM.glyphs.VTextOr
 *@see com.xerox.VTM.glyphs.LText
 *@see com.xerox.VTM.glyphs.LBText
 *@see net.claribole.zvtm.glyphs.VTextST
 */
public class VTextOrST extends VTextOr implements Translucent {

    AlphaComposite acST;

    float alpha = 0.5f;

    /**
     *@param t text string
     *@param or orientation
     *@param a alpha channel value in [0;1.0] 0 is fully transparent, 1 is opaque
     */
    public VTextOrST(String t, float or, float a) {
        super(t, or);
        alpha = a;
        acST = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    }

    /**
     *@param x coordinate in virtual space
     *@param y coordinate in virtual space
     *@param z z-index (pass 0 if you do not use z-ordering)
     *@param c fill color
     *@param t text string
     *@param or orientation
     *@param a alpha channel value in [0;1.0] 0 is fully transparent, 1 is opaque
     */
    public VTextOrST(long x, long y, int z, Color c, String t, float or, float a) {
        super(x, y, z, c, t, or);
        alpha = a;
        acST = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    }

    /**
     *@param x coordinate in virtual space
     *@param y coordinate in virtual space
     *@param z z-index (pass 0 if you do not use z-ordering)
     *@param c fill color
     *@param t text string
     *@param or orientation
     *@param ta text-anchor (for alignment: one of VText.TEXT_ANCHOR_*)
     *@param a alpha channel value in [0;1.0] 0 is fully transparent, 1 is opaque
     */
    public VTextOrST(long x, long y, int z, Color c, String t, float or, short ta, float a) {
        super(x, y, z, c, t, or, ta);
        alpha = a;
        acST = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    }

    /**
     *@param x coordinate in virtual space
     *@param y coordinate in virtual space
     *@param z z-index (pass 0 if you do not use z-ordering)
     *@param c fill color
     *@param t text string
     *@param or orientation
     *@param ta text-anchor (for alignment: one of VText.TEXT_ANCHOR_*)
     *@param a alpha channel value in [0;1.0] 0 is fully transparent, 1 is opaque
     *@param scale scaleFactor w.r.t original image size
     */
    public VTextOrST(long x, long y, int z, Color c, String t, float or, short ta, float a, float scale) {
        super(x, y, z, c, t, or, ta);
        alpha = a;
        acST = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        scaleFactor = scale;
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

    public void draw(Graphics2D g, int vW, int vH, int i, Stroke stdS, AffineTransform stdT, int dx, int dy) {
        if (!pc[i].valid) {
            g.setFont((font != null) ? font : VirtualSpaceManager.getMainFont());
            bounds = g.getFontMetrics().getStringBounds(text, g);
            pc[i].cw = (int) Math.abs(Math.round(bounds.getWidth() * scaleFactor));
            pc[i].ch = (int) Math.abs(Math.round(bounds.getHeight() * scaleFactor));
            pc[i].valid = true;
        }
        if (alpha == 0) {
            return;
        }
        g.setColor(this.color);
        trueCoef = scaleFactor * coef;
        if (trueCoef * fontSize > vsm.getTextDisplayedAsSegCoef() || !zoomSensitive) {
            g.setFont((font != null) ? font : VirtualSpaceManager.getMainFont());
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
            if (alpha < 1.0f) {
                g.setComposite(acST);
                g.drawString(text, 0.0f, 0.0f);
                g.setComposite(acO);
            } else {
                g.drawString(text, 0.0f, 0.0f);
            }
            g.setTransform(stdT);
        } else {
            g.fillRect(dx + pc[i].cx, pc[i].cy, 1, 1);
        }
    }

    public void drawForLens(Graphics2D g, int vW, int vH, int i, Stroke stdS, AffineTransform stdT, int dx, int dy) {
        if (!pc[i].lvalid) {
            g.setFont((font != null) ? font : VirtualSpaceManager.getMainFont());
            bounds = g.getFontMetrics().getStringBounds(text, g);
            pc[i].lcw = (int) Math.abs(Math.round(bounds.getWidth() * scaleFactor));
            pc[i].lch = (int) Math.abs(Math.round(bounds.getHeight() * scaleFactor));
            pc[i].lvalid = true;
        }
        if (alpha == 0) {
            return;
        }
        g.setColor(this.color);
        trueCoef = scaleFactor * coef;
        if (trueCoef * fontSize > vsm.getTextDisplayedAsSegCoef() || !zoomSensitive) {
            g.setFont((font != null) ? font : VirtualSpaceManager.getMainFont());
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
            if (alpha < 1.0f) {
                g.setComposite(acST);
                g.drawString(text, 0.0f, 0.0f);
                g.setComposite(acO);
            } else {
                g.drawString(text, 0.0f, 0.0f);
            }
            g.setTransform(stdT);
        } else {
            g.fillRect(dx + pc[i].lcx, pc[i].lcy, 1, 1);
        }
    }

    public Object clone() {
        VTextOrST res = new VTextOrST(vx, vy, 0, color, (new StringBuffer(text)).toString(), orient, text_anchor, alpha);
        res.mouseInsideColor = this.mouseInsideColor;
        return res;
    }
}
