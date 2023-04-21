package org.fudaa.ebli.geometrie;

import org.fudaa.ebli.commun.VecteurGenerique;

/**
 * vecteur de GrNoeud. Cette classe peut �tre utilisee � la place de ListeGrNoeud. Moins performant pour les insertions
 * mais moins gourmand en m�moire : des noeuds de liaisons ne sont pas cr�es.
 *
 * @version $Id: VecteurGrPositionRelativeSegment.java,v 1.1 2006-09-19 14:55:50 deniger Exp $
 * @author Fred Deniger
 */
public final class VecteurGrPositionRelativeSegment extends VecteurGenerique {

    public VecteurGrPositionRelativeSegment() {
        super();
    }

    /**
   * Renvoie l'element d'index <code>_j</code>.
   *
   * @param _j l'index de l'objet a renvoyer.
   * @return l'objet d'index <code>_j</code>.
   */
    public GrPositionRelativeSegment renvoie(final int _j) {
        return (GrPositionRelativeSegment) internRenvoie(_j);
    }

    /**
   * @return le tableau correspondant
   */
    public GrPositionRelativeSegment[] tableau() {
        final GrPositionRelativeSegment[] r = new GrPositionRelativeSegment[nombre()];
        v_.toArray(r);
        return r;
    }

    /**
   * Vide le vecteur et l'initialise avec <code>_o</code>.
   *
   * @param _o le tableau des nouveaux elements.
   */
    public void tableau(final GrPositionRelativeSegment[] _o) {
        super.internTableau(_o);
    }

    public void ajoute(final GrPositionRelativeSegment _o) {
        super.ajoute(_o);
    }

    public void insere(final GrPositionRelativeSegment _o, final int _j) {
        super.insere(_o, _j);
    }

    public void remplace(final GrPositionRelativeSegment _o, final int _j) {
        super.remplace(_o, _j);
    }

    public void enleve(final GrPositionRelativeSegment _o) {
        super.enleve(_o);
    }

    public int indice(final GrPositionRelativeSegment _o) {
        return super.indice(_o);
    }

    public boolean contient(final GrPositionRelativeSegment _o) {
        return super.contient(_o);
    }
}
