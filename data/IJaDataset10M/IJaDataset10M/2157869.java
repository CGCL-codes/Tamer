package playground.yu.integration.cadyts.parameterCalibration.withCarCounts.testLeftTurn;

import org.apache.log4j.Logger;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.IterationStartsEvent;
import org.matsim.core.controler.events.ScoringEvent;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.IterationStartsListener;
import org.matsim.core.controler.listener.ScoringListener;
import org.matsim.core.controler.listener.StartupListener;
import playground.yu.integration.cadyts.parameterCalibration.withCarCounts.mnlValidation.CadytsChoice;
import playground.yu.integration.cadyts.parameterCalibration.withCarCounts.scoring.PlansScoring4PC_I;

/**
 * a changed copy of {@code PlansScoring} for the parameter calibration,
 * especially in order to put new parameters to CharyparNagelScoringConfigGroup
 * 
 * @author yu
 * 
 */
public class PlansScoringWithLeftTurnPenalty4PC implements PlansScoring4PC_I, StartupListener, ScoringListener, IterationStartsListener {

    private static final Logger log = Logger.getLogger(PlansScoringWithLeftTurnPenalty4PC.class);

    protected CadytsChoice planScorer;

    @Override
    public CadytsChoice getPlanScorer() {
        return planScorer;
    }

    @Override
    public void notifyIterationStarts(final IterationStartsEvent event) {
        planScorer.reset(event.getIteration());
    }

    @Override
    public void notifyScoring(final ScoringEvent event) {
        planScorer.finish();
    }

    @Override
    public void notifyStartup(final StartupEvent event) {
        Controler ctl = event.getControler();
        planScorer = new Events2ScoreWithLeftTurnPenalty4PC(ctl.getConfig(), ctl.getScoringFunctionFactory(), ctl.getScenario());
        log.debug("PlansScoringWithLeftTurnPenalty loaded ScoringFunctionFactory");
        ctl.getEvents().addHandler(planScorer);
    }
}
