package org.fudaa.dodico.mascaret;

import java.io.File;
import org.fudaa.dodico.corba.mascaret.SNoeud;
import org.fudaa.dodico.corba.mascaret.SParametresApporDeversoirs;
import org.fudaa.dodico.corba.mascaret.SParametresApportCasier;
import org.fudaa.dodico.corba.mascaret.SParametresBarrPrincip;
import org.fudaa.dodico.corba.mascaret.SParametresBiblio;
import org.fudaa.dodico.corba.mascaret.SParametresBranches;
import org.fudaa.dodico.corba.mascaret.SParametresCalage;
import org.fudaa.dodico.corba.mascaret.SParametresCalageAuto;
import org.fudaa.dodico.corba.mascaret.SParametresCasier;
import org.fudaa.dodico.corba.mascaret.SParametresConcInitTracer;
import org.fudaa.dodico.corba.mascaret.SParametresConcentrations;
import org.fudaa.dodico.corba.mascaret.SParametresCondInit;
import org.fudaa.dodico.corba.mascaret.SParametresCondLimTracer;
import org.fudaa.dodico.corba.mascaret.SParametresConfluent;
import org.fudaa.dodico.corba.mascaret.SParametresConfluents;
import org.fudaa.dodico.corba.mascaret.SParametresConvecDiffu;
import org.fudaa.dodico.corba.mascaret.SParametresCrueCalageAuto;
import org.fudaa.dodico.corba.mascaret.SParametresCruesCalageAuto;
import org.fudaa.dodico.corba.mascaret.SParametresDebordProgr;
import org.fudaa.dodico.corba.mascaret.SParametresDeversLateraux;
import org.fudaa.dodico.corba.mascaret.SParametresDeversoirsV5P2;
import org.fudaa.dodico.corba.mascaret.SParametresDonneesLoi;
import org.fudaa.dodico.corba.mascaret.SParametresExtrLibres;
import org.fudaa.dodico.corba.mascaret.SParametresFichReprise;
import org.fudaa.dodico.corba.mascaret.SParametresFichResCasier;
import org.fudaa.dodico.corba.mascaret.SParametresFrottement;
import org.fudaa.dodico.corba.mascaret.SParametresGen;
import org.fudaa.dodico.corba.mascaret.SParametresGeoReseau;
import org.fudaa.dodico.corba.mascaret.SParametresGeom;
import org.fudaa.dodico.corba.mascaret.SParametresImpress;
import org.fudaa.dodico.corba.mascaret.SParametresImpressResult;
import org.fudaa.dodico.corba.mascaret.SParametresImpressResultTracer;
import org.fudaa.dodico.corba.mascaret.SParametresLiaisons;
import org.fudaa.dodico.corba.mascaret.SParametresLigEau;
import org.fudaa.dodico.corba.mascaret.SParametresListing;
import org.fudaa.dodico.corba.mascaret.SParametresLoi;
import org.fudaa.dodico.corba.mascaret.SParametresLoiTracer;
import org.fudaa.dodico.corba.mascaret.SParametresLoisHydrau;
import org.fudaa.dodico.corba.mascaret.SParametresLoisTracer;
import org.fudaa.dodico.corba.mascaret.SParametresMaillage;
import org.fudaa.dodico.corba.mascaret.SParametresMaillageClavier;
import org.fudaa.dodico.corba.mascaret.SParametresModelPhy;
import org.fudaa.dodico.corba.mascaret.SParametresNoeuds;
import org.fudaa.dodico.corba.mascaret.SParametresNum;
import org.fudaa.dodico.corba.mascaret.SParametresNumCasier;
import org.fudaa.dodico.corba.mascaret.SParametresNumQualiteEau;
import org.fudaa.dodico.corba.mascaret.SParametresParamsCalageAuto;
import org.fudaa.dodico.corba.mascaret.SParametresPasStock;
import org.fudaa.dodico.corba.mascaret.SParametresPerteCharge;
import org.fudaa.dodico.corba.mascaret.SParametresPlanim;
import org.fudaa.dodico.corba.mascaret.SParametresPlanimMaillage;
import org.fudaa.dodico.corba.mascaret.SParametresQApport;
import org.fudaa.dodico.corba.mascaret.SParametresReprEtude;
import org.fudaa.dodico.corba.mascaret.SParametresResult;
import org.fudaa.dodico.corba.mascaret.SParametresRubens;
import org.fudaa.dodico.corba.mascaret.SParametresSeuil;
import org.fudaa.dodico.corba.mascaret.SParametresSingularite;
import org.fudaa.dodico.corba.mascaret.SParametresSourcesTraceurs;
import org.fudaa.dodico.corba.mascaret.SParametresStockage;
import org.fudaa.dodico.corba.mascaret.SParametresTemp;
import org.fudaa.dodico.corba.mascaret.SParametresTraceur;
import org.fudaa.dodico.corba.mascaret.SParametresVarCalc;
import org.fudaa.dodico.corba.mascaret.SParametresVarStock;
import org.fudaa.dodico.corba.mascaret.SParametresZoneSeche;
import org.fudaa.dodico.corba.mascaret.SParametresZoneStockage;
import org.fudaa.dodico.corba.mascaret.SParametresZonesCalageAuto;
import org.fudaa.dodico.dico.DicoCasFileFormatAbstract;
import org.fudaa.dodico.dico.DicoCasFileFormatDefault;
import org.fudaa.dodico.dico.DicoCasFileFormatVersionAbstract;
import org.fudaa.dodico.dico.DicoCasInterface;
import org.fudaa.dodico.dico.DicoCasReader;
import org.fudaa.dodico.dico.DicoDataType;
import org.fudaa.dodico.dico.DicoDynamiqueGenerator;
import org.fudaa.dodico.dico.DicoEntite;
import org.fudaa.dodico.dico.DicoLanguage;
import org.fudaa.dodico.dico.DicoModelAbstract;
import org.fudaa.dodico.dico.DicoParams;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.ctulu.ProgressionInterface;

/**
 * Lecture d'un fichier cas, avec le fichier dico associ�. Cette lecture s'appuie sur le package
 * org.fudaa.dodico.dico.
 * 
 * Cette classe n'est pas directement parall�le � EdamoxWriter, qui fonctionne sur un principe de
 * reflexion.
 * @author bmarchan
 */
public class EdamoxReader {

    DicoDynamiqueGenerator generator_;

    /** Le fichier cas */
    File fcas_;

    /** Le dictionnaire */
    File fdico_;

    /** Le mod�le dico contenant les mots cl�s avec leurs valeurs par defaut */
    DicoModelAbstract md_;

    /** Les param�tres avec leurs valeurs d�finies */
    DicoParams params_;

    /** La version du ficheir est-elle 5.2 */
    boolean bnoyau52_ = false;

    /**
   * Constructeur
   * @param _fcas Fichier cas.
   * @param _fdico Fichier dico associ�.
   * @param _bnoyau52 La lecture se fait suivant le format mascaret 5.2
   */
    public EdamoxReader(File _fcas, File _fdico, boolean _bnoyau52) {
        bnoyau52_ = _bnoyau52;
        fcas_ = _fcas;
        fdico_ = _fdico;
    }

