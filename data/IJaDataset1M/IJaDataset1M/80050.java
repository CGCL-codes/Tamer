package playground.scnadine.drawODsForChoiceSetGeneration.algorithms;

import playground.scnadine.drawODsForChoiceSetGeneration.ChoiceSetStage;
import playground.scnadine.drawODsForChoiceSetGeneration.ChoiceSetStages;

public class ChoiceSetStageCalcMainRoadType extends ChoiceSetStageAlgorithm {

    public ChoiceSetStageCalcMainRoadType() {
        super();
    }

    @Override
    public void run(ChoiceSetStages choiceSetStages) {
        for (ChoiceSetStage stage : choiceSetStages.getChoiceSetStages()) {
            if (stage.getPercentageOnMW() >= 0.5) {
                stage.setMainRoadType("mw");
            } else if (stage.getPercentageOnEU() >= 0.5) {
                stage.setMainRoadType("eu");
            } else if (stage.getPercentageOnUM() >= 0.5) {
                stage.setMainRoadType("um");
            } else if (stage.getPercentageOnLR() >= 0.5) {
                stage.setMainRoadType("lr");
            } else {
                stage.setMainRoadType("other");
            }
        }
    }
}
