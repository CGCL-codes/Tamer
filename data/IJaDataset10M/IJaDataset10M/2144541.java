package org.fudaa.dodico.mascaret;

import java.io.File;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import org.fudaa.ctulu.CtuluLibFile;
import org.fudaa.dodico.calcul.DCalcul;
import org.fudaa.dodico.corba.calcul.SProgression;
import org.fudaa.dodico.corba.mascaret.ICalculMascaret;
import org.fudaa.dodico.corba.mascaret.ICalculMascaretOperations;
import org.fudaa.dodico.corba.mascaret.IParametresMascaret;
import org.fudaa.dodico.corba.mascaret.IParametresMascaretHelper;
import org.fudaa.dodico.corba.mascaret.IResultatsMascaret;
import org.fudaa.dodico.corba.mascaret.IResultatsMascaretHelper;
import org.fudaa.dodico.corba.mascaret.SParametresTailleMaxFichier;
import org.fudaa.dodico.corba.mascaret.SResultatsEcran;
import org.fudaa.dodico.corba.mascaret.SResultatsLIS;
import org.fudaa.dodico.corba.mascaret.SResultatsTemporelSpatial;
import org.fudaa.dodico.corba.objet.IConnexion;
import org.fudaa.dodico.objet.CDodico;
import org.fudaa.dodico.objet.CExec;
import java.text.NumberFormat;
import org.fudaa.dodico.corba.objet.IPersonne;
import org.fudaa.ctulu.CtuluLib;
import java.io.FileOutputStream;

/**
 * Classe qui impl�mente cot� serveur l’interface ��ICalculMascaret�� qui
 * contient les m�thodes li�es aux lancement du calcul.
 * @version      $Revision: 1.43 $ $Date: 2008-03-05 10:18:50 $ by $Author: opasteur $
 * @author       Jean-Marc Lacombe
 */
public class DCalculMascaret extends DCalcul implements ICalculMascaret, ICalculMascaretOperations {

    private boolean noyau5_2_ = false;

    private boolean lire_;

    private SProgression strOperation_;

    private String etatCalcul_ = "ecriture parametres";

    private String messagesAvertissements_ = "";

    private boolean bcalage_ = false;

    private boolean bInterrompu_ = false;

    private boolean bRecupererResultats_ = false;

    private long instantDebutCalcul_ = 0;

    private CExec execCalculEnCours;

    private static final String SOUS_REP_5_2 = "mascaret_5_2";

    private static final String SOUS_REP_7_0 = "mascaret_7_0";

    public DCalculMascaret() {
        super();
        lire_ = true;
        strOperation_ = new SProgression("", 0);
        setFichiersExtensions(new String[] { ".cas", ".geo", ".lig", "_lec.opt", "_lec.rep", ".met", ".phy", ".conc", ".lis", "_ecr.opt", ".rub", ".mai", "_ecr.rep", ".casier", ".cas_opt", ".cas_rub", ".liai_opt", ".liai_rub", ".cas_lis", ".liai_lis", ".cal_lis", ".cal_opt", ".tra_lis", ".tra_opt", ".tra_rub" });
    }

    public final Object clone() throws CloneNotSupportedException {
        return new DCalculMascaret();
    }

    public String toString() {
        return "DCalculMascaret()";
    }

    public String description() {
        return "Mascaret, serveur de calcul d'hydraulique uni-dimensionnel: " + super.description();
    }

    public boolean lireFichier() {
        return lire_;
    }

    public void lireFichier(boolean _val) {
        lire_ = _val;
    }

