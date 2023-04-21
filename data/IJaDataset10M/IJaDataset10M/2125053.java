package org.fudaa.dodico.hydraulique1d.metier;

import java.util.Arrays;
import java.util.Vector;
import org.fudaa.dodico.hydraulique1d.metier.MetierBarragePrincipal;
import org.fudaa.dodico.hydraulique1d.metier.MetierBief;
import org.fudaa.dodico.hydraulique1d.metier.MetierCasier;
import org.fudaa.dodico.hydraulique1d.metier.MetierExtremite;
import org.fudaa.dodico.hydraulique1d.metier.MetierHydraulique1d;
import org.fudaa.dodico.hydraulique1d.metier.MetierLiaison;
import org.fudaa.dodico.hydraulique1d.metier.MetierLimite;
import org.fudaa.dodico.hydraulique1d.metier.MetierNoeud;
import org.fudaa.dodico.hydraulique1d.metier.MetierProfil;
import org.fudaa.dodico.hydraulique1d.metier.MetierReseau;
import org.fudaa.dodico.hydraulique1d.metier.MetierResultatsBief;
import org.fudaa.dodico.hydraulique1d.metier.MetierSingularite;
import org.fudaa.dodico.hydraulique1d.metier.MetierZoneFrottement;
import org.fudaa.dodico.hydraulique1d.metier.MetierZonePlanimetrage;
import org.fudaa.dodico.hydraulique1d.metier.SMetierIndiceZoneStockage;
import org.fudaa.dodico.hydraulique1d.metier.evenement.Notifieur;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierApport;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierDeversoir;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierDeversoirComportementZCoefQ;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierPerteCharge;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierSeuil;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierSeuilDenoye;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierSeuilGeometrique;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierSeuilLimniAmont;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierSeuilLoi;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierSeuilNoye;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierSeuilTarageAmont;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierSeuilTarageAval;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierSeuilTranscritique;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierSeuilVanne;
import org.fudaa.dodico.hydraulique1d.metier.singularite.MetierSource;
import org.fudaa.dodico.hydraulique1d.CGlobal;

/**
 * Impl�mentation de l'objet m�tier du "r�seau" hydraulique de l'�tude.
 * G�re les �l�ments du r�seau � savoirs : les biefs, les casiers, les liaisons avec les casiers,
 * ainsi que les noeuds (confluents) et les singularit�s non rattach�s aux biefs.
 * @version      $Revision: 1.2 $ $Date: 2007-11-20 11:42:25 $ by $Author: bmarchan $
 * @author       Axel von Arnim
 */
public class MetierReseau extends MetierHydraulique1d {

    private transient Vector nouveauxNoeud_ = new Vector();

    private transient Vector nouvellesSing_ = new Vector();

    public void initialise(MetierHydraulique1d _o) {
        if (_o instanceof MetierReseau) {
            MetierReseau q = (MetierReseau) _o;
            if (q.biefs() != null) {
                MetierBief[] ip = new MetierBief[q.biefs().length];
                for (int i = 0; i < ip.length; i++) ip[i] = (MetierBief) q.biefs()[i].creeClone();
                biefs(ip);
            }
            if (q.casiers() != null) {
                MetierCasier[] ip = new MetierCasier[q.casiers().length];
                for (int i = 0; i < ip.length; i++) ip[i] = (MetierCasier) q.casiers()[i].creeClone();
                casiers(ip);
            }
            if (q.liaisons() != null) {
                MetierLiaison[] ip = new MetierLiaison[q.liaisons().length];
                for (int i = 0; i < ip.length; i++) ip[i] = (MetierLiaison) q.liaisons()[i].creeClone();
                liaisons(ip);
            }
        }
    }

    public final MetierHydraulique1d creeClone() {
        MetierReseau r = new MetierReseau();
        r.initialise(this);
        return r;
    }

    public final String toString() {
        String s = "r�seau";
        return s;
    }

    public MetierReseau() {
        super();
        biefs_ = new MetierBief[0];
        casiers_ = new MetierCasier[0];
        liaisons_ = new MetierLiaison[0];
        nbPasPlanimetrageImpose_ = false;
        nbPasPlanimetrage_ = 50;
        notifieObjetCree();
    }

    public void dispose() {
        biefs_ = null;
        casiers_ = null;
        casiers_ = null;
        nbPasPlanimetrageImpose_ = false;
        nbPasPlanimetrage_ = 0;
        super.dispose();
    }

    private MetierBief[] biefs_;

    public MetierBief[] biefs() {
        return biefs_;
    }

