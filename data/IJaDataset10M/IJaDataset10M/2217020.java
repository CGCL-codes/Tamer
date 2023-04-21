package org.fudaa.fudaa.hiswa;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.vecmath.Vector3f;
import com.memoire.bu.*;
import com.memoire.fu.FuLib;
import org.fudaa.dodico.corba.hiswa.ICalculHiswa;
import org.fudaa.dodico.corba.hiswa.ICalculHiswaHelper;
import org.fudaa.dodico.corba.hiswa.IParametresHiswa;
import org.fudaa.dodico.corba.hiswa.IResultatsHiswa;
import org.fudaa.dodico.corba.hiswa.SParametresHIS;
import org.fudaa.dodico.corba.hiswa.SResultatsHiswaTable;
import org.fudaa.dodico.corba.objet.IConnexion;
import org.fudaa.dodico.corba.objet.ITransfertFichierASCII;
import org.fudaa.dodico.corba.objet.ITransfertFichierASCIIHelper;
import org.fudaa.dodico.hiswa.DCalculHiswa;
import org.fudaa.dodico.hiswa.DParametresHiswa;
import org.fudaa.dodico.hiswa.parser.TokenMgrError;
import org.fudaa.ebli.calque.*;
import org.fudaa.ebli.geometrie.GrPolygone;
import org.fudaa.ebli.palette.BPaletteCouleurSimple;
import org.fudaa.ebli.ressource.EbliResource;
import org.fudaa.ebli.trace.BParametresGouraud;
import org.fudaa.ebli.volume.BGrilleReguliere;
import org.fudaa.ebli.volume.BGroupeLumiere;
import org.fudaa.ebli.volume.BGroupeStandard;
import org.fudaa.ebli.volume.BGroupeVolume;
import org.fudaa.ebli.volume.BLumiereDirectionnelle;
import org.fudaa.ebli.volume.EbliFilleVue3D;
import org.fudaa.ebli.volume.VolumePreferencesPanel;
import org.fudaa.fudaa.commun.dodico.FudaaDodicoTacheConnexion;
import org.fudaa.fudaa.commun.dodico.FudaaImplementation;
import org.fudaa.fudaa.commun.projet.FudaaFiltreFichier;
import org.fudaa.fudaa.commun.projet.FudaaProjet;
import org.fudaa.fudaa.ressource.FudaaResource;

/**
 * L'implementation du client Hiswa.
 *
 * @version      $Revision: 1.20 $ $Date: 2007-05-04 13:59:05 $ by $Author: deniger $
 * @author       Axel Guerrand , Claudio Toni Branco
 */
public class HiswaImplementation extends FudaaImplementation {

    public static final boolean IS_APPLICATION = true;

    public static ICalculHiswa SERVEUR_HISWA;

    public static IConnexion CONNEXION_HISWA;

    private IParametresHiswa iparams_;

    private IResultatsHiswa iresults_;

    private ITransfertFichierASCII iflux_;

    static BuInformationsSoftware isHiswa_;

    static BuInformationsDocument idHiswa_;

    FudaaProjet projet_;

    EbliFilleVue3D fVue3D_;

    HiswaFilleCalques fCalques_;

    private HiswaFilleTableau fTableau_;

    HiswaOutilsResultats resYapod_;

    private HiswaFilleEditeur fEditeur_;

    BArbreCalque arbre_;

    BGroupeCalque gc_;

    private BCalqueLegende leg_;

    BVueCalque vc_;

    GrPolygone rectangle_;

    private BParametresGouraud paramsGouraud_;

    private HiswaAssistant assistant_;

    private BuHelpFrame aide_;

    private BuTaskView taches_;

    private HiswaPreferences options_;

    private BGrilleReguliere houle_;

    private BGroupeVolume imp_;

    BGroupeVolume gv_;

    private String pathFichierImport_;

    static final String[] parametresResultats_ = { "Hauteur de Houle", "Direction", "Periode", "Profondeur d'eau", "Vitesse", "Force", "Transport d'Energie", "Etalement Directionnel", "Dissipation", "Perte D'Energie (theta)", "Fraction of breaking Waves", "Distance (% courbe)", "Vitesse Orbitale Maximale", "Cambrure", "Longueur de la vague" };

    static final String[] correspondance_ = { "hsign", "dir", "period", "depth", "vel", "force", "transp", "dspr", "dissip", "leak", "qb", "dist", "ubot", "steepn", "wlength" };

    boolean j3d_;

    static {
        isHiswa_ = new BuInformationsSoftware();
        isHiswa_.name = "Hiswa";
        isHiswa_.version = "0.25";
        isHiswa_.date = "10-fev-2000";
        isHiswa_.rights = "Tous droits r�serv�s. CETMEF (c)1999";
        isHiswa_.contact = "claudio.branco@etu.utc.fr";
        isHiswa_.license = "CETMEF classe A";
        isHiswa_.languages = "fr,en";
        isHiswa_.logo = HiswaResource.HISWA.getIcon("hiswa_logo");
        isHiswa_.banner = HiswaResource.HISWA.getIcon("hiswa_banner");
        isHiswa_.http = "http://172.17.250.86/~desnoix/fudaa/";
        isHiswa_.update = "http://172.17.250.86/~desnoix/deltas/";
        isHiswa_.authors = new String[] { "Claudio Toni Branco", "Axel Guerrand" };
        isHiswa_.contributors = new String[] { "Equipes Dodico, Ebli et Fudaa" };
        isHiswa_.testers = new String[] { "G�rard Goasguen - CETMEF Brest" };
        idHiswa_ = new BuInformationsDocument();
        idHiswa_.name = "Etude";
        idHiswa_.version = "0.01";
        idHiswa_.organization = "CETMEF";
        idHiswa_.author = System.getProperty("user.name");
        idHiswa_.contact = idHiswa_.author + "@cetmef.equipement.gouv.fr";
        idHiswa_.date = FuLib.date();
        idHiswa_.logo = EbliResource.EBLI.getIcon("minlogo.gif");
        BuPrinter.INFO_LOG = isHiswa_;
        BuPrinter.INFO_DOC = idHiswa_;
    }

