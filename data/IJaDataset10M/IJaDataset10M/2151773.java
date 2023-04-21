package tutorial.unsupported.example50VeryExperimentalWithinDayReplanning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.mobsim.framework.MobsimAgent;
import org.matsim.core.mobsim.framework.MobsimDriverAgent;
import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimBeforeSimStepListener;
import org.matsim.core.mobsim.framework.listeners.MobsimListener;
import org.matsim.core.mobsim.qsim.agents.ExperimentalBasicWithindayAgent;
import org.matsim.core.mobsim.qsim.interfaces.MobsimVehicle;
import org.matsim.core.mobsim.qsim.interfaces.Netsim;
import org.matsim.core.mobsim.qsim.qnetsimengine.NetsimLink;
import org.matsim.core.population.PopulationFactoryImpl;
import org.matsim.core.router.PlansCalcRoute;
import org.matsim.core.router.util.DijkstraFactory;
import org.matsim.core.router.util.PersonalizableTravelDisutility;
import org.matsim.core.router.util.PersonalizableTravelTime;
import org.matsim.pt.transitSchedule.api.TransitStopFacility;

/**As stated in the package info, this class is an <i>untested</i> design suggestion.  Comments are welcome.  kai, dec'10
 *
 * @author nagel
 */
public class MyWithinDayMobsimListener implements MobsimListener, MobsimBeforeSimStepListener {

    private static final Logger log = Logger.getLogger(MyWithinDayMobsimListener.class);

    private PersonalizableTravelDisutility travCostCalc;

    private PersonalizableTravelTime travTimeCalc;

    private PlansCalcRoute routeAlgo;

    private Scenario scenario;

    MyWithinDayMobsimListener(PersonalizableTravelDisutility travelCostCalculator, PersonalizableTravelTime travelTimeCalculator) {
        this.travCostCalc = travelCostCalculator;
        this.travTimeCalc = travelTimeCalculator;
    }

    @Override
    public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent event) {
        Netsim mobsim = (Netsim) event.getQueueSimulation();
        this.scenario = mobsim.getScenario();
        Collection<MobsimAgent> agentsToReplan = getAgentsToReplan(mobsim);
        this.routeAlgo = new PlansCalcRoute(mobsim.getScenario().getConfig().plansCalcRoute(), mobsim.getScenario().getNetwork(), this.travCostCalc, this.travTimeCalc, new DijkstraFactory(), ((PopulationFactoryImpl) mobsim.getScenario().getPopulation().getFactory()).getModeRouteFactory());
        for (MobsimAgent pa : agentsToReplan) {
            doReplanning(pa, mobsim);
        }
    }

    private List<MobsimAgent> getAgentsToReplan(Netsim mobsim) {
        List<MobsimAgent> set = new ArrayList<MobsimAgent>();
        for (NetsimLink link : mobsim.getNetsimNetwork().getNetsimLinks().values()) {
            for (MobsimVehicle vehicle : link.getAllNonParkedVehicles()) {
                MobsimDriverAgent agent = vehicle.getDriver();
                System.out.println(agent.getId());
                if (true) {
                    System.out.println("found agent");
                    set.add(agent);
                }
            }
        }
        return set;
    }

    private boolean doReplanning(MobsimAgent personAgent, Netsim mobsim) {
        if (!(personAgent instanceof ExperimentalBasicWithindayAgent)) {
            log.error("agent of wrong type; returning ... ");
        }
        ExperimentalBasicWithindayAgent withindayAgent = (ExperimentalBasicWithindayAgent) personAgent;
        Plan plan = withindayAgent.getSelectedPlan();
        if (plan == null) {
            log.info(" we don't have a selected plan; returning ... ");
            return false;
        }
        if (withindayAgent.getCurrentPlanElement() instanceof Activity) {
            double oldTime = -1.;
            double newTime = -1.;
            mobsim.rescheduleActivityEnd(withindayAgent, oldTime, newTime);
        } else if (withindayAgent.getCurrentPlanElement() instanceof Leg) {
            Leg leg = (Leg) withindayAgent.getCurrentPlanElement();
            if (TransportMode.car.equals(leg.getMode())) {
            } else if (TransportMode.pt.equals(leg.getMode())) {
                TransitStopFacility stop = null;
            }
        }
        withindayAgent.resetCaches();
        return true;
    }
}
