package playground.scnadine.postprocessChoiceSets.carStagesAlgorithms;

import java.util.Calendar;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.trafficmonitoring.TravelTimeCalculator;
import playground.scnadine.postprocessChoiceSets.CarChoiceSetRoute;
import playground.scnadine.postprocessChoiceSets.CarChoiceSetStage;
import playground.scnadine.postprocessChoiceSets.CarChoiceSetStages;

public class CarStagesCalcTravelCosts extends CarStagesAlgorithm {

    private TravelTimeCalculator traveltime;

    private final Network network;

    public CarStagesCalcTravelCosts(TravelTimeCalculator traveltime, Network network) {
        super();
        this.traveltime = traveltime;
        this.network = network;
    }

    @Override
    public void run(CarChoiceSetStages choiceSetStages) {
        for (CarChoiceSetStage stage : choiceSetStages.getChoiceSetStages()) {
            if (stage.getChoiceSetRoutes().getCalculationBase().equals("fftt")) {
                for (CarChoiceSetRoute route : stage.getChoiceSetRoutes().getChoiceSetRoutes()) {
                    if (route.useRoute()) {
                        double freeFlowTravelTime = 0;
                        for (Id linkId : route.getLinkIds()) {
                            Link link = this.network.getLinks().get(linkId);
                            freeFlowTravelTime += (link.getLength() / link.getFreespeed());
                        }
                        route.setTravelCost(freeFlowTravelTime / 60);
                    }
                }
            } else if (stage.getChoiceSetRoutes().getCalculationBase().equals("dyntt")) {
                double timeOfDay = stage.getStartTime().get(Calendar.HOUR_OF_DAY) * 3600 + stage.getStartTime().get(Calendar.MINUTE) * 60 + stage.getStartTime().get(Calendar.SECOND);
                for (CarChoiceSetRoute route : stage.getChoiceSetRoutes().getChoiceSetRoutes()) {
                    if (route.useRoute()) {
                        double dynamicTravelTime = 0;
                        for (Id currentLinkId : route.getLinkIds()) {
                            Link link = this.network.getLinks().get(currentLinkId);
                            dynamicTravelTime += this.traveltime.getLinkTravelTime(link, timeOfDay);
                        }
                        route.setTravelCost(dynamicTravelTime / 60);
                    }
                }
            } else {
                for (CarChoiceSetRoute route : stage.getChoiceSetRoutes().getChoiceSetRoutes()) {
                    if (route.useRoute()) {
                        route.setTravelCost(route.getDistance() / 1000);
                    }
                }
            }
        }
    }
}
