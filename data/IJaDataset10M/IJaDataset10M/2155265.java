package org.fudaa.dodico.mascaret;

import org.fudaa.dodico.corba.hydraulique1d.ICalculHydraulique1d;
import org.fudaa.dodico.corba.hydraulique1d.IEtude1d;
import org.fudaa.dodico.corba.hydraulique1d.IParametresTailleMaxFichier;
import org.fudaa.dodico.corba.hydraulique1d.calageauto.IResultatsCalageAuto;
import org.fudaa.dodico.corba.mascaret.ICalculMascaret;
import org.fudaa.dodico.corba.mascaret.ICalculMascaretHelper;
import org.fudaa.dodico.corba.mascaret.IParametresMascaret;
import org.fudaa.dodico.corba.mascaret.IParametresMascaretHelper;
import org.fudaa.dodico.corba.mascaret.IResultatsMascaret;
import org.fudaa.dodico.corba.mascaret.IResultatsMascaretHelper;
import org.fudaa.dodico.corba.mascaret.SParametresCAS;
import org.fudaa.dodico.corba.mascaret.SParametresNCA;
import org.fudaa.dodico.corba.mascaret.SParametresREP;
import org.fudaa.dodico.corba.mascaret.SParametresTailleMaxFichier;
import org.fudaa.dodico.corba.objet.IConnexion;
import org.fudaa.dodico.objet.UsineLib;

/**
 * Classe qui contient des m�thodes utiles � la conversion entre les 2 mod�les.
 *
 * @version      $Revision: 1.28 $ $Date: 2006-09-12 08:31:30 $ by $Author: opasteur $
 * @author       Jean-Marc Lacombe
 */
public class CConversionHydraulique1d {

    public IParametresMascaret parametresMascaret;

    public IResultatsMascaret resultatsMascaret;

    private IConnexion connexion_;

    public ICalculHydraulique1d calculHydraulique1d;

    public CConversionHydraulique1d(ICalculHydraulique1d calculHydraulique1d_, IConnexion c) {
        if (calculHydraulique1d_ == null) return;
        calculHydraulique1d = calculHydraulique1d_;
        ICalculMascaret calculMascaret_ = ICalculMascaretHelper.narrow(calculHydraulique1d_.calculCode());
        parametresMascaret = IParametresMascaretHelper.narrow(calculMascaret_.parametres(c));
        resultatsMascaret = IResultatsMascaretHelper.narrow(calculMascaret_.resultats(c));
        connexion_ = c;
    }

