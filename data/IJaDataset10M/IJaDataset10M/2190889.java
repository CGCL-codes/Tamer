package org.fudaa.fudaa.crue.modelling.action;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.fudaa.dodico.crue.metier.etude.EMHProjet;
import org.fudaa.fudaa.crue.comparison.ScenarioComparaisonLauncher;
import org.fudaa.fudaa.crue.loader.LoaderService;
import org.fudaa.fudaa.crue.loader.ProjectLoadContainer;
import org.fudaa.fudaa.crue.study.services.EMHProjetService;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

@ActionID(category = "View", id = "org.fudaa.fudaa.crue.modelling.action.ModellingComparisonOtherEtuAction")
@ActionRegistration(displayName = "#CTL_ModellingComparisonOtherEtuAction")
@ActionReferences({ @ActionReference(path = "Menu/Modelling", position = 4, separatorAfter = 5) })
public final class ModellingComparisonOtherEtuAction extends AbstractModellingAction {

    private LoaderService loaderService = Lookup.getDefault().lookup(LoaderService.class);

    EMHProjetService projetService = Lookup.getDefault().lookup(EMHProjetService.class);

    public ModellingComparisonOtherEtuAction() {
        super(false);
        putValue(Action.NAME, NbBundle.getMessage(AbstractModellingAction.class, "CTL_ModellingComparisonOtherEtuAction"));
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new ModellingComparisonOtherEtuAction();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProjectLoadContainer otherProjet = loaderService.loadProject();
        if (otherProjet != null) {
            final EMHProjet projet = otherProjet.getProjet();
            boolean isSame = otherProjet.getEtuFile().equals(projetService.getEtuFile());
            new ScenarioComparaisonLauncher(scenarioService.getScenarioLoaded(), null, projet, isSame).compute();
        }
    }
}
