package algo.graph.dynamicflow.eat;

import de.tu_berlin.math.coga.common.algorithm.Algorithm;
import algo.graph.shortestpath.Dijkstra;
import ds.graph.Node;

/**
 *
 * @author Martin Groß
 */
public class LongestShortestPathTimeHorizonEstimator extends Algorithm<EarliestArrivalFlowProblem, TimeHorizonBounds> {

    @Override
    protected TimeHorizonBounds runAlgorithm(EarliestArrivalFlowProblem problem) {
        int longest = 0;
        for (Node source : problem.getSources()) {
            Dijkstra dijkstra = new Dijkstra(problem.getNetwork(), problem.getTransitTimes(), source);
            dijkstra.run();
            if (dijkstra.getDistance(problem.getSink()) > longest) {
                longest = dijkstra.getDistance(problem.getSink());
            }
        }
        int supply = 0;
        for (Node source : problem.getSources()) supply += problem.getSupplies().get(source);
        return new TimeHorizonBounds(longest + 1, longest + supply + 1);
    }
}
