package org.fudaa.ebli.calque.dessin;

import java.awt.Graphics2D;
import java.awt.Point;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.geometrie.GrMorphisme;
import org.fudaa.ebli.geometrie.GrObjet;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.geometrie.GrPolyligne;
import org.fudaa.ebli.trace.TraceGeometrie;
import org.fudaa.ebli.trace.TraceIcon;
import org.fudaa.ebli.trace.TracePoint;

/**
 * Un multipoint.
 *
 * @version      $Revision: 1.1.2.1 $ $Date: 2008-03-26 14:41:09 $ by $Author: bmarchan $
 * @author       Bertrand Marchand
 */
public class DeMultiPoint extends DeForme {

    GrPolyligne ligne_;

    public DeMultiPoint() {
        super();
        ligne_ = new GrPolyligne();
    }

    public DeMultiPoint(final GrPolyligne _l) {
        super();
        ligne_ = _l;
    }

    public int getNombre() {
        return ligne_.nombre();
    }

    public int getForme() {
        return MULTI_POINT;
    }

    public GrObjet getGeometrie() {
        return ligne_;
    }

    /**
   * @return le dernier point
   */
    public GrPoint getDernier() {
        if (getNombre() > 0) {
            return ligne_.sommet(getNombre() - 1);
        }
        return null;
    }

    public void ajoute(final GrPoint _p) {
        ligne_.sommets_.ajoute(_p);
    }

    public void enleveDernier() {
        if (ligne_.sommets_.nombre() > 1) {
            ligne_.sommets_.enleveDernier();
        }
    }

    /**
   * Affiche le multipoint en bouclant sur tous les points.
   */
    public void affiche(final Graphics2D _g2d, final TraceGeometrie _t, final boolean _rapide) {
        super.affiche(_g2d, _t, _rapide);
        for (int i = 0; i < ligne_.nombre(); i++) _t.dessinePoint(_g2d, ligne_.sommet(i), _rapide);
    }

    public void affiche(final Graphics2D _g, final TraceIcon _tp, final boolean _rapide, final GrPoint _tmp, final GrMorphisme _versEcran) {
        final int nb = ligne_.nombre();
        if (nb == 0) {
            return;
        }
        for (int i = nb - 1; i >= 0; i--) {
            _tmp.initialiseAvec(ligne_.sommet(i));
            _tmp.autoApplique(_versEcran);
            _tp.paintIconCentre(_g, _tmp.x_, _tmp.y_);
        }
    }

    /**
   * Affiche le multipoint en bouclant sur tous les points.
   */
    public void affiche(final Graphics2D _g, final TracePoint _tp, final boolean _rapide, final GrPoint _tmp, final GrMorphisme _versEcran) {
        final int nb = ligne_.nombre();
        if (nb == 0) {
            return;
        }
        for (int i = nb - 1; i >= 0; i--) {
            _tmp.initialiseAvec(ligne_.sommet(i));
            _tmp.autoApplique(_versEcran);
            _tp.dessinePoint(_g, _tmp.x_, _tmp.y_);
        }
    }

    /**
   * Retourne les points du contour de l'objet.
   *
   * @return Les points de contour.
   * @see    org.fudaa.ebli.geometrie.GrContour
   */
    public GrPoint[] contour() {
        return ligne_.contour();
    }

    /**
   * Indique si l'objet est s�lectionn� pour un point donn�.<p>
   *
   * Dans le cadre de la s�lection ponctuelle.
   *
   * @param _ecran Le morphisme pour la transformation en coordonn�es �cran.
   * @param _dist  La tol�rence en coordonn�es �cran pour laquelle on consid�re
   *               l'objet s�lectionn�.
   * @param _pt    Le point de s�lection en coordonn�es �cran.
   *
   * @return <I>true</I>  L'objet est s�lectionn�.
   *         <I>false</I> L'objet n'est pas s�lectionn�.
   *
   * @see org.fudaa.ebli.calque.BCalqueSelectionInteraction
   */
    public boolean estSelectionne(final GrMorphisme _ecran, final double _dist, final Point _pt) {
        for (int i = 0; i < ligne_.nombre(); i++) {
            if (ligne_.sommet(i).estSelectionne(_ecran, _dist, _pt)) return true;
        }
        return false;
    }

    public void autoApplique(final GrMorphisme _t) {
        ligne_.autoApplique(_t);
    }

    /**
   * Retourne la boite englobante de l'objet.
   */
    public GrBoite boite() {
        return ligne_.boite();
    }
}
