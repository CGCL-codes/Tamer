package fr.inria.zvtm.glyphs;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import fr.inria.zvtm.engine.VirtualSpaceManager;
import fr.inria.zvtm.engine.Camera;
import fr.inria.zvtm.glyphs.Glyph;
import fr.inria.zvtm.glyphs.ClosedShape;
import fr.inria.zvtm.glyphs.VImage;
import fr.inria.zvtm.glyphs.RectangularShape;
import fr.inria.zvtm.glyphs.projection.RProjectedCoordsP;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

/** Glyph encapsulating a PDF Page.
 *@author Emmanuel Pietriga
 */
public abstract class ZPDFPage extends ClosedShape implements RectangularShape {

    /** Page width in virtual space. */
    double vw;

    /** Page height in virtual space. */
    double vh;

    /** Aspect ratio: width divided by height (read-only). */
    public double ar;

    public RProjectedCoordsP[] pc;

    public boolean zoomSensitive = true;

    public double scaleFactor = 1.0f;

    @Override
    public void initCams(int nbCam) {
        pc = new RProjectedCoordsP[nbCam];
        for (int i = 0; i < nbCam; i++) {
            pc[i] = new RProjectedCoordsP();
        }
    }

    @Override
    public void addCamera(int verifIndex) {
        if (pc != null) {
            if (verifIndex == pc.length) {
                RProjectedCoordsP[] ta = pc;
                pc = new RProjectedCoordsP[ta.length + 1];
                for (int i = 0; i < ta.length; i++) {
                    pc[i] = ta[i];
                }
                pc[pc.length - 1] = new RProjectedCoordsP();
            } else {
                System.err.println("ZPDFPage: Error while adding camera " + verifIndex);
            }
        } else {
            if (verifIndex == 0) {
                pc = new RProjectedCoordsP[1];
                pc[0] = new RProjectedCoordsP();
            } else {
                System.err.println("ZPDFPage: Error while adding camera " + verifIndex);
            }
        }
    }

    @Override
    public void removeCamera(int index) {
        pc[index] = null;
    }

    void computeSize() {
        size = Math.sqrt(Math.pow(vw, 2) + Math.pow(vh, 2));
    }

    @Override
    public double getSize() {
        return size;
    }

    @Override
    public void sizeTo(double s) {
    }

    @Override
    public void reSize(double factor) {
    }

    @Override
    public double getOrient() {
        return 0;
    }

    @Override
    public void orientTo(double angle) {
    }

    @Override
    public void highlight(boolean b, Color selectedColor) {
    }

    public void setWidth(double w) {
    }

    public void setHeight(double h) {
    }

    public double getWidth() {
        return vw;
    }

    public double getHeight() {
        return vh;
    }

    /** Set to false if the image should not be scaled according to camera's altitude. Its size can still be changed, but its apparent size will always be the same, no matter the camera's altitude.
		*@see #isZoomSensitive()
		*/
    public void setZoomSensitive(boolean b) {
        if (zoomSensitive != b) {
            zoomSensitive = b;
            VirtualSpaceManager.INSTANCE.repaint();
        }
    }

    /** Indicates whether the image is scaled according to camera's altitude.
		*@see #setZoomSensitive(boolean b)
		*/
    public boolean isZoomSensitive() {
        return zoomSensitive;
    }

    @Override
    public boolean fillsView(double w, double h, int camIndex) {
        return false;
    }

    @Override
    public boolean coordInside(int jpx, int jpy, int camIndex, double cvx, double cvy) {
        if ((jpx >= (pc[camIndex].cx - pc[camIndex].cw)) && (jpx <= (pc[camIndex].cx + pc[camIndex].cw)) && (jpy >= (pc[camIndex].cy - pc[camIndex].ch)) && (jpy <= (pc[camIndex].cy + pc[camIndex].ch))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean visibleInRegion(double wb, double nb, double eb, double sb, int i) {
        if ((vx >= wb) && (vx <= eb) && (vy >= sb) && (vy <= nb)) {
            return true;
        } else if (((vx - vw / 2d) <= eb) && ((vx + vw / 2d) >= wb) && ((vy - vh / 2d) <= nb) && ((vy + vh / 2d) >= sb)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean visibleInDisc(double dvx, double dvy, double dvr, Shape dvs, int camIndex, int jpx, int jpy, int dpr) {
        return dvs.intersects(vx - vw / 2d, vy - vh / 2d, vw, vh);
    }

    @Override
    public short mouseInOut(int jpx, int jpy, int camIndex, double cvx, double cvy) {
        if (coordInside(jpx, jpy, camIndex, cvx, cvy)) {
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

    @Override
    public void resetMouseIn() {
        for (int i = 0; i < pc.length; i++) {
            resetMouseIn(i);
        }
    }

    @Override
    public void resetMouseIn(int i) {
        if (pc[i] != null) {
            pc[i].prevMouseIn = false;
        }
        borderColor = bColor;
    }

    @Override
    public void project(Camera c, Dimension d) {
        int i = c.getIndex();
        coef = c.focal / (c.focal + c.altitude);
        pc[i].cx = (int) Math.round((d.width / 2d) + (vx - c.vx) * coef);
        pc[i].cy = (int) Math.round((d.height / 2d) - (vy - c.vy) * coef);
        if (zoomSensitive) {
            pc[i].cw = (int) Math.round(vw / 2d * coef);
            pc[i].ch = (int) Math.round(vh / 2d * coef);
        } else {
            pc[i].cw = (int) Math.round(vw / 2d);
            pc[i].ch = (int) Math.round(vh / 2d);
        }
    }

    @Override
    public void projectForLens(Camera c, int lensWidth, int lensHeight, float lensMag, double lensx, double lensy) {
        int i = c.getIndex();
        coef = c.focal / (c.focal + c.altitude) * lensMag;
        pc[i].lcx = (int) Math.round(lensWidth / 2d + (vx - lensx) * coef);
        pc[i].lcy = (int) Math.round(lensHeight / 2d - (vy - lensy) * coef);
        if (zoomSensitive) {
            pc[i].lcw = (int) Math.round(vw / 2d * coef);
            pc[i].lch = (int) Math.round(vh / 2d * coef);
        } else {
            pc[i].lcw = (int) Math.round(vw / 2d);
            pc[i].lch = (int) Math.round(vh / 2d);
        }
    }

    /** Flush any resource used. */
    public abstract void flush();

    @Override
    public Shape getJava2DShape() {
        return new Rectangle2D.Double(vx - vw / 2.0, vy - vh / 2.0, vw, vh);
    }
}
