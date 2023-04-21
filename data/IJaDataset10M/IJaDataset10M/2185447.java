package org.fudaa.dodico.lido;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.fudaa.dodico.calcul.DParametres;
import org.fudaa.dodico.corba.lido.IParametresLido;
import org.fudaa.dodico.corba.lido.IParametresLidoOperations;
import org.fudaa.dodico.corba.lido.SParametresApportLigneCLM;
import org.fudaa.dodico.corba.lido.SParametresBiefBlocPRO;
import org.fudaa.dodico.corba.lido.SParametresBiefLimBlocRZO;
import org.fudaa.dodico.corba.lido.SParametresBiefLimLigneRZO;
import org.fudaa.dodico.corba.lido.SParametresBiefNoeudBlocRZO;
import org.fudaa.dodico.corba.lido.SParametresBiefNoeudLigneRZO;
import org.fudaa.dodico.corba.lido.SParametresBiefSingBlocRZO;
import org.fudaa.dodico.corba.lido.SParametresBiefSingLigneRZO;
import org.fudaa.dodico.corba.lido.SParametresBiefSituBlocRZO;
import org.fudaa.dodico.corba.lido.SParametresBiefSituLigneRZO;
import org.fudaa.dodico.corba.lido.SParametresCAL;
import org.fudaa.dodico.corba.lido.SParametresCLM;
import org.fudaa.dodico.corba.lido.SParametresCondLimBlocCLM;
import org.fudaa.dodico.corba.lido.SParametresCondLimPointLigneCLM;
import org.fudaa.dodico.corba.lido.SParametresEXT;
import org.fudaa.dodico.corba.lido.SParametresFIC;
import org.fudaa.dodico.corba.lido.SParametresFicCAL;
import org.fudaa.dodico.corba.lido.SParametresGenCAL;
import org.fudaa.dodico.corba.lido.SParametresLIG;
import org.fudaa.dodico.corba.lido.SParametresLimBiefLIG;
import org.fudaa.dodico.corba.lido.SParametresPRO;
import org.fudaa.dodico.corba.lido.SParametresPasLigneCAL;
import org.fudaa.dodico.corba.lido.SParametresPerteLigneCLM;
import org.fudaa.dodico.corba.lido.SParametresPlaniCAL;
import org.fudaa.dodico.corba.lido.SParametresRZO;
import org.fudaa.dodico.corba.lido.SParametresSNG;
import org.fudaa.dodico.corba.lido.SParametresSectionBlocCAL;
import org.fudaa.dodico.corba.lido.SParametresSectionLigneCAL;
import org.fudaa.dodico.corba.lido.SParametresSectionsCAL;
import org.fudaa.dodico.corba.lido.SParametresSerieBlocCAL;
import org.fudaa.dodico.corba.lido.SParametresSerieLigneCAL;
import org.fudaa.dodico.corba.lido.SParametresSingBlocSNG;
import org.fudaa.dodico.corba.lido.SParametresTempCAL;
import org.fudaa.dodico.fortran.FortranReader;
import org.fudaa.dodico.fortran.FortranWriter;

/**
 * Les parametres Lido.
 * 
 * @version $Revision: 1.12 $ $Date: 2007-04-30 14:21:38 $ by $Author: deniger $
 * @author Mickael Rubens , Axel von Arnim
 */
public class DParametresLido extends DParametres implements IParametresLido, IParametresLidoOperations {

    public static final boolean DEBUG = "yes".equals(System.getProperty("fudaa.debug"));

    private SParametresCAL paramsCAL_;

    private SParametresCLM paramsCLM_;

    private SParametresFIC paramsFIC_;

    private SParametresLIG paramsLIG_;

    private SParametresPRO paramsPRO_;

    private SParametresRZO paramsRZO_;

    private SParametresSNG paramsSNG_;

    private SParametresEXT paramsEXT_;

    public DParametresLido() {
        super();
    }

    public final Object clone() throws CloneNotSupportedException {
        return new DParametresLido();
    }

    public SParametresCAL parametresCAL() {
        return paramsCAL_;
    }

    public void parametresCAL(SParametresCAL p) {
        paramsCAL_ = p;
    }

    public SParametresCLM parametresCLM() {
        return paramsCLM_;
    }

    public void parametresCLM(SParametresCLM p) {
        paramsCLM_ = p;
    }

    public SParametresFIC parametresFIC() {
        return paramsFIC_;
    }

    public void parametresFIC(SParametresFIC p) {
        paramsFIC_ = p;
    }

    public SParametresLIG parametresLIG() {
        return paramsLIG_;
    }

    public void parametresLIG(SParametresLIG p) {
        paramsLIG_ = p;
    }

    public SParametresPRO parametresPRO() {
        return paramsPRO_;
    }

    public void parametresPRO(SParametresPRO p) {
        paramsPRO_ = p;
    }

    public SParametresRZO parametresRZO() {
        return paramsRZO_;
    }

    public void parametresRZO(SParametresRZO p) {
        paramsRZO_ = p;
    }

    public SParametresSNG parametresSNG() {
        return paramsSNG_;
    }

    public void parametresSNG(SParametresSNG p) {
        paramsSNG_ = p;
    }

    public SParametresEXT parametresEXT() {
        return paramsEXT_;
    }

    public void parametresEXT(SParametresEXT p) {
        paramsEXT_ = p;
    }

