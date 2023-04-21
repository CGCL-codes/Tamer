package net.claribole.zvtm.glyphs;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.Shape;
import com.xerox.VTM.engine.Camera;
import com.xerox.VTM.engine.LongPoint;
import com.xerox.VTM.glyphs.VSlice;
import com.xerox.VTM.glyphs.Translucent;
import net.claribole.zvtm.glyphs.projection.ProjRing;

/**
 * Like a slice, but with iner and outer arcs (slice of a ring instead of slice of a pie)
 * @author Emmanuel Pietriga
 *@see com.xerox.VTM.glyphs.VSlice
 *@see com.xerox.VTM.glyphs.VSliceST
 */
public class VRingST extends VRing implements Translucent {

    AlphaComposite acST;

    float alpha = 0.5f;

    /** Construct a slice by giving its 3 vertices
        *@param v array of 3 points representing the absolute coordinates of the slice's vertices. The first element must be the point that is not an endpoint of the arc 
        *@param irr inner ring radius as a percentage of outer ring radius
		*@param z z-index (pass 0 if you do not use z-ordering)
        *@param c fill color
        *@param bc border color
     	*@param a alpha channel value in [0;1.0] 0 is fully transparent, 1 is opaque
        */
    public VRingST(LongPoint[] v, float irr, int z, Color c, Color bc, float a) {
        super(v, irr, z, c, bc);
        alpha = a;
        acST = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    }

    /** Construct a slice by giving its size, angle and orientation
        *@param x x-coordinate in virtual space of vertex that is not an arc endpoint
        *@param y y-coordinate in virtual space of vertex that is not an arc endpoint
        *@param z z-index (pass 0 if you do not use z-ordering)
        *@param vs arc radius in virtual space
        *@param ag arc angle in virtual space (in rad)
        *@param irr inner ring radius as a percentage of outer ring radius
        *@param or slice orientation in virtual space (interpreted as the orientation of the segment linking the vertex that is not an arc endpoint to the middle of the arc) (in rad)
        *@param c fill color
        *@param bc border color
     	*@param a alpha channel value in [0;1.0] 0 is fully transparent, 1 is opaque
        */
    public VRingST(long x, long y, int z, long vs, double ag, float irr, double or, Color c, Color bc, float a) {
        super(x, y, z, vs, ag, irr, or, c, bc);
        alpha = a;
        acST = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    }

    /** Construct a slice by giving its size, angle and orientation
        *@param x x-coordinate in virtual space of vertex that is not an arc endpoint
        *@param y y-coordinate in virtual space of vertex that is not an arc endpoint 
        *@param z z-index (pass 0 if you do not use z-ordering)
        *@param vs arc radius in virtual space
        *@param ag arc angle in virtual space (in degrees)
        *@param irr inner ring radius as a percentage of outer ring radius
        *@param or slice orientation in virtual space (interpreted as the orientation of the segment linking the vertex that is not an arc endpoint to the middle of the arc)  (in degrees)
        *@param c fill color
        *@param bc border color
     	*@param a alpha channel value in [0;1.0] 0 is fully transparent, 1 is opaque
        */
    public VRingST(long x, long y, int z, long vs, int ag, float irr, int or, Color c, Color bc, float a) {
        super(x, y, z, vs, ag, irr, or, c, bc);
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

    public void draw(Graphics2D g, int vW, int vH, int i, Stroke stdS, AffineTransform stdT, int dx, int dy) {
        if (alpha == 0) {
            return;
        }
        if (pc[i].outerCircleRadius > 2) {
            if (isFilled()) {
                outerSlice.setArc(dx + pc[i].cx - pc[i].outerCircleRadius, dy + pc[i].cy - pc[i].outerCircleRadius, 2 * pc[i].outerCircleRadius, 2 * pc[i].outerCircleRadius, (int) Math.round(orientDeg - angleDeg / 2.0), angleDeg, Arc2D.PIE);
                innerSlice.setFrame(dx + pc[i].cx - pr[i].innerRingRadius, dy + pc[i].cy - pr[i].innerRingRadius, 2 * pr[i].innerRingRadius, 2 * pr[i].innerRingRadius);
                pr[i].ring = new Area(outerSlice);
                subring = new Area(innerSlice);
                pr[i].ring.subtract(subring);
                g.setColor(this.color);
                if (alpha < 1.0f) {
                    g.setComposite(acST);
                    g.fill(pr[i].ring);
                    g.setComposite(acO);
                } else {
                    g.fill(pr[i].ring);
                }
            }
            if (isBorderDrawn()) {
                g.setColor(borderColor);
                if (stroke != null) {
                    g.setStroke(stroke);
                    if (alpha < 1.0f) {
                        g.setComposite(acST);
                        g.draw(pr[i].ring);
                        g.setComposite(acO);
                    } else {
                        g.draw(pr[i].ring);
                    }
                    g.setStroke(stdS);
                } else {
                    if (alpha < 1.0f) {
                        g.setComposite(acST);
                        g.draw(pr[i].ring);
                        g.setComposite(acO);
                    } else {
                        g.draw(pr[i].ring);
                    }
                }
            }
        } else {
            g.setColor(this.color);
            g.fillRect(dx + pc[i].cx, dy + pc[i].cy, 1, 1);
        }
    }

    public void drawForLens(Graphics2D g, int vW, int vH, int i, Stroke stdS, AffineTransform stdT, int dx, int dy) {
        if (alpha == 0) {
            return;
        }
        if (pc[i].louterCircleRadius > 2) {
            if (isFilled()) {
                outerSlice.setArc(dx + pc[i].lcx - pc[i].louterCircleRadius, dy + pc[i].lcy - pc[i].louterCircleRadius, 2 * pc[i].louterCircleRadius, 2 * pc[i].louterCircleRadius, (int) Math.round(orientDeg - angleDeg / 2.0), angleDeg, Arc2D.PIE);
                innerSlice.setFrame(dx + pc[i].lcx - pr[i].linnerRingRadius, dy + pc[i].lcy - pr[i].linnerRingRadius, 2 * pr[i].linnerRingRadius, 2 * pr[i].linnerRingRadius);
                pr[i].lring = new Area(outerSlice);
                subring = new Area(innerSlice);
                pr[i].lring.subtract(subring);
                g.setColor(this.color);
                if (alpha < 1.0f) {
                    g.setComposite(acST);
                    g.fill(pr[i].lring);
                    g.setComposite(acO);
                } else {
                    g.fill(pr[i].lring);
                }
            }
            if (isBorderDrawn()) {
                g.setColor(borderColor);
                if (stroke != null) {
                    g.setStroke(stroke);
                    if (alpha < 1.0f) {
                        g.setComposite(acST);
                        g.draw(pr[i].lring);
                        g.setComposite(acO);
                    } else {
                        g.draw(pr[i].lring);
                    }
                    g.setStroke(stdS);
                } else {
                    if (alpha < 1.0f) {
                        g.setComposite(acST);
                        g.draw(pr[i].lring);
                        g.setComposite(acO);
                    } else {
                        g.draw(pr[i].lring);
                    }
                }
            }
        } else {
            if (alpha < 1.0f) {
                g.setComposite(acST);
                g.setColor(this.color);
                g.fillRect(dx + pc[i].lcx, dy + pc[i].lcy, 1, 1);
                g.setComposite(acO);
            } else {
                g.setColor(this.color);
                g.fillRect(dx + pc[i].lcx, dy + pc[i].lcy, 1, 1);
            }
        }
    }

    /** Not implement yet. */
    public Object clone() {
        return null;
    }
}
