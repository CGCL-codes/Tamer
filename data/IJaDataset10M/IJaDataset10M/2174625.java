package org.fudaa.fudaa.refonde;

import java.util.Vector;
import org.fudaa.ebli.geometrie.GrElement;
import org.fudaa.ebli.geometrie.GrMaillageElement;
import org.fudaa.ebli.geometrie.GrNoeud;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.geometrie.GrPolyligne;
import org.fudaa.ebli.geometrie.GrSegment;
import org.fudaa.ebli.geometrie.GrVecteur;
import org.fudaa.fudaa.commun.conversion.FudaaInterpolateurMaillage;

/**
 * Domaine g�ometrique de type digue. Ce domaine ne contient qu'un seul contour,
 * peut �tre maill� en T3 ou Q4 suivant un mailleur qui lui est propre.
 *
 * @version      $Id: RefondeDomaineDigue.java,v 1.10 2007-01-19 13:14:14 deniger Exp $
 * @author       Bertrand Marchand
 */
public class RefondeDomaineDigue extends RefondeDomaine {

    public static final int SOL = 2;

    public static final int OUVRAGE = 3;

    protected int typeMaillage_ = OUVRAGE;

    protected GrPolyligne extDigue_;

    protected int[] nbElements_;

    /**
   * Cr�ation d'un domaine vide
   */
    public RefondeDomaineDigue() {
        super();
        extDigue_ = null;
        nbElements_ = new int[0];
        setGroupeProprietes(new RefondeGroupeProprietes(RefondeGroupeProprietes.HOULE_FOND_PAROI_PERFOREE));
    }

    /**
   * Cr�ation d'un domaine de type digue
   */
    public RefondeDomaineDigue(RefondeContour _cntr) {
        this(_cntr, _cntr.getPolylignes()[0]);
    }

    /**
   * Cr�ation d'un domaine de type digue, en pr�cisant l'extr�mit� de digue
   */
    public RefondeDomaineDigue(RefondeContour _cntr, RefondePolyligne _poly) {
        super();
        setContour(_cntr);
        GrPolyligne[] pls = _cntr.getPolylignes();
        extDigue_ = _poly;
        nbElements_ = new int[pls.length / 2 - 1];
        for (int i = 0; i < nbElements_.length; i++) nbElements_[i] = 3;
        setGroupeProprietes(new RefondeGroupeProprietes(RefondeGroupeProprietes.HOULE_FOND_PAROI_PERFOREE));
    }

    /**
   * Nombre d'�l�ments par troncon, ordonn�s � partir de l'extremit� de digue
   */
    public void setNbElements(int[] _nbElements) {
        nbElements_ = _nbElements;
    }

    public int[] getNbElements() {
        return nbElements_;
    }

    /**
   * Type du maillage (SOL, OUVRAGE)
   */
    public void setTypeMaillage(int _type) {
        typeMaillage_ = _type;
    }

    public int getTypeMaillage() {
        return typeMaillage_;
    }

    /**
   * Polyligne extremit� de digue
   */
    public void setExtremiteDigue(GrPolyligne _pl) {
        extDigue_ = _pl;
    }

    public GrPolyligne getExtremiteDigue() {
        return extDigue_;
    }

    /**
   * Polyligne extremite de fin de digue
   */
    public GrPolyligne getExtremiteFinDigue() {
        RefondePolyligne[] pls = contours_[0].getPolylignes();
        int i;
        for (i = 0; i < pls.length; i++) if (pls[i] == extDigue_) break;
        return pls[(i + pls.length / 2) % pls.length];
    }

    /**
   * Contour
   */
    public void setContour(RefondeContour _cntr) {
        if (_cntr.getPolylignes().length % 2 != 0) throw new IllegalArgumentException("Le contour n'a pas un nombre pair de polylignes");
        setContours(new RefondeContour[] { _cntr });
    }

    public RefondeContour getContour() {
        return getContours()[0];
    }

    /**
   * Mailler la digue
   */
    public void mailler() {
        FudaaInterpolateurMaillage it;
        GrNoeud[] nds;
        GrMaillageElement r;
        RefondePolyligne[] pls;
        setMaillage(null);
        if (typeMaillage_ == SOL) r = maillerT3(); else {
            pls = getContour().getPolylignes();
            for (int i = 0; i < pls.length; i++) {
                if (pls[i].hasNoeuds()) throw new IllegalArgumentException("Impossible de mailler le domaine\n" + "une des polylignes du contour a d�j� des noeuds");
            }
            r = maillerQ4();
        }
        nds = r.noeuds();
        it = RefondeImplementation.projet.getGeometrie().interpolateur();
        for (int i = 0; i < nds.length; i++) nds[i].point_ = it.interpolePoint(nds[i].point_);
        setMaillage(r);
        return;
    }

    protected GrMaillageElement maillerT3() {
        throw new IllegalArgumentException("M�thode non impl�ment�e");
    }

