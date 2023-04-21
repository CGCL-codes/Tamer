package org.fudaa.fudaa.lido.ihmhelper.gestion;

import java.util.Vector;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuDialogError;
import org.fudaa.dodico.corba.lido.SParametresBiefBlocPRO;
import org.fudaa.dodico.corba.lido.SParametresBiefSituLigneRZO;
import org.fudaa.dodico.corba.lido.SParametresPRO;
import org.fudaa.dodico.corba.lido.SParametresRZO;
import org.fudaa.fudaa.commun.projet.FudaaParamEvent;
import org.fudaa.fudaa.commun.projet.FudaaProjet;
import org.fudaa.fudaa.lido.LidoApplication;
import org.fudaa.fudaa.lido.LidoResource;

/**
 * @version      $Revision: 1.11 $ $Date: 2006-09-19 15:05:02 $ by $Author: deniger $
 * @author       Axel von Arnim
 */
public class LidoPH_Bief extends LidoPH_Base {

    private SParametresRZO rzo_;

    private SParametresPRO pro_;

    private static final double DBIEF = LidoResource.BIEF.DBIEF;

    LidoPH_Bief(final FudaaProjet p, final LidoParamsHelper ph) {
        super(p, ph);
    }

    void setProjet(final FudaaProjet p) {
        rzo_ = (SParametresRZO) p.getParam(LidoResource.RZO);
        if (rzo_ == null) {
            System.err.println("LidoPH_Bief: Warning: passing null RZO to constructor");
        }
        pro_ = (SParametresPRO) p.getParam(LidoResource.PRO);
        if (pro_ == null) {
            System.err.println("LidoPH_Bief: Warning: passing null PRO to constructor");
        }
    }

    public SParametresBiefSituLigneRZO nouveauBief(final int pos) {
        return nouveauBief(pos, 0., 0.);
    }

    public SParametresBiefSituLigneRZO nouveauBief(final SParametresBiefBlocPRO[] profils, final double offset) {
        if ((rzo_ == null) || (rzo_.blocSitus == null) || (rzo_.blocSitus.ligneSitu == null)) {
            System.err.println("LidoPH_Bief: Warning: biefs null");
            return null;
        }
        if ((profils == null) || (profils.length == 0)) {
            return null;
        }
        double decalage = 0.;
        if (rzo_.blocSitus.ligneSitu.length > 0) {
            decalage = getMaxBiefAbscisse() + DBIEF - profils[0].abscisse;
            if (offset > 0) {
                if (offset > decalage) {
                    decalage = offset;
                } else {
                    double lastoffset = offset;
                    double max = profils[0].abscisse;
                    for (int i = 1; i < profils.length; i++) {
                        if (profils[i].abscisse > max) {
                            max = profils[i].abscisse;
                        }
                        lastoffset = max + offset;
                    }
                    final int[] biefsRecouv = verifieRecouvrement(offset, lastoffset);
                    String err = "";
                    for (int i = 0; i < biefsRecouv.length; i++) {
                        err = err + biefsRecouv[i] + " ";
                    }
                    if (biefsRecouv.length > 0) {
                        new BuDialogError((BuCommonInterface) LidoApplication.FRAME, ((BuCommonInterface) LidoApplication.FRAME).getInformationsSoftware(), "Le d�calage " + offset + " n'est pas valide.\n" + "Il y a recouvrement avec les biefs:\n" + err + "\n").activate();
                        return null;
                    }
                    decalage = offset;
                }
            }
            for (int i = 0; i < profils.length; i++) {
                profils[i].abscisse += decalage;
                profils[i].abscisse = (double) (Math.round(profils[i].abscisse * 100)) / 100;
            }
        }
        return nouveauBief(getNumeroBiefLibre(), profils[0].abscisse, profils[profils.length - 1].abscisse);
    }

