package playground.dressler.ea_flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import org.matsim.core.api.network.Link;
import org.matsim.core.api.network.Node;
import org.matsim.core.api.population.NetworkRoute;
import org.matsim.core.network.NetworkLayer;
import org.matsim.core.population.routes.NodeNetworkRoute;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelCost;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.utils.misc.Time;
import playground.dressler.Intervall.src.Intervalls.EdgeIntervall;
import playground.dressler.Intervall.src.Intervalls.EdgeIntervalls;

/**
 * Implementation of the Moore-Bellman-Ford Algorithm for a static network! i =
 * 1 .. n for all e = (v,w) if l(w) > l(v) + c(e) then l(w) = l(v) + c(e), p(w) =
 * v.
 * 
 */
public class MooreBellmanFordMoreDynamic implements LeastCostPathCalculator {

    /**
	 * The network on which we find routes. We expect the network to change
	 * between runs!
	 */
    final NetworkLayer network;

    /**
	 * The cost calculator. Provides the cost for each link and time step.
	 */
    final TravelCost costFunction;

    /**
	 * The travel time calculator. Provides the travel time for each link and
	 * time step. This is ignored.
	 */
    final TravelTime timeFunction;

    private HashMap<Link, EdgeIntervalls> flow;

    private Distances Dists;

    private LinkedList<Link> pathToRoute;

    private int timeHorizon;

    private int gamma;

    final FakeTravelTimeCost length = new FakeTravelTimeCost();

    private HashMap<Node, Link> pred = new HashMap<Node, Link>();

    private HashMap<Node, Integer> waited = new HashMap<Node, Integer>();

    /**
	 * Default constructor.
	 * 
	 * @param network
	 *            The network on which to route.
	 * @param costFunction
	 *            Determines the link cost defining the cheapest route. Note,
	 *            comparisons are only made with accuraracy 0.001 due to
	 *            numerical problems otherwise.
	 * @param timeFunction
	 *            Determines the travel time on links. This is ignored!
	 */
    public MooreBellmanFordMoreDynamic(final NetworkLayer network, final TravelCost costFunction, final TravelTime timeFunction, HashMap<Link, EdgeIntervalls> flow) {
        this.network = network;
        this.costFunction = costFunction;
        this.timeFunction = timeFunction;
        this.flow = flow;
        Dists = new Distances(network);
        timeHorizon = Integer.MAX_VALUE;
        pathToRoute = new LinkedList<Link>();
        gamma = Integer.MAX_VALUE;
        for (Node node : network.getNodes().values()) {
            pred.put(node, null);
            waited.put(node, 0);
        }
    }

    /**
	 * Default constructor.
	 * 
	 * @param network
	 *            The network on which to route.
	 * @param costFunction
	 *            Determines the link cost defining the cheapest route. Note,
	 *            comparisons are only made with accuraracy 0.001 due to
	 *            numerical problems otherwise.
	 * @param timeFunction
	 *            Determines the travel time on links. This is ignored!
	 */
    public MooreBellmanFordMoreDynamic(final NetworkLayer network, final TravelCost costFunction, final TravelTime timeFunction, HashMap<Link, EdgeIntervalls> flow, int timeHorizon) {
        this.network = network;
        this.costFunction = costFunction;
        this.timeFunction = timeFunction;
        this.flow = flow;
        this.timeHorizon = timeHorizon;
        Dists = new Distances(network);
        pathToRoute = new LinkedList<Link>();
        gamma = Integer.MAX_VALUE;
        for (Node node : network.getNodes().values()) {
            pred.put(node, null);
            waited.put(node, 0);
        }
    }