    protected GrMaillageElement maillerQ4() {
        int nbTroncons = nbElements_.length;
        GrPolyligne[] plsOrigin = contours_[0].getPolylignes();
        GrPolyligne[] pls;
        GrPolyligne extOriente = null;
        RefondeContour ctOriente;
        GrNoeud nd;
        ctOriente = contours_[0].copie();
        pls = ctOriente.getPolylignes();
        for (int i = 0; i < plsOrigin.length; i++) if (plsOrigin[i] == extDigue_) {
            extOriente = pls[i];
            break;
        }
        ctOriente.orienteTrigo(true);
        pls = ctOriente.getPolylignes();
        int indiceExt;
        for (indiceExt = 0; indiceExt < pls.length; indiceExt++) if (pls[indiceExt] == extOriente) break;
        GrPoint[][] pTrcn = new GrPoint[nbTroncons][];
        GrPoint[][] pRect = new GrPoint[nbTroncons][];
        GrPoint[] pi = new GrPoint[4];
        RefondeDroite[] dNorm = new RefondeDroite[4];
        GrVecteur[] vNorm = new GrVecteur[2];
        GrSegment[] sg = new GrSegment[2];
        GrPolyligne[] pl = new GrPolyligne[2];
        for (int i = 0; i < nbTroncons; i++) {
            pTrcn[i] = new GrPoint[4];
            pRect[i] = new GrPoint[4];
            pl[0] = pls[(indiceExt + i + 1) % pls.length];
            pl[1] = pls[(indiceExt - i - 1 + pls.length) % pls.length];
            for (int j = 0; j < 2; j++) {
                pTrcn[i][j * 2] = pl[j].sommet(0);
                pTrcn[i][(j * 2) + 1] = pl[j].sommet(pl[j].nombre() - 1);
                vNorm[j] = pl[j].vecteur(0).rotationZ90();
                dNorm[j * 2] = new RefondeDroite(pTrcn[i][j * 2], vNorm[j]);
                dNorm[(j * 2) + 1] = new RefondeDroite(pTrcn[i][(j * 2) + 1], vNorm[j]);
                sg[j] = pl[(j + 1) % 2].segment(0);
                for (int k = 0; k < 2; k++) pi[j * 2 + k] = dNorm[j * 2 + k].intersectionSegmentXY(sg[j]);
            }
            if (pi[0] == null && pi[3] == null || pi[1] == null && pi[2] == null) throw new IllegalArgumentException("G�ometrie mal d�finie");
            if (pi[0] == null) {
                pRect[i][0] = pi[3];
                pRect[i][3] = pTrcn[i][3];
            } else {
                pRect[i][0] = pTrcn[i][0];
                pRect[i][3] = pi[0];
            }
            if (pi[1] == null) {
                pRect[i][1] = pi[2];
                pRect[i][2] = pTrcn[i][2];
            } else {
                pRect[i][1] = pTrcn[i][1];
                pRect[i][2] = pi[1];
            }
        }
        Vector vnds = new Vector();
        for (int i = 0; i < nbTroncons; i++) {
            int nbNoeuds = nbElements_[i] + 1;
            for (int j = 0; j < nbNoeuds; j++) {
                if (j == 0 && (i == 0 || pRect[i][0].distanceXY(pRect[i - 1][1]) < 1.e-4 || pRect[i][3].distanceXY(pRect[i - 1][2]) < 1.e-4)) {
                    vnds.add(nd = new GrNoeud(pTrcn[i][0]));
                    nd.data(new RefondeNoeudData());
                    vnds.add(nd = new GrNoeud(pTrcn[i][3]));
                    nd.data(new RefondeNoeudData());
                } else if (j == nbNoeuds - 1 && i == nbTroncons - 1) {
                    vnds.add(nd = new GrNoeud(pTrcn[i][1]));
                    nd.data(new RefondeNoeudData());
                    vnds.add(nd = new GrNoeud(pTrcn[i][2]));
                    nd.data(new RefondeNoeudData());
                } else if (j != nbNoeuds - 1 || (pRect[i][1].distanceXY(pRect[i + 1][0]) > 1.e-4 && pRect[i][2].distanceXY(pRect[i + 1][3]) > 1.e-4)) {
                    vnds.add(nd = new GrNoeud(pRect[i][0].x_ + (pRect[i][1].x_ - pRect[i][0].x_) * j / (nbNoeuds - 1), pRect[i][0].y_ + (pRect[i][1].y_ - pRect[i][0].y_) * j / (nbNoeuds - 1), 0.));
                    nd.data(new RefondeNoeudData());
                    vnds.add(nd = new GrNoeud(pRect[i][3].x_ + (pRect[i][2].x_ - pRect[i][3].x_) * j / (nbNoeuds - 1), pRect[i][3].y_ + (pRect[i][2].y_ - pRect[i][3].y_) * j / (nbNoeuds - 1), 0.));
                    nd.data(new RefondeNoeudData());
                }
            }
        }
        GrNoeud[] nds = new GrNoeud[vnds.size()];
        vnds.toArray(nds);
        GrElement[] els = new GrElement[(vnds.size() - 2) / 2];
        for (int i = 0; i < els.length; i++) {
            GrNoeud[] ndsEle = new GrNoeud[] { nds[i * 2 + 2], nds[i * 2 + 0], nds[i * 2 + 1], nds[i * 2 + 3] };
            els[i] = new GrElement(ndsEle, GrElement.Q4);
            els[i].data(new RefondeElementData());
        }
        return new GrMaillageElement(els, nds);
    }
}
