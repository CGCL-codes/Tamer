package org.fudaa.fudaa.crue.common;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import com.memoire.bu.BuAbstractPreferencesPanel;
import com.memoire.bu.BuMenu;
import com.memoire.bu.BuMenuBar;
import com.memoire.bu.BuPreferences;
import com.memoire.bu.BuPreferencesDialog;
import com.memoire.bu.BuPreferencesMainPanel;
import com.memoire.bu.BuResource;
import com.memoire.bu.BuScrollPane;
import com.memoire.bu.BuTaskView;
import org.fudaa.dodico.crue.common.CruePreferences;
import org.fudaa.dodico.crue.io.Crue10FileFormatFactory;
import org.fudaa.dodico.crue.io.common.CrueFileType;
import org.fudaa.dodico.crue.metier.etude.ManagerEMHScenario;
import org.fudaa.dodico.crue.projet.OrdonnanceurCrue10;
import org.fudaa.dodico.crue.projet.OrdonnanceurCrue9;
import org.fudaa.fudaa.commun.impl.FudaaGuiLib;
import org.fudaa.fudaa.crue.explorer.CrueLauncher;
import org.fudaa.fudaa.crue.projet.CrueProjet;
import org.fudaa.fudaa.crue.projet.ScenarioComparaisonLauncher;
import org.fudaa.fudaa.crue.projet.ScenarioSaverUI;

/**
 * Implementation du logiciel crue.
 * 
 * @author Adrien Hadoux
 */
public class CrueEditorImplementation extends CrueCommonImplementation {

    private final class TogggleActionListener implements ActionListener {

        private final String cmd;

        /**
     * @param cmd
     */
        public TogggleActionListener(String cmd) {
            super();
            this.cmd = cmd;
        }

        public void actionPerformed(ActionEvent e) {
            getMainPanel().getRightColumn().getToggleComponent(cmd).setVisible(getMainPanel().getRightColumn().getToggleButton(cmd).isSelected());
            getMainPanel().getRightColumn().revalidate();
        }
    }

    /**
   * Projet Crue.
   */
    protected CrueProjet currentProjet;

    /**
   * @return the currentProjet
   */
    public CrueProjet getCurrentProjet() {
        return currentProjet;
    }

    BuTaskView taches_;

    protected void cmdOuvrirEtude() {
        final File f = FudaaGuiLib.ouvrirFileChooser("Fichier etu", Crue10FileFormatFactory.getInstance().getFileFormat(CrueFileType.ETU, Crue10FileFormatFactory.getLastVersion()).getFileFormat().createFileFilter(), getFrame(), false);
        if (f != null) {
            cmdOuvrirFile(f);
        }
    }

    /**
   * Ouvrir un projet ETU
   */
    @Override
    public void cmdOuvrirFile(final File _f) {
        if (_f == null) {
            return;
        }
        final CrueProjet crueProjet = CrueProjet.open(_f, this);
        setProjet(_f, crueProjet);
    }

    public void setProjet(final File _f, final CrueProjet crueProjet) {
        if (crueProjet != null) {
            if (currentProjet != null) {
                if (!question(FCrueResource.getS("ihm.confirmation"), FCrueResource.getS("ihm.confirm.fermeture.etude"))) {
                    return;
                }
                currentProjet.close();
            }
            currentProjet = crueProjet;
            init(crueProjet, _f);
        }
    }

    /**
   * Initialisation de l'interface avec les donnees metier CNR.
   * 
   * @param project
   * @param f
   */
    public void init(final CrueProjet crueproject, final File f) {
        currentProjet = crueproject;
        String title = f.getAbsolutePath();
        final ManagerEMHScenario courant = currentProjet.getProject().getScenarioCourant();
        if (courant != null) {
            title = title + " | Scénario: " + courant.getNom();
            if (courant.getRunCourant() != null) {
                title = title + " | Run: " + courant.getRunCourant().getId();
            }
        }
        this.setTitle("FCv" + CrueLauncher.infoCrue_.version + "| " + title);
        currentProjet.activate();
        projectOpened();
    }

