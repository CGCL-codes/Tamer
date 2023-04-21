package playground.yu.tests;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Route;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.population.MatsimPopulationReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.population.algorithms.AbstractPersonAlgorithm;
import org.matsim.population.algorithms.PlanAlgorithm;

public class PseudoPtLegRouteTimeDistTest extends AbstractPersonAlgorithm implements PlanAlgorithm {

    @Override
    public void run(Person person) {
        for (Plan plan : person.getPlans()) {
            run(plan);
        }
    }

    @Override
    public void run(Plan plan) {
        for (PlanElement pe : plan.getPlanElements()) {
            if (pe instanceof Leg) {
                Leg leg = (Leg) pe;
                if (leg.getMode().equals(TransportMode.pt)) {
                    Route route = leg.getRoute();
                    System.out.println("personID:\t" + plan.getPerson().getId() + "\tLeg traveltime:\t" + leg.getTravelTime() + "\tRoute traveltime:\t" + route.getTravelTime() + "\tdistance:\t" + route.getDistance());
                }
            }
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        new MatsimNetworkReader(scenario).readFile("test/input/2car1ptRoutes/net2.xml");
        new MatsimPopulationReader(scenario).readFile("test/input/2car1ptRoutes/preparePop/1.xml");
        new PseudoPtLegRouteTimeDistTest().run(scenario.getPopulation());
    }
}
