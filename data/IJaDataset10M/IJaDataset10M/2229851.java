package playground.dsantani.postprocessChoiceSets.carStageAlgorithms;

import java.util.Calendar;
import java.util.Iterator;
import org.matsim.api.basic.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.config.Config;
import org.matsim.core.trafficmonitoring.TravelTimeCalculator;
import playground.dsantani.postprocessChoiceSets.CarChoiceSetRoute;
import playground.dsantani.postprocessChoiceSets.CarModelChoiceSetRoute;
import playground.dsantani.postprocessChoiceSets.CarModelChoiceSetStage;

public class CarModelStageCalcPathSizeFactor3 extends CarModelStageAlgorithm {

    private TravelTimeCalculator traveltime;

    private double rammingGamma;

    public CarModelStageCalcPathSizeFactor3(TravelTimeCalculator traveltime, Config config, String CONFIG_MODULE) {
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
    public void run(CarModelChoiceSetStage choiceSetStage) {
        Iterator<CarChoiceSetRoute> it = choiceSetStage.getChoiceSetRoutes().getChoiceSetRoutes().iterator();
        while (it.hasNext()) {
            CarModelChoiceSetRoute route = (CarModelChoiceSetRoute) it.next();
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
