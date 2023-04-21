package org.fudaa.ebli.trace;

import java.awt.Graphics2D;
import java.awt.Polygon;

/**
 * Trace d'iso-surfaces avec une palette d�finie par des plages de valeurs.
 * <p>
 * Les plages peuvent �tre r�parties de mani�re non homog�ne. Le trac� accepte �galement une palette � plages non
 * contig�es (avec des trous), ou dont les bornes sont incluses dans l'intervalle min/max des valeurs � tracer.
 * 
 * @version $Id: TraceIsoSurfacesAvecPlages.java,v 1.13 2007-04-30 14:22:22 deniger Exp $
 * @author Bertrand Marchand
 */
public class TraceIsoSurfacesAvecPlages extends TraceIsoAvecPlage {

    /**
   * Cr�ation du trac� d'isosurface.
   * 
   * @param _pal la palette a partir de laquelle le trace sera fait
   */
    public TraceIsoSurfacesAvecPlages(final TraceIsoPlageInterface _pal, final int _alpha) {
        updateFromPlageData(_pal, _alpha);
    }

    /**
   * @param _g le graphics support du dessin
   * @param _p le polygone a dessine
   * @param _v les valeurs en chaque point du polygone
   */
    public void draw(final Graphics2D _g, final Polygon _p, final double[] _v) {
        drawX(_g, _p, _v);
    }

    private void drawX(final Graphics2D _g, final Polygon _p, final double[] _v) {
        if (_p.npoints == 3) {
            draw3(_g, _p, _v);
        } else if (_p.npoints == 4) {
            draw4(_g, _p, _v);
        } else if (_p.npoints >= 5) {
            draw5(_g, _p, _v);
        }
    }

    private void draw5(final Graphics2D _g, final Polygon _p, final double[] _v) {
        final Polygon p1 = new Polygon();
        final Polygon p2 = new Polygon();
        final double[] v1 = new double[3];
        final double[] v2 = new double[_p.npoints - 1];
        p1.addPoint(_p.xpoints[0], _p.ypoints[0]);
        p1.addPoint(_p.xpoints[1], _p.ypoints[1]);
        p1.addPoint(_p.xpoints[_p.npoints - 1], _p.ypoints[_p.npoints - 1]);
        v1[0] = _v[0];
        v1[1] = _v[1];
        v1[2] = _v[_p.npoints - 1];
        for (int i = 1; i < _p.npoints; i++) {
            p2.addPoint(_p.xpoints[i], _p.ypoints[i]);
            v2[i - 1] = _v[i];
        }
        draw3(_g, p1, v1);
        drawX(_g, p2, v2);
    }

    private void draw4(final Graphics2D _g, final Polygon _p, final double[] _v) {
        final Polygon p1 = new Polygon();
        final Polygon p2 = new Polygon();
        p1.addPoint(_p.xpoints[0], _p.ypoints[0]);
        p1.addPoint(_p.xpoints[1], _p.ypoints[1]);
        p1.addPoint(_p.xpoints[3], _p.ypoints[3]);
        p2.addPoint(_p.xpoints[1], _p.ypoints[1]);
        p2.addPoint(_p.xpoints[2], _p.ypoints[2]);
        p2.addPoint(_p.xpoints[3], _p.ypoints[3]);
        final double[] v1 = new double[3];
        final double[] v2 = new double[3];
        v1[0] = _v[0];
        v1[1] = _v[1];
        v1[2] = _v[3];
        v2[0] = _v[1];
        v2[1] = _v[2];
        v2[2] = _v[3];
        draw3(_g, p1, v1);
        draw3(_g, p2, v2);
    }

    final int[] xp_ = new int[5];

    final int[] yp_ = new int[5];

    /**
   * Trac� d'un triangle.
   */
    private void draw3(final Graphics2D _g, final Polygon _p, final double[] _v) {
        if (_v[0] == _v[1] && _v[1] == _v[2]) {
            int i1 = 0;
            final double d1 = _v[0];
            for (i1 = 0; i1 < vniv_.length; i1++) {
                if (vniv_[i1] > d1) {
                    break;
                }
            }
            if (cniv_[i1] == null) {
                final int k = i1 - 1;
                if ((k >= 0) && (vniv_[k] >= d1)) {
                    i1 = k;
                }
            }
            if (cniv_[i1] != null) {
                _g.setColor(cniv_[i1]);
                _g.fillPolygon(_p);
                return;
            }
        }
        int np;
        final int[] px = _p.xpoints;
        final int[] py = _p.ypoints;
        double d;
        int nmin;
        int nmoy;
        int nmax;
        if (_v[0] > _v[1]) {
            if (_v[0] > _v[2]) {
                nmax = 0;
                if (_v[1] > _v[2]) {
                    nmoy = 1;
                    nmin = 2;
                } else {
                    nmoy = 2;
                    nmin = 1;
                }
            } else {
                nmin = 1;
                nmoy = 0;
                nmax = 2;
            }
        } else {
            if (_v[0] < _v[2]) {
                nmin = 0;
                if (_v[1] < _v[2]) {
                    nmoy = 1;
                    nmax = 2;
                } else {
                    nmoy = 2;
                    nmax = 1;
                }
            } else {
                nmin = 2;
                nmoy = 0;
                nmax = 1;
            }
        }
        boolean nminInt = false;
        boolean nmoyInt = false;
        boolean nmaxInt = false;
        int n11 = -1;
        int n12 = -1;
        int n21 = -1;
        int n22 = -1;
        np = -1;
        for (int i = 0; i < vniv_.length; i++) {
            if (nmaxInt) {
                break;
            }
            if (nminInt) {
                final int xp1 = xp_[np - 1];
                final int yp1 = yp_[np - 1];
                final int xp2 = xp_[np - 2];
                final int yp2 = yp_[np - 2];
                xp_[0] = xp1;
                yp_[0] = yp1;
                xp_[1] = xp2;
                yp_[1] = yp2;
                np = 2;
            } else {
                np = 0;
            }
            if (_v[nmin] < vniv_[i] && !nminInt) {
                xp_[np] = px[nmin];
                yp_[np] = py[nmin];
                np++;
                nminInt = true;
                n11 = nmin;
                n12 = nmoy;
                n21 = nmin;
                n22 = nmax;
            }
            if (_v[nmoy] < vniv_[i] && !nmoyInt) {
                xp_[np] = px[nmoy];
                yp_[np] = py[nmoy];
                np++;
                nmoyInt = true;
                n11 = nmoy;
                n12 = nmax;
            }
            if (_v[nmax] < vniv_[i] && !nmaxInt) {
                xp_[np] = px[nmax];
                yp_[np] = py[nmax];
                np++;
                nmaxInt = true;
            }
            if (nminInt && !nmaxInt) {
                d = (vniv_[i] - _v[n11]) / (_v[n12] - _v[n11]);
                xp_[np] = (int) ((px[n12] - px[n11]) * d + px[n11]);
                yp_[np] = (int) ((py[n12] - py[n11]) * d + py[n11]);
                np++;
                d = (vniv_[i] - _v[n21]) / (_v[n22] - _v[n21]);
                xp_[np] = (int) ((px[n22] - px[n21]) * d + px[n21]);
                yp_[np] = (int) ((py[n22] - py[n21]) * d + py[n21]);
                np++;
            }
            if (nminInt && cniv_[i] != null) {
                _g.setColor(cniv_[i]);
                _g.fillPolygon(xp_, yp_, np);
            }
        }
    }
}
