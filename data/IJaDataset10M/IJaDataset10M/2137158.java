package playground.mmoyo.ptRouterAdapted.precalculation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.population.routes.ModeRouteFactory;
import org.matsim.core.router.util.LeastCostPathCalculatorFactory;
import org.matsim.core.router.util.PersonalizableTravelDisutility;
import org.matsim.core.router.util.PersonalizableTravelTime;
import org.matsim.core.utils.collections.Tuple;
import org.matsim.pt.config.TransitConfigGroup;
import org.matsim.pt.router.PlansCalcTransitRoute;
import org.matsim.pt.router.TransitRouterConfig;
import org.matsim.pt.router.TransitRouterImpl;
import org.matsim.pt.routes.ExperimentalTransitRoute;
import org.matsim.pt.transitSchedule.api.TransitSchedule;
import playground.mmoyo.ptRouterAdapted.AdaptedTransitRouter;
import playground.mmoyo.ptRouterAdapted.MyTransitRouterConfig;

/**
 *  Finds pt connection according to a priority : time or less transfers
 * @author manuel
 */
public class PrecalPlansCalcTransitRoute extends PlansCalcTransitRoute {

    private static final Logger log = Logger.getLogger(PrecalPlansCalcTransitRoute.class);

    private final AdaptedTransitRouter adaptedTransitRouter;

    final String PT = "pt";

    final String nullString = "";

    final String COMMA = ",";

    final String NO_PT_FOUND = "no pt connection found";

    int kBestRoute = 0;

    public PrecalPlansCalcTransitRoute(final PlansCalcRouteConfigGroup config, final Network network, final PersonalizableTravelDisutility costCalculator, final PersonalizableTravelTime timeCalculator, final LeastCostPathCalculatorFactory factory, final ModeRouteFactory routeFactory, final TransitSchedule schedule, final TransitConfigGroup transitConfig, final MyTransitRouterConfig myTransitRouterConfig) {
        super(config, network, costCalculator, timeCalculator, factory, routeFactory, transitConfig, new TransitRouterImpl(myTransitRouterConfig, schedule), schedule);
        this.adaptedTransitRouter = new AdaptedTransitRouter(myTransitRouterConfig, schedule);
        throw new RuntimeException("this uses the standard TransitRouterConfig in parts of the code, and your " + "own myTransitRouterConfig in other parts of the code, leading to potential inconsistencies. " + "Also makes it impossible to refactor.  Could you please explain your design decisions " + "with comments in the code?  And please talk to me if you need this code here.  Thanks, kai");
    }

    @Override
    protected double handlePtPlan(final Leg leg, final Activity fromAct, final Activity toAct, final double depTime, final Person person) {
        double travelTime = 0.0;
        double vdTime = depTime;
        Map<Id, StaticConnection> connectionMap = new HashMap<Id, StaticConnection>();
        List<Leg> selectedConnection = null;
        for (vdTime = depTime + 600; vdTime >= depTime - 300; vdTime -= 180) {
            List<Leg> legs = this.adaptedTransitRouter.calcRoute(fromAct.getCoord(), toAct.getCoord(), vdTime, person);
            travelTime = 0.0;
            double travelDistance = 0.0;
            int ptLegsNum = 0;
            if (legs != null) {
                StringBuilder routeId = new StringBuilder(this.nullString);
                for (Leg leg2 : legs) {
                    travelTime += leg2.getTravelTime();
                    if (leg2.getMode().equals(this.PT)) {
                        routeId.append(((ExperimentalTransitRoute) leg2.getRoute()).getRouteDescription());
                        routeId.append(this.COMMA);
                        ptLegsNum++;
                    }
                }
                if (ptLegsNum > 0) {
                    StaticConnection staticConnection = new StaticConnection(new IdImpl(routeId.toString()), legs, travelTime, travelDistance, ptLegsNum);
                    if (!connectionMap.containsKey(staticConnection.getId())) {
                        connectionMap.put(staticConnection.getId(), staticConnection);
                    }
                } else {
                    selectedConnection = legs;
                }
            }
        }
        if (connectionMap.size() > 0) {
            StaticConnection[] connectionArray = connectionMap.values().toArray(new StaticConnection[connectionMap.size()]);
            Arrays.sort(connectionArray);
            Random randomGenerator = new Random();
            if (this.kBestRoute < connectionArray.length) {
                if (connectionArray.length > 1) {
                    this.kBestRoute = 0;
                }
                selectedConnection = connectionArray[this.kBestRoute].getLegs();
            }
            randomGenerator = null;
            connectionArray = null;
        } else {
            log.warn(this.NO_PT_FOUND);
        }
        super.getLegReplacements().add(new Tuple<Leg, List<Leg>>(leg, selectedConnection));
        connectionMap = null;
        return travelTime;
    }
}
