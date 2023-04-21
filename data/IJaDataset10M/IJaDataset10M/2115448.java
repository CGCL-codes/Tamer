package org.matsim.contrib.evacuation.tutorial;

import org.apache.log4j.Logger;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.AfterMobsimEvent;
import org.matsim.core.controler.events.BeforeMobsimEvent;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.AfterMobsimListener;
import org.matsim.core.controler.listener.BeforeMobsimListener;
import org.matsim.core.controler.listener.StartupListener;
import org.matsim.core.events.parallelEventsHandler.ParallelEventsManagerImpl;
import org.matsim.core.mobsim.framework.events.MobsimInitializedEvent;
import org.matsim.core.mobsim.framework.listeners.FixedOrderSimulationListener;
import org.matsim.core.mobsim.framework.listeners.MobsimInitializedListener;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.multimodalsimengine.router.util.TravelTimeFactoryWrapper;
import org.matsim.core.population.PopulationFactoryImpl;
import org.matsim.core.population.routes.ModeRouteFactory;
import org.matsim.core.replanning.modules.AbstractMultithreadedModule;
import org.matsim.core.router.costcalculators.OnlyTimeDependentTravelCostCalculatorFactory;
import org.matsim.core.router.costcalculators.TravelDisutilityFactory;
import org.matsim.core.router.util.DijkstraFactory;
import org.matsim.core.router.util.LeastCostPathCalculatorFactory;
import org.matsim.core.router.util.PersonalizableTravelTime;
import org.matsim.core.scoring.OnlyTimeDependentScoringFunctionFactory;
import org.matsim.withinday.mobsim.ReplanningManager;
import org.matsim.withinday.mobsim.WithinDayQSimFactory;
import org.matsim.withinday.replanning.identifiers.ActivityEndIdentifierFactory;
import org.matsim.withinday.replanning.identifiers.InitialIdentifierImplFactory;
import org.matsim.withinday.replanning.identifiers.LeaveLinkIdentifierFactory;
import org.matsim.withinday.replanning.identifiers.interfaces.DuringActivityIdentifier;
import org.matsim.withinday.replanning.identifiers.interfaces.DuringLegIdentifier;
import org.matsim.withinday.replanning.identifiers.interfaces.InitialIdentifier;
import org.matsim.withinday.replanning.identifiers.tools.ActivityReplanningMap;
import org.matsim.withinday.replanning.identifiers.tools.LinkReplanningMap;
import org.matsim.withinday.replanning.identifiers.tools.SelectHandledAgentsByProbability;
import org.matsim.withinday.replanning.modules.ReplanningModule;
import org.matsim.withinday.replanning.replanners.CurrentLegReplannerFactory;
import org.matsim.withinday.replanning.replanners.InitialReplannerFactory;
import org.matsim.withinday.replanning.replanners.NextLegReplannerFactory;
import org.matsim.withinday.replanning.replanners.interfaces.WithinDayDuringActivityReplanner;
import org.matsim.withinday.replanning.replanners.interfaces.WithinDayDuringLegReplanner;
import org.matsim.withinday.replanning.replanners.interfaces.WithinDayInitialReplanner;
import org.matsim.withinday.trafficmonitoring.TravelTimeCollector;
import org.matsim.withinday.trafficmonitoring.TravelTimeCollectorFactory;

public class WithinDayControllerListener implements StartupListener, BeforeMobsimListener, AfterMobsimListener, MobsimInitializedListener {

    /**
	 * Define the probability that an Agent uses the replanning 
	 * strategy. It is possible to assign multiple strategies 
	 * to the agents.
	 */
    private double pInitialReplanning = 0.0;

    private double pDuringActivityReplanning = 1.0;

    private double pDuringLegReplanning = 1.0;

    private int lastIteration = 0;

    /**
	 * Define the objects that are needed for the replanning.
	 */
    private InitialIdentifier initialIdentifier;

    private DuringActivityIdentifier duringActivityIdentifier;

    private DuringLegIdentifier duringLegIdentifier;

    private WithinDayInitialReplanner initialReplanner;

    private WithinDayDuringActivityReplanner duringActivityReplanner;

    private WithinDayDuringLegReplanner duringLegReplanner;

    private ActivityReplanningMap activityReplanningMap;

    private LinkReplanningMap linkReplanningMap;

    private PersonalizableTravelTime travelTime;

    private TravelDisutilityFactory costFactory;

    private ReplanningManager replanningManager;

    private SelectHandledAgentsByProbability selector;

    private FixedOrderSimulationListener fosl;

    private static final Logger log = Logger.getLogger(WithinDayControllerListener.class);

