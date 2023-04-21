package org.fudaa.dodico.geometrie;

import org.fudaa.dodico.corba.geometrie.IBoite;
import org.fudaa.dodico.corba.geometrie.IPoint;
import org.fudaa.dodico.corba.geometrie.IPointHelper;
import org.fudaa.dodico.corba.geometrie.IPointOperations;

/**
 * @version $Revision: 1.12 $ $Date: 2006-09-19 14:42:26 $ by $Author: deniger $
 * @author Fred Deniger
 */
public class DPoint extends DPonctuelFini implements IPoint, IPointOperations {

    /**
   * Les coordonnees du point.
   */
    private double[] c_;

    /**
   * Constructeur de l objet DPoint.
   */
    public DPoint() {
        c_ = new double[3];
        initialiseCoordonnees();
    }

    public final Object clone() throws CloneNotSupportedException {
        final DPoint r = new DPoint();
        r.coordonnees(coordonnees());
        return r;
    }

    public String toString() {
        String r = "DPoint(";
        for (int i = 0; i < c_.length; i++) {
            r += c_[i];
            if (i + 1 < c_.length) {
                r += ", ";
            } else {
                r += ")";
            }
        }
        return r;
    }

    public double[] coordonnees() {
        return c_;
    }

    /**
   * Modifie les coordonnees de ce point.
   * 
   * @param _c
   */
    public void coordonnees(final double[] _c) {
        initialiseCoordonnees();
        final int max = Math.min(_c.length, 3);
        for (int i = 0; i < max; i++) {
            c_[i] = _c[i];
        }
    }

    public double distance(final IPoint _p) {
        final int d = dimension();
        final double[] cp = _p.coordonnees();
        double r = 0.;
        for (int i = 0; i < d; i++) {
            final double v = cp[i] - c_[i];
            r += v * v;
        }
        return Math.sqrt(r);
    }

    /**
   * Retourne la distance XY entre ce point et un autre.
   * 
   * @param _p
   * @return la distance
   */
    public double distanceXY(final IPoint _p) {
        int d = dimension();
        final double[] cp = _p.coordonnees();
        double r = 0.;
        if (d > 2) {
            d = 2;
        }
        for (int i = 0; i < d; i++) {
            final double v = cp[i] - c_[i];
            r += v * v;
        }
        return Math.sqrt(r);
    }

    public double[][] h4() {
        final double[][] r = new double[1][4];
        for (int i = 0; i < Math.min(3, c_.length); i++) {
            r[0][i] = c_[i];
        }
        r[0][3] = 1.;
        return r;
    }

    /**
   * Modifie les coordonnees de ce point.
   * 
   * @param _r
   */
    public void h4(final double[][] _r) {
        c_ = new double[3];
        for (int i = 0; i < 3; i++) {
            c_[i] = _r[0][i];
        }
    }

    /**
   * Retourne la dimension du point. cad son nombre de coordonnees.
   * 
   * @return la dimension
   */
    public int dimension() {
        return c_.length;
    }

    /**
   * Retourne la boite englobante, cad une boite de volume nul dont les les extremites sont le point lui-meme.
   * 
   * @return la boite
   */
    public IBoite boite() {
        return null;
    }

    /**
   * Retourne le centre apparent d'un point cad le point lui-meme.
   * 
   * @return le centre
   */
    public IPoint centreApparent() {
        return IPointHelper.narrow(tie());
    }

    /**
   * Retourne le barycentre d'un point cad le point lui-meme.
   * 
   * @return le barycentre
   */
    public IPoint barycentre() {
        return IPointHelper.narrow(tie());
    }

    /**
   * Constructeur de l objet initialiseCoordonnees.
   */
    private void initialiseCoordonnees() {
        c_[0] = 0;
        c_[1] = 0;
        c_[2] = 0;
    }
}