    /**
   * Converti les param�tres pour le cas de calage/simulation.
   * @param _calage true : Cas de calage.
   */
    public final void convertirParametres(boolean _calage) {
        parametresMascaret.parametresCAS(convertirParametresCas(_calage, calculHydraulique1d.etude()));
        parametresMascaret.parametresGEO(ConvH1D_Masc.convertirParametresGeo(calculHydraulique1d.etude().reseau().biefs()));
        parametresMascaret.loisHydrauliques(ConvH1D_Masc.convertirLoisHydrauliques(calculHydraulique1d.etude().donneesHydro().getToutesLoisSaufTracerEtGeometrique()));
        if (calculHydraulique1d.etude().donneesHydro().conditionsInitiales().ligneEauInitiale() != null) {
            parametresMascaret.parametresLigneDEauInitiale(ConvH1D_Masc.convertirParametresLig(calculHydraulique1d.etude().donneesHydro().conditionsInitiales().ligneEauInitiale(), calculHydraulique1d.etude().reseau(), calculHydraulique1d.etude().paramResultats().decalage()));
        }
        if (calculHydraulique1d.etude().donneesHydro().conditionsInitiales().paramsReprise() != null) parametresMascaret.parametresREP(ConvH1D_Masc.convertirParametresRep(calculHydraulique1d.etude().donneesHydro().conditionsInitiales().paramsReprise()));
        parametresMascaret.parametresNCA(new SParametresNCA("mascaret" + connexion_.numero() + ".cas"));
        IParametresTailleMaxFichier t = calculHydraulique1d.etude().paramResultats().paramTailleMaxFichier();
        parametresMascaret.parametresTailleMaxFichier(new SParametresTailleMaxFichier(t.maxListingCode(), t.maxListingDamocles(), t.maxListingCalage(), t.maxListingTracer(), t.maxResultatRubens(), t.maxResultatOpthyca(), t.maxResultatReprise(), t.maxResultatRubensTracer(), t.maxResultatOpthycaTracer()));
        boolean presenceCasier = (calculHydraulique1d.etude().reseau().casiers().length > 0);
        if (presenceCasier) {
            parametresMascaret.casierGEO(ConvH1D_Masc.convertirParametresGeoCasiers(calculHydraulique1d.etude().reseau().casiers()));
        }
        if (calculHydraulique1d.etude().qualiteDEau().parametresModeleQualiteEau().presenceTraceurs()) {
            parametresMascaret.loisTracer(ConvH1D_Masc.convertirLoisTracer(calculHydraulique1d.etude().donneesHydro().getLoisTracer(), calculHydraulique1d.etude().qualiteDEau().parametresModeleQualiteEau().vvNomTracer()));
            parametresMascaret.parametresConcentInitiales(ConvH1D_Masc.convertitIConcentrationInitiale_ResultatTemporelSpatial(calculHydraulique1d.etude().qualiteDEau().concentrationsInitiales(), calculHydraulique1d.etude().qualiteDEau().parametresModeleQualiteEau().vvNomTracer()));
            parametresMascaret.parametresPhysModele(ConvH1D_Masc.convertirParametresPhysiqueQE(calculHydraulique1d.etude().qualiteDEau().parametresGenerauxQualiteDEau().paramsPhysTracer()));
            parametresMascaret.paramMeteoTracer(ConvH1D_Masc.convertirParametresMeteoQE(calculHydraulique1d.etude().qualiteDEau().parametresGenerauxQualiteDEau().paramMeteoTracer()));
        }
    }

    /**
   * Conversion des r�sultats pour le calage.
   */
    public void convertirResultatsPourCalageAuto() {
        IResultatsCalageAuto res = calculHydraulique1d.etude().calageAuto().resultats();
        if (res == null) res = UsineLib.findUsine().creeHydraulique1dResultatsCalageAuto();
        calculHydraulique1d.etude().calageAuto().resultats(res);
        if (resultatsMascaret.resultatsLIS() != null && resultatsMascaret.resultatsLIS().contenu != null) {
            res.listingMascaret(resultatsMascaret.resultatsLIS().contenu);
        } else res.listingMascaret(new byte[0]);
        if (resultatsMascaret.resultatsDAMOC() != null && resultatsMascaret.resultatsDAMOC().contenu != null) res.listingDamocles(resultatsMascaret.resultatsDAMOC().contenu); else res.listingDamocles(new byte[0]);
        if (resultatsMascaret.resultatsCalAutoLIS() != null && resultatsMascaret.resultatsCalAutoLIS().contenu != null) res.listingCalage(resultatsMascaret.resultatsCalAutoLIS().contenu); else res.listingCalage(new byte[0]);
        if (resultatsMascaret.resultatsEcran() != null && resultatsMascaret.resultatsEcran().contenu != null) res.messagesEcran(resultatsMascaret.resultatsEcran().contenu); else res.messagesEcran(new byte[0]);
        if (resultatsMascaret.resultatsEcranErreur() != null && resultatsMascaret.resultatsEcranErreur().contenu != null) res.messagesEcranErreur(resultatsMascaret.resultatsEcranErreur().contenu); else res.messagesEcranErreur(new byte[0]);
        if (resultatsMascaret.resultatsCalAuto() != null) {
            res.resultats(ConvMasc_H1D.convertirResultatsTemporelSpatialMasToH1d(resultatsMascaret.resultatsCalAuto()));
            res.resultats().setTemporel(false);
        }
        if (resultatsMascaret.resultatsCalAuto() != null) {
            res.zonesFrottementCalees(ConvMasc_H1D.convertirResultatsCalage2ZonesFrottement(calculHydraulique1d.etude().reseau().biefs(), parametresMascaret.parametresCAS().parametresCalageAuto.zones, resultatsMascaret.resultatsCalAuto()));
        }
    }

