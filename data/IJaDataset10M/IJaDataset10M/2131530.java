package playground.mfeil;

import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.population.LegImpl;
import org.matsim.core.scoring.ScoringFunction;
import org.matsim.core.scoring.ScoringFunctionAdapter;
import org.matsim.core.scoring.ScoringFunctionFactory;

/**
 * @author dgrether
 */
public class PlanScorer {

    private ScoringFunctionFactory factory;

    public PlanScorer(final ScoringFunctionFactory factory) {
        this.factory = factory;
    }

    public double getScore(final Plan plan) {
        ScoringFunctionAdapter function = (ScoringFunctionAdapter) this.factory.createNewScoringFunction(plan);
        boolean firstActivityDone = false;
        for (PlanElement pe : plan.getPlanElements()) {
            if (pe instanceof Activity) {
                Activity act = (Activity) pe;
                if (firstActivityDone) {
                    function.startActivity(act.getStartTime(), act);
                    firstActivityDone = true;
                }
                function.endActivity(act.getEndTime(), act);
            } else if (pe instanceof LegImpl) {
                LegImpl leg = (LegImpl) pe;
                function.startLeg(leg.getDepartureTime(), leg);
                function.endLeg(leg.getArrivalTime());
            }
        }
        function.finish();
        return function.getScore();
    }
}
