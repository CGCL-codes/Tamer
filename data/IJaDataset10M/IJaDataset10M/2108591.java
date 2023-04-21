package org.fudaa.fudaa.hydraulique1d.metier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import org.fudaa.fudaa.hydraulique1d.metier.MetierBief;
import org.fudaa.fudaa.hydraulique1d.metier.MetierConditionsInitiales;
import org.fudaa.fudaa.hydraulique1d.metier.MetierDonneesHydrauliques;
import org.fudaa.fudaa.hydraulique1d.metier.MetierLaisse;
import org.fudaa.fudaa.hydraulique1d.metier.MetierLoiHydraulique;
import org.fudaa.fudaa.hydraulique1d.metier.evenement.H1dObjetEvent;
import org.fudaa.fudaa.hydraulique1d.metier.evenement.H1dObjetEventListener;
import org.fudaa.fudaa.hydraulique1d.metier.evenement.Notifieur;
import org.fudaa.fudaa.hydraulique1d.metier.loi.MetierLoiGeometrique;
import org.fudaa.fudaa.hydraulique1d.metier.loi.MetierLoiHydrogramme;
import org.fudaa.fudaa.hydraulique1d.metier.loi.MetierLoiLimniHydrogramme;
import org.fudaa.fudaa.hydraulique1d.metier.loi.MetierLoiLimnigramme;
import org.fudaa.fudaa.hydraulique1d.metier.loi.MetierLoiOuvertureVanne;
import org.fudaa.fudaa.hydraulique1d.metier.loi.MetierLoiRegulation;
import org.fudaa.fudaa.hydraulique1d.metier.loi.MetierLoiSeuil;
import org.fudaa.fudaa.hydraulique1d.metier.loi.MetierLoiTarage;
import org.fudaa.fudaa.hydraulique1d.metier.MetierHydraulique1d;
import org.fudaa.fudaa.hydraulique1d.metier.loi.MetierLoiTracer;

/**
 * Impl�mentation de l'objet m�tier "donn�es hydrauliques" de l'�tude.
 * Contients les conditions initiales, les lois hydrauliques, les laisses de crues.
 * @version      $Revision: 1.1.2.1 $ $Date: 2007-09-06 14:39:57 $ by $Author: bmarchan $
 * @author       Jean-Marc Lacombe
 */
public class MetierDonneesHydrauliques extends MetierHydraulique1d implements H1dObjetEventListener {

    /*** MetierHydraulique1d ***/
    public void initialise(MetierHydraulique1d _o) {
        if (_o instanceof MetierDonneesHydrauliques) {
            MetierDonneesHydrauliques c = (MetierDonneesHydrauliques) _o;
            conditionsInitiales((MetierConditionsInitiales) c.conditionsInitiales().creeClone());
            if (c.lois() != null) {
                MetierLoiHydraulique[] il = new MetierLoiHydraulique[c.lois().length];
                for (int i = 0; i < il.length; i++) il[i] = (MetierLoiHydraulique) c.lois()[i].creeClone();
                lois(il);
            }
            if (c.laisses() != null) {
                MetierLaisse[] il = new MetierLaisse[c.laisses().length];
                for (int i = 0; i < il.length; i++) il[i] = (MetierLaisse) c.laisses()[i].creeClone();
                laisses(il);
            }
        }
    }

    public final MetierHydraulique1d creeClone() {
        MetierDonneesHydrauliques c = new MetierDonneesHydrauliques();
        c.initialise(this);
        return c;
    }

    public final String toString() {
        String s = "donneesHydrauliques";
        return s;
    }

    public MetierDonneesHydrauliques() {
        super();
        lois_ = new MetierLoiHydraulique[0];
        conditionsInitiales_ = new MetierConditionsInitiales();
        laisses_ = new MetierLaisse[0];
        Notifieur.getNotifieur().addObjetEventListener(this);
        notifieObjetCree();
    }

    public void dispose() {
        Notifieur.getNotifieur().removeObjetEventListener(this);
        lois_ = null;
        conditionsInitiales_ = null;
        laisses_ = null;
        super.dispose();
    }

    public void objetCree(H1dObjetEvent e) {
    }

    public void objetSupprime(H1dObjetEvent e) {
        MetierHydraulique1d src = e.getSource();
        if (src instanceof MetierBief) {
            supprimePointsLigneEauInitAvecBief((MetierBief) src);
            supprimeZonesSechesAvecBief((MetierBief) src);
        }
    }

