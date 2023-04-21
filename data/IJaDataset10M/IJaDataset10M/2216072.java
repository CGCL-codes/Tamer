package playground.scnadine.postprocessChoiceSets.carStageAlgorithms;

import java.util.ArrayList;
import java.util.HashMap;
import org.matsim.api.core.v01.Id;
import playground.scnadine.postprocessChoiceSets.CarChoiceSetRoute;
import playground.scnadine.postprocessChoiceSets.CarChoiceSetStage;

public class CarStageCalcLinkCompareRouteMatrix extends CarStageAlgorithm {

    public CarStageCalcLinkCompareRouteMatrix() {
        super();
    }

    @Override
    public void run(CarChoiceSetStage choiceSetStage) {
        for (CarChoiceSetRoute route : choiceSetStage.getChoiceSetRoutes().getChoiceSetRoutes()) {
            if (route.useRoute()) {
                HashMap<Id, ArrayList<Id>> linkCompareRouteMatrix = new HashMap<Id, ArrayList<Id>>();
                for (Id linkId : route.getLinkIds()) {
                    ArrayList<Id> routesSharingLink = new ArrayList<Id>();
                    for (CarChoiceSetRoute compareRoute : choiceSetStage.getChoiceSetRoutes().getChoiceSetRoutes()) {
                        if (compareRoute.useRoute() && compareRoute.getLinkIds().contains(linkId)) {
                            routesSharingLink.add(compareRoute.getId());
                        }
                    }
                    linkCompareRouteMatrix.put(linkId, new ArrayList<Id>(routesSharingLink));
                }
                route.setLinkCompareRouteMatrix(linkCompareRouteMatrix);
            }
        }
    }
}