    public void convertirResultats() {
        if (calculHydraulique1d.etude().resultatsGeneraux() == null) calculHydraulique1d.etude().resultatsGeneraux(UsineLib.findUsine().creeHydraulique1dResultatsGeneraux());
        convertirResultatsLis();
        convertirResultatsLisDamocles();
        convertirResultatsTemporelSpatial();
        convertirResultatsCasier();
        convertirResultatsLiaison();
        convertirResultatsEcran();
        convertirResultatsEcranErreur();
        convertirResultatsRep();
        convertirResultatLisCasier();
        convertirResultatLisLiaison();
        convertirResultatLisTracer();
        convertirResultatsTracer();
    }

    private final SParametresCAS convertirParametresCas(boolean _calage, IEtude1d etude) {
        return ConvH1D_Masc.convertirParametresCas(_calage, etude, connexion_.numero());
    }

    private void convertirResultatsLis() {
        if (resultatsMascaret.resultatsLIS() == null) calculHydraulique1d.etude().resultatsGeneraux().listing(null); else if (resultatsMascaret.resultatsLIS().contenu == null) calculHydraulique1d.etude().resultatsGeneraux().listing(null); else if (resultatsMascaret.resultatsLIS().contenu.length == 0) calculHydraulique1d.etude().resultatsGeneraux().listing(null); else {
            calculHydraulique1d.etude().resultatsGeneraux().listing(resultatsMascaret.resultatsLIS().contenu);
        }
    }

    private void convertirResultatsLisDamocles() {
        if (resultatsMascaret.resultatsDAMOC() == null) calculHydraulique1d.etude().resultatsGeneraux().listingDamocles(null); else if (resultatsMascaret.resultatsDAMOC().contenu == null) calculHydraulique1d.etude().resultatsGeneraux().listingDamocles(null); else if (resultatsMascaret.resultatsDAMOC().contenu.length == 0) calculHydraulique1d.etude().resultatsGeneraux().listingDamocles(null); else calculHydraulique1d.etude().resultatsGeneraux().listingDamocles(resultatsMascaret.resultatsDAMOC().contenu);
    }

    /**
   * conversion Fichier listing casier
   */
    private void convertirResultatLisCasier() {
        if (resultatsMascaret.casierLIS() == null) calculHydraulique1d.etude().resultatsGeneraux().listingCasier(null); else if (resultatsMascaret.casierLIS().contenu == null) calculHydraulique1d.etude().resultatsGeneraux().listingCasier(null); else if (resultatsMascaret.casierLIS().contenu.length == 0) calculHydraulique1d.etude().resultatsGeneraux().listingCasier(null); else calculHydraulique1d.etude().resultatsGeneraux().listingCasier(resultatsMascaret.casierLIS().contenu);
    }

    /**
   * conversion Fichier listing liaison
   */
    private void convertirResultatLisLiaison() {
        if (resultatsMascaret.liaisonLIS() == null) calculHydraulique1d.etude().resultatsGeneraux().listingLiaison(null); else if (resultatsMascaret.liaisonLIS().contenu == null) calculHydraulique1d.etude().resultatsGeneraux().listingLiaison(null); else if (resultatsMascaret.liaisonLIS().contenu.length == 0) calculHydraulique1d.etude().resultatsGeneraux().listingLiaison(null); else calculHydraulique1d.etude().resultatsGeneraux().listingLiaison(resultatsMascaret.liaisonLIS().contenu);
    }

    private void convertirResultatsEcran() {
        if (resultatsMascaret.resultatsEcran() == null) calculHydraulique1d.etude().resultatsGeneraux().messagesEcran(new byte[0]); else if (resultatsMascaret.resultatsEcran().contenu == null) calculHydraulique1d.etude().resultatsGeneraux().messagesEcran(new byte[0]); else if (resultatsMascaret.resultatsEcran().contenu.length == 0) calculHydraulique1d.etude().resultatsGeneraux().messagesEcran(new byte[0]); else calculHydraulique1d.etude().resultatsGeneraux().messagesEcran(resultatsMascaret.resultatsEcran().contenu);
    }