    public SParametresBiefSituLigneRZO nouveauBief(final int pos, final double x1, final double x2) {
        if ((rzo_ == null) || (rzo_.blocSitus == null) || (rzo_.blocSitus.ligneSitu == null)) {
            System.err.println("LidoPH_Bief: Warning: biefs null");
            return null;
        }
        final SParametresBiefSituLigneRZO[] biefs = rzo_.blocSitus.ligneSitu;
        final SParametresBiefSituLigneRZO nouv = new SParametresBiefSituLigneRZO();
        nouv.numBief = pos;
        nouv.x1 = x1;
        nouv.x2 = x2;
        nouv.branch1 = getNumeroExtremiteLibre();
        nouv.branch2 = nouv.branch1 + 1;
        final SParametresBiefSituLigneRZO[] nouvBiefs = new SParametresBiefSituLigneRZO[biefs.length + 1];
        for (int i = 0; i < pos; i++) {
            nouvBiefs[i] = biefs[i];
        }
        nouvBiefs[pos] = nouv;
        for (int i = pos; i < biefs.length; i++) {
            nouvBiefs[i + 1] = biefs[i];
        }
        rzo_.blocSitus.ligneSitu = nouvBiefs;
        rzo_.nbBief++;
        for (int i = 0; i < rzo_.blocSitus.ligneSitu.length; i++) {
            rzo_.blocSitus.ligneSitu[i].numBief = i;
        }
        ph_.SINGULARITE().nouveauSingularite(nouv);
        prop_.firePropertyChange("biefs", biefs, nouvBiefs);
        fireParamStructCreated(new FudaaParamEvent(this, 0, LidoResource.RZO, nouv, "bief " + (nouv.numBief + 1)));
        return nouv;
    }

    public SParametresBiefSituLigneRZO[] supprimeSelection(final SParametresBiefSituLigneRZO[] sel) {
        if ((rzo_ == null) || (rzo_.blocSitus == null) || (rzo_.blocSitus.ligneSitu == null)) {
            System.err.println("LidoPH_Bief: Warning: biefs null");
            return null;
        }
        if (sel == null) {
            return null;
        }
        final SParametresBiefSituLigneRZO[] biefs = rzo_.blocSitus.ligneSitu;
        final SParametresBiefSituLigneRZO[] nouvBiefs = new SParametresBiefSituLigneRZO[biefs.length - sel.length];
        int n = 0;
        int j = 0;
        for (int i = 0; i < biefs.length; i++) {
            for (j = 0; j < sel.length; j++) {
                if (biefs[i] == sel[j]) {
                    break;
                }
            }
            if (j == sel.length) {
                nouvBiefs[n++] = biefs[i];
            } else {
                ph_.NOEUD().majBiefSupprime(biefs[i]);
                ph_.LIMITE().majBiefSupprime(biefs[i]);
                fireParamStructDeleted(new FudaaParamEvent(this, 0, LidoResource.RZO, biefs[i], "bief " + (biefs[i].numBief + 1)));
            }
        }
        rzo_.blocSitus.ligneSitu = nouvBiefs;
        rzo_.nbBief = nouvBiefs.length;
        ph_.SINGULARITE().supprimeSelectionBiefs(sel);
        recalculeExtremites();
        for (int i = 0; i < rzo_.blocSitus.ligneSitu.length; i++) {
            rzo_.blocSitus.ligneSitu[i].numBief = i;
        }
        prop_.firePropertyChange("biefs", biefs, nouvBiefs);
        return sel;
    }

