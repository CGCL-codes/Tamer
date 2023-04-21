package net.claribole.eval.alphalens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import net.claribole.zvtm.glyphs.projection.BProjectedCoords;
import com.xerox.VTM.engine.Camera;
import com.xerox.VTM.glyphs.ClosedShape;
import com.xerox.VTM.glyphs.Glyph;

class VCircleS extends ClosedShape {

    /**radius in virtual space (equal to bounding circle radius since this is a circle)*/
    public long vr;

    public BProjectedCoords[] pc;

    Stroke lensStroke;

    /**
     *@param x coordinate in virtual space
     *@param y coordinate in virtual space
     *@param z z-index (pass 0 if you do not use z-ordering)
     *@param r radius in virtual space
     *@param c fill color
     *@param bc border color
     */
    public VCircleS(long x, long y, int z, long r, Color c, Color bc, float lensStrokeWidth) {
        vx = x;
        vy = y;
        vz = z;
        vr = r;
        computeSize();
        orient = 0;
        setColor(c);
        setBorderColor(bc);
        lensStroke = new BasicStroke(lensStrokeWidth);
    }

    public void initCams(int nbCam) {
        pc = new BProjectedCoords[nbCam];
        for (int i = 0; i < nbCam; i++) {
            pc[i] = new BProjectedCoords();
        }
    }

    public void addCamera(int verifIndex) {
        if (pc != null) {
            if (verifIndex == pc.length) {
                BProjectedCoords[] ta = pc;
                pc = new BProjectedCoords[ta.length + 1];
                for (int i = 0; i < ta.length; i++) {
                    pc[i] = ta[i];
                }
                pc[pc.length - 1] = new BProjectedCoords();
            } else {
                System.err.println("VCircle:Error while adding camera " + verifIndex);
            }
        } else {
            if (verifIndex == 0) {
                pc = new BProjectedCoords[1];
                pc[0] = new BProjectedCoords();
            } else {
                System.err.println("VCircle:Error while adding camera " + verifIndex);
            }
        }
    }

    public void removeCamera(int index) {
        pc[index] = null;
    }

    public void resetMouseIn() {
        for (int i = 0; i < pc.length; i++) {
            resetMouseIn(i);
        }
    }

    public void resetMouseIn(int i) {
        if (pc[i] != null) {
            pc[i].prevMouseIn = false;
        }
        borderColor = bColor;
    }

    public float getOrient() {
        return orient;
    }

    /** Cannot be reoriented (it makes no sense). */
    public void orientTo(float angle) {
    }

    public void orientToNS(float angle) {
    }

    public float getSize() {
        return size;
    }

    void computeSize() {
        size = (float) vr;
    }

    public void sizeTo(float radius) {
        size = radius;
        vr = (long) Math.round(size);
        try {
            vsm.repaintNow();
        } catch (NullPointerException e) {
        }
    }

    public void sizeTo(long radius) {
        size = (float) radius;
        vr = radius;
        try {
            vsm.repaintNow();
        } catch (NullPointerException e) {
        }
    }

    public void reSize(float factor) {
        size *= factor;
        vr = (long) Math.round(size);
        try {
            vsm.repaintNow();
        } catch (NullPointerException e) {
        }
    }