    private void convertirResultatsEcranErreur() {
        System.out.println("convertirResultatsEcranErreur()");
        if (resultatsMascaret.resultatsEcranErreur() == null) calculHydraulique1d.etude().resultatsGeneraux().messagesEcranErreur(new byte[0]); else if (resultatsMascaret.resultatsEcranErreur().contenu == null) calculHydraulique1d.etude().resultatsGeneraux().messagesEcranErreur(new byte[0]); else if (resultatsMascaret.resultatsEcranErreur().contenu.length == 0) calculHydraulique1d.etude().resultatsGeneraux().messagesEcranErreur(new byte[0]); else {
            System.out.println("resultatsMascaret.resultatsEcranErreur() Pas vide");
            System.out.println("resultatsMascaret.resultatsEcranErreur().contenu=" + resultatsMascaret.resultatsEcranErreur().contenu);
            calculHydraulique1d.etude().resultatsGeneraux().messagesEcranErreur(resultatsMascaret.resultatsEcranErreur().contenu);
        }
    }

    /**
   *  conversion R�sultats temporel spatial (mascaret)
   */
    private void convertirResultatsTemporelSpatial() {
        if (resultatsMascaret.resultatsTemporelSpatial() == null) calculHydraulique1d.etude().resultatsGeneraux().resultatsTemporelSpatial(null); else calculHydraulique1d.etude().resultatsGeneraux().resultatsTemporelSpatial(ConvMasc_H1D.convertirResultatsTemporelSpatialMasToH1d(resultatsMascaret.resultatsTemporelSpatial()));
    }

    /**
   *  conversion R�sultats Casier
   */
    private void convertirResultatsCasier() {
        if (resultatsMascaret.resultatsCasier() == null) calculHydraulique1d.etude().resultatsGeneraux().resultatsTemporelCasier(null); else calculHydraulique1d.etude().resultatsGeneraux().resultatsTemporelCasier(ConvMasc_H1D.convertirResultatsTemporelSpatialMasToH1d(resultatsMascaret.resultatsCasier()));
    }

    /**
   *  conversion R�sultats Liaison
   */
    private void convertirResultatsLiaison() {
        if (resultatsMascaret.resultatsLiaison() == null) calculHydraulique1d.etude().resultatsGeneraux().resultatsTemporelLiaison(null); else calculHydraulique1d.etude().resultatsGeneraux().resultatsTemporelLiaison(ConvMasc_H1D.convertirResultatsTemporelSpatialMasToH1d(resultatsMascaret.resultatsLiaison()));
    }

    private void convertirResultatsRep() {
        SParametresREP rep = resultatsMascaret.resultatsREP();
        if (rep == null) calculHydraulique1d.etude().resultatsGeneraux().resultatReprise(ConvMasc_H1D.RES_REP_VIDE); else if (rep.contenu == null) calculHydraulique1d.etude().resultatsGeneraux().resultatReprise(ConvMasc_H1D.RES_REP_VIDE); else if (rep.contenu.length == 0) calculHydraulique1d.etude().resultatsGeneraux().resultatReprise(ConvMasc_H1D.RES_REP_VIDE); else calculHydraulique1d.etude().resultatsGeneraux().resultatReprise(ConvMasc_H1D.convertirResultatsRep(resultatsMascaret.resultatsREP()));
    }

    private void convertirResultatsTracer() {
        if (resultatsMascaret.resultatsTracer() == null) calculHydraulique1d.etude().resultatsGeneraux().resultatsTemporelTracer(null); else calculHydraulique1d.etude().resultatsGeneraux().resultatsTemporelTracer(ConvMasc_H1D.convertirResultatsTemporelSpatialMasToH1d(resultatsMascaret.resultatsTracer()));
    }

    private void convertirResultatLisTracer() {
        if (resultatsMascaret.resultatsTracerLIS() == null) calculHydraulique1d.etude().resultatsGeneraux().listingTracer(null); else if (resultatsMascaret.resultatsTracerLIS().contenu == null) calculHydraulique1d.etude().resultatsGeneraux().listingTracer(null); else if (resultatsMascaret.resultatsTracerLIS().contenu.length == 0) calculHydraulique1d.etude().resultatsGeneraux().listingTracer(null); else calculHydraulique1d.etude().resultatsGeneraux().listingTracer(resultatsMascaret.resultatsTracerLIS().contenu);
    }
}
