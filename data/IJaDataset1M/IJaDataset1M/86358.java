package playground.scnadine.postprocessChoiceSets.algorithms;

import java.util.Calendar;
import org.matsim.api.basic.v01.Id;
import org.matsim.core.api.network.Link;
import org.matsim.core.config.Config;
import org.matsim.core.trafficmonitoring.TravelTimeCalculator;
import playground.scnadine.postprocessChoiceSets.ChoiceSetRoute;
import playground.scnadine.postprocessChoiceSets.ChoiceSetStage;

public class ChoiceSetStageCalcPathSizeFactor3 extends ChoiceSetStageAlgorithm {

    private TravelTimeCalculator traveltime;

    private double rammingGamma;

    public ChoiceSetStageCalcPathSizeFactor3(TravelTimeCalculator traveltime, Config config, String CONFIG_MODULE) {
        super();
        this.traveltime = traveltime;
        String rammingGammaString = config.findParam(CONFIG_MODULE, "rammingGamma");
        if (rammingGammaString.equals("infinity")) {
            this.rammingGamma = Double.MAX_VALUE;
        } else {
            this.rammingGamma = Double.parseDouble(rammingGammaString);
        }
    }

    @Override
    public void run(ChoiceSetStage choiceSetStage) {
        for (ChoiceSetRoute route : choiceSetStage.getChoiceSetRoutes().getChoiceSetRoutes()) {
            if (route.useRoute()) {
                double pathSizeFactor = 0;
                for (Link currentLink : route.getLinks()) {
                    double lengthOfShortesRouteUsingCurrentLink = route.getTravelCost();
                    for (Id routeId : route.getLinkCompareRouteMatrix().get(currentLink.getId())) {
                        if (choiceSetStage.getChoiceSetRoutes().getChoiceSetRoute(routeId).getTravelCost() < lengthOfShortesRouteUsingCurrentLink) {
                            lengthOfShortesRouteUsingCurrentLink = choiceSetStage.getChoiceSetRoutes().getChoiceSetRoute(routeId).getTravelCost();
                        }
                    }
                    double sumOfShortestPathRatios = 0;
                    for (Id routeId : route.getLinkCompareRouteMatrix().get(currentLink.getId())) {
                        sumOfShortestPathRatios += Math.pow((lengthOfShortesRouteUsingCurrentLink / choiceSetStage.getChoiceSetRoutes().getChoiceSetRoute(routeId).getTravelCost()), this.rammingGamma);
                    }
                    if (choiceSetStage.getChoiceSetRoutes().getCalculationBase().equals("dist")) {
                        pathSizeFactor += (currentLink.getLength() / 1000) / route.getTravelCost() * (1 / sumOfShortestPathRatios);
                    } else if (choiceSetStage.getChoiceSetRoutes().getCalculationBase().equals("fftt")) {
                        pathSizeFactor += ((currentLink.getLength() / currentLink.getFreespeed(0)) / 60) / route.getTravelCost() * (1 / sumOfShortestPathRatios);
                    } else if (choiceSetStage.getChoiceSetRoutes().getCalculationBase().equals("dyntt") && (route.getTravelCost() > 0)) {
                        double timeOfDay = choiceSetStage.getStartTime().get(Calendar.HOUR_OF_DAY) * 3600 + choiceSetStage.getStartTime().get(Calendar.MINUTE) * 60 + choiceSetStage.getStartTime().get(Calendar.SECOND);
                        pathSizeFactor += (this.traveltime.getLinkTravelTime(currentLink, timeOfDay) / 60) / route.getTravelCost() * (1 / sumOfShortestPathRatios);
                    }
                    route.setPathSize3(pathSizeFactor);
                }
            }
        }
    }
}