    public boolean fillsView(long w, long h, int camIndex) {
        if ((Math.sqrt(Math.pow(w - pc[camIndex].cx, 2) + Math.pow(h - pc[camIndex].cy, 2)) <= pc[camIndex].cr) && (Math.sqrt(Math.pow(pc[camIndex].cx, 2) + Math.pow(h - pc[camIndex].cy, 2)) <= pc[camIndex].cr) && (Math.sqrt(Math.pow(w - pc[camIndex].cx, 2) + Math.pow(pc[camIndex].cy, 2)) <= pc[camIndex].cr) && (Math.sqrt(Math.pow(pc[camIndex].cx, 2) + Math.pow(pc[camIndex].cy, 2)) <= pc[camIndex].cr)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean coordInside(int x, int y, int camIndex) {
        if (Math.sqrt(Math.pow(x - pc[camIndex].cx, 2) + Math.pow(y - pc[camIndex].cy, 2)) <= pc[camIndex].cr) {
            return true;
        } else {
            return false;
        }
    }

    public short mouseInOut(int x, int y, int camIndex) {
        if (coordInside(x, y, camIndex)) {
            if (!pc[camIndex].prevMouseIn) {
                pc[camIndex].prevMouseIn = true;
                return Glyph.ENTERED_GLYPH;
            } else {
                return Glyph.NO_CURSOR_EVENT;
            }
        } else {
            if (pc[camIndex].prevMouseIn) {
                pc[camIndex].prevMouseIn = false;
                return Glyph.EXITED_GLYPH;
            } else {
                return Glyph.NO_CURSOR_EVENT;
            }
        }
    }

    public boolean visibleInDisc(long dvx, long dvy, long dradius, int camIndex) {
        return Math.sqrt(Math.pow(vx - dvx, 2) + Math.pow(vy - dvy, 2)) < (dradius + vr);
    }

    public void project(Camera c, Dimension d) {
        int i = c.getIndex();
        coef = (float) (c.focal / (c.focal + c.altitude));
        pc[i].cx = (d.width / 2) + Math.round((vx - c.posx) * coef);
        pc[i].cy = (d.height / 2) - Math.round((vy - c.posy) * coef);
        pc[i].cr = Math.round(vr * coef);
    }

    public void projectForLens(Camera c, int lensWidth, int lensHeight, float lensMag, long lensx, long lensy) {
        int i = c.getIndex();
        coef = ((float) (c.focal / (c.focal + c.altitude))) * lensMag;
        pc[i].lcx = (lensWidth / 2) + Math.round((vx - (lensx)) * coef);
        pc[i].lcy = (lensHeight / 2) - Math.round((vy - (lensy)) * coef);
        pc[i].lcr = Math.round(vr * coef);
    }

    public void draw(Graphics2D g, int vW, int vH, int i, Stroke stdS, AffineTransform stdT, int dx, int dy) {
        if (pc[i].cr > 1) {
            if (isFilled()) {
                g.setColor(this.color);
                g.fillOval(dx + pc[i].cx - pc[i].cr, dy + pc[i].cy - pc[i].cr, 2 * pc[i].cr, 2 * pc[i].cr);
            }
            if (isBorderDrawn()) {
                g.setColor(borderColor);
                if (stroke != null) {
                    g.setStroke(stroke);
                    g.drawOval(dx + pc[i].cx - pc[i].cr, dy + pc[i].cy - pc[i].cr, 2 * pc[i].cr, 2 * pc[i].cr);
                    g.setStroke(stdS);
                } else {
                    g.drawOval(dx + pc[i].cx - pc[i].cr, dy + pc[i].cy - pc[i].cr, 2 * pc[i].cr, 2 * pc[i].cr);
                }
            }
        } else {
            g.setColor(this.color);
            g.fillRect(dx + pc[i].cx, dy + pc[i].cy, 1, 1);
        }
    }

    public void drawForLens(Graphics2D g, int vW, int vH, int i, Stroke stdS, AffineTransform stdT, int dx, int dy) {
        if (pc[i].lcr > 1) {
            if (isFilled()) {
                g.setColor(this.color);
                g.fillOval(dx + pc[i].lcx - pc[i].lcr, dy + pc[i].lcy - pc[i].lcr, 2 * pc[i].lcr, 2 * pc[i].lcr);
            }
            if (isBorderDrawn()) {
                g.setColor(borderColor);
                g.setStroke(lensStroke);
                g.drawOval(dx + pc[i].lcx - pc[i].lcr, dy + pc[i].lcy - pc[i].lcr, 2 * pc[i].lcr, 2 * pc[i].lcr);
                g.setStroke(stdS);
            }
        } else {
            g.setColor(this.color);
            g.fillRect(dx + pc[i].lcx, dy + pc[i].lcy, 1, 1);
        }
    }

    public Object clone() {
        return null;
    }
}