    /**
   * Lit la totalit� du fichier, et stocke les infos dans les DicoEntite.
   * @exception FichierMascaretException En cas de pb de lecture du dictionnaire.
   *
   */
    public void load() throws FichierMascaretException {
        generator_ = DicoDynamiqueGenerator.loadGenerator(fdico_, new Progression());
        if (generator_.getResult().containsFatalError()) {
            String cause = generator_.getResult().getFatalError().getMessage();
            int numline = generator_.getResult().getFatalError().getOffset();
            throw new FichierMascaretException(numline, "DICO", cause);
        }
        md_ = generator_.buildModel(new Progression(), DicoLanguage.FRENCH_ID);
        DicoCasFileFormatAbstract ft = new DicoCasFileFormatDefault(md_);
        DicoCasReader reader = new DicoCasReader((DicoCasFileFormatVersionAbstract) ft.getLastVersionInstance(fcas_));
        reader.setFile(fcas_);
        reader.setProgressReceiver(new Progression());
        CtuluIOOperationSynthese s = reader.read();
        params_ = new DicoParams((DicoCasInterface) s.getSource(), (DicoCasFileFormatVersionAbstract) ft.getLastVersionInstance(fcas_));
    }

    /**
   * Lecture des param�tres g�n�raux
   */
    public SParametresGen litParametresGen() {
        SParametresGen params = new SParametresGen();
        params.versionCode = getEntier("VERSION DU CODE");
        params.code = getEntier("NOYAU DE CALCUL");
        params.fichMotsCles = getChaine("FICHIER DES MOT-CLES");
        params.dictionaire = getChaine("DICTIONNAIRE");
        params.progPrincipal = getChaine("PROGRAMME PRINCIPAL");
        params.sauveModele = getBooleen("SAUVEGARDE DU MODELE");
        params.fichSauvModele = getChaine("FICHIER SAUVEGARDE DU MODELE");
        params.validationCode = getBooleen("CALCUL POUR VALIDATION DU CODE");
        params.typeValidation = getEntier("TYPE DE CALCUL DE VALIDATION EFFECTUE");
        params.presenceCasiers = getBooleen("PRESENCE DE CASIERS");
        params.bibliotheques = new SParametresBiblio();
        params.bibliotheques.bibliotheques = getChaine("BIBLIOTHEQUES");
        return params;
    }

    /**
   * Lecture des param�tres du mod�le physique
   */
    public SParametresModelPhy litParametresModelPhys() {
        SParametresModelPhy params = new SParametresModelPhy();
        params.perteChargeConf = getBooleen("PERTES DE CHARGE AUTOMATIQUE AUX CONFLUENTS");
        params.compositionLits = getEntier("COMPOSITION DES LITS");
        params.conservFrotVertical = getBooleen("CONSERVATION DU FROTTEMENT SUR LES PAROIS VERTICALES");
        params.elevCoteArrivFront = getDouble("ELEVATION DE COTE ARRIVEE DU FRONT");
        params.interpolLinStrickler = getBooleen("INTERPOLATION LINEAIRE DES STRICKLER");
        params.debordement = new SParametresDebordProgr();
        params.debordement.litMajeur = getBooleen("DEBORDEMENT PROGRESSIF LIT MAJEUR");
        params.debordement.zoneStock = getBooleen("DEBORDEMENT PROGRESSIF ZONES DE STOCKAGE");
        return params;
    }

    /**
   * Lecture des param�tres num�riques
   * @param _casiers Si il y a presence de casiers.
   */
    public SParametresNum litParametresNum(boolean _casiers) {
        SParametresNum params = new SParametresNum();
        params.calcOndeSubmersion = getBooleen("CALCUL D'UNE ONDE DE SUBMERSION");
        params.froudeLimCondLim = getDouble("FROUDE LIMITE POUR LES CONDITIONS LIMITES");
        params.traitImplicitFrot = getBooleen("TRAITEMENT IMPLICITE DU FROTTEMENT");
        params.hauteurEauMini = getDouble("HAUTEUR D'EAU MINIMALE");
        params.implicitNoyauTrans = getBooleen("IMPLICITATION DU NOYAU TRANSCRITIQUE");
        if (bnoyau52_) params.optimisNoyauTrans = false; else params.optimisNoyauTrans = getBooleen("OPTIMISATION DU NOYAU TRANSCRITIQUE");
        params.perteChargeAutoElargissement = getBooleen("PERTES DE CHARGE AUTOMATIQUE NOYAU TRANSCRITIQUE");
        if (_casiers) {
            params.parametresNumeriqueCasier = new SParametresNumCasier();
            params.parametresNumeriqueCasier.coefImplicationSystemeCasiers = getDouble("COEFFICIENT D'IMPLICITATION DU SYSTEME DES CASIERS");
            params.parametresNumeriqueCasier.coefImplicationDansCouplage = getDouble("CASIERS COEFFICIENT D'IMPLICITATION DANS LE COUPLAGE");
            params.parametresNumeriqueCasier.nbMaxIterationDansCouplage = getEntier("CASIERS NOMBRE MAXIMUM D'ITERATIONS DANS LE COUPLAGE");
        }
        return params;
    }

    /**
   * Lecture des param�tres temporels
   */
    public SParametresTemp litParametresTemp() {
        SParametresTemp params = new SParametresTemp();
        params.pasTemps = getDouble("PAS DE TEMPS");
        params.tempsInit = getDouble("TEMPS INITIAL");
        params.critereArret = getEntier("CRITERE D'ARRET DU CALCUL");
        params.nbPasTemps = getEntier("NOMBRE DE PAS DE TEMPS");
        params.tempsMax = getDouble("TEMPS MAXIMUM");
        params.pasTempsVar = getBooleen("PAS DE TEMPS VARIABLE SUIVANT NOMBRE DE COURANT");
        params.nbCourant = getDouble("NOMBRE DE COURANT SOUHAITE");
        params.coteMax = getDouble("COTE MAXIMALE DE CONTROLE");
        params.abscisseControle = getDouble("POINT DE CONTROLE ABSCISSE");
        params.biefControle = getEntier("POINT DE CONTROLE BIEF ASSOCIE");
        return params;
    }