    public SProgression progression() {
        if (!etatCalcul_.equals("calcul")) {
            return strOperation_;
        } else {
            String sousRep = SOUS_REP_7_0;
            if (noyau5_2_) {
                sousRep = SOUS_REP_5_2;
            } else {
                sousRep = SOUS_REP_7_0;
            }
            File controle = new File(cheminServeur() + sousRep + File.separator + "Controle.txt");
            if (!controle.exists()) {
                setProgression(bcalage_ ? "Calcul calage..." : "Calcul...", 0);
                instantDebutCalcul_ = System.currentTimeMillis();
            } else {
                try {
                    String res = CtuluLibFile.litFichierTexte(controle, controle.length(), false, false);
                    if (res != null) {
                        int index = res.indexOf("CALCUL");
                        if (index != -1) {
                            float pourcent = Float.parseFloat(res.substring(index + 7).trim());
                            int pourcentBorne = Math.min(Math.round(pourcent * 100), 100);
                            if (pourcentBorne == 0) {
                                instantDebutCalcul_ = System.currentTimeMillis();
                            }
                            long instantCourant = System.currentTimeMillis();
                            long dureeCalculCourant = instantCourant - instantDebutCalcul_;
                            String tpsEcouleFormate = formatTemps(dureeCalculCourant);
                            if (dureeCalculCourant < (2592000000l)) {
                                if (pourcent > 0) {
                                    double estimationDureeDouble = ((double) dureeCalculCourant) / (Math.min((double) pourcent, 1));
                                    long estimationDureeFinCalcul = (long) estimationDureeDouble;
                                    String estimationFormate = formatTemps(estimationDureeFinCalcul);
                                    setProgression(bcalage_ ? "Calcul calage..." : "Calcul... " + pourcentBorne + "%          Temps �coul� : " + tpsEcouleFormate + "          Dur�e estim�e : " + estimationFormate, pourcentBorne);
                                } else {
                                    setProgression(bcalage_ ? "Calcul calage..." : "Calcul... " + pourcentBorne + "%          Temps �coul� : " + tpsEcouleFormate, pourcentBorne);
                                }
                            } else {
                                setProgression(bcalage_ ? "Calcul calage..." : "Calcul... " + pourcentBorne + "%", pourcentBorne);
                            }
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("DCalculMascaret probl�me d'entr�es-sorties pour Controle.txt");
                } catch (NumberFormatException ex) {
                    System.out.println("DCalculMascaret probl�me d'interpretation pour Controle.txt");
                } catch (IndexOutOfBoundsException ex) {
                    System.out.println("DCalculMascaret probl�me d'�criture concurrente pour Controle.txt");
                } catch (Exception ex) {
                    System.out.println("DCalculMascaret probl�me !: " + ex.getMessage());
                    CDodico.exceptionAxel(this, ex);
                }
            }
            return strOperation_;
        }
    }

    public String getAvertissements() {
        if (bRecupererResultats_) return messagesAvertissements_; else return "";
    }

    private synchronized void setProgression(String op, int pc) {
        if (op == null) {
            op = "";
        }
        strOperation_.operation = op;
        strOperation_.pourcentage = pc;
    }

    public void calcul(IConnexion c) {
        setProgression("Ecriture des fichiers param�tres", 0);
        bInterrompu_ = false;
        bRecupererResultats_ = false;
        etatCalcul_ = "ecriture parametres";
        messagesAvertissements_ = "";
        if (!verifieConnexion(c)) {
            return;
        }
        IParametresMascaret params = IParametresMascaretHelper.narrow(parametres(c));
        if (params == null) {
            CDodico.exceptionAxel(this, new Exception("params non definis (null)"));
        }
        IResultatsMascaret results = IResultatsMascaretHelper.narrow(resultats(c));
        if (results == null) {
            CDodico.exceptionAxel(this, new Exception("results non definis (null)"));
        }
        log(c, "lancement du calcul");
        try {
            if (bInterrompu_) return;
            int noEtude = c.numero();
            String os = System.getProperty("os.name");
            String path = cheminServeur();
            String sousRep = SOUS_REP_7_0;
            if (noyau5_2_) {
                path = path + SOUS_REP_5_2 + File.separator;
                sousRep = SOUS_REP_5_2;
            } else {
                path = path + SOUS_REP_7_0 + File.separator;
                sousRep = SOUS_REP_7_0;
            }
            String nomEtude = "mascaret" + noEtude;
            setProgression("Ecriture des fichiers param�tres", 0);
            System.out.println("Ecriture des fichiers parametres");
            File ficCAS = getFichier(c, ".cas", sousRep);
            if (ficCAS.exists()) {
                ficCAS.delete();
            }
            File ficGEO = getFichier(c, ".geo", sousRep);
            if (ficGEO.exists()) {
                ficGEO.delete();
            }
            File ficLIG = getFichier(c, ".lig", sousRep);
            if (ficLIG.exists()) {
                ficLIG.delete();
            }
            File ficLecREP = getFichier(c, "_lec.rep", sousRep);
            if (ficLecREP.exists()) {
                ficLecREP.delete();
            }
            File ficNCA = new File(path + "FichierCas.txt");
            if (ficNCA.exists()) {
                ficNCA.delete();
            }
            File ficControle = new File(path + "Controle.txt");
            if (ficControle.exists()) {
                ficControle.delete();
            }
            File repertoireCourant = new File(path);
            File[] fichiers = repertoireCourant.listFiles();
            for (int i = 0; i < fichiers.length; i++) {
                if (fichiers[i].getName().endsWith(".loi")) {
                    fichiers[i].delete();
                }
            }
            if (bInterrompu_) return;
            File ficDamocles = null;
            if (os.startsWith("Windows") || os.startsWith("Linux")) {
                ficDamocles = new File(path + "listing.damoc");
            } else {
                ficDamocles = new File(path + nomEtude + ".listing");
            }
            if (ficDamocles.exists()) {
                ficDamocles.delete();
            }
            File ficLIS = getFichier(c, ".lis", sousRep);
            if (ficLIS.exists()) {
                ficLIS.delete();
            }
            File ficOPT = getFichier(c, ".opt", sousRep);
            if (ficOPT.exists()) {
                ficOPT.delete();
            }
            File ficRUB = getFichier(c, ".rub", sousRep);
            if (ficRUB.exists()) {
                ficRUB.delete();
            }
            File ficMAI = getFichier(c, ".mai", sousRep);
            if (ficMAI.exists()) {
                ficMAI.delete();
            }
            File ficEcrREP = getFichier(c, "_ecr.rep", sousRep);
            if (ficEcrREP.exists()) {
                ficEcrREP.delete();
            }
            File ficCalLIS = getFichier(c, ".cal_lis", sousRep);
            if (ficCalLIS.exists()) {
                ficCalLIS.delete();
            }
            File ficCalOPT = getFichier(c, ".cal_opt", sousRep);
            if (ficCalOPT.exists()) {
                ficCalOPT.delete();
            }
            boolean traceurs = false;
            if (params.parametresCAS().parametresTracer != null) {
                traceurs = params.parametresCAS().parametresTracer.presenceTraceurs;
            }
            File ficParamMeteo = getFichier(c, ".met", sousRep);
            if (ficParamMeteo.exists()) {
                ficParamMeteo.delete();
            }
            File ficParamPhy = getFichier(c, ".phy", sousRep);
            if (ficParamPhy.exists()) {
                ficParamPhy.delete();
            }
            File ficConInit = getFichier(c, ".conc", sousRep);
            if (ficConInit.exists()) {
                ficConInit.delete();
            }
            File ficTraLIS = getFichier(c, ".tra_lis", sousRep);
            if (ficTraLIS.exists()) {
                ficTraLIS.delete();
            }
            File ficTraOPT = getFichier(c, ".tra_opt", sousRep);
            if (ficTraOPT.exists()) {
                ficTraOPT.delete();
            }
            File ficTraRUB = getFichier(c, ".tra_rub", sousRep);
            if (ficTraRUB.exists()) {
                ficTraRUB.delete();
            }
            boolean casier = params.parametresCAS().parametresGen.presenceCasiers;
            File ficCasierGEO = getFichier(c, ".casier", sousRep);
            File ficCasierOPT = getFichier(c, ".cas_opt", sousRep);
            File ficCasierRUB = getFichier(c, "cas_rub", sousRep);
            File ficLiaisonOPT = getFichier(c, "liai_opt", sousRep);
            File ficLiaisonRUB = getFichier(c, ".liai_rub", sousRep);
            File ficCasierLIS = getFichier(c, ".cas_lis", sousRep);
            File ficLiaisonLIS = getFichier(c, ".liai_lis", sousRep);
            if (ficCasierGEO.exists()) {
                ficCasierGEO.delete();
            }
            if (ficCasierOPT.exists()) {
                ficCasierOPT.delete();
            }
            if (ficCasierRUB.exists()) {
                ficCasierRUB.delete();
            }
            if (ficLiaisonOPT.exists()) {
                ficLiaisonOPT.delete();
            }
            if (ficLiaisonRUB.exists()) {
                ficLiaisonRUB.delete();
            }
            if (ficCasierLIS.exists()) {
                ficCasierLIS.delete();
            }
            if (ficLiaisonLIS.exists()) {
                ficLiaisonLIS.delete();
            }
            setProgression("Ecriture des fichiers param�tres", 5);
            if (bInterrompu_) return;
            DParametresMascaret.ecritParametresNCA(ficNCA, params.parametresNCA());
            setProgression("Ecriture des fichiers param�tres", 10);
            if (bInterrompu_) return;
            DParametresMascaret.ecritParametresCAS(ficCAS, params.parametresCAS());
            setProgression("Ecriture des fichiers param�tres", 55);
            if (bInterrompu_) return;
            DParametresMascaret.ecritParametresGEO(ficGEO, params.parametresGEO(), false);
            setProgression("Ecriture des fichiers des lois hydrauliques", 70);
            if (bInterrompu_) return;
            DParametresMascaret.ecritLoisHydrauliques(path + nomEtude, true, params.loisHydrauliques());
            if (casier) {
                setProgression("Ecriture du fichier g�om�trie des casiers", 80);
                if (bInterrompu_) return;
                DParametresMascaret.ecritCasierGEO(ficCasierGEO, params.casierGEO());
            }
            if (traceurs) {
                setProgression("Ecriture des fichiers concernant la qualit� d'eau", 85);
                if (bInterrompu_) return;
                if (params.parametresConcentInitiales() != null) {
                    setProgression("Ecriture du fichier des concentrations initiales", 86);
                    if (bInterrompu_) return;
                    DParametresMascaret.ecritParametresConcentInitiales(ficConInit, params.parametresConcentInitiales());
                }
                if ((params.loisTracer() != null) && (params.loisTracer().length != 0)) {
                    setProgression("Ecriture des fichiers des Lois Tracer", 88);
                    if (bInterrompu_) return;
                    DParametresMascaret.ecritLoisTracer(path + nomEtude, true, params.loisTracer());
                }
                if (params.parametresPhysModele() != null) {
                    setProgression("Ecriture du fichier des param�tres physiques", 90);
                    if (bInterrompu_) return;
                    DParametresMascaret.ecritParamsPhysiquesTracer(ficParamPhy, params.parametresPhysModele());
                }
                if (params.paramMeteoTracer() != null) {
                    setProgression("Ecriture du fichier m�t�o", 92);
                    if (bInterrompu_) return;
                    DParametresMascaret.ecritFichierMeteo(ficParamMeteo, params.paramMeteoTracer());
                }
            }
            if (params.parametresCAS().parametresCondInit.ligneEau.LigEauInit) {
                setProgression("Ecriture des fichiers param�tres", 96);
                if (bInterrompu_) return;
                System.out.println("ficLIG=" + ficLIG);
                DParametresMascaret.ecritParametresLigneEauInitiale(ficLIG, params.parametresLigneDEauInitiale());
            }
            setProgression("Ecriture des fichiers param�tres", 100);
            if (bInterrompu_) return;
            if (params.parametresCAS().parametresCondInit.repriseEtude.repriseCalcul) {
                DParametresMascaret.ecritParametresREPSansLigInit(ficLecREP, params.parametresREP());
            }
            bcalage_ = params.parametresCAS().parametresCalageAuto != null && params.parametresCAS().parametresCalageAuto.parametres != null && params.parametresCAS().parametresCalageAuto.parametres.modeCalageAuto;
            etatCalcul_ = "calcul";
            setProgression("Calcul", 0);
            System.out.println("Appel de l'executable mascaret");
            String[] cmd;
            if (os.startsWith("Windows")) {
                cmd = new String[1];
                cmd[0] = path + "mascaret.exe";
            } else {
                cmd = new String[1];
                cmd[0] = path + "mascaret";
            }
            for (int i = 0; i < cmd.length; i++) {
                System.out.println("cmd[" + i + "] = " + cmd[i]);
            }
            execCalculEnCours = new CExec();
            execCalculEnCours.setCommand(cmd);
            execCalculEnCours.setExecDirectory(new File(path.substring(0, path.length() - 1)));
            System.out.println("setExecDirectory : " + path.substring(0, path.length() - 1));
            ByteArrayOutputStream streamEcran = new ByteArrayOutputStream();
            ByteArrayOutputStream streamErreur = new ByteArrayOutputStream();
            execCalculEnCours.setOutStream(new PrintStream(streamEcran));
            execCalculEnCours.setErrStream(new PrintStream(streamErreur));
            new Thread() {

                public void run() {
                    try {
                        sleep(100);
                        Process process = execCalculEnCours.getProcess();
                        while (process == null) {
                            sleep(1000);
                            process = execCalculEnCours.getProcess();
                        }
                        OutputStream output = process.getOutputStream();
                        output.write('\n');
                        output.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            execCalculEnCours.exec();
            if (bInterrompu_) {
                streamErreur.write("\n\nInterruption du calcul par l'utilisateur !!!\n".getBytes());
                if (!bRecupererResultats_) return;
            }
            instantDebutCalcul_ = System.currentTimeMillis();
            System.out.println("Fin du calcul");
            etatCalcul_ = "Lecture des resultats";
            System.out.println("Lecture des resultats");
            SParametresTailleMaxFichier t = params.parametresTailleMaxFichier();
            setProgression("Lecture des resultats", 0);
            String h = InetAddress.getLocalHost().getHostName();
            results.resultatsEcran(new SResultatsEcran(streamEcran.toByteArray()));
            results.resultatsEcranErreur(new SResultatsEcran(streamErreur.toByteArray()));
            setProgression("Lecture des resultats", 10);
            if (bInterrompu_ && !bRecupererResultats_) return;
            try {
                if (ficDamocles.exists()) {
                    results.resultatsDAMOC(DResultatsMascaret.litResultatsDAMOC(ficDamocles, t.maxListingDamocles));
                    long tailleMaxOctet = Math.round(t.maxListingDamocles * 1024);
                    long tailleFichier = ficDamocles.length();
                    if (tailleFichier > tailleMaxOctet) {
                        System.out.println("(tailleFichier=" + tailleFichier + " > tailleMaxOctet=" + tailleMaxOctet + ")");
                        messagesAvertissements_ += "Le fichier listing Damocles: " + ficDamocles.getAbsolutePath() + " est trop volumineux : " + (tailleFichier / 1024) + "Ko.\n";
                        messagesAvertissements_ += "Lecture de la fin du fichier, pour le r�cup�rer enti�rement, veuillez le copier � partir de la machine " + h + ".\n\n";
                    }
                } else {
                    results.resultatsDAMOC(null);
                }
                setProgression("Lecture des resultats", 30);
            } catch (Throwable e) {
                messagesAvertissements_ += "Le fichier listing Damocles est corrompu. Abandon de la lecture du fichier ";
                results.resultatsDAMOC(null);
            }
            if (bInterrompu_ && !bRecupererResultats_) return;
            try {
                if (ficLIS.exists()) {
                    results.resultatsLIS(DResultatsMascaret.litResultatsLIS(ficLIS, t.maxListingMascaret));
                    long tailleMaxOctet = Math.round(t.maxListingMascaret * 1024);
                    long tailleFichier = ficLIS.length();
                    if (tailleFichier > tailleMaxOctet) {
                        messagesAvertissements_ += "Le fichier listing : " + ficLIS.getAbsolutePath() + " est trop volumineux : " + (tailleFichier / 1024) + "Ko.\n";
                        messagesAvertissements_ += "Lecture de la fin du fichier, pour le r�cup�rer enti�rement, veuillez le copier � partir de la machine " + h + ".\n\n";
                    }
                } else {
                    results.resultatsLIS(null);
                }
                setProgression("Lecture des resultats", 35);
            } catch (Throwable e) {
                messagesAvertissements_ += "Le fichier listing Mascaret est corrompu. Abandon de la lecture du fichier ";
                results.resultatsLIS(null);
            }
            if (bInterrompu_ && !bRecupererResultats_) return;
            try {
                if (ficCalLIS.exists()) {
                    results.resultatsCalAutoLIS(DResultatsMascaret.litResultatsCalAutoLIS(ficCalLIS, t.maxListingCalage));
                    long tailleMaxOctet = Math.round(t.maxListingCalage * 1024);
                    long tailleFichier = ficCalLIS.length();
                    if (tailleFichier > tailleMaxOctet) {
                        messagesAvertissements_ += "Le fichier listing calage : " + ficCalLIS.getAbsolutePath() + " est trop volumineux : " + (tailleFichier / 1024) + "Ko.\n";
                        messagesAvertissements_ += "Lecture de la fin du fichier, pour le r�cup�rer enti�rement, veuillez le copier � partir de la machine " + h + ".\n\n";
                    }
                } else {
                    results.resultatsCalAutoLIS(null);
                }
                setProgression("Lecture des resultats", 40);
            } catch (Throwable e) {
                messagesAvertissements_ += "Le fichier listing Calage est corrompu. Abandon de la lecture du fichier ";
                results.resultatsCalAutoLIS(null);
            }
            if (bInterrompu_ && !bRecupererResultats_) return;
            try {
                if (ficCalOPT.exists()) {
                    long tailleMaxOctet = Math.round(t.maxResultatOpthyca * 1024);
                    long tailleFichier = ficCalOPT.length();
                    if (tailleFichier > tailleMaxOctet) {
                        messagesAvertissements_ += "Le fichier r�sultat du calage Opthyca : " + ficCalOPT.getAbsolutePath() + " est trop volumineux : " + (tailleFichier / 1024) + "Ko.\n";
                        messagesAvertissements_ += "Lecture non effectu�e, pour le r�cup�rer, veuillez le copier � partir de la machine " + h + ".\n\n";
                    } else {
                        SResultatsTemporelSpatial sres = DResultatsMascaret.litResultatsCalAuto(ficCalOPT, t.maxResultatOpthyca, DResultatsMascaret.OPTYCA);
                        results.resultatsCalAuto(sres);
                    }
                } else {
                    results.resultatsCalAuto(null);
                }
            } catch (Throwable e) {
                messagesAvertissements_ += "Le fichier de r�sultats calage Optyca est corrompu. Abandon de la lecture du fichier ";
                results.resultatsCalAuto(null);
            }
            try {
                if (ficTraLIS.exists()) {
                    results.resultatsTracerLIS(DResultatsMascaret.litResultatsTracerLIS(ficTraLIS, t.maxListingTracer));
                    long tailleMaxOctet = Math.round(t.maxListingTracer * 1024);
                    long tailleFichier = ficTraLIS.length();
                    if (tailleFichier > tailleMaxOctet) {
                        messagesAvertissements_ += "Le fichier listing tracer : " + ficTraLIS.getAbsolutePath() + " est trop volumineux : " + (tailleFichier / 1024) + "Ko.\n";
                        messagesAvertissements_ += "Lecture de la fin du fichier, pour le r�cup�rer enti�rement, veuillez le copier � partir de la machine " + h + ".\n\n";
                    }
                } else {
                    results.resultatsTracerLIS(null);
                }
                setProgression("Lecture des resultats", 45);
            } catch (Throwable e) {
                messagesAvertissements_ += "Le fichier listing Tracer est corrompu. Abandon de la lecture du fichier ";
                results.resultatsTracerLIS(null);
            }
            if (bInterrompu_ && !bRecupererResultats_) return;
            try {
                if (ficTraOPT.exists()) {
                    long tailleMaxOctet = Math.round(t.maxResultatOpthycaTracer * 1024);
                    long tailleFichier = ficTraOPT.length();
                    if (tailleFichier > tailleMaxOctet) {
                        messagesAvertissements_ += "Le fichier r�sultat Opthyca de la qualit� d'eau : " + ficOPT.getAbsolutePath() + " est trop volumineux : " + (tailleFichier / 1024) + "Ko.\n";
                        messagesAvertissements_ += "Lecture non effectu�e, pour le r�cup�rer, veuillez le copier � partir de la machine " + h + ".\n\n";
                    } else {
                        SResultatsTemporelSpatial sres = DResultatsMascaret.litResultatsTracer(ficTraOPT, t.maxResultatOpthycaTracer, DResultatsMascaret.OPTYCA);
                        results.resultatsTracer(sres);
                    }
                } else if (ficTraRUB.exists()) {
                    long tailleMaxOctet = Math.round(t.maxResultatRubensTracer * 1024);
                    long tailleFichier = (ficTraRUB.length() / 1024) + 1;
                    if (tailleFichier > tailleMaxOctet) {
                        messagesAvertissements_ += "Le fichier r�sultat Opthyca de la qualit� d'eau : " + ficOPT.getAbsolutePath() + " est trop volumineux : " + (tailleFichier / 1024) + "Ko.\n";
                        messagesAvertissements_ += "Lecture non effectu�e, pour le r�cup�rer, veuillez le copier � partir de la machine " + h + ".\n\n";
                    } else {
                        SResultatsTemporelSpatial sres = DResultatsMascaret.litResultatsTracer(ficTraRUB, t.maxResultatRubensTracer, DResultatsMascaret.RUBENS);
                        results.resultatsTracer(sres);
                    }
                } else {
                    results.resultatsTracer(null);
                }
                setProgression("Lecture des resultats", 50);
            } catch (Throwable e) {
                messagesAvertissements_ += "Le fichier des r�sultats Tracer est corrompu. Abandon de la lecture du fichier ";
                results.resultatsTracer(null);
            }
            if (bInterrompu_ && !bRecupererResultats_) return;
            try {
                if (ficEcrREP.exists()) {
                    long tailleMaxOctet = Math.round(t.maxResultatReprise * 1024);
                    long tailleFichier = ficEcrREP.length();
                    if (tailleFichier > tailleMaxOctet) {
                        messagesAvertissements_ += "Le fichier reprise : " + ficEcrREP.getAbsolutePath() + " est trop volumineux : " + (tailleFichier / 1024) + "Ko.\n";
                        messagesAvertissements_ += "Lecture non effectu�e, pour le r�cup�rer, veuillez le copier � partir de la machine " + h + ".\n\n";
                    } else {
                        results.resultatsREP(DResultatsMascaret.litResultatsREP(ficEcrREP, t.maxResultatReprise));
                    }
                } else {
                    results.resultatsREP(null);
                }
                setProgression("Lecture des resultats", 60);
            } catch (Throwable e) {
                messagesAvertissements_ += "Le fichier de reprise est corrompu. Abandon de la lecture du fichier ";
                results.resultatsREP(null);
            }
            if (bInterrompu_ && !bRecupererResultats_) return;
            try {
                if (ficOPT.exists()) {
                    long tailleMaxOctet = Math.round(t.maxResultatOpthyca * 1024);
                    long tailleFichier = ficOPT.length();
                    if (tailleFichier > tailleMaxOctet) {
                        messagesAvertissements_ += "Le fichier r�sultat Opthyca : " + ficOPT.getAbsolutePath() + " est trop volumineux : " + (tailleFichier / 1024) + "Ko.\n";
                        messagesAvertissements_ += "Lecture non effectu�e, pour le r�cup�rer, veuillez le copier � partir de la machine " + h + ".\n\n";
                    } else {
                        SResultatsTemporelSpatial sres = DResultatsMascaret.litResultatsTemporelSpatial(ficOPT, t.maxResultatOpthyca, DResultatsMascaret.OPTYCA);
                        sres.resultatsPermanent = params.parametresCAS().parametresGen.code == 1;
                        results.resultatsTemporelSpatial(sres);
                    }
                } else if (ficRUB.exists()) {
                    long tailleMaxOctet = Math.round(t.maxResultatRubens * 1024);
                    long tailleFichier = ficRUB.length();
                    if (tailleFichier > tailleMaxOctet) {
                        messagesAvertissements_ += "Le fichier r�sultat Rubens : " + ficRUB.getAbsolutePath() + " est trop volumineux : " + (tailleFichier / 1024) + "Ko.\n";
                        messagesAvertissements_ += "Lecture non effectu�e, pour le r�cup�rer, veuillez le copier � partir de la machine " + h + ".\n\n";
                    } else {
                        results.resultatsTemporelSpatial(DResultatsMascaret.litResultatsTemporelSpatial(ficRUB, t.maxResultatRubens, DResultatsMascaret.RUBENS));
                    }
                } else {
                    results.resultatsTemporelSpatial(null);
                }
                setProgression("Lecture des resultats", 85);
            } catch (Throwable e) {
                messagesAvertissements_ += "Le fichier r�sultat hydraulique est corrompu. Abandon de la lecture du fichier ";
                results.resultatsTemporelSpatial(null);
            }
            if (bInterrompu_ && !bRecupererResultats_) return;
            try {
                if (ficCasierOPT.exists()) {
                    results.resultatsCasier(DResultatsMascaret.litResultatsTemporelSpatial(ficCasierOPT, 1000000., DResultatsMascaret.OPTYCA));
                } else if (ficCasierRUB.exists()) {
                    results.resultatsCasier(DResultatsMascaret.litResultatsTemporelSpatial(ficCasierRUB, 1000000., DResultatsMascaret.RUBENS));
                } else {
                    results.resultatsCasier(null);
                }
                setProgression("Lecture des resultats", 90);
                if (ficLiaisonOPT.exists()) {
                    results.resultatsLiaison(DResultatsMascaret.litResultatsTemporelSpatial(ficLiaisonOPT, 1000000., DResultatsMascaret.OPTYCA));
                } else if (ficLiaisonRUB.exists()) {
                    results.resultatsLiaison(DResultatsMascaret.litResultatsTemporelSpatial(ficLiaisonRUB, 1000000., DResultatsMascaret.RUBENS));
                } else {
                    results.resultatsLiaison(null);
                }
                setProgression("Lecture des resultats", 95);
            } catch (Throwable e) {
                messagesAvertissements_ += "Le fichier r�sultat liaison est corrompu. Abandon de la lecture du fichier ";
                results.resultatsLiaison(null);
            }
            if (bInterrompu_ && !bRecupererResultats_) return;
            try {
                if (ficCasierLIS.exists()) {
                    results.casierLIS(DResultatsMascaret.litResultatsLIS(ficCasierLIS, 1000000.));
                } else {
                    results.casierLIS(new SResultatsLIS(new byte[0]));
                }
                setProgression("Lecture des resultats", 97);
            } catch (Throwable e) {
                messagesAvertissements_ += "Le fichier listing casiers est corrompu. Abandon de la lecture du fichier ";
                results.casierLIS(new SResultatsLIS(new byte[0]));
            }
            if (bInterrompu_ && !bRecupererResultats_) return;
            try {
                if (ficLiaisonLIS.exists()) {
                    results.liaisonLIS(DResultatsMascaret.litResultatsLIS(ficLiaisonLIS, 1000000.));
                } else {
                    results.liaisonLIS(new SResultatsLIS(new byte[0]));
                }
            } catch (Throwable e) {
                messagesAvertissements_ += "Le fichier listing liaisons est corrompu. Abandon de la lecture du fichier ";
                results.liaisonLIS(new SResultatsLIS(new byte[0]));
            }
            setProgression("Lecture des resultats", 100);
            if (bInterrompu_ && !bRecupererResultats_) return;
            System.out.println("Lecture des resultats terminee.");
            setProgression(null, 0);
            if (bInterrompu_ && !bRecupererResultats_) return;
            log(c, "calcul termin�");
        } catch (Exception ex) {
            ex.printStackTrace();
            setProgression(null, 0);
            if (bInterrompu_) {
                return;
            }
            log(c, "erreur du calcul");
            throw new RuntimeException("Erreur de calcul\n" + ex.getLocalizedMessage());
        } catch (OutOfMemoryError er) {
            setProgression(null, 0);
            if (bInterrompu_) {
                return;
            }
            log(c, "erreur du calcul");
            throw new RuntimeException("Erreur de calcul, Pas assez de m�moire vive\n" + er.getLocalizedMessage());
        } catch (Error er) {
            setProgression(null, 0);
            if (bInterrompu_) {
                return;
            }
            log(c, "erreur du calcul");
            throw new RuntimeException("Erreur grave lors du calcul\n" + er.getLocalizedMessage());
        }
    }

    private String formatTemps(long millisec) {
        long nbHour = millisec / 3600000;
        long resteMilli = millisec - (nbHour * 3600000);
        long nbMin = resteMilli / 60000;
        resteMilli = resteMilli - (nbMin * 60000);
        long nbSec = resteMilli / 1000;
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(2);
        return nf.format(nbHour) + ":" + nf.format(nbMin) + ":" + nf.format(nbSec);
    }

    private File getFichier(IConnexion _c, String _ext, String _sousRep) {
        File fichier = getFichier(_c, _ext);
        return new File(fichier.getParent() + File.separator + _sousRep, fichier.getName());
    }

    public void ArreterCalcul(boolean bInterrompu, boolean bRecupererResultats) {
        bRecupererResultats_ = bRecupererResultats;
        bInterrompu_ = bInterrompu;
        if (bInterrompu) {
            if (etatCalcul_.equals("ecriture parametres")) {
                System.err.println("Interruption du calcul par l'utilisateur avant le d�but du calcul ! ");
                messagesAvertissements_ += "Interruption du calcul par l'utilisateur avant le d�but du calcul !\n\n";
            } else if (etatCalcul_.equals("calcul")) {
                System.err.println("Interruption du calcul par l'utilisateur ! (" + strOperation_.pourcentage + "% effectu�)");
                messagesAvertissements_ += "Interruption du calcul par l'utilisateur !(" + strOperation_.pourcentage + "% effectu�)\n\n";
            }
            if (execCalculEnCours != null) {
                execCalculEnCours.getProcess().destroy();
                setProgression("Calcul", 100);
            }
        }
    }

    public boolean IsAfficherResultats() {
        if (bInterrompu_) return bRecupererResultats_; else return true;
    }

    public void setNoyau(boolean noyau5_2) {
        noyau5_2_ = noyau5_2;
    }

    /**
   * Retourne le chemin complet pour le noyau selectionn�.
   */
    public String getCheminNoyau() {
        String ssrep;
        if (noyau5_2_) {
            ssrep = SOUS_REP_5_2;
        } else {
            ssrep = SOUS_REP_7_0;
        }
        return cheminServeur() + File.separator + ssrep;
    }

    /**
   * Cree une connexion pour la IPersonne _p en faisant appel a la methode mere de DTache. Si la connexion est correcte
   * (non nulle), des instances de IParametres et de IResultats sont crees et referencees dans la hashtable
   * <code>donnees_</code>.
   *
   * @return IConnexion cree par DTache.connexion(_p)
   * @see org.fudaa.dodico.objet.DTache
   */
    public IConnexion connexion(final IPersonne _p) {
        final IConnexion c = super.connexion(_p);
        try {
            if (CtuluLib.isJavaWebStart()) {
                String cheminFudaa = System.getenv("APPDATA") + File.separatorChar + "FudaaMascaret";
                String cheminServeur = cheminFudaa + File.separatorChar + "serveurs" + File.separatorChar + "mascaret";
                System.setProperty("FUDAA_SERVEUR", cheminServeur);
                String cheminNoyau5_2 = cheminServeur + File.separatorChar + SOUS_REP_5_2;
                String cheminNoyau7_0 = cheminServeur + File.separatorChar + SOUS_REP_7_0;
                File repNoyau5_2 = new File(cheminNoyau5_2);
                repNoyau5_2.mkdirs();
                File repNoyau6_1 = new File(cheminNoyau7_0);
                repNoyau6_1.mkdirs();
                copieResource(cheminServeur, SOUS_REP_5_2 + File.separatorChar + "Abaques.txt");
                copieResource(cheminServeur, SOUS_REP_5_2 + File.separatorChar + "dico.txt");
                copieResource(cheminServeur, SOUS_REP_5_2 + File.separatorChar + "mascaret");
                copieResource(cheminServeur, SOUS_REP_5_2 + File.separatorChar + "mascaret.exe");
                copieResource(cheminServeur, SOUS_REP_5_2 + File.separatorChar + "mascaret.sh");
                copieResource(cheminServeur, SOUS_REP_7_0 + File.separatorChar + "Abaques.txt");
                copieResource(cheminServeur, SOUS_REP_7_0 + File.separatorChar + "dico.txt");
                copieResource(cheminServeur, SOUS_REP_7_0 + File.separatorChar + "dico_sans_calage.txt");
                copieResource(cheminServeur, SOUS_REP_7_0 + File.separatorChar + "mascaret.exe");
            }
        } catch (IOException ex) {
            System.err.println("Resource impossible � copier");
            ex.printStackTrace();
        }
        return c;
    }

    private static void copieResource(String cheminServeur, String cheminResource) throws IOException {
        InputStream fluxIn = DCalculMascaret.class.getResourceAsStream("/serveurs/mascaret/" + cheminResource.replace('\\', '/'));
        if (fluxIn == null) {
            System.err.println("La resource " + "/serveurs/mascaret/" + cheminResource.replace('\\', '/') + " est introuvable");
        } else {
            OutputStream fluxOut = new FileOutputStream(cheminServeur + File.separatorChar + cheminResource);
            CtuluLibFile.copyStream(fluxIn, fluxOut, true, true);
        }
    }
}
