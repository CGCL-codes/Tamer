package playground.dsantani.choiceSetGeneration.algorithms;

import java.util.Iterator;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.config.Config;
import org.matsim.core.network.NetworkLayer;
import org.matsim.core.network.NodeImpl;
import org.matsim.core.router.Dijkstra;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.LeastCostPathCalculator.Path;
import playground.dsantani.choiceSetGeneration.ChoiceSetRoute;
import playground.dsantani.choiceSetGeneration.ChoiceSetRoutes;

public class ChoiceSetRouteCheckSimilarityConstraint extends ChoiceSetRouteAlgorithm {

    private double overlapFactorShortRoutes;

    private double overlapFactorLongRoutes;

    private int longRouteBoundary;

    private NetworkLayer network;

    private LeastCostPathCalculator dijkstra;

    private int timeOfDay;

    private NodeImpl endNode;

    public ChoiceSetRouteCheckSimilarityConstraint(NetworkLayer network, Config config, String CONFIG_MODULE, NodeImpl endNode) {
        super();
        this.network = network;
        this.endNode = endNode;
        this.overlapFactorShortRoutes = Double.parseDouble(config.findParam(CONFIG_MODULE, "overlapFactorShortRoutes"));
        this.overlapFactorLongRoutes = Double.parseDouble(config.findParam(CONFIG_MODULE, "overlapFactorLongRoutes"));
        this.longRouteBoundary = Integer.parseInt(config.findParam(CONFIG_MODULE, "longRouteBoundary"));
        TravelDistanceCostCalculator timeCostCalc = new TravelDistanceCostCalculator();
        this.dijkstra = new Dijkstra(this.network, timeCostCalc, timeCostCalc);
        this.timeOfDay = Integer.parseInt(config.findParam(CONFIG_MODULE, "defaultTime"));
    }

    @Override
    public void run(ChoiceSetRoutes choiceSetRoutes) {
        int i = 0;
        while (i < choiceSetRoutes.getChoiceSetRoutes().size() - 1) {
            ChoiceSetRoute currentRoute = choiceSetRoutes.getChoiceSetRoutes().get(i);
            Iterator<ChoiceSetRoute> it_compare = choiceSetRoutes.getChoiceSetRoutes().listIterator(i);
            while (it_compare.hasNext()) {
                ChoiceSetRoute compareRoute = it_compare.next();
                double overlap = 0;
                for (Link currentRouteLink : currentRoute.getLinks()) {
                    for (Link compareRouteLink : compareRoute.getLinks()) {
                        if (currentRouteLink == compareRouteLink) {
                            overlap += currentRouteLink.getLength();
                        }
                    }
                }
                if (currentRoute.getLinks().size() <= this.longRouteBoundary) {
                    if ((currentRoute.getDistance() + getPathDistance(this.dijkstra.calcLeastCostPath(currentRoute.getLastNode(), this.endNode, this.timeOfDay))) > (compareRoute.getDistance() + getPathDistance(this.dijkstra.calcLeastCostPath(compareRoute.getLastNode(), this.endNode, this.timeOfDay))) && overlap / (compareRoute.getDistance() + getPathDistance(this.dijkstra.calcLeastCostPath(currentRoute.getLastNode(), this.endNode, this.timeOfDay))) > this.overlapFactorShortRoutes) {
                        choiceSetRoutes.getChoiceSetRoutes().remove(i);
                        break;
                    } else if ((currentRoute.getDistance() + getPathDistance(this.dijkstra.calcLeastCostPath(currentRoute.getLastNode(), this.endNode, this.timeOfDay))) < (compareRoute.getDistance() + getPathDistance(this.dijkstra.calcLeastCostPath(compareRoute.getLastNode(), this.endNode, this.timeOfDay))) && overlap / (currentRoute.getDistance() + getPathDistance(this.dijkstra.calcLeastCostPath(currentRoute.getLastNode(), this.endNode, this.timeOfDay))) > this.overlapFactorShortRoutes) it_compare.remove();
                } else {
                    if ((currentRoute.getDistance() + getPathDistance(this.dijkstra.calcLeastCostPath(currentRoute.getLastNode(), this.endNode, this.timeOfDay))) > (compareRoute.getDistance() + getPathDistance(this.dijkstra.calcLeastCostPath(compareRoute.getLastNode(), this.endNode, this.timeOfDay))) && overlap / (compareRoute.getDistance() + getPathDistance(this.dijkstra.calcLeastCostPath(currentRoute.getLastNode(), this.endNode, this.timeOfDay))) > this.overlapFactorLongRoutes) {
                        choiceSetRoutes.getChoiceSetRoutes().remove(i);
                        break;
                    } else if ((currentRoute.getDistance() + getPathDistance(this.dijkstra.calcLeastCostPath(currentRoute.getLastNode(), this.endNode, this.timeOfDay))) < (compareRoute.getDistance() + getPathDistance(this.dijkstra.calcLeastCostPath(compareRoute.getLastNode(), this.endNode, this.timeOfDay))) && overlap / (currentRoute.getDistance() + getPathDistance(this.dijkstra.calcLeastCostPath(currentRoute.getLastNode(), this.endNode, this.timeOfDay))) > this.overlapFactorLongRoutes) it_compare.remove();
                }
            }
            i++;
        }
    }

    private double getPathDistance(final Path path) {
        double dist = 0;
        for (Link link : path.links) {
            dist += link.getLength();
        }
        return dist;
    }
}