    public SParametresBiefSituLigneRZO scindeBief(final SParametresBiefSituLigneRZO bief, final SParametresBiefBlocPRO profil) {
        if ((rzo_ == null) || (rzo_.blocSitus == null) || (rzo_.blocSitus.ligneSitu == null)) {
            System.err.println("LidoPH_Bief: Warning: biefs null");
            return null;
        }
        if ((pro_ == null) || (pro_.profilsBief == null)) {
            System.err.println("LidoPH_Bief: Warning: profils null");
            return null;
        }
        final int verifbief = getBiefContenantAbscisse(profil.abscisse);
        if (verifbief != bief.numBief) {
            System.err.println("LidoPH_Bief: Erreur: le profil " + profil.indice + " n'appartient pas au bief " + bief.numBief);
            return null;
        }
        final SParametresBiefSituLigneRZO[] biefs = rzo_.blocSitus.ligneSitu;
        final SParametresBiefBlocPRO profilSuivant = ph_.PROFIL().profilSuivant(profil);
        if (profilSuivant == null) {
            return null;
        }
        final SParametresBiefSituLigneRZO nouv = new SParametresBiefSituLigneRZO();
        nouv.numBief = getNumeroBiefLibre();
        nouv.x1 = profilSuivant.abscisse;
        nouv.x2 = bief.x2;
        nouv.branch1 = getNumeroExtremiteLibre();
        nouv.branch2 = bief.branch2;
        bief.x2 = profil.abscisse;
        bief.branch2 = nouv.branch1 + 1;
        final SParametresBiefSituLigneRZO[] nouvBiefs = new SParametresBiefSituLigneRZO[biefs.length + 1];
        final int pos = bief.numBief + 1;
        for (int i = 0; i < pos; i++) {
            nouvBiefs[i] = biefs[i];
        }
        nouvBiefs[pos] = nouv;
        for (int i = pos; i < biefs.length; i++) {
            nouvBiefs[i + 1] = biefs[i];
        }
        rzo_.blocSitus.ligneSitu = nouvBiefs;
        rzo_.nbBief++;
        for (int i = 0; i < rzo_.blocSitus.ligneSitu.length; i++) {
            rzo_.blocSitus.ligneSitu[i].numBief = i;
        }
        ph_.SINGULARITE().nouveauSingularite(nouv);
        ph_.NOEUD().nouveauNoeud(new int[] { bief.branch2, nouv.branch1 });
        prop_.firePropertyChange("biefs", biefs, nouvBiefs);
        fireParamStructCreated(new FudaaParamEvent(this, 0, LidoResource.RZO, nouv, "bief " + (nouv.numBief + 1)));
        return nouv;
    }

    public Integer[] extremitesLibres() {
        if ((rzo_ == null) || (rzo_.blocSitus == null) || (rzo_.blocSitus.ligneSitu == null)) {
            System.err.println("LidoPH_Bief: Warning: biefs null");
            return null;
        }
        if ((rzo_ == null) || (rzo_.blocNoeuds == null) || (rzo_.blocNoeuds.ligneNoeud == null)) {
            System.err.println("LidoPH_Bief: Warning: noeuds null");
            return null;
        }
        final Vector extr = new Vector();
        boolean trouve1 = false;
        boolean trouve2 = false;
        for (int i = 0; i < rzo_.blocSitus.ligneSitu.length; i++) {
            for (int j = 0; j < rzo_.blocNoeuds.ligneNoeud.length; j++) {
                for (int k = 0; k < rzo_.blocNoeuds.ligneNoeud[j].noeud.length; k++) {
                    if (rzo_.blocSitus.ligneSitu[i].branch1 == rzo_.blocNoeuds.ligneNoeud[j].noeud[k]) {
                        trouve1 = true;
                    }
                    if (rzo_.blocSitus.ligneSitu[i].branch2 == rzo_.blocNoeuds.ligneNoeud[j].noeud[k]) {
                        trouve2 = true;
                    }
                }
            }
            if (!trouve1) {
                extr.add(new Integer(rzo_.blocSitus.ligneSitu[i].branch1));
            }
            if (!trouve2) {
                extr.add(new Integer(rzo_.blocSitus.ligneSitu[i].branch2));
            }
            trouve1 = trouve2 = false;
        }
        final Integer[] res = new Integer[extr.size()];
        for (int i = 0; i < extr.size(); i++) {
            res[i] = (Integer) extr.get(i);
        }
        return res;
    }

    public int getBiefContenantAbscisse(final double abs) {
        if ((rzo_ == null) || (rzo_.blocSitus == null) || (rzo_.blocSitus.ligneSitu == null)) {
            System.err.println("LidoPH_Bief: Warning: biefs null");
            return -1;
        }
        for (int i = 0; i < rzo_.nbBief; i++) {
            if ((Math.round(LidoResource.PRECISION * rzo_.blocSitus.ligneSitu[i].x1) <= Math.round(LidoResource.PRECISION * abs)) && (Math.round(LidoResource.PRECISION * rzo_.blocSitus.ligneSitu[i].x2) >= Math.round(LidoResource.PRECISION * abs))) {
                return i;
            }
        }
        return -1;
    }

