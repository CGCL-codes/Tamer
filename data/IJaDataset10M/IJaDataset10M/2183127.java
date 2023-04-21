package org.fudaa.dodico.hydraulique1d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import org.fudaa.dodico.corba.geometrie.SPoint2D;
import org.fudaa.dodico.corba.hydraulique1d.IBief;
import org.fudaa.dodico.corba.hydraulique1d.IBiefOperations;
import org.fudaa.dodico.corba.hydraulique1d.IExtremite;
import org.fudaa.dodico.corba.hydraulique1d.ILimite;
import org.fudaa.dodico.corba.hydraulique1d.IProfil;
import org.fudaa.dodico.corba.hydraulique1d.IResultatsBief;
import org.fudaa.dodico.corba.hydraulique1d.ISectionCalculee;
import org.fudaa.dodico.corba.hydraulique1d.ISingularite;
import org.fudaa.dodico.corba.hydraulique1d.IZoneFrottement;
import org.fudaa.dodico.corba.hydraulique1d.IZonePlanimetrage;
import org.fudaa.dodico.corba.hydraulique1d.singularite.IApport;
import org.fudaa.dodico.corba.hydraulique1d.singularite.ISource;
import org.fudaa.dodico.corba.hydraulique1d.singularite.IBarrage;
import org.fudaa.dodico.corba.hydraulique1d.singularite.IDeversoir;
import org.fudaa.dodico.corba.hydraulique1d.singularite.IDeversoirComportementLoi;
import org.fudaa.dodico.corba.hydraulique1d.singularite.IDeversoirComportementZCoefQ;
import org.fudaa.dodico.corba.hydraulique1d.singularite.IPerteCharge;
import org.fudaa.dodico.corba.hydraulique1d.singularite.ISeuil;
import org.fudaa.dodico.corba.hydraulique1d.singularite.ISeuilDenoye;
import org.fudaa.dodico.corba.hydraulique1d.singularite.ISeuilGeometrique;
import org.fudaa.dodico.corba.hydraulique1d.singularite.ISeuilLimniAmont;
import org.fudaa.dodico.corba.hydraulique1d.singularite.ISeuilLoi;
import org.fudaa.dodico.corba.hydraulique1d.singularite.ISeuilNoye;
import org.fudaa.dodico.corba.hydraulique1d.singularite.ISeuilTarageAmont;
import org.fudaa.dodico.corba.hydraulique1d.singularite.ISeuilTarageAval;
import org.fudaa.dodico.corba.objet.IObjet;
import org.fudaa.dodico.objet.UsineLib;

/**
 * Impl�mentation de l'objet m�tier "bief" (�l�ment du r�seau hydraulique).
 * Contient 2 extremit�s (amont et aval), un tableau de profils.
 * On peut y attacher des singularit�s.
 * On associe les zones de frottements et de planim�trage.
 * Les r�sultats biefs et les sections calcul�es ne sont pas utilis�s dans Fudaa-Mascaret.
 *
 * @version      $Revision: 1.26 $ $Date: 2006-12-12 08:55:32 $ by $Author: opasteur $
 * @author       Axel von Arnim
 */
public class DBief extends DHydraulique1d implements IBief, IBiefOperations {

    public static final BiefComparator COMPARATOR = new BiefComparator();

    public void initialise(IObjet _o) {
        if (_o instanceof IBief) {
            IBief q = (IBief) _o;
            if (q.profils() != null) {
                IProfil[] ip = new IProfil[q.profils().length];
                for (int i = 0; i < ip.length; i++) ip[i] = (IProfil) q.profils()[i].creeClone();
                profils(ip);
            }
            if (q.zonesFrottement() != null) {
                IZoneFrottement[] izf = new IZoneFrottement[q.zonesFrottement().length];
                for (int i = 0; i < izf.length; i++) izf[i] = (IZoneFrottement) q.zonesFrottement()[i].creeClone();
                zonesFrottement(izf);
            }
            if (q.zonesPlanimetrage() != null) {
                IZonePlanimetrage[] izp = new IZonePlanimetrage[q.zonesPlanimetrage().length];
                for (int i = 0; i < izp.length; i++) izp[i] = (IZonePlanimetrage) q.zonesPlanimetrage()[i].creeClone();
                zonesPlanimetrage(izp);
            }
            if (q.sectionsCalculees() != null) {
                ISectionCalculee[] is = new ISectionCalculee[q.sectionsCalculees().length];
                for (int i = 0; i < is.length; i++) is[i] = (ISectionCalculee) q.sectionsCalculees()[i].creeClone();
                sectionsCalculees(is);
            }
        }
    }

