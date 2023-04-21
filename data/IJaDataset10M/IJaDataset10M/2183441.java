package com.insanityengine.ghia.renderer;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.applet.*;
import com.insanityengine.ghia.m3.*;
import com.insanityengine.ghia.libograf.*;
import com.insanityengine.ghia.pixels.*;

/**
 *
 * <P>
 * </P>
 *
 * @author BrianHammond
 *
 * $Header: /cvsroot/ghia/ghia/src/java/com/insanityengine/ghia/renderer/Scantron.java,v 1.3 2006/11/30 23:52:07 brianin3d Exp $
 *
 */
public class Scantron {

    /** 
	 * 
	 * Constructor
	 * 
	 */
    public Scantron() {
    }

    /** 
	 * 
	 * Constructor
	 * 
	 */
    public Scantron(int width, int height) {
        init(width, height);
    }

    /** 
	 * 
	 * isReady
	 * 
	 * @return a boolean
	 * 
	 */
    public final boolean isReady() {
        return (null != lx);
    }

    /** 
	 * 
	 * init
	 * 
	 * @param height
	 * 
	 */
    public final void init(int width, int height) {
        this.w = width;
        this.h = height;
        lx = new double[h];
        lz = new double[h];
        ltx = new double[h];
        lty = new double[h];
        rx = new double[h];
        rz = new double[h];
        rtx = new double[h];
        rty = new double[h];
    }

    /** 
	 * 
	 * getDistance
	 * 
	 * @param i
	 * 
	 * @return a double
	 * 
	 */
    public final double getDistance(int i) {
        return 1 + rx[i] - lx[i];
    }

    public final double getLx(int i) {
        return lx[i];
    }

    public final double getLz(int i) {
        return lz[i];
    }

    public final double getLtx(int i) {
        return ltx[i];
    }

    public final double getLty(int i) {
        return lty[i];
    }

    public final double getRx(int i) {
        return rx[i];
    }

    public final double getRz(int i) {
        return rz[i];
    }

    public final double getRtx(int i) {
        return rtx[i];
    }

    public final double getRty(int i) {
        return rty[i];
    }

    /** 
	 * 
	 * reset
	 * 
	 */
    public final void reset() {
        int cleared = 1;
        rx[0] = -M3_Constants.bigNumber;
        lx[0] = +M3_Constants.bigNumber;
        for (cleared = 1; cleared < h / 2; cleared *= 2) {
            System.arraycopy(rx, 0, rx, cleared, cleared);
            System.arraycopy(lx, 0, lx, cleared, cleared);
        }
        System.arraycopy(rx, 0, rx, cleared, h - cleared);
        System.arraycopy(lx, 0, lx, cleared, h - cleared);
        this.setMinScanLine(h + 1);
        this.setMaxScanLine(-1);
    }

    /** 
	 * 
	 * setL
	 * 
	 * @param i
	 * @param x
	 * @param z
	 * @param tx
	 * @param ty
	 * 
	 */
    public final void setL(int i, double _x, double _z, double _tx, double _ty) {
        if (_x < lx[i]) {
            this.use(i);
            lx[i] = _x;
            lz[i] = _z;
            ltx[i] = _tx;
            lty[i] = _ty;
        }
    }

    /** 
	 * 
	 * setR
	 * 
	 * @param i
	 * @param x
	 * @param z
	 * @param tx
	 * @param ty
	 * 
	 */
    public final void setR(int i, double _x, double _z, double _tx, double _ty) {
        if (_x < rx[i]) {
            this.use(i);
            rx[i] = _x;
            rz[i] = _z;
            rtx[i] = _tx;
            rty[i] = _ty;
        }
    }

    /** 
	 * 
	 * set
	 * 
	 * @param i
	 * @param x
	 * @param z
	 * @param tx
	 * @param ty
	 * 
	 */
    public final void set(int i, double _x, double _z, double _tx, double _ty) {
        if (_x < lx[i]) {
            this.use(i);
            lx[i] = _x;
            lz[i] = _z;
            ltx[i] = _tx;
            lty[i] = _ty;
        }
        if (_x > rx[i]) {
            this.use(i);
            rx[i] = _x;
            rz[i] = _z;
            rtx[i] = _tx;
            rty[i] = _ty;
        }
    }

    /** 
	 * 
	 * set
	 * 
	 */
    public final void set(int i, double _lx, double _lz, double _ltx, double _lty, double _rx, double _rz, double _rtx, double _rty) {
        if (_lx < lx[i]) {
            this.use(i);
            lx[i] = _lx;
            lz[i] = _lz;
            ltx[i] = _ltx;
            lty[i] = _lty;
        }
        if (_rx > rx[i]) {
            this.use(i);
            rx[i] = _rx;
            rz[i] = _rz;
            rtx[i] = _rtx;
            rty[i] = _rty;
        }
    }

    /** 
	 * 
	 * scanPolyEdge
	 * 
	 * @param p0in
	 * @param p1in
	 * 
	 */
    public final void scanPolyEdge(Pt3 p0_in, Pt3 p1_in) {
        if (p0_in.y == p1_in.y) {
            horizonalLine(p0_in, p1_in);
        } else {
            reallyScan(p0_in, p1_in);
        }
    }