    /**
   * Lecture de la geometrie-reseau
   */
    public SParametresGeoReseau litParametresGeoReseau() {
        SParametresGeoReseau params = new SParametresGeoReseau();
        params.geometrie = new SParametresGeom();
        params.geometrie.fichier = getChaine("FICHIER DE GEOMETRIE");
        params.geometrie.format = getEntier("FORMAT DU FICHIER DE GEOMETRIE");
        params.geometrie.profilsAbscAbsolu = getBooleen("PROFILS EN ABSCISSE ABSOLUE");
        params.branches = new SParametresBranches();
        params.branches.nb = getEntier("NOMBRE DE BRANCHES");
        params.branches.numeros = getTabEntier("BRANCHE NUMERO", params.branches.nb);
        params.branches.abscDebut = getTabDouble("ABSCISSE DEBUT", params.branches.nb);
        params.branches.abscFin = getTabDouble("ABSCISSE FIN", params.branches.nb);
        params.branches.numExtremDebut = getTabEntier("NUM DE L'EXTREMITE DE DEBUT", params.branches.nb);
        params.branches.numExtremFin = getTabEntier("NUM DE L'EXTREMITE DE FIN", params.branches.nb);
        params.noeuds = new SParametresNoeuds();
        params.noeuds.nb = getEntier("NOMBRE DE NOEUDS");
        params.noeuds.noeuds = new SNoeud[params.noeuds.nb];
        for (int i = 0; i < params.noeuds.nb; i++) {
            params.noeuds.noeuds[i] = new SNoeud();
            params.noeuds.noeuds[i].num = getTabEntier("NOEUD " + (i + 1), 5);
        }
        params.extrLibres = new SParametresExtrLibres();
        params.extrLibres.nb = getEntier("NOMBRE D'EXTREMITES LIBRES");
        params.extrLibres.num = getTabEntier("EXTREMITE LIBRE NUMERO", params.extrLibres.nb);
        params.extrLibres.numExtrem = getTabEntier("EXTREMITE NUMERO", params.extrLibres.nb);
        params.extrLibres.Nom = getTabChaine("NOM EXTREMITE", params.extrLibres.nb);
        params.extrLibres.typeCond = getTabEntier("TYPE DE CONDITION", params.extrLibres.nb);
        params.extrLibres.numLoi = getTabEntier("NUMERO DE LA LOI", params.extrLibres.nb);
        return params;
    }

    /**
   * Lecture des confluents 
   */
    public SParametresConfluents litParametresConfluents() {
        SParametresConfluents params = new SParametresConfluents();
        params.nbConfluents = getEntier("NOMBRE DE CONFLUENTS");
        params.confluents = new SParametresConfluent[params.nbConfluents];
        for (int i = 0; i < params.nbConfluents; i++) {
            params.confluents[i] = new SParametresConfluent();
            params.confluents[i].nbAffluent = getEntier("NOMBRE D'AFFLUENTS DU CONFLUENT " + (i + 1));
            params.confluents[i].nom = getChaine("NOM DU CONFLUENT " + (i + 1));
            params.confluents[i].abscisses = getTabDouble("ABSCISSE DE L'AFFLUENT DU CONFLUENT " + (i + 1), params.confluents[i].nbAffluent);
            params.confluents[i].ordonnees = getTabDouble("ORDONNEE DE L'AFFLUENT DU CONFLUENT " + (i + 1), params.confluents[i].nbAffluent);
            params.confluents[i].angles = getTabDouble("ANGLE DE L'AFFLUENT DU CONFLUENT " + (i + 1), params.confluents[i].nbAffluent);
        }
        return params;
    }

    /**
   * Lecture du planimetrage et du maillage
   */
    public SParametresPlanimMaillage litParametresPlanimMaillage() {
        SParametresPlanimMaillage params = new SParametresPlanimMaillage();
        params.methodeMaillage = getEntier("METHODE DE CALCUL DU MAILLAGE");
        params.planim = new SParametresPlanim();
        params.planim.nbPas = getEntier("NOMBRE DE PAS DE PLANIMETRAGE");
        params.planim.nbZones = getEntier("NOMBRE DE ZONES DE PLANIMETRAGE");
        params.planim.valeursPas = getTabDouble("VALEUR DU PAS", params.planim.nbZones);
        params.planim.num1erProf = getTabEntier("NUMERO DU PREMIER PROFIL", params.planim.nbZones);
        params.planim.numDerProf = getTabEntier("NUMERO DU DERNIER PROFIL", params.planim.nbZones);
        params.maillage = new SParametresMaillage();
        params.maillage.modeSaisie = getEntier("MODE DE SAISIE DU MAILLAGE");
        params.maillage.fichMaillage = getChaine("FICHIER DU MAILLAGE");
        params.maillage.sauvMaillage = getBooleen("SAUVEGARDE MAILLAGE");
        params.maillage.fichSauvMaillage = getChaine("FICHIER DE SAUVEGARDE DU MAILLAGE");
        params.maillage.maillageClavier = new SParametresMaillageClavier();
        params.maillage.maillageClavier.nbSections = getEntier("NOMBRE DE SECTIONS DE CALCUL");
        params.maillage.maillageClavier.numSection = getTabEntier("NUMEROS DES SECTIONS DE CALCUL", params.maillage.maillageClavier.nbSections);
        params.maillage.maillageClavier.branchesSection = getTabEntier("BRANCHES DES SECTIONS DE CALCUL", params.maillage.maillageClavier.nbSections);
        params.maillage.maillageClavier.absSection = getTabDouble("ABSCISSES DES SECTIONS DE CALCUL", params.maillage.maillageClavier.nbSections);
        params.maillage.maillageClavier.nbPlages = getEntier("NOMBRE DE PLAGES DE DISCRETISATION");
        params.maillage.maillageClavier.num1erProfPlage = getTabEntier("NUMERO DU PREMIER PROFIL DE LA SERIE", params.maillage.maillageClavier.nbPlages);
        params.maillage.maillageClavier.numDerProfPlage = getTabEntier("NUMERO DU DERNIER PROFIL DE LA SERIE", params.maillage.maillageClavier.nbPlages);
        params.maillage.maillageClavier.pasEspacePlage = getTabDouble("PAS D'ESPACE DE LA SERIE", params.maillage.maillageClavier.nbPlages);
        params.maillage.maillageClavier.nbZones = getEntier("NOMBRE DE ZONES DE DISCRETISATION");
        params.maillage.maillageClavier.numBrancheZone = getTabEntier("NUMERO DE BRANCHE DE ZONE", params.maillage.maillageClavier.nbZones);
        params.maillage.maillageClavier.absDebutZone = getTabDouble("ABSCISSE DE DEBUT DE ZONE", params.maillage.maillageClavier.nbZones);
        params.maillage.maillageClavier.absFinZone = getTabDouble("ABSCISSE DE FIN DE ZONE", params.maillage.maillageClavier.nbZones);
        params.maillage.maillageClavier.nbSectionZone = getTabEntier("NOMBRE DE SECTIONS DE LA ZONE", params.maillage.maillageClavier.nbZones);
        return params;
    }

