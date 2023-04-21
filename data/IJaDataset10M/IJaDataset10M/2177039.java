package org.fudaa.fudaa.refonde;

import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.geometrie.GrSegment;
import org.fudaa.ebli.geometrie.GrVecteur;

/**
 * Classe repr�sentant une droite. Pour l'instant locale, la classe ne fait pas
 * de calcul en 3D, et n'est donc pas integrable dans le package geometrie.
 *
 * @version      $Id: RefondeDroite.java,v 1.5 2006-09-08 16:04:28 opasteur Exp $
 * @author       Bertrand Marchand
 */
class RefondeDroite {

    GrPoint[] points;

    /**
   * Cr�ation � partir de 2 points
   */
    public RefondeDroite(GrPoint _p1, GrPoint _p2) {
        points = new GrPoint[2];
        points[0] = _p1;
        points[1] = _p2;
    }

    /**
   * Cr�ation � partir d'un point et un vecteur
   */
    public RefondeDroite(GrPoint _p, GrVecteur _v) {
        points = new GrPoint[2];
        points[0] = _p;
        points[1] = _p.addition(_v);
    }

    /**
   * Vecteur directeur de la droite
   */
    public GrVecteur vecteur() {
        return points[0].soustraction(points[1]);
    }

    /**
   * Recherche du point d'intersection entre 2 droites
   * @param _d La droite
   * @return Le point d'intersection <I>null</I> si aucun point d'intersection
   *         ou si les droites sont confondues
   */
    public GrPoint intersectionDroiteXY(RefondeDroite _d) {
        GrVecteur vd1 = vecteur();
        GrVecteur vd2 = _d.vecteur();
        double a1;
        double b1;
        double c1;
        double a2;
        double b2;
        double c2;
        double xi;
        double yi;
        double prod;
        a1 = -vd1.y_;
        b1 = vd1.x_;
        c1 = -a1 * points[0].x_ - b1 * points[0].y_;
        a2 = -vd2.y_;
        b2 = vd2.x_;
        c2 = -a2 * _d.points[0].x_ - b2 * _d.points[0].y_;
        prod = a2 * b1 - a1 * b2;
        if (prod == 0.) return null;
        yi = (a1 * c2 - a2 * c1) / prod;
        xi = (b2 * c1 - b1 * c2) / prod;
        return new GrPoint(xi, yi, 0.);
    }

    /**
   * Recherche du point d'intersection entre la droite et un segment
   * @param _s Le segment
   * @return Le point d'intersection <I>null</I> si aucun point d'intersection
   *         ou si le point est hors segment
   */
    public GrPoint intersectionSegmentXY(GrSegment _s) {
        GrPoint r;
        r = intersectionDroiteXY(new RefondeDroite(_s.o_, _s.e_));
        if (r == null) return null;
        if (r.x_ + Math.abs(r.x_ * 1.e-10) < Math.min(_s.e_.x_, _s.o_.x_) || r.x_ - Math.abs(r.x_ * 1.e-10) > Math.max(_s.e_.x_, _s.o_.x_) || r.y_ + Math.abs(r.y_ * 1.e-10) < Math.min(_s.e_.y_, _s.o_.y_) || r.y_ - Math.abs(r.y_ * 1.e-10) > Math.max(_s.e_.y_, _s.o_.y_)) return null;
        return r;
    }
}