    /**
	 * Calculates the cheapest route from Node 'fromNode' to Node 'toNode' at
	 * starting time 'startTime'.
	 * 
	 * @param fromNode
	 *            The Node at which the route should start.
	 * @param toNode
	 *            The Node at which the route should end.
	 * @param startTime
	 *            ignored
	 * @see org.matsim.core.router.util.LeastCostPathCalculator#calcLeastCostPath(org.matsim.core.api.network.Node,
	 *      org.matsim.core.api.network.Node, double)
	 */
    public Path calcLeastCostPath(final Node fromNode, final Node toNode, final double startTime) {
        boolean found = false;
        found = doCalculations(fromNode, toNode, startTime, flow);
        if (pathToRoute == null) {
            return null;
        }
        if (timeHorizon < Dists.getDistance(toNode)) {
            return null;
        }
        if (!found) {
            System.out.println("Warum?");
            return null;
        }
        ArrayList<Node> routeNodes = new ArrayList<Node>();
        Node tmpNode = fromNode;
        routeNodes.add(tmpNode);
        if (pathToRoute.peek().getFromNode().equals(fromNode) || pathToRoute.peek().getToNode().equals(fromNode)) {
            for (int i = 0; i < pathToRoute.size(); i++) {
                if (pathToRoute.get(i).getToNode().equals(tmpNode)) {
                    tmpNode = pathToRoute.get(i).getFromNode();
                } else if (pathToRoute.get(i).getFromNode().equals(tmpNode)) {
                    tmpNode = pathToRoute.get(i).getToNode();
                } else {
                    System.out.println("ERROR: couldn't construct route!");
                    return null;
                }
                routeNodes.add(tmpNode);
            }
        } else {
            for (int i = pathToRoute.size() - 1; i >= 0; i++) {
                if (pathToRoute.get(i).getToNode().equals(tmpNode)) {
                    tmpNode = pathToRoute.get(i).getFromNode();
                } else if (pathToRoute.get(i).getFromNode().equals(tmpNode)) {
                    tmpNode = pathToRoute.get(i).getToNode();
                } else {
                    System.out.println("ERROR: couldn't construct route!");
                    return null;
                }
                routeNodes.add(tmpNode);
            }
        }
        return new Path(routeNodes, null, Time.UNDEFINED_TIME, 0);
    }

    /**
	 * Calculates the cheapest route from Node 'fromNode' to Node 'toNode' at
	 * starting time 'startTime'.
	 * 
	 * @param fromNode
	 *            The Node at which the route should start.
	 * @param toNode
	 *            The Node at which the route should end.
	 * @param startTime
	 *            ignored
	 * @see org.matsim.core.router.util.LeastCostPathCalculator#calcLeastCostPath(org.matsim.core.api.network.Node,
	 *      org.matsim.core.api.network.Node, double)
	 */
    public NetworkRoute calcLeastCostPath(final Node fromNode, final Node toNode, final double startTime, HashMap<Link, EdgeIntervalls> flow) {
        this.flow = flow;
        boolean found = false;
        found = doCalculations(fromNode, toNode, startTime, flow);
        if (pathToRoute == null) {
            return null;
        }
        if (timeHorizon < Dists.getDistance(toNode)) {
            return null;
        }
        if (!found) {
            System.out.println("Warum?");
            return null;
        }
        ArrayList<Node> routeNodes = new ArrayList<Node>();
        Node tmpNode = fromNode;
        routeNodes.add(tmpNode);
        if (pathToRoute.peek().getFromNode().equals(fromNode) || pathToRoute.peek().getToNode().equals(fromNode)) {
            for (int i = 0; i < pathToRoute.size(); i++) {
                if (pathToRoute.get(i).getToNode().equals(tmpNode)) {
                    tmpNode = pathToRoute.get(i).getFromNode();
                } else if (pathToRoute.get(i).getFromNode().equals(tmpNode)) {
                    tmpNode = pathToRoute.get(i).getToNode();
                } else {
                    System.out.println("ERROR: couldn't construct route!");
                    return null;
                }
                routeNodes.add(tmpNode);
            }
        } else {
            for (int i = pathToRoute.size() - 1; i >= 0; i++) {
                if (pathToRoute.get(i).getToNode().equals(tmpNode)) {
                    tmpNode = pathToRoute.get(i).getFromNode();
                } else if (pathToRoute.get(i).getFromNode().equals(tmpNode)) {
                    tmpNode = pathToRoute.get(i).getToNode();
                } else {
                    System.out.println("ERROR: couldn't construct route!");
                    return null;
                }
                routeNodes.add(tmpNode);
            }
        }
        NetworkRoute route = new NodeNetworkRoute();
        route.setNodes(routeNodes);
        return route;
    }