    /**
   * Lecture des singularites 
   */
    public SParametresSingularite litParametresSingularite() {
        SParametresSingularite params = new SParametresSingularite();
        params.nbSeuils = getEntier("NOMBRE DE SEUILS");
        params.barragePrincipal = new SParametresBarrPrincip();
        params.barragePrincipal.numBranche = getEntier("NUM BRANCHE DU BARRAGE PRINCIPAL");
        params.barragePrincipal.abscisse = getDouble("ABSCISSE DU BARRAGE PRINCIPAL");
        params.barragePrincipal.typeRupture = getEntier("TYPE DE RUPTURE DU BARRAGE PRINCIPAL");
        params.barragePrincipal.coteCrete = getDouble("COTE DE CRETE DU BARRAGE PRINCIPAL");
        if (params.barragePrincipal.numBranche == 0) params.barragePrincipal = null;
        params.seuils = new SParametresSeuil[params.nbSeuils];
        for (int i = 0; i < params.seuils.length; i++) {
            params.seuils[i] = new SParametresSeuil();
            params.seuils[i].nom = getChaine("NOM SEUIL " + (i + 1));
            params.seuils[i].type = getEntier("TYPE SEUIL " + (i + 1));
            params.seuils[i].numBranche = getEntier("NUM BRANCHE SEUIL " + (i + 1));
            params.seuils[i].abscisse = getDouble("ABSCISSE SEUIL " + (i + 1));
            params.seuils[i].coteCrete = getDouble("COTE CRETE SEUIL " + (i + 1));
            params.seuils[i].coteCreteMoy = getDouble("COTE MOYENNE CRETE " + (i + 1));
            params.seuils[i].coteRupture = getDouble("COTE RUPTURE SEUIL " + (i + 1));
            params.seuils[i].coeffDebit = getDouble("COEFF DEBIT SEUIL " + (i + 1));
            params.seuils[i].largVanne = getDouble("LARGEUR VANNE " + (i + 1));
            params.seuils[i].numLoi = getEntier("NUMERO LOI SEUIL " + (i + 1));
            params.seuils[i].nbPtLoiSeuil = getEntier("NOMBRE DE POINTS DE LA LOI SEUIL " + (i + 1));
            params.seuils[i].abscTravCrete = getTabDouble("ABSCISSE EN TRAVERS CRETE " + (i + 1), params.seuils[i].nbPtLoiSeuil);
            params.seuils[i].cotesCrete = getTabDouble("COTE CRETE " + (i + 1), params.seuils[i].nbPtLoiSeuil);
            params.seuils[i].epaisseur = getEntier("EPAISSEUR SEUIL " + (i + 1));
            params.seuils[i].gradient = getDouble("GRADIENT DE DESCENTE SEUIL " + (i + 1));
        }
        params.pertesCharges = new SParametresPerteCharge();
        params.pertesCharges.nbPerteCharge = getEntier("NOMBRE DE PERTES DE CHARGE SINGULIERES");
        params.pertesCharges.numBranche = getTabEntier("NUM BRANCHE DE LA PERTE DE CHARGE SINGULIERE", params.pertesCharges.nbPerteCharge);
        params.pertesCharges.abscisses = getTabDouble("ABSCISSE DE LA PERTE DE CHARGE SINGULIERE", params.pertesCharges.nbPerteCharge);
        params.pertesCharges.coefficients = getTabDouble("COEFFICIENT DE LA PERTE DE CHARGE SINGULIERE", params.pertesCharges.nbPerteCharge);
        return params;
    }

    /**
   * Lecture des param�tres casiers
   */
    public SParametresCasier litParametresCasier() {
        SParametresCasier params = new SParametresCasier();
        params.nbCasiers = getEntier("NOMBRE DE CASIERS");
        params.optionPlanimetrage = getTabEntier("CASIERS OPTION DE PLANIMETRAGE", params.nbCasiers);
        params.optionCalcul = getTabEntier("CASIERS OPTION DE CALCUL", params.nbCasiers);
        for (int i = 0; i < params.nbCasiers; i++) params.optionCalcul[i] = params.optionCalcul[0];
        params.fichierGeomCasiers = getChaine("CASIERS FICHIER GEOMETRIE");
        params.cotesInitiale = getTabDouble("CASIERS COTE INITIALE", params.nbCasiers);
        params.pasPlanimetrage = getTabDouble("CASIERS PAS DE PLANIMETRAGE", params.nbCasiers);
        params.nbCotesPlanimetrage = getTabEntier("CASIERS NOMBRE DE COTES DE PLANIMETRAGE", params.nbCasiers);
        params.liaisons = new SParametresLiaisons();
        params.liaisons.nbLiaisons = getEntier("NOMBRE DE LIAISONS");
        params.liaisons.types = getTabEntier("LIAISON TYPE", params.liaisons.nbLiaisons);
        params.liaisons.nature = getTabEntier("LIAISON NATURE", params.liaisons.nbLiaisons);
        params.liaisons.cote = getTabDouble("LIAISON COTE", params.liaisons.nbLiaisons);
        params.liaisons.largeur = getTabDouble("LIAISON LARGEUR", params.liaisons.nbLiaisons);
        params.liaisons.longueur = getTabDouble("LIAISON LONGUEUR", params.liaisons.nbLiaisons);
        params.liaisons.rugosite = getTabDouble("LIAISON RUGOSITE", params.liaisons.nbLiaisons);
        params.liaisons.section = getTabDouble("LIAISON SECTION", params.liaisons.nbLiaisons);
        params.liaisons.coefPerteCharge = getTabDouble("LIAISON COEFFICIENT PERTE DE CHARGE", params.liaisons.nbLiaisons);
        params.liaisons.coefDebitSeuil = getTabDouble("LIAISON COEFFICIENT DE DEBIT SEUIL", params.liaisons.nbLiaisons);
        params.liaisons.coefActivation = getTabDouble("LIAISON COEFFICIENT D'ACTIVATION", params.liaisons.nbLiaisons);
        params.liaisons.coefDebitOrifice = getTabDouble("LIAISON COEFFICIENT DE DEBIT ORIFICE", params.liaisons.nbLiaisons);
        params.liaisons.typeOrifice = getTabEntier("LIAISON TYPE ORIFICE", params.liaisons.nbLiaisons);
        params.liaisons.numCasierOrigine = getTabEntier("LIAISON NUMERO DU CASIER ORIGINE", params.liaisons.nbLiaisons);
        params.liaisons.numCasierFin = getTabEntier("LIAISON NUMERO DU CASIER FIN", params.liaisons.nbLiaisons);
        params.liaisons.numBiefAssocie = getTabEntier("LIAISON NUMERO DU BIEF ASSOCIE", params.liaisons.nbLiaisons);
        params.liaisons.abscBief = getTabDouble("LIAISON ABSCISSE CORRESPONDANTE", params.liaisons.nbLiaisons);
        return params;
    }