    /** .CAL. * */
    public static void ecritParametresCAL(File fichier, SParametresCAL params) {
        try {
            FortranWriter fcal = new FortranWriter(new FileWriter(fichier));
            if (params.status) {
                System.out.println("Creation du fichier " + fichier.getName());
                if ((params.titreCAL[0] == null) || (params.titreCAL[0] == "")) {
                    params.titreCAL[0] = "FICHIER DES DONNEES GENERALES DU CALCUL -" + fichier.getName();
                }
                if ((params.titreCAL[1] == null) || (params.titreCAL[1] == "")) {
                    params.titreCAL[1] = " ------------------------------------------------------------------------------";
                }
                int[] fmt = new int[] { 72 };
                for (int i = 0; i < 2; i++) {
                    fcal.stringField(0, params.titreCAL[i]);
                    fcal.writeFields(fmt);
                }
                fcal.writeln("#");
                fcal.stringField(0, "CODE   =");
                fcal.stringField(1, params.genCal.code);
                fcal.stringField(2, "REGIME =");
                fcal.stringField(3, params.genCal.regime);
                fcal.stringField(4, "SEUIL  =");
                fcal.stringField(5, params.genCal.seuil);
                fcal.writeFields();
                fcal.stringField(0, "COMPOSITION DES LITS =");
                fcal.stringField(1, params.genCal.compLits);
                fcal.writeFields();
                fcal.stringField(0, "CONSERV. DU FROT. SUR LES PAROIS VERT.    : FROTPV =");
                fcal.stringField(1, params.genCal.frotPV);
                fcal.writeFields();
                fcal.writeln("#");
                fcal.stringField(0, "IMPRESSION DE LA GEOMETRIE                : IMPGEO =");
                fcal.stringField(1, params.genCal.impGeo);
                fcal.writeFields();
                fcal.stringField(0, "IMPRESSION DU PLANIMETRAGE                : IMPLAN =");
                fcal.stringField(1, params.genCal.impPlan);
                fcal.writeFields();
                fcal.stringField(0, "IMPRESSION DU RESEAU                      : IMPREZ =");
                fcal.stringField(1, params.genCal.impRez);
                fcal.writeFields();
                fcal.stringField(0, "IMPRESSION DES HYDROGRAMMES D ENTREE      : IMPHYD =");
                fcal.stringField(1, params.genCal.impHyd);
                fcal.writeFields();
                fcal.writeln("#");
                fcal.stringField(0, "NUMERO DU FICHIER GEOMETRIQUE             : NFGEOM =");
                fcal.intField(1, params.fic.nFGeo);
                fcal.writeFields();
                fcal.stringField(0, "NUMERO DU FICHIER SINGULARITE             : NFSING =");
                fcal.intField(1, params.fic.nFSing);
                fcal.writeFields();
                fcal.stringField(0, "NUMERO DU RESEAU                          : NFREZO =");
                fcal.intField(1, params.fic.nFRez);
                fcal.writeFields();
                fcal.stringField(0, "NUMERO DU FICHIER LIGNE D EAU INITIALE    : NFLIGN =");
                fcal.intField(1, params.fic.nFLign);
                fcal.writeFields();
                fcal.stringField(0, "NUMERO DU FICHIER CONDITIONS LIMITES      : NFCLIM =");
                fcal.intField(1, params.fic.nFCLim);
                fcal.writeFields();
                fcal.stringField(0, "NUMERO DU FICHIER DE STOCKAGE EN LECTURE  : NFSLEC =");
                fcal.intField(1, params.fic.nFSLec);
                fcal.writeFields();
                fcal.stringField(0, "NUMERO DU FICHIER DE STOCKAGE EN ECRITURE : NFSECR =");
                fcal.intField(1, params.fic.nFSEcr);
                fcal.writeFields();
                fcal.writeln("#");
                fcal.stringField(0, "BIEF A TRAITER : XORIGI =");
                fcal.doubleField(1, params.genCal.biefXOrigi);
                fcal.stringField(2, "XFIN  =");
                fcal.doubleField(3, params.genCal.biefXFin);
                fcal.writeFields();
                fcal.writeln("#");
                fcal.writeln("#     VARIABLES DU PLANIMETRAGE ");
                fcal.stringField(0, "NOMBRE DE VALEURS =");
                fcal.intField(1, params.planimetrage.varPlanNbVal);
                fcal.stringField(2, "NOMBRE DE PAS =");
                fcal.intField(3, params.planimetrage.varPlanNbPas);
                fcal.writeFields();
                int j = 0;
                for (int i = 0; i < params.planimetrage.varPlanNbPas; i++) {
                    j = i + 1;
                    fcal.stringField(0, "PAS " + Integer.toString(j) + "   =");
                    fcal.doubleField(1, params.planimetrage.varsPlanimetrage[i].taillePas);
                    fcal.stringField(2, "DU PROFIL =");
                    fcal.intField(3, params.planimetrage.varsPlanimetrage[i].profilDebut);
                    fcal.stringField(4, "AU PROFIL =");
                    fcal.intField(5, params.planimetrage.varsPlanimetrage[i].profilFin);
                    fcal.writeFields();
                }
                fcal.writeln("#");
                fcal.writeln("#     VARIABLES TEMPORELLES (EN SECONDES)");
                fcal.stringField(0, "T INITIAL                     =");
                fcal.doubleField(1, params.temporel.tInit);
                fcal.writeFields();
                fcal.stringField(0, "T MAXIMAL                     =");
                fcal.doubleField(1, params.temporel.tMax);
                fcal.writeFields();
                fcal.stringField(0, "PAS DE TEMPS                  =");
                fcal.doubleField(1, params.temporel.pas2T);
                fcal.writeFields();
                fcal.stringField(0, "NUMERO DU DERNIER PAS STOCKE  =");
                fcal.intField(1, params.temporel.numDerPaStoc);
                fcal.writeFields();
                fcal.stringField(0, "PAS DE TEMPS D IMPRESSION     =");
                fcal.doubleField(1, params.temporel.pas2TImp);
                fcal.writeFields();
                fcal.stringField(0, "PAS DE TEMPS DE STOCKAGE      =");
                fcal.doubleField(1, params.temporel.pas2TImp);
                fcal.writeFields();
                fcal.writeln("#");
                fcal.writeln("#-----------------------------------------------------------------------");
                fcal.stringField(0, "CHOIX DES SECTIONS DE CALCUL :  NCHOIX =");
                fcal.intField(1, params.sections.nChoix);
                fcal.writeFields();
                j = 0;
                if (params.sections.nChoix == 2) {
                    fcal.stringField(0, "NOMBRE DE SEGMENTS = ");
                    fcal.intField(1, params.sections.series.nbSeries);
                    fcal.writeFields();
                    for (int i = 0; i < params.sections.series.nbSeries; i++) {
                        fcal.stringField(0, " SEGMENT = " + Integer.toString(j) + ": XD =");
                        fcal.doubleField(1, params.sections.series.ligne[i].absDebBief);
                        fcal.stringField(2, ": XF =");
                        fcal.doubleField(3, params.sections.series.ligne[i].absFinBief);
                        fcal.stringField(4, "IPX =");
                        fcal.intField(5, params.sections.series.ligne[i].nbSectCalc);
                        fcal.writeFields();
                    }
                } else {
                    if (params.sections.nChoix == 3) {
                        fcal.stringField(0, "NOMBRE DE SECTIONS = ");
                        fcal.intField(1, params.sections.sections.nbSect);
                        fcal.writeFields();
                        fcal.stringField(0, "XPX =");
                        fcal.writeFields();
                        j = 0;
                        for (int i = 0; i < params.sections.sections.nbSect; i++) {
                            if (j > 4) {
                                fcal.writeFields();
                                j = 0;
                            }
                            fcal.doubleField(j++, params.sections.sections.ligne[i].absSect);
                        }
                        fcal.writeFields();
                    }
                }
                fcal.stringField(0, "**********FIN DU FICHIER PARAMETRES*************************************");
                fcal.writeFields(fmt);
            } else {
                System.out.println("Fichier " + fichier.getName() + " vide");
            }
            fcal.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("IT: " + ex);
        }
        ;
    }

    public static void ecritParametresCLM(File fichier, SParametresCLM params) {
        try {
            FortranWriter fclm = new FortranWriter(new BufferedWriter(new FileWriter(fichier)));
            if (params.status) {
                System.out.println("Creation du fichier " + fichier.getName());
                int[] fmt = new int[] { 72 };
                if (params.titreCLM == "") {
                    params.titreCLM = "FICHIER DES CONDITIONS LIMITES - " + fichier.getName();
                }
                fclm.stringField(0, params.titreCLM);
                fclm.writeFields(fmt);
                fclm.stringField(0, " NLIMIT = ");
                fclm.intField(1, params.nbCondLim);
                fclm.writeFields();
                for (int i = 0; i < params.nbCondLim; i++) {
                    fclm.stringField(0, "------------------------------------------------------------------------");
                    fclm.writeFields(fmt);
                    fclm.stringField(0, "CONDITION LIMITE:");
                    fclm.intField(1, params.condLimites[i].numCondLim);
                    fclm.stringField(2, params.condLimites[i].description);
                    fclm.writeFields();
                    fclm.stringField(0, "ILIM=");
                    fclm.intField(1, params.condLimites[i].nbPoints);
                    fclm.writeFields();
                    for (int j = 0; j < params.condLimites[i].nbPoints; j++) {
                        fclm.stringField(0, "T=");
                        fclm.doubleField(1, params.condLimites[i].point[j].tLim);
                        fclm.stringField(2, "Z=");
                        fclm.doubleField(3, params.condLimites[i].point[j].zLim);
                        fclm.stringField(4, "Q=");
                        fclm.doubleField(5, params.condLimites[i].point[j].qLim);
                        fclm.writeFields();
                    }
                }
                fclm.stringField(0, "------------------------------------------------------------------------");
                fclm.writeFields(fmt);
                if (params.sousTitrePerte == "") {
                    params.sousTitrePerte = "DEFINITION DES PERTES SINGULIERES";
                }
                fclm.stringField(0, params.sousTitrePerte);
                fclm.writeFields(fmt);
                fclm.stringField(0, "NBPERT=");
                fclm.intField(1, params.nbPerte);
                fclm.writeFields();
                for (int i = 0; i < params.nbPerte; i++) {
                    fclm.stringField(0, " XPERTE:");
                    fclm.doubleField(1, params.perte[i].xPerte);
                    fclm.stringField(2, " COEPER: ");
                    fclm.doubleField(3, params.perte[i].coefPerte);
                    fclm.writeFields();
                }
                fclm.stringField(0, "------------------------------------------------------------------------");
                fclm.writeFields(fmt);
                if (params.sousTitreApport == "") {
                    params.sousTitreApport = "DEFINITION DES DEBITS DES ECHANGES LATERAUX";
                }
                fclm.stringField(0, params.sousTitreApport);
                fclm.writeFields(fmt);
                fclm.stringField(0, "NBAPPO=");
                fclm.intField(1, params.nbApport);
                fclm.writeFields();
                for (int i = 0; i < params.nbApport; i++) {
                    fclm.stringField(0, " XAPPOR:");
                    fclm.doubleField(1, params.apport[i].xApport);
                    fclm.stringField(2, " NHYAPP: ");
                    fclm.intField(3, params.apport[i].numLoi);
                    fclm.stringField(4, " KHYAPP: ");
                    fclm.intField(5, (params.apport[i].typLoi == 4 ? 2 : params.apport[i].typLoi));
                    fclm.stringField(6, " COEAPP: ");
                    fclm.doubleField(7, params.apport[i].coefApport);
                    fclm.writeFields();
                }
                fclm.stringField(0, " FIN DU FICHIER ********************************************************");
                fclm.writeFields(fmt);
            } else {
                System.out.println("Fichier " + fichier.getName() + " vide");
            }
            fclm.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("IT: " + ex);
        }
        ;
    }