    /**
	 * Calculates the cheapest route from Node 'fromNode' to Node 'toNode' at
	 * starting time 'startTime'. This returns an array of links which is more
	 * useful than the Route object
	 * 
	 * @param fromNode
	 *            The Node at which the route should start.
	 * @param toNode
	 *            The Node at which the route should end.
	 * @param startTime
	 *            ignored
	 * @see org.matsim.core.router.util.LeastCostPathCalculator#calcLeastCostPath(org.matsim.core.api.network.Node,
	 *      org.matsim.core.api.network.Node, double)
	 */
    public ArrayList<Link> calcLeastCostLinkRoute(final Node fromNode, final Node toNode, final double startTime) {
        boolean found = false;
        found = doCalculations(fromNode, toNode, startTime, flow);
        if (pathToRoute == null) {
            return null;
        }
        if (timeHorizon < Dists.getDistance(toNode)) {
            return null;
        }
        if (!found) {
            System.out.println("Warum?");
            return null;
        }
        ArrayList<Link> routeLinks = new ArrayList<Link>();
        if (pathToRoute.peek().getFromNode().equals(fromNode) || pathToRoute.peek().getToNode().equals(fromNode)) {
            for (int i = 0; i < pathToRoute.size(); i++) {
                routeLinks.add(pathToRoute.get(i));
            }
        } else {
            for (int i = pathToRoute.size() - 1; i >= 0; i--) {
                routeLinks.add(pathToRoute.get(i));
            }
        }
        return routeLinks;
    }

    /**
	 * Calculates the cheapest route from Node 'fromNode' to Node 'toNode' at
	 * starting time 'startTime'. This returns an array of links which is more
	 * useful than the Route object
	 * 
	 * @param fromNode
	 *            The Node at which the route should start.
	 * @param toNode
	 *            The Node at which the route should end.
	 * @param startTime
	 *            ignored
	 * @see org.matsim.core.router.util.LeastCostPathCalculator#calcLeastCostPath(org.matsim.core.api.network.Node,
	 *      org.matsim.core.api.network.Node, double)
	 */
    public ArrayList<Link> calcLeastCostLinkRoute(final Node fromNode, final Node toNode, final double startTime, HashMap<Link, EdgeIntervalls> flow) {
        this.flow = flow;
        boolean found = false;
        found = doCalculations(fromNode, toNode, startTime, flow);
        if (pathToRoute == null) {
            return null;
        }
        if (timeHorizon < Dists.getDistance(toNode)) {
            return null;
        }
        if (!found) {
            System.out.println("Warum?");
            return null;
        }
        ArrayList<Link> routeLinks = new ArrayList<Link>();
        if (pathToRoute.peek().getFromNode().equals(fromNode) || pathToRoute.peek().getToNode().equals(fromNode)) {
            for (int i = 0; i < pathToRoute.size(); i++) {
                routeLinks.add(pathToRoute.get(i));
            }
        } else {
            for (int i = pathToRoute.size() - 1; i >= 0; i--) {
                routeLinks.add(pathToRoute.get(i));
            }
        }
        return routeLinks;
    }

    /**
	 * Calculates the cheapest route from Node 'fromNode' to Node 'toNode' at
	 * starting time 'startTime'. This returns an array of links which is more
	 * useful than the Route object
	 * 
	 * @param fromNode
	 *            The Node at which the route should start.
	 * @param toNode
	 *            The Node at which the route should end.
	 * @param startTime
	 *            ignored
	 * @see org.matsim.core.router.util.LeastCostPathCalculator#calcLeastCostPath(org.matsim.core.api.network.Node,
	 *      org.matsim.core.api.network.Node, double)
	 */
    public HashMap<Link, EdgeIntervalls> calcLeastCostFlow(final Node fromNode, final Node toNode, final double startTime) {
        boolean found = false;
        found = doCalculations(fromNode, toNode, startTime, flow);
        if (pathToRoute == null) {
            return null;
        }
        if (pathToRoute == null) {
            return null;
        }
        if (!found) {
            System.out.println("Warum?");
            return null;
        }
        gamma = calculateGamma(fromNode, toNode, pathToRoute);
        if (gamma == 0) {
            return null;
        }
        Node tmpNode = fromNode;
        Link tmpLink;
        Node node = tmpNode;
        int dist;
        HashMap<Link, EdgeIntervalls> newFlow = flow;
        while (!pathToRoute.isEmpty()) {
            tmpLink = pathToRoute.poll();
            dist = Dists.getDistance(tmpLink.getFromNode());
            if (tmpNode.equals(tmpLink.getToNode())) {
                newFlow.get(tmpLink).augmentreverse(dist, gamma);
                node = tmpLink.getFromNode();
            } else if (tmpNode.equals(tmpLink.getFromNode())) {
                newFlow.get(tmpLink).augment(dist, gamma, (int) (tmpLink.getCapacity(1.)));
                node = tmpLink.getToNode();
            } else {
                System.out.println("Error with LinkedList path!");
            }
            tmpNode = node;
        }
        return newFlow;
    }