    public BuInformationsSoftware getInformationsSoftware() {
        return isHiswa_;
    }

    public static BuInformationsSoftware informationsSoftware() {
        return isHiswa_;
    }

    public BuInformationsDocument getInformationsDocument() {
        return idHiswa_;
    }

    public void init() {
        super.init();
        j3d_ = true;
        try {
            Class.forName("javax.media.j3d.BranchGroup");
        } catch (final ClassNotFoundException ex) {
            j3d_ = false;
            System.out.println("Pas de 3D!!!");
        }
        projet_ = null;
        fCalques_ = null;
        fVue3D_ = null;
        fTableau_ = null;
        resYapod_ = null;
        fEditeur_ = null;
        gc_ = null;
        leg_ = null;
        rectangle_ = null;
        paramsGouraud_ = null;
        aide_ = null;
        options_ = null;
        try {
            setTitle("Hiswa " + isHiswa_.version);
            final BuMenuBar mb = getMainMenuBar();
            ((BuMenu) mb.getMenu("IMPORTER")).addMenuItem("HISWA SOURCE", "IMPORTERHISWA", true);
            ((BuMenu) mb.getMenu("EXPORTER")).addMenuItem("HISWA " + isHiswa_.version, "EXPORTERHISWA", true);
            mb.addMenu(buildCalculMenu());
            mb.computeMnemonics();
            final BuToolBar tb = getMainToolBar();
            tb.addSeparator();
            tb.addToolButton("Connecter", "CONNECTER", FudaaResource.FUDAA.getIcon("connecter"), true);
            tb.addToolButton("Calculer", "CALCULER", false);
            setEnabledForAction("DEFAIRE", false);
            setEnabledForAction("REFAIRE", false);
            setEnabledForAction("COPIER", false);
            setEnabledForAction("COUPER", false);
            setEnabledForAction("COLLER", false);
            setEnabledForAction("TOUTSELECTIONNER", false);
            setEnabledForAction("RECHERCHER", false);
            setEnabledForAction("REMPLACER", false);
            setEnabledForAction("CREER", true);
            setEnabledForAction("OUVRIR", true);
            setEnabledForAction("ENREGISTRER", false);
            setEnabledForAction("ENREGISTRERSOUS", false);
            setEnabledForAction("FERMER", false);
            setEnabledForAction("QUITTER", true);
            setEnabledForAction("IMPORTER", false);
            setEnabledForAction("EXPORTER", false);
            setEnabledForAction("IMPRIMER", false);
            setEnabledForAction("PARAMETRES", false);
            setEnabledForAction("PREFERENCE", true);
            setEnabledForAction("AIDE_INDEX", true);
            setEnabledForAction("AIDE_ASSISTANT", true);
            final BuMainPanel mp = getApp().getMainPanel();
            mp.setLogo(isHiswa_.logo);
            final BuColumn rc = mp.getRightColumn();
            assistant_ = new HiswaAssistant();
            rc.addToggledComponent("Assistant", "ASSISTANT", assistant_, this);
            taches_ = new BuTaskView();
            final BuScrollPane sp = new BuScrollPane(taches_);
            sp.setPreferredSize(new Dimension(150, 80));
            rc.addToggledComponent("T�ches", "TACHE", sp, this);
            mp.setTaskView(taches_);
        } catch (final Throwable t) {
            System.err.println("$$$ " + t);
            t.printStackTrace();
        }
    }

    public void start() {
        String pathProject;
        super.start();
        BuPreferences.BU.applyOn(this);
        HiswaPreferences.HISWA.applyOn(this);
        pathProject = "exemples" + File.separator + "hiswa";
        projet_ = new FudaaProjet(getApp(), new FudaaFiltreFichier("hsw"));
        assistant_.changeAttitude(BuAssistant.PAROLE, "Bienvenue !");
        final BuMainPanel mp = getMainPanel();
        final BuColumn rc = mp.getRightColumn();
        final BuInformationsDocument id = new BuInformationsDocument();
        id.name = "Etude";
        id.organization = "CETMEF";
        id.author = System.getProperty("user.name");
        id.contact = id.author + "@stcpmvn.equipement.gouv.fr";
        id.date = FuLib.date();
        id.logo = EbliResource.EBLI.getIcon("minlogo.gif");
        gc_ = new BGroupeCalque();
        gc_.setName("Calques");
        gc_.setBackground(Color.white);
        arbre_ = new BArbreCalque();
        final JScrollPane sp = new JScrollPane(arbre_);
        sp.setSize(150, 150);
        rc.addToggledComponent("Calques", "CALQUE", sp, this);
        mp.doLayout();
        mp.validate();
        getMainMenuBar().removeActionListener(this);
        assistant_.addEmitters((Container) getApp());
        assistant_.addEmitters((Container) getApp());
        assistant_.changeAttitude(BuAssistant.PAROLE, "Vous pouvez cr�er un\nnouveau projet Hiswa\nou en ouvrir un");
        getMainMenuBar().addActionListener(this);
    }