    public static void ecritParametresFIC(SParametresFIC params, File fichier) {
        try {
            FortranWriter ffic = new FortranWriter(new FileWriter(fichier));
            if (params.status) {
                System.out.println("Creation du fichier " + fichier.getName());
                int[] fmt = new int[] { 40, 20 };
                int[] fmtf = new int[] { 72 };
                ffic.stringField(0, "         Racine du Fichier de Profil  : ");
                ffic.stringField(1, params.racineFicPro);
                ffic.writeFields(fmt);
                ffic.stringField(0, "         Racine du Fichier de l'Etude : ");
                ffic.stringField(1, params.racineFicEtu);
                ffic.writeFields(fmt);
                ffic.stringField(0, "         Fichier Lecture Archivage    : ");
                ffic.stringField(1, params.ficLectArchi);
                ffic.writeFields(fmt);
                ffic.stringField(0, "         Fichier Ecriture Archivage   : ");
                ffic.stringField(1, params.ficEcriArchi);
                ffic.writeFields(fmt);
                ffic.stringField(0, " Fin du fichier lidonp.fic *********************************************");
                ffic.writeFields(fmtf);
            } else {
                System.out.println("Fichier " + fichier.getName() + " vide");
            }
            ffic.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("IT: " + ex);
        }
        ;
    }