    public void objetModifie(H1dObjetEvent e) {
        if ((e.getSource() instanceof MetierBief) && ("indice".equals(e.getChamp()))) {
            System.out.println("MODICATION DE L'INDICE DU BIEF");
            System.out.println("MetierDonneesHydrauliques objetModifie(H1dObjetEvent e=)");
            System.out.println("\t e.getSource=" + e.getSource().getClass().getName());
            System.out.println("\t e.getChamp=" + e.getChamp());
            System.out.println("\t e.getMessage=" + e.getMessage());
            System.out.println("\t e.isConsomme=" + e.isConsomme());
            int nouveauNumero = ((MetierBief) e.getSource()).indice() + 1;
            StringTokenizer st = new StringTokenizer(e.getMessage(), "()");
            if (st.countTokens() >= 2) {
                st.nextToken();
                int ancienNumero = Integer.parseInt(st.nextToken());
                miseAJourNumeroBiefPointsLigneEauInit(nouveauNumero, ancienNumero);
            }
        }
    }

    private MetierConditionsInitiales conditionsInitiales_;

    public MetierConditionsInitiales conditionsInitiales() {
        return conditionsInitiales_;
    }

    public void conditionsInitiales(MetierConditionsInitiales conditionsInitiales) {
        if (conditionsInitiales_ == conditionsInitiales) return;
        conditionsInitiales_ = conditionsInitiales;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "conditionsInitiales");
    }

    private MetierLoiHydraulique[] lois_;

    public MetierLoiHydraulique[] lois() {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        return lois_;
    }

    public void lois(MetierLoiHydraulique[] lois) {
        if (lois == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        if (egale(lois_, lois)) return;
        lois_ = lois;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "lois");
    }

    private MetierLaisse[] laisses_;

    public MetierLaisse[] laisses() {
        return laisses_;
    }

    public void laisses(MetierLaisse[] laisses) {
        if (egale(laisses_, laisses)) return;
        laisses_ = laisses;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "laisses");
    }

    public void ajouteLoi(MetierLoiHydraulique loi) {
        if (loi == null) return;
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        if (isContient(loi)) return;
        List listLoi = new ArrayList(Arrays.asList(lois_));
        listLoi.add(loi);
        lois_ = (MetierLoiHydraulique[]) listLoi.toArray(new MetierLoiHydraulique[0]);
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "lois");
        initIndiceLois();
    }

    private final boolean isContient(MetierLoiHydraulique loi) {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        if (loi != null) {
            for (int i = 0; i < lois_.length; i++) {
                if (loi.numero() == lois_[i].numero()) return true;
            }
        }
        return false;
    }

    public void supprimeLois(MetierLoiHydraulique[] _lois) {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
            return;
        }
        if (_lois == null) return;
        if (_lois.length == 0) return;
        Vector newlois = new Vector();
        boolean LoiTrouvee = false;
        for (int i = 0; i < lois_.length; i++) {
            for (int j = 0; j < _lois.length; j++) {
                if (lois_[i] == _lois[j]) {
                    MetierLoiHydraulique.supprimeLoiHydraulique(lois_[i]);
                    LoiTrouvee = true;
                }
                if (LoiTrouvee) break;
            }
            if (!LoiTrouvee) newlois.add(lois_[i]);
            LoiTrouvee = false;
        }
        lois_ = new MetierLoiHydraulique[newlois.size()];
        for (int i = 0; i < lois_.length; i++) lois_[i] = (MetierLoiHydraulique) newlois.get(i);
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "lois");
        initIndiceLois();
    }

    public MetierLaisse creeLaisse(MetierBief biefRattache) {
        MetierLaisse laisse = new MetierLaisse();
        laisse.site().biefRattache(biefRattache);
        MetierLaisse laisses[] = new MetierLaisse[laisses_.length + 1];
        for (int i = 0; i < laisses_.length; i++) laisses[i] = laisses_[i];
        laisses[laisses.length - 1] = laisse;
        laisses(laisses);
        return laisse;
    }

    public void supprimeLaisses(MetierLaisse[] laisses) {
        if (laisses_ == null) return;
        for (int i = 0; i < laisses.length; i++) {
            supprimeLaisse(laisses[i]);
        }
    }

    protected void supprimeLaisse(MetierLaisse laisse) {
        if (laisses_ == null) return;
        Vector newlaiss = new Vector();
        for (int i = 0; i < laisses_.length; i++) {
            if (laisses_[i] == laisse) {
                laisse.supprime();
            } else newlaiss.add(laisses_[i]);
        }
        MetierLaisse[] nlaisses = new MetierLaisse[newlaiss.size()];
        for (int i = 0; i < nlaisses.length; i++) nlaisses[i] = (MetierLaisse) newlaiss.get(i);
        laisses(nlaisses);
    }

    public void supprimeZonesSechesAvecBief(MetierBief bief) {
        if (conditionsInitiales() != null) {
            conditionsInitiales().supprimeZonesSechesAvecBief(bief);
        }
    }

    public void supprimePointsLigneEauInitAvecBief(MetierBief bief) {
        System.out.println("MetierDonneesHydrauliques supprimePointsLigneEauInitAvecBief(..)");
        System.out.println("bief.indice=" + bief.indice());
        if (conditionsInitiales() != null) {
            if (conditionsInitiales().ligneEauInitiale() != null) {
                conditionsInitiales().ligneEauInitiale().supprimePointsBiefNumero(bief.indice() + 1);
            }
        }
    }

    public void miseAJourNumeroBiefPointsLigneEauInit(int nouveau, int ancien) {
        if (conditionsInitiales() != null) {
            if (conditionsInitiales().ligneEauInitiale() != null) {
                conditionsInitiales().ligneEauInitiale().miseAJourNumeroBiefPointsLigneEauInit(nouveau, ancien);
            }
        }
    }

    public MetierLoiGeometrique creeLoiGeometrique() {
        MetierLoiGeometrique loi = new MetierLoiGeometrique();
        ajouteLoi(loi);
        return loi;
    }

    public MetierLoiHydrogramme creeLoiHydrogramme() {
        MetierLoiHydrogramme loi = new MetierLoiHydrogramme();
        ajouteLoi(loi);
        return loi;
    }

    public MetierLoiLimnigramme creeLoiLimnigramme() {
        MetierLoiLimnigramme loi = new MetierLoiLimnigramme();
        ajouteLoi(loi);
        return loi;
    }

    public MetierLoiLimniHydrogramme creeLoiLimniHydrogramme() {
        MetierLoiLimniHydrogramme loi = new MetierLoiLimniHydrogramme();
        ajouteLoi(loi);
        return loi;
    }

    public MetierLoiOuvertureVanne creeLoiOuvertureVanne() {
        MetierLoiOuvertureVanne loi = new MetierLoiOuvertureVanne();
        ajouteLoi(loi);
        return loi;
    }

    public MetierLoiRegulation creeLoiRegulation() {
        MetierLoiRegulation loi = new MetierLoiRegulation();
        ajouteLoi(loi);
        return loi;
    }

    public MetierLoiSeuil creeLoiSeuil() {
        MetierLoiSeuil loi = new MetierLoiSeuil();
        ajouteLoi(loi);
        return loi;
    }

    public MetierLoiTarage creeLoiTarage() {
        MetierLoiTarage loi = new MetierLoiTarage();
        ajouteLoi(loi);
        return loi;
    }

    public MetierLoiTracer creeLoiTracer(int nbColonne) {
        MetierLoiTracer loi = new MetierLoiTracer();
        loi.setTailleTableau(nbColonne);
        ajouteLoi(loi);
        return loi;
    }

    public MetierLoiHydraulique getLoi(int numero) {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        MetierLoiHydraulique loi = null;
        for (int i = 0; i < lois_.length; i++) {
            if (lois_[i].numero() == numero) {
                loi = lois_[i];
                break;
            }
        }
        return loi;
    }

    public int getIndiceLoi(MetierLoiHydraulique loi) {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        MetierLoiHydraulique[] loisSaufTracer;
        loisSaufTracer = getToutesLoisSaufTracer();
        for (int i = 0; i < loisSaufTracer.length; i++) {
            if (loisSaufTracer[i] == loi) {
                return i;
            }
        }
        return -1;
    }

    public int getIndiceLoiTracer(MetierLoiTracer loi) {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        MetierLoiTracer[] loisTracer;
        loisTracer = getLoisTracer();
        for (int i = 0; i < loisTracer.length; i++) {
            if (loisTracer[i] == loi) {
                return i;
            }
        }
        return -1;
    }

    public void initIndiceLois() {
        ArrayList vLois = new ArrayList();
        ArrayList vLoisGeo = new ArrayList();
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        for (int i = 0; i < lois_.length; i++) {
            if (lois_[i] instanceof MetierLoiGeometrique) vLoisGeo.add(lois_[i]); else vLois.add(lois_[i]);
        }
        vLois.addAll(vLoisGeo);
        lois_ = (MetierLoiHydraulique[]) vLois.toArray(new MetierLoiHydraulique[0]);
        for (int i = 0; i < lois_.length; i++) {
            lois_[i].indice(i);
        }
    }

    public MetierLoiHydraulique[] getToutesLoisSaufGeometrique() {
        ArrayList vLois = new ArrayList();
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        for (int i = 0; i < lois_.length; i++) {
            if (!(lois_[i] instanceof MetierLoiGeometrique)) vLois.add(lois_[i]);
        }
        MetierLoiHydraulique[] tLois = (MetierLoiHydraulique[]) vLois.toArray(new MetierLoiHydraulique[0]);
        return tLois;
    }

    public MetierLoiHydraulique[] getToutesLoisSaufTracer() {
        Vector vLois = new Vector();
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        for (int i = 0; i < lois_.length; i++) {
            if (!(lois_[i] instanceof MetierLoiTracer)) vLois.addElement(lois_[i]);
        }
        MetierLoiHydraulique[] tLois = new MetierLoiHydraulique[vLois.size()];
        for (int i = 0; i < tLois.length; i++) {
            tLois[i] = (MetierLoiHydraulique) vLois.elementAt(i);
        }
        return tLois;
    }

    public MetierLoiHydraulique[] getToutesLoisSaufTracerEtGeometrique() {
        MetierLoiHydraulique[] loiSaufGeo = getToutesLoisSaufGeometrique();
        Vector vLois = new Vector();
        if (loiSaufGeo == null) {
            loiSaufGeo = new MetierLoiHydraulique[0];
        }
        for (int i = 0; i < loiSaufGeo.length; i++) {
            if (!(loiSaufGeo[i] instanceof MetierLoiTracer)) vLois.addElement(loiSaufGeo[i]);
        }
        MetierLoiHydraulique[] tLois = new MetierLoiHydraulique[vLois.size()];
        for (int i = 0; i < tLois.length; i++) {
            tLois[i] = (MetierLoiHydraulique) vLois.elementAt(i);
        }
        return tLois;
    }

    public MetierLoiHydrogramme[] getLoisHydrogramme() {
        Vector vLois = new Vector();
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        for (int i = 0; i < lois_.length; i++) {
            if (lois_[i] instanceof MetierLoiHydrogramme) vLois.addElement(lois_[i]);
        }
        MetierLoiHydrogramme[] tLois = new MetierLoiHydrogramme[vLois.size()];
        for (int i = 0; i < tLois.length; i++) {
            tLois[i] = (MetierLoiHydrogramme) vLois.elementAt(i);
        }
        return tLois;
    }

    public MetierLoiLimniHydrogramme[] getLoisLimniHydrogramme() {
        Vector vLois = new Vector();
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        for (int i = 0; i < lois_.length; i++) {
            if (lois_[i] instanceof MetierLoiLimniHydrogramme) vLois.addElement(lois_[i]);
        }
        MetierLoiLimniHydrogramme[] tLois = new MetierLoiLimniHydrogramme[vLois.size()];
        for (int i = 0; i < tLois.length; i++) {
            tLois[i] = (MetierLoiLimniHydrogramme) vLois.elementAt(i);
        }
        return tLois;
    }

    public MetierLoiRegulation[] getLoisRegulation() {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        Vector vLois = new Vector();
        for (int i = 0; i < lois_.length; i++) {
            if (lois_[i] instanceof MetierLoiRegulation) vLois.addElement(lois_[i]);
        }
        MetierLoiRegulation[] tLois = new MetierLoiRegulation[vLois.size()];
        for (int i = 0; i < tLois.length; i++) {
            tLois[i] = (MetierLoiRegulation) vLois.elementAt(i);
        }
        return tLois;
    }

    public MetierLoiGeometrique[] getLoisGeometrique() {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        Vector vLois = new Vector();
        for (int i = 0; i < lois_.length; i++) {
            if (lois_[i] instanceof MetierLoiGeometrique) vLois.addElement(lois_[i]);
        }
        MetierLoiGeometrique[] tLois = new MetierLoiGeometrique[vLois.size()];
        for (int i = 0; i < tLois.length; i++) {
            tLois[i] = (MetierLoiGeometrique) vLois.elementAt(i);
        }
        return tLois;
    }

    public MetierLoiLimnigramme[] getLoisLimnigramme() {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        Vector vLois = new Vector();
        for (int i = 0; i < lois_.length; i++) {
            if (lois_[i] instanceof MetierLoiLimnigramme) vLois.addElement(lois_[i]);
        }
        MetierLoiLimnigramme[] tLois = new MetierLoiLimnigramme[vLois.size()];
        for (int i = 0; i < tLois.length; i++) {
            tLois[i] = (MetierLoiLimnigramme) vLois.elementAt(i);
        }
        return tLois;
    }

    public MetierLoiOuvertureVanne[] getLoisOuvertureVanne() {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        Vector vLois = new Vector();
        for (int i = 0; i < lois_.length; i++) {
            if (lois_[i] instanceof MetierLoiOuvertureVanne) {
                vLois.addElement(lois_[i]);
            }
        }
        MetierLoiOuvertureVanne[] tLois = new MetierLoiOuvertureVanne[vLois.size()];
        for (int i = 0; i < tLois.length; i++) {
            tLois[i] = (MetierLoiOuvertureVanne) vLois.elementAt(i);
        }
        return tLois;
    }

    public MetierLoiTarage[] getLoisTarage() {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        Vector vLois = new Vector();
        for (int i = 0; i < lois_.length; i++) {
            if (lois_[i] instanceof MetierLoiTarage) vLois.addElement(lois_[i]);
        }
        MetierLoiTarage[] tLois = new MetierLoiTarage[vLois.size()];
        for (int i = 0; i < tLois.length; i++) {
            tLois[i] = (MetierLoiTarage) vLois.elementAt(i);
        }
        return tLois;
    }

    public MetierLoiTracer[] getLoisTracer() {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        Vector vLois = new Vector();
        for (int i = 0; i < lois_.length; i++) {
            if (lois_[i] instanceof MetierLoiTracer) vLois.addElement(lois_[i]);
        }
        MetierLoiTracer[] tLois = new MetierLoiTracer[vLois.size()];
        for (int i = 0; i < tLois.length; i++) {
            tLois[i] = (MetierLoiTracer) vLois.elementAt(i);
        }
        return tLois;
    }

    public MetierLoiSeuil[] getLoisSeuil() {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        Vector vLois = new Vector();
        for (int i = 0; i < lois_.length; i++) {
            if (lois_[i] instanceof MetierLoiSeuil) vLois.addElement(lois_[i]);
        }
        MetierLoiSeuil[] tLois = new MetierLoiSeuil[vLois.size()];
        for (int i = 0; i < tLois.length; i++) {
            tLois[i] = (MetierLoiSeuil) vLois.elementAt(i);
        }
        return tLois;
    }

    public double getTempsInitial() {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        double res = Double.POSITIVE_INFINITY;
        boolean found = false;
        for (int i = 0; i < lois_.length; i++) {
            double[] t = new double[0];
            if (lois_[i] instanceof MetierLoiLimniHydrogramme) t = ((MetierLoiLimniHydrogramme) lois_[i]).t(); else if (lois_[i] instanceof MetierLoiHydrogramme) t = ((MetierLoiHydrogramme) lois_[i]).t(); else if (lois_[i] instanceof MetierLoiLimnigramme) t = ((MetierLoiLimnigramme) lois_[i]).t();
            for (int p = 0; p < t.length; p++) {
                if (t[p] < res) {
                    res = t[p];
                    found = true;
                }
            }
        }
        if (!found) res = 1.;
        return res;
    }

    public double getTempsFinal() {
        if (lois_ == null) {
            lois_ = new MetierLoiHydraulique[0];
        }
        double res = Double.NEGATIVE_INFINITY;
        boolean found = false;
        for (int i = 0; i < lois_.length; i++) {
            double[] t = new double[0];
            if (lois_[i] instanceof MetierLoiLimniHydrogramme) t = ((MetierLoiLimniHydrogramme) lois_[i]).t(); else if (lois_[i] instanceof MetierLoiHydrogramme) t = ((MetierLoiHydrogramme) lois_[i]).t(); else if (lois_[i] instanceof MetierLoiLimnigramme) t = ((MetierLoiLimnigramme) lois_[i]).t();
            for (int p = 0; p < t.length; p++) {
                if (t[p] > res) {
                    res = t[p];
                    found = true;
                }
            }
        }
        if (!found) res = -1.;
        return res;
    }

    public boolean verifiePermanent(MetierLoiHydraulique l) {
        if (l instanceof MetierLoiHydrogramme) return ((MetierLoiHydrogramme) l).verifiePermanent();
        return false;
    }

    public boolean verifieTempsNonPermanent(MetierLoiHydraulique l) {
        if (l instanceof MetierLoiHydrogramme) return ((MetierLoiHydrogramme) l).verifieTempsNonPermanent();
        return true;
    }
}