    public final IObjet creeClone() {
        IBief p = UsineLib.findUsine().creeHydraulique1dBief();
        p.initialise(tie());
        return p;
    }

    public final String toString() {
        String s = "bief " + (indice_ + 1);
        return s;
    }

    public String[] getInfos() {
        String[] res = new String[2];
        res[0] = "Bief" + (indice_ + 1);
        res[1] = "Abscisse d�but ";
        try {
            res[1] = res[1] + extrAmont_.profilRattache().abscisse();
        } catch (NullPointerException e) {
            res[1] = res[1] + "inconnu";
        }
        res[1] = res[1] + " Abscisse fin ";
        try {
            res[1] = res[1] + extrAval_.profilRattache().abscisse();
        } catch (NullPointerException e) {
            res[1] = res[1] + "inconnu";
        }
        return res;
    }

    public DBief() {
        super();
        numero_ = Identifieur.IDENTIFIEUR.identificateurLibre(getClass().getName());
        indice_ = 0;
        profils_ = new IProfil[0];
        extrAmont_ = UsineLib.findUsine().creeHydraulique1dExtremite();
        extrAval_ = UsineLib.findUsine().creeHydraulique1dExtremite();
        zonesFrottement_ = new IZoneFrottement[0];
        zonesPlanimetrage_ = new IZonePlanimetrage[0];
        singularites_ = new ISingularite[0];
        sectionsCalculees_ = new ISectionCalculee[0];
        resultatsBief_ = null;
    }

    public void dispose() {
        indice_ = -1;
        profils_ = null;
        extrAmont_ = null;
        extrAval_ = null;
        zonesFrottement_ = null;
        zonesPlanimetrage_ = null;
        singularites_ = null;
        sectionsCalculees_ = null;
        resultatsBief_ = null;
    }

    private int indice_ = -1;

    public int indice() {
        return indice_;
    }