    public SParametresBiefSituLigneRZO getBiefContenantExtremite(final int extr) {
        if ((rzo_ == null) || (rzo_.blocSitus == null) || (rzo_.blocSitus.ligneSitu == null)) {
            System.err.println("LidoPH_Bief: Warning: biefs null");
            return null;
        }
        for (int i = 0; i < rzo_.nbBief; i++) {
            if ((rzo_.blocSitus.ligneSitu[i].branch1 == extr) || (rzo_.blocSitus.ligneSitu[i].branch2 == extr)) {
                return rzo_.blocSitus.ligneSitu[i];
            }
        }
        return null;
    }

    public double getAbscisseExtremite(final int extr) {
        if ((rzo_ == null) || (rzo_.blocSitus == null) || (rzo_.blocSitus.ligneSitu == null)) {
            System.err.println("LidoPH_Bief: Warning: biefs null");
            return 0.;
        }
        for (int i = 0; i < rzo_.nbBief; i++) {
            if (rzo_.blocSitus.ligneSitu[i].branch1 == extr) {
                return rzo_.blocSitus.ligneSitu[i].x1;
            }
            if (rzo_.blocSitus.ligneSitu[i].branch2 == extr) {
                return rzo_.blocSitus.ligneSitu[i].x2;
            }
        }
        return 0.;
    }

    public void reindiceBiefs() {
        if ((rzo_ == null) || (rzo_.blocSitus == null) || (rzo_.blocSitus.ligneSitu == null)) {
            System.err.println("LidoPH_Bief: Warning: biefs null");
            return;
        }
        boolean maj = false;
        ph_.SINGULARITE().reindiceSingularites();
        for (int i = 0; i < rzo_.nbBief; i++) {
            if (rzo_.blocSitus.ligneSitu[i].numBief != i) {
                maj = true;
                rzo_.blocSitus.ligneSitu[i].numBief = i;
            }
        }
        if (maj) {
            fireParamStructModified(new FudaaParamEvent(this, 0, LidoResource.RZO, rzo_.blocSitus.ligneSitu, "bief reordonnes"));
            prop_.firePropertyChange("biefs", null, rzo_.blocSitus.ligneSitu);
        }
    }

    public int compteProfilsDansBief(final SParametresBiefSituLigneRZO b) {
        if ((pro_ == null) || (pro_.profilsBief == null)) {
            System.err.println("LidoPH_Bief: Warning: profils null");
            return 0;
        }
        int count = 0;
        for (int i = 0; i < pro_.nbProfils; i++) {
            if ((Math.round(LidoResource.PRECISION * pro_.profilsBief[i].abscisse) >= Math.round(LidoResource.PRECISION * b.x1)) && (Math.round(LidoResource.PRECISION * pro_.profilsBief[i].abscisse) <= Math.round(LidoResource.PRECISION * b.x2))) {
                count++;
            }
        }
        return count;
    }

