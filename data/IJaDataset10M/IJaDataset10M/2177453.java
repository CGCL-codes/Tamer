package playground.scnadine.postprocessChoiceSets.carStageAlgorithms;

import playground.scnadine.postprocessChoiceSets.CarChoiceSetRoute;
import playground.scnadine.postprocessChoiceSets.CarChoiceSetStage;

public class CarStageClearLinkCompareRouteMatrix extends CarStageAlgorithm {

    public CarStageClearLinkCompareRouteMatrix() {
        super();
    }

    @Override
    public void run(CarChoiceSetStage choiceSetStage) {
        for (CarChoiceSetRoute route : choiceSetStage.getChoiceSetRoutes().getChoiceSetRoutes()) {
            route.getLinkCompareRouteMatrix().clear();
        }
    }
}
