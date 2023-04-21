package org.matsim.roadpricing;

import java.util.List;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.population.ActivityImpl;
import org.matsim.core.population.LegImpl;
import org.matsim.core.population.routes.ModeRouteFactory;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.router.AStarLandmarks;
import org.matsim.core.router.PlansCalcRoute;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.LeastCostPathCalculator.Path;
import org.matsim.core.router.util.LeastCostPathCalculatorFactory;
import org.matsim.core.router.util.PersonalizableTravelDisutility;
import org.matsim.core.router.util.PersonalizableTravelTime;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.utils.misc.NetworkUtils;
import org.matsim.core.utils.misc.Time;

/**
 * A special router for complete plans that assigns the best routes to a plan
 * with respect to an area toll. Uses internally the {@link AStarLandmarks} routing algorithm.
 *
 * @author mrieser
 */
public class PlansCalcAreaTollRoute extends PlansCalcRoute {

    private final RoadPricingScheme scheme;

    private final TravelTime timeCalculator;

    private final LeastCostPathCalculator tollRouter;

    /**
	 * Constructs a new Area-Toll Router.
	 * @param network
	 * @param costCalculator This must be a normal implementation of TravelCost that does not take care of the area toll!
	 * @param timeCalculator
	 * @param factory
	 * @param scheme
	 */
    public PlansCalcAreaTollRoute(PlansCalcRouteConfigGroup configGroup, final Network network, final PersonalizableTravelDisutility costCalculator, final PersonalizableTravelTime timeCalculator, LeastCostPathCalculatorFactory factory, final ModeRouteFactory routeFactory, final RoadPricingScheme scheme) {
        super(configGroup, network, costCalculator, timeCalculator, factory, routeFactory);
        this.scheme = scheme;
        this.timeCalculator = timeCalculator;
        this.tollRouter = factory.createPathCalculator(network, new TravelDisutilityIncludingToll(costCalculator, scheme), timeCalculator);
    }