    /**
	 * Calculates the cheapest route from Node 'fromNode' to Node 'toNode' at
	 * starting time 'startTime'. This returns an array of links which is more
	 * useful than the Route object
	 * 
	 * @param fromNode
	 *            The Node at which the route should start.
	 * @param toNode
	 *            The Node at which the route should end.
	 * @param startTime
	 *            ignored
	 * @see org.matsim.core.router.util.LeastCostPathCalculator#calcLeastCostPath(org.matsim.core.api.network.Node,
	 *      org.matsim.core.api.network.Node, double)
	 */
    public HashMap<Link, EdgeIntervalls> calcLeastCostFlow(final Node fromNode, final Node toNode, final double startTime, HashMap<Link, EdgeIntervalls> flow) {
        this.flow = flow;
        boolean found = false;
        found = doCalculations(fromNode, toNode, startTime, flow);
        if (pathToRoute == null) {
            return null;
        }
        if (pathToRoute == null) {
            return null;
        }
        if (!found) {
            System.out.println("Warum?");
            return null;
        }
        gamma = calculateGamma(fromNode, toNode, pathToRoute);
        System.out.println("Gamma: " + gamma);
        if (gamma == 0) {
            return null;
        }
        Node tmpNode = fromNode;
        Link tmpLink;
        Node node = tmpNode;
        int dist;
        HashMap<Link, EdgeIntervalls> newFlow = flow;
        int tmp = 0;
        while (!pathToRoute.isEmpty()) {
            tmpLink = pathToRoute.poll();
            dist = Dists.getDistance(tmpLink.getFromNode());
            if (tmpNode.equals(tmpLink.getToNode())) {
                tmp += waited.get(tmpLink.getFromNode());
                newFlow.get(tmpLink).augmentreverse(tmp, gamma);
                tmp -= length.getLinkTravelCost(tmpLink, 0.);
                node = tmpLink.getFromNode();
            } else if (tmpNode.equals(tmpLink.getFromNode())) {
                tmp += waited.get(tmpLink.getToNode());
                newFlow.get(tmpLink).augment(tmp, gamma, (int) (tmpLink.getCapacity(1.)));
                tmp += length.getLinkTravelCost(tmpLink, 0.);
                node = tmpLink.getToNode();
            } else {
                System.out.println("Error with LinkedList path!");
            }
            tmpNode = node;
        }
        return newFlow;
    }

