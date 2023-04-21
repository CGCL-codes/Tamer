package org.matsim.roadpricing;

import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.LegImpl;
import org.matsim.core.population.PopulationFactoryImpl;
import org.matsim.core.population.routes.ModeRouteFactory;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.router.PlansCalcRoute;
import org.matsim.core.router.costcalculators.FreespeedTravelTimeAndDisutility;
import org.matsim.core.router.util.AStarLandmarksFactory;
import org.matsim.core.router.util.DijkstraFactory;
import org.matsim.core.router.util.PersonalizableTravelDisutility;
import org.matsim.core.router.util.PreProcessLandmarks;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.roadpricing.RoadPricingScheme.Cost;
import org.matsim.testcases.MatsimTestCase;

/**
 * Tests the correct working of {@link TravelDisutilityIncludingToll} by using it
 * to calculate some routes with {@link PlansCalcRoute}.
 *
 * @author mrieser
 */
public class TollTravelCostCalculatorTest extends MatsimTestCase {

    public void testDistanceTollRouter() {
        Config config = loadConfig(null);
        ScenarioImpl scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        Fixture.createNetwork2(scenario);
        Network network = scenario.getNetwork();
        RoadPricingScheme toll = new RoadPricingScheme();
        toll.setType("distance");
        toll.addLink(scenario.createId("5"));
        toll.addLink(scenario.createId("11"));
        Fixture.createPopulation2(scenario);
        Population population = scenario.getPopulation();
        ModeRouteFactory routeFactory = ((PopulationFactoryImpl) population.getFactory()).getModeRouteFactory();
        FreespeedTravelTimeAndDisutility timeCostCalc = new FreespeedTravelTimeAndDisutility(config.planCalcScore());
        PersonalizableTravelDisutility costCalc = new TravelDisutilityIncludingToll(timeCostCalc, toll);
        AStarLandmarksFactory routerFactory = new AStarLandmarksFactory(network, timeCostCalc);
        PreProcessLandmarks commonRouterData = new PreProcessLandmarks(timeCostCalc);
        commonRouterData.run(network);
        Person person1 = population.getPersons().get(new IdImpl("1"));
        LegImpl leg = ((LegImpl) (person1.getPlans().get(0).getPlanElements().get(1)));
        new PlansCalcRoute(config.plansCalcRoute(), network, costCalc, timeCostCalc, new DijkstraFactory(), routeFactory).run(population);
        Fixture.compareRoutes("2 5 6", (NetworkRoute) ((LegImpl) (person1.getPlans().get(0).getPlanElements().get(1))).getRoute());
        clearRoutes(population);
        assertNull(leg.getRoute());
        new PlansCalcRoute(config.plansCalcRoute(), network, costCalc, timeCostCalc, routerFactory, routeFactory).run(population);
        Fixture.compareRoutes("2 5 6", (NetworkRoute) leg.getRoute());
        Cost morningCost = toll.addCost(6 * 3600, 10 * 3600, 0.0006);
        clearRoutes(population);
        new PlansCalcRoute(config.plansCalcRoute(), network, costCalc, timeCostCalc, routeFactory).run(population);
        Fixture.compareRoutes("2 5 6", (NetworkRoute) leg.getRoute());
        clearRoutes(population);
        new PlansCalcRoute(config.plansCalcRoute(), network, costCalc, timeCostCalc, routerFactory, routeFactory).run(population);
        Fixture.compareRoutes("2 5 6", (NetworkRoute) leg.getRoute());
        toll.removeCost(morningCost);
        toll.addCost(6 * 3600, 10 * 3600, 0.0007);
        clearRoutes(population);
        new PlansCalcRoute(config.plansCalcRoute(), network, costCalc, timeCostCalc, routeFactory).run(population);
        Fixture.compareRoutes("2 3 4 6", (NetworkRoute) leg.getRoute());
        clearRoutes(population);
        new PlansCalcRoute(config.plansCalcRoute(), network, costCalc, timeCostCalc, routerFactory, routeFactory).run(population);
        Fixture.compareRoutes("2 3 4 6", (NetworkRoute) leg.getRoute());
    }

    public void testCordonTollRouter() {
        Config config = loadConfig(null);
        ScenarioImpl scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        Fixture.createNetwork2(scenario);
        Network network = scenario.getNetwork();
        RoadPricingScheme toll = new RoadPricingScheme();
        toll.setType("cordon");
        toll.addLink(scenario.createId("5"));
        toll.addLink(scenario.createId("11"));
        Fixture.createPopulation2(scenario);
        Population population = scenario.getPopulation();
        ModeRouteFactory routeFactory = ((PopulationFactoryImpl) population.getFactory()).getModeRouteFactory();
        FreespeedTravelTimeAndDisutility timeCostCalc = new FreespeedTravelTimeAndDisutility(config.planCalcScore());
        PersonalizableTravelDisutility costCalc = new TravelDisutilityIncludingToll(timeCostCalc, toll);
        AStarLandmarksFactory routerFactory = new AStarLandmarksFactory(network, timeCostCalc);
        Person person1 = population.getPersons().get(new IdImpl("1"));
        LegImpl leg = ((LegImpl) (person1.getPlans().get(0).getPlanElements().get(1)));
        new PlansCalcRoute(config.plansCalcRoute(), network, costCalc, timeCostCalc, routeFactory).run(population);
        Fixture.compareRoutes("2 5 6", (NetworkRoute) leg.getRoute());
        clearRoutes(population);
        new PlansCalcRoute(config.plansCalcRoute(), network, costCalc, timeCostCalc, routerFactory, routeFactory).run(population);
        Fixture.compareRoutes("2 5 6", (NetworkRoute) leg.getRoute());
        Cost morningCost = toll.addCost(6 * 3600, 10 * 3600, 0.06);
        clearRoutes(population);
        new PlansCalcRoute(config.plansCalcRoute(), network, costCalc, timeCostCalc, routeFactory).run(population);
        Fixture.compareRoutes("2 5 6", (NetworkRoute) leg.getRoute());
        toll.removeCost(morningCost);
        clearRoutes(population);
        new PlansCalcRoute(config.plansCalcRoute(), network, costCalc, timeCostCalc, routerFactory, routeFactory).run(population);
        Fixture.compareRoutes("2 5 6", (NetworkRoute) leg.getRoute());
        toll.addCost(6 * 3600, 10 * 3600, 0.067);
        clearRoutes(population);
        new PlansCalcRoute(config.plansCalcRoute(), network, costCalc, timeCostCalc, routeFactory).run(population);
        Fixture.compareRoutes("2 3 4 6", (NetworkRoute) leg.getRoute());
        clearRoutes(population);
        new PlansCalcRoute(config.plansCalcRoute(), network, costCalc, timeCostCalc, routerFactory, routeFactory).run(population);
        Fixture.compareRoutes("2 3 4 6", (NetworkRoute) leg.getRoute());
    }

    /**
	 * Clears all routes from all legs of all persons in the given population to make sure they are calculated from new.
	 *
	 * @param population
	 */
    private void clearRoutes(final Population population) {
        for (Person person : population.getPersons().values()) {
            for (Plan plan : person.getPlans()) {
                for (PlanElement pe : plan.getPlanElements()) {
                    if (pe instanceof Leg) {
                        ((Leg) pe).setRoute(null);
                    }
                }
            }
        }
    }
}
