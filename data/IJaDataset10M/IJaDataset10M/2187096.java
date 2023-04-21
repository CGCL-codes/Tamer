package eu.planets_project.pp.plato.action.fte;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import org.apache.commons.logging.Log;
import org.jboss.annotation.ejb.cache.Cache;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import eu.planets_project.pp.plato.action.interfaces.IAnalyseResults;
import eu.planets_project.pp.plato.action.interfaces.IFastTrackAnalyseResults;
import eu.planets_project.pp.plato.action.interfaces.IWorkflowStep;
import eu.planets_project.pp.plato.action.workflow.AbstractWorkflowStep;
import eu.planets_project.pp.plato.bean.PrepareChangesForPersist;
import eu.planets_project.pp.plato.model.PlanState;
import eu.planets_project.pp.plato.model.tree.Leaf;
import eu.planets_project.pp.plato.util.PlatoLogger;

/**
 * Action handler for the third step of Fast-track evaluation: Analyse results.
 * @author cbu
 */
@Stateful
@Scope(ScopeType.SESSION)
@Name("FTanalyse")
@Cache(org.jboss.ejb3.cache.NoPassivationCache.class)
public class AnalyseResultsFastTrack extends AbstractWorkflowStep implements IFastTrackAnalyseResults {

    private static final long serialVersionUID = 4514931519604967651L;

    private static final Log log = PlatoLogger.getLogger(AnalyseResultsFastTrack.class);

    @In(create = true)
    private IAnalyseResults analyseResults;

    public AnalyseResultsFastTrack() {
        requiredPlanState = new Integer(PlanState.FTE_ALTERNATIVES_EVALUATED);
    }

    @Override
    protected void init() {
        for (Leaf l : selectedPlan.getTree().getRoot().getAllLeaves()) {
            l.initTransformer(new Double(2.5));
        }
        analyseResults.init();
    }

    public String createPreservationPlan() {
        selectedPlan.createPPFromFastTrack();
        selectedPlan.getState().setValue(PlanState.INITIALISED);
        return "success";
    }

    /**
     * @see AbstractWorkflowStep#getWorkflowstepName()
     */
    @Override
    protected String getWorkflowstepName() {
        return "FTanalyse";
    }

    /**
     * @see AbstractWorkflowStep#getSuccessor()
     */
    @Override
    protected IWorkflowStep getSuccessor() {
        return null;
    }

    @Override
    public String save() {
        analyseResults.save();
        if (selectedPlan.getRecommendation().getAlternative() != null) {
            selectedPlan.getState().setValue(PlanState.FTE_RESULTS_ANALYSED);
        } else {
            selectedPlan.getState().setValue(PlanState.FTE_ALTERNATIVES_EVALUATED);
        }
        PrepareChangesForPersist prep = new PrepareChangesForPersist(user.getUsername());
        prep.prepare(selectedPlan.getState());
        em.persist(em.merge(selectedPlan.getState()));
        em.flush();
        changed = "";
        return "";
    }

    public boolean validate(boolean showValidationErrors) {
        return true;
    }

    @Destroy
    @Remove
    public void destroy() {
    }
}
