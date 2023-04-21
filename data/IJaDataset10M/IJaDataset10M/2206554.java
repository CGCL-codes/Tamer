package org.fudaa.ebli.geometrie;

import java.lang.ref.WeakReference;
import java.util.Vector;

/**
 * Un reservoir d'objet GrPoint
 *
 * @version      $Id: ReserveGrPoint.java,v 1.4 2003-11-25 10:06:43 deniger Exp $
 * @author       Fred Deniger
 */
public final class ReserveGrPoint {

    private VecteurGrPoint vecteur_;

    private int nb_;

    public static final ReserveGrPoint EBLI = new ReserveGrPoint();

    public static final int NB = 20;

    private ReserveGrPoint() {
        vecteur_ = new VecteurGrPoint(NB);
        nb_ = 0;
    }

    public GrPoint cree(int _x, int _y, int _z) {
        if (nb_ > 0) {
            GrPoint p = vecteur_.renvoie(--nb_);
            vecteur_.enleve(nb_);
            p.x = _x;
            p.y = _y;
            p.z = _z;
            return p;
        } else return new GrPoint(_x, _y, _z);
    }

    public GrPoint cree() {
        return cree(0, 0, 0);
    }

    public void libere(GrPoint _p) {
        if (vecteur_.contient(_p)) {
            System.out.println("ERREUR point deja lib�r�");
            return;
        }
        if (nb_ - NB < 0) {
            vecteur_.ajoute(_p);
            nb_++;
        }
    }
}
