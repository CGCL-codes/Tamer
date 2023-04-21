package org.matsim.locationchoice.bestresponse;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.router.Dijkstra;
import org.matsim.core.router.util.DijkstraNodeData;
import org.matsim.core.router.util.PreProcessDijkstra;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.utils.collections.PseudoRemovePriorityQueue;

public class BackwardDijkstraMultipleDestinations extends Dijkstra {

    private static final Logger log = Logger.getLogger(BackwardDijkstraMultipleDestinations.class);

    final TravelDisutility costFunction;

    final TravelTime timeFunction;

    private int iterationID = Integer.MIN_VALUE + 1;

    private Node deadEndEntryNode;

    private final boolean pruneDeadEnds;

    private double estimatedStartTime = 0.0;

    public BackwardDijkstraMultipleDestinations(final Network network, final TravelDisutility costFunction, final TravelTime timeFunction) {
        this(network, costFunction, timeFunction, null);
    }

    public BackwardDijkstraMultipleDestinations(final Network network, final TravelDisutility costFunction, final TravelTime timeFunction, final PreProcessDijkstra preProcessData) {
        super(network, costFunction, timeFunction, preProcessData);
        this.network = network;
        this.costFunction = costFunction;
        this.timeFunction = timeFunction;
        if (preProcessData != null) {
            if (preProcessData.containsData() == false) {
                this.pruneDeadEnds = false;
                log.warn("The preprocessing data provided to router class Dijkstra contains no data! Please execute its run(...) method first!");
                log.warn("Running without dead-end pruning.");
            } else {
                this.pruneDeadEnds = true;
            }
        } else {
            this.pruneDeadEnds = false;
        }
    }

    @Override
    public Path calcLeastCostPath(final Node fromNode, final Node toNode, final double startTime) {
        double arrivalTime = 0;
        augmentIterationId();
        if (this.pruneDeadEnds == true) {
            this.deadEndEntryNode = getPreProcessData(toNode).getDeadEndEntryNode();
        }
        ArrayList<Node> nodes = new ArrayList<Node>();
        ArrayList<Link> links = new ArrayList<Link>();
        nodes.add(0, toNode);
        Link tmpLink = getData(toNode).getPrevLink();
        if (tmpLink != null) {
            while (tmpLink.getToNode() != fromNode) {
                links.add(0, tmpLink);
                nodes.add(0, tmpLink.getToNode());
                tmpLink = getData(tmpLink.getToNode()).getPrevLink();
            }
            links.add(0, tmpLink);
            nodes.add(0, tmpLink.getFromNode());
        }
        DijkstraNodeData toNodeData = getData(toNode);
        arrivalTime = toNodeData.getTime();
        double travelTime = -1.0 * (arrivalTime - this.estimatedStartTime);
        Path path = new Path(nodes, links, travelTime, toNodeData.getCost());
        return path;
    }

    public void calcLeastCostTree(Node fromNode, double startTime) {
        augmentIterationId();
        PseudoRemovePriorityQueue<Node> pendingNodes = new PseudoRemovePriorityQueue<Node>(500);
        DijkstraNodeData data = getData(fromNode);
        visitNode(fromNode, data, pendingNodes, this.estimatedStartTime, 0, null);
        while (true) {
            Node outNode = pendingNodes.poll();
            if (outNode == null) return;
            relaxNode(outNode, null, pendingNodes);
        }
    }

    @Override
    protected void relaxNode(final Node outNode, final Node toNode, final PseudoRemovePriorityQueue<Node> pendingNodes) {
        DijkstraNodeData outData = getData(outNode);
        double currTime = outData.getTime();
        double currCost = outData.getCost();
        if (this.pruneDeadEnds) {
            PreProcessDijkstra.DeadEndData ddOutData = getPreProcessData(outNode);
            for (Link l : outNode.getInLinks().values()) {
                if (canPassLink(l)) {
                    Node n = l.getFromNode();
                    PreProcessDijkstra.DeadEndData ddData = getPreProcessData(n);
                    if ((ddData.getDeadEndEntryNode() == null) || (ddOutData.getDeadEndEntryNode() != null) || ((this.deadEndEntryNode != null) && (this.deadEndEntryNode.getId() == ddData.getDeadEndEntryNode().getId()))) {
                        addToPendingNodes(l, n, pendingNodes, currTime, currCost, toNode);
                    }
                }
            }
        } else {
            for (Link l : outNode.getInLinks().values()) {
                if (canPassLink(l)) {
                    addToPendingNodes(l, l.getFromNode(), pendingNodes, currTime, currCost, toNode);
                }
            }
        }
    }

    @Override
    protected boolean addToPendingNodes(final Link l, final Node n, final PseudoRemovePriorityQueue<Node> pendingNodes, double currTime, final double currCost, final Node toNode) {
        double travelTime = 0.0;
        double travelCost = 0.0;
        if (currTime < 0) {
            double timeMod = 24.0 * 3600.0 - Math.abs(currTime % (24.0 * 3600.0));
            travelTime = -1.0 * this.timeFunction.getLinkTravelTime(l, timeMod);
            travelCost = this.costFunction.getLinkTravelDisutility(l, timeMod);
        } else {
            travelTime = -1.0 * this.timeFunction.getLinkTravelTime(l, currTime);
            travelCost = this.costFunction.getLinkTravelDisutility(l, currTime);
        }
        DijkstraNodeData data = getData(n);
        double nCost = data.getCost();
        if (!data.isVisited(this.iterationID)) {
            visitNode(n, data, pendingNodes, currTime + travelTime, currCost + travelCost, l);
            return true;
        }
        double totalCost = currCost + travelCost;
        if (totalCost < nCost) {
            pendingNodes.remove(n);
            data.visit(l, totalCost, currTime + travelTime, this.iterationID);
            pendingNodes.add(n, getPriority(data));
            return true;
        }
        return false;
    }

    @Override
    protected void visitNode(final Node n, final DijkstraNodeData data, final PseudoRemovePriorityQueue<Node> pendingNodes, final double time, final double cost, final Link outLink) {
        data.visit(outLink, cost, time, this.iterationID);
        pendingNodes.add(n, getPriority(data));
    }

    public double getEstimatedStartTime() {
        return estimatedStartTime;
    }

    public void setEstimatedStartTime(double estimatedStartTime) {
        this.estimatedStartTime = estimatedStartTime;
    }
}
