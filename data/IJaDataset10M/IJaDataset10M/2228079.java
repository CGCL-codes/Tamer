package playground.scnadine.postprocessChoiceSets.carStageAlgorithms;

import java.util.Calendar;
import java.util.Iterator;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.trafficmonitoring.TravelTimeCalculator;
import playground.scnadine.postprocessChoiceSets.CarChoiceSetRoute;
import playground.scnadine.postprocessChoiceSets.CarModelChoiceSetRoute;
import playground.scnadine.postprocessChoiceSets.CarModelChoiceSetStage;

public class CarModelStageCalcCommonalityFactor4 extends CarModelStageAlgorithm {

    private TravelTimeCalculator traveltime;

    public CarModelStageCalcCommonalityFactor4(TravelTimeCalculator traveltime) {
        super();
        this.traveltime = traveltime;
    }

    @Override
    public void run(CarModelChoiceSetStage choiceSetStage) {
        Iterator<CarChoiceSetRoute> it = choiceSetStage.getChoiceSetRoutes().getChoiceSetRoutes().iterator();
        while (it.hasNext()) {
            CarModelChoiceSetRoute route = (CarModelChoiceSetRoute) it.next();
            if (route.useRoute()) {
                double commonalityFactor = 1;
                if (choiceSetStage.getChoiceSetRoutes().getCalculationBase().equals("dist")) {
                    Iterator<CarChoiceSetRoute> itt = choiceSetStage.getChoiceSetRoutes().getChoiceSetRoutes().iterator();
                    while (itt.hasNext()) {
                        CarModelChoiceSetRoute compareRoute = (CarModelChoiceSetRoute) itt.next();
                        if (!compareRoute.getId().equals(route.getId())) {
                            double commonLinksLength = 0;
                            for (Link link : route.getCompareRouteLinkMatrix().get(compareRoute.getId())) {
                                commonLinksLength += link.getLength();
                            }
                            commonLinksLength = commonLinksLength / 1000;
                            commonalityFactor += (commonLinksLength / Math.sqrt(route.getTravelCost() * compareRoute.getTravelCost())) * ((route.getTravelCost() - commonLinksLength) / (compareRoute.getTravelCost() - commonLinksLength));
                        }
                    }
                    if (Double.isInfinite(commonalityFactor)) {
                        route.setCommonalityFactor4(-99999);
                    } else {
                        route.setCommonalityFactor4(commonalityFactor);
                    }
                } else if (choiceSetStage.getChoiceSetRoutes().getCalculationBase().equals("fftt")) {
                    Iterator<CarChoiceSetRoute> itt = choiceSetStage.getChoiceSetRoutes().getChoiceSetRoutes().iterator();
                    while (itt.hasNext()) {
                        CarModelChoiceSetRoute compareRoute = (CarModelChoiceSetRoute) itt.next();
                        if (!compareRoute.getId().equals(route.getId())) {
                            double commonLinksTravelTime = 0;
                            for (Link link : route.getCompareRouteLinkMatrix().get(compareRoute.getId())) {
                                commonLinksTravelTime += link.getLength() / link.getFreespeed();
                            }
                            commonLinksTravelTime = commonLinksTravelTime / 60;
                            commonalityFactor += (commonLinksTravelTime / Math.sqrt(route.getTravelCost() * compareRoute.getTravelCost())) * ((route.getTravelCost() - commonLinksTravelTime) / (compareRoute.getTravelCost() - commonLinksTravelTime));
                        }
                    }
                    if (Double.isInfinite(commonalityFactor)) {
                        route.setCommonalityFactor4(-99999);
                    } else {
                        route.setCommonalityFactor4(commonalityFactor);
                    }
                } else if (choiceSetStage.getChoiceSetRoutes().getCalculationBase().equals("dyntt")) {
                    if (route.getTravelCost() > 0) {
                        double timeOfDay = choiceSetStage.getStartTime().get(Calendar.HOUR_OF_DAY) * 3600 + choiceSetStage.getStartTime().get(Calendar.MINUTE) * 60 + choiceSetStage.getStartTime().get(Calendar.SECOND);
                        Iterator<CarChoiceSetRoute> itt = choiceSetStage.getChoiceSetRoutes().getChoiceSetRoutes().iterator();
                        while (itt.hasNext()) {
                            CarModelChoiceSetRoute compareRoute = (CarModelChoiceSetRoute) itt.next();
                            if ((compareRoute.getTravelCost() > 0) && !compareRoute.getId().equals(route.getId())) {
                                double commonLinksTravelTime = 0;
                                for (Link link : route.getCompareRouteLinkMatrix().get(compareRoute.getId())) {
                                    commonLinksTravelTime += this.traveltime.getLinkTravelTime(link, timeOfDay);
                                }
                                commonLinksTravelTime = commonLinksTravelTime / 60;
                                commonalityFactor += (commonLinksTravelTime / Math.sqrt(route.getTravelCost() * compareRoute.getTravelCost())) * ((route.getTravelCost() - commonLinksTravelTime) / (compareRoute.getTravelCost() - commonLinksTravelTime));
                            }
                        }
                        if (Double.isInfinite(commonalityFactor)) {
                            route.setCommonalityFactor4(-99999);
                        } else {
                            route.setCommonalityFactor4(commonalityFactor);
                        }
                    } else {
                        route.setCommonalityFactor4(choiceSetStage.getChoiceSetRoutes().getChoiceSetRoutes().size());
                    }
                }
            }
        }
    }
}
