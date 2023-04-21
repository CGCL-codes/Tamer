package org.matsim.planomat.costestimators;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
import org.jgap.impl.DoubleGene;
import org.matsim.plans.Act;
import org.matsim.plans.Leg;
import org.matsim.plans.Plan;
import org.matsim.plans.Route;
import org.matsim.scoring.ScoringFunction;
import org.matsim.world.Location;

public class CharyparNagelFitnessFunction extends FitnessFunction {

    public static final double FITNESS_OFFSET = 10000.0;

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Plan plan;

    private LegTravelTimeEstimator legTravelTimeEstimator;

    private ScoringFunction sf;

    public CharyparNagelFitnessFunction(ScoringFunction sf, Plan plan, LegTravelTimeEstimator legTravelTimeEstimator) {
        super();
        this.sf = sf;
        this.plan = plan;
        this.legTravelTimeEstimator = legTravelTimeEstimator;
    }

    @Override
    protected double evaluate(IChromosome a_subject) {
        double planScore = 0.0;
        double travelTime;
        sf.reset();
        double now = 0.0;
        for (int ii = 0; ii < a_subject.size(); ii++) {
            now += ((DoubleGene) a_subject.getGene(ii)).doubleValue();
            sf.startLeg(now, null);
            Location origin = ((Act) plan.getActsLegs().get(ii * 2)).getLink();
            Location destination = ((Act) plan.getActsLegs().get((ii + 1) * 2)).getLink();
            Route route = ((Leg) plan.getActsLegs().get((ii * 2) + 1)).getRoute();
            travelTime = this.legTravelTimeEstimator.getLegTravelTimeEstimation(this.plan.getPerson().getId(), now, origin, destination, route, "car");
            now += travelTime;
            sf.endLeg(now);
        }
        sf.finish();
        planScore = sf.getScore();
        return Math.max(0.0, planScore + CharyparNagelFitnessFunction.FITNESS_OFFSET);
    }
}
