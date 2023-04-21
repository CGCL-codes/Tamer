package org.fudaa.fudaa.crue.projet;

import java.io.File;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import org.fudaa.ctulu.CtuluLogGroup;
import org.fudaa.ctulu.CtuluLog;
import org.fudaa.dodico.crue.config.CrueConfigMetierContext;
import org.fudaa.dodico.crue.config.CrueConfigMetierLoader;
import org.fudaa.dodico.crue.config.CrueOptions;
import org.fudaa.dodico.crue.config.CrueConfigMetier;
import org.fudaa.dodico.crue.io.Crue10FileFormat;
import org.fudaa.dodico.crue.io.Crue10FileFormatFactory;
import org.fudaa.dodico.crue.io.Crue10FileFormatFactory.FileFormatResult;
import org.fudaa.dodico.crue.io.common.CrueDataImpl;
import org.fudaa.dodico.crue.io.common.CrueFileType;
import org.fudaa.dodico.crue.io.common.CrueIOResu;
import org.fudaa.dodico.crue.metier.emh.EMHScenario;
import org.fudaa.dodico.crue.metier.etude.EMHProjet;
import org.fudaa.dodico.crue.metier.etude.ManagerEMHScenario;
import org.fudaa.dodico.crue.validation.ValidateConnectionModele;
import org.fudaa.dodico.crue.validation.ValidatorHelper;
import org.fudaa.fudaa.crue.builder.CrueModeleBuilder;
import org.fudaa.fudaa.crue.builder.CrueScenarioBuilder;
import org.fudaa.fudaa.crue.common.CommonGuiLib;
import org.fudaa.fudaa.crue.common.CrueEditorImplementation;
import org.fudaa.fudaa.crue.common.FCrueResource;
import org.fudaa.fudaa.crue.common.Messages;
import org.fudaa.fudaa.crue.common.UiContext;
import org.fudaa.fudaa.crue.common.UserPreferencesSaver;
import org.fudaa.fudaa.crue.view.CrueFilleEMH;
import org.fudaa.fudaa.crue.view.CrueFilleInfosGenerales;
import org.fudaa.fudaa.crue.view.CrueFilleScenarioManager;
import org.jdesktop.swingx.JXTreeTable;

/**
 * Projet Crue. Contient le contenu du fichier xml ETU lu. Manage les scc�narios, modeles et sous modeles crue au sein
 * de l'application fudaa.
 * 
 * @author Adrien Hadoux
 */
public class CrueProjet {

    private CrueFilleEMH emhViewFille;

    final File fichierEtu_;

    final CrueEditorImplementation impl;

    protected JList listeModeles = null;

    protected JList listeScenarios = null;

    protected JList listeSousModeles = null;

    final CtuluLogGroup managerError;

    /**
   * Structure correspondante au contenu du fichier xml ETU.
   */
    EMHProjet project;

    protected JList scenarioCourant;

    private CrueFilleScenarioManager scenarioFille;

    /**
   * Tree contenant l'arborescence du projet.
   */
    protected JXTreeTable treeScenario;

