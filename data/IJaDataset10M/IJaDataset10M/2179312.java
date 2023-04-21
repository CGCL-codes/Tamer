package org.fudaa.ebli.calque;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.vividsolutions.jts.geom.LinearRing;
import org.fudaa.ctulu.gis.GISGeometryFactory;
import org.fudaa.ebli.commun.EbliSelectionMode;
import org.fudaa.ebli.commun.EbliSelectionState;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.geometrie.GrMorphisme;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.geometrie.GrPolygone;

/**
 * @version $Id: ZCalqueSelectionInteractionMulti.java,v 1.13.6.1 2008-05-13 12:10:37 bmarchan Exp $
 * @author Bertrand Marchand, Fred Deniger
 */
public class ZCalqueSelectionInteractionMulti extends ZCalqueSelectionInteractionAbstract {

    private Set zcalquesActifs_;

    /**
   * Objets s�lectionn�s pour les CalqueModele.
   */
    private Set zcalquesSelectionNonVide_;

    /**
   * Cr�ation d'un calque de s�lection sans objets s�lectionnables.
   */
    public ZCalqueSelectionInteractionMulti(final BGroupeCalque _donnees) {
        super(_donnees);
    }

    private void clearSelectionsCalquesActifs() {
        if (zcalquesActifs_ != null) {
            for (final Iterator it = zcalquesActifs_.iterator(); it.hasNext(); ) {
                ((ZCalqueAffichageDonneesInterface) it.next()).clearSelection();
            }
        }
        if (zcalquesSelectionNonVide_ != null) {
            for (final Iterator it = zcalquesSelectionNonVide_.iterator(); it.hasNext(); ) {
                ((ZCalqueAffichageDonneesInterface) it.next()).clearSelection();
            }
            zcalquesSelectionNonVide_.clear();
        }
    }

    private void clearSelectionsCalquesSelectionNonVide() {
        if (zcalquesSelectionNonVide_ != null) {
            for (final Iterator it = zcalquesSelectionNonVide_.iterator(); it.hasNext(); ) {
                ((ZCalqueAffichageDonneesInterface) it.next()).clearSelection();
            }
            zcalquesSelectionNonVide_.clear();
        }
    }

    /**
   * Recupere les donnees.
   */
    protected void formeSaisie() {
        if (zcalquesActifs_ == null) {
            return;
        }
        if (modificateur_.getModificateur() == EbliSelectionState.ACTION_REPLACE) {
            clearSelectionsCalquesSelectionNonVide();
        }
        if (mode_ == PONCTUEL || (mode_ == RECTANGLE && listePoints_.renvoie(0).distanceXY(listePoints_.renvoie(2)) < 4)) {
            final GrPoint pt = listePoints_.renvoie(0);
            pt.autoApplique(getVersReel());
            for (final Iterator it = zcalquesActifs_.iterator(); it.hasNext(); ) {
                final ZCalqueAffichageDonneesInterface zcalque = (ZCalqueAffichageDonneesInterface) it.next();
                final boolean selectionTrouve = zcalque.changeSelection(pt, tolerancePixel_, modificateur_.getModificateur());
                if (selectionTrouve) {
                    break;
                }
            }
        } else {
            final GrPolygone poly = plHelper_.toGrPolygone();
            poly.autoApplique(getVersReel());
            final LinearRing l = GISGeometryFactory.INSTANCE.createLinearRing(poly.sommets_.createCoordinateSequence(true));
            for (final Iterator it = zcalquesActifs_.iterator(); it.hasNext(); ) {
                final ZCalqueAffichageDonneesInterface zcalque = (ZCalqueAffichageDonneesInterface) it.next();
                zcalque.changeSelection(l, modificateur_.getModificateur(), EbliSelectionMode.MODE_ALL);
            }
        }
    }

    private void initCalqueActif() {
        if (zcalquesActifs_ == null) {
            zcalquesActifs_ = new HashSet();
        }
    }

    private void initCalqueSelectionNonVide() {
        if (zcalquesSelectionNonVide_ == null) {
            zcalquesSelectionNonVide_ = new HashSet();
        }
    }

    /**
   * Methode a appeler pour vider la liste des calques actifs et met la liste des calques ayant une selection non vide a
   * jour. Les calques sans selection sont enleves, puis les calques actifs avec une selection sont ajoutes.
   */
    private void clearCalquesActifs() {
        ZCalqueAffichageDonneesInterface z;
        if (zcalquesSelectionNonVide_ != null) {
            for (final Iterator it = zcalquesSelectionNonVide_.iterator(); it.hasNext(); ) {
                z = ((ZCalqueAffichageDonneesInterface) it.next());
                if (z != null && z.isSelectionEmpty()) {
                    zcalquesSelectionNonVide_.remove(z);
                }
            }
        }
        if (zcalquesActifs_ != null) {
            for (final Iterator it = zcalquesActifs_.iterator(); it.hasNext(); ) {
                z = (ZCalqueAffichageDonneesInterface) it.next();
                if (z != null && !z.isSelectionEmpty()) {
                    initCalqueSelectionNonVide();
                    zcalquesSelectionNonVide_.add(z);
                }
            }
            zcalquesActifs_.clear();
        }
    }

    /**
   * Efface les selections de tous les calques.
   */
    public void clearSelections() {
        clearSelectionsCalquesActifs();
        clearSelectionsCalquesSelectionNonVide();
    }

    public void paintDonnees(final Graphics2D _g, final GrMorphisme _versEcran, final GrMorphisme _versReel, final GrBoite _clipReel) {
        if ((zcalquesActifs_ != null) && zcalquesActifs_.size() > 0) {
            for (final Iterator it = zcalquesActifs_.iterator(); it.hasNext(); ) {
                final ZCalqueAffichageDonneesInterface calque = (ZCalqueAffichageDonneesInterface) it.next();
                if (calque != null && !calque.isSelectionEmpty()) {
                    calque.paintSelection(_g, trace_, _versEcran, _clipReel);
                }
            }
        }
    }
}
