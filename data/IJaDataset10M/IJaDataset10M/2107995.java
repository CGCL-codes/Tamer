package org.fudaa.fudaa.sipor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import com.birosoft.liquid.LiquidLookAndFeel;
import com.memoire.bu.*;
import com.memoire.fu.FuLib;
import com.memoire.fu.FuLog;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.dodico.corba.objet.IConnexion;
import org.fudaa.dodico.corba.sipor.ICalculSipor;
import org.fudaa.dodico.corba.sipor.ICalculSiporHelper;
import org.fudaa.dodico.corba.sipor.IParametresSipor;
import org.fudaa.dodico.corba.sipor.IResultatsSipor;
import org.fudaa.dodico.corba.sipor.SParametresResultatsCompletSimulation;
import org.fudaa.dodico.objet.CExec;
import org.fudaa.dodico.sipor.DCalculSipor;
import org.fudaa.dodico.sipor.DParametresSipor;
import org.fudaa.dodico.sipor.DResultatsSipor;
import org.fudaa.fudaa.commun.dodico.FudaaDodicoTacheConnexion;
import org.fudaa.fudaa.commun.impl.FudaaCommonImplementation;
import org.fudaa.fudaa.commun.impl.FudaaStartupExitPreferencesPanel;
import org.fudaa.fudaa.commun.projet.FudaaProjet;
import org.fudaa.fudaa.ressource.FudaaResource;
import org.fudaa.fudaa.utilitaire.ServeurCopieEcran;

/**
 * L'implementation du client Sipor.
 * 
 * @version $Revision: 1.86 $ $Date: 2008-01-15 11:38:50 $ by $Author: bmarchan $
 * @author Adrien Hadoux
 */
public class SiporImplementation extends FudaaCommonImplementation {

    public static final boolean IS_APPLICATION = true;

    public static ICalculSipor SERVEUR_SIPOR;

    public static IConnexion CONNEXION_SIPOR;

    /**
   * Pointe vers parametres() de SERVEUR_SIPOR.
   */
    protected IParametresSipor siporParams_;

    /**
   * Pointe vers resultats() de SERVEUR_SIPOR.
   */
    protected IResultatsSipor siporResults_;

    protected static final BuInformationsSoftware isSipor_ = new BuInformationsSoftware();

    protected static final BuInformationsDocument idSipor_ = new BuInformationsDocument();

    protected Hashtable projets_;

    protected FudaaProjet projet_;

    protected SiporOutilsDonnees outils_;

    public static Color bleuSipor = new Color(54, 138, 191);

    public static Color bleuClairSipor = new Color(132, 166, 188);

    /**
   * Les donn�es de la simulation version 2006
   */
    SiporDataSimulation donnees_ = new SiporDataSimulation(this);

    /**
   * Fenetre de gestion des quais
   */
    SiporVisualiserQuais gestionQuais_;

    /**
   * Fenetre de gestion des cat�gories
   */
    SiporVisualiserNavires gestionNavires_;

    /**
   * Fenetre de gestion des chenaux
   */
    SiporVisualiserChenal gestionChenaux_;

    /**
   * Fenetre de gestion des cercles
   */
    SiporVisualiserCercles gestionCercles_;

    /**
   * Fenetre de gestion des ecluses
   */
    SiporVisualiserGares gestionGares_;

    /**
   * Fenetre de gestion des bassins
   */
    SiporVisualiserBassins gestionBassins_;

    /**
   * Fenetre de gestion des ecluses
   */
    SiporVisualiserEcluses gestionEcluses_;

    /**
   * fenetre de gestion des mar�es:
   */
    SiporFrameSaisieMaree gestionMarees_;

    /**
   * fenetre de gstion des donn�es g�n�rales
   */
    SiporFrameSaisieDonneesGenerales gestionDonneesGenerales_;

    /**
   * fenetre de gestion des bassins
   */
    SiporPanelTopologieBassin gestionTopoBassins_;

    /**
   * fenetre de gestion des ecluses
   */
    SiporPanelTopologieEcluse gestionTopoEcluses_;

    /**
   * fenetre de gestion des cheanux
   */
    SiporPanelTopologieChenal gestionTopoChenaux_;

    /**
   * fenetre de gestion des cercles
   */
    SiporPanelTopologieCercle gestionTopoCercles_;

    /**
   * fenetre de modelisation du port
   */
    SiporDessinerPortFrame gestionModelisation_;

    /**
   * fenetre de gestion des regles de navigations pour les chenaux
   */
    SiporPanelReglesNavigationChenal gestionNavigChenal_;

    /**
   * fenetre des regles de navig pour les cercles
   */
    SiporPanelReglesNavigationCercle gestionNavigCercle_;

    /**
   * fenetre des dur�es de parcours des chenaux
   */
    SiporPanelReglesParcoursChenal gestionDureeParcoursChenal_;

    /**
   * fenetre des dur�es de parcours dees cercles.
   */
    SiporPanelReglesParcoursCercle gestionDureeParcoursCercle_;

    /**
   * Fenetre qui gere le rappel des donn�es.
   */
    SiporFrameGenerationRappelDonnees rappelDonnees_;

    protected BuBrowserFrame fRappelDonnees_;

    protected BuBrowserFrame fStatistiquesFinales_;

    protected BuAssistant assistant_;

    protected BuHelpFrame aide_;

    protected BuTaskView taches_;

    protected SiporListeSimulations liste_;

    protected BuInternalFrame fGraphiques_;

    static {
        isSipor_.banner = SiporResource.SIPOR.getIcon("animation1");
        isSipor_.logo = SiporResource.SIPOR.getIcon("logo3");
        isSipor_.name = "Sipor";
        isSipor_.version = "0.20";
        isSipor_.date = "04-septembre -2006";
        isSipor_.rights = "Tous droits r�serv�s. CETMEF (c)1999,2006";
        isSipor_.contact = "alain.pourplanche@cetmef.equipement.gouv.fr";
        isSipor_.license = "GPL2";
        isSipor_.languages = "fr,en";
        isSipor_.http = "http://marina.cetmef.equipement.gouv.fr/fudaa/";
        isSipor_.update = "http://marina.cetmef.equipement.gouv.fr/fudaa/deltas/";
        isSipor_.man = "http://marina.cetmef.equipement.gouv.fr/fudaa/manuels/sipor/";
        isSipor_.authors = new String[] { "Adrien Hadoux" };
        isSipor_.contributors = new String[] { "Equipes Dodico, Ebli et Fudaa" };
        isSipor_.documentors = new String[] {};
        isSipor_.testers = new String[] { "Alain Pourplanche", "Alain Chambreuil" };
        idSipor_.name = "Etude";
        idSipor_.version = "0.02";
        idSipor_.organization = "CETMEF";
        idSipor_.author = "Adrien Hadoux";
        idSipor_.contact = "adrien.hadoux@tremplin-utc.net";
        idSipor_.date = FuLib.date();
        BuPrinter.INFO_LOG = isSipor_;
        BuPrinter.INFO_DOC = idSipor_;
    }

    protected BuButton supprimerSimu_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_enlever"), "Enlever");

    protected BuButton ajouterSimu_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_ajouter"), "Ajouter");

    public BuInformationsSoftware getInformationsSoftware() {
        return isSipor_;
    }

    public static BuInformationsSoftware informationsSoftware() {
        return isSipor_;
    }

    public BuInformationsDocument getInformationsDocument() {
        return idSipor_;
    }

    public void init() {
        super.init();
        final BuSplashScreen ss = getSplashScreen();
        projet_ = null;
        aide_ = null;
        try {
            setTitle(getInformationsSoftware().name + CtuluLibString.ESPACE + getInformationsSoftware().version);
            if (ss != null) {
                ss.setText("Menus et barres d'outils compl�t�s par Sipor");
                ss.setProgression(50);
            }
            final BuMenuBar mb = getMainMenuBar();
            mb.addMenu(construitMenuDonneesGenerales(IS_APPLICATION));
            mb.addMenu(construitMenuParametresConstruction(IS_APPLICATION));
            mb.addMenu(construitMenuTopologie(IS_APPLICATION));
            mb.addMenu(construitMenuReglesNavigations(IS_APPLICATION));
            mb.addMenu(construitMenuGeneration(IS_APPLICATION));
            mb.addMenu(construitMenuSimulation(IS_APPLICATION));
            mb.addMenu(construitMenuExploitation(IS_APPLICATION));
            mb.addMenu(construitMenuComparaisonSimulations(IS_APPLICATION));
            final BuToolBar tb = getMainToolBar();
            construitToolBar(tb);
            setEnabledForAction("CREER", true);
            setEnabledForAction("OUVRIR", true);
            setEnabledForAction("REOUVRIR", true);
            setEnabledForAction("ENREGISTRER", false);
            setEnabledForAction("ENREGISTRERSOUS", false);
            setEnabledForAction("FERMER", false);
            setEnabledForAction("QUITTER", true);
            setEnabledForAction("IMPORTER", false);
            setEnabledForAction("EXPORTER", false);
            setEnabledForAction("IMPRIMER", false);
            setEnabledForAction("PREFERENCE", true);
            setEnabledForAction("AIDE_INDEX", false);
            setEnabledForAction("AIDE_ASSISTANT", false);
            setEnabledForAction("TABLEAU", false);
            setEnabledForAction("GRAPHE", false);
            BuActionRemover.removeAction(getMainToolBar(), "COPIER");
            BuActionRemover.removeAction(getMainToolBar(), "COUPER");
            BuActionRemover.removeAction(getMainToolBar(), "COLLER");
            BuActionRemover.removeAction(getMainToolBar(), "DEFAIRE");
            BuActionRemover.removeAction(getMainToolBar(), "REFAIRE");
            BuActionRemover.removeAction(getMainToolBar(), "TOUTSELECTIONNER");
            BuActionRemover.removeAction(getMainToolBar(), "RANGERICONES");
            BuActionRemover.removeAction(getMainToolBar(), "RANGERPALETTES");
            BuActionRemover.removeAction(getMainToolBar(), "POINTEURAIDE");
            BuActionRemover.removeAction(getMainToolBar(), "RECHERCHER");
            BuActionRemover.removeAction(getMainToolBar(), "REMPLACER");
            BuActionRemover.removeAction(getMainToolBar(), "CONNECTER");
            final BuMenuRecentFiles mr = (BuMenuRecentFiles) mb.getMenu("REOUVRIR");
            if (mr != null) {
                mr.setPreferences(SiporPreferences.SIPOR);
                mr.setResource(SiporResource.SIPOR);
                mr.setEnabled(true);
            }
            if (ss != null) {
                ss.setText("Assistant, barres des t�ches");
                ss.setProgression(75);
            }
            assistant_ = new SiporAssistant();
            taches_ = new BuTaskView();
            BuPanel csp1 = new BuPanel(new BorderLayout());
            liste_ = new SiporListeSimulations(getApp());
            final BuScrollPane sp1 = new BuScrollPane(taches_);
            final BuScrollPane sp2 = new BuScrollPane(liste_);
            sp1.setPreferredSize(new Dimension(150, 80));
            sp2.setPreferredSize(new Dimension(150, 80));
            csp1.add(sp2, BorderLayout.CENTER);
            BuPanel b1 = new BuPanel(new GridLayout(2, 1, 2, 5));
            b1.add(ajouterSimu_);
            b1.add(supprimerSimu_);
            csp1.add(b1, BorderLayout.SOUTH);
            getMainPanel().getRightColumn().addToggledComponent(BuResource.BU.getString("Taches"), "TACHE", sp1, this);
            getMainPanel().getRightColumn().addToggledComponent("Simulations", "SIMULATIONS", csp1, this);
            ajouterSimu_.addActionListener(this);
            supprimerSimu_.addActionListener(this);
            if (ss != null) {
                ss.setText("Logo et barre des taches");
                ss.setProgression(95);
            }
            getMainPanel().setLogo(getInformationsSoftware().logo);
            getMainPanel().setTaskView(taches_);
            if (ss != null) {
                ss.setText("Termin�");
                ss.setProgression(100);
            }
        } catch (final Throwable t) {
            System.err.println("$$$ " + t);
            t.printStackTrace();
        }
        BuResource.BU.setIconFamily("crystal");
        LiquidLookAndFeel.setShowTableGrids(true);
    }