    /**
   * @param f
   * @param impl
   * @return
   */
    public static CrueProjet open(File f, CrueEditorImplementation impl) {
        CrueProjet res = new CrueProjet(f, impl);
        CtuluLogGroup managerError = new CtuluLogGroup(Messages.RESOURCE_BUNDLE);
        CtuluLog analyser = managerError.createLog();
        CrueConfigMetierLoader natureLoader = new CrueConfigMetierLoader();
        CrueIOResu<CrueConfigMetier> load = natureLoader.load();
        if (!load.getAnalyse().isEmpty()) {
            managerError.addLog(load.getAnalyse());
            CommonGuiLib.showDialog(managerError, impl.getUIContext(), FCrueResource.getS("ihm.titre.chargement.projet"));
            if (load.getAnalyse().containsErrors()) {
                impl.error(FCrueResource.getS("ihm.res.chargt.etude.abort"));
                return null;
            }
        }
        FileFormatResult<EMHProjet> fileFormatResult = Crue10FileFormatFactory.getInstance().getFileFormatForFile(CrueFileType.ETU, f);
        if (fileFormatResult.log.containsErrorOrFatalError()) {
            managerError.addLog(fileFormatResult.log);
        } else {
            Crue10FileFormat<EMHProjet> fileFormat = fileFormatResult.fileFormatFound;
            boolean ok = fileFormat.isValide(f, analyser);
            if (ok) {
                res.project = fileFormat.read(f, analyser, new CrueDataImpl(load.getMetier(), new CrueOptions())).getMetier();
                if (res.project != null) {
                    res.project.getInfos().setXmlVersion(fileFormat.getVersionName());
                }
            }
        }
        CommonGuiLib.showDialog(managerError, impl.getUIContext(), FCrueResource.getS("ihm.titre.chargement.projet"));
        if (managerError.containsFatalError() || res.project == null) {
            impl.error(FCrueResource.getS("ihm.res.chargt.etude.abort"));
            return null;
        }
        res.project.setPropDefinition(load.getMetier());
        if (res.project.getScenarioCourant() != null) {
            if (impl.question(FCrueResource.getS("ihm.chargement"), FCrueResource.getS("ihm.confirm.chargt.scenarioCourant", res.project.getScenarioCourant().getNom()))) {
                ScenarioLoaderUI algo = new ScenarioLoaderUI(res.project.getScenarioCourant(), res.project);
                boolean loaded = algo.loadScenarioAndShowBilan(impl.getUIContext());
                if (!loaded) {
                    res.project.setScenarioCourant(null);
                }
            } else {
                res.project.setScenarioCourant(null);
            }
        }
        res.updateScenarioCourant();
        return res;
    }

    /**
   * Lancement d'un projet.
   * 
   * @param fichierEtu
   */
    public CrueProjet(final File fichierEtu, CrueEditorImplementation impl) {
        fichierEtu_ = fichierEtu;
        this.impl = impl;
        managerError = new CtuluLogGroup(Messages.RESOURCE_BUNDLE);
    }

    /**
   * 
   */
    public void addEMHViewFille() {
        if (getProject().getScenarioCourant() == null || !getProject().getScenarioCourant().isLoaded()) {
            impl.message(Messages.getString("error.scenario.empty"));
            return;
        }
        boolean isNew = false;
        if (emhViewFille == null) {
            emhViewFille = new CrueFilleEMH(this);
            isNew = true;
        }
        impl.addInternalFrame(emhViewFille);
        if (isNew) {
            UserPreferencesSaver.loadInternalFrameLocationAndDimension(emhViewFille, getImpl().getMainPanel().getDesktop());
        }
    }

    public void addScenarioManagerFille() {
        boolean isNew = false;
        if (scenarioFille == null) {
            scenarioFille = new CrueFilleScenarioManager(this);
            isNew = true;
        }
        impl.addInternalFrame(scenarioFille);
        if (isNew) {
            UserPreferencesSaver.loadInternalFrameLocationAndDimension(scenarioFille, getImpl().getMainPanel().getDesktop());
            scenarioFille.updateSplitSize();
        }
    }

    public void close() {
        saveWindowPreferences();
        removeInternalFrame(scenarioFille);
        removeInternalFrame(emhViewFille);
        removeInternalFrame(etudeFille);
        scenarioFille = null;
        emhViewFille = null;
        etudeFille = null;
    }

    private CrueFilleInfosGenerales etudeFille;

    /**
   * 
   */
    public void addEtudeManagerFille() {
        boolean isNew = false;
        if (etudeFille == null) {
            isNew = true;
            etudeFille = new CrueFilleInfosGenerales(this);
        }
        impl.addInternalFrame(etudeFille);
        if (isNew) {
            UserPreferencesSaver.loadInternalFrameLocationAndDimension(etudeFille, getImpl().getMainPanel().getDesktop());
        }
    }