    private boolean doCalculations(final Node fromNode, final Node toNode, final double startTime, final HashMap<Link, EdgeIntervalls> flow) {
        init(fromNode);
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(fromNode);
        Node v, w;
        int dist;
        while (!queue.isEmpty()) {
            v = queue.poll();
            for (Link link : v.getOutLinks().values()) {
                w = link.getToNode();
                if (!Dists.getDistance(v).equals(Integer.MAX_VALUE)) {
                    dist = (int) length.getLinkTravelCost(link, 1.) + Dists.getDistance(v);
                } else {
                    dist = Integer.MAX_VALUE;
                }
                if (Dists.getDistance(w) > dist) {
                    if (!Dists.getDistance(v).equals(Integer.MAX_VALUE)) {
                        if (flow.get(link).getFlowAt(Dists.getDistance(v)) < (int) (link.getCapacity(1.))) {
                            visitNode(v, w, dist);
                            pred.put(w, link);
                            waited.put(w, 0);
                            if (!queue.contains(w)) {
                                queue.add(w);
                            }
                        } else {
                            EdgeIntervall tmpInt = flow.get(link).getIntervallAt(Dists.getDistance(v));
                            while (!(tmpInt.getFlow() < link.getCapacity(1.)) && !(tmpInt.equals(flow.get(link).getLast()))) {
                                tmpInt = flow.get(link).getNext(tmpInt);
                            }
                            if (tmpInt.equals(flow.get(link).getLast())) {
                                if (!(tmpInt == null) && !(tmpInt.equals(flow.get(link).getIntervallAt(Dists.getDistance(v))))) {
                                    int t = tmpInt.getLowBound() + (int) (length.getLinkTravelCost(link, 1));
                                    if (Dists.getDistance(w) > t) {
                                        visitNode(v, w, t);
                                        pred.put(w, link);
                                        waited.put(w, t - dist);
                                        if (!queue.contains(w)) {
                                            queue.add(w);
                                        }
                                    }
                                }
                            } else if (tmpInt.getFlow() < link.getCapacity(1.)) {
                                int t = tmpInt.getLowBound() + (int) (length.getLinkTravelCost(link, 1));
                                if (Dists.getDistance(w) > t) {
                                    visitNode(v, w, t);
                                    pred.put(w, link);
                                    waited.put(w, t - dist);
                                    if (!queue.contains(w)) {
                                        queue.add(w);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (Link link : v.getInLinks().values()) {
                w = link.getFromNode();
                if (!Dists.getDistance(v).equals(Integer.MAX_VALUE)) {
                    dist = Dists.getDistance(v) - (int) length.getLinkTravelCost(link, 1.);
                } else {
                    dist = Integer.MAX_VALUE;
                }
                if (dist >= 0) {
                    if (Dists.getDistance(w) > dist) {
                        if (!Dists.getDistance(w).equals(Integer.MAX_VALUE)) {
                            if (flow.get(link).getFlowAt(dist) > 0) {
                                visitNode(v, w, dist);
                                pred.put(w, link);
                                waited.put(v, 0);
                                if (!queue.contains(w)) {
                                    queue.add(w);
                                }
                            } else {
                                int t = dist;
                                EdgeIntervall tmpInt = flow.get(link).getIntervallAt(dist);
                                while (tmpInt.getFlow() == 0 && !tmpInt.equals(flow.get(link).getLast())) {
                                    tmpInt = flow.get(link).getNext(tmpInt);
                                }
                                if (tmpInt.equals(flow.get(link).getLast())) {
                                    if (tmpInt.getFlow() > 0) {
                                        t = tmpInt.getLowBound() + (int) (length.getLinkTravelCost(link, 1));
                                        if (Dists.getDistance(w) > t) {
                                            visitNode(v, w, t);
                                            pred.put(w, link);
                                            waited.put(v, t - dist);
                                            if (!queue.contains(w)) {
                                                queue.add(w);
                                            }
                                        }
                                    }
                                } else if (tmpInt.getFlow() > 0) {
                                    t = tmpInt.getLowBound() + (int) (length.getLinkTravelCost(link, 1));
                                    if (flow.get(link).getFlowAt(t) > 0) {
                                        if (Dists.getDistance(w) > t) {
                                            visitNode(v, w, t);
                                            pred.put(w, link);
                                            waited.put(v, t - dist);
                                            if (!queue.contains(w)) {
                                                queue.add(w);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (Dists.getDistance(toNode) == Integer.MAX_VALUE) {
            pathToRoute = null;
            System.out.println("No path found!");
            return false;
        } else if (Dists.getDistance(toNode) > timeHorizon) {
            pathToRoute = null;
            System.out.println("Out of time horizon!");
            System.out.println(timeHorizon + " < " + Dists.getDistance(toNode));
            return false;
        } else {
            pathToRoute = findPath(fromNode, toNode);
            return true;
        }
    }

    /**
	 * Initializes the nodes of the network
	 * 
	 * @param fromNode
	 *            The starting node
	 */
    void init(final Node fromNode) {
        Dists = new Distances(network);
        for (Node node : network.getNodes().values()) {
            if (node.equals(fromNode)) {
                Dists.setDistance(node, 0);
            } else {
                Dists.setDistance(node, Integer.MAX_VALUE);
            }
        }
        if (!pathToRoute.isEmpty()) {
            pathToRoute.clear();
        }
        gamma = Integer.MAX_VALUE;
        for (Node node : network.getNodes().values()) {
            pred.put(node, null);
            waited.put(node, 0);
        }
    }

    void visitNode(final Node fromNode, final Node toNode, int arrivesAt) {
        int tmpTime = Dists.getMinTime(toNode);
        if (arrivesAt < tmpTime) {
            Dists.setDistance(toNode, arrivesAt);
        }
    }

    boolean visitNode(final Node fromNode, final Node toNode) {
        int tmpTime = Dists.getMinTime(toNode);
        boolean found = false;
        Link thisLink = network.getLink("1");
        boolean forward, backward, flowPossibleForward, flowPossibleBackward;
        for (Link link : network.getLinks().values()) {
            forward = (link.getFromNode().equals(fromNode)) && (link.getToNode().equals(toNode));
            backward = (link.getFromNode().equals(toNode)) && (link.getToNode().equals(fromNode));
            flowPossibleForward = flow.get(link).getFlowAt(tmpTime) < (int) (link.getCapacity(1.));
            flowPossibleBackward = flow.get(link).getFlowAt(tmpTime) > 0;
            if ((forward && flowPossibleForward) || (backward && flowPossibleBackward)) {
                found = true;
                thisLink = link;
                break;
            }
        }
        if (found == true) {
            int arrivesAt = Dists.getDistance(fromNode) + (int) (Math.ceil(thisLink.getEuklideanDistance()));
            if (arrivesAt < tmpTime) {
                Dists.setDistance(toNode, arrivesAt);
            }
        }
        return found;
    }

    LinkedList<Link> findPath(final Node fromNode, final Node toNode) {
        LinkedList<Link> path = new LinkedList<Link>();
        Node tmpNode = toNode;
        Node node = tmpNode;
        Link tmpLink;
        while (!tmpNode.equals(fromNode)) {
            tmpLink = pred.get(tmpNode);
            if (tmpLink.getFromNode().equals(tmpNode)) {
                node = tmpLink.getToNode();
                path.addFirst(tmpLink);
            } else if (tmpLink.getToNode().equals(tmpNode)) {
                node = tmpLink.getFromNode();
                path.addFirst(tmpLink);
            } else {
                System.out.println("AAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHH!!!!!!!!!!!!!");
            }
            if (!node.equals(tmpNode)) {
                tmpNode = node;
            } else {
                System.out.println("TOT");
            }
        }
        printWaitPath(path, fromNode);
        return path;
    }

    void printAll() {
        Dists.printAll();
    }

    void printPath(LinkedList<Link> path) {
        System.out.print("Path: ");
        for (Link link : path) {
            System.out.print("(" + link.getFromNode().getId() + "," + link.getToNode().getId() + ") ");
        }
        System.out.println();
    }

    void printWaitPath(LinkedList<Link> path, Node fromNode) {
        System.out.print("Path: ");
        Node tmpNode = fromNode;
        Link tmpLink;
        Node node = fromNode;
        for (int i = 0; i < path.size(); i++) {
            tmpLink = path.get(i);
            if (tmpLink.getFromNode().equals(tmpNode)) {
                node = tmpLink.getToNode();
                for (int j = 0; j < waited.get(node); j++) {
                    System.out.print("(" + tmpNode.getId() + "," + tmpNode.getId() + ") ");
                }
            } else if (tmpLink.getToNode().equals(tmpNode)) {
                node = tmpLink.getFromNode();
                for (int j = 0; j < waited.get(node); j++) {
                    System.out.print("(" + tmpNode.getId() + "," + tmpNode.getId() + ") ");
                }
            } else {
                System.out.println("Error while trying to print out!");
                break;
            }
            System.out.print("(" + tmpLink.getFromNode().getId() + "," + tmpLink.getToNode().getId() + ") ");
            tmpNode = node;
        }
        System.out.println();
    }

    int calculateGamma(Node fromNode, Node toNode, LinkedList<Link> pathToRoute) {
        gamma = Integer.MAX_VALUE;
        LinkedList<Link> path = pathToRoute;
        Node tmpNode = fromNode;
        Link tmpLink;
        Node node = fromNode;
        int dist;
        int tmp = 0;
        for (int i = 0; i < path.size(); i++) {
            tmpLink = path.get(i);
            if (!(tmpLink == null)) {
                dist = Dists.getDistance(tmpLink.getFromNode());
                if (tmpNode.equals(tmpLink.getToNode())) {
                    node = tmpLink.getFromNode();
                    tmp += waited.get(node);
                    if (gamma > flow.get(tmpLink).getFlowAt(tmp)) {
                        gamma = flow.get(tmpLink).getFlowAt(tmp);
                    }
                    tmp -= length.getLinkTravelCost(tmpLink, 0.);
                } else if (tmpNode.equals(tmpLink.getFromNode())) {
                    node = tmpLink.getToNode();
                    tmp += waited.get(node);
                    if (gamma > ((int) (tmpLink.getCapacity(1.)) - flow.get(tmpLink).getFlowAt(tmp))) {
                        gamma = (int) (tmpLink.getCapacity(1.)) - flow.get(tmpLink).getFlowAt(tmp);
                    }
                    tmp += length.getLinkTravelCost(tmpLink, 0.);
                }
            } else {
                System.out.println("Error with HashMap preds! (calculateGamma)");
            }
            tmpNode = node;
        }
        return gamma;
    }
}