    @Override
    protected void handlePlan(Person person, final Plan plan) {
        boolean agentPaysToll = false;
        List<?> actslegs = plan.getPlanElements();
        ActivityImpl fromAct = (ActivityImpl) actslegs.get(0);
        final int TOLL_INDEX = 0;
        final int NOTOLL_INDEX = 1;
        final int nofLegs = (actslegs.size() - 1) / 2;
        NetworkRoute[][] routes = new NetworkRoute[2][nofLegs];
        double[][] depTimes = new double[2][nofLegs];
        boolean[] isCarLeg = new boolean[nofLegs];
        int routeIndex = 0;
        depTimes[TOLL_INDEX][routeIndex] = fromAct.getEndTime();
        depTimes[NOTOLL_INDEX][routeIndex] = fromAct.getEndTime();
        for (int i = 2, n = actslegs.size(); i < n; i += 2) {
            LegImpl leg = (LegImpl) actslegs.get(i - 1);
            ActivityImpl toAct = (ActivityImpl) actslegs.get(i);
            isCarLeg[routeIndex] = TransportMode.car.equals(leg.getMode());
            if (!isCarLeg[routeIndex]) {
                super.handleLeg(person, leg, fromAct, toAct, depTimes[NOTOLL_INDEX][routeIndex]);
            } else {
                Link fromLink = this.network.getLinks().get(fromAct.getLinkId());
                Link toLink = this.network.getLinks().get(toAct.getLinkId());
                Node startNode = fromLink.getToNode();
                Node endNode = toLink.getFromNode();
                NetworkRoute tollRoute = (NetworkRoute) (this.getRouteFactory().createRoute(TransportMode.car, fromLink.getId(), toLink.getId()));
                NetworkRoute noTollRoute = null;
                boolean tollRouteInsideTollArea = false;
                if (toLink != fromLink) {
                    Path path = this.getLeastCostPathCalculator().calcLeastCostPath(startNode, endNode, depTimes[TOLL_INDEX][routeIndex]);
                    if (path == null) {
                        throw new RuntimeException("No route found from node " + startNode.getId() + " to node " + endNode.getId() + ".");
                    }
                    tollRouteInsideTollArea = routeOverlapsTollLinks(fromLink, path, toLink, depTimes[TOLL_INDEX][routeIndex]);
                    tollRoute.setLinkIds(fromLink.getId(), NetworkUtils.getLinkIds(path.links), toLink.getId());
                    tollRoute.setTravelTime((int) path.travelTime);
                    tollRoute.setTravelCost(path.travelCost);
                } else {
                    tollRoute.setDistance(0.0);
                    tollRoute.setTravelTime(0.0);
                }
                if (tollRouteInsideTollArea && !agentPaysToll) {
                    Path path = this.tollRouter.calcLeastCostPath(startNode, endNode, depTimes[TOLL_INDEX][routeIndex]);
                    noTollRoute = (NetworkRoute) (this.getRouteFactory().createRoute(TransportMode.car, fromLink.getId(), toLink.getId()));
                    noTollRoute.setLinkIds(fromLink.getId(), NetworkUtils.getLinkIds(path.links), toLink.getId());
                    noTollRoute.setTravelTime((int) path.travelTime);
                    noTollRoute.setTravelCost(path.travelCost);
                    if (routeOverlapsTollLinks(fromLink, path, toLink, depTimes[TOLL_INDEX][routeIndex])) {
                        agentPaysToll = true;
                        noTollRoute = null;
                    }
                }
                routes[TOLL_INDEX][routeIndex] = tollRoute;
                if (noTollRoute == null) {
                    routes[NOTOLL_INDEX][routeIndex] = tollRoute;
                } else {
                    routes[NOTOLL_INDEX][routeIndex] = noTollRoute;
                }
                int nextIndex = routeIndex + 1;
                if (nextIndex < routes[0].length) {
                    depTimes[TOLL_INDEX][nextIndex] = depTimes[TOLL_INDEX][routeIndex] + routes[TOLL_INDEX][routeIndex].getTravelTime();
                    depTimes[NOTOLL_INDEX][nextIndex] = depTimes[NOTOLL_INDEX][routeIndex] + routes[NOTOLL_INDEX][routeIndex].getTravelTime();
                    double endTime = toAct.getEndTime();
                    double dur = toAct.getMaximumDuration();
                    if ((endTime != Time.UNDEFINED_TIME) && (dur != Time.UNDEFINED_TIME)) {
                        double min = Math.min(endTime, depTimes[TOLL_INDEX][nextIndex] + dur);
                        if (depTimes[TOLL_INDEX][nextIndex] < min) depTimes[TOLL_INDEX][nextIndex] = min;
                        min = Math.min(endTime, depTimes[NOTOLL_INDEX][nextIndex] + dur);
                        if (depTimes[NOTOLL_INDEX][nextIndex] < min) depTimes[NOTOLL_INDEX][nextIndex] = min;
                    } else if (endTime != Time.UNDEFINED_TIME) {
                        if (depTimes[TOLL_INDEX][nextIndex] < endTime) depTimes[TOLL_INDEX][nextIndex] = endTime;
                        if (depTimes[NOTOLL_INDEX][nextIndex] < endTime) depTimes[NOTOLL_INDEX][nextIndex] = endTime;
                    } else if (dur != Time.UNDEFINED_TIME) {
                        depTimes[TOLL_INDEX][nextIndex] += dur;
                        depTimes[NOTOLL_INDEX][nextIndex] += dur;
                    } else if ((i + 1) != actslegs.size()) {
                        throw new RuntimeException("act " + i + " has neither end-time nor duration.");
                    }
                }
            }
            routeIndex++;
            fromAct = toAct;
        }
        if (!agentPaysToll) {
            double cheapestCost = 0.0;
            double noTollCost = 0.0;
            for (int i = 0, n = routes[0].length; i < n; i++) {
                if (isCarLeg[i]) {
                    cheapestCost += Math.min(routes[TOLL_INDEX][i].getTravelCost(), routes[NOTOLL_INDEX][i].getTravelCost());
                    noTollCost += routes[NOTOLL_INDEX][i].getTravelCost();
                }
            }
            double tollAmount = this.scheme.getCostArray()[0].amount;
            agentPaysToll = (cheapestCost + tollAmount) < noTollCost;
        }
        if (agentPaysToll) {
            for (int i = 0; i < nofLegs; i++) {
                if (isCarLeg[i]) {
                    LegImpl leg = (LegImpl) actslegs.get(i * 2 + 1);
                    if (routes[TOLL_INDEX][i].getTravelCost() < routes[NOTOLL_INDEX][i].getTravelCost()) {
                        leg.setRoute(routes[TOLL_INDEX][i]);
                    } else {
                        leg.setRoute(routes[NOTOLL_INDEX][i]);
                    }
                }
            }
        } else {
            for (int i = 0; i < nofLegs; i++) {
                if (isCarLeg[i]) {
                    LegImpl leg = (LegImpl) actslegs.get(i * 2 + 1);
                    leg.setRoute(routes[NOTOLL_INDEX][i]);
                }
            }
        }
    }

    /**
	 * Tests, whether the route from <code>startLink</code> along <code>route</code>
	 * to <code>endLink<code>, started at <code>depTime</code>, will likely lead
	 * over tolled links.
	 *
	 * @param startLink The link on which the agent starts.
	 * @param route The route to test.
	 * @param endLink The link on which the agent arrives.
	 * @param depTime The time at which the agent departs.
	 * @return true if the route leads into an active tolling area and an agent
	 * taking this route will likely have to pay the toll, false otherwise.
	 */
    private boolean routeOverlapsTollLinks(final Link startLink, final Path route, final Link endLink, final double depTime) {
        double time = depTime;
        if (isLinkTolled(startLink, time)) {
            return true;
        }
        for (Link link : route.links) {
            if (isLinkTolled(link, time)) {
                return true;
            }
            time += this.timeCalculator.getLinkTravelTime(link, time);
        }
        return isLinkTolled(endLink, time);
    }

    private boolean isLinkTolled(final Link link, final double time) {
        return this.scheme.getLinkCostInfo(link.getId(), time, null) != null;
    }
}