    boolean majProfilSupprime(final SParametresBiefBlocPRO p) {
        boolean maj = false;
        if ((rzo_ == null) || (rzo_.blocSitus == null) || (rzo_.blocSitus.ligneSitu == null)) {
            System.err.println("LidoPH_Bief: Warning: biefs null");
            return maj;
        }
        if (p == null) {
            return maj;
        }
        for (int b = 0; b < rzo_.nbBief; b++) {
            if (Math.round(LidoResource.PRECISION * rzo_.blocSitus.ligneSitu[b].x1) == Math.round(LidoResource.PRECISION * p.abscisse)) {
                if ((p.indice >= (pro_.nbProfils - 1)) || (Math.round(LidoResource.PRECISION * rzo_.blocSitus.ligneSitu[b].x2) == Math.round(LidoResource.PRECISION * p.abscisse))) {
                    supprimeSelection(new SParametresBiefSituLigneRZO[] { rzo_.blocSitus.ligneSitu[b] });
                    System.err.println("Suppression bief " + b);
                } else {
                    rzo_.blocSitus.ligneSitu[b].x1 = pro_.profilsBief[p.indice + 1].abscisse;
                    fireParamStructModified(new FudaaParamEvent(this, 0, LidoResource.RZO, rzo_.blocSitus.ligneSitu[b], "bief " + (rzo_.blocSitus.ligneSitu[b].numBief + 1)));
                    System.err.println("Bief " + b + ", branch1: p" + p.indice + " -> p" + (p.indice + 1));
                }
                prop_.firePropertyChange("biefs", null, rzo_.blocSitus.ligneSitu);
                maj = true;
            } else if (Math.round(LidoResource.PRECISION * rzo_.blocSitus.ligneSitu[b].x2) == Math.round(LidoResource.PRECISION * p.abscisse)) {
                if (p.indice <= 0) {
                    supprimeSelection(new SParametresBiefSituLigneRZO[] { rzo_.blocSitus.ligneSitu[b] });
                    System.err.println("Suppression bief " + b);
                } else {
                    rzo_.blocSitus.ligneSitu[b].x2 = pro_.profilsBief[p.indice - 1].abscisse;
                    fireParamStructModified(new FudaaParamEvent(this, 0, LidoResource.RZO, rzo_.blocSitus.ligneSitu[b], "bief " + (rzo_.blocSitus.ligneSitu[b].numBief + 1)));
                    System.err.println("Bief " + b + ", branch2: p" + p.indice + " -> p" + (p.indice - 1));
                }
                prop_.firePropertyChange("biefs", null, rzo_.blocSitus.ligneSitu);
                maj = true;
            }
        }
        return maj;
    }

    private int getNumeroExtremiteLibre() {
        int maxExt = 0;
        for (int i = 0; i < rzo_.nbBief; i++) {
            if (rzo_.blocSitus.ligneSitu[i].branch1 > maxExt) {
                maxExt = rzo_.blocSitus.ligneSitu[i].branch1;
            }
            if (rzo_.blocSitus.ligneSitu[i].branch2 > maxExt) {
                maxExt = rzo_.blocSitus.ligneSitu[i].branch2;
            }
        }
        return maxExt + 1;
    }

    private int getNumeroBiefLibre() {
        return rzo_.nbBief;
    }

    private double getMaxBiefAbscisse() {
        double abs = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < rzo_.blocSitus.ligneSitu.length; i++) {
            if (rzo_.blocSitus.ligneSitu[i].x2 > abs) {
                abs = rzo_.blocSitus.ligneSitu[i].x2;
            }
        }
        return abs;
    }

    private void recalculeExtremites() {
        int oldBranch = 0;
        for (int i = 0; i < rzo_.blocSitus.ligneSitu.length; i++) {
            oldBranch = rzo_.blocSitus.ligneSitu[i].branch1;
            rzo_.blocSitus.ligneSitu[i].branch1 = (2 * i + 1);
            ph_.NOEUD().majExtrRenommee(oldBranch, rzo_.blocSitus.ligneSitu[i].branch1);
            ph_.LIMITE().majExtrRenommee(oldBranch, rzo_.blocSitus.ligneSitu[i].branch1);
            oldBranch = rzo_.blocSitus.ligneSitu[i].branch2;
            rzo_.blocSitus.ligneSitu[i].branch2 = (2 * i + 2);
            ph_.NOEUD().majExtrRenommee(oldBranch, rzo_.blocSitus.ligneSitu[i].branch2);
            ph_.LIMITE().majExtrRenommee(oldBranch, rzo_.blocSitus.ligneSitu[i].branch2);
        }
    }

    private int[] verifieRecouvrement(final double x1, final double x2) {
        final Vector res = new Vector();
        for (int i = 0; i < rzo_.blocSitus.ligneSitu.length; i++) {
            if (((rzo_.blocSitus.ligneSitu[i].x1 > x1) && (rzo_.blocSitus.ligneSitu[i].x1 < x2)) || ((rzo_.blocSitus.ligneSitu[i].x2 > x1) && (rzo_.blocSitus.ligneSitu[i].x2 < x2))) {
                res.add(new Integer(rzo_.blocSitus.ligneSitu[i].numBief));
            }
        }
        final int[] resi = new int[res.size()];
        for (int i = 0; i < resi.length; i++) {
            resi[i] = ((Integer) res.get(i)).intValue();
        }
        return resi;
    }
}