    private int w, h;

    private double lx[], lz[], ltx[], lty[];

    private double rx[], rz[], rtx[], rty[];

    /** 
	 * 
	 * horizonalLine
	 * 
	 * @param p0in
	 * @param p1in
	 * 
	 */
    private final void horizonalLine(Pt3 p0_in, Pt3 p1_in) {
        if (p0_in.y >= 0 && p0_in.y < h) {
            setL((int) p0_in.y, p0_in.x, p0_in.z, p0_in.s, p0_in.t);
            setR((int) p1_in.y, p1_in.x, p1_in.z, p1_in.s, p1_in.t);
        }
    }

    /** 
	 * 
	 * reallyScan
	 * 
	 * @param p0in
	 * @param p1in
	 * 
	 */
    private final void reallyScan(Pt3 p0_in, Pt3 p1_in) {
        Pt3 p0 = p0_in;
        Pt3 p1 = p1_in;
        x0 = (int) p0.x;
        y0 = (int) p0.y;
        x1 = (int) p1.x;
        y1 = (int) p1.y;
        diffx = (x1 - x0);
        diffy = (y1 - y0);
        adiffx = (diffx < 0) ? -diffx : diffx;
        adiffy = (diffy < 0) ? -diffy : diffy;
        boolean recalc = false;
        boolean useX = true;
        if (adiffx < adiffy && 0 != diffx) {
            useX = true;
            if (diffx < 0) {
                recalc = true;
            }
        } else {
            useX = false;
            if (diffy < 0) {
                recalc = true;
            }
        }
        if (recalc) {
            p1 = p0_in;
            p0 = p1_in;
            x0 = (int) p0.x;
            y0 = (int) p0.y;
            x1 = (int) p1.x;
            y1 = (int) p1.y;
            diffx = (x1 - x0);
            diffy = (y1 - y0);
        }
        z = p0.z;
        dz = p1.z - z;
        tx = p0.s;
        ty = p0.t;
        if (useX) {
            zinc = dz / (adiffy + adiffx);
            o_inc = diffy / (double) (1 + adiffx);
            tx_inc = (p1.s - p0.s) / (adiffx + adiffy);
            ty_inc = (p1.t - p0.t) / (adiffx + adiffy);
            if (diffy < 0) linc = -1; else linc = 1;
            for (x = x0, o = y0; x != x1 + 1; o += o_inc, x++) {
                next = (int) (o + o_inc + linc);
                for (j = (int) o; j != next; j += linc) {
                    if (j > 0 && j < h) {
                        set(j, x, z, tx, ty);
                    }
                    z += zinc;
                    tx += tx_inc;
                    ty += ty_inc;
                }
            }
        } else {
            zinc = dz / (adiffy + 1);
            o_inc = diffx / (double) (1 + adiffy);
            tx_inc = (p1.s - p0.s) / (1 + adiffy);
            ty_inc = (p1.t - p0.t) / (1 + adiffy);
            for (y = y0, o = x0; y != y1 + 1; o += o_inc, y++) {
                if (y > 0 && y < h) {
                    set(y, o, z, tx, ty, o + o_inc, z + zinc, tx, ty);
                }
                z += zinc;
                tx += tx_inc;
                ty += ty_inc;
            }
        }
    }

    /** 
	 * 
	 * getMinScanLine
	 * 
	 * @return a int
	 * 
	 */
    public int getMinScanLine() {
        return this.minScanLine_;
    }

    /** 
	 * 
	 * setMinScanLine
	 * 
	 * @param name
	 * 
	 */
    public void setMinScanLine(int minScanLine) {
        this.minScanLine_ = minScanLine;
    }

    public void tryToSetMinScanLine(int minScanLine) {
        if (minScanLine < this.getMinScanLine()) {
            this.setMinScanLine(minScanLine);
        }
    }

    /** 
	 * 
	 * getMaxScanLine
	 * 
	 * @return a int
	 * 
	 */
    public int getMaxScanLine() {
        return this.maxScanLine_;
    }

    /** 
	 * 
	 * setMaxScanLine
	 * 
	 * @param name
	 * 
	 */
    public void setMaxScanLine(int maxScanLine) {
        this.maxScanLine_ = maxScanLine;
    }

    public void tryToSetMaxScanLine(int maxScanLine) {
        if (maxScanLine > this.getMaxScanLine()) {
            this.setMaxScanLine(maxScanLine);
        }
    }

    public void use(int scanLine) {
        this.tryToSetMinScanLine(scanLine);
        this.tryToSetMaxScanLine(scanLine);
    }

    private int minScanLine_;

    private int maxScanLine_;

    int x0, y0;

    int x1, y1;

    int diffx, diffy;

    int adiffx, adiffy;

    int x, y, j;

    double zinc;

    double z;

    double dz;

    double o, o_inc;

    int next, inc, linc;

    double tx;

    double ty;

    double tx_inc, ty_inc;
}

;