    /**
   * Lecture des apports et deversoirs
   */
    public SParametresApporDeversoirs litParametresApporDeversoirs() {
        SParametresApporDeversoirs params = new SParametresApporDeversoirs();
        params.debitsApports = new SParametresQApport();
        params.debitsApports.nbQApport = getEntier("NOMBRE DE DEBITS D'APPORTS");
        params.debitsApports.noms = getTabChaine("NOM DE L'APPORT", params.debitsApports.nbQApport);
        params.debitsApports.numBranche = getTabEntier("NUMERO BRANCHE APPORT", params.debitsApports.nbQApport);
        params.debitsApports.abscisses = getTabDouble("ABSCISSE APPORT", params.debitsApports.nbQApport);
        params.debitsApports.longueurs = getTabDouble("LONGUEUR APPORT", params.debitsApports.nbQApport);
        params.debitsApports.numLoi = getTabEntier("NUMERO LOI APPORT", params.debitsApports.nbQApport);
        params.deversLate = new SParametresDeversLateraux();
        params.deversLate.nbDeversoirs = getEntier("NOMBRE DE DEVERSOIRS");
        if (bnoyau52_) {
            params.deversLate.nom = new String[params.deversLate.nbDeversoirs];
            params.deversLate.type = new int[params.deversLate.nbDeversoirs];
            params.deversLate.numBranche = new int[params.deversLate.nbDeversoirs];
            params.deversLate.abscisse = new double[params.deversLate.nbDeversoirs];
            params.deversLate.longueur = new double[params.deversLate.nbDeversoirs];
            params.deversLate.coteCrete = new double[params.deversLate.nbDeversoirs];
            params.deversLate.coeffDebit = new double[params.deversLate.nbDeversoirs];
            params.deversLate.numLoi = new int[params.deversLate.nbDeversoirs];
            for (int i = 0; i < params.deversLate.nbDeversoirs; i++) {
                params.deversLate.nom[i] = getChaine("NOM DEVERSOIR " + (i + 1));
                params.deversLate.type[i] = getEntier("TYPE DEVERSOIR " + (i + 1));
                params.deversLate.numBranche[i] = getEntier("NUM BRANCHE DEVERSOIR " + (i + 1));
                params.deversLate.abscisse[i] = getDouble("ABSCISSE DEVERSOIR " + (i + 1));
                params.deversLate.longueur[i] = getDouble("LONGUEUR DEVERSOIR " + (i + 1));
                params.deversLate.coteCrete[i] = getDouble("COTE CRETE DEVERSOIR " + (i + 1));
                params.deversLate.coeffDebit[i] = getDouble("COEFF DEBIT DEVERSOIR " + (i + 1));
                params.deversLate.numLoi[i] = getEntier("NUMERO LOI DEVERSOIR " + (i + 1));
            }
        } else {
            params.deversLate.nom = getTabChaine("NOM DEVERSOIR", params.deversLate.nbDeversoirs);
            params.deversLate.type = getTabEntier("TYPE DEVERSOIR", params.deversLate.nbDeversoirs);
            params.deversLate.numBranche = getTabEntier("NUM BRANCHE DEVERSOIR", params.deversLate.nbDeversoirs);
            params.deversLate.abscisse = getTabDouble("ABSCISSE DEVERSOIR", params.deversLate.nbDeversoirs);
            params.deversLate.longueur = getTabDouble("LONGUEUR DEVERSOIR", params.deversLate.nbDeversoirs);
            params.deversLate.coteCrete = getTabDouble("COTE CRETE DEVERSOIR", params.deversLate.nbDeversoirs);
            params.deversLate.coeffDebit = getTabDouble("COEFF DEBIT DEVERSOIR", params.deversLate.nbDeversoirs);
            params.deversLate.numLoi = getTabEntier("NUMERO LOI DEVERSOIR", params.deversLate.nbDeversoirs);
        }
        params.apportCasier = new SParametresApportCasier();
        params.apportCasier.nbApportPluie = getEntier("NOMBRE D'APPORTS DE PLUIE");
        params.apportCasier.numCasier = getTabEntier("NUMERO DU CASIER ASSOCIE", params.apportCasier.nbApportPluie);
        params.apportCasier.numLoi = getTabEntier("NUMERO DE LA LOI ASSOCIEE", params.apportCasier.nbApportPluie);
        return params;
    }

    /**
   * Lecture des param�tres de calage
   */
    public SParametresCalage litParametresCalage() {
        SParametresCalage params = new SParametresCalage();
        params.frottement = new SParametresFrottement();
        params.frottement.loi = getEntier("LOI DE FROTTEMENT");
        params.frottement.nbZone = getEntier("NOMBRE DE ZONES DE FROTTEMENT");
        params.frottement.numBranche = getTabEntier("NUMERO DE BRANCHE DE ZONE DE FROTTEMENT", params.frottement.nbZone);
        params.frottement.absDebZone = getTabDouble("ABSCISSE DEBUT ZONE DE FROTTEMENT", params.frottement.nbZone);
        params.frottement.absFinZone = getTabDouble("ABSCISSE FIN ZONE DE FROTTEMENT", params.frottement.nbZone);
        params.frottement.coefLitMin = getTabDouble("VALEUR DU COEFFICIENT LIT MINEUR", params.frottement.nbZone);
        params.frottement.coefLitMaj = getTabDouble("VALEUR DU COEFFICIENT LIT MAJEUR", params.frottement.nbZone);
        params.zoneStockage = new SParametresZoneStockage();
        params.zoneStockage.nbProfils = getEntier("NOMBRE DE PROFILS COMPORTANT DES ZONES DE STOCKAGE");
        params.zoneStockage.numProfil = getTabEntier("NUMERO PROFIL STOCKAGE", params.zoneStockage.nbProfils);
        params.zoneStockage.limGauchLitMaj = getTabDouble("LIMITE GAUCHE LIT MAJEUR", params.zoneStockage.nbProfils);
        params.zoneStockage.limDroitLitMaj = getTabDouble("LIMITE DROITE LIT MAJEUR", params.zoneStockage.nbProfils);
        return params;
    }

    /**
   * Lecture des param�tres de calage automatique
   */
    public SParametresCalageAuto litParametresCalageAuto() {
        SParametresCalageAuto params = new SParametresCalageAuto();
        params.parametres = new SParametresParamsCalageAuto();
        if (bnoyau52_) {
            params.parametres.modeCalageAuto = false;
            return params;
        }
        params.parametres.modeCalageAuto = getBooleen("OPTION CALAGE");
        params.parametres.pasGradient = getDouble("PAS DE CALCUL DU GRADIENT");
        params.parametres.nbMaxIterations = getEntier("NOMBRE D'ITERATIONS");
        params.parametres.typeLit = getEntier("CHOIX DU LIT MINEUR-MAJEUR");
        params.parametres.precision = getDouble("PRECISION CONVERGENCE");
        params.parametres.roInit = getDouble("RO INITIAL");
        params.parametres.methOptimisation = getEntier("METHODE D'OPTIMISATION");
        params.parametres.nomFichResult = getChaine("FICHIER RESULTATS CALAGE 1");
        params.parametres.nomFichListing = getChaine("FICHIER RESULTATS CALAGE 2");
        params.zones = new SParametresZonesCalageAuto();
        params.zones.nbZones = getEntier("NOMBRE DE ZONES DE FROTTEMENT POUR LE CALAGE");
        params.zones.absDebZone = getTabDouble("ABSCISSE DE DEBUT DE ZONE DE CALAGE", params.zones.nbZones);
        params.zones.absFinZone = getTabDouble("ABSCISSE DE FIN DE ZONE DE CALAGE", params.zones.nbZones);
        params.zones.coefLitMin = getTabDouble("VALEUR DU COEFFICIENT MINEUR", params.zones.nbZones);
        params.zones.coefLitMaj = getTabDouble("VALEUR DU COEFFICIENT MAJEUR", params.zones.nbZones);
        params.crues = new SParametresCruesCalageAuto();
        params.crues.nbCrues = getEntier("NOMBRE DE CRUES DE CALAGE");
        params.crues.crues = new SParametresCrueCalageAuto[params.crues.nbCrues];
        for (int i = 0; i < params.crues.nbCrues; i++) {
            params.crues.crues[i] = new SParametresCrueCalageAuto();
            params.crues.crues[i].debitAmont = getDouble("DEBIT POUR LA CRUE " + (i + 1));
            params.crues.crues[i].coteAval = getDouble("COTE AVAL POUR LA CRUE " + (i + 1));
            params.crues.crues[i].nbMesures = getEntier("NOMBRE DE MESURES POUR LA CRUE " + (i + 1));
            params.crues.crues[i].absMesures = getTabDouble("ABSCISSE DES MESURES POUR LA CRUE " + (i + 1), params.crues.crues[i].nbMesures);
            params.crues.crues[i].coteMesures = getTabDouble("COTE DES MESURES POUR LA CRUE " + (i + 1), params.crues.crues[i].nbMesures);
            params.crues.crues[i].pondMesures = getTabDouble("PONDERATION POUR LES MESURES CRUE " + (i + 1), params.crues.crues[i].nbMesures);
            params.crues.crues[i].nbApports = getEntier("NOMBRE D'APPORTS POUR LA CRUE " + (i + 1));
            params.crues.crues[i].absApports = getTabDouble("ABSCISSE DES APPORTS POUR LA CRUE " + (i + 1), params.crues.crues[i].nbApports);
            params.crues.crues[i].debitApports = getTabDouble("DEBIT DES APPORTS POUR LA CRUE " + (i + 1), params.crues.crues[i].nbApports);
        }
        return params;
    }

