package org.fudaa.fudaa.crue.modelling.action;

import org.fudaa.dodico.crue.metier.emh.EMHScenario;
import org.fudaa.fudaa.crue.common.PerspectiveEnum;
import org.fudaa.fudaa.crue.common.action.AbstractPerspectiveAwareAction;
import org.fudaa.fudaa.crue.common.services.ModellingScenarioService;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;

public abstract class AbstractModellingAction extends AbstractPerspectiveAwareAction {

    protected ModellingScenarioService scenarioService = Lookup.getDefault().lookup(ModellingScenarioService.class);

    private final Result<EMHScenario> resultat;

    public AbstractModellingAction(boolean enableInEditMode) {
        super(PerspectiveEnum.MODELLING, enableInEditMode);
        resultat = scenarioService.getLookup().lookupResult(EMHScenario.class);
        resultat.addLookupListener(this);
        resultChanged(null);
    }

    @Override
    protected boolean getEnableState() {
        return super.getEnableState() && scenarioService.isScenarioLoaded();
    }
}