    private BuMenu buildCalculMenu() {
        final BuMenu r = new BuMenu("Hiswa", "MENU");
        r.addSeparator("Param�tres");
        r.addMenuItem("Editer le Fichier \".his\"", "EDITERHISWA", BuResource.BU.getIcon("texte"), false, KeyEvent.VK_J);
        r.addMenuItem("Param�tres", "PARAMETRES", BuResource.BU.getIcon("parametre"), false);
        r.addSeparator("Calcul");
        r.addMenuItem("Calculer", "CALCULER", false);
        r.addSeparator("R�sultats");
        r.addMenuItem("Tableau des R�sultats", "RESULTATSBRUTS", BuResource.BU.getIcon("tableau"), false);
        final BuMenu subMenuCalques = new BuMenu("Calques", "CALQUES");
        subMenuCalques.setIcon(BuResource.BU.getIcon("calque"));
        subMenuCalques.addMenuItem("Visualiser", "VISUALISER[false]", false);
        final BuMenu subMenuCalquesSelection = new BuMenu("Ajouter un Calque", "SELECTIONCALQUES", true);
        int indiceCalque = 0;
        for (indiceCalque = 0; indiceCalque < parametresResultats_.length; indiceCalque++) {
            subMenuCalquesSelection.addMenuItem(parametresResultats_[indiceCalque], "AJOUTCALQUE[" + (indiceCalque) + "]", false);
        }
        subMenuCalques.addSubMenu(subMenuCalquesSelection, false);
        r.addSubMenu(subMenuCalques, false);
        r.addMenuItem("Vue 3D de la Houle", "VUE3D", BuResource.BU.getIcon("houle"), false);
        return r;
    }

    public void actionPerformed(final ActionEvent _evt) {
        String action = _evt.getActionCommand();
        String arg = "";
        System.err.println("ACTION=" + action);
        final int i = action.indexOf('[');
        if (i >= 0) {
            arg = action.substring(i + 1, action.length() - 1);
            action = action.substring(0, i);
        }
        if (action.equals("CREER")) {
            creer();
        } else if (action.equals("OUVRIR")) {
            ouvrir();
        } else if (action.equals("ENREGISTRER")) {
            enregistrer();
        } else if (action.equals("ENREGISTRERSOUS")) {
            enregistrerSous();
        } else if (action.equals("FERMER")) {
            fermer();
        } else if ((action.equals("IMPORTERHISWA")) || (action.equals("IMPORTER"))) {
            importerHiswa();
        } else if (action.equals("EXPORTERHISWA")) {
            exporterHiswa();
        } else if (action.equals("PREFERENCE")) {
            preferences();
        } else if (action.equals("EDITERHISWA")) {
            editer();
        } else if (action.equals("PARAMETRES")) {
            parametre();
        } else if (action.equals("CALCULER")) {
            calculer();
        } else if (action.equals("VISUALISER")) {
            affichageResultats(arg);
        } else if (action.equals("AJOUTCALQUE")) {
            ajoutCalque(arg);
        } else if (action.equals("RESULTATSBRUTS")) {
            resultatsBruts();
        } else if (action.equals("VUE3D")) {
            vue3d();
        } else if (action.equals("ASSISTANT") || action.equals("CALQUE") || action.equals("TACHE")) {
            final BuColumn rc = getMainPanel().getRightColumn();
            rc.toggleComponent(action);
            setCheckedForAction(action, rc.isToggleComponentVisible(action));
        } else {
            super.actionPerformed(_evt);
        }
    }

    public void displayURL(String _url) {
        final String netHelp = System.getProperty("net.access.man");
        if (!"remote".equals(netHelp)) {
            _url = "file:" + System.getProperty("user.dir") + "/manuels/vag/utilisation.html";
        }
        if (BuPreferences.BU.getStringProperty("browser.internal").equals("true")) {
            if (aide_ == null) {
                aide_ = new BuHelpFrame();
            }
            addInternalFrame(aide_);
            aide_.setDocumentUrl(_url);
        } else {
            if (_url == null) {
                final BuInformationsSoftware il = getInformationsSoftware();
                _url = il.http;
            }
            BuBrowserControl.displayURL(_url);
        }
    }

    private void updateTitle() {
        String name = projet_.getFichier();
        final int pos = name.lastIndexOf(System.getProperty("file.separator"));
        if (pos != -1) {
            name = name.substring(pos + 1);
        }
        setTitle("Hiswa " + isHiswa_.version + " - " + name);
    }

    private void creer() {
        if (projet_.estConfigure()) {
            fermer();
        }
        projet_.creer();
        if (!projet_.estConfigure()) {
            projet_.fermer();
        } else {
            setEnabledForAction("IMPORTER", true);
            setEnabledForAction("FERMER", true);
            setEnabledForAction("ENREGISTRER", true);
            setEnabledForAction("ENREGISTRERSOUS", true);
        }
        updateTitle();
    }