    /**
   * Lecture de la qualit� d'eau
   * 
   * @param _nbExtrLibres
   *          Nombre d'extremites libres.
   */
    public SParametresTraceur litParametresTracer(int _nbExtrLibres) {
        SParametresTraceur params = new SParametresTraceur();
        if (bnoyau52_) {
            params.presenceTraceurs = false;
            return params;
        }
        params.presenceTraceurs = getBooleen("PRESENCE DE TRACEURS");
        params.nbTraceur = getEntier("NOMBRE DE TRACEURS");
        params.parametresConvecDiffu = new SParametresConvecDiffu();
        params.parametresConvecDiffu.convectionTraceurs = getTabBooleen("CONVECTION DES TRACEURS", params.nbTraceur);
        params.parametresConvecDiffu.optionConvection = getEntier("OPTION DE CONVECTION POUR LES TRACEURS");
        params.parametresConvecDiffu.ordreSchemaConvec = getEntier("ORDRE DU SCHEMA DE CONVECTION VOLUMES FINIS");
        params.parametresConvecDiffu.paramW = getDouble("PARAMETRE W DU SCHEMA DE CONVECTION VOLUMES FINIS");
        params.parametresConvecDiffu.LimitPente = getBooleen("LIMITEUR DE PENTE DU SCHEMA VOLUMES FINIS");
        params.parametresConvecDiffu.diffusionTraceurs = getTabBooleen("DIFFUSION DES TRACEURS", params.nbTraceur);
        params.parametresConvecDiffu.optionCalculDiffusion = getEntier("OPTION DE CALCUL DE LA DISPERSION POUR LES TRACEURS");
        params.parametresConvecDiffu.coeffDiffusion1 = getDouble("COEFFICIENT DE DIFFUSION 1 POUR LES TRACEURS");
        params.parametresConvecDiffu.coeffDiffusion2 = getDouble("COEFFICIENT DE DIFFUSION 2 POUR LES TRACEURS");
        params.parametresNumQualiteEau = new SParametresNumQualiteEau();
        params.parametresNumQualiteEau.modeleQualiteEau = getEntier("MODELE DE QUALITE D'EAU");
        params.parametresNumQualiteEau.fichParamPhysiqueTracer = getChaine("FICHIER DES PARAMETRES PHYSIQUES TRACER");
        params.parametresNumQualiteEau.fichMeteoTracer = getChaine("FICHIER DES DONNEES METEO TRACER");
        params.parametresNumQualiteEau.frequenceCouplHydroTracer = getEntier("FREQUENCE DE COUPLAGE ENTRE HYDRAULIQUE ET TRACER");
        params.parametresImpressTracer = new SParametresImpressResultTracer();
        params.parametresImpressTracer.fichListTracer = getChaine("FICHIER LISTING TRACER");
        params.parametresImpressTracer.concentInit = getBooleen("IMPRESSION DES CONCENTRATIONS INITIALES");
        params.parametresImpressTracer.loiTracer = getBooleen("IMPRESSION DES LOIS TRACER");
        params.parametresImpressTracer.concentrations = getBooleen("IMPRESSION DES CONCENTRATIONS SUR LE LISTING");
        params.parametresImpressTracer.bilanTracer = getBooleen("IMPRESSION DU BILAN TRACER SUR LE LISTING");
        params.parametresImpressTracer.fichResultTracer = getChaine("FICHIER RESULTATS TRACER");
        params.parametresImpressTracer.formatFichResultat = getEntier("POST-PROCESSEUR TRACER");
        params.parametresCondLimTracer = new SParametresCondLimTracer();
        params.parametresCondLimTracer.typeCondLimTracer = getTabEntier("TYPE DE CONDITIONS LIMITES TRACER", _nbExtrLibres);
        params.parametresCondLimTracer.numLoiCondLimTracer = getTabEntier("NUMERO DES LOIS TRACER POUR LES CL", _nbExtrLibres);
        params.parametresConcInitTracer = new SParametresConcInitTracer();
        params.parametresConcInitTracer.presenceConcInit = getBooleen("PRESENCE CONCENTRATIONS INITIALES");
        params.parametresConcInitTracer.modeEntree = getEntier("MODE D'ENTREE DES CONCENTRATIONS INITIALES");
        params.parametresConcInitTracer.fichConcInit = getChaine("FICHIER DES CONCENTRATIONS INITIALES");
        params.parametresConcInitTracer.nbPts = getEntier("NOMBRE DE POINTS DECRIVANT LES CONC INITIALES");
        params.parametresConcInitTracer.branche = null;
        params.parametresConcInitTracer.abscisse = null;
        params.parametresConcInitTracer.concentrations = null;
        params.parametresSourcesTraceurs = new SParametresSourcesTraceurs();
        params.parametresSourcesTraceurs.nbSources = getEntier("NOMBRE DE SOURCES DE TRACEURS");
        params.parametresSourcesTraceurs.noms = getTabChaine("NOM DES SOURCES", params.parametresSourcesTraceurs.nbSources);
        params.parametresSourcesTraceurs.typeSources = getTabEntier("TYPE DES SOURCES", params.parametresSourcesTraceurs.nbSources);
        params.parametresSourcesTraceurs.numBranche = getTabEntier("BRANCHE DES SOURCES", params.parametresSourcesTraceurs.nbSources);
        params.parametresSourcesTraceurs.abscisses = getTabDouble("ABSCISSE DES SOURCES", params.parametresSourcesTraceurs.nbSources);
        params.parametresSourcesTraceurs.longueurs = getTabDouble("LONGUEUR DES SOURCES", params.parametresSourcesTraceurs.nbSources);
        params.parametresSourcesTraceurs.numLoi = getTabEntier("NUMERO DES LOIS TRACER POUR LES SOURCES", params.parametresSourcesTraceurs.nbSources);
        params.parametresLoisTracer = new SParametresLoisTracer();
        params.parametresLoisTracer.nbLoisTracer = getEntier("NOMBRE DE LOIS TRACER");
        params.parametresLoisTracer.loisTracer = new SParametresLoiTracer[params.parametresLoisTracer.nbLoisTracer];
        for (int i = 0; i < params.parametresLoisTracer.nbLoisTracer; i++) {
            params.parametresLoisTracer.loisTracer[i] = new SParametresLoiTracer();
            params.parametresLoisTracer.loisTracer[i].nom = getChaine("LOI TRACER " + (i + 1) + " NOM");
            params.parametresLoisTracer.loisTracer[i].modeEntree = getEntier("LOI TRACER " + (i + 1) + " MODE D'ENTREE");
            params.parametresLoisTracer.loisTracer[i].fichier = getChaine("LOI TRACER " + (i + 1) + " FICHIER");
            params.parametresLoisTracer.loisTracer[i].uniteTps = getEntier("LOI TRACER " + (i + 1) + " UNITE DE TEMPS");
            params.parametresLoisTracer.loisTracer[i].nbPoints = getEntier("LOI TRACER " + (i + 1) + " NOMBRE DE POINTS");
            params.parametresLoisTracer.loisTracer[i].tps = getTabDouble("LOI TRACER " + (i + 1) + " TEMPS", params.parametresLoisTracer.loisTracer[i].nbPoints);
            params.parametresLoisTracer.loisTracer[i].concentrations = new SParametresConcentrations[params.nbTraceur];
            for (int t = 0; t < params.nbTraceur; t++) {
                params.parametresLoisTracer.loisTracer[i].concentrations[t] = new SParametresConcentrations();
                params.parametresLoisTracer.loisTracer[i].concentrations[t].concentrations = getTabDouble("LOI TRACER " + (i + 1) + " CONCENTRATION TRACEUR " + (t + 1), params.parametresLoisTracer.loisTracer[i].nbPoints);
            }
        }
        return params;
    }