    public void biefs(MetierBief[] s) {
        if (egale(biefs_, s)) return;
        biefs_ = s;
        trieBiefs();
        initIndiceBief();
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "biefs");
    }

    private MetierCasier[] casiers_;

    public MetierCasier[] casiers() {
        return casiers_;
    }

    public void casiers(MetierCasier[] s) {
        if (egale(casiers_, s)) return;
        casiers_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "casiers");
    }

    private MetierLiaison[] liaisons_;

    public MetierLiaison[] liaisons() {
        return liaisons_;
    }

    public void liaisons(MetierLiaison[] s) {
        if (egale(liaisons_, s)) return;
        liaisons_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "liaisons");
    }

    private boolean nbPasPlanimetrageImpose_;

    public boolean nbPasPlanimetrageImpose() {
        return nbPasPlanimetrageImpose_;
    }

    public void nbPasPlanimetrageImpose(boolean nbPasPlanimetrageImpose) {
        if (nbPasPlanimetrageImpose == nbPasPlanimetrageImpose_) return;
        nbPasPlanimetrageImpose_ = nbPasPlanimetrageImpose;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "nbPasPlanimetrageImpose");
    }

    private int nbPasPlanimetrage_;

    public int nbPasPlanimetrage() {
        return nbPasPlanimetrage_;
    }

    public void nbPasPlanimetrage(int nbPasPlanimetrage) {
        if (nbPasPlanimetrage == nbPasPlanimetrage_) return;
        nbPasPlanimetrage_ = nbPasPlanimetrage;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "nbPasPlanimetrage");
    }

    public int getIndiceCasier(MetierCasier casier) {
        for (int i = 0; i < casiers_.length; i++) {
            if (casier == casiers_[i]) return i;
        }
        return -1;
    }

    public int getIndiceLiaison(MetierLiaison liaison) {
        for (int i = 0; i < liaisons_.length; i++) {
            if (liaison == liaisons_[i]) return i;
        }
        return -1;
    }

    public MetierCasier ajouterCasier() {
        MetierCasier casier = new MetierCasier();
        MetierCasier casiers[] = new MetierCasier[casiers_.length + 1];
        for (int i = 0; i < casiers_.length; i++) casiers[i] = casiers_[i];
        casiers[casiers.length - 1] = casier;
        casiers(casiers);
        initNumeroCasier();
        return casier;
    }

    public void supprimeCasiers(MetierCasier[] _casiers) {
        if (casiers_ == null) return;
        if (casiers_.length == 0) return;
        Vector newcasiers = new Vector();
        boolean casierTrouvee = false;
        for (int i = 0; i < casiers_.length; i++) {
            for (int j = 0; j < _casiers.length; j++) {
                if (casiers_[i] == _casiers[j]) {
                    casiers_[i].supprime();
                    casierTrouvee = true;
                }
                if (casierTrouvee) break;
            }
            if (!casierTrouvee) newcasiers.add(casiers_[i]);
            casierTrouvee = false;
        }
        casiers_ = new MetierCasier[newcasiers.size()];
        for (int i = 0; i < casiers_.length; i++) casiers_[i] = (MetierCasier) newcasiers.get(i);
        initNumeroCasier();
    }

    public MetierLiaison ajouterLiaison() {
        System.err.println("on est dans ajouterLiaison");
        MetierLiaison liaison = new MetierLiaison();
        MetierLiaison liaisons[] = new MetierLiaison[liaisons_.length + 1];
        for (int i = 0; i < liaisons_.length; i++) liaisons[i] = liaisons_[i];
        liaisons[liaisons.length - 1] = liaison;
        liaisons(liaisons);
        initNumeroLiaison();
        return liaison;
    }

    public void supprimeLiaisons(MetierLiaison[] _liaisons) {
        if (liaisons_ == null) return;
        if (liaisons_.length == 0) return;
        Vector newliaisons = new Vector();
        boolean liaisonTrouvee = false;
        for (int i = 0; i < liaisons_.length; i++) {
            for (int j = 0; j < _liaisons.length; j++) {
                if (liaisons_[i] == _liaisons[j]) {
                    liaisons_[i].supprime();
                    liaisonTrouvee = true;
                }
                if (liaisonTrouvee) break;
            }
            if (!liaisonTrouvee) newliaisons.add(liaisons_[i]);
            liaisonTrouvee = false;
        }
        liaisons_ = new MetierLiaison[newliaisons.size()];
        for (int i = 0; i < liaisons_.length; i++) liaisons_[i] = (MetierLiaison) newliaisons.get(i);
        initNumeroLiaison();
    }

    public void initIndiceNumero() {
        initIndiceBief();
        initNumeroCasier();
        initNumeroLiaison();
        initNumeroNoeud();
        initNumeroSing();
    }

    public void initIndiceBief() {
        int numeroProfil = 1;
        for (int i = 0; i < biefs_.length; i++) {
            MetierBief b = biefs_[i];
            b.indice(i);
            MetierProfil[] profs = b.profils();
            if (profs != null) {
                for (int j = 0; j < profs.length; j++) {
                    MetierProfil p = profs[j];
                    p.numero(numeroProfil);
                    numeroProfil++;
                }
            }
        }
    }

    public void initNumeroCasier() {
        for (int i = 0; i < casiers_.length; i++) {
            casiers_[i].numero(i + 1);
        }
    }

    public void initNumeroLiaison() {
        for (int i = 0; i < liaisons_.length; i++) {
            liaisons_[i].numero(i + 1);
        }
    }

    public void initNumeroNoeud() {
        MetierNoeud[] noeudsConnectes = noeudsConnectesBiefs();
        for (int i = 0; i < noeudsConnectes.length; i++) {
            noeudsConnectes[i].numero(i + 1);
        }
        int num = noeudsConnectes.length + 1;
        int nbNouveauxNoeuds = nouveauxNoeud_.size();
        for (int i = 0; i < nbNouveauxNoeuds; i++) {
            MetierNoeud inoeud = (MetierNoeud) nouveauxNoeud_.get(i);
            if (!isContient(noeudsConnectes, inoeud)) {
                inoeud.numero(num);
                num++;
            }
        }
    }

    public void initNumeroSing() {
        MetierSingularite[] singularitesConnectees = singularites();
        for (int i = 0; i < singularitesConnectees.length; i++) {
            singularitesConnectees[i].numero(i + 1);
        }
        int num = singularitesConnectees.length + 1;
        int nbNouvellesSing = nouvellesSing_.size();
        for (int i = 0; i < nbNouvellesSing; i++) {
            MetierSingularite ising = (MetierSingularite) nouvellesSing_.get(i);
            if (!isContient(singularitesConnectees, ising)) {
                ising.numero(num);
                num++;
            }
        }
    }

    public MetierBief creeBief() {
        MetierBief bief = new MetierBief();
        MetierBief biefs[] = new MetierBief[biefs_.length + 1];
        for (int i = 0; i < biefs_.length; i++) biefs[i] = biefs_[i];
        biefs[biefs.length - 1] = bief;
        biefs(biefs);
        initIndiceBief();
        return bief;
    }

    public MetierNoeud creeNoeud() {
        MetierNoeud inoeud = new MetierNoeud();
        nouveauxNoeud_.add(inoeud);
        initNumeroNoeud();
        return inoeud;
    }

    public MetierApport creeApport() {
        MetierApport ising = new MetierApport();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierSource creeSource() {
        MetierSource ising = new MetierSource();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierPerteCharge creePerteCharge() {
        MetierPerteCharge ising = new MetierPerteCharge();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierSeuil creeSeuil() {
        MetierSeuil ising = new MetierSeuilLoi();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierSeuilTranscritique creeSeuilTranscritique() {
        MetierSeuilTranscritique ising = new MetierSeuilTranscritique();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierSeuilNoye creeSeuilNoye() {
        MetierSeuilNoye ising = new MetierSeuilNoye();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierSeuilDenoye creeSeuilDenoye() {
        MetierSeuilDenoye ising = new MetierSeuilDenoye();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierSeuilLimniAmont creeSeuilLimniAmont() {
        MetierSeuilLimniAmont ising = new MetierSeuilLimniAmont();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierSeuilTarageAmont creeSeuilTarageAmont() {
        MetierSeuilTarageAmont ising = new MetierSeuilTarageAmont();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierSeuilTarageAval creeSeuilTarageAval() {
        MetierSeuilTarageAval ising = new MetierSeuilTarageAval();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierSeuilLoi creeSeuilLoi() {
        MetierSeuilLoi ising = new MetierSeuilLoi();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierSeuilGeometrique creeSeuilGeometrique() {
        MetierSeuilGeometrique ising = new MetierSeuilGeometrique();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierSeuilVanne creeSeuilVanne() {
        MetierSeuilVanne ising = new MetierSeuilVanne();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierDeversoir creeDeversoir() {
        MetierDeversoir ising = new MetierDeversoirComportementZCoefQ();
        nouvellesSing_.add(ising);
        initNumeroSing();
        return ising;
    }

    public MetierBarragePrincipal creeBarragePrincipal() {
        return new MetierBarragePrincipal();
    }

    public void creeResultatsReseau() {
        MetierBief[] biefs = biefs();
        for (int i = 0; i < biefs.length; i++) {
            MetierResultatsBief rb = new MetierResultatsBief();
            biefs[i].resultatsBief(rb);
        }
    }

    public void supprimeBiefs(MetierBief[] _biefs) {
        if (biefs_ == null) return;
        if (biefs_.length == 0) return;
        Vector newbiefs = new Vector();
        boolean biefTrouvee = false;
        for (int i = 0; i < biefs_.length; i++) {
            for (int j = 0; j < _biefs.length; j++) {
                if (biefs_[i] == _biefs[j]) {
                    supprimeZonesPlanimAvecBief(biefs_[i]);
                    supprimeZonesFrottementAvecBief(biefs_[i]);
                    biefs_[i].supprime();
                    biefTrouvee = true;
                }
                if (biefTrouvee) break;
            }
            if (!biefTrouvee) newbiefs.add(biefs_[i]);
            biefTrouvee = false;
        }
        biefs_ = new MetierBief[newbiefs.size()];
        for (int i = 0; i < biefs_.length; i++) biefs_[i] = (MetierBief) newbiefs.get(i);
        trieBiefs();
        initIndiceBief();
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "biefs");
    }

    private void supprimeZonesPlanimAvecBief(MetierBief bief) {
        MetierZonePlanimetrage[] zones = bief.zonesPlanimetrage();
        int taille = zones.length;
        for (int i = 0; i < taille; i++) {
            bief.supprimeZonePlanimetrage(zones[i]);
        }
    }

    private void supprimeZonesFrottementAvecBief(MetierBief bief) {
        MetierZoneFrottement[] zones = bief.zonesFrottement();
        int taille = zones.length;
        for (int i = 0; i < taille; i++) {
            bief.supprimeZoneFrottement(zones[i]);
        }
    }

    public void supprimeSingularites(MetierSingularite[] sings) {
        boolean existeSuppression = false;
        for (int i = 0; i < sings.length; i++) {
            boolean supression = supprimeSingularite(sings[i]);
            if (supression && !existeSuppression) {
                existeSuppression = true;
            }
        }
        if (existeSuppression) {
            initNumeroSing();
        }
    }

    private boolean supprimeSingularite(MetierSingularite sing) {
        if (sing == null) return false;
        for (int i = 0; i < biefs_.length; i++) {
            MetierSingularite[] biefsings = biefs_[i].singularites();
            for (int j = 0; j < biefsings.length; j++) {
                if (biefsings[j] == sing) {
                    biefs_[i].supprimeSingularite(biefsings[j]);
                    nouvellesSing_.remove(sing);
                    return true;
                }
            }
        }
        MetierSingularite.supprimeSingularite(sing);
        return nouvellesSing_.remove(sing);
    }

    public void supprimeProfils(MetierProfil[] profils) {
        for (int i = 0; i < biefs_.length; i++) {
            MetierProfil[] biefprofs = biefs_[i].profils();
            for (int j = 0; j < biefprofs.length; j++) {
                for (int k = 0; k < profils.length; k++) {
                    if (biefprofs[j].numero() == profils[k].numero()) {
                        biefs_[i].supprimeProfils(new MetierProfil[] { biefprofs[j] });
                    }
                }
            }
        }
    }

    public void supprimeExtremite(MetierExtremite extremite) {
        if (extremite != null) {
            extremite.supprime();
        }
    }

    public void supprimeNoeud(MetierNoeud noeud) {
        if (noeud != null) {
            MetierNoeud n = null;
            for (int i = 0; i < biefs_.length; i++) {
                MetierBief b = biefs_[i];
                n = b.extrAmont().noeudRattache();
                if (n == noeud) {
                    b.extrAmont().noeudRattache(null);
                }
                n = b.extrAval().noeudRattache();
                if (n == noeud) {
                    b.extrAval().noeudRattache(null);
                }
            }
            nouveauxNoeud_.remove(noeud);
            noeud.supprime();
            initNumeroNoeud();
        }
    }

    public void supprimeLimites(MetierLimite[] limites) {
        for (int i = 0; i < biefs_.length; i++) {
            MetierLimite[] bieflims = biefs_[i].limites();
            for (int j = 0; j < bieflims.length; j++) {
                for (int k = 0; k < limites.length; k++) {
                    if (bieflims[j] == limites[k]) {
                        biefs_[i].supprimeLimite(bieflims[j]);
                    }
                }
            }
        }
    }

    public void supprimeZonesFrottement(MetierZoneFrottement[] zones) {
        for (int i = 0; i < biefs_.length; i++) {
            MetierZoneFrottement[] biefzones = biefs_[i].zonesFrottement();
            for (int j = 0; j < biefzones.length; j++) {
                for (int k = 0; k < zones.length; k++) {
                    if (biefzones[j] == zones[k]) {
                        biefs_[i].supprimeZoneFrottement(biefzones[j]);
                    }
                }
            }
        }
    }

    public void supprimeZonesPlanimetrage(MetierZonePlanimetrage[] zones) {
        for (int i = 0; i < biefs_.length; i++) {
            MetierZonePlanimetrage[] biefzones = biefs_[i].zonesPlanimetrage();
            for (int j = 0; j < biefzones.length; j++) {
                for (int k = 0; k < zones.length; k++) {
                    if (biefzones[j] == zones[k]) {
                        biefs_[i].supprimeZonePlanimetrage(biefzones[j]);
                    }
                }
            }
        }
    }

    public MetierZonePlanimetrage creeZonePlanimetrage(int profilDebut, int profilFin, double taillePas) {
        MetierBief bief = getBiefContenantProfilNumero(profilDebut);
        if (getBiefContenantProfilNumero(profilFin) != bief) {
            System.err.println("DReseau: erreur: zone planimetrage definie sur plus d'un bief!");
            return null;
        }
        MetierZonePlanimetrage zone = bief.creeZonePlanimetrage();
        zone.abscisseDebut(getProfilNumero(profilDebut).abscisse());
        zone.abscisseFin(getProfilNumero(profilFin).abscisse());
        zone.taillePas(taillePas);
        return zone;
    }

    public SMetierIndiceZoneStockage[] getIndicesZonesStockages() {
        Vector vZones = new Vector();
        for (int i = 0; i < biefs_.length; i++) {
            MetierBief b = biefs_[i];
            for (int j = 0; j < b.profils().length; j++) {
                MetierProfil p = b.profils()[j];
                if (p.hasZoneStockage()) {
                    vZones.add(new SMetierIndiceZoneStockage(i, j, p.indiceLitMajDr(), p.indiceLitMajGa()));
                }
            }
        }
        SMetierIndiceZoneStockage[] zones = new SMetierIndiceZoneStockage[vZones.size()];
        for (int i = 0; i < zones.length; i++) {
            zones[i] = (SMetierIndiceZoneStockage) vZones.get(i);
        }
        return zones;
    }

    public void setIndicesZonesStockage(SMetierIndiceZoneStockage[] indicesZonesStockages) {
        for (int i = 0; i < biefs_.length; i++) {
            MetierProfil[] profs = biefs_[i].profilsAvecZonesStockage();
            if (profs != null) {
                for (int j = 0; j < profs.length; j++) {
                    MetierProfil p = profs[j];
                    p.indiceLitMajGa(0);
                    p.indiceLitMajDr(p.points().length - 1);
                }
            }
        }
        for (int i = 0; i < indicesZonesStockages.length; i++) {
            SMetierIndiceZoneStockage z = indicesZonesStockages[i];
            MetierProfil p = biefs_[z.indiceBief].profils()[z.indiceProfil];
            p.indiceLitMajDr(z.indiceLitMajDroit);
            p.indiceLitMajGa(z.indiceLitMajGauche);
        }
    }

    public double getXOrigine() {
        double res = Double.MAX_VALUE;
        for (int i = 0; i < biefs_.length; i++) {
            double rb = biefs_[i].getXOrigine();
            if (rb < res) res = rb;
        }
        return res;
    }

    public double getXFin() {
        double res = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < biefs_.length; i++) {
            double rb = biefs_[i].getXFin();
            if (rb > res) res = rb;
        }
        return res;
    }

    public MetierExtremite[] extremites() {
        Vector extremites = new Vector();
        for (int i = 0; i < biefs_.length; i++) {
            MetierBief b = biefs_[i];
            if (!extremites.contains(b.extrAmont())) extremites.addElement(b.extrAmont());
            if (!extremites.contains(b.extrAval())) extremites.addElement(b.extrAval());
        }
        MetierExtremite[] extremitesNew = new MetierExtremite[extremites.size()];
        for (int i = 0; i < extremitesNew.length; i++) extremitesNew[i] = (MetierExtremite) extremites.get(i);
        return extremitesNew;
    }

    public MetierLimite[] limites() {
        Vector limites = new Vector();
        MetierExtremite[] extremites = extremites();
        for (int i = 0; i < extremites.length; i++) {
            MetierLimite cond = ((MetierExtremite) extremites[i]).conditionLimite();
            if ((cond != null) && (!limites.contains(cond))) limites.add(cond);
        }
        MetierLimite[] res = new MetierLimite[limites.size()];
        for (int i = 0; i < res.length; i++) res[i] = (MetierLimite) limites.get(i);
        return res;
    }

    public MetierExtremite[] extremitesLibres() {
        Vector extremitesLibre = new Vector();
        for (int i = 0; i < biefs_.length; i++) {
            MetierBief b = (MetierBief) biefs_[i];
            if ((b.extrAmont().noeudRattache() == null) && (!extremitesLibre.contains(b.extrAmont()))) {
                extremitesLibre.addElement(b.extrAmont());
            }
            if ((b.extrAval().noeudRattache() == null) && (!extremitesLibre.contains(b.extrAval()))) {
                extremitesLibre.addElement(b.extrAval());
            }
        }
        MetierExtremite[] res = new MetierExtremite[extremitesLibre.size()];
        for (int i = 0; i < res.length; i++) res[i] = (MetierExtremite) extremitesLibre.get(i);
        return res;
    }

    public MetierExtremite getExtremiteNumero(int numero) {
        MetierExtremite res = null;
        for (int i = 0; i < biefs_.length; i++) {
            MetierExtremite e = biefs_[i].extrAmont();
            if (e.numero() == numero) {
                res = e;
                break;
            }
            e = biefs_[i].extrAval();
            if (e.numero() == numero) {
                res = e;
                break;
            }
        }
        return res;
    }

    public MetierExtremite getExtremiteContenantLimite(MetierLimite limite) {
        MetierExtremite res = null;
        for (int i = 0; i < biefs_.length; i++) {
            MetierExtremite e = biefs_[i].extrAmont();
            if (e.conditionLimite() == limite) {
                res = e;
                break;
            }
            e = biefs_[i].extrAval();
            if (e.conditionLimite() == limite) {
                res = e;
                break;
            }
        }
        return res;
    }

    public MetierNoeud[] noeudsConnectesBiefs() {
        Vector noeuds = new Vector();
        MetierNoeud n = null;
        for (int i = 0; i < biefs_.length; i++) {
            MetierBief b = biefs_[i];
            n = b.extrAmont().noeudRattache();
            if ((n != null) && (!noeuds.contains(n))) noeuds.addElement(n);
            n = b.extrAval().noeudRattache();
            if ((n != null) && (!noeuds.contains(n))) noeuds.addElement(n);
        }
        MetierNoeud[] res = new MetierNoeud[noeuds.size()];
        for (int i = 0; i < res.length; i++) res[i] = (MetierNoeud) noeuds.get(i);
        return res;
    }

    public MetierNoeud getNoeudNumero(int numero) {
        MetierNoeud[] noeuds = noeudsConnectesBiefs();
        for (int i = 0; i < noeuds.length; i++) {
            if (noeuds[i].numero() == numero) return noeuds[i];
        }
        return null;
    }

    public int getNumeroApparitionProfil(MetierProfil profil) {
        int numero = 1;
        for (int i = 0; i < biefs_.length; i++) {
            for (int j = 0; j < biefs_[i].profils().length; j++) {
                if (biefs_[i].profils()[j] == profil) {
                    return numero;
                }
                numero++;
            }
        }
        return 0;
    }

    public MetierProfil[] profils() {
        int nb = nbProfils();
        MetierProfil[] res = new MetierProfil[nb];
        int nbPrec = 0;
        for (int b = 0; b < biefs_.length; b++) {
            MetierProfil[] ps = biefs_[b].profils();
            for (int i = 0; i < ps.length; i++) res[nbPrec + i] = ps[i];
            nbPrec += ps.length;
        }
        return res;
    }

    public MetierSingularite[] singularites() {
        Vector resV = new Vector();
        for (int b = 0; b < biefs_.length; b++) {
            MetierSingularite[] ps = biefs_[b].singularites();
            for (int i = 0; i < ps.length; i++) resV.add(ps[i]);
        }
        return (MetierSingularite[]) resV.toArray(new MetierSingularite[0]);
    }

    public MetierSingularite getSingulariteId(int id) {
        MetierSingularite[] singularites = singularites();
        for (int i = 0; i < singularites.length; i++) {
            if (singularites[i].id() == id) return singularites[i];
        }
        return null;
    }

    public MetierZonePlanimetrage[] zonesPlanimetrage() {
        Vector resV = new Vector();
        for (int b = 0; b < biefs_.length; b++) {
            MetierZonePlanimetrage[] ps = biefs_[b].zonesPlanimetrage();
            if (ps != null) {
                for (int i = 0; i < ps.length; i++) resV.add(ps[i]);
            }
        }
        MetierZonePlanimetrage[] res = new MetierZonePlanimetrage[resV.size()];
        for (int i = 0; i < res.length; i++) res[i] = (MetierZonePlanimetrage) resV.get(i);
        return res;
    }

    public MetierZoneFrottement[] zonesFrottement() {
        Vector resV = new Vector();
        for (int b = 0; b < biefs_.length; b++) {
            MetierZoneFrottement[] ps = biefs_[b].zonesFrottement();
            for (int i = 0; i < ps.length; i++) resV.add(ps[i]);
        }
        MetierZoneFrottement[] res = new MetierZoneFrottement[resV.size()];
        for (int i = 0; i < res.length; i++) res[i] = (MetierZoneFrottement) resV.get(i);
        return res;
    }

    public MetierProfil getProfilNumero(int numero) {
        MetierProfil[] profils = profils();
        for (int i = 0; i < profils.length; i++) {
            if (profils[i].numero() == numero) return profils[i];
        }
        return null;
    }

    public MetierProfil getProfilAbscisse(double abscisseProfil) {
        MetierProfil[] profils = profils();
        for (int i = 0; i < profils.length; i++) {
            if (CGlobal.egale(profils[i].abscisse(), abscisseProfil)) return profils[i];
        }
        return null;
    }

    public int getIndiceProfilNumero(int numero) {
        MetierProfil[] profils = profils();
        for (int i = 0; i < profils.length; i++) {
            if (profils[i].numero() == numero) return i;
        }
        return -1;
    }

    public int getIndiceProfilAbscisse(double abscisseProfil) {
        MetierProfil[] profils = profils();
        for (int i = 0; i < profils.length; i++) {
            if (CGlobal.egale(profils[i].abscisse(), abscisseProfil)) return i;
        }
        return -1;
    }

    public int getIndiceBief(MetierBief bief) {
        for (int i = 0; i < biefs_.length; i++) {
            if (bief == biefs_[i]) return i;
        }
        return -1;
    }

    public boolean hasSingularites() {
        boolean res = false;
        for (int i = 0; i < biefs_.length; i++) {
            res = res || (biefs_[i].seuils().length > 0);
            if (res) break;
        }
        return res;
    }

    public boolean hasZonesStockage() {
        boolean res = false;
        for (int i = 0; i < biefs_.length; i++) {
            res = res || (biefs_[i].profilsAvecZonesStockage().length > 0);
            if (res) break;
        }
        return res;
    }

    public MetierBief getBiefContenantProfilNumero(int numeroProfil) {
        for (int i = 0; i < biefs_.length; i++) {
            if (biefs_[i].contientProfilNumero(numeroProfil)) return biefs_[i];
        }
        return null;
    }

    public MetierBief getBiefContenantProfilAbscisse(double abscisseProfil) {
        for (int i = 0; i < biefs_.length; i++) {
            if (biefs_[i].contientProfilAbscisse(abscisseProfil)) return biefs_[i];
        }
        return null;
    }

    public MetierBief getBiefContenantAbscisse(double abscisse) {
        for (int i = 0; i < biefs_.length; i++) {
            if (biefs_[i].contientAbscisse(abscisse)) return biefs_[i];
        }
        return null;
    }

    public MetierBief getBiefContenantSingularite(MetierSingularite sing) {
        for (int i = 0; i < biefs_.length; i++) {
            if (biefs_[i].contientSingularite(sing)) return biefs_[i];
        }
        return null;
    }

    public MetierZoneFrottement getFrottementContenantProfilNumero(int numeroProfil) {
        MetierBief bief = getBiefContenantProfilNumero(numeroProfil);
        if (bief == null) return null;
        MetierZoneFrottement[] zones = bief.zonesFrottement();
        for (int i = 0; i < zones.length; i++) {
            if (zones[i].contientAbscisse(bief.profils()[bief.getIndiceProfilNumero(numeroProfil)].abscisse())) return zones[i];
        }
        return null;
    }

    public int nbProfils() {
        int nb = 0;
        for (int i = 0; i < biefs_.length; i++) {
            nb += biefs_[i].profils().length;
        }
        return nb;
    }

    public MetierSeuil[] seuils() {
        MetierSeuil[] s = new MetierSeuil[0];
        for (int i = 0; i < biefs_.length; i++) {
            MetierSeuil[] sb = biefs_[i].seuils();
            MetierSeuil[] tmp = s;
            s = new MetierSeuil[tmp.length + sb.length];
            for (int j = 0; j < tmp.length; j++) s[j] = tmp[j];
            for (int j = 0; j < sb.length; j++) s[tmp.length + j] = sb[j];
        }
        return s;
    }

    public MetierPerteCharge[] pertesCharges() {
        MetierPerteCharge[] s = new MetierPerteCharge[0];
        for (int i = 0; i < biefs_.length; i++) {
            MetierPerteCharge[] sb = biefs_[i].pertesCharges();
            MetierPerteCharge[] tmp = s;
            s = new MetierPerteCharge[tmp.length + sb.length];
            for (int j = 0; j < tmp.length; j++) s[j] = tmp[j];
            for (int j = 0; j < sb.length; j++) s[tmp.length + j] = sb[j];
        }
        return s;
    }

    public MetierApport[] apports() {
        MetierApport[] s = new MetierApport[0];
        for (int i = 0; i < biefs_.length; i++) {
            MetierApport[] sb = biefs_[i].apports();
            MetierApport[] tmp = s;
            s = new MetierApport[tmp.length + sb.length];
            for (int j = 0; j < tmp.length; j++) s[j] = tmp[j];
            for (int j = 0; j < sb.length; j++) s[tmp.length + j] = sb[j];
        }
        return s;
    }

    public MetierSource[] sources() {
        MetierSource[] s = new MetierSource[0];
        for (int i = 0; i < biefs_.length; i++) {
            MetierSource[] sb = biefs_[i].sources();
            MetierSource[] tmp = s;
            s = new MetierSource[tmp.length + sb.length];
            for (int j = 0; j < tmp.length; j++) s[j] = tmp[j];
            for (int j = 0; j < sb.length; j++) s[tmp.length + j] = sb[j];
        }
        return s;
    }

    public MetierDeversoir[] deversoirs() {
        MetierDeversoir[] s = new MetierDeversoir[0];
        for (int i = 0; i < biefs_.length; i++) {
            MetierDeversoir[] sb = biefs_[i].deversoirs();
            MetierDeversoir[] tmp = s;
            s = new MetierDeversoir[tmp.length + sb.length];
            for (int j = 0; j < tmp.length; j++) s[j] = tmp[j];
            for (int j = 0; j < sb.length; j++) s[tmp.length + j] = sb[j];
        }
        return s;
    }

    public int nbPasTempsResultats() {
        int nb = 0;
        for (int i = 0; i < biefs_.length; i++) {
            if (biefs_[i].resultatsBief() != null) nb = Math.max(nb, biefs_[i].resultatsBief().pasTemps().length);
        }
        return nb;
    }

    public MetierBief getBiefNumero(int numeroBief) {
        for (int i = 0; i < biefs_.length; i++) {
            if (biefs_[i].numero() == numeroBief) return biefs_[i];
        }
        return null;
    }

    /**
   * Retoune l'estimation du nombre de planim�trage.
   * @return Le max de l'estimation de chaque bief et ne pouvant d�passer 250
   */
    public int getNbPasPlanimetrage() {
        int nbPas = 0;
        for (int i = 0; i < biefs_.length; i++) {
            nbPas = Math.max(nbPas, biefs_[i].getNbPasPlanimetrage());
        }
        return Math.min(nbPas, 250);
    }

    public int getNbPasPlanimetrage(MetierZonePlanimetrage[] zonesPlanimetrage) {
        int nbPas = 0;
        for (int i = 0; i < biefs_.length; i++) {
            nbPas = Math.max(nbPas, biefs_[i].getNbPasPlanimetrage(zonesPlanimetrage));
        }
        return Math.min(nbPas, 250);
    }

    public MetierSingularite[] getSingularitesHorsBief() {
        MetierSingularite[] sings = singularites();
        Vector resV = new Vector();
        for (int i = 0; i < sings.length; i++) {
            MetierBief bief = getBiefContenantAbscisse(sings[i].abscisse());
            if (bief == null) {
                System.err.println("Singularit� " + sings[i].numero() + " hors-bief");
                resV.add(sings[i]);
            }
        }
        MetierSingularite[] res = new MetierSingularite[resV.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = (MetierSingularite) resV.get(i);
        }
        return res;
    }

    public double interpoleFond(double abscisse) {
        MetierBief bief = getBiefContenantAbscisse(abscisse);
        return bief.interpoleFond(abscisse);
    }

    /**
   * @todo
   * @param numeroBief int
   * @param abscisseRelative double
   * @param distanceEntreBief double
   * @return double
   */
    public double getAbscisseAbsolue(int numeroBief, double abscisseRelative, double distanceEntreBief) {
        System.out.println("numeroBief=" + numeroBief);
        System.out.println("abscisseRelative=" + abscisseRelative);
        int indice = numeroBief - 1;
        double abscisseAbsolueDebut = 0;
        double abscisseRelativeDebut = Double.NaN;
        for (int i = 0; i < biefs_.length; i++) {
            if (indice == biefs_[i].indice()) {
                abscisseRelativeDebut = biefs_[i].getXOrigine();
                break;
            }
            abscisseAbsolueDebut = abscisseAbsolueDebut + biefs_[i].getLongueur() + distanceEntreBief;
        }
        System.out.println("abscisseAbsolueDebut=" + abscisseAbsolueDebut);
        System.out.println("abscisseRelativeDebut=" + abscisseRelativeDebut);
        return abscisseRelative - abscisseRelativeDebut + abscisseAbsolueDebut;
    }

    /**
   * Retourne l'abscisse relative � partir de l'abscisse absolue.
   * @param numeroBief int
   * @param abscisseAbsolue double
   * @param abscisseAbsolueDebutBief double
   * @return double
   */
    public double getAbscisseRelative(int numeroBief, double abscisseAbsolue, double abscisseAbsolueDebutBief) {
        int indice = numeroBief - 1;
        double abscisseRelativeDebut = Double.NaN;
        for (int i = 0; i < biefs_.length; i++) {
            if (indice == biefs_[i].indice()) {
                abscisseRelativeDebut = biefs_[i].getXOrigine();
            }
        }
        return abscisseAbsolue - abscisseAbsolueDebutBief + abscisseRelativeDebut;
    }

    private void trieBiefs() {
        if (biefs_ == null) return;
        Arrays.sort(biefs_, MetierBief.COMPARATOR);
    }

    private static final boolean isContient(Object[] objets, Object objet) {
        for (int i = 0; i < objets.length; i++) {
            if (objets[i] == objet) {
                return true;
            }
        }
        return false;
    }
}
