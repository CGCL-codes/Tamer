package org.matsim.core.scoring;

import org.matsim.api.basic.v01.TransportMode;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.ScenarioImpl;
import org.matsim.core.api.network.Link;
import org.matsim.core.api.population.Activity;
import org.matsim.core.api.population.Leg;
import org.matsim.core.api.population.Person;
import org.matsim.core.api.population.Plan;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.CharyparNagelScoringConfigGroup;
import org.matsim.core.events.AgentMoneyEvent;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.scoring.charyparNagel.CharyparNagelScoringFunctionFactory;
import org.matsim.testcases.MatsimTestCase;

/**
 * An abstract class to test features that all scoring functions should support.
 *
 * @author mrieser
 */
public abstract class ScoringFunctionTest extends MatsimTestCase {

    protected abstract ScoringFunction getScoringFunctionInstance(final Plan plan);

    /**
	 * Sets up the configuration to be useful for scoring plans. This implementation
	 * sets the parameters for scoring functions returned by 
	 * {@link CharyparNagelScoringFunctionFactory}, overwrite it to test your own 
	 * custom scoring function.
	 *
	 * @param config
	 */
    protected void setupScoringConfig(final Config config) {
        CharyparNagelScoringConfigGroup scoring = config.charyparNagelScoring();
        scoring.setBrainExpBeta(2.0);
        scoring.setLateArrival(-18.0);
        scoring.setEarlyDeparture(0.0);
        scoring.setPerforming(6.0);
        scoring.setTraveling(-6.0);
        scoring.setTravelingPt(0.0);
        scoring.setMarginalUtlOfDistanceCar(0.0);
        scoring.setWaiting(0.0);
        CharyparNagelScoringConfigGroup.ActivityParams params = new CharyparNagelScoringConfigGroup.ActivityParams("home");
        params.setTypicalDuration(16 * 3600);
        scoring.addActivityParams(params);
        params = new CharyparNagelScoringConfigGroup.ActivityParams("work");
        params.setTypicalDuration(8 * 3600);
        scoring.addActivityParams(params);
    }

    /**
	 * Tests if the scoring function correctly handles {@link AgentMoneyEvent}.
	 * It generates one person with one plan having two activities (home, work)
	 * and a car-leg in between. It then tests the scoring function by calling
	 * several methods on an instance of the scoring function with the
	 * aforementioned plan.
	 */
    public void testAddMoney() {
        Scenario scenario = new ScenarioImpl();
        setupScoringConfig(scenario.getConfig());
        Person person1 = new PersonImpl(new IdImpl(1));
        Plan plan1 = person1.createPlan(true);
        Activity act1a = plan1.createActivity("home", (Link) null);
        Leg leg1 = plan1.createLeg(TransportMode.car);
        Activity act1b = plan1.createActivity("work", (Link) null);
        ScoringFunction sf1 = getScoringFunctionInstance(plan1);
        sf1.startActivity(0, act1a);
        sf1.endActivity(7 * 3600);
        sf1.startLeg(7 * 3600, leg1);
        sf1.endLeg(7 * 3600 + 100);
        sf1.startActivity(7 * 3600 + 100, act1b);
        sf1.endActivity(24 * 3600);
        sf1.finish();
        double score1 = sf1.getScore();
        ScoringFunction sf2 = getScoringFunctionInstance(plan1);
        sf2.startActivity(0, act1a);
        sf2.addMoney(1.23);
        sf2.endActivity(7 * 3600);
        sf2.startLeg(7 * 3600, leg1);
        sf2.addMoney(-2.46);
        sf2.endLeg(7 * 3600 + 100);
        sf2.startActivity(7 * 3600 + 100, act1b);
        sf2.addMoney(4.86);
        sf2.endActivity(24 * 3600);
        sf2.addMoney(-0.28);
        sf2.finish();
        double score2 = sf2.getScore();
        assertEquals(1.23 - 2.46 + 4.86 - 0.28, score2 - score1, EPSILON);
    }
}