    public static void ecritParametresLIG(File _fichier, SParametresLIG params) {
        try {
            if (params != null && params.status) {
                FortranWriter flig = new FortranWriter(new FileWriter(_fichier));
                System.out.println("Creation du fichier " + _fichier.getName());
                int[] fmt1 = new int[] { 72 };
                int[] fmt2 = new int[] { 8, 5, 8, 5 };
                int[] fmt3;
                int[] fmt4 = new int[] { 5 };
                for (int i = 0; i < 3; i++) {
                    flig.stringField(0, params.titreLIG[i]);
                    flig.writeFields(fmt1);
                }
                flig.stringField(0, " IMAX  =");
                flig.intField(1, params.nbSections);
                flig.stringField(2, " NBBIEF=");
                flig.intField(3, params.nbBief);
                flig.writeFields(fmt2);
                int cmpt = 1;
                for (int i = 0; i < params.nbBief; i++) {
                    flig.intField(cmpt++, params.delimitBief[i].sectionDebut);
                    flig.intField(cmpt++, params.delimitBief[i].sectionFin);
                    if (cmpt > 10) {
                        fmt3 = new int[] { 8, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
                        flig.writeFields(fmt3);
                        cmpt = 1;
                    } else {
                        if (params.nbBief == i + 1) {
                            fmt3 = new int[cmpt];
                            fmt3[0] = 8;
                            for (int j = 1; j < cmpt; j++) {
                                fmt3[j] = 5;
                            }
                            flig.writeFields(fmt3);
                        }
                    }
                    flig.stringField(0, "I1,I2 = ");
                }
                if ((params.x != null) && (params.x.length > 0)) {
                    flig.stringField(0, " X");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.x);
                }
                if ((params.zref != null) && (params.zref.length > 0)) {
                    flig.stringField(0, " ZREF");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.zref);
                }
                if ((params.z != null) && (params.z.length > 0)) {
                    flig.stringField(0, " Z");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.z);
                }
                if ((params.q != null) && (params.q.length > 0)) {
                    flig.stringField(0, " Q");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.q);
                }
                if ((params.vmin != null) && (params.vmin.length > 0)) {
                    flig.stringField(0, " VMIN");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.vmin);
                }
                if ((params.vmaj != null) && (params.vmaj.length > 0)) {
                    flig.stringField(0, " VMAJ");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.vmaj);
                }
                if ((params.rgc != null) && (params.rgc.length > 0)) {
                    flig.stringField(0, " RGC");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.rgc);
                }
                if ((params.rdc != null) && (params.rdc.length > 0)) {
                    flig.stringField(0, " RDC");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.rdc);
                }
                if ((params.st1 != null) && (params.st1.length > 0)) {
                    flig.stringField(0, " ST1");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.st1);
                }
                if ((params.st2 != null) && (params.st2.length > 0)) {
                    flig.stringField(0, " ST2");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.st2);
                }
                if ((params.rmin != null) && (params.rmin.length > 0)) {
                    flig.stringField(0, " RMIN");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.rmin);
                }
                if ((params.rmaj != null) && (params.rmaj.length > 0)) {
                    flig.stringField(0, " RMAJ");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.rmaj);
                }
                if ((params.vol != null) && (params.vol.length > 0)) {
                    flig.stringField(0, " VOL");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.vol);
                }
                if ((params.vols != null) && (params.vols.length > 0)) {
                    flig.stringField(0, " VOLS");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.vols);
                }
                if ((params.frou != null) && (params.frou.length > 0)) {
                    flig.stringField(0, " FROU");
                    flig.writeFields(fmt4);
                    LIGWriteTable(flig, params.frou);
                }
                flig.stringField(0, " FIN");
                flig.writeFields(fmt4);
                flig.close();
            } else {
                System.out.println("Fichier " + _fichier.getName() + " vide");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("IT: " + ex);
        }
    }

    private static void LIGWriteTable(FortranWriter flig, double[] t) throws IOException {
        int l = t.length;
        int nbColEntiere = l / 5;
        int nbColIsole = l % 5;
        int[] fmt5;
        if (nbColEntiere > 0) {
            fmt5 = new int[] { 10, 10, 10, 10, 10 };
            for (int i = 0; i < nbColEntiere; i++) {
                for (int j = 4; j >= 0; j--) {
                    flig.doubleField(j, t[i * 5 + j]);
                }
                flig.writeFields(fmt5);
            }
        }
        if (nbColIsole > 0) {
            fmt5 = new int[nbColIsole];
            for (int i = nbColIsole - 1; i >= 0; i--) {
                fmt5[i] = 10;
                flig.doubleField(i, t[l - nbColIsole + i]);
            }
            flig.writeFields(fmt5);
        }
    }

    public static void ecritParametresPRO(File fichier, SParametresPRO params, SParametresRZO rzo) {
        try {
            FortranWriter fpro = new FortranWriter(new FileWriter(fichier));
            String separation = new String("------------------------------------------------------------------------");
            if (params.status) {
                System.out.println("Creation du fichier " + fichier.getName());
                int[] fmt = new int[] { 72 };
                for (int i = 0; i < 6; i++) {
                    fpro.stringField(0, params.titrePRO[i]);
                    fpro.writeFields(fmt);
                }
                fpro.stringField(0, "ENTREE DES PROFILS (PAR POINTS = 1 , PAR LARGEURS = 2 ==>");
                fpro.intField(1, params.entreeProfils);
                fpro.writeFields();
                fpro.stringField(0, "ZONES DE STOCKAGE  (OUI = 1  ,  NON = 0)              ==>");
                fpro.intField(1, params.zoneStock);
                fpro.writeFields();
                fpro.stringField(0, "NOMBRE DE PROFILS                                     ==>");
                fpro.intField(1, params.nbProfils);
                fpro.writeFields();
                fpro.stringField(0, separation);
                fpro.writeFields(fmt);
                for (int i = 0; i < params.nbProfils; i++) {
                    fpro.stringField(0, params.profilsBief[i].numProfil);
                    fpro.stringField(1, "ABCS. DX==>");
                    fpro.doubleField(2, params.profilsBief[i].abscisse);
                    fpro.writeFields();
                    fpro.stringField(0, "J=  " + Integer.toString(i + 1) + "   LIT MINEUR : LIMITES  ==>");
                    if (params.entreeProfils == 1) {
                        fpro.doubleField(1, params.profilsBief[i].absMajMin[0]);
                        fpro.doubleField(2, params.profilsBief[i].absMajMin[1]);
                        fpro.stringField(3, "STRICKLER ==>");
                        fpro.doubleField(4, params.profilsBief[i].coefStrickMajMin);
                        fpro.writeFields();
                        fpro.stringField(0, "        LIT MAJEUR : LIMITES  ==>");
                        fpro.doubleField(1, params.profilsBief[i].absMajSto[0]);
                        fpro.doubleField(2, params.profilsBief[i].absMajSto[1]);
                        fpro.stringField(3, "STRICKLER ==>");
                        fpro.doubleField(4, params.profilsBief[i].coefStrickMajSto);
                        fpro.writeFields();
                    } else {
                        fpro.doubleField(1, params.profilsBief[i].altMinMaj[0]);
                        fpro.doubleField(2, params.profilsBief[i].altMinMaj[1]);
                        fpro.stringField(3, "STRICKLER ==>");
                        fpro.doubleField(4, params.profilsBief[i].coefStrickMajMin);
                        fpro.writeFields();
                        fpro.stringField(0, "        LIT MAJEUR : LIMITES  ==>");
                        fpro.doubleField(1, params.profilsBief[i].altMajSto[0]);
                        fpro.doubleField(2, params.profilsBief[i].altMajSto[1]);
                        fpro.stringField(3, "STRICKLER ==>");
                        fpro.doubleField(4, params.profilsBief[i].coefStrickMajSto);
                        fpro.writeFields();
                    }
                    fpro.stringField(0, "        COTE DES RIVES RG,RD  ==>");
                    fpro.doubleField(1, params.profilsBief[i].coteRivGa);
                    fpro.doubleField(2, params.profilsBief[i].coteRivDr);
                    fpro.writeFields();
                    fpro.stringField(0, "NOMBRE DE POINTS      ==>");
                    fpro.intField(1, params.profilsBief[i].nbPoints);
                    fpro.writeFields();
                    fpro.stringField(0, "ABSCISSES DES POINTS (DXP) ===>");
                    fpro.writeFields();
                    int cmpt = 0;
                    for (int j = 0; j < params.profilsBief[i].nbPoints; j++) {
                        if (cmpt > 9) {
                            fpro.writeFields();
                            cmpt = 0;
                        }
                        fpro.doubleField(cmpt++, params.profilsBief[i].abs[j]);
                    }
                    fpro.writeFields();
                    fpro.stringField(0, "COTES DES POINTS     (DYP) ===>");
                    fpro.writeFields();
                    cmpt = 0;
                    for (int j = 0; j < params.profilsBief[i].nbPoints; j++) {
                        if (cmpt > 9) {
                            fpro.writeFields();
                            cmpt = 0;
                        }
                        fpro.doubleField(cmpt++, params.profilsBief[i].cotes[j]);
                    }
                    fpro.writeFields();
                    fpro.stringField(0, separation);
                    fpro.writeFields(fmt);
                }
                if ((params.nbProfils > 0) && (rzo != null) && (rzo.nbBief > 0)) {
                    for (int i = 0; i < rzo.nbBief - 1; i++) {
                        SParametresBiefBlocPRO premierProfilAmont = getProfilByAbscisse(params, rzo.blocSitus.ligneSitu[i + 1].x1);
                        fpro.stringField(0, "JDBIEF==>");
                        fpro.intField(1, premierProfilAmont.indice + 1);
                        fpro.stringField(2, "DBIEF==>");
                        fpro.doubleField(3, rzo.blocSitus.ligneSitu[i + 1].x1 - rzo.blocSitus.ligneSitu[i].x2);
                        fpro.writeFields();
                    }
                    fpro.stringField(0, "JDBIEF==>");
                    fpro.intField(1, params.nbProfils + 1);
                    fpro.stringField(2, "DBIEF==>");
                    fpro.doubleField(3, 0.);
                    fpro.writeFields();
                }
                fpro.stringField(0, "**********FIN DU FICHIER GEOMETRIE**************************************");
                fpro.writeFields(fmt);
            } else {
                System.out.println("Fichier " + fichier.getName() + " vide");
                params.status = false;
            }
            fpro.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("IT: " + ex);
        }
        ;
    }

    private static SParametresBiefBlocPRO getProfilByAbscisse(SParametresPRO pro, double abscisse) {
        for (int i = 0; i < pro.nbProfils; i++) {
            if ((i > 0) && (pro.profilsBief[i].abscisse > abscisse)) return pro.profilsBief[i - 1];
        }
        return null;
    }

    public static void ecritParametresRZO(File fichier, SParametresRZO params) {
        try {
            FortranWriter frzo = new FortranWriter(new FileWriter(fichier));
            if (params.status) {
                System.out.println("Creation du fichier " + fichier.getName());
                int[] fmt = new int[] { 72 };
                String separation = new String("------------------------------------------------------------------------");
                for (int i = 0; i < 2; i++) {
                    frzo.stringField(0, params.titreRZO[i]);
                    frzo.writeFields(fmt);
                }
                frzo.stringField(0, separation);
                frzo.writeFields(fmt);
                frzo.stringField(0, "NBBIEF = ");
                frzo.intField(1, params.nbBief);
                frzo.stringField(2, "NBNOEU = ");
                frzo.intField(3, params.nbNoeud);
                frzo.stringField(4, "NBLIMI = ");
                frzo.intField(5, params.nbLimi);
                frzo.writeFields();
                frzo.stringField(0, separation);
                frzo.writeFields(fmt);
                for (int i = 0; i < params.nbBief; i++) {
                    frzo.stringField(0, "BIEF NUMERO " + (params.blocSitus.ligneSitu[i].numBief + 1) + ":  X1=");
                    frzo.doubleField(1, params.blocSitus.ligneSitu[i].x1);
                    frzo.stringField(2, "X2=");
                    frzo.doubleField(3, params.blocSitus.ligneSitu[i].x2);
                    frzo.stringField(4, "BRANCH==>");
                    frzo.intField(5, params.blocSitus.ligneSitu[i].branch1);
                    frzo.intField(6, params.blocSitus.ligneSitu[i].branch2);
                    frzo.writeFields();
                }
                frzo.stringField(0, separation);
                frzo.writeFields(fmt);
                for (int i = 0; i < params.nbNoeud; i++) {
                    frzo.stringField(0, "NOEUD" + Integer.toString(i + 1) + "==>");
                    for (int j = 0; j < 5; j++) {
                        frzo.intField(j + 1, params.blocNoeuds.ligneNoeud[i].noeud[j]);
                    }
                    frzo.writeFields();
                }
                frzo.stringField(0, separation);
                frzo.writeFields(fmt);
                frzo.stringField(0, "NLIM   numero des limites libres     =>");
                for (int i = 0; i < params.nbLimi; i++) {
                    frzo.intField(i + 1, params.blocLims.ligneLim[i].numExtBief);
                }
                frzo.writeFields();
                frzo.stringField(0, "NHYLIM numero des conditions limites =>");
                for (int i = 0; i < params.nbLimi; i++) {
                    frzo.intField(i + 1, params.blocLims.ligneLim[i].numLoi);
                }
                frzo.writeFields();
                frzo.stringField(0, "KHYLIM type des lois                 =>");
                for (int i = 0; i < params.nbLimi; i++) {
                    frzo.intField(i + 1, (params.blocLims.ligneLim[i].typLoi == 4 ? 2 : params.blocLims.ligneLim[i].typLoi));
                }
                frzo.writeFields();
                frzo.stringField(0, separation);
                frzo.writeFields(fmt);
                for (int i = 0; i < params.nbBief; i++) {
                    frzo.stringField(0, "BIEF NUMERO " + (params.blocSings.ligneSing[i].numBief + 1) + ":   XSEUI ==>");
                    frzo.doubleField(1, params.blocSings.ligneSing[i].xSing);
                    frzo.stringField(2, "NSEUI ==>");
                    frzo.intField(3, params.blocSings.ligneSing[i].nSing);
                    frzo.writeFields();
                }
                frzo.stringField(0, "FIN DU FICHIER ********************************************************");
                frzo.writeFields(fmt);
            } else {
                System.out.println("Fichier " + fichier.getName() + " vide");
            }
            frzo.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("IT: " + ex);
        }
        ;
    }

    public static void ecritParametresSNG(File fichier, SParametresSNG params) {
        try {
            FortranWriter fsng = new FortranWriter(new FileWriter(fichier));
            if (params.status) {
                System.out.println("Creation du fichier " + fichier.getName());
                fsng.stringField(0, params.titre);
                fsng.writeFields();
                fsng.stringField(0, "  NBSING =");
                fsng.intField(1, params.nbSing);
                fsng.stringField(2, "  NS1 =   ");
                fsng.intField(3, params.nbMaxParam);
                fsng.stringField(4, "  NS2 =   ");
                fsng.intField(5, params.nbValTD);
                fsng.writeFields();
                for (int i = 0; i < params.nbSing; i++) {
                    fsng.stringField(0, "------------------------------ KTYPE = " + params.singularites[i].tabParamEntier[0] + " -------------------------------");
                    fsng.writeFields();
                    fsng.stringField(0, params.singularites[i].titre);
                    fsng.writeFields();
                    fsng.stringField(0, " K = ");
                    fsng.intField(1, params.singularites[i].numSing);
                    fsng.writeFields();
                    fsng.stringField(0, "IDSING =");
                    for (int j = 0; j < params.nbMaxParam; j++) {
                        fsng.intField(j + 1, params.singularites[i].tabParamEntier[j]);
                    }
                    fsng.writeFields();
                    fsng.stringField(0, "RDSING =");
                    for (int j = 0; j < params.nbMaxParam; j++) {
                        fsng.doubleField(j + 1, params.singularites[i].tabParamReel[j]);
                    }
                    fsng.writeFields();
                    if (params.singularites[i].ns3 > 0) {
                        for (int j = 0; j < params.singularites[i].ns3; j++) {
                            int l = 0;
                            fsng.stringField(0, "LIGNE " + (j + 1));
                            fsng.writeFields();
                            for (int k = 0; k < params.nbValTD; k++) {
                                fsng.doubleField(l++, params.singularites[i].tabDonLois[j][k]);
                                if (l >= Math.min(5, params.nbValTD)) {
                                    fsng.writeFields();
                                    l = 0;
                                }
                            }
                        }
                    }
                }
                fsng.stringField(0, "------------------------------------------------------------------------");
                fsng.writeFields();
                fsng.stringField(0, " FIN DU FICHIER ********************************************************");
                fsng.writeFields();
            } else {
                System.out.println("Fichier " + fichier.getName() + " vide");
            }
            fsng.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("IT: " + ex);
        }
    }

    public static SParametresCAL litParametresCAL(File fichier) throws IOException {
        SParametresCAL params = new SParametresCAL();
        params.genCal = new SParametresGenCAL();
        params.fic = new SParametresFicCAL();
        params.sections = new SParametresSectionsCAL();
        params.sections.series = new SParametresSerieBlocCAL();
        params.sections.series.ligne = new SParametresSerieLigneCAL[0];
        params.sections.sections = new SParametresSectionBlocCAL();
        params.sections.sections.ligne = new SParametresSectionLigneCAL[0];
        params.genCal.code = new String();
        params.genCal.regime = new String();
        params.genCal.seuil = new String();
        params.genCal.compLits = new String();
        params.genCal.frotPV = new String();
        params.genCal.impGeo = new String();
        params.genCal.impPlan = new String();
        params.genCal.impRez = new String();
        params.genCal.impHyd = new String();
        params.planimetrage = new SParametresPlaniCAL();
        params.planimetrage.varsPlanimetrage = new SParametresPasLigneCAL[0];
        params.temporel = new SParametresTempCAL();
        FortranReader fcal = new FortranReader(new FileReader(fichier));
        if (fcal.ready()) {
            params.status = true;
            System.out.println("Lecture du fichier " + fichier.getName());
            String commentaire = new String();
            int[] fmt = new int[] { 72 };
            params.titreCAL = new String[2];
            for (int i = 0; i < 2; i++) {
                fcal.readFields(fmt);
                params.titreCAL[i] = fcal.stringField(0);
            }
            do {
                fcal.readFields();
                commentaire = fcal.stringField(0);
            } while (commentaire.charAt(0) == '#');
            params.genCal.code = fcal.stringField(1).trim();
            params.genCal.regime = fcal.stringField(3).trim();
            params.genCal.seuil = fcal.stringField(5).trim();
            do {
                fcal.readFields();
                commentaire = fcal.stringField(0);
            } while (commentaire.charAt(0) == '#');
            params.genCal.compLits = fcal.stringField(1).trim();
            do {
                fcal.readFields();
                commentaire = fcal.stringField(0);
            } while (commentaire.charAt(0) == '#');
            params.genCal.frotPV = fcal.stringField(1).trim();
            do {
                fcal.readFields();
                commentaire = fcal.stringField(0);
            } while (commentaire.charAt(0) == '#');
            params.genCal.impGeo = fcal.stringField(1).trim();
            fcal.readFields();
            params.genCal.impPlan = fcal.stringField(1).trim();
            fcal.readFields();
            params.genCal.impRez = fcal.stringField(1).trim();
            fcal.readFields();
            params.genCal.impHyd = fcal.stringField(1).trim();
            do {
                fcal.readFields();
                commentaire = fcal.stringField(0);
            } while (commentaire.charAt(0) == '#');
            params.fic.nFGeo = fcal.intField(1);
            fcal.readFields();
            params.fic.nFSing = fcal.intField(1);
            fcal.readFields();
            params.fic.nFRez = fcal.intField(1);
            fcal.readFields();
            params.fic.nFLign = fcal.intField(1);
            fcal.readFields();
            params.fic.nFCLim = fcal.intField(1);
            fcal.readFields();
            params.fic.nFSLec = fcal.intField(1);
            fcal.readFields();
            params.fic.nFSEcr = fcal.intField(1);
            do {
                fcal.readFields();
                commentaire = fcal.stringField(0);
            } while (commentaire.charAt(0) == '#');
            params.genCal.biefXOrigi = fcal.doubleField(1);
            params.genCal.biefXFin = fcal.doubleField(3);
            do {
                fcal.readFields();
                commentaire = fcal.stringField(0);
            } while (commentaire.charAt(0) == '#');
            params.planimetrage.varPlanNbVal = fcal.intField(1);
            params.planimetrage.varPlanNbPas = fcal.intField(3);
            int j = 0;
            params.planimetrage.varsPlanimetrage = new SParametresPasLigneCAL[params.planimetrage.varPlanNbPas];
            for (int i = 0; i < params.planimetrage.varPlanNbPas; i++) {
                j = i + 1;
                params.planimetrage.varsPlanimetrage[i] = new SParametresPasLigneCAL();
                fcal.readFields();
                params.planimetrage.varsPlanimetrage[i].taillePas = fcal.doubleField(1);
                params.planimetrage.varsPlanimetrage[i].profilDebut = fcal.intField(3);
                params.planimetrage.varsPlanimetrage[i].profilFin = fcal.intField(5);
            }
            do {
                fcal.readFields();
                commentaire = fcal.stringField(0);
            } while (commentaire.charAt(0) == '#');
            params.temporel.tInit = fcal.doubleField(1);
            fcal.readFields();
            params.temporel.tMax = fcal.doubleField(1);
            fcal.readFields();
            params.temporel.pas2T = fcal.doubleField(1);
            fcal.readFields();
            params.temporel.numDerPaStoc = fcal.intField(1);
            fcal.readFields();
            params.temporel.pas2TImp = fcal.doubleField(1);
            fcal.readFields();
            params.temporel.pas2TStoc = params.temporel.pas2TImp;
            do {
                fcal.readFields();
                commentaire = fcal.stringField(0);
            } while (commentaire.charAt(0) == '#');
            params.sections.nChoix = fcal.intField(1);
            j = 0;
            if (params.sections.nChoix == 2) {
                params.sections.series = new SParametresSerieBlocCAL();
                fcal.readFields();
                params.sections.series.nbSeries = fcal.intField(1);
                params.sections.series.ligne = new SParametresSerieLigneCAL[params.sections.series.nbSeries];
                for (int i = 0; i < params.sections.series.nbSeries; i++) {
                    params.sections.series.ligne[i] = new SParametresSerieLigneCAL();
                    fcal.readFields();
                    params.sections.series.ligne[i].absDebBief = fcal.doubleField(1);
                    params.sections.series.ligne[i].absFinBief = fcal.doubleField(3);
                    params.sections.series.ligne[i].nbSectCalc = fcal.intField(5);
                }
            } else {
                if (params.sections.nChoix == 3) {
                    params.sections.sections = new SParametresSectionBlocCAL();
                    fcal.readFields();
                    params.sections.sections.nbSect = fcal.intField(1);
                    fcal.readFields();
                    fcal.readFields();
                    params.sections.sections.ligne = new SParametresSectionLigneCAL[params.sections.sections.nbSect];
                    j = 5;
                    for (int i = 0; i < params.sections.sections.nbSect; i++) {
                        if (j > 4) {
                            fcal.readFields();
                            j = 0;
                        }
                        params.sections.sections.ligne[i] = new SParametresSectionLigneCAL();
                        params.sections.sections.ligne[i].absSect = fcal.doubleField(j++);
                    }
                }
            }
        } else {
            System.out.println("Fichier " + fichier.getName() + " vide !");
            params.status = false;
        }
        fcal.close();
        return params;
    }

    public static SParametresCLM litParametresCLM(File fichier) throws IOException {
        SParametresCLM params = new SParametresCLM();
        params.condLimites = new SParametresCondLimBlocCLM[0];
        params.titreCLM = new String();
        params.sousTitrePerte = new String();
        params.perte = new SParametresPerteLigneCLM[0];
        params.apport = new SParametresApportLigneCLM[0];
        params.sousTitreApport = new String();
        FortranReader fclm = new FortranReader(new FileReader(fichier));
        if (fclm.ready()) {
            params.status = true;
            System.out.println("Lecture du fichier " + fichier.getName());
            int[] fmt = new int[] { 72 };
            fclm.readFields(fmt);
            params.titreCLM = new String(fclm.stringField(0));
            fclm.readFields();
            params.nbCondLim = fclm.intField(1);
            params.condLimites = new SParametresCondLimBlocCLM[params.nbCondLim];
            for (int i = 0; i < params.nbCondLim; i++) {
                params.condLimites[i] = new SParametresCondLimBlocCLM();
                fclm.readFields(fmt);
                fclm.readFields();
                params.condLimites[i].description = new String();
                params.condLimites[i].numCondLim = fclm.intField(1);
                params.condLimites[i].description = fclm.stringField(2);
                fclm.readFields();
                params.condLimites[i].nbPoints = fclm.intField(1);
                params.condLimites[i].point = new SParametresCondLimPointLigneCLM[params.condLimites[i].nbPoints];
                boolean hasT = false;
                boolean hasQ = false;
                boolean hasZ = false;
                for (int j = 0; j < params.condLimites[i].nbPoints; j++) {
                    params.condLimites[i].point[j] = new SParametresCondLimPointLigneCLM();
                    fclm.readFields();
                    params.condLimites[i].point[j].tLim = fclm.doubleField(1);
                    if (params.condLimites[i].point[j].tLim != 0.) hasT = true;
                    params.condLimites[i].point[j].zLim = fclm.doubleField(3);
                    if (params.condLimites[i].point[j].zLim != 0.) hasZ = true;
                    params.condLimites[i].point[j].qLim = fclm.doubleField(5);
                    if (params.condLimites[i].point[j].qLim != 0.) hasQ = true;
                }
                if ((!hasT) && (!hasZ) && (!hasQ)) params.condLimites[i].typLoi = 0;
                if (!hasT) params.condLimites[i].typLoi = 3; else if (!hasZ) params.condLimites[i].typLoi = 1; else if (!hasQ) params.condLimites[i].typLoi = 2;
            }
            fclm.readFields(fmt);
            fclm.readFields(fmt);
            params.sousTitrePerte = new String(fclm.stringField(0));
            fclm.readFields();
            params.nbPerte = fclm.intField(1);
            params.perte = new SParametresPerteLigneCLM[params.nbPerte];
            for (int i = 0; i < params.nbPerte; i++) {
                params.perte[i] = new SParametresPerteLigneCLM();
                fclm.readFields();
                params.perte[i].xPerte = fclm.doubleField(1);
                params.perte[i].coefPerte = fclm.doubleField(3);
            }
            fclm.readFields(fmt);
            fclm.readFields(fmt);
            params.sousTitreApport = fclm.stringField(0);
            fclm.readFields();
            params.nbApport = fclm.intField(1);
            params.apport = new SParametresApportLigneCLM[params.nbApport];
            for (int i = 0; i < params.nbApport; i++) {
                params.apport[i] = new SParametresApportLigneCLM();
                fclm.readFields();
                params.apport[i].xApport = fclm.doubleField(1);
                params.apport[i].numLoi = fclm.intField(3);
                params.apport[i].typLoi = fclm.intField(5);
                params.apport[i].coefApport = fclm.doubleField(7);
            }
        } else {
            System.out.println("Fichier " + fichier.getName() + " vide !");
            params.status = false;
        }
        fclm.close();
        return params;
    }

    public static SParametresFIC creeParametresFIC(int n) {
        SParametresFIC paramsFIC = new SParametresFIC();
        paramsFIC.status = true;
        paramsFIC.racineFicPro = "lido" + n;
        paramsFIC.racineFicEtu = "lido" + n;
        paramsFIC.ficLectArchi = "";
        paramsFIC.ficEcriArchi = "";
        return paramsFIC;
    }

    public static SParametresFIC litParametresFIC(File fichier) throws IOException {
        SParametresFIC params = new SParametresFIC();
        params.racineFicPro = new String();
        params.racineFicEtu = new String();
        params.ficLectArchi = new String();
        params.ficEcriArchi = new String();
        FortranReader ffic = new FortranReader(new FileReader(fichier));
        if (ffic.ready()) {
            params.status = true;
            System.out.println("Lecture du fichier " + fichier.getName());
            int[] fmt = new int[] { 40, 20 };
            ffic.readFields(fmt);
            params.racineFicPro = ffic.stringField(1);
            ffic.readFields(fmt);
            params.racineFicEtu = ffic.stringField(1);
            ffic.readFields(fmt);
            params.ficLectArchi = ffic.stringField(1);
            ffic.readFields(fmt);
            params.ficEcriArchi = ffic.stringField(1);
            ffic.readFields(fmt);
        } else {
            System.out.println("Fichier " + fichier.getName() + " vide !");
            params.status = false;
        }
        ffic.close();
        return params;
    }

    public static SParametresLIG litParametresLIG(File fichier) throws IOException {
        SParametresLIG params = new SParametresLIG();
        params.titreLIG = new String[0];
        params.delimitBief = new SParametresLimBiefLIG[0];
        params.x = new double[0];
        params.z = new double[0];
        params.zref = new double[0];
        params.q = new double[0];
        params.vmin = new double[0];
        params.vmaj = new double[0];
        params.rgc = new double[0];
        params.rdc = new double[0];
        params.st1 = new double[0];
        params.st2 = new double[0];
        params.rmin = new double[0];
        params.rmaj = new double[0];
        params.vol = new double[0];
        params.vols = new double[0];
        params.frou = new double[0];
        FortranReader flig = new FortranReader(new FileReader(fichier));
        if (flig.ready()) {
            params.status = true;
            System.out.println("Lecture du fichier " + fichier.getName());
            int[] fmt1 = new int[] { 72 };
            int[] fmt2 = new int[] { 8, 5, 8, 5 };
            int[] fmt3;
            params.titreLIG = new String[3];
            for (int i = 0; i < 3; i++) {
                params.titreLIG[i] = new String();
                flig.readFields(fmt1);
                params.titreLIG[i] = flig.stringField(0);
            }
            flig.readFields(fmt2);
            params.nbSections = flig.intField(1);
            params.nbBief = flig.intField(3);
            int cmpt = 11;
            params.delimitBief = new SParametresLimBiefLIG[params.nbBief];
            for (int j = 0; j < params.nbBief; j++) {
                if (cmpt > 10) {
                    if (params.nbBief - j < 5) {
                        fmt3 = new int[2 * (params.nbBief - j) + 1];
                        fmt3[0] = 8;
                        for (int k = 1; k <= 2 * (params.nbBief - j); k++) {
                            fmt3[k] = 5;
                        }
                    } else {
                        fmt3 = new int[] { 8, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
                    }
                    flig.readFields(fmt3);
                    cmpt = 1;
                }
                params.delimitBief[j] = new SParametresLimBiefLIG();
                params.delimitBief[j].sectionDebut = flig.intField(cmpt++);
                params.delimitBief[j].sectionFin = flig.intField(cmpt++);
            }
            while (flig.ready()) {
                flig.readFields();
                String var = flig.stringField(0);
                if (DEBUG) System.err.println("lecture de " + var);
                if ("X".equals(var)) {
                    params.x = new double[params.nbSections];
                    LIGReadTable(flig, params.x);
                } else if ("ZREF".equals(var)) {
                    params.zref = new double[params.nbSections];
                    LIGReadTable(flig, params.zref);
                } else if ("Z".equals(var)) {
                    params.z = new double[params.nbSections];
                    LIGReadTable(flig, params.z);
                } else if ("Q".equals(var)) {
                    params.q = new double[params.nbSections];
                    LIGReadTable(flig, params.q);
                } else if ("VMIN".equals(var)) {
                    params.vmin = new double[params.nbSections];
                    LIGReadTable(flig, params.vmin);
                } else if ("VMAJ".equals(var)) {
                    params.vmaj = new double[params.nbSections];
                    LIGReadTable(flig, params.vmaj);
                } else if ("RGC".equals(var)) {
                    params.rgc = new double[params.nbSections];
                    LIGReadTable(flig, params.rgc);
                } else if ("RDC".equals(var)) {
                    params.rdc = new double[params.nbSections];
                    LIGReadTable(flig, params.rdc);
                } else if ("ST1".equals(var)) {
                    params.st1 = new double[params.nbSections];
                    LIGReadTable(flig, params.st1);
                } else if ("ST2".equals(var)) {
                    params.st2 = new double[params.nbSections];
                    LIGReadTable(flig, params.st2);
                } else if ("RMIN".equals(var)) {
                    params.rmin = new double[params.nbSections];
                    LIGReadTable(flig, params.rmin);
                } else if ("RMAJ".equals(var)) {
                    params.rmaj = new double[params.nbSections];
                    LIGReadTable(flig, params.rmaj);
                } else if ("VOL".equals(var)) {
                    params.vol = new double[params.nbSections];
                    LIGReadTable(flig, params.vol);
                } else if ("VOLS".equals(var)) {
                    params.vols = new double[params.nbSections];
                    LIGReadTable(flig, params.vols);
                } else if ("FROU".equals(var)) {
                    params.frou = new double[params.nbSections];
                    LIGReadTable(flig, params.frou);
                } else if ("FIN".equals(var)) {
                    break;
                } else if (var == null || "".equals(var)) {
                    continue;
                } else {
                    throw new IOException("variable inconnue: " + var + " (ligne " + flig.getLineNumber() + ")");
                }
            }
        } else {
            params.status = false;
            System.out.println("Fichier " + fichier.getName() + " vide !");
        }
        flig.close();
        return params;
    }

    private static void LIGReadTable(FortranReader flig, double[] t) throws IOException {
        int cmpt = 5;
        int[] fmt5;
        for (int k = 0; k < t.length; k++) {
            if (cmpt > 4) {
                if (t.length - k < 5) {
                    fmt5 = new int[(t.length - k)];
                    for (int l = 0; l < (t.length - k); l++) {
                        fmt5[l] = 10;
                    }
                } else {
                    fmt5 = new int[] { 10, 10, 10, 10, 10 };
                }
                flig.readFields(fmt5);
                cmpt = 0;
            }
            t[k] = flig.doubleField(cmpt++);
        }
    }

    public static SParametresRZO litParametresRZO(File fichier) throws IOException {
        SParametresRZO params = new SParametresRZO();
        params.titreRZO = new String[0];
        params.blocSitus = new SParametresBiefSituBlocRZO();
        params.blocSitus.ligneSitu = new SParametresBiefSituLigneRZO[0];
        params.blocNoeuds = new SParametresBiefNoeudBlocRZO();
        params.blocNoeuds.ligneNoeud = new SParametresBiefNoeudLigneRZO[0];
        params.blocLims = new SParametresBiefLimBlocRZO();
        params.blocLims.ligneLim = new SParametresBiefLimLigneRZO[0];
        params.blocSings = new SParametresBiefSingBlocRZO();
        params.blocSings.ligneSing = new SParametresBiefSingLigneRZO[0];
        FortranReader frzo = new FortranReader(new FileReader(fichier));
        if (frzo.ready()) {
            params.status = true;
            System.out.println("Lecture du fichier " + fichier.getName());
            int[] fmt = new int[] { 72 };
            params.titreRZO = new String[2];
            for (int i = 0; i < 2; i++) {
                frzo.readFields(fmt);
                params.titreRZO[i] = frzo.stringField(0);
            }
            frzo.readFields(fmt);
            frzo.readFields();
            params.nbBief = frzo.intField(1);
            params.nbNoeud = frzo.intField(3);
            params.nbLimi = frzo.intField(5);
            frzo.readFields(fmt);
            params.blocSitus = new SParametresBiefSituBlocRZO();
            params.blocSitus.ligneSitu = new SParametresBiefSituLigneRZO[params.nbBief];
            for (int i = 0; i < params.nbBief; i++) {
                params.blocSitus.ligneSitu[i] = new SParametresBiefSituLigneRZO();
                frzo.readFields();
                params.blocSitus.ligneSitu[i].numBief = i;
                params.blocSitus.ligneSitu[i].x1 = frzo.doubleField(1);
                params.blocSitus.ligneSitu[i].x2 = frzo.doubleField(3);
                params.blocSitus.ligneSitu[i].branch1 = frzo.intField(5);
                params.blocSitus.ligneSitu[i].branch2 = frzo.intField(6);
            }
            frzo.readFields(fmt);
            params.blocNoeuds = new SParametresBiefNoeudBlocRZO();
            params.blocNoeuds.ligneNoeud = new SParametresBiefNoeudLigneRZO[params.nbNoeud];
            for (int i = 0; i < params.nbNoeud; i++) {
                params.blocNoeuds.ligneNoeud[i] = new SParametresBiefNoeudLigneRZO();
                frzo.readFields();
                params.blocNoeuds.ligneNoeud[i].noeud = new int[5];
                for (int j = 0; j < 5; j++) {
                    params.blocNoeuds.ligneNoeud[i].noeud[j] = frzo.intField(j + 1);
                }
            }
            frzo.readFields(fmt);
            frzo.readFields();
            params.blocLims = new SParametresBiefLimBlocRZO();
            params.blocLims.ligneLim = new SParametresBiefLimLigneRZO[params.nbLimi];
            for (int i = 0; i < params.nbLimi; i++) {
                params.blocLims.ligneLim[i] = new SParametresBiefLimLigneRZO();
                params.blocLims.ligneLim[i].numExtBief = frzo.intField(i + 1);
            }
            frzo.readFields();
            for (int i = 0; i < params.nbLimi; i++) {
                params.blocLims.ligneLim[i].numLoi = frzo.intField(i + 1);
            }
            frzo.readFields();
            for (int i = 0; i < params.nbLimi; i++) {
                params.blocLims.ligneLim[i].typLoi = frzo.intField(i + 1);
            }
            frzo.readFields(fmt);
            frzo.getLine();
            params.blocSings = new SParametresBiefSingBlocRZO();
            params.blocSings.ligneSing = new SParametresBiefSingLigneRZO[params.nbBief];
            for (int i = 0; i < params.nbBief; i++) {
                params.blocSings.ligneSing[i] = new SParametresBiefSingLigneRZO();
                frzo.readFields();
                params.blocSings.ligneSing[i].numBief = i;
                params.blocSings.ligneSing[i].xSing = frzo.doubleField(1);
                params.blocSings.ligneSing[i].nSing = frzo.intField(3);
            }
        } else {
            System.out.println("Fichier " + fichier.getName() + " vide !");
            params.status = false;
        }
        frzo.close();
        return params;
    }

    public static SParametresSNG litParametresSNG(File fichier) throws IOException {
        SParametresSNG params = new SParametresSNG();
        params.singularites = new SParametresSingBlocSNG[0];
        params.titre = "";
        FortranReader fsng = new FortranReader(new FileReader(fichier));
        if (fsng.ready()) {
            params.status = true;
            System.out.println("Lecture du fichier " + fichier.getName());
            int[] fmt = new int[] { 72 };
            fsng.readFields(fmt);
            params.titre = fsng.stringField(0);
            fsng.readFields();
            params.nbSing = fsng.intField(1);
            if (DEBUG) System.err.println("nbSing " + params.nbSing);
            params.nbMaxParam = fsng.intField(3);
            if (DEBUG) System.err.println("nbMaxParam " + params.nbMaxParam);
            params.nbValTD = fsng.intField(5);
            if (DEBUG) System.err.println("nbValTD " + params.nbValTD);
            fsng.readFields();
            params.singularites = new SParametresSingBlocSNG[params.nbSing];
            for (int i = 0; i < params.nbSing; i++) {
                params.singularites[i] = new SParametresSingBlocSNG();
                params.singularites[i].tabParamEntier = new int[params.nbMaxParam];
                params.singularites[i].tabParamReel = new double[params.nbMaxParam];
                fsng.readFields();
                params.singularites[i].titre = fsng.stringField(0);
                if (DEBUG) System.err.println("titre " + params.singularites[i].titre);
                fsng.readFields();
                params.singularites[i].numSing = fsng.intField(1);
                if (DEBUG) System.err.println("numSing " + params.singularites[i].numSing);
                fsng.readFields();
                for (int j = 0; j < params.nbMaxParam; j++) {
                    params.singularites[i].tabParamEntier[j] = fsng.intField(j + 1);
                    if (DEBUG) System.err.print(params.singularites[i].tabParamEntier[j]);
                }
                if (DEBUG) System.err.println("");
                fsng.readFields();
                for (int j = 0; j < params.nbMaxParam; j++) {
                    params.singularites[i].tabParamReel[j] = fsng.doubleField(j + 1);
                    if (DEBUG) System.err.print(params.singularites[i].tabParamReel[j]);
                }
                if (DEBUG) System.err.println("");
                if (params.singularites[i].tabParamEntier[0] == 1) {
                    params.singularites[i].ns3 = 2 + params.singularites[i].tabParamEntier[2];
                } else {
                    if ((params.singularites[i].tabParamEntier[0] > 1) && (params.singularites[i].tabParamEntier[0] < 9) && (params.singularites[i].tabParamEntier[0] != 6)) {
                        params.singularites[i].ns3 = 2;
                    } else {
                        if ((params.singularites[i].tabParamEntier[0] == 9) || (params.singularites[i].tabParamEntier[0] == 6)) {
                            params.singularites[i].ns3 = 0;
                        }
                    }
                }
                fsng.readFields();
                if (params.singularites[i].ns3 > 0) {
                    params.singularites[i].tabDonLois = new double[params.singularites[i].ns3][params.nbValTD];
                    for (int j = 0; j < params.singularites[i].ns3; j++) {
                        int cmpt = 5;
                        for (int k = 0; k < params.nbValTD; k++) {
                            if (cmpt >= 5) {
                                fsng.readFields();
                                cmpt = 0;
                            }
                            params.singularites[i].tabDonLois[j][k] = fsng.doubleField(cmpt++);
                            if (DEBUG) System.err.print(params.singularites[i].tabDonLois[j][k]);
                        }
                        fsng.readFields();
                        if (DEBUG) System.err.println("");
                    }
                }
            }
        } else {
            System.out.println("Fichier " + fichier.getName() + " vide !");
            params.status = false;
        }
        fsng.close();
        return params;
    }
}