    public void start() {
        super.start();
        assistant_.changeAttitude(BuAssistant.PAROLE, "Bienvenue !\n" + getInformationsSoftware().name + " " + getInformationsSoftware().version);
        final BuMainPanel mp = getMainPanel();
        mp.doLayout();
        mp.validate();
        projets_ = new Hashtable();
        outils_ = new SiporOutilsDonnees(projet_);
        assistant_.addEmitters((Container) getApp());
        assistant_.changeAttitude(BuAssistant.ATTENTE, "Vous pouvez cr�er une\nnouvelle simulation\nou en ouvrir une");
        BuPreferences.BU.applyOn(this);
        SiporPreferences.SIPOR.applyOn(this);
    }

    protected void construitToolBar(final BuToolBar newtb) {
        newtb.removeAll();
        newtb.addToolButton("Creer", "CREER", FudaaResource.FUDAA.getIcon("crystal_creer"), false);
        newtb.addToolButton("Ouvrir", "OUVRIR", FudaaResource.FUDAA.getIcon("crystal_ouvrir"), false);
        newtb.addToolButton("Enreg", "ENREGISTRER", FudaaResource.FUDAA.getIcon("crystal_enregistrer"), false);
        newtb.addToolButton("nettoyer", "NETTOYER", FudaaResource.FUDAA.getIcon("crystal_valeur-initiale"), false);
        newtb.addSeparator();
        newtb.addSeparator();
        newtb.addSeparator();
        newtb.addSeparator();
        newtb.addToolButton("G�n�ral", "DONNEESGENERALES", FudaaResource.FUDAA.getIcon("analyser_"), false);
        newtb.addToolButton("Mar�es", "PARAMETREMAREE", FudaaResource.FUDAA.getIcon("crystal_parametre"), false);
        newtb.addSeparator();
        newtb.addToolButton("Cat.Nav", "PARAMETRECATEGORIE", FudaaResource.FUDAA.getIcon("crystal_parametre"), false);
        newtb.addToolButton("Quais", "PARAMETREQUAI", FudaaResource.FUDAA.getIcon("crystal_parametre"), false);
        newtb.addToolButton("Chenal", "PARAMETRECHENAL", FudaaResource.FUDAA.getIcon("crystal_parametre"), false);
        newtb.addSeparator();
        newtb.addToolButton("Topo chenal", "TOPOLOGIECHENAL", FudaaResource.FUDAA.getIcon("graphe_"), false);
        newtb.addToolButton("Mod�lisation port", "MODELISATIONTOPOLOGIE", FudaaResource.FUDAA.getIcon("crystal_colorier"), false);
        newtb.addSeparator();
        newtb.addToolButton("Reg.Nav chenaux", "REGLESNAVIGATIONCHENAL", FudaaResource.FUDAA.getIcon("configurer_"), false);
        newtb.addToolButton("Dur.Parcours chenaux", "REGLESDUREEPARCOURSCHENAL", FudaaResource.FUDAA.getIcon("configurer_"), false);
        newtb.addSeparator();
        newtb.addSeparator();
        newtb.addSeparator();
        newtb.addSeparator();
        newtb.addToolButton("Verif", "VERIFICATIONDONNEES", FudaaResource.FUDAA.getIcon("crystal_analyser"), false);
        newtb.addToolButton("Dup.simu", "DUPLIQUERSIMU", FudaaResource.FUDAA.getIcon("dupliquer_"), false);
        newtb.addToolButton("Calcul", "LANCEMENTCALCUL", FudaaResource.FUDAA.getIcon("crystal_executer"), false);
        newtb.addSeparator();
        newtb.addToolButton("Gen nav", "GRAPHEGENERATIONNAV", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        newtb.addToolButton("Duree parcours", "GRAPHEDUREEPARCOURS", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        newtb.addToolButton("Occupation globale quai", "TABLEAUOCCUPGLOBAL", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        newtb.addToolButton("Attente trajet", "ATTENTESPECIALISEE", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        newtb.addToolButton("Croisements", "CROISEMENTSCHENAUXTABLEAU", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
    }

    /**
   * Cr�� le menu donn�es g�n�rales
   * 
   * @param _app
   * 
   */
    protected BuMenu construitMenuDonneesGenerales(final boolean _app) {
        final BuMenu r = new BuMenu("G�n�ralit�s", "GENERALITES");
        BuMenuItem b = r.addMenuItem("donn�es g�n�rales", "DONNEESGENERALES", FudaaResource.FUDAA.getIcon("analyser"), false);
        b.setMnemonic(KeyEvent.VK_O);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        r.setMnemonic(KeyEvent.VK_G);
        r.getAccessibleContext().setAccessibleDescription("Saisie des donn�es g�n�rales");
        return r;
    }

    /**
   * Cr�� le menu simulation Ce menu permet d'entrer les parametres de saisies de la simulation -dans le menu
   * simulation, on a la possibilit� de cliquer sur les sous menus parmares quai, ecluses,chenaux.
   */
    protected BuMenu construitMenuParametresConstruction(final boolean _app) {
        final BuMenu r = new BuMenu("Parametres", "PARAMETRESSIMULATION");
        BuMenuItem b = r.addMenuItem("Gares", "PARAMETREGARE", FudaaResource.FUDAA.getIcon("crystal_parametre"), false);
        b.setMnemonic(KeyEvent.VK_G);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        b = r.addMenuItem("Bassins", "PARAMETREBASSIN", FudaaResource.FUDAA.getIcon("crystal_parametre"), false);
        b.setMnemonic(KeyEvent.VK_B);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        b = r.addMenuItem("Mar�es", "PARAMETREMAREE", FudaaResource.FUDAA.getIcon("crystal_parametre"), false);
        b.setMnemonic(KeyEvent.VK_M);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        b = r.addMenuItem("Tron�ons (chenaux)", "PARAMETRECHENAL", FudaaResource.FUDAA.getIcon("crystal_parametre"), false);
        b.setMnemonic(KeyEvent.VK_T);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        b = r.addMenuItem("Cercles", "PARAMETRECERCLE", FudaaResource.FUDAA.getIcon("crystal_parametre"), false);
        b.setMnemonic(KeyEvent.VK_C);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        b = r.addMenuItem("Ecluse", "PARAMETREECLUSE", FudaaResource.FUDAA.getIcon("crystal_parametre"), false);
        b.setMnemonic(KeyEvent.VK_E);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        b = r.addMenuItem("Quais", "PARAMETREQUAI", FudaaResource.FUDAA.getIcon("crystal_parametre"), false);
        b.setMnemonic(KeyEvent.VK_Q);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        b = r.addMenuItem("Cat�gories de Navire", "PARAMETRECATEGORIE", FudaaResource.FUDAA.getIcon("crystal_parametre"), false);
        b.setMnemonic(KeyEvent.VK_N);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        r.setMnemonic(KeyEvent.VK_P);
        r.getAccessibleContext().setAccessibleDescription("Saisie des param�tres du port");
        return r;
    }

    protected BuMenu construitMenuGeneration(final boolean _app) {
        final BuMenu r = new BuMenu("G�n�ration", "GENERATION");
        BuMenuItem b = r.addMenuItem("G�n�ration des navires", "GENARR", FudaaResource.FUDAA.getIcon(""), false);
        b = r.addMenuItem("Affichage des navires", "GENARR2", FudaaResource.FUDAA.getIcon(""), false);
        return r;
    }

    /**
   * Cr�ation du menu projet. Ce menu permet de lancer les calculs et d'exploiter les resultats retourne le menu de
   * construction du projet:
   */
    protected BuMenu construitMenuSimulation(final boolean _app) {
        final BuMenu r = new BuMenu("Simulation", "LANCEMENTSIMULATION");
        r.addSeparator("Projet");
        BuMenuItem b = r.addMenuItem("Rappel de donn�es", "RAPPELDONNEES", FudaaResource.FUDAA.getIcon(""), false);
        b.setMnemonic(KeyEvent.VK_R);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        b = r.addMenuItem("Dupliquer simulation", "DUPLIQUERSIMU", FudaaResource.FUDAA.getIcon("dupliquer_"), false);
        b.setMnemonic(KeyEvent.VK_I);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        r.addSeparator("Simulation");
        b = r.addMenuItem("Verification", "VERIFICATIONDONNEES", FudaaResource.FUDAA.getIcon("crystal_analyser"), false);
        b.setMnemonic(KeyEvent.VK_V);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        b = r.addMenuItem("Calculer", "LANCEMENTCALCUL", FudaaResource.FUDAA.getIcon("crystal_executer"), false);
        b.setMnemonic(KeyEvent.VK_L);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        r.addSeparator("Historique");
        b = r.addMenuItem("Acc�s � l'interface historique", "AUTRE2", false);
        b.setMnemonic(KeyEvent.VK_H);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        b = r.addMenuItem("Suppression fichier historique", "AUTRE1", false);
        b.setMnemonic(KeyEvent.VK_F);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.SHIFT_MASK | ActionEvent.CTRL_MASK));
        r.setMnemonic(KeyEvent.VK_U);
        r.getAccessibleContext().setAccessibleDescription("Menu Simulation");
        return r;
    }

    /**
   * Cr�ation du menu projet. Ce menu permet de lancer les calculs et d'exploiter les resultats retourne le menu de
   * construction du projet:
   */
    protected BuMenu construitMenuSimulation2(final boolean _app) {
        final BuMenu r = new BuMenu("Simulation", "SIMULATION");
        r.addMenuItem("Param�tres", "PARAMETRE", false);
        r.addMenuItem("Calculer", "CALCULER", FudaaResource.FUDAA.getIcon("crystal_calculer"), false);
        r.addMenuItem("Rappel des donn�es", "TEXTE", false);
        r.addMenuItem("Exploitation de la simulation", "TABLEAU", false);
        r.addMenuItem("Exploitation du projet", "GRAPHE", false);
        return r;
    }

    /**
   * Cr�ation du menu topologie. Ce menu permet de cr�er la topologie du port a parti des diff�rents �l�ments d�ja
   * inscrits Il est neccessaire d'avoir au moins 2 gares afin de pouvoir cr��er une topologie retourne le menu de
   * topologie
   */
    protected BuMenu construitMenuTopologie(final boolean _app) {
        final BuMenu r = new BuMenu("Topologie du port", "TOPOLOGIE");
        r.addSeparator("Topologie");
        BuMenuItem b = r.addMenuItem("Bassins", "TOPOLOGIEBASSIN", FudaaResource.FUDAA.getIcon("graphe_"), false);
        b.setMnemonic(KeyEvent.VK_B);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.SHIFT_MASK | ActionEvent.ALT_MASK));
        b = r.addMenuItem("Tron�ons (chenaux)", "TOPOLOGIECHENAL", FudaaResource.FUDAA.getIcon("graphe_"), false);
        b.setMnemonic(KeyEvent.VK_T);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.SHIFT_MASK | ActionEvent.ALT_MASK));
        b = r.addMenuItem("Ecluses", "TOPOLOGIEECLUSE", FudaaResource.FUDAA.getIcon("graphe_"), false);
        b.setMnemonic(KeyEvent.VK_E);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.SHIFT_MASK | ActionEvent.ALT_MASK));
        b = r.addMenuItem("Cercles d'�vitage", "TOPOLOGIECERCLE", FudaaResource.FUDAA.getIcon("graphe_"), false);
        b.setMnemonic(KeyEvent.VK_C);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.SHIFT_MASK | ActionEvent.ALT_MASK));
        r.addSeparator("Construction");
        b = r.addMenuItem("mod�lisation port", "MODELISATIONTOPOLOGIE", FudaaResource.FUDAA.getIcon("crystal_colorier"), false);
        b.setMnemonic(KeyEvent.VK_D);
        b.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.SHIFT_MASK));
        r.setMnemonic(KeyEvent.VK_E);
        r.getAccessibleContext().setAccessibleDescription("Topologie du port");
        return r;
    }

    /**
   * Cr�ation du menu DES REGLES DE NAVIGATION. Ce menu permet de lancer les saisies des regles de navigations retourne
   * le menu de construction du projet:
   */
    protected BuMenu construitMenuReglesNavigations(final boolean _app) {
        final BuMenu r = new BuMenu("Navigation ", "REGLESNAVIGATION");
        final BuMenu r1 = new BuMenu("Regles de croisements ", "NAVIGATIONCROISEMENT");
        final BuMenu r2 = new BuMenu("Durees de parcours ", "NAVIGATIONDUREES");
        r1.addMenuItem("Chenaux", "REGLESNAVIGATIONCHENAL", FudaaResource.FUDAA.getIcon("configurer_"), false);
        r1.addMenuItem("Cercles", "REGLESNAVIGATIONCERCLE", FudaaResource.FUDAA.getIcon("configurer_"), false);
        r2.addMenuItem("Chenaux", "REGLESDUREEPARCOURSCHENAL", FudaaResource.FUDAA.getIcon("configurer_"), false);
        r2.addMenuItem("Cercles", "REGLESDUREEPARCOURSCERCLE", FudaaResource.FUDAA.getIcon("configurer_"), false);
        r.addSubMenu(r1, false);
        r.addSubMenu(r2, false);
        r.setMnemonic(KeyEvent.VK_I);
        r.getAccessibleContext().setAccessibleDescription("Navigation");
        return r;
    }

    /**
   * Methode qui permet la cr�ation de resultats des parametres.
   * 
   * @param _app
   * @return le menu des resultats.
   */
    protected BuMenu construitMenuExploitation(final boolean _app) {
        final BuMenu r = new BuMenu("Resultats", "RESULTATSSIMU");
        final BuMenu r4 = new BuMenu("Occupation des quais", "OCC");
        final BuMenu r5 = new BuMenu("Attentes", "ATT");
        final BuMenu r6 = new BuMenu("Croisements", "CR");
        r.addMenuItem("G�n�ration de navires", "GRAPHEGENERATIONNAV", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        r.addMenuItem("Historique", "HISTORIQUETABLEAU", FudaaResource.FUDAA.getIcon("crystal_arbre"), false);
        r.addMenuItem("Durees de parcours", "GRAPHEDUREEPARCOURS", FudaaResource.FUDAA.getIcon("crystal_arbre"), false);
        r4.addMenuItem("Occupations globales", "TABLEAUOCCUPGLOBAL", FudaaResource.FUDAA.getIcon("crystal_arbre"), false);
        r4.addMenuItem("Occupations par quais", "TABLEAUOCCUPQUAI", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        r5.addMenuItem("attentes par element", "ATTENTEGENERALESELEMENTTABLEAU", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        r5.addMenuItem("attentes par cat�gorie", "ATTENTEGENERALESCATEGORIETABLEAU", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        r5.addMenuItem("attentes par trajet", "ATTENTESPECIALISEE", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        r6.addMenuItem("chenaux", "CROISEMENTSCHENAUXTABLEAU", FudaaResource.FUDAA.getIcon("crystal_arbre"), false);
        r6.addMenuItem("cercles", "CROISEMENTSCERCLESTABLEAU", FudaaResource.FUDAA.getIcon("crystal_arbre"), false);
        r.addSubMenu(r4, false);
        r.addSubMenu(r5, false);
        r.addSubMenu(r6, false);
        r.setMnemonic(KeyEvent.VK_R);
        r.getAccessibleContext().setAccessibleDescription("r�sultats de la simulation");
        return r;
    }

    protected BuMenu construitMenuAutre(final boolean _app) {
        final BuMenu r = new BuMenu("Autre", "AUTRE");
        return r;
    }

    protected BuMenu construitMenuComparaisonSimulations(final boolean _app) {
        final BuMenu r = new BuMenu("Comparaison", "COMPARESIMU");
        r.addMenuItem("G�n�ration de navires", "COMPARESIMU1", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        r.addMenuItem("Occupation globale des quais", "COMPARESIMU2", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        r.addMenuItem("Occupation d�taill�e des quais", "COMPARESIMU4", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        r.addMenuItem("dur�e de parcours", "COMPARESIMU3", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        final BuMenu r1 = new BuMenu("Attentes", "ACOMPARE");
        r1.addMenuItem("Attentes �l�ments", "COMPARESIMU5", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        r1.addMenuItem("Attentes trajet", "COMPARESIMU6", FudaaResource.FUDAA.getIcon("crystal_graphe"), false);
        r.addSubMenu(r1, true);
        r.setMnemonic(KeyEvent.VK_C);
        r.getAccessibleContext().setAccessibleDescription("Comparaisons de simulation");
        return r;
    }

    protected BuMenu construitMenuProjets(final boolean _app) {
        final BuMenu menu = new BuMenu("Projet", "PROJET");
        menu.addMenuItem("Enregistrer", "ENREGISTRER_LISTE", false);
        menu.addMenuItem("Ouvrir", "OUVRIR_LISTE", true);
        return menu;
    }

    public void actionPerformed(final ActionEvent _evt) {
        String action = _evt.getActionCommand();
        System.err.println("ACTION=" + action);
        String fichier = SiporLib.actionArgumentStringOnly(action);
        action = SiporLib.actionStringWithoutArgument(action);
        if (action.equals("DONNEESGENERALES")) {
            parametreDonneesGenerales();
        }
        if (action.equals("PARAMETREQUAI")) {
            if (this.donnees_.listebassin_.listeBassins_.size() > 0) {
                parametreQuais();
                assistant_.changeAttitude(BuAssistant.ATTENTE, "Je veux plein de quais!!\n" + getInformationsSoftware().name + " " + getInformationsSoftware().version);
            } else {
                new BuDialogError(getApp(), getInformationsSoftware(), "Erreur,Il faut au moins un bassin pour cr�er un quai!! ").activate();
            }
        } else if (action.equals("PARAMETREECLUSE")) {
            parametreEcluse();
            assistant_.changeAttitude(BuAssistant.PAROLE, "Mes ecluses... \nOu les ai-je mises???\n" + getInformationsSoftware().name + " " + getInformationsSoftware().version);
        } else if (action.equals("PARAMETRECERCLE")) {
            parametreCercle();
            assistant_.changeAttitude(BuAssistant.PAROLE, "Va y avoir du traffic!\n" + getInformationsSoftware().name + " " + getInformationsSoftware().version);
        } else if (action.equals("PARAMETREMAREE")) {
            parametreMaree();
            assistant_.changeAttitude(BuAssistant.PAROLE, "ah non il pas l'heure de\n se baigner....\n" + getInformationsSoftware().name + " " + getInformationsSoftware().version);
        } else if (action.equals("PARAMETRECHENAL")) {
            parametreChenal();
            assistant_.changeAttitude(BuAssistant.PAROLE, "Par ou je passe??\n" + getInformationsSoftware().name + " " + getInformationsSoftware().version);
        } else if (action.equals("PARAMETRECATEGORIE")) {
            if (this.donnees_.listeGare_.listeGares_.size() <= 0) {
                new BuDialogError(getApp(), getInformationsSoftware(), "Erreur,Il faut au moins une gare de d�part pour cr�er un navire!! ").activate();
            } else if (this.donnees_.lQuais_.lQuais_.size() <= 0) {
                new BuDialogError(getApp(), getInformationsSoftware(), "Erreur,Il faut au moins un quai preferentiel pour cr�er un navire!! ").activate();
            } else {
                parametreNavire();
                assistant_.changeAttitude(BuAssistant.PAROLE, "Oh mon bateau!! tu es\n le plus beau de tous\n les bateaux!\n" + getInformationsSoftware().name + " " + getInformationsSoftware().version);
            }
        } else if (action.equals("PARAMETREBASSIN")) {
            parametreBassin();
            assistant_.changeAttitude(BuAssistant.PAROLE, "Qu'est ce qu'on est bien\n quand on est dans son bassin!!\n" + getInformationsSoftware().name + " " + getInformationsSoftware().version);
        } else if (action.equals("PARAMETREGARE")) {
            parametreGare();
            assistant_.changeAttitude(BuAssistant.PAROLE, "Il en faut, il en faut!!\n" + getInformationsSoftware().name + " " + getInformationsSoftware().version);
        } else if (action.equals("TOPOLOGIEBASSIN")) {
            activerCommandesTopologie(1);
        } else if (action.equals("TOPOLOGIECHENAL")) {
            activerCommandesTopologie(3);
        } else if (action.equals("TOPOLOGIEECLUSE")) {
            activerCommandesTopologie(2);
        } else if (action.equals("TOPOLOGIECERCLE")) {
            activerCommandesTopologie(4);
        } else if (action.equals("MODELISATIONTOPOLOGIE")) {
            activerModelisation();
        } else if (action.equals("REGLESNAVIGATIONCHENAL")) {
            activerReglesNavigation();
        } else if (action.equals("REGLESNAVIGATIONCERCLE")) {
            activerReglesNavigation2();
        } else if (action.equals("GENARR")) {
            lancementGenarr();
        } else if (action.equals("GENARR2")) {
            new BuTaskOperation(this, "generation navires", "affichageGenarr").start();
        } else if (action.equals("VERIFICATIONDONNEES")) {
            activerVerficiationDonnees();
        } else if (action.equals("DUPLIQUERSIMU")) {
            donnees_.dupliquerSimulation();
        } else if (action.equals("REGLESDUREEPARCOURSCHENAL")) {
            activerReglesDuree();
        } else if (action.equals("REGLESDUREEPARCOURSCERCLE")) {
            activerReglesDuree2();
        } else if (action.equals("RAPPELDONNEES")) {
            activationRappelDonnees();
        } else if (action.equals("LANCEMENTCALCUL")) {
            new BuTaskOperation(this, "Calcul", "lancementCalcul").start();
        } else if (action.equals("GRAPHEGENERATIONNAV")) {
            new BuTaskOperation(this, "generation navires", "activerResultatsgenerationNavires").start();
        } else if (action.equals("HISTORIQUETABLEAU") || action.equals("AUTRE2")) {
            new BuTaskOperation(this, "historique", "activerResultatHistorique").start();
        } else if (action.equals("AUTRE1")) {
            new BuTaskOperation(this, "Suppression Hisotrique", "supprimerHistoriqueSimulation").start();
        } else if (action.equals("GRAPHEDUREEPARCOURS")) {
            new BuTaskOperation(this, "durees parcours", "activerResultatDureesParcours").start();
        } else if (action.equals("CROISEMENTSCHENAUXTABLEAU")) {
            new BuTaskOperation(this, "croisements chenaux", "activerResultatCroisementsChenaux").start();
        } else if (action.equals("CROISEMENTSCERCLESTABLEAU")) {
            new BuTaskOperation(this, "croisements cercles", "activerResultatCroisementsCercles").start();
        } else if (action.equals("ATTENTEGENERALESELEMENTTABLEAU")) {
            new BuTaskOperation(this, "attentes elements", "activerResultatAttentesGeneralesElement").start();
        } else if (action.equals("ATTENTEGENERALESCATEGORIETABLEAU")) {
            new BuTaskOperation(this, "attentes cat�gories", "activerResultatAttentesGeneralesCategorie").start();
        } else if (action.equals("ATTENTESPECIALISEE")) {
            new BuTaskOperation(this, "attentes trajet", "activerResultatAttentesSpecialisee").start();
        } else if (action.equals("TABLEAUOCCUPGLOBAL")) {
            new BuTaskOperation(this, "occupation globale", "activerResultatOccupGlobales").start();
        } else if (action.equals("TABLEAUOCCUPQUAI")) {
            new BuTaskOperation(this, "occupation detaillee", "activerResultatOccupQuai").start();
        } else if (action.equals("COMPARESIMU1")) {
            new BuTaskOperation(this, "comparaison nb navires", "activerComparaisonNombreNavires").start();
        } else if (action.equals("COMPARESIMU2")) {
            new BuTaskOperation(this, "comparaison nb navires", "activerComparaisonOccupationGlobale").start();
        } else if (action.equals("COMPARESIMU3")) {
            new BuTaskOperation(this, "comparaison dur�e de parcours", "activerComparaisonDureesParcours").start();
        } else if (action.equals("COMPARESIMU4")) {
            new BuTaskOperation(this, "comparaison d'occupation aux quais", "activerComparaisonOccupationQuais").start();
        } else if (action.equals("COMPARESIMU5")) {
            new BuTaskOperation(this, "comparaison d'occupation aux quais", "activerComparaisonAttenteGenerale").start();
        } else if (action.equals("COMPARESIMU6")) {
            new BuTaskOperation(this, "comparaison d'occupation aux quais", "activerComparaisonAttenteTrajet").start();
        } else if (action.equals("CREER")) {
            creer();
        } else if (action.equals("CREER2")) {
            creer();
        } else if (action.equals("OUVRIR")) {
            ouvrir(null);
            setEnabledForAction("REOUVRIR", true);
        } else if (action.equals("OUVRIR2")) {
            ouvrir(null);
            setEnabledForAction("REOUVRIR", true);
        } else if (action.equals("REOUVRIR")) {
            ouvrir(fichier);
            System.out.println("fichier reouvrir: " + fichier);
        } else if (action.equals("NETTOYER")) {
            fermerToutesLesInterfaces();
        } else if (action.equals("ENREGISTRER")) {
            enregistrer();
        } else if (action.equals("ENREGISTRERSOUS")) {
            enregistrerSous();
        } else if (action.equals("FERMER")) {
            fermer();
        } else if (action.equals("QUITTER")) {
            exit();
        } else if (_evt.getSource().equals(supprimerSimu_)) {
            supprimerSimulation();
        } else if (_evt.getSource().equals(ajouterSimu_)) {
            ouvrir(null);
        } else if (action.equals("ENREGISTRER_LISTE")) {
            liste_.enregistre();
        } else if (action.equals("OUVRIR_LISTE")) {
            liste_.ouvrir();
        } else {
            super.actionPerformed(_evt);
        }
    }

    public void oprServeurCopie() {
        System.err.println("Lancement du serveur de copie d'ecran");
        new ServeurCopieEcran(getMainPanel(), "ScreenSpy");
    }

    /**
   * Retire une �ventuelle extension au nom du fichier du projet et lance la lecture du fichier de r�sultats brutes
   * correspondants.
   */
    public void recevoirResultats() {
        projet_.addResult(SiporResource.resultats, siporResults_.litResultatsSipor());
    }

    /**
   * Methode permettant d'effectuer le lancement de al simulation envoyer les parametres au server via la classe
   * SiporParams_
   */
    public void oprCalculer() {
        lancementCalcul();
    }

    /**
   * Commandes activ�es d�s qu'une simulation est charg�e. Les commandes sont activ�es apres chargmeent d un projet
   */
    protected void activerCommandesSimulation() {
        setEnabledForAction("PARAMETRE", true);
        setEnabledForAction("TEXTE", true);
        setEnabledForAction("ENREGISTRER", true);
        setEnabledForAction("ENREGISTRERSOUS", true);
        setEnabledForAction("FERMER", true);
        setEnabledForAction("CALCULER", true);
        setEnabledForAction("ENREGISTRER_LISTE", true);
        setEnabledForAction("DONNEESGENERALES", true);
        setEnabledForAction("PARAMETREQUAI", true);
        setEnabledForAction("PARAMETREECLUSE", true);
        setEnabledForAction("PARAMETREBASSIN", true);
        setEnabledForAction("PARAMETREGARE", true);
        setEnabledForAction("PARAMETRECERCLE", true);
        setEnabledForAction("PARAMETRECATEGORIE", true);
        setEnabledForAction("PARAMETRECHENAL", true);
        setEnabledForAction("PARAMETREMAREE", true);
        setEnabledForAction("TOPOLOGIEBASSIN", true);
        setEnabledForAction("TOPOLOGIEECLUSE", true);
        setEnabledForAction("TOPOLOGIECHENAL", true);
        setEnabledForAction("TOPOLOGIECERCLE", true);
        setEnabledForAction("MODELISATIONTOPOLOGIE", true);
        setEnabledForAction("NAVIGATIONCROISEMENT", true);
        setEnabledForAction("NAVIGATIONDUREES", true);
        setEnabledForAction("REGLESNAVIGATION", true);
        setEnabledForAction("REGLESNAVIGATIONCHENAL", true);
        setEnabledForAction("REGLESNAVIGATIONCERCLE", true);
        setEnabledForAction("REGLESDUREEPARCOURSCHENAL", true);
        setEnabledForAction("REGLESDUREEPARCOURSCERCLE", true);
        setEnabledForAction("VERIFICATIONDONNEES", true);
        setEnabledForAction("DUPLIQUERSIMU", true);
        setEnabledForAction("NETTOYER", true);
        setEnabledForAction("GENARR", true);
        setEnabledForAction("GENARR2", true);
        activerCommandesResultatsSimulation();
    }

    void activerCommandesResultatsSimulation() {
        if (this.donnees_.params_.niveau >= 1) {
            setEnabledForAction("RAPPELDONNEES", true);
        }
        if (this.donnees_.params_.niveau >= 1) {
            setEnabledForAction("LANCEMENTCALCUL", true);
        }
        if (this.donnees_.params_.niveau >= 2) {
            setEnabledForAction("AUTRE1", true);
            setEnabledForAction("AUTRE2", true);
            setEnabledForAction("GRAPHEGENERATIONNAV", true);
            setEnabledForAction("HISTORIQUETABLEAU", true);
            setEnabledForAction("GRAPHEDUREEPARCOURS", true);
            setEnabledForAction("ATTENTEGENERALESELEMENTTABLEAU", true);
            setEnabledForAction("ATTENTEGENERALESCATEGORIETABLEAU", true);
            setEnabledForAction("ATTENTESPECIALISEE", true);
            setEnabledForAction("TABLEAUOCCUPGLOBAL", true);
            setEnabledForAction("TABLEAUOCCUPQUAI", true);
            setEnabledForAction("CROISEMENTSCHENAUXTABLEAU", true);
            setEnabledForAction("CROISEMENTSCERCLESTABLEAU", true);
            setEnabledForAction("RESULTATSSIMU", true);
            setEnabledForAction("GN", true);
            setEnabledForAction("HIST", true);
            setEnabledForAction("DURR", true);
            setEnabledForAction("OCC", true);
            setEnabledForAction("ATT", true);
            setEnabledForAction("CR", true);
            setEnabledForAction("COMPARESIMU1", true);
            setEnabledForAction("COMPARESIMU2", true);
            setEnabledForAction("COMPARESIMU3", true);
            setEnabledForAction("COMPARESIMU4", true);
            setEnabledForAction("COMPARESIMU5", true);
            setEnabledForAction("COMPARESIMU6", true);
        }
    }

    /**
   * M�thode qui active la saisie des topologie des que les conditions suivantes sont remplies: - au moins 2 gares
   * saisies par l'utilisateur dans la partie donn�e - voir avec Alain P et C pour d'autre règles d'activation de la
   * siaie de la topologie Les commandes sont activ�es des que les conditions ci dessus sont remplies Fonctionnement de
   * la methode:
   * 
   * @param numeroParametreConsidere un entier en parametre d'entr�e qui indique de quel �l�ment de saisie il s'agit
   *          exemple: 1 => bassin 2 => Ecluse......
   */
    protected void activerCommandesTopologie(final int numeroParametreConsidere) {
        if (numeroParametreConsidere == 1) {
            if (donnees_.listeGare_.listeGares_.size() >= 1) {
                if (numeroParametreConsidere == 1 && donnees_.listebassin_.listeBassins_.size() >= 1) {
                    this.addInternalFrame(new SiporPanelTopologieBassin(donnees_));
                } else {
                    new BuDialogError(getApp(), getInformationsSoftware(), "Erreur,aucun bassin existant!! ").activate();
                }
            } else {
                new BuDialogError(getApp(), getInformationsSoftware(), "Il faut au moins une gare pour la topologie du bassin! Veuillez d'abord cr�er une gare").activate();
                return;
            }
        }
        if (numeroParametreConsidere != 1) {
            if (donnees_.listeGare_.listeGares_.size() >= 2) {
                if (numeroParametreConsidere == 2) {
                    if (donnees_.listeEcluse_.listeEcluses_.size() >= 1) {
                        this.addInternalFrame(new SiporPanelTopologieEcluse(donnees_));
                    } else {
                        new BuDialogError(getApp(), getInformationsSoftware(), "Erreur,aucune ecluse existante!! ").activate();
                    }
                }
                if (numeroParametreConsidere == 3) {
                    if (donnees_.listeChenal_.listeChenaux_.size() >= 1) {
                        this.addInternalFrame(new SiporPanelTopologieChenal(donnees_));
                    } else {
                        new BuDialogError(getApp(), getInformationsSoftware(), "Erreur,aucun chenal existant!! ").activate();
                    }
                }
                if (numeroParametreConsidere == 4) {
                    if (donnees_.listeCercle_.listeCercles_.size() >= 1) {
                        this.addInternalFrame(new SiporPanelTopologieCercle(donnees_));
                    } else {
                        new BuDialogError(getApp(), getInformationsSoftware(), "Erreur,aucun cercle existant!! ").activate();
                    }
                }
            } else {
                new BuDialogError(getApp(), getInformationsSoftware(), "Il faut au moins deux gares pour la topologie! Veuillez d'abord cr�er suffisament de gares").activate();
            }
        }
    }

    /**
   * Methode qui r�alise els controles et lance la fenetre de modelisation des ports
   */
    public void activerModelisation() {
        if (donnees_.listeGare_.listeGares_.size() >= 2) {
            if (this.gestionModelisation_ == null) {
                FuLog.debug("interface nulle");
                gestionModelisation_ = new SiporDessinerPortFrame(this.donnees_, this);
                gestionModelisation_.setVisible(true);
                addInternalFrame(gestionModelisation_);
            } else {
                FuLog.debug("interface ferm�e");
                if (gestionModelisation_.isClosed()) {
                    addInternalFrame(gestionModelisation_);
                } else {
                    FuLog.debug("interface cas de figur restant autre que null et fermeture");
                    activateInternalFrame(gestionModelisation_);
                    addInternalFrame(gestionModelisation_);
                }
            }
        } else {
            new BuDialogError(getApp(), getInformationsSoftware(), "Il faut au moins deux gares pour dessiner la topologie! Veuillez d'abord cr�er suffisament de gares").activate();
        }
    }

    /**
   * Methode qui r�alise les controles et lance la fenetre de saisies des regles de navigation.
   */
    protected void activerReglesNavigation() {
        if (this.donnees_.listeChenal_.listeChenaux_.size() <= 0) {
            new BuDialogError(getApp(), getInformationsSoftware(), "Il faut au moins un chenal pour lancer les regles de navigations!Veuillez d'abord cr�er un chenal").activate();
        } else if (this.donnees_.categoriesNavires_.listeNavires_.size() <= 0) {
            new BuDialogError(getApp(), getInformationsSoftware(), "Il faut au moins une cat�gorie de navire pour lancer les regles de navigations!Veuillez d'abord cr�er une cat�gorie").activate();
        } else {
            gestionNavigChenal_ = new SiporPanelReglesNavigationChenal(this.donnees_);
            addInternalFrame(gestionNavigChenal_);
        }
    }

    /**
   * Methode de lancement des regles de navigations du cercle.
   */
    protected void activerReglesNavigation2() {
        if (this.donnees_.listeCercle_.listeCercles_.size() <= 0) {
            new BuDialogError(getApp(), getInformationsSoftware(), "Il faut au moins un cercle pour lancer les regles de navigations!Veuillez d'abord cr�er un cercle").activate();
        } else if (this.donnees_.categoriesNavires_.listeNavires_.size() <= 0) {
            new BuDialogError(getApp(), getInformationsSoftware(), "Il faut au moins une cat�gorie de navire pour lancer les regles de navigations!Veuillez d'abord cr�er une cat�gorie").activate();
        } else {
            this.addInternalFrame(new SiporPanelReglesNavigationCercle(donnees_));
        }
    }

    /**
   * Methode de lancement des durees de parcours des cheneaux
   */
    protected void activerReglesDuree() {
        if (this.donnees_.listeChenal_.listeChenaux_.size() <= 0) {
            new BuDialogError(getApp(), getInformationsSoftware(), "Il faut au moins un chenal pour lancer les regles de navigations!Veuillez d'abord cr�er un chenal").activate();
        } else if (this.donnees_.categoriesNavires_.listeNavires_.size() <= 0) {
            new BuDialogError(getApp(), getInformationsSoftware(), "Il faut au moins une cat�gorie de navire pour lancer les regles de navigations!Veuillez d'abord cr�er une cat�gorie").activate();
        } else {
            this.addInternalFrame(new SiporPanelReglesParcoursChenal(donnees_));
        }
    }

    /**
   * Methode de lancement des durees de parcours des cercles
   */
    protected void activerReglesDuree2() {
        if (this.donnees_.listeCercle_.listeCercles_.size() <= 0) {
            new BuDialogError(getApp(), getInformationsSoftware(), "Il faut au moins un cercle pour lancer les regles de navigations!Veuillez d'abord cr�er un cercle").activate();
        } else if (this.donnees_.categoriesNavires_.listeNavires_.size() <= 0) {
            new BuDialogError(getApp(), getInformationsSoftware(), "Il faut au moins une cat�gorie de navire pour lancer les regles de navigations!Veuillez d'abord cr�er une cat�gorie").activate();
        } else {
            this.addInternalFrame(new SiporPanelReglesParcoursCercle(donnees_));
        }
    }

    /**
   * Methode tres importante de verification des donn�es avant lancement de la simulation;
   */
    protected void activerVerficiationDonnees() {
        if (this.donnees_.verificationDonnees()) {
            if (this.donnees_.params_.niveau < 1) {
                this.donnees_.params_.niveau = 1;
            }
            activerCommandesResultatsSimulation();
        }
    }

    /**
   * Methode qui place le niveau de s�curite de l'application au plus bas.
   * Consequence: on doit redemarrer le test de v�rification des coh�rences des donn�es.
   * Utiliser: on utilise cette m�thode dans le cas ou l'on modifie des donn�es.
   *
   */
    public void baisserNiveauSecurite() {
        this.donnees_.params_.niveau = 0;
        setEnabledForAction("RAPPELDONNEES", false);
        setEnabledForAction("LANCEMENTCALCUL", false);
        setEnabledForAction("AUTRE1", false);
        setEnabledForAction("AUTRE2", false);
        setEnabledForAction("GRAPHEGENERATIONNAV", false);
        setEnabledForAction("HISTORIQUETABLEAU", false);
        setEnabledForAction("GRAPHEDUREEPARCOURS", false);
        setEnabledForAction("ATTENTEGENERALESELEMENTTABLEAU", false);
        setEnabledForAction("ATTENTEGENERALESCATEGORIETABLEAU", false);
        setEnabledForAction("ATTENTESPECIALISEE", false);
        setEnabledForAction("TABLEAUOCCUPGLOBAL", false);
        setEnabledForAction("TABLEAUOCCUPQUAI", false);
        setEnabledForAction("CROISEMENTSCHENAUXTABLEAU", false);
        setEnabledForAction("CROISEMENTSCERCLESTABLEAU", false);
        setEnabledForAction("GN", false);
        setEnabledForAction("HIST", false);
        setEnabledForAction("DURR", false);
        setEnabledForAction("OCC", false);
        setEnabledForAction("ATT", false);
        setEnabledForAction("CR", false);
        setEnabledForAction("COMPARESIMU1", false);
        setEnabledForAction("COMPARESIMU2", false);
        setEnabledForAction("COMPARESIMU3", false);
        setEnabledForAction("COMPARESIMU4", false);
        setEnabledForAction("COMPARESIMU5", false);
        setEnabledForAction("COMPARESIMU6", false);
    }

    /**
   * methode de lancement de calculs de sipor
   */
    public void lancementCalcul() {
        enregistrer();
        SiporProgressFrame fenetreProgression = new SiporProgressFrame();
        this.addInternalFrame(fenetreProgression);
        final BuMainPanel mp = getMainPanel();
        mp.setProgression(20);
        mp.setMessage("Envoie des param�tres au noyau de calcul");
        mp.setProgression(10);
        try {
            fenetreProgression.miseAjourBarreProgression(0, "Ecriture des param�tres...", "Ecriture des param�tres cat�gories de navire");
            DParametresSipor.ecritDonneesCategoriesNavires(this.donnees_.params_.navires, this.donnees_.projet_.getFichier());
            fenetreProgression.miseAjourBarreProgression(2, "Ecriture des param�tres donn�es g�n�rales");
            DParametresSipor.ecritDonneesGenerales(this.donnees_.params_.donneesGenerales, this.donnees_.projet_.getFichier());
            fenetreProgression.miseAjourBarreProgression(4, "Ecriture des param�tres �cluses");
            DParametresSipor.ecritDonneesEcluses(this.donnees_.params_.ecluses, this.donnees_.projet_.getFichier());
            fenetreProgression.miseAjourBarreProgression(6, "Ecriture des param�tres bassins");
            DParametresSipor.ecritDonneesBassins(this.donnees_.params_.bassins, this.donnees_.projet_.getFichier());
            fenetreProgression.miseAjourBarreProgression(8, "Ecriture des param�tres gares");
            DParametresSipor.ecritDonneesGares(this.donnees_.params_.gares, this.donnees_.projet_.getFichier());
            fenetreProgression.miseAjourBarreProgression(10, "Ecriture des param�tres mar�e");
            DParametresSipor.ecritDonneesMarees(this.donnees_.params_.maree, this.donnees_.projet_.getFichier());
            fenetreProgression.miseAjourBarreProgression(12, "Ecriture des param�tres quais");
            DParametresSipor.ecritDonneesQuais(this.donnees_.params_.quais, this.donnees_.projet_.getFichier());
            fenetreProgression.miseAjourBarreProgression(14, "Ecriture des param�tres cercles d'�vitage");
            DParametresSipor.ecritDonneesCercles(this.donnees_.params_.cercles, this.donnees_.projet_.getFichier());
            fenetreProgression.miseAjourBarreProgression(16, "Ecriture des param�tres chenaux");
            DParametresSipor.ecritDonneesChenal(this.donnees_.params_.cheneaux, this.donnees_.projet_.getFichier());
            fenetreProgression.miseAjourBarreProgression(18, "Ecriture des dur�es de parcours chenaux");
            DParametresSipor.ecritDonneesDureeParcoursChenal(this.donnees_.params_, this.donnees_.projet_.getFichier());
            fenetreProgression.miseAjourBarreProgression(20, "Ecriture des dur�es de parcours cercles d'�vitage");
            DParametresSipor.ecritDonneesDureeParcoursCercle(this.donnees_.params_, this.donnees_.projet_.getFichier());
            fenetreProgression.miseAjourBarreProgression(22, "Ecriture des croisements cercles");
            DParametresSipor.ecritDonneesCroisementsCercle(this.donnees_.params_.cercles, this.donnees_.projet_.getFichier());
            fenetreProgression.miseAjourBarreProgression(5, "Ecriture des croisements chenaux");
            DParametresSipor.ecritDonneesCroisementsChenal(this.donnees_.params_.cheneaux, this.donnees_.projet_.getFichier());
        } catch (final Exception ex) {
        }
        mp.setMessage("Execution du calcul...");
        mp.setProgression(25);
        fenetreProgression.miseAjourBarreProgression(25, "Lancement du noyau de calcul...", "");
        File fic = new File(donnees_.projet_.getFichier() + ".his");
        if (fic.exists()) {
            int confirmation = new BuDialogConfirmation(donnees_.application_.getApp(), SiporImplementation.isSipor_, " Un fichier historique existe.\n Ce fichier contient les r�sultats d'une pr�c�dente ex�cution \n du noyau de calcul.\n Voulez-vous relancer le noyau de calcul?\n Si vous voulez directement lire le fichier historique,\n tapez non.").activate();
            if (confirmation == 0) {
                if (!ExecuterCommandeDepuisServeur(1, donnees_.projet_.getFichier())) return;
            }
        } else {
            if (!ExecuterCommandeDepuisServeur(1, donnees_.projet_.getFichier())) return;
        }
        fenetreProgression.miseAjourBarreProgression(60, "V�rification de la g�n�ration...", "");
        File fichier = new File(donnees_.projet_.getFichier() + ".his");
        if (!fichier.exists()) {
            new BuDialogError(null, SiporImplementation.isSipor_, "Le fichier historique est introuvable.\n Veuillez relancer le calcul (onglet Simulation, 'Calculer')").activate();
            return;
        }
        mp.setMessage("Fin calcul du noyau, exploitation des r�sultats.");
        mp.setProgression(65);
        fenetreProgression.miseAjourBarreProgression(65, "Lecture des r�sultats...", "");
        this.donnees_.listeResultatsSimu_ = DResultatsSipor.litResultatsSipor(this.donnees_.projet_.getFichier() + ".his");
        fenetreProgression.miseAjourBarreProgression(70, "Exploitation des r�sultats...", "");
        calculExploitationResultats(fenetreProgression);
        new BuDialogMessage(donnees_.application_.getApp(), donnees_.application_.getInformationsSoftware(), "La simulation est termin�e, vous pouvez visualiser les r�sultats.\n Un fichier historique \n\n" + donnees_.projet_.getFichier() + ".his \n\n a �t� cr��. \n Il a servi � r�aliser les statistiques.\n Attention ce fichier occupe beaucoup de place m�moire. \n Vous pouvez le supprimer (onglet Simulation).").activate();
        fenetreProgression.terminaison();
    }

    /**
   * Methode qui realise les calculs de toutes les interfaces de SIPOR.
   */
    public void calculExploitationResultats(SiporProgressFrame fenetreProgression) {
        final BuMainPanel mp = getMainPanel();
        this.donnees_.params_.ResultatsCompletsSimulation = new SParametresResultatsCompletSimulation();
        fenetreProgression.miseAjourBarreProgression(70, "exploitation des g�n�rations de navires.");
        mp.setMessage("exploitation des g�n�rations de navires.");
        mp.setProgression(70);
        SiporAlgorithmeGenerationBateaux.calcul(donnees_);
        fenetreProgression.miseAjourBarreProgression(75, "calcul des generation des quais globaux.");
        mp.setMessage("calcul des generation des quais globaux.");
        mp.setProgression(75);
        SiporAlgorithmeOccupGlobale.calcul(this.donnees_);
        fenetreProgression.miseAjourBarreProgression(80, "calcul des quais d�taill�s.");
        mp.setMessage("calcul des quais d�taill�s");
        mp.setProgression(80);
        SiporAlgorithmeOccupationsQuais.calcul(donnees_);
        fenetreProgression.miseAjourBarreProgression(85, "calcul des attentes g�n�rales par elements et cat�gories.");
        mp.setMessage("calcul des attentes g�n�rales par elements et cat�gories");
        mp.setProgression(85);
        SiporAlgorithmeAttentesGenerales.CalculApresSImu(donnees_);
        fenetreProgression.miseAjourBarreProgression(90, "calcul des dur�es de parcours.");
        mp.setMessage("calcul des dur�es de parcours");
        mp.setProgression(90);
        SiporAlgorithmeTOUTESDureesParcours.calcul(this.donnees_);
        fenetreProgression.miseAjourBarreProgression(95, "calcul des attentes par trajet.");
        mp.setMessage("calcul des attentes par trajet");
        mp.setProgression(95);
        SiporAlgorithmeTOUTESAttentesTrajet.calcul(donnees_);
        donnees_.enregistrer();
        fenetreProgression.miseAjourBarreProgression(100, "simulation et exploitation termin�es.", "");
        mp.setMessage("simulation et exploitation termin�es.");
        mp.setProgression(100);
        this.donnees_.params_.niveau = 2;
        activerCommandesResultatsSimulation();
    }

    protected void changerProjet(final String nomProjet) {
        projet_ = (FudaaProjet) projets_.get(nomProjet);
    }

    protected void creer() {
        this.donnees_.creer();
    }

    protected void ouvrir(String fichier) {
        donnees_.ouvrir(fichier);
    }

    protected void enregistrer() {
        if (donnees_.projet_ != null) {
            donnees_.enregistrer();
        }
    }

    protected void enregistrerRappelDonneesSous() {
        final String[] ext = { "html", "htm" };
        final String nomFichier = MethodesUtiles.choisirFichierEnregistrement(ext, (Component) getApp());
        if (nomFichier != null) {
            MethodesUtiles.enregistrer(nomFichier, fRappelDonnees_.getHtmlSource(), getApp());
            fRappelDonnees_.putClientProperty("NomFichier", nomFichier);
        }
    }

    protected void enregistrerSous() {
        this.donnees_.enregistrerSous();
    }

    /**
   * Methode de fermeture du projet 2 methodes � g�rer: cas 1: aucun projet charg� cas 2: un projet charg�
   */
    protected void fermer() {
        if (this.donnees_.projet_ == null) {
            return;
        }
        final BuDialogConfirmation c = new BuDialogConfirmation(getApp(), getInformationsSoftware(), "Voulez-vous enregistrer la simulation\n" + this.donnees_.projet_.getFichier());
        if (c.activate() == JOptionPane.YES_OPTION) {
            enregistrer();
        }
        setEnabledForAction("PARAMETRE", false);
        setEnabledForAction("TEXTE", false);
        setEnabledForAction("FERMER", false);
        setEnabledForAction("ENREGISTRER", false);
        setEnabledForAction("ENREGISTRERSOUS", false);
        setEnabledForAction("CALCULER", false);
        setEnabledForAction("PARAMETRE", false);
        activeActionsExploitation();
        setTitle("Sipor");
    }

    protected void quitter() {
        final Object[] liste = projets_.keySet().toArray();
        if (liste.length != 0) {
            final BuDialogConfirmation c = new BuDialogConfirmation(getApp(), getInformationsSoftware(), "Voulez-vous enregistrer le projet actuel\n");
            if (c.activate() == JOptionPane.YES_OPTION) {
                liste_.enregistre();
            }
            for (int i = 0; i < liste.length; i++) {
                changerProjet((String) liste[i]);
                fermer();
            }
        }
        exit();
    }

    protected void importerProjet() {
    }

    protected void importer(final String _vagId) {
    }

    /** Cr�ation ou affichage de la fentre pour les pr�f�rences. */
    protected void buildPreferences(final List _prefs) {
        _prefs.add(new BuBrowserPreferencesPanel(this));
        _prefs.add(new BuDesktopPreferencesPanel(this));
        _prefs.add(new BuLanguagePreferencesPanel(this));
        _prefs.add(new SiporPreferencesPanel());
        _prefs.add(new FudaaStartupExitPreferencesPanel(true));
        _prefs.add(new BuLookPreferencesPanel(this));
    }

    /**
   * Methode de creation d'une fenetre interne de saisie des donnees generales dans l application principale
   */
    void parametreDonneesGenerales() {
        if (gestionDonneesGenerales_ == null) {
            FuLog.debug("interface nulle");
            gestionDonneesGenerales_ = new SiporFrameSaisieDonneesGenerales(this.donnees_);
            gestionDonneesGenerales_.setVisible(true);
            addInternalFrame(gestionDonneesGenerales_);
        } else {
            FuLog.debug("interface r�duite");
            if (gestionDonneesGenerales_.isClosed()) {
                addInternalFrame(gestionDonneesGenerales_);
            } else {
                FuLog.debug("interface cas de figur restant autre que null et fermeture");
                activateInternalFrame(gestionDonneesGenerales_);
                addInternalFrame(gestionDonneesGenerales_);
            }
        }
    }

    /**
   * Methode de creation d'une fenetre interne de saisie des quais dans l application principale
   */
    void parametreQuais() {
        if (gestionQuais_ == null) {
            FuLog.debug("interface nulle");
            gestionQuais_ = new SiporVisualiserQuais(this.donnees_);
            gestionQuais_.setVisible(true);
            addInternalFrame(gestionQuais_);
        } else {
            FuLog.debug("interface ferm�e");
            if (gestionQuais_.isClosed()) {
                gestionQuais_.dispose();
                gestionQuais_ = new SiporVisualiserQuais(this.donnees_);
                addInternalFrame(gestionQuais_);
            } else {
                FuLog.debug("interface cas de figur restant autre que null et fermeture");
                activateInternalFrame(gestionQuais_);
                gestionQuais_.dispose();
                gestionQuais_ = new SiporVisualiserQuais(this.donnees_);
                addInternalFrame(gestionQuais_);
            }
        }
    }

    /**
   * Methode de cration d une fenetre interne de saisie d'Ecluses dans l application principale
   */
    void parametreEcluse() {
        if (gestionEcluses_ == null) {
            FuLog.debug("interface nulle");
            gestionEcluses_ = new SiporVisualiserEcluses(this.donnees_);
            gestionEcluses_.setVisible(true);
            addInternalFrame(gestionEcluses_);
        } else {
            FuLog.debug("interface ferm�e");
            if (gestionEcluses_.isClosed()) {
                gestionEcluses_.dispose();
                gestionEcluses_ = new SiporVisualiserEcluses(this.donnees_);
                addInternalFrame(gestionEcluses_);
            } else {
                FuLog.debug("interface cas de figur restant autre que null et fermeture");
                activateInternalFrame(gestionEcluses_);
                gestionEcluses_.dispose();
                gestionEcluses_ = new SiporVisualiserEcluses(this.donnees_);
                addInternalFrame(gestionEcluses_);
            }
        }
    }

    /**
   * Methode de cration d une fenetre interne de saisie de bassins dans l application principale
   */
    void parametreBassin() {
        if (gestionBassins_ == null) {
            FuLog.debug("interface nulle");
            gestionBassins_ = new SiporVisualiserBassins(this.donnees_);
            gestionBassins_.setVisible(true);
            addInternalFrame(gestionBassins_);
        } else {
            FuLog.debug("interface ferm�e");
            if (gestionBassins_.isClosed()) {
                addInternalFrame(gestionBassins_);
            } else {
                FuLog.debug("interface cas de figur restant autre que null et fermeture");
                activateInternalFrame(gestionBassins_);
                addInternalFrame(gestionBassins_);
            }
        }
    }

    /**
   * Methode de cration d une fenetre interne de saisie de gares dans l application principale
   */
    void parametreGare() {
        if (gestionGares_ == null) {
            FuLog.debug("interface nulle");
            gestionGares_ = new SiporVisualiserGares(this.donnees_);
            gestionGares_.setVisible(true);
            gestionGares_.affichagePanel_.maj(donnees_);
            addInternalFrame(gestionGares_);
        } else {
            FuLog.debug("interface ferm�e");
            if (gestionGares_.isClosed()) {
                gestionGares_.affichagePanel_.maj(donnees_);
                addInternalFrame(gestionGares_);
            } else {
                FuLog.debug("interface cas de figur restant autre que null et fermeture");
                gestionGares_.affichagePanel_.maj(donnees_);
                activateInternalFrame(gestionGares_);
                addInternalFrame(gestionGares_);
            }
        }
    }

    /**
   * Methode de cration d une fenetre interne de saisie de cheneaux dans l application principale
   */
    void parametreChenal() {
        if (gestionChenaux_ == null) {
            FuLog.debug("interface nulle");
            gestionChenaux_ = new SiporVisualiserChenal(this.donnees_);
            gestionChenaux_.setVisible(true);
            addInternalFrame(gestionChenaux_);
        } else {
            FuLog.debug("interface ferm�e");
            if (gestionChenaux_.isClosed()) {
                addInternalFrame(gestionChenaux_);
            } else {
                FuLog.debug("interface cas de figur restant autre que null et fermeture");
                activateInternalFrame(gestionChenaux_);
                addInternalFrame(gestionChenaux_);
            }
        }
    }

    /**
   * Methode de cration d une fenetre interne de saisie de cheneaux dans l application principale
   */
    void parametreMaree() {
        if (gestionMarees_ == null) {
            FuLog.debug("interface nulle");
            gestionMarees_ = new SiporFrameSaisieMaree(this.donnees_);
            gestionMarees_.setVisible(true);
            addInternalFrame(gestionMarees_);
        } else {
            FuLog.debug("interface ferm�e");
            if (gestionMarees_.isClosed()) {
                addInternalFrame(gestionMarees_);
            } else {
                FuLog.debug("interface cas de figur restant autre que null et fermeture");
                activateInternalFrame(gestionMarees_);
                addInternalFrame(gestionMarees_);
            }
        }
    }

    /**
   * Methode de creation d une fenetre interne de saisie des cat�gories de navire dans l application principale
   */
    void parametreNavire() {
        if (gestionNavires_ == null) {
            FuLog.debug("interface nulle");
            gestionNavires_ = new SiporVisualiserNavires(this.donnees_);
            gestionNavires_.setVisible(true);
            addInternalFrame(gestionNavires_);
        } else {
            FuLog.debug("interface ferm�e");
            if (gestionNavires_.isClosed()) {
                gestionNavires_.dispose();
                gestionNavires_ = new SiporVisualiserNavires(this.donnees_);
                addInternalFrame(gestionNavires_);
            } else {
                FuLog.debug("interface cas de figur restant autre que null et fermeture");
                activateInternalFrame(gestionNavires_);
                gestionNavires_.dispose();
                gestionNavires_ = new SiporVisualiserNavires(this.donnees_);
                addInternalFrame(gestionNavires_);
            }
        }
    }

    /**
   * Methode de cr�ation d une fenetre interne de saisie de cercles dans l application principale
   */
    void parametreCercle() {
        if (gestionCercles_ == null) {
            FuLog.debug("interface nulle");
            gestionCercles_ = new SiporVisualiserCercles(this.donnees_);
            gestionCercles_.setVisible(true);
            addInternalFrame(gestionCercles_);
        } else {
            FuLog.debug("interface ferm�e");
            if (gestionCercles_.isClosed()) {
                addInternalFrame(gestionCercles_);
            } else {
                FuLog.debug("interface cas de figur restant autre que null et fermeture");
                activateInternalFrame(gestionCercles_);
                addInternalFrame(gestionCercles_);
            }
        }
    }

    void activationRappelDonnees() {
        if (rappelDonnees_ == null) {
            FuLog.debug("interface nulle");
            rappelDonnees_ = new SiporFrameGenerationRappelDonnees(this.donnees_);
            rappelDonnees_.setVisible(true);
            addInternalFrame(rappelDonnees_);
        } else {
            FuLog.debug("interface ferm�e");
            if (rappelDonnees_.isClosed()) {
                addInternalFrame(rappelDonnees_);
            } else {
                FuLog.debug("interface cas de figur restant autre que null et fermeture");
                activateInternalFrame(rappelDonnees_);
                addInternalFrame(rappelDonnees_);
            }
        }
    }

    /**
   * methode qui active le lancement de l'interface de visualisation des parametres r�sultats de la g�n�ration de
   * navires
   */
    public void activerResultatsgenerationNavires() {
        if (this.donnees_.params_.ResultatsCompletsSimulation.ResultatsGenerationNavires == null) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.getInformationsSoftware(), "Erreur!!\n Relancez la simulation dabord," + " \n les r�sultats de la simulation ne sont pas charg�s" + "\n (pour relancer la simulation, aller dans" + "\n l'onglet Simulation et cliquer sur \"Calculer\")").activate();
            return;
        }
        addInternalFrame(new SiporResultatGenerationBateaux(donnees_));
    }

    public void activerResultatHistorique() {
        if (this.donnees_.listeResultatsSimu_ == null) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.getInformationsSoftware(), "Erreur!!\n Relancez la simulation dabord," + " \n les r�sultats de la simulation ne sont pas charg�s" + "\n (pour relancer la simulation, aller dans" + "\n l'onglet Simulation et cliquer sur \"Calculer\")").activate();
            return;
        }
        addInternalFrame(new SiporResultatHistorique(donnees_));
    }

    public void activerResultatDureesParcours() {
        if (this.donnees_.params_.ResultatsCompletsSimulation.TOUTEDureesParoucrs == null) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.getInformationsSoftware(), "Erreur!!\n Relancez la simulation dabord," + " \n les r�sultats de la simulation ne sont pas charg�s" + "\n (pour relancer la simulation, aller dans" + "\n l'onglet Simulation et cliquer sur \"Calculer\")").activate();
            return;
        }
        addInternalFrame(new SiporResultatsDureesParcours(donnees_));
    }

    public void activerResultatCroisementsChenaux() {
        if (this.donnees_.listeResultatsSimu_ == null) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.getInformationsSoftware(), "Erreur!!\n Relancez la simulation dabord," + " \n les r�sultats de la simulation ne sont pas charg�s" + "\n (pour relancer la simulation, aller dans" + "\n l'onglet Simulation et cliquer sur \"Calculer\")").activate();
            return;
        }
        addInternalFrame(new SiporResultatsCroisementsChenal(donnees_));
    }

    public void activerResultatCroisementsCercles() {
        if (this.donnees_.listeResultatsSimu_ == null) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.getInformationsSoftware(), "Erreur!!\n Relancez la simulation dabord," + " \n les r�sultats de la simulation ne sont pas charg�s" + "\n (pour relancer la simulation, aller dans" + "\n l'onglet Simulation et cliquer sur \"Calculer\")").activate();
            return;
        }
        addInternalFrame(new SiporResultatsCroisementsCercle(donnees_));
    }

    public void activerResultatAttentesGeneralesElement() {
        if (this.donnees_.params_.ResultatsCompletsSimulation.AttentesTousElementsToutesCategories == null) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.getInformationsSoftware(), "Erreur!!\n Relancez la simulation dabord," + " \n les r�sultats de la simulation ne sont pas charg�s" + "\n (pour relancer la simulation, aller dans" + "\n l'onglet Simulation et cliquer sur \"Calculer\")").activate();
            return;
        }
        addInternalFrame(new SiporResultatsAttenteGeneraleElement(donnees_));
    }

    public void activerResultatAttentesGeneralesCategorie() {
        if (this.donnees_.params_.ResultatsCompletsSimulation.AttentesTousElementsToutesCategories == null) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.getInformationsSoftware(), "Erreur!!\n Relancez la simulation dabord," + " \n les r�sultats de la simulation ne sont pas charg�s" + "\n (pour relancer la simulation, aller dans" + "\n l'onglet Simulation et cliquer sur \"Calculer\")").activate();
            return;
        }
        addInternalFrame(new SiporResultatsAttenteGeneraleCategories(donnees_));
    }

    public void activerResultatAttentesSpecialisee() {
        if (this.donnees_.params_.ResultatsCompletsSimulation.TOUTESAttenteTrajet == null) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.getInformationsSoftware(), "Erreur!!\n Relancez la simulation dabord," + " \n les r�sultats de la simulation ne sont pas charg�s" + "\n (pour relancer la simulation, aller dans" + "\n l'onglet Simulation et cliquer sur \"Calculer\")").activate();
            return;
        }
        addInternalFrame(new SiporResultatsAttenteTrajet(donnees_));
    }

    public void activerResultatOccupGlobales() {
        if (this.donnees_.params_.ResultatsCompletsSimulation.tableauOccupationGlobal == null) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.getInformationsSoftware(), "\n Veuillez relancer la simulation dabord," + " \n les r�sultats de la simulation ne sont pas charg�s" + "\n (pour relancer la simulation, aller dans" + "\n l'onglet Simulation et cliquer sur \"Calculer\")").activate();
            return;
        }
        addInternalFrame(new SiporResultatOccupationGlobales(donnees_));
    }

    public void activerResultatOccupQuai() {
        if (this.donnees_.params_.ResultatsCompletsSimulation.tableauOccupationTousQuais == null) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.getInformationsSoftware(), "Veuillez relancer la simulation dabord," + " \n les r�sultats de la simulation ne sont pas charg�s" + "\n (pour relancer la simulation, aller dans" + "\n l'onglet Simulation et cliquer sur \"Calculer\")").activate();
            return;
        }
        addInternalFrame(new SiporResultatsOccupationQuai(donnees_));
    }

    public void activerComparaisonNombreNavires() {
        addInternalFrame(new SiporResultatComparaisonGenerationBateaux(donnees_));
    }

    public void activerComparaisonOccupationGlobale() {
        addInternalFrame(new SiporResultatComparaisonOccupationGlobale(donnees_));
    }

    public void activerComparaisonDureesParcours() {
        addInternalFrame(new SiporResultatComparaisonDureeParcours(donnees_));
    }

    public void activerComparaisonOccupationQuais() {
        addInternalFrame(new SiporResultatComparaisonOccupQuais(donnees_));
    }

    public void activerComparaisonAttenteGenerale() {
        addInternalFrame(new SiporResultatComparaisonAttenteElement(donnees_));
    }

    public void activerComparaisonAttenteTrajet() {
        addInternalFrame(new SiporResultatComparaisonAttentetrajet(donnees_));
    }

    /**
   * methode de fermeture de toutes les interfaces
   */
    void fermerToutesLesInterfaces() {
        if (gestionQuais_ != null) {
            gestionQuais_.dispose();
        }
        if (gestionNavires_ != null) {
            gestionNavires_.dispose();
        }
        if (gestionChenaux_ != null) {
            gestionChenaux_.dispose();
        }
        if (gestionCercles_ != null) {
            gestionCercles_.dispose();
        }
        if (gestionGares_ != null) {
            gestionGares_.dispose();
        }
        if (gestionBassins_ != null) {
            gestionBassins_.dispose();
        }
        if (gestionEcluses_ != null) {
            gestionEcluses_.dispose();
        }
        if (gestionMarees_ != null) {
            gestionMarees_.dispose();
        }
        if (gestionDonneesGenerales_ != null) {
            gestionDonneesGenerales_.dispose();
        }
        if (gestionTopoBassins_ != null) {
            gestionTopoBassins_.dispose();
        }
        if (gestionTopoEcluses_ != null) {
            gestionTopoEcluses_.dispose();
        }
        if (gestionTopoChenaux_ != null) {
            gestionTopoChenaux_.dispose();
        }
        if (gestionTopoCercles_ != null) {
            gestionTopoCercles_.dispose();
        }
        if (gestionModelisation_ != null) {
            gestionModelisation_.dispose();
        }
        if (gestionNavigChenal_ != null) {
            gestionNavigChenal_.dispose();
        }
        if (gestionNavigCercle_ != null) {
            gestionNavigCercle_.dispose();
        }
        if (gestionDureeParcoursChenal_ != null) {
            gestionDureeParcoursChenal_.dispose();
        }
        if (gestionDureeParcoursCercle_ != null) {
            gestionDureeParcoursCercle_.dispose();
        }
    }

    void initialiserToutesLesInterfaces() {
        fermerToutesLesInterfaces();
        gestionQuais_ = null;
        gestionNavires_ = null;
        gestionChenaux_ = null;
        gestionCercles_ = null;
        gestionGares_ = null;
        gestionBassins_ = null;
        gestionEcluses_ = null;
        gestionMarees_ = null;
        gestionDonneesGenerales_ = null;
        gestionTopoBassins_ = null;
        gestionTopoEcluses_ = null;
        gestionTopoChenaux_ = null;
        gestionTopoCercles_ = null;
        gestionModelisation_ = null;
        gestionNavigChenal_ = null;
        gestionNavigCercle_ = null;
        gestionDureeParcoursChenal_ = null;
        gestionDureeParcoursCercle_ = null;
    }

    protected BuInternalFrame creerFenetreInterne() {
        final BuInternalFrame frame = new BuInternalFrame("Graphe", true, true, true, true);
        frame.setBackground(Color.white);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setBounds(200, 200, 200, 200);
        frame.setSize(400, 400);
        frame.setVisible(true);
        addInternalFrame(frame);
        return frame;
    }

    protected void activeActionsExploitation() {
        SiporOutilsDonnees simulation = null;
        simulation = new SiporOutilsDonnees(projet_);
        if (simulation.getResultats() != null) {
            setEnabledForAction("TABLEAU", true);
        } else {
            setEnabledForAction("TABLEAU", false);
        }
        final Object[] simulations = projets_.keySet().toArray();
        for (int i = 0; i < simulations.length; i++) {
            simulation = new SiporOutilsDonnees((FudaaProjet) projets_.get(simulations[i]));
            if (simulation.getResultats() != null) {
                setEnabledForAction("GRAPHE", true);
                return;
            }
        }
        setEnabledForAction("GRAPHE", false);
    }

    public void exit() {
        fermer();
        super.exit();
    }

    public boolean isCloseFrameMode() {
        return false;
    }

    /**
   * @see org.fudaa.fudaa.commun.dodico.FudaaImplementation#clearVariables()
   */
    protected void clearVariables() {
        CONNEXION_SIPOR = null;
        SERVEUR_SIPOR = null;
    }

    /**
   * @see org.fudaa.fudaa.commun.dodico.FudaaImplementation#getTacheConnexionMap()
   */
    protected FudaaDodicoTacheConnexion[] getTacheConnexionMap() {
        final FudaaDodicoTacheConnexion c = new FudaaDodicoTacheConnexion(SERVEUR_SIPOR, CONNEXION_SIPOR);
        return new FudaaDodicoTacheConnexion[] { c };
    }

    /**
   * @see org.fudaa.fudaa.commun.dodico.FudaaImplementation#getTacheDelegateClass()
   */
    protected Class[] getTacheDelegateClass() {
        return new Class[] { DCalculSipor.class };
    }

    /**
   * @see org.fudaa.fudaa.commun.dodico.FudaaImplementation#initConnexions(java.util.Map)
   */
    protected void initConnexions(final Map _r) {
        final FudaaDodicoTacheConnexion c = (FudaaDodicoTacheConnexion) _r.get(DCalculSipor.class);
        CONNEXION_SIPOR = c.getConnexion();
        SERVEUR_SIPOR = ICalculSiporHelper.narrow(c.getTache());
    }

    /**
   * @see org.fudaa.fudaa.commun.impl.FudaaCommonImplementation#getApplicationPreferences()
   */
    public BuPreferences getApplicationPreferences() {
        return SiporPreferences.SIPOR;
    }

    /**
   * Methode de suppression d'une simulation parmi les simulations ouvertes.
   *
   */
    public void supprimerSimulation() {
        if (liste_.getModel().getSize() <= 1) {
            exit();
            return;
        }
        String nomProjet = (String) liste_.getSelectedValue();
        FudaaProjet deleteProject = (FudaaProjet) projets_.get(liste_.getSelectedValue());
        int indiceProjetSupprime = liste_.getSelectedIndex();
        if (deleteProject == donnees_.projet_) {
            if (indiceProjetSupprime != 0) liste_.setSelectedIndex(0); else liste_.setSelectedIndex(1);
            donnees_.changerProjet((FudaaProjet) projets_.get(liste_.getSelectedValue()));
        }
        System.out.println("\nl'indice a supprimer est " + indiceProjetSupprime);
        System.out.println("la taille du modele est " + liste_.getModel().getSize());
        System.out.println("\nl'indice a selectionne est " + liste_.getSelectedIndex());
        ((DefaultListModel) liste_.getModel()).remove(indiceProjetSupprime);
        projets_.remove(nomProjet);
        liste_.revalidate();
    }

    /**
   * Methode de suppression du fichier historique.
   * Methode importante car le fichier est souvent tr�s volumineux et les r�sultats sont sauvegard� donc historique inutile.
   *
   */
    public void supprimerHistoriqueSimulation() {
        String path = donnees_.projet_.getFichier() + ".his";
        File historique = new File(path);
        BuDialogConfirmation mess = new BuDialogConfirmation(this, isSipor_, "Voulez-vous vraiment supprimer le fichier historique?");
        final int confirmation = mess.activate();
        if (confirmation == 0) {
            boolean reussite = historique.delete();
            if (!reussite) new BuDialogError(this.getApp(), isSipor_, "Le fichier historique n'a pas pu �tre supprim�.").activate(); else new BuDialogMessage(this.getApp(), isSipor_, "Le fichier historique a �t� correctement supprim�.").activate();
        }
    }

    /**
   * Methode qui renseigne sur le chenmin vers les serveurs sipor.
   * @return
   */
    protected final String cheminServeur() {
        String path = System.getProperty("FUDAA_SERVEUR");
        if ((path == null) || path.equals("")) {
            path = System.getProperty("user.dir") + File.separator + "serveurs" + File.separator + "Fudaa-Sipor";
        }
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        return path;
    }

    /**
  * Methode qui permet d'executer une comande via la classe CExec. 
  * @param progFortran programme executable a lancer 
  * 0= SiporGenarr
  * 1= noyau de calcul 
  * @param nomEtude nom de l'etude sipor
  */
    protected boolean ExecuterCommandeDepuisServeur(int progFortran, String nomEtude) {
        final String os = System.getProperty("os.name");
        final String path = cheminServeur();
        System.out.println("**\nLe chemin des serveurs est:\n " + path + " \n**");
        try {
            String[] cmd;
            if (os.startsWith("Windows")) {
                cmd = new String[2];
                if (progFortran == 0) cmd[0] = path + "bin" + "\\win\\siporGenarr_win.exe"; else cmd[0] = path + "bin" + "\\win\\sipor_win.exe";
                cmd[1] = nomEtude;
            } else {
                cmd = new String[2];
                if (progFortran == 0) cmd[0] = path + "bin" + "/linux/siporGenarr_linux.x"; else cmd[0] = path + "bin" + "/linux/sipor_linux.x";
                cmd[1] = nomEtude;
            }
            System.out.println("**\nLa commande ex�cut�e est: \n " + cmd[0] + " " + cmd[1] + "\n**");
            try {
                final CExec ex = new CExec();
                ex.setCommand(cmd);
                ex.setOutStream(System.out);
                ex.setErrStream(System.err);
                ex.exec();
            } catch (final Throwable _e1) {
                System.out.println("Erreur rencontr�e lors de l'execution du code de calcul");
                _e1.printStackTrace();
                new BuDialogError(this.getApp(), isSipor_, "Erreur rencontr�e lors de l'execution du code de calcul").activate();
                return false;
            }
        } catch (final Exception ex) {
            System.out.println("Erreur lors de l'execution du code de calcul");
            if (progFortran == 0) new BuDialogMessage(this.getApp(), isSipor_, "Impossible d'executer le g�n�rateur de navire genarr").activate(); else new BuDialogMessage(this.getApp(), isSipor_, "Impossible d'executer le noyau de calcul Sipor").activate();
            return false;
        }
        return true;
    }

    /**
   * Methode de lancement de l'executable GENARR.
   * Permet de g�n�rer un fichier contenant la liste des navires.
   *
   */
    public void lancementGenarr() {
        if (donnees_.categoriesNavires_.listeNavires_.size() == 0) {
            new BuDialogError(this.getApp(), isSipor_, "Il doit exister au moins une cat�gorie de navire.\n" + "vant de r�aliser la g�n�ration de navires.").activate();
            return;
        }
        try {
            DParametresSipor.ecritDonneesCategoriesNavires(this.donnees_.params_.navires, this.donnees_.projet_.getFichier());
            DParametresSipor.ecritDonneesGenerales(this.donnees_.params_.donneesGenerales, this.donnees_.projet_.getFichier());
            if (!ExecuterCommandeDepuisServeur(0, donnees_.projet_.getFichier())) return;
            boolean result = donnees_.genarr_.lectureFichierGenarr(donnees_.projet_.getFichier());
            if (!result) return;
            new BuDialogMessage(this.getApp(), isSipor_, "La g�n�ration de navires a �t� r�alis� avec succ�s.").activate();
        } catch (IOException e) {
            new BuDialogError(this.getApp(), isSipor_, "Erreur dans la cr�ation du fichier des cat�gories de navire.\n" + "v�rifier la saisie des cat�gories de navires.").activate();
        }
    }

    /**
   * Methode d'aafichage des navires g�n�r�s par GENARR.
   * L'affichage s'effectue sous forme de tableau r�capitulatif triable.
   *
   */
    public void affichageGenarr() {
        File fichier = new File(donnees_.projet_.getFichier() + ".arriv");
        if (!fichier.exists()) {
            new BuDialogError(null, SiporImplementation.isSipor_, "Le fichier de g�n�ration est introuvable.\n Veuillez relancer la g�n�ration de navire (onglet G�n�ration)").activate();
            return;
        }
        boolean result = donnees_.genarr_.lectureFichierGenarr(donnees_.projet_.getFichier());
        if (!result) return;
        addInternalFrame(new GenarrFrameAffichage(donnees_));
    }
}