    public void indice(int s) {
        if (indice_ == s) return;
        indice_ = s;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "indice");
        if (extrAmont_ != null) {
            extrAmont_.numero(2 * indice_ + 1);
        }
        if (extrAval_ != null) {
            extrAval_.numero(2 * indice_ + 2);
        }
    }

    private int numero_;

    public int numero() {
        return numero_;
    }

    public void numero(int s) {
        if (numero_ == s) return;
        numero_ = s;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "numero");
    }

    private IProfil[] profils_;

    public IProfil[] profils() {
        return profils_;
    }

    public void profils(IProfil[] s) {
        profils(s, true);
    }

    public void profils(IProfil[] s, boolean _trie) {
        if (egale(profils_, s)) {
            if (_trie) trieProfils();
            miseAJourExtremite();
        } else {
            profils_ = s;
            if (_trie) trieProfils();
            miseAJourExtremite();
            UsineLib.findUsine().fireObjetModifie(toString(), tie(), "profils");
        }
    }

    private IExtremite extrAval_;

    public IExtremite extrAval() {
        return extrAval_;
    }

    public void extrAval(IExtremite s) {
        if (extrAval_ == s) return;
        extrAval_ = s;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "extrAval");
    }

    private IExtremite extrAmont_;

    public IExtremite extrAmont() {
        return extrAmont_;
    }

    public void extrAmont(IExtremite s) {
        if (extrAmont_ == s) return;
        extrAmont_ = s;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "extrAmont");
    }

    private IZoneFrottement[] zonesFrottement_;

    public IZoneFrottement[] zonesFrottement() {
        return zonesFrottement_;
    }

    public void zonesFrottement(IZoneFrottement[] s) {
        if (egale(zonesFrottement_, s)) return;
        zonesFrottement_ = s;
        trieZonesFrottement();
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "zonesFrottement");
    }

    private IZonePlanimetrage[] zonesPlanimetrage_;

    public IZonePlanimetrage[] zonesPlanimetrage() {
        return zonesPlanimetrage_;
    }

    public void zonesPlanimetrage(IZonePlanimetrage[] s) {
        if (egale(zonesPlanimetrage_, s)) return;
        zonesPlanimetrage_ = s;
        trieZonesPlanimetrage();
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "zonesPlanimetrage");
    }

    private ISingularite[] singularites_;

    public ISingularite[] singularites() {
        return singularites_;
    }

    public void singularites(ISingularite[] s) {
        if (egale(singularites_, s)) return;
        singularites_ = s;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "singularites");
    }

    private ISectionCalculee[] sectionsCalculees_;

    public ISectionCalculee[] sectionsCalculees() {
        return sectionsCalculees_;
    }

    public void sectionsCalculees(ISectionCalculee[] s) {
        if (egale(sectionsCalculees_, s)) return;
        sectionsCalculees_ = s;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "sectionsCalculees");
    }

    private IResultatsBief resultatsBief_;

    public IResultatsBief resultatsBief() {
        return resultatsBief_;
    }

    public void resultatsBief(IResultatsBief s) {
        if (resultatsBief_ == s) return;
        resultatsBief_ = s;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "resultatsBief");
    }

    public IApport creeApport() {
        IApport sing = UsineLib.findUsine().creeHydraulique1dApport();
        ajouteSingularite(sing);
        return sing;
    }

    public ISource creeSource() {
        ISource sing = UsineLib.findUsine().creeHydraulique1dSource();
        ajouteSingularite(sing);
        return sing;
    }

    public IPerteCharge creePerteCharge() {
        IPerteCharge sing = UsineLib.findUsine().creeHydraulique1dPerteCharge();
        ajouteSingularite(sing);
        return sing;
    }

    public ISeuilNoye creeSeuilNoye() {
        ISeuilNoye sing = UsineLib.findUsine().creeHydraulique1dSeuilNoye();
        ajouteSingularite(sing);
        return sing;
    }

    public ISeuilDenoye creeSeuilDenoye() {
        ISeuilDenoye sing = UsineLib.findUsine().creeHydraulique1dSeuilDenoye();
        ajouteSingularite(sing);
        return sing;
    }

    public ISeuilGeometrique creeSeuilGeometrique() {
        ISeuilGeometrique sing = UsineLib.findUsine().creeHydraulique1dSeuilGeometrique();
        ajouteSingularite(sing);
        return sing;
    }

    public ISeuilTarageAmont creeSeuilTarageAmont() {
        ISeuilTarageAmont sing = UsineLib.findUsine().creeHydraulique1dSeuilTarageAmont();
        ajouteSingularite(sing);
        return sing;
    }

    public ISeuilTarageAval creeSeuilTarageAval() {
        ISeuilTarageAval sing = UsineLib.findUsine().creeHydraulique1dSeuilTarageAval();
        ajouteSingularite(sing);
        return sing;
    }

    public ISeuilLimniAmont creeSeuilLimniAmont() {
        ISeuilLimniAmont sing = UsineLib.findUsine().creeHydraulique1dSeuilLimniAmont();
        ajouteSingularite(sing);
        return sing;
    }

    public void supprimeSingularite(ISingularite sing) {
        if (singularites_ == null) return;
        Vector newsings = new Vector();
        for (int i = 0; i < singularites_.length; i++) {
            if (singularites_[i] == sing) {
                DSingularite.supprimeSingularite(singularites_[i]);
            } else newsings.add(singularites_[i]);
        }
        singularites_ = new ISingularite[newsings.size()];
        for (int i = 0; i < singularites_.length; i++) singularites_[i] = (ISingularite) newsings.get(i);
    }

    public void ajouteSingularite(ISingularite sing) {
        if (sing == null) return;
        List listSing = new ArrayList(Arrays.asList(singularites_));
        if (listSing.contains(sing)) return;
        listSing.add(sing);
        singularites_ = (ISingularite[]) listSing.toArray(new ISingularite[0]);
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "singularites");
    }

    public IProfil dupliqueProfil(IProfil _profil, int nouvPos) {
        IProfil profil = (IProfil) _profil.creeClone();
        IProfil[] profils = new IProfil[profils_.length + 1];
        for (int i = 0; i < nouvPos; i++) profils[i] = profils_[i];
        profils[nouvPos] = profil;
        for (int i = nouvPos + 1; i < profils.length; i++) profils[i] = profils_[i - 1];
        profils(profils);
        return profil;
    }

    public IProfil creeProfil() {
        IProfil profil = UsineLib.findUsine().creeHydraulique1dProfil();
        IProfil profils[] = new IProfil[profils_.length + 1];
        for (int i = 0; i < profils_.length; i++) profils[i] = profils_[i];
        profils[profils.length - 1] = profil;
        profils(profils, false);
        return profil;
    }

    public void supprimeProfils(IProfil[] profils) {
        if (profils_ == null) return;
        Vector newprofs = new Vector();
        boolean profilTrouvee = false;
        for (int i = 0; i < profils_.length; i++) {
            for (int j = 0; j < profils.length; j++) {
                if (profils_[i] == profils[j]) {
                    UsineLib.findUsine().supprimeHydraulique1dProfil((IProfil) profils_[i]);
                    profilTrouvee = true;
                }
                if (profilTrouvee) break;
            }
            if (!profilTrouvee) newprofs.add(profils_[i]);
            profilTrouvee = false;
        }
        IProfil[] nprofils = new IProfil[newprofs.size()];
        for (int i = 0; i < nprofils.length; i++) nprofils[i] = (IProfil) newprofs.get(i);
        profils(nprofils);
    }

    public void miseAZeroProfils(IProfil[] profils) {
        if (profils == null) return;
        for (int i = 0; i < profils.length; i++) profils[i].abscisse(0.);
    }

    public void decalageAbscisseProfils(IProfil[] profils, double offset) {
        if (profils == null) return;
        for (int i = 0; i < profils.length; i++) profils[i].abscisse(profils[i].abscisse() + offset);
    }

    public boolean normaliseProfils(IProfil[] profils) {
        if (profils == null) return false;
        boolean res = false;
        System.err.println("normalisation des profils");
        for (int i = 0; i < profils.length; i++) {
            SPoint2D[] points = profils[i].points();
            if (points.length > 0) {
                if (points[0].y < points[points.length - 1].y) {
                    SPoint2D[] tmpPoints = new SPoint2D[points.length + 1];
                    tmpPoints[0] = new SPoint2D();
                    tmpPoints[0].y = points[points.length - 1].y;
                    tmpPoints[0].x = points[0].x;
                    for (int j = 0; j < points.length; j++) {
                        tmpPoints[j + 1] = new SPoint2D();
                        tmpPoints[j + 1].y = points[j].y;
                        tmpPoints[j + 1].x = points[j].x;
                    }
                    profils[i].points(tmpPoints);
                    res = res || true;
                } else if (points[0].y > points[points.length - 1].y) {
                    SPoint2D[] tmpPoints = new SPoint2D[points.length + 1];
                    for (int j = 0; j < points.length; j++) {
                        tmpPoints[j].y = points[j].y;
                        tmpPoints[j].x = points[j].x;
                    }
                    tmpPoints[points.length].y = points[0].y;
                    tmpPoints[points.length].x = points[points.length - 1].x;
                    profils[i].points(tmpPoints);
                    res = res || true;
                }
            }
        }
        return res;
    }

    public void supprimeLimite(ILimite limite) {
        if (extrAmont_.conditionLimite() == limite) {
            extrAmont_.conditionLimite(null);
            UsineLib.findUsine().supprimeHydraulique1dLimite(limite);
        }
    }

    public IZoneFrottement creeZoneFrottement() {
        IZoneFrottement zone = UsineLib.findUsine().creeHydraulique1dZoneFrottement();
        if (extrAmont().profilRattache() != null) {
            zone.abscisseDebut(extrAmont().profilRattache().abscisse());
        }
        if (extrAval().profilRattache() != null) {
            zone.abscisseFin(extrAval().profilRattache().abscisse());
        }
        zone.biefRattache(this);
        IZoneFrottement zones[] = new IZoneFrottement[zonesFrottement_.length + 1];
        for (int i = 0; i < zonesFrottement_.length; i++) zones[i] = zonesFrottement_[i];
        zones[zones.length - 1] = zone;
        zonesFrottement(zones);
        return zone;
    }

    public void supprimeZoneFrottement(IZoneFrottement zone) {
        if (zonesFrottement_ == null) return;
        Vector newfrots = new Vector();
        for (int i = 0; i < zonesFrottement_.length; i++) {
            if (zonesFrottement_[i] == zone) {
                UsineLib.findUsine().supprimeHydraulique1dZoneFrottement(zonesFrottement_[i]);
            } else newfrots.add(zonesFrottement_[i]);
        }
        IZoneFrottement[] zonesFrottement = new IZoneFrottement[newfrots.size()];
        for (int i = 0; i < zonesFrottement.length; i++) zonesFrottement[i] = (IZoneFrottement) newfrots.get(i);
        zonesFrottement(zonesFrottement);
    }

    public IZonePlanimetrage creeZonePlanimetrage() {
        IZonePlanimetrage zone = UsineLib.findUsine().creeHydraulique1dZonePlanimetrage();
        if (extrAmont() != null) {
            if (extrAmont().profilRattache() != null) {
                zone.abscisseDebut(extrAmont().profilRattache().abscisse());
            }
        }
        if (extrAval() != null) {
            if (extrAval().profilRattache() != null) {
                zone.abscisseFin(extrAval().profilRattache().abscisse());
            }
        }
        zone.biefRattache(this);
        IZonePlanimetrage zones[] = new IZonePlanimetrage[zonesPlanimetrage_.length + 1];
        for (int i = 0; i < zonesPlanimetrage_.length; i++) zones[i] = zonesPlanimetrage_[i];
        zones[zonesPlanimetrage_.length] = zone;
        zonesPlanimetrage(zones);
        return zone;
    }

    public void supprimeZonePlanimetrage(IZonePlanimetrage zone) {
        if (zonesPlanimetrage_ == null) return;
        Vector newplani = new Vector();
        for (int i = 0; i < zonesPlanimetrage_.length; i++) {
            if (zonesPlanimetrage_[i] == zone) {
                UsineLib.findUsine().supprimeHydraulique1dZonePlanimetrage(zonesPlanimetrage_[i]);
            } else newplani.add(zonesPlanimetrage_[i]);
        }
        IZonePlanimetrage[] zonesPlanimetrage = new IZonePlanimetrage[newplani.size()];
        for (int i = 0; i < zonesPlanimetrage.length; i++) zonesPlanimetrage[i] = (IZonePlanimetrage) newplani.get(i);
        zonesPlanimetrage(zonesPlanimetrage);
    }

    public double getXOrigine() {
        double res = Double.MAX_VALUE;
        for (int i = 0; i < profils_.length; i++) {
            double rb = profils_[i].abscisse();
            if (rb < res) res = rb;
        }
        return res;
    }

    public double getXFin() {
        double res = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < profils_.length; i++) {
            double rb = profils_[i].abscisse();
            if (rb > res) res = rb;
        }
        return res;
    }

    public ISeuil[] seuils() {
        if (singularites_ == null) return new ISeuil[0];
        Vector seuils = new Vector();
        for (int i = 0; i < singularites_.length; i++) {
            ISingularite s = singularites_[i];
            if (s instanceof ISeuil) seuils.addElement(s);
        }
        ISeuil[] res = new ISeuil[seuils.size()];
        for (int i = 0; i < res.length; i++) res[i] = (ISeuil) seuils.get(i);
        return res;
    }

    public IPerteCharge[] pertesCharges() {
        if (singularites_ == null) return new IPerteCharge[0];
        Vector pertesCharges = new Vector();
        for (int i = 0; i < singularites_.length; i++) {
            ISingularite s = singularites_[i];
            if (s instanceof IPerteCharge) pertesCharges.addElement(s);
        }
        IPerteCharge[] res = new IPerteCharge[pertesCharges.size()];
        for (int i = 0; i < res.length; i++) res[i] = (IPerteCharge) pertesCharges.get(i);
        return res;
    }

    public IApport[] apports() {
        if (singularites_ == null) return new IApport[0];
        Vector apports = new Vector();
        for (int i = 0; i < singularites_.length; i++) {
            ISingularite s = singularites_[i];
            if (s instanceof IApport) apports.addElement(s);
        }
        IApport[] res = new IApport[apports.size()];
        for (int i = 0; i < res.length; i++) res[i] = (IApport) apports.get(i);
        return res;
    }

    public ISource[] sources() {
        if (singularites_ == null) return new ISource[0];
        Vector sources = new Vector();
        for (int i = 0; i < singularites_.length; i++) {
            ISingularite s = singularites_[i];
            if (s instanceof ISource) sources.addElement(s);
        }
        ISource[] res = new ISource[sources.size()];
        for (int i = 0; i < res.length; i++) res[i] = (ISource) sources.get(i);
        return res;
    }

    public IDeversoir[] deversoirs() {
        if (singularites_ == null) return new IDeversoir[0];
        Vector deversoirs = new Vector();
        for (int i = 0; i < singularites_.length; i++) {
            ISingularite s = singularites_[i];
            if (s instanceof IDeversoir) deversoirs.addElement(s);
        }
        IDeversoir[] res = new IDeversoir[deversoirs.size()];
        for (int i = 0; i < res.length; i++) res[i] = (IDeversoir) deversoirs.get(i);
        return res;
    }

    public IDeversoirComportementLoi transformeDeversoirZQ2Loi(IDeversoirComportementZCoefQ aRemplacer) {
        int num = aRemplacer.numero();
        String nom = aRemplacer.nom();
        double abs = aRemplacer.abscisse();
        double lg = aRemplacer.longueur();
        int i;
        for (i = 0; i < singularites_.length; i++) {
            if (singularites_[i].id() == aRemplacer.id()) {
                UsineLib.findUsine().supprimeHydraulique1dDeversoirComportementZCoefQ(aRemplacer);
                IDeversoirComportementLoi deversoirNew = UsineLib.findUsine().creeHydraulique1dDeversoirComportementLoi();
                deversoirNew.numero(num);
                deversoirNew.nom(nom);
                deversoirNew.abscisse(abs);
                deversoirNew.longueur(lg);
                singularites_[i] = deversoirNew;
                break;
            }
        }
        return (IDeversoirComportementLoi) singularites_[i];
    }

    public IDeversoirComportementZCoefQ transformeDeversoirLoi2ZQ(IDeversoirComportementLoi aRemplacer) {
        int num = aRemplacer.numero();
        String nom = aRemplacer.nom();
        double abs = aRemplacer.abscisse();
        double lg = aRemplacer.longueur();
        int i;
        for (i = 0; i < singularites_.length; i++) {
            if (singularites_[i].equals(aRemplacer)) {
                UsineLib.findUsine().supprimeHydraulique1dDeversoirComportementLoi(aRemplacer);
                IDeversoirComportementZCoefQ deversoirNew = UsineLib.findUsine().creeHydraulique1dDeversoirComportementZCoefQ();
                deversoirNew.numero(num);
                deversoirNew.nom(nom);
                deversoirNew.abscisse(abs);
                deversoirNew.longueur(lg);
                singularites_[i] = deversoirNew;
                break;
            }
        }
        return (IDeversoirComportementZCoefQ) singularites_[i];
    }

    public IBarrage transformeSeuilLoi2Barrage(ISeuilLoi aRemplacer) {
        int num = aRemplacer.numero();
        String nom = aRemplacer.nom();
        double abs = aRemplacer.abscisse();
        double zRupture = aRemplacer.coteRupture();
        double zCrete = aRemplacer.coteCrete();
        int i;
        for (i = 0; i < singularites_.length; i++) {
            if (singularites_[i].equals(aRemplacer)) {
                UsineLib.findUsine().supprimeHydraulique1dSeuilLoi(aRemplacer);
                IBarrage seuilNew = UsineLib.findUsine().creeHydraulique1dBarrage();
                seuilNew.numero(num);
                seuilNew.nom(nom);
                seuilNew.abscisse(abs);
                seuilNew.coteRupture(zRupture);
                seuilNew.coteCrete(zCrete);
                singularites_[i] = seuilNew;
                break;
            }
        }
        return (IBarrage) singularites_[i];
    }

    public ISeuilLoi transformeBarrage2SeuilLoi(IBarrage aRemplacer) {
        int num = aRemplacer.numero();
        String nom = aRemplacer.nom();
        double abs = aRemplacer.abscisse();
        double zRupture = aRemplacer.coteRupture();
        double zCrete = aRemplacer.coteCrete();
        int i;
        for (i = 0; i < singularites_.length; i++) {
            if (singularites_[i].equals(aRemplacer)) {
                UsineLib.findUsine().supprimeHydraulique1dBarrage(aRemplacer);
                ISeuilLoi seuilNew = UsineLib.findUsine().creeHydraulique1dSeuilLoi();
                seuilNew.numero(num);
                seuilNew.nom(nom);
                seuilNew.abscisse(abs);
                seuilNew.coteRupture(zRupture);
                seuilNew.coteCrete(zCrete);
                singularites_[i] = seuilNew;
                break;
            }
        }
        return (ISeuilLoi) singularites_[i];
    }

    public ILimite[] limites() {
        Vector limites = new Vector();
        if (extrAmont_.conditionLimite() != null) limites.add(extrAmont_.conditionLimite());
        if (extrAval_.conditionLimite() != null) limites.add(extrAval_.conditionLimite());
        ILimite[] res = new ILimite[limites.size()];
        for (int i = 0; i < res.length; i++) res[i] = (ILimite) limites.get(i);
        return res;
    }

    public IProfil[] profilsAvecZonesStockage() {
        if (profils_ == null) return new IProfil[0];
        Vector profilsAStockage = new Vector();
        IProfil p;
        for (int i = 0; i < profils_.length; i++) {
            p = profils_[i];
            if (p.hasZoneStockage()) profilsAStockage.addElement(p);
        }
        IProfil[] res = new IProfil[profilsAStockage.size()];
        for (int i = 0; i < res.length; i++) res[i] = (IProfil) profilsAStockage.get(i);
        return res;
    }

    public int getIndiceProfilNumero(int numeroProfil) {
        for (int i = 0; i < profils_.length; i++) {
            if (profils_[i].numero() == numeroProfil) return i;
        }
        return -1;
    }

    public int getIndiceProfilAbscisse(double abscisseProfil) {
        for (int i = 0; i < profils_.length; i++) {
            if (CGlobal.egale(profils_[i].abscisse(), abscisseProfil)) return i; else {
                if (profils_[i].abscisse() > abscisseProfil) {
                    return -i;
                }
            }
        }
        return Integer.MIN_VALUE;
    }

    public boolean contientProfilNumero(int numeroProfil) {
        return getIndiceProfilNumero(numeroProfil) >= 0;
    }

    public boolean contientProfilAbscisse(double abscisseProfil) {
        return getIndiceProfilAbscisse(abscisseProfil) >= 0;
    }

    public boolean contientAbscisse(double abscisse) {
        if ((profils_ == null) || (profils_.length == 0)) return false;
        return CGlobal.appartient(abscisse, profils_[0].abscisse(), profils_[profils_.length - 1].abscisse());
    }

    public boolean contientSingularite(ISingularite sing) {
        if (singularites_ == null) return false;
        for (int i = 0; i < singularites_.length; i++) if (singularites_[i] == sing) return true;
        return false;
    }

    public int getNbPasPlanimetrage() {
        int nbPas = 0;
        org.omg.CORBA.DoubleHolder coteMin = new org.omg.CORBA.DoubleHolder();
        org.omg.CORBA.DoubleHolder coteMax = new org.omg.CORBA.DoubleHolder();
        for (int i = 0; i < zonesPlanimetrage_.length; i++) {
            double pas = zonesPlanimetrage_[i].taillePas();
            int indDebut = getIndiceProfilAbscisse(zonesPlanimetrage_[i].abscisseDebut());
            int indFin = getIndiceProfilAbscisse(zonesPlanimetrage_[i].abscisseFin());
            if (indDebut < 0) indDebut = -indDebut;
            if (indFin < 0) indFin = -indFin;
            for (int j = indDebut; j <= indFin; j++) {
                profils_[j].coteMinMax(coteMin, coteMax);
                double h = coteMax.value - coteMin.value;
                nbPas = Math.max((int) (h / pas) + 1, nbPas);
            }
        }
        return nbPas;
    }

    public double interpoleFond(double abscisse) {
        double res = Double.POSITIVE_INFINITY;
        IProfil p1 = null;
        IProfil p2 = null;
        for (int i = 0; i < profils_.length; i++) {
            if (profils_[i].abscisse() >= abscisse) {
                if (i > 0) p1 = profils_[i - 1];
                p2 = profils_[i];
                break;
            }
        }
        if ((p1 != null) && (p2 != null)) {
            double n1 = p1.getFond();
            double n2 = p2.getFond();
            res = n2 + (n1 - n2) * (p2.abscisse() - abscisse) / (p2.abscisse() - p1.abscisse());
        }
        return res;
    }

    public double getLongueur() {
        if (profils_ == null) return 0;
        if (profils_.length == 0) return 0;
        return getXFin() - getXOrigine();
    }

    private void trieProfils() {
        if (profils_ == null) return;
        Arrays.sort(profils_, DProfil.COMPARATOR);
    }

    private void miseAJourExtremite() {
        if (profils_ == null) return;
        if (profils_.length > 0) {
            if (extrAmont_ != null) extrAmont_.profilRattache(profils_[0]);
            if (extrAval_ != null) extrAval_.profilRattache(profils_[profils_.length - 1]);
        }
    }

    private void trieZonesFrottement() {
        if (zonesFrottement_ == null) return;
        Arrays.sort(zonesFrottement_, DZone.COMPARATOR);
    }

    private void trieZonesPlanimetrage() {
        if (zonesPlanimetrage_ == null) return;
        Arrays.sort(zonesPlanimetrage_, DZone.COMPARATOR);
    }
}

class BiefComparator implements Comparator {

    BiefComparator() {
    }

    public int compare(Object o1, Object o2) {
        IProfil p1 = ((IBief) o1).extrAmont().profilRattache();
        IProfil p2 = ((IBief) o2).extrAmont().profilRattache();
        double xo1 = (p1 == null ? Double.MAX_VALUE : p1.abscisse());
        double xo2 = (p2 == null ? Double.MAX_VALUE : p2.abscisse());
        return xo1 < xo2 ? -1 : xo1 == xo2 ? 0 : 1;
    }

    public boolean equals(Object obj) {
        return false;
    }
}