    /**
   * Lecture des lois hydrauliques
   */
    public SParametresLoisHydrau litParametresLoisHydrau() {
        SParametresLoisHydrau params = new SParametresLoisHydrau();
        params.nb = getEntier("NOMBRE DE LOIS HYDRAULIQUES");
        params.lois = new SParametresLoi[params.nb];
        for (int i = 0; i < params.nb; i++) {
            params.lois[i] = new SParametresLoi();
            params.lois[i].nom = getChaine("LOI " + (i + 1) + " NOM");
            params.lois[i].type = getEntier("LOI " + (i + 1) + " TYPE");
            params.lois[i].donnees = new SParametresDonneesLoi();
            params.lois[i].donnees.modeEntree = getEntier("LOI " + (i + 1) + " MODE D'ENTREE");
            params.lois[i].donnees.fichier = getChaine("LOI " + (i + 1) + " FICHIER");
            if (params.lois[i].donnees.modeEntree != 1) {
                System.err.println("LOI " + (i + 1) + " MODE D'ENTREE : Seule la valeur 1 est autoris�e");
            }
        }
        return params;
    }

    /**
   * Lecture des conditions initiales
   */
    public SParametresCondInit litParametresCondInit() {
        SParametresCondInit params = new SParametresCondInit();
        params.repriseEtude = new SParametresReprEtude();
        params.repriseEtude.repriseCalcul = getBooleen("REPRISE DE CALCUL");
        params.repriseEtude.fichRepriseLec = getChaine("FICHIER DE REPRISE EN LECTURE");
        params.repriseEtude.formatBinFich = getChaine("BINAIRE DU FICHIER DE REPRISE EN LECTURE");
        params.ligneEau = new SParametresLigEau();
        params.ligneEau.LigEauInit = getBooleen("PRESENCE LIGNE D'EAU INITIALE");
        params.ligneEau.modeEntree = getEntier("MODE D'ENTREE DE LA LIGNE D'EAU");
        params.ligneEau.fichLigEau = getChaine("FICHIER LIGNE D'EAU");
        params.ligneEau.formatFichLig = getEntier("FORMAT LIGNE D'EAU");
        params.ligneEau.nbPts = getEntier("NOMBRE DE POINTS DECRIVANT LA LIGNE D'EAU");
        params.ligneEau.branche = getTabEntier("BRANCHE DE LA LIGNE D'EAU", params.ligneEau.nbPts);
        params.ligneEau.abscisse = getTabDouble("ABSCISSE DE LA LIGNE D'EAU", params.ligneEau.nbPts);
        params.ligneEau.cote = getTabDouble("COTE DE LA LIGNE D'EAU", params.ligneEau.nbPts);
        params.ligneEau.debit = getTabDouble("DEBIT DE LA LIGNE D'EAU", params.ligneEau.nbPts);
        params.zonesSeches = new SParametresZoneSeche();
        params.zonesSeches.nb = getEntier("NOMBRE DE ZONES SECHES");
        params.zonesSeches.branche = getTabEntier("BRANCHE DE ZONE SECHE", params.zonesSeches.nb);
        params.zonesSeches.absDebut = getTabDouble("ABSCISSE DE DEBUT DE ZONE SECHE", params.zonesSeches.nb);
        params.zonesSeches.absFin = getTabDouble("ABSCISSE DE FIN DE ZONE SECHE", params.zonesSeches.nb);
        return params;
    }

    /**
   * Lecture des param�tres pour l'impression et les resultats
   */
    public SParametresImpressResult litParametresImpressResult() {
        SParametresImpressResult params = new SParametresImpressResult();
        params.titreCalcul = getChaine("TITRE DU CALCUL");
        params.impression = new SParametresImpress();
        params.impression.geometrie = getBooleen("IMPRESSION DE LA GEOMETRIE");
        params.impression.planimetrage = getBooleen("IMPRESSION DU PLANIMETRAGE");
        params.impression.reseau = getBooleen("IMPRESSION DU RESEAU");
        params.impression.loiHydrau = getBooleen("IMPRESSION DES LOIS HYDRAULIQUES");
        params.impression.ligneEauInit = getBooleen("IMPRESSION DE LA LIGNE D'EAU INITIALE");
        params.impression.calcul = getBooleen("IMPRESSION CALCUL");
        params.pasStockage = new SParametresPasStock();
        params.pasStockage.premPasTpsStock = getEntier("PREMIER PAS DE TEMPS A STOCKER");
        params.pasStockage.pasStock = getEntier("PAS DE STOCKAGE");
        params.pasStockage.pasImpression = getEntier("PAS D'IMPRESSION");
        params.resultats = new SParametresResult();
        params.resultats.fichResultat = getChaine("FICHIER RESULTATS");
        params.resultats.fichResultat2 = getChaine("FICHIER RESULTATS 2");
        params.resultats.formatBinFich = getChaine("BINAIRE DU FICHIER RESULTATS");
        params.resultats.postProcesseur = getEntier("POST-PROCESSEUR");
        params.listing = new SParametresListing();
        params.listing.fichListing = getChaine("FICHIER LISTING");
        params.fichReprise = new SParametresFichReprise();
        params.fichReprise.fichRepriseEcr = getChaine("FICHIER DE REPRISE EN ECRITURE");
        params.fichReprise.formatBinFich = getChaine("BINAIRE DU FICHIER DE REPRISE EN ECRITURE");
        params.rubens = new SParametresRubens();
        params.rubens.ecartInterBranch = getDouble("ECART ENTRE BRANCHES");
        params.stockage = new SParametresStockage();
        params.stockage.option = getEntier("OPTION DE STOCKAGE");
        params.stockage.nbSite = getEntier("NOMBRE DE SITES");
        params.stockage.branche = getTabEntier("BRANCHE DU SITE", params.stockage.nbSite);
        params.stockage.abscisse = getTabDouble("ABSCISSE DU SITE", params.stockage.nbSite);
        params.casier = new SParametresFichResCasier();
        params.casier.resultatCasier = getChaine("FICHIER RESULTATS CASIERS");
        params.casier.listingCasier = getChaine("FICHIER LISTING CASIERS");
        params.casier.resultatLiaison = getChaine("FICHIER RESULTATS LIAISONS");
        params.casier.listingLiaison = getChaine("FICHIER LISTING LIAISONS");
        return params;
    }

