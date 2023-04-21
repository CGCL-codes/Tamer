package playground.yu.removeOldestPlan;

import java.util.List;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.controler.Controler;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.population.PlanImpl;
import org.matsim.core.replanning.PlanStrategyImpl;
import org.matsim.core.replanning.StrategyManager;
import org.matsim.core.replanning.selectors.RandomPlanSelector;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.config.ConfigUtils;
import org.matsim.testcases.MatsimTestCase;
import playground.yu.replanning.StrategyManagerWithRemoveOldestPlan;

/**
 * testcase for the function in class {@code
 * playground.yu.replanning.StrategyManagerWithRemoveOldestPlan}
 * 
 * @author yu
 * 
 */
public class StrategyManagerRemoveOldestPlanTest extends MatsimTestCase {

    private static class ROPControler extends Controler {

        public ROPControler(String[] args) {
            super(args);
        }
    }

    /**
	 * test remove oldest Plan, who has been very long not used i.e. with
	 * smallest index in choice set, at the same Time, the "makeSelectedYoung"
	 * can also be tested, some codes are copied from {@code
	 * org.matsim.core.replanning.StrategyManagerTest.
	 * testSetPlanSelectorForRemoval()}
	 */
    public void testRemoveOldestPlan() {
        StrategyManager manager = new StrategyManager();
        manager.addStrategy(new PlanStrategyImpl(new RandomPlanSelector()), 1.0);
        PersonImpl p = new PersonImpl(new IdImpl(1));
        PlanImpl[] plans = new PlanImpl[7];
        for (int i = 0; i < plans.length; i++) {
            plans[i] = p.createAndAddPlan(false);
            plans[i].setScore(Double.valueOf(i * 10));
        }
        Population pop = ((ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig())).getPopulation();
        pop.addPerson(p);
        {
            manager.setMaxPlansPerAgent(plans.length - 2);
            manager.run(pop);
            List<Plan> planList = p.getPlans();
            assertEquals("wrong number of plans.", 5, planList.size());
            assertFalse("plan should have been removed.", planList.contains(plans[0]));
            assertFalse("plan should have been removed.", planList.contains(plans[1]));
            for (int i = 2; i < plans.length; i++) {
                assertTrue("plan should not have been removed.", planList.contains(plans[i]));
            }
            for (Plan plan : planList) {
                System.out.println("index\t" + planList.indexOf(plan) + "\tplan :\t" + plan);
            }
        }
        manager = new StrategyManagerWithRemoveOldestPlan();
        manager.addStrategy(new PlanStrategyImpl(new RandomPlanSelector()), 1.0);
        p = new PersonImpl(new IdImpl(1));
        plans = new PlanImpl[7];
        for (int i = 0; i < plans.length; i++) {
            plans[i] = p.createAndAddPlan(false);
            plans[i].setScore(Double.valueOf((7 - i) * 10));
        }
        pop.getPersons().clear();
        pop.addPerson(p);
        {
            manager.setMaxPlansPerAgent(plans.length - 3);
            manager.run(pop);
            List<Plan> planList = p.getPlans();
            assertEquals("wrong number of plans.", 4, planList.size());
            assertFalse("plan should have been removed.", planList.contains(plans[0]));
            assertFalse("plan should have been removed.", planList.contains(plans[1]));
            assertFalse("plan should have been removed.", planList.contains(plans[2]));
            for (int i = 3; i < plans.length; i++) {
                assertTrue("plan should have not been removed.", planList.contains(plans[3]));
            }
            assertTrue("the last(newest) plan should be a selected Plan", planList.get(manager.getMaxPlansPerAgent() - 1).isSelected());
            for (Plan plan : planList) {
                System.out.println("index\t" + planList.indexOf(plan) + "\tplan :\t" + plan);
            }
        }
    }
}
