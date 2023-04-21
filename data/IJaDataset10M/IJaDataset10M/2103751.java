package playground.thibautd.jointtripsoptimizer.run;

import org.apache.log4j.Logger;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.corelisteners.EventsHandling;
import org.matsim.core.controler.corelisteners.PlansDumping;
import org.matsim.core.controler.corelisteners.PlansScoring;
import org.matsim.core.controler.corelisteners.RoadPricing;
import org.matsim.core.population.PopulationWriter;
import org.matsim.core.replanning.StrategyManager;
import org.matsim.core.replanning.StrategyManagerConfigLoader;
import org.matsim.core.router.PlansCalcRoute;
import org.matsim.core.router.util.PersonalizableTravelCost;
import org.matsim.core.router.util.PersonalizableTravelTime;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.population.algorithms.PlanAlgorithm;
import playground.thibautd.jointtripsoptimizer.population.JointActingTypes;
import playground.thibautd.jointtripsoptimizer.population.PopulationWithJointTripsWriterHandler;
import playground.thibautd.jointtripsoptimizer.population.ScenarioWithCliques;
import playground.thibautd.jointtripsoptimizer.replanning.JointPlansReplanning;
import playground.thibautd.jointtripsoptimizer.replanning.JointStrategyManager;
import playground.thibautd.jointtripsoptimizer.router.CarPassengerLegRouter;
import playground.thibautd.jointtripsoptimizer.scoring.JointPlansScoring;

/**
 * Custom controler for handling clique replanning
 * @author thibautd
 */
public class JointControler extends Controler {

    private static final Logger log = Logger.getLogger(JointControler.class);

    private JointPlansScoring plansScoring = null;

    /**
	 * Only constructor available, to enforce the initialization of a
	 * ScenarioWithCliques in the controler.
	 * The config has to be set in the scenario before.
	 */
    public JointControler(final ScenarioWithCliques scenario) {
        super((ScenarioImpl) scenario);
    }

    /**
	 * Same as the loadCoreListeners of the base class, excepts that it loads a
	 * JointPlanReplanning instance instead of a PlansReplanning one.
	 * This allows handling PopulationWithCliques populations.
	 * This has the drawback of breaking the getRoadPricing (final) method of
	 * the controler.
	 * {@inheritDoc}
	 * @see Controler#loadCoreListeners()
	 */
    @Override
    protected void loadCoreListeners() {
        this.addCoreControlerListener(new CoreControlerListener());
        this.plansScoring = new JointPlansScoring();
        this.addCoreControlerListener(this.plansScoring);
        if (this.config.scenario().isUseRoadpricing()) {
            this.addCoreControlerListener(new RoadPricing());
            log.warn("RoadPricing set in JointControler: getRoadPricing will be" + " broken.");
        }
        this.addCoreControlerListener(new JointPlansReplanning());
        this.addCoreControlerListener(new PlansDumping());
        this.addCoreControlerListener(new EventsHandling(this.events));
    }

    @Override
    protected StrategyManager loadStrategyManager() {
        StrategyManager manager = new JointStrategyManager();
        StrategyManagerConfigLoader.load(this, manager);
        return manager;
    }

    @Override
    public PlanAlgorithm createRoutingAlgorithm() {
        return createRoutingAlgorithm(this.createTravelCostCalculator(), this.getTravelTimeCalculator());
    }

    /**
	 * Creates a routing algorithm, which takes explicitly car passenger mode
	 * into account.
	 *
	 * @param travelCosts
	 *            the travel costs to be used for the routing
	 * @param travelTimes
	 *            the travel times to be used for the routing
	 * @return a new instance of a {@link PlanAlgorithm} to calculate the routes
	 *         of plans with the specified travelCosts and travelTimes. Only to
	 *         be used by a single thread, use multiple instances for multiple
	 *         threads!
	 */
    @Override
    public PlanAlgorithm createRoutingAlgorithm(final PersonalizableTravelCost travelCosts, final PersonalizableTravelTime travelTimes) {
        log.debug("routing algorithm created");
        PlansCalcRoute router = (PlansCalcRoute) super.createRoutingAlgorithm(travelCosts, travelTimes);
        router.addLegHandler(JointActingTypes.PASSENGER, new CarPassengerLegRouter());
        return router;
    }

    /**
	 * Exports plans in an importable format
	 */
    @Override
    protected void shutdown(final boolean unexpected) {
        super.shutdown(unexpected);
        PopulationWriter popWriter = new PopulationWriter(this.population, this.network, (this.getScenario()).getKnowledges());
        popWriter.setWriterHandler(new PopulationWithJointTripsWriterHandler(this.network, (this.getScenario()).getKnowledges()));
        popWriter.write(this.getControlerIO().getOutputFilename(FILENAME_POPULATION));
    }
}