    @Override
    public void init() {
        super.init();
        final BuMenuBar mb = getMainMenuBar();
        BuMenu menu = (BuMenu) mb.getMenu("MENU_EDITION");
        menu.addMenuItem(Messages.getString("impl.configText.label"), "CONFIG_TEXT_EDITOR", BuResource.BU.getToolIcon("crystal_configurer"), new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ExecConfigUIChooser chooser = new ExecConfigUIChooser(ExecConfigPreferences.TEXT_EDITOR);
                chooser.setUi(CrueEditorImplementation.this);
                chooser.display(CrueEditorImplementation.this.getFrame());
            }
        });
        menu.addMenuItem(Messages.getString("impl.configCrue9.label"), "CONFIG_CRUE9", BuResource.BU.getToolIcon("crystal_configurer"), new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ExecConfigUIChooser chooser = new ExecConfigUIChooser(ExecConfigPreferences.CRUE9);
                chooser.setUi(CrueEditorImplementation.this);
                chooser.display(CrueEditorImplementation.this.getFrame());
            }
        });
        final int nb = mb.getMenuCount();
        mb.add(buildValiderMenu(), nb - 1);
        mb.add(buildEMHMenu(), nb - 1);
        mb.add(buildScenarioMenu(), nb - 1);
        mb.add(buildEtudeMenu(), nb - 1);
    }

    Point lastPointNormal;

    Dimension lastSizeNormal;

    @Override
    public void start() {
        super.start();
        final BuMenuBar mb = getMainMenuBar();
        setEnabledForAction("PREFERENCE", true);
        mb.getMenu("MENU_FICHIER").setEnabled(false);
        mb.getMenu("MENU_AIDE").setEnabled(false);
        getFrame().setName("editor.mainWindow");
        UserPreferencesSaver.loadWindowPreferences((JFrame) getFrame());
        getFrame().addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                if (!UserPreferencesSaver.isWindowMaximized((JFrame) getFrame())) {
                    lastPointNormal = getFrame().getLocation();
                }
            }

            @Override
            public void componentResized(ComponentEvent e) {
                if (!UserPreferencesSaver.isWindowMaximized((JFrame) getFrame())) {
                    lastSizeNormal = getFrame().getSize();
                }
            }
        });
    }

    private void projectOpened() {
        changeMenuState(true);
        rebuildPanelRight();
    }

    public void projectClosed() {
        if (currentProjet != null) {
            scenarioLoaded(false);
            currentProjet.close();
        }
        currentProjet = null;
        getFrame().setTitle(getInformationsSoftware().name + " " + getInformationsSoftware().version);
        changeMenuState(false);
        rebuildPanelRight();
    }

    protected void changeMenuState(final boolean enable) {
        setEnabledForAction("GESTIONETUDE", enable);
        setEnabledForAction("FERMERETUDE", enable);
        setEnabledForAction("SHOW_INFO", enable);
    }

    @Override
    public void exit() {
        if (currentProjet != null) {
            currentProjet.saveWindowPreferences();
        }
        UserPreferencesSaver.saveWindowPreferences((JFrame) getFrame(), lastPointNormal, lastSizeNormal);
        CruePreferences.Crue.writeIniFile();
        BuPreferences.BU.writeIniFile();
        super.exit();
    }

    /**
   * @param enable true si scenario courant est charge
   */
    public void scenarioLoaded(final boolean enable) {
        setEnabledForAction("GESTIONSCENARIOS", enable);
        setEnabledForAction("ENREGISTRERSCENARIOS", enable);
        setEnabledForAction("GESTIONCOMPARAISON", enable);
        setEnabledForAction("VALIDERSCENARIOMENU", enable);
        setEnabledForAction("VALIDER_SCENARIO", enable);
        setEnabledForAction("ENREGISTRERSCENARIO_CRUE10", enable);
        setEnabledForAction("ENREGISTRERSCENARIO_CRUE9", enable);
        setEnabledForAction("GESTIONEMH", enable);
        setEnabledForAction("MENUPROJET", enable);
        setEnabledForAction("VALIDERSCENARIOMENU", enable);
        setEnabledForAction("MENUEMH", enable);
    }

    private BuMenu buildEtudeMenu() {
        final BuMenu menu = new BuMenu(BuResource.BU.getString("Etude"), "MenuETUDE");
        menu.setIcon(null);
        menu.addMenuItem(FCrueResource.getS("ihm.ouvrir.etude"), "OUVRIRETUDE", FCrueResource.CRUE.getIcon("crystal_ouvrir.png"), this).setEnabled(true);
        menu.addMenuItem(FCrueResource.getS("ihm.gerer.etude"), "GESTIONETUDE", FCrueResource.CRUE.getIcon("information"), this).setEnabled(false);
        menu.addMenuItem(FCrueResource.getS("ihm.fermer.etude"), "FERMERETUDE", FCrueResource.CRUE.getIcon("crystal_fermer.png"), this).setEnabled(false);
        menu.addSeparator();
        menu.addMenuItem(FCrueResource.getS("ihm.infos.etude"), "SHOW_INFO", FCrueResource.CRUE.getIcon("crystal_aide.png"), this).setEnabled(currentProjet != null);
        return menu;
    }

    /**
   * Construit le menu de base du projet CNR.
   * 
   * @param project
   * @return
   */
    private BuMenu buildScenarioMenu() {
        final BuMenu menu = new BuMenu(BuResource.BU.getString("Scénario"), "MENUPROJET");
        menu.setEnabled(false);
        menu.setIcon(null);
        menu.addMenuItem(FCrueResource.getS("ihm.gerer.scenarios"), "GESTIONSCENARIOS", FCrueResource.CRUE.getIcon("crystal_ouvrir.png"), this).setEnabled(false);
        menu.addMenuItem(FCrueResource.getS("ihm.enregistrer.scenario"), "ENREGISTRERSCENARIOS", FCrueResource.CRUE.getIcon("crystal_enregistrer.png"), this).setEnabled(false);
        menu.addMenuItem(FCrueResource.getS("ihm.enregistrerSous.scenario.crue9"), "ENREGISTRERSCENARIO_CRUE9", FCrueResource.CRUE.getIcon("crystal_exporter.png"), this).setEnabled(false);
        menu.addMenuItem(FCrueResource.getS("ihm.enregistrerSous.scenario.crue10"), "ENREGISTRERSCENARIO_CRUE10", FCrueResource.CRUE.getIcon("crystal_exporter.png"), this).setEnabled(false);
        menu.addSeparator(FCrueResource.getS("ihm.operations"));
        menu.addMenuItem(FCrueResource.getS("ihm.comparaison.scenarios"), "GESTIONCOMPARAISON", FCrueResource.CRUE.getIcon("crystal_controler.png"), this).setEnabled(false);
        return menu;
    }

    private BuMenu buildValiderMenu() {
        final BuMenu menu = new BuMenu(FCrueResource.getS("modelisation.menu"), "VALIDERSCENARIOMENU");
        menu.setEnabled(false);
        menu.setIcon(null);
        menu.addMenuItem(FCrueResource.getS("valider.menu.action"), "VALIDER_SCENARIO", FCrueResource.CRUE.getIcon("crystal_document.png"), this).setEnabled(false);
        return menu;
    }

    /**
   * Menu des EMH.
   * 
   * @return
   */
    private BuMenu buildEMHMenu() {
        final BuMenu menu = new BuMenu(BuResource.BU.getString("EMH"), "MENUEMH");
        menu.setEnabled(false);
        menu.setIcon(null);
        menu.addSeparator(FCrueResource.getS("ihm.emh"));
        menu.addMenuItem(FCrueResource.getS("ihm.gerer.emhs"), "GESTIONEMH", FCrueResource.CRUE.getIcon("crystal_document.png"), this).setEnabled(false);
        return menu;
    }

    public void rebuildPanelRight() {
        getMainPanel().getRightColumn().removeToggledComponent("SCENARIOS");
        getMainPanel().getRightColumn().removeToggledComponent("MODELES");
        getMainPanel().getRightColumn().removeToggledComponent("SSMODELES");
        if (currentProjet != null) {
            getMainPanel().getRightColumn().addToggledComponent(BuResource.BU.getString("Scenarios"), "SCENARIOS", new BuScrollPane(currentProjet.getListeScenarios()), new TogggleActionListener("SCENARIOS"));
            getMainPanel().getRightColumn().addToggledComponent(BuResource.BU.getString("Modèles"), "MODELES", new BuScrollPane(currentProjet.getListeModeles()), new TogggleActionListener("MODELES"));
            getMainPanel().getRightColumn().addToggledComponent(BuResource.BU.getString("Sous-Modèles"), "SSMODELES", new BuScrollPane(currentProjet.getListeSousModeles()), new TogggleActionListener("SSMODELES"));
            getMainPanel().updateSplits();
        }
        getMainPanel().getRightColumn().revalidate();
    }

    @Override
    public void actionPerformed(final ActionEvent _evt) {
        final String action = _evt.getActionCommand();
        if (action.equals("OUVRIRETUDE")) {
            cmdOuvrirEtude();
        } else if (action.equals("SHOW_INFO") && currentProjet != null) {
            currentProjet.displayInfoOnConfigLoaded();
        } else if (action.equals("ENREGISTRERSCENARIOS")) {
            currentProjet.getManagerError().clear();
            final ScenarioSaverUI saver = new ScenarioSaverUI(currentProjet.getProject().getScenarioCourant(), currentProjet);
            saver.saveWithScenarioType();
        } else if (action.equals("GESTIONSCENARIOS")) {
            currentProjet.addScenarioManagerFille();
        } else if (action.equals("GESTIONEMH")) {
            currentProjet.addEMHViewFille();
        } else if (action.equals("FERMERETUDE")) {
            if (question(FCrueResource.getS("ihm.confirmation"), FCrueResource.getS("ihm.question.fermeture.etude"))) {
                projectClosed();
            }
        } else if (action.equals("GESTIONETUDE")) {
            currentProjet.addEtudeManagerFille();
        } else if (action.equals("GESTIONCOMPARAISON")) {
            new ScenarioComparaisonLauncher(currentProjet.getProject()).compute(getUIContext());
        } else if ("ENREGISTRERSCENARIO_CRUE9".equals(action)) {
            currentProjet.getManagerError().clear();
            List<String> allExtensions = OrdonnanceurCrue9.getAllExtensions();
            final File dest = ouvrirFileChooser(FCrueResource.getS("ihm.enregistrerSous.scenario.crue9"), allExtensions.toArray(new String[allExtensions.size()]), true, new CrueExportCtuluFileChooserTest(this, allExtensions));
            if (dest != null) {
                final ScenarioSaverUI saver = new ScenarioSaverUI(CrueExportCtuluFileChooserTest.getSansExtension(dest).getAbsolutePath(), currentProjet.getProject().getScenarioCourant(), currentProjet);
                saver.generateInCrue9();
            }
        } else if ("ENREGISTRERSCENARIO_CRUE10".equals(action)) {
            currentProjet.getManagerError().clear();
            final File dest = ouvrirFileChooser(FCrueResource.getS("ihm.enregistrerSous.scenario.crue10"), new String[] { "xml" }, true, new CrueExportCtuluFileChooserTest(this, OrdonnanceurCrue10.getAllExtensions()));
            if (dest != null) {
                final ScenarioSaverUI saver = new ScenarioSaverUI(CrueExportCtuluFileChooserTest.getSansExtension(dest).getAbsolutePath(), currentProjet.getProject().getScenarioCourant(), currentProjet);
                saver.generateInCrue10();
            }
        } else if (action.equals("VALIDER_SCENARIO")) {
            currentProjet.validCurrentScenario();
        } else {
            super.actionPerformed(_evt);
        }
    }

    /**
   * Description de la methode.
   */
    protected void preferences() {
        final BuPreferencesMainPanel pn = new BuPreferencesMainPanel();
        final List r = new ArrayList();
        buildPreferences(r);
        final int nb = r.size();
        for (int i = 0; i < nb; i++) {
            pn.addTab((BuAbstractPreferencesPanel) r.get(i));
        }
        pn.setPreferredSize(new Dimension(850, 500));
        final BuPreferencesDialog d = new BuPreferencesDialog(getFrame(), pn);
        d.pack();
        d.setLocationRelativeTo(getFrame());
        d.setVisible(true);
        d.dispose();
    }

    protected void buildPreferences(final List _frAddTab) {
        _frAddTab.add(new CruePreferencesPanel(this, getApplicationPreferences()));
    }

    private void toggleVisible(String cmd) {
        getMainPanel().getRightColumn().getToggleComponent(cmd).setVisible(getMainPanel().getRightColumn().getToggleButton(cmd).isSelected());
        getMainPanel().getRightColumn().revalidate();
    }
}