    public WithinDayControllerListener() {
        log.info("Please call setControllerParameters(Controler controller) so configure the Controller.");
    }

    public void setControllerParameters(Controler controller) {
        controller.setScoringFunctionFactory(new OnlyTimeDependentScoringFunctionFactory());
        controller.setMobsimFactory(new WithinDayQSimFactory());
        controller.addControlerListener(this);
        fosl = new FixedOrderSimulationListener();
        controller.getQueueSimulationListener().add(fosl);
        fosl.addSimulationListener(this);
    }

    @Override
    public void notifyStartup(StartupEvent event) {
        event.getControler().getConfig().controler().setLastIteration(lastIteration);
        if (event.getControler().getEvents() instanceof ParallelEventsManagerImpl) {
            throw new RuntimeException("Using a ParallelEventsManagerImpl is not supported by the WithinDay Replanning Code. Please use an EventsMangerImpl or a SimStepParallelEventsManagerImpl.");
        }
        activityReplanningMap = new ActivityReplanningMap();
        event.getControler().getEvents().addHandler(activityReplanningMap);
        fosl.addSimulationListener(activityReplanningMap);
        linkReplanningMap = new LinkReplanningMap(event.getControler().getNetwork());
        event.getControler().getEvents().addHandler(linkReplanningMap);
        fosl.addSimulationListener(linkReplanningMap);
        travelTime = new TravelTimeCollectorFactory().createTravelTimeCollector(event.getControler().getScenario(), null);
        fosl.addSimulationListener((TravelTimeCollector) travelTime);
        event.getControler().getEvents().addHandler((TravelTimeCollector) travelTime);
        costFactory = new OnlyTimeDependentTravelCostCalculatorFactory();
    }

    @Override
    public void notifyBeforeMobsim(BeforeMobsimEvent event) {
        int numReplanningThreads = event.getControler().getConfig().global().getNumberOfThreads();
        replanningManager = new ReplanningManager(numReplanningThreads);
        fosl.addSimulationListener(replanningManager);
        selector = new SelectHandledAgentsByProbability();
        fosl.addSimulationListener(selector);
    }

    @Override
    public void notifyMobsimInitialized(MobsimInitializedEvent e) {
        log.info("Initialize Replanning Routers");
        QSim sim = (QSim) e.getQueueSimulation();
        LeastCostPathCalculatorFactory factory = new DijkstraFactory();
        ModeRouteFactory routeFactory = ((PopulationFactoryImpl) sim.getScenario().getPopulation().getFactory()).getModeRouteFactory();
        TravelTimeFactoryWrapper wrapper = new TravelTimeFactoryWrapper(travelTime);
        AbstractMultithreadedModule router = new ReplanningModule(sim.getScenario().getConfig(), sim.getScenario().getNetwork(), costFactory, wrapper, factory, routeFactory);
        this.initialIdentifier = new InitialIdentifierImplFactory(sim).createIdentifier();
        this.selector.addIdentifier(this.initialIdentifier, this.pInitialReplanning);
        this.initialReplanner = new InitialReplannerFactory(sim.getScenario(), router, 1.0).createReplanner();
        this.initialReplanner.addAgentsToReplanIdentifier(this.initialIdentifier);
        this.replanningManager.addIntialReplanner(this.initialReplanner);
        this.duringActivityIdentifier = new ActivityEndIdentifierFactory(activityReplanningMap).createIdentifier();
        this.selector.addIdentifier(this.duringActivityIdentifier, this.pDuringActivityReplanning);
        this.duringActivityReplanner = new NextLegReplannerFactory(sim.getScenario(), router, 1.0).createReplanner();
        this.duringActivityReplanner.addAgentsToReplanIdentifier(this.duringActivityIdentifier);
        this.replanningManager.addDuringActivityReplanner(this.duringActivityReplanner);
        this.duringLegIdentifier = new LeaveLinkIdentifierFactory(linkReplanningMap).createIdentifier();
        this.selector.addIdentifier(this.duringLegIdentifier, this.pDuringLegReplanning);
        this.duringLegReplanner = new CurrentLegReplannerFactory(sim.getScenario(), router, 1.0).createReplanner();
        this.duringLegReplanner.addAgentsToReplanIdentifier(this.duringLegIdentifier);
        this.replanningManager.addDuringLegReplanner(this.duringLegReplanner);
    }

    @Override
    public void notifyAfterMobsim(AfterMobsimEvent event) {
        fosl.removeSimulationListener(replanningManager);
        fosl.removeSimulationListener(selector);
        replanningManager = null;
        selector = null;
    }
}