    private void importerHiswa() {
        String path;
        String filename = null;
        final JFileChooser chooser = new JFileChooser();
        int returnVal;
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(final java.io.File f) {
                return f.isDirectory() || f.getName().endsWith(".his") || f.getName().endsWith(".HIS");
            }

            public String getDescription() {
                return "Etude Hiswa " + isHiswa_.version + " (*.his)";
            }
        });
        path = "exemples" + File.separator + "hiswa";
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir") + File.separator + path));
        returnVal = chooser.showOpenDialog(HiswaApplication.FRAME);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            pathFichierImport_ = chooser.getSelectedFile().getParent() + File.separator;
            if (pathFichierImport_ == null) {
                pathFichierImport_ = File.separator;
            }
            filename = chooser.getSelectedFile().getAbsolutePath();
        }
        if (filename == null) {
            return;
        }
        System.out.println("Fichier selectionne :" + filename);
        try {
            projet_.addImport(HiswaResource.HIS, filename);
        } catch (final TokenMgrError err) {
            final String message = err.getMessage();
            System.err.println("Erreur Lexicale ");
            new BuDialogError(this, isHiswa_, "Erreur Lexicale : \n" + message + "\n").activate();
            return;
        }
        final SParametresHIS params = (SParametresHIS) projet_.getParam(HiswaResource.HIS);
        String mesgError = "";
        boolean errorFrame = false;
        boolean errorTable = false;
        boolean errorPaper = false;
        boolean errorFixed = false;
        if (!(params.parametresFrame.ecriture)) {
            errorFrame = true;
            mesgError += "\nLa commande Frame n'est pas mentionn�e";
        }
        if (!(params.parametresTable.ecriture)) {
            errorTable = true;
            mesgError += "\nLa commande Table n'est pas mentionn�e";
        }
        if ((!errorTable) && !(params.parametresTable.format).equals("PAPER")) {
            errorPaper = true;
            mesgError += "\nLe format du Tableau de resultats n'est pas \"PAPER\"";
        }
        if (!(params.parametresGrid.option.startsWith("F"))) {
            errorFixed = true;
            mesgError += "\nL'option \"FIXED\" de la commande\"GRID\" n'est pas mentionn�e";
        }
        if (errorFrame || errorTable || errorPaper || errorFixed) {
            new BuDialogError(getApp(), isHiswa_, "Le fichier n'est pas valide : " + mesgError).activate();
            return;
        }
        setEnabledForAction("PARAMETRES", true);
        setEnabledForAction("CALCULER", true);
        setEnabledForAction("IMPORTER", false);
        setEnabledForAction("EXPORTER", true);
        setEnabledForAction("HISWA", true);
        setEnabledForAction("FERMER", true);
        setEnabledForAction("EDITERHISWA", true);
        updateTitle();
        new BuDialogMessage(getApp(), isHiswa_, "Param�tres charg�s").activate();
    }

    private void exporterHiswa() {
        String path;
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(final java.io.File f) {
                return f.isDirectory() || f.getName().endsWith(".his");
            }

            public String getDescription() {
                return "Etude Hiswa " + isHiswa_.version + " (*.his)";
            }
        });
        path = "exemples" + File.separator + "hiswa";
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir") + File.separator + path));
        final int returnVal = chooser.showOpenDialog(HiswaApplication.FRAME);
        String filename = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filename = chooser.getSelectedFile().getAbsolutePath();
        }
        if (filename == null) {
            return;
        }
        System.out.println("Fichier selectionne :" + filename);
        if (!filename.endsWith(".his")) {
            try {
                filename = filename.substring(0, filename.lastIndexOf('.'));
                filename += ".his";
            } catch (final StringIndexOutOfBoundsException ex) {
                filename += ".his";
            }
        }
        projet_.export(HiswaResource.HIS, filename);
        updateTitle();
    }

    private void calculer() {
        if (!isConnected()) {
            new BuDialogError(getApp(), isHiswa_, "vous n'etes pas connect� � un serveur!").activate();
            return;
        }
        if ((fEditeur_ != null) && (fEditeur_.hasNewParams())) {
            System.err.println("Les Parametres ont change");
            projet_.addParam(HiswaResource.HIS, fEditeur_.getParams());
        }
        final BuTaskOperation op = new BuTaskOperation(this, "Calcul", "oprCalculer");
        op.start();
        final BuMainPanel mp = getMainPanel();
        new Thread() {

            public void run() {
                String msg;
                if (op.isAlive()) {
                    msg = SERVEUR_HISWA.getMsgOperationEnCours();
                    while (msg == null) {
                        msg = SERVEUR_HISWA.getMsgOperationEnCours();
                        try {
                            Thread.sleep(300);
                        } catch (final InterruptedException e) {
                        }
                    }
                }
                while (op.isAlive()) {
                    msg = SERVEUR_HISWA.getMsgOperationEnCours();
                    if (msg == null) {
                        mp.setProgression(100);
                        mp.setMessage("Op�ration termin�e");
                        try {
                            Thread.sleep(1000);
                        } catch (final InterruptedException e) {
                        }
                        break;
                    }
                    mp.setMessage(msg.substring(0, msg.indexOf(':')));
                    mp.setProgression(Integer.parseInt(msg.substring(msg.indexOf(':') + 1)));
                    try {
                        Thread.sleep(1000);
                    } catch (final InterruptedException e) {
                    }
                }
                mp.setProgression(0);
                mp.setMessage("");
            }
        }.start();
    }

    public void oprCalculer() {
        setEnabledForAction("CALCULER", false);
        setEnabledForAction("RESULTATSBRUTS", false);
        setEnabledForAction("VUE3D", false);
        setEnabledForAction("EDITERHISWA", false);
        setEnabledForAction("VISUALISER[false]", false);
        setEnabledForAction("CALQUES", false);
        System.err.println("Transmission des parametres...");
        iparams_.parametresHIS((SParametresHIS) projet_.getParam(HiswaResource.HIS));
        System.err.println("Execution du calcul...");
        String msg = "";
        try {
            iflux_ = ITransfertFichierASCIIHelper.narrow(SERVEUR_HISWA.copieFichiers());
        } catch (final Exception e) {
            e.printStackTrace();
        }
        SParametresHIS paramsHIS = iparams_.parametresHIS();
        final String[] nomFich = new String[(paramsHIS.parametresRead.grilles).length];
        try {
            for (int i = 0; i < nomFich.length; i++) {
                nomFich[i] = paramsHIS.parametresRead.grilles[i].fname;
                iflux_.nomFichier(nomFich[i]);
                try {
                    System.err.println("Copie du fichier : \n" + pathFichierImport_ + nomFich[i] + " sur le serveur ...");
                    final LineNumberReader fic = new LineNumberReader(new BufferedReader(new FileReader(pathFichierImport_ + nomFich[i]), 262144));
                    String ligne;
                    final String[] lignes = new String[1];
                    while (!((ligne = fic.readLine()) == null)) {
                        lignes[0] = ligne + "\n";
                        iflux_.ecrit(lignes);
                    }
                    fic.close();
                } catch (final IOException e) {
                    System.err.println("Erreur lors de la lecture du fichier annexe \na copier sur le serveur : " + e.getMessage());
                }
            }
            iflux_.fermer();
            paramsHIS = null;
            SERVEUR_HISWA.calcul(CONNEXION_HISWA);
        } catch (final org.omg.CORBA.UNKNOWN u) {
            msg += "Un erreur s'est produite.\n";
            msg += u.getMessage() + "\n";
            new BuDialogError(getApp(), isHiswa_, msg).activate();
            setEnabledForAction("CALCULER", true);
            return;
        }
        new BuDialogMessage(getApp(), isHiswa_, "Calcul Termin�\nCliquez sur \"Continuer\" pour voir les r�sultats.").activate();
        System.err.println("Operation de Calcul terminee.");
        receptionResultats();
        setEnabledForAction("CALCULER", true);
        setEnabledForAction("CALQUES", true);
        setEnabledForAction("RESULTATSBRUTS", true);
        setEnabledForAction("EDITERHISWA", true);
    }

    protected void receptionResultats() {
        System.err.println("Reception des resultats...");
        projet_.addResult(HiswaResource.DAT, iresults_.resultatsHiswaTable());
        if (fEditeur_ != null) {
            fEditeur_.resetParams();
        }
        affichageResultats("true");
    }

    protected void affichageResultats(final String calcul) {
        final boolean calcul_ = (Boolean.valueOf(calcul)).booleanValue();
        final BuMainPanel mp = getMainPanel();
        System.err.println("Visualisation des resultats...");
        if ((projet_.containsResults())) {
            mp.setMessage("Affichage...");
            mp.setProgression(10);
            new BuTaskOperation(this, "Construction du GroupeCalques") {

                public void act() {
                    if (fCalques_ == null) {
                        gc_ = construitCalques();
                        mp.setProgression(20);
                        gc_.setTitle("Calques Hiswa");
                        gc_.setBackground(Color.white);
                        mp.setProgression(50);
                        fCalques_ = new HiswaFilleCalques(gc_, arbre_);
                        mp.setProgression(70);
                        arbre_.setModel(fCalques_.getArbreCalqueModel());
                        vc_ = fCalques_.getVueCalque();
                        vc_.setBackground(Color.white);
                        vc_.setTaskView(mp.getTaskView());
                        fCalques_.setSize(new Dimension(750, 500));
                        mp.setProgression(80);
                        addInternalFrame(fCalques_);
                        mp.setProgression(100);
                    } else {
                        updateCalques(gc_);
                        mp.setProgression(40);
                        arbre_.refresh();
                        fCalques_.repaint(0);
                        mp.setProgression(80);
                        if (fCalques_.isClosed()) {
                            addInternalFrame(fCalques_);
                        } else {
                            activateInternalFrame(fCalques_);
                        }
                        mp.setProgression(100);
                    }
                    mp.setMessage("");
                    mp.setProgression(0);
                    if (resYapod_.hasToChangeRepere()) {
                        vc_.changeRepere(this, rectangle_.boite());
                    }
                    setEnabledForAction("SELECTIONCALQUES", true);
                    final String[] resuValides = ((SParametresHIS) projet_.getParam(HiswaResource.HIS)).parametresTable.options;
                    final boolean[] validation = new boolean[parametresResultats_.length];
                    for (int i = 0; i < resuValides.length; i++) {
                        for (int j = 0; j < correspondance_.length; j++) {
                            if (resuValides[i].equals("VEL")) {
                                validation[4] = true;
                                break;
                            }
                            if (resuValides[i].equals("FORCE")) {
                                validation[5] = true;
                                break;
                            }
                            if (resuValides[i].equals("TRANSP")) {
                                validation[6] = true;
                                break;
                            }
                            if ((resuValides[i].toLowerCase()).equals(correspondance_[j])) {
                                validation[j] = true;
                                break;
                            }
                        }
                    }
                    for (int i = 0; i < parametresResultats_.length; i++) {
                        setEnabledForAction("AJOUTCALQUE[" + i + "]", validation[i]);
                    }
                    if (j3d_) {
                        setEnabledForAction("VUE3D", true);
                    }
                }
            }.start();
            if (calcul_) {
                new BuTaskOperation(this, "Construction du Tableau des Resultats") {

                    public void act() {
                        resultatsBruts();
                    }
                }.start();
            }
        } else {
            final String msgError = "Le projet ne contient pas de R�sultats!\nVous devez lancer un calcul d'abord";
            final BuDialogError dialogError = new BuDialogError(this, isHiswa_, msgError);
            dialogError.activate();
        }
    }

    protected BGroupeCalque construitCalques() {
        try {
            final SParametresHIS paramsHIS = (SParametresHIS) projet_.getParam(HiswaResource.HIS);
            final SResultatsHiswaTable resTable = (SResultatsHiswaTable) projet_.getResult(HiswaResource.DAT);
            resYapod_ = new HiswaOutilsResultats();
            resYapod_.setBaseResultats(resTable, paramsHIS);
            rectangle_ = resYapod_.getRectangle();
            paramsGouraud_ = new BParametresGouraud();
            paramsGouraud_.setNiveau(32);
            paramsGouraud_.setTaille(32);
            final BCalqueCartouche cart = new BCalqueCartouche();
            cart.setName("cqCARTOUCHE");
            cart.setTitle("Cartouche");
            cart.setInformations(idHiswa_);
            cart.setForeground(Color.black);
            cart.setBackground(new Color(255, 255, 224));
            cart.setFont(new Font("SansSerif", Font.PLAIN, 10));
            cart.setVisible(false);
            final BCalqueDomaine dom = new BCalqueDomaine();
            dom.setName("cqDOMAINE");
            dom.setTitle("Domaine");
            dom.setFont(new Font("SansSerif", Font.PLAIN, 8));
            dom.setAttenue(false);
            dom.setVisible(false);
            final BCalqueGrille cg = new BCalqueGrille();
            cg.setForeground(Color.lightGray);
            cg.setTitle("Grille");
            cg.setName("cqGRILLE");
            cg.setPasX(resYapod_.getPasOptimal());
            cg.setPasY(resYapod_.getPasOptimal());
            leg_ = new BCalqueLegende();
            leg_.setTitle("L�gende");
            leg_.setName("cqLEGENDE");
            leg_.setForeground(java.awt.Color.blue);
            final BCalqueGrilleReguliere cHS = new BCalqueGrilleReguliere();
            cHS.setTitle(HiswaTableauResultats.RESULTATS[0]);
            cHS.setName("cqHiswaHsign");
            cHS.setRectangle(rectangle_);
            cHS.setValeurs(resYapod_.getSpecificResult("hsign"));
            cHS.setLegende(leg_);
            leg_.setBackground(cHS, new Color(255, 230, 250));
            cHS.setParametresGouraud(paramsGouraud_);
            cHS.setVisible(false);
            gc_ = new BGroupeCalque();
            gc_.add(cart);
            gc_.add(leg_);
            gc_.add(cg);
            gc_.add(cHS);
            gc_.add(dom);
            cg.setBoite(rectangle_.boite());
            return gc_;
        } catch (final Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    protected void updateCalques(final BGroupeCalque gc) {
        final BCalque[] calques = gc.getTousCalques();
        BCalqueGrille cg = null;
        for (int i = 0; i < calques.length; i++) {
            if (calques[i] instanceof BCalqueCartouche) {
                ((BCalqueCartouche) calques[i]).setInformations(idHiswa_);
            } else if (calques[i] instanceof BCalqueDomaine) {
            } else if (calques[i] instanceof BCalqueGrille) {
                cg = (BCalqueGrille) calques[i];
            }
        }
        if (cg != null) {
            cg.setBoite(gc.getDomaine());
        }
    }

    protected void ajoutCalque(final String arg) {
        int indice;
        BCalqueAffichage calque;
        indice = Integer.parseInt(arg);
        if (((indice >= 4) && (indice <= 6)) || (indice == 1)) {
            calque = resYapod_.getSpecificCalqueVecteur(correspondance_[indice]);
        } else {
            calque = new BCalqueGrilleReguliere();
            ((BCalqueGrilleReguliere) calque).setRectangle(rectangle_);
            ((BCalqueGrilleReguliere) calque).setValeurs(resYapod_.getSpecificResult(correspondance_[indice]));
            ((BCalqueGrilleReguliere) calque).setParametresGouraud(paramsGouraud_);
        }
        calque.setTitle(parametresResultats_[indice]);
        calque.setName("cqHiswa" + correspondance_[indice]);
        calque.setLegende(leg_);
        leg_.setBackground(calque, new Color(255, 230, 250));
        calque.setVisible(false);
        gc_.add(calque);
        calque.revalidate();
        arbre_.refresh();
    }

    protected void resultatsBruts() {
        final HiswaTableauResultats tabResultats = new HiswaTableauResultats();
        String[] resuDemandes;
        String[] resuDemandesTmp;
        int nbResultatsSelectionnes = 0;
        resuDemandesTmp = new String[20];
        options_ = HiswaPreferences.HISWA;
        for (int i = 0; i < 20; i++) {
            if (options_.getBooleanProperty(HiswaTableauResultats.RESULTATS[i])) {
                resuDemandesTmp[nbResultatsSelectionnes++] = HiswaTableauResultats.RESULTATS[i];
            }
        }
        resuDemandes = new String[nbResultatsSelectionnes];
        System.arraycopy(resuDemandesTmp, 0, resuDemandes, 0, nbResultatsSelectionnes);
        resuDemandesTmp = null;
        tabResultats.setResultatsDemandes(resuDemandes);
        tabResultats.setResultatsValides(((SParametresHIS) projet_.getParam(HiswaResource.HIS)).parametresTable.options);
        tabResultats.setResultats((SResultatsHiswaTable) projet_.getResult(HiswaResource.DAT));
        if (fTableau_ == null) {
            fTableau_ = new HiswaFilleTableau();
            fTableau_.setTable(tabResultats);
            fTableau_.setPreferredSize(new Dimension(640, 350));
            fTableau_.pack();
            addInternalFrame(fTableau_);
        } else {
            if (fTableau_.isClosed()) {
                addInternalFrame(fTableau_);
            } else {
                activateInternalFrame(fTableau_);
            }
            fTableau_.setTable(tabResultats);
            fTableau_.setPreferredSize(new Dimension(640, 350));
            fTableau_.pack();
            fTableau_.setVisible(true);
        }
    }

    protected void vue3d() {
        final BuMainPanel mp = getMainPanel();
        new BuTaskOperation(this, "Construction de la Vue 3D") {

            public void act() {
                if (fVue3D_ == null) {
                    System.out.println("Creation vue3D");
                    mp.setMessage("Construction des Volumes");
                    mp.setProgression(10);
                    gv_ = construitVolumes();
                    mp.setProgression(80);
                    final BGroupeLumiere gl = new BGroupeLumiere();
                    gl.setName("Lumi�res");
                    final BLumiereDirectionnelle l1 = new BLumiereDirectionnelle(new Vector3f(1, 0, 1), Color.white);
                    final BLumiereDirectionnelle l2 = new BLumiereDirectionnelle(new Vector3f(-1, 0, 1), Color.white);
                    l1.setName("Droite");
                    l2.setName("Gauche");
                    gl.add(l1);
                    gl.add(l2);
                    l1.setRapide(false);
                    l1.setVisible(true);
                    l2.setRapide(false);
                    l2.setVisible(true);
                    final BGroupeStandard gs = new BGroupeStandard();
                    gs.setName("Univers");
                    gs.add(gv_);
                    gs.add(gl);
                    fVue3D_ = new EbliFilleVue3D(idHiswa_, getApp(), false);
                    fVue3D_.setRoot(gs);
                    fVue3D_.getUnivers().init();
                    fVue3D_.setSize(new Dimension(600, 400));
                    mp.setProgression(80);
                    addInternalFrame(fVue3D_);
                    try {
                        fVue3D_.setIcon(true);
                    } catch (final PropertyVetoException e) {
                    }
                    activateInternalFrame(fVue3D_);
                    fVue3D_.getUnivers().getCanvas3D().repaint();
                    mp.setProgression(100);
                } else {
                    if (fVue3D_.isClosed()) {
                        addInternalFrame(fVue3D_);
                        fVue3D_.montre();
                    } else {
                        activateInternalFrame(fVue3D_);
                        fVue3D_.montre();
                    }
                }
                mp.setMessage("");
                mp.setProgression(0);
            }
        }.start();
    }

    protected BGroupeVolume construitVolumes() {
        BGroupeVolume gv = null;
        double[][] resCalques = null;
        double[] resVolume = null;
        int nbMailleX = 0;
        int nbMailleY = 0;
        final BuMainPanel mp = getMainPanel();
        try {
            mp.setMessage("Creation de la Vue 3D de la Hauteur de Houle");
            mp.setProgression(20);
            System.out.println("Creation de la Vue 3D de la Hauteur de Houle");
            houle_ = new BGrilleReguliere("Hauteur de Houle");
            houle_.setBoite(rectangle_.boite().o_, rectangle_.boite().e_);
            mp.setProgression(30);
            resCalques = resYapod_.getSpecificResult("hsign");
            nbMailleX = resYapod_.getNbMailleX();
            nbMailleY = resYapod_.getNbMailleY();
            resVolume = new double[(nbMailleX + 1) * (nbMailleY + 1)];
            for (int j = 0; j < (nbMailleY + 1); j++) {
                for (int i = 0; i < (nbMailleX + 1); i++) {
                    resVolume[j * (nbMailleX + 1) + i] = resCalques[i][j];
                }
            }
            mp.setProgression(40);
            houle_.setGeometrie(nbMailleX + 1, nbMailleY + 1, resVolume);
            houle_.setCouleur(new Color(50, 95, 193));
            houle_.setEclairage(true);
            houle_.setTransparence(0);
            final BPaletteCouleurSimple pc = new BPaletteCouleurSimple();
            houle_.setCouleur(Color.BLUE);
            imp_ = new BGroupeVolume();
            imp_.setName("Import");
            gv = new BGroupeVolume();
            mp.setProgression(75);
            gv.add(houle_);
            gv.add(imp_);
            imp_.setVisible(true);
            imp_.setRapide(false);
            houle_.setRapide(false);
            houle_.setVisible(true);
            gv.setName("Volumes");
            mp.setMessage("Construction des Volumes achev�e");
            mp.setMessage("");
            return gv;
        } catch (final Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    protected void editer() {
        PipedOutputStream out;
        PipedInputStream in;
        try {
            in = new PipedInputStream();
            out = new PipedOutputStream(in);
            if ((fEditeur_ != null) && (fEditeur_.hasNewParams())) {
                System.err.println("Les Parametres ont change");
                DParametresHiswa.ecritParametresHIS(out, fEditeur_.getParams());
            } else {
                DParametresHiswa.ecritParametresHIS(out, (SParametresHIS) projet_.getParam(HiswaResource.HIS));
            }
            if (fEditeur_ == null) {
                fEditeur_ = new HiswaFilleEditeur(this, in);
                fEditeur_.setProjet(projet_);
                fEditeur_.setTokenMarker(new HiswaTokenMarker());
                fEditeur_.setPreferredSize(new Dimension(640, 480));
                addInternalFrame(fEditeur_);
            } else {
                if (fEditeur_.isClosed()) {
                    fEditeur_.clearTextArea();
                    fEditeur_.setProjet(projet_);
                    fEditeur_.setText(in);
                    addInternalFrame(fEditeur_);
                } else {
                    activateInternalFrame(fEditeur_);
                }
                fEditeur_.setVisible(true);
            }
            in.close();
            out.close();
            setEnabledForAction("REFAIRE", false);
            setEnabledForAction("DEFAIRE", false);
        } catch (final IOException ex) {
            System.err.println("Erreur lors de La lecture du fichier de Parametres: ");
            ex.printStackTrace();
            return;
        }
    }

    public void fullscreen() {
        if (getCurrentInternalFrame() == fVue3D_) {
            fVue3D_.fullscreen();
        } else {
            super.fullscreen();
        }
    }

    private void ouvrir() {
        if (projet_.estConfigure()) {
            fermer();
        }
        projet_.setEnrResultats(true);
        System.err.println("Ouverture du projet ...");
        projet_.ouvrir();
        if (!projet_.estConfigure()) {
            projet_.fermer();
        } else {
            pathFichierImport_ = projet_.getFichier();
            pathFichierImport_ = pathFichierImport_.substring(0, pathFichierImport_.lastIndexOf(System.getProperty("file.separator")));
            pathFichierImport_ += File.separator;
            new BuDialogMessage(getApp(), isHiswa_, "Param�tres charg�s").activate();
            if (!projet_.containsParam(HiswaResource.HIS)) {
                setEnabledForAction("IMPORTER", true);
            } else {
                setEnabledForAction("EDITERHISWA", true);
            }
            setEnabledForAction("EXPORTER", true);
            setEnabledForAction("ENREGISTRER", true);
            setEnabledForAction("ENREGISTRERSOUS", true);
            setEnabledForAction("FERMER", true);
            if (projet_.containsResults()) {
                setEnabledForAction("CALQUES", true);
                setEnabledForAction("VISUALISER[false]", true);
                setEnabledForAction("RESULTATSBRUTS", true);
            }
            if (isConnected()) {
                if (projet_.containsParam(HiswaResource.HIS)) {
                    setEnabledForAction("CALCULER", true);
                }
            } else {
                setEnabledForAction("CALCULER", false);
            }
            updateTitle();
        }
    }

    private void enregistrer() {
        String msg = "";
        if ((projet_.containsResults())) {
            projet_.setEnrResultats(true);
        }
        if ((fEditeur_ != null) && (fEditeur_.hasNewParams())) {
            msg += "Vous avez edit� les param�tres sans relancer de calcul\n";
            msg += "Voulez-vous quand meme les enregistrer?";
            if (new BuDialogConfirmation(getApp(), isHiswa_, msg).activate() == JOptionPane.YES_OPTION) {
                projet_.addParam(HiswaResource.HIS, fEditeur_.getParams());
            }
        }
        System.err.println("Enregistrement du projet ...");
        projet_.enregistre();
    }

    private void enregistrerSous() {
        String msg = "";
        if ((projet_.containsResults())) {
            projet_.setEnrResultats(true);
        }
        if ((fEditeur_ != null) && (fEditeur_.hasNewParams())) {
            msg += "Vous avez edit� les param�tres sans relancer de calcul\n";
            msg += "Voulez-vous quand meme les enregistrer?";
            if (new BuDialogConfirmation(getApp(), isHiswa_, msg).activate() == JOptionPane.YES_OPTION) {
                projet_.addParam(HiswaResource.HIS, fEditeur_.getParams());
            }
        }
        System.err.println("Enregistrement du projet ...");
        projet_.enregistreSous();
        updateTitle();
    }

    private void fermer() {
        if (projet_.estConfigure()) {
            if (new BuDialogConfirmation(getApp(), isHiswa_, "Voulez-vous enregistrer votre projet?").activate() == JOptionPane.YES_OPTION) {
                enregistrer();
            }
        }
        final BuDesktop dk = getMainPanel().getDesktop();
        if (fCalques_ != null) {
            arbre_.removeTreeSelectionListener(fCalques_);
            try {
                fCalques_.setClosed(true);
                fCalques_.setSelected(false);
            } catch (final PropertyVetoException e) {
            }
            dk.removeInternalFrame(fCalques_);
            gc_.detruire(gc_);
            gc_ = new BGroupeCalque();
            gc_.setTitle("Calques Hiswa");
            arbre_.setModel(null);
            arbre_.refresh();
            fCalques_ = null;
            leg_ = null;
            rectangle_ = null;
            resYapod_ = null;
            paramsGouraud_ = null;
        }
        if (fTableau_ != null) {
            try {
                fTableau_.setClosed(true);
                fTableau_.setSelected(false);
            } catch (final PropertyVetoException e) {
            }
            dk.removeInternalFrame(fTableau_);
            fTableau_ = null;
        }
        if (fEditeur_ != null) {
            try {
                fEditeur_.setClosed(true);
                fEditeur_.setSelected(false);
            } catch (final PropertyVetoException e) {
            }
            dk.removeInternalFrame(fEditeur_);
            fEditeur_ = null;
        }
        if (fVue3D_ != null) {
            try {
                fVue3D_.setClosed(true);
                fVue3D_.setSelected(false);
            } catch (final PropertyVetoException e) {
            }
            dk.removeInternalFrame(fVue3D_);
            fVue3D_.cache();
            fVue3D_ = null;
        }
        projet_.fermer();
        dk.repaint();
        setEnabledForAction("IMPRIMER", false);
        setEnabledForAction("IMPORTER", false);
        setEnabledForAction("EXPORTER", false);
        setEnabledForAction("ENREGISTRER", false);
        setEnabledForAction("ENREGISTRERSOUS", false);
        setEnabledForAction("FERMER", false);
        setEnabledForAction("PARAMETRES", false);
        setEnabledForAction("EDITERHISWA", false);
        setEnabledForAction("CALCULER", false);
        setEnabledForAction("CALQUES", false);
        setEnabledForAction("SELECTIONCALQUES", false);
        for (int i = 1; i < HiswaTableauResultats.RESULTATS.length; i++) {
            setEnabledForAction("AJOUTCALQUE[" + i + "]", false);
        }
        setEnabledForAction("VISUALISER[false]", false);
        setEnabledForAction("RESULTATSBRUTS", false);
        setEnabledForAction("VUE3D", false);
        setTitle("Hiswa " + isHiswa_.version);
    }

    protected void buildPreferences(final List _prefs) {
        _prefs.add(new BuUserPreferencesPanel(this));
        _prefs.add(new BuLanguagePreferencesPanel(this));
        _prefs.add(new BuLookPreferencesPanel(this));
        _prefs.add(new BuDesktopPreferencesPanel(this));
        _prefs.add(new BuBrowserPreferencesPanel(this));
        _prefs.add(new HiswaPreferencesPanel(this));
        _prefs.add(new VolumePreferencesPanel(this));
    }

    private void parametre() {
    }

    public void exit() {
        fermer();
        closeConnexions();
        super.exit();
    }

    public void finalize() {
        closeConnexions();
    }

    public boolean isCloseFrameMode() {
        return false;
    }

    /**
   * @see org.fudaa.fudaa.commun.impl.FudaaCommonImplementation#getApplicationPreferences()
   */
    public BuPreferences getApplicationPreferences() {
        return HiswaPreferences.HISWA;
    }

    /**
   * @see org.fudaa.fudaa.commun.dodico.FudaaImplementation#clearVariables()
   */
    protected void clearVariables() {
        CONNEXION_HISWA = null;
        SERVEUR_HISWA = null;
    }

    /**
   * @see org.fudaa.fudaa.commun.dodico.FudaaImplementation#getTacheConnexionMap()
   */
    protected FudaaDodicoTacheConnexion[] getTacheConnexionMap() {
        final FudaaDodicoTacheConnexion c = new FudaaDodicoTacheConnexion(SERVEUR_HISWA, CONNEXION_HISWA);
        return new FudaaDodicoTacheConnexion[] { c };
    }

    /**
   * @see org.fudaa.fudaa.commun.dodico.FudaaImplementation#getTacheDelegateClass()
   */
    protected Class[] getTacheDelegateClass() {
        return new Class[] { DCalculHiswa.class };
    }

    /**
   * @see org.fudaa.fudaa.commun.dodico.FudaaImplementation#initConnexions(java.util.Map)
   */
    protected void initConnexions(final Map _r) {
        final FudaaDodicoTacheConnexion c = (FudaaDodicoTacheConnexion) _r.get(DCalculHiswa.class);
        CONNEXION_HISWA = c.getConnexion();
        SERVEUR_HISWA = ICalculHiswaHelper.narrow(c.getTache());
    }
}