    /**
   * Lecture des param�tres variables calcul�es
   */
    public SParametresVarCalc litParametresVarCalc() {
        SParametresVarCalc params = new SParametresVarCalc();
        params.varCalculees = getTabBooleen("VARIABLES CALCULEES", 15);
        return params;
    }

    /**
   * Lecture des param�tres variables stock�es
   */
    public SParametresVarStock litParametresVarStock() {
        SParametresVarStock params = new SParametresVarStock();
        params.varStockees = getTabBooleen("VARIABLES STOCKEES", 42);
        return params;
    }

    /**
   * Retourne la valeur enti�re d'un mot cl�
   */
    private int getEntier(String _key) {
        DicoEntite ent = md_.getEntite(_key);
        if (ent == null) throw new IllegalArgumentException("Mot cl� '" + _key + "' attendu");
        return Integer.parseInt(params_.getValue(ent));
    }

    /**
   * Retourne la valeur r�elle d'un mot cl�
   */
    private double getDouble(String _key) {
        DicoEntite ent = md_.getEntite(_key);
        if (ent == null) throw new IllegalArgumentException("Mot cl� '" + _key + "' attendu");
        return Double.parseDouble(params_.getValue(ent));
    }

    /**
   * Retourne la valeur string d'un mot cl�
   */
    private String getChaine(String _key) {
        DicoEntite ent = md_.getEntite(_key);
        if (ent == null) throw new IllegalArgumentException("Mot cl� '" + _key + "' attendu");
        return params_.getValue(ent);
    }

    /**
   * Retourne la valeur booleene d'un mot cl�
   */
    private boolean getBooleen(String _key) {
        DicoEntite ent = md_.getEntite(_key);
        if (ent == null) throw new IllegalArgumentException("Mot cl� '" + _key + "' attendu");
        return DicoDataType.Binaire.getValue(params_.getValue(ent));
    }

    /**
   * Retourne le tableau entier d'un mot cl�. Le tableau est remplit avec les valeurs fix�es, puis les valeurs par defaut si pas
   * assez de valeurs par defaut, puis 0 si pas assez de valeurs par defaut.
   * @param _nb Le nombre de valeurs � recuperer.
   * @return Le tableau d'entiers.
   */
    private int[] getTabEntier(String _key, int _nb) {
        DicoEntite ent = md_.getEntite(_key);
        if (ent == null) throw new IllegalArgumentException("Mot cl� '" + _key + "' attendu");
        int[] ret = new int[_nb];
        if (_nb == 0) return ret;
        String[] sdefs = ent.getDefautValue().split(";");
        String[] sfixs = params_.getValue(ent).split(";");
        int ind = 0;
        while (ind < Math.min(_nb, sfixs.length)) ret[ind] = Integer.parseInt(sfixs[ind++]);
        while (ind < Math.min(_nb, sdefs.length)) ret[ind] = Integer.parseInt(sdefs[ind++]);
        while (ind < _nb) ret[ind++] = 0;
        return ret;
    }

    /**
   * Retourne le tableau double d'un mot cl�. Le tableau est remplit avec les valeurs fix�es, puis les valeurs par defaut si pas
   * assez de valeurs par defaut, puis 0 si pas assez de valeurs par defaut.
   * @param _nb Le nombre de valeurs � recuperer.
   * @return Le tableau de doubles.
   */
    private double[] getTabDouble(String _key, int _nb) {
        DicoEntite ent = md_.getEntite(_key);
        if (ent == null) throw new IllegalArgumentException("Mot cl� '" + _key + "' attendu");
        double[] ret = new double[_nb];
        if (_nb == 0) return ret;
        String[] sdefs = ent.getDefautValue().split(";");
        String[] sfixs = params_.getValue(ent).split(";");
        int ind = 0;
        while (ind < Math.min(_nb, sfixs.length)) ret[ind] = Double.parseDouble(sfixs[ind++]);
        while (ind < Math.min(_nb, sdefs.length)) ret[ind] = Double.parseDouble(sdefs[ind++]);
        while (ind < _nb) ret[ind++] = 0;
        return ret;
    }

    /**
   * Retourne le tableau string d'un mot cl�. Le tableau est remplit avec les valeurs fix�es, puis les valeurs par defaut si pas
   * assez de valeurs par defaut, puis "" si pas assez de valeurs par defaut.
   * @param _nb Le nombre de valeurs � recuperer.
   * @return Le tableau de String.
   */
    private String[] getTabChaine(String _key, int _nb) {
        DicoEntite ent = md_.getEntite(_key);
        if (ent == null) throw new IllegalArgumentException("Mot cl� '" + _key + "' attendu");
        String[] ret = new String[_nb];
        if (_nb == 0) return ret;
        String[] sdefs = ent.getDefautValue().split(";");
        String[] sfixs = params_.getValue(ent).split(";");
        int ind = 0;
        while (ind < Math.min(_nb, sfixs.length)) ret[ind] = sfixs[ind++];
        while (ind < Math.min(_nb, sdefs.length)) ret[ind] = sdefs[ind++];
        while (ind < _nb) ret[ind++] = "";
        return ret;
    }

    /**
   * Retourne le tableau de booleen d'un mot cl�. Le tableau est remplit avec les valeurs fix�es, puis les valeurs par defaut si pas
   * assez de valeurs par defaut, puis false si pas assez de valeurs par defaut.
   * @param _nb Le nombre de valeurs � recuperer.
   * @return Le tableau de booleen.
   */
    private boolean[] getTabBooleen(String _key, int _nb) {
        DicoEntite ent = md_.getEntite(_key);
        if (ent == null) throw new IllegalArgumentException("Mot cl� '" + _key + "' attendu");
        boolean[] ret = new boolean[_nb];
        if (_nb == 0) return ret;
        String[] sdefs = ent.getDefautValue().split(";");
        String[] sfixs = params_.getValue(ent).split(";");
        int ind = 0;
        while (ind < Math.min(_nb, sfixs.length)) ret[ind] = DicoDataType.Binaire.getValue(sfixs[ind++]);
        while (ind < Math.min(_nb, sdefs.length)) ret[ind] = DicoDataType.Binaire.getValue(sdefs[ind++]);
        while (ind < _nb) ret[ind++] = false;
        return ret;
    }

    /**
   * Retourne les mots cl�s ignor�s (parce que non trait�es)
   * @return
   */
    public String[] getMotsIgnores() {
        return null;
    }

    /**
   * Retourne les mots cl�s inconnus (parce que pas dans le dictionnaire)
   * @return
   */
    public String[] getMotsInconnus() {
        return null;
    }

    class Progression implements ProgressionInterface {

        public void reset() {
            System.out.println("Reset");
        }

        public void setDesc(String _s) {
            System.out.println("Desc: " + _s);
        }

        public void setProgression(int _v) {
            System.out.println("Progression: " + _v);
        }
    }
}
