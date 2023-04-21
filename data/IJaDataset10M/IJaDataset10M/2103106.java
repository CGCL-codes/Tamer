package playground.scnadine.postprocessChoiceSets.algorithms;

import playground.scnadine.postprocessChoiceSets.ChoiceSetRoute;
import playground.scnadine.postprocessChoiceSets.ChoiceSetStage;

public class ChoiceSetStageClearLinkCompareRouteMatrix extends ChoiceSetStageAlgorithm {

    public ChoiceSetStageClearLinkCompareRouteMatrix() {
        super();
    }

    @Override
    public void run(ChoiceSetStage choiceSetStage) {
        for (ChoiceSetRoute route : choiceSetStage.getChoiceSetRoutes().getChoiceSetRoutes()) {
            route.getLinkCompareRouteMatrix().clear();
        }
    }
}
