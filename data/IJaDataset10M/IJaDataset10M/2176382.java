package org.fudaa.fudaa.crue.modelling;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import org.fudaa.dodico.crue.metier.emh.EMHScenario;
import org.fudaa.fudaa.crue.common.services.ModellingScenarioService;
import org.fudaa.fudaa.crue.emh.EMHTreePanel;
import org.fudaa.fudaa.crue.study.services.EMHProjetService;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupListener;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.fudaa.fudaa.crue.modelling//ModellingEMH//EN", autostore = false)
@TopComponent.Description(preferredID = "ModellingEMHTopComponent", iconBase = "org/fudaa/fudaa/crue/modelling/rond-orange_16.png", persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = false)
@ActionID(category = "Window", id = "org.fudaa.fudaa.crue.modelling.ModellingEMHTopComponent")
@TopComponent.OpenActionRegistration(displayName = "#CTL_ModellingEMHAction", preferredID = "ModellingEMHTopComponent")
public final class ModellingEMHTopComponent extends TopComponent implements LookupListener {

    private ModellingScenarioService scenarioService = Lookup.getDefault().lookup(ModellingScenarioService.class);

    EMHProjetService projetService = Lookup.getDefault().lookup(EMHProjetService.class);

    private final Result<EMHScenario> resultat;

    private EMHTreePanel emhTreePanel;

    public ModellingEMHTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ModellingEMHTopComponent.class, "CTL_ModellingEMHAction"));
        setToolTipText(NbBundle.getMessage(ModellingEMHTopComponent.class, "HINT_ModellingEMHTopComponent"));
        resultat = scenarioService.getLookup().lookupResult(EMHScenario.class);
        resultat.addLookupListener(this);
        resultChanged(null);
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        if (scenarioService.isScenarioLoaded()) {
            scenarioLoaded();
        } else {
            scenarioUnloaded();
        }
    }

    private void scenarioLoaded() {
        emhTreePanel = new EMHTreePanel(projetService.getSelectedProject(), scenarioService.getScenarioLoaded());
        this.removeAll();
        this.add(new JScrollPane(emhTreePanel.build()));
        this.repaint();
    }

    private void scenarioUnloaded() {
        this.removeAll();
        this.add(new JLabel(MessagesModelling.getMessage("emhTopComponent.NoScenarioLoadedInformations")));
        super.revalidate();
        this.repaint();
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }
}
