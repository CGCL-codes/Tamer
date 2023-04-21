package org.fudaa.fudaa.crue.modelling.action;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.fudaa.dodico.crue.projet.create.RunCreatorOptions;
import org.fudaa.fudaa.crue.study.actions.RunOptionPanelBuilder;
import org.fudaa.fudaa.crue.study.services.EMHProjetService;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

@ActionID(category = "View", id = "org.fudaa.fudaa.crue.modelling.ModellingLaunchRunOptionsAction")
@ActionRegistration(displayName = "#CTL_ModellingLaunchRunOptionsAction")
@ActionReferences({ @ActionReference(path = "Menu/Modelling", position = 14) })
public final class ModellingLaunchRunOptionsAction extends AbstractModellingAction {

    EMHProjetService projetService = Lookup.getDefault().lookup(EMHProjetService.class);

    public ModellingLaunchRunOptionsAction() {
        super(true);
        putValue(Action.NAME, NbBundle.getMessage(ModellingLaunchRunOptionsAction.class, "CTL_ModellingLaunchRunOptionsAction"));
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new ModellingLaunchRunOptionsAction();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RunOptionPanelBuilder panelBuilder = new RunOptionPanelBuilder(scenarioService.getManagerScenarioLoaded());
        RunCreatorOptions options = panelBuilder.getOptions();
        if (options == null) {
            return;
        }
        projetService.launchRun(scenarioService.getManagerScenarioLoaded(), options);
    }
}