    public File getFichierEtu() {
        return fichierEtu_;
    }

    public CrueEditorImplementation getImpl() {
        return impl;
    }

    public UiContext getUiContext() {
        return impl.getUIContext();
    }

    /**
   * @return the listeModeles
   */
    public JList getListeModeles() {
        return listeModeles;
    }

    /**
   * @return the listeScenarios
   */
    public JList getListeScenarios() {
        return listeScenarios;
    }

    /**
   * @return the listeSousModeles
   */
    public JList getListeSousModeles() {
        return listeSousModeles;
    }

    public CtuluLogGroup getManagerError() {
        return managerError;
    }

    public void displayInfoOnConfigLoaded() {
        CrueConfigMetierContext ctx = project.getPropDefinition().getContext();
        String version = FCrueResource.getS("etu.versionLoaded", getProject().getInfos().getXmlVersion());
        String msg1 = ctx.isHomeLoaded() ? FCrueResource.getS("propriete.natureDef.loadedInHome", ctx.getHomeCrueConfigMetier().getAbsolutePath()) : FCrueResource.getS("propriete.natureDef.loadedDefault");
        impl.message("<html><body>" + version + "<br>" + msg1);
    }

    public EMHProjet getProject() {
        return project;
    }

    public void activate() {
        addScenarioManagerFille();
    }

    public void changeScenarioCourant(ManagerEMHScenario newCourant) {
        if (newCourant == null) {
            return;
        }
        ManagerEMHScenario courant = project.getScenarioCourant();
        if (newCourant != courant && project.getScenarioCourant() != null) {
            project.getScenarioCourant().clean();
        }
        close();
        impl.projectClosed();
        project.setScenarioCourant(newCourant);
        updateScenarioCourant();
        impl.init(this, getFichierEtu());
    }

    public void updateScenarioCourant() {
        if (project.getListeScenarios() != null) {
            listeScenarios = CrueScenarioBuilder.buildListScenario(project.getListeScenarios(), impl.getUIContext());
        }
        if (project.getListeModeles() != null) {
            listeModeles = CrueModeleBuilder.buildListModele(project.getListeModeles(), impl.getUIContext());
        }
        if (project.getListeSousModeles() != null) {
            listeSousModeles = CrueModeleBuilder.buildListSousModele(project.getListeSousModeles(), impl.getUIContext());
        }
        ManagerEMHScenario courant = project.getScenarioCourant();
        if (courant == null || (courant != null && !courant.isLoaded())) {
            project.setScenarioCourant(null);
            impl.scenarioLoaded(false);
            impl.getFrame().setTitle(fichierEtu_.getAbsolutePath());
            return;
        }
        scenarioCourant = CrueScenarioBuilder.buildListScenarioCourant(courant, impl.getUIContext());
        impl.scenarioLoaded(courant != null && courant.isLoaded());
    }

    private void removeInternalFrame(JInternalFrame frame) {
        if (frame != null) {
            this.impl.removeInternalFrame(frame);
            frame.dispose();
        }
    }

    public void setProject(EMHProjet project) {
        this.project = project;
    }

    /**
   * 
   */
    public void validCurrentScenario() {
        if (project.getScenarioCourant() != null) {
            EMHScenario emhScenario = project.getScenarioCourant().getEmh();
            CtuluLog analyse = ValidateConnectionModele.validateConnexite(emhScenario.getConcatSousModele());
            CtuluLogGroup mng = new CtuluLogGroup(Messages.RESOURCE_BUNDLE);
            mng.addLog(analyse);
            ValidatorHelper.validateAll(mng, emhScenario);
            CommonGuiLib.showDialog(mng, getImpl().getUIContext(), "Validation du scénario courant");
        }
    }

    public void saveWindowPreferences() {
        if (scenarioFille != null) {
            scenarioFille.saveSizes();
        }
        if (emhViewFille != null) {
            emhViewFille.saveSize();
        }
        if (etudeFille != null) {
            etudeFille.saveSizes();
        }
    }
}
