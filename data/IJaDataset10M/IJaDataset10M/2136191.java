package playground.dsantani.postprocessChoiceSets.carStageAlgorithms;

import java.util.Calendar;
import org.matsim.api.basic.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.trafficmonitoring.TravelTimeCalculator;
import playground.dsantani.postprocessChoiceSets.CarChoiceSetRoute;
import playground.dsantani.postprocessChoiceSets.CarChoiceSetStage;

public class CarStageCalcPathSizeFactor2 extends CarStageAlgorithm {

    private TravelTimeCalculator traveltime;

    public CarStageCalcPathSizeFactor2(TravelTimeCalculator traveltime) {
        super();
        this.traveltime = traveltime;
    }

    @Override
    public void run(CarChoiceSetStage choiceSetStage) {
        for (CarChoiceSetRoute route : choiceSetStage.getChoiceSetRoutes().getChoiceSetRoutes()) {
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
                        sumOfShortestPathRatios += lengthOfShortesRouteUsingCurrentLink / choiceSetStage.getChoiceSetRoutes().getChoiceSetRoute(routeId).getTravelCost();
                    }
                    if (choiceSetStage.getChoiceSetRoutes().getCalculationBase().equals("dist")) {
                        pathSizeFactor += (currentLink.getLength() / 1000) / route.getTravelCost() * (1 / sumOfShortestPathRatios);
                    } else if (choiceSetStage.getChoiceSetRoutes().getCalculationBase().equals("fftt")) {
                        pathSizeFactor += ((currentLink.getLength() / currentLink.getFreespeed(0)) / 60) / route.getTravelCost() * (1 / sumOfShortestPathRatios);
                    } else if (choiceSetStage.getChoiceSetRoutes().getCalculationBase().equals("dyntt") && (route.getTravelCost() > 0)) {
                        double timeOfDay = choiceSetStage.getStartTime().get(Calendar.HOUR_OF_DAY) * 3600 + choiceSetStage.getStartTime().get(Calendar.MINUTE) * 60 + choiceSetStage.getStartTime().get(Calendar.SECOND);
                        pathSizeFactor += (this.traveltime.getLinkTravelTime(currentLink, timeOfDay) / 60) / route.getTravelCost() * (1 / sumOfShortestPathRatios);
                    }
                    route.setPathSize2(pathSizeFactor);
                }
            }
        }
    }
}
