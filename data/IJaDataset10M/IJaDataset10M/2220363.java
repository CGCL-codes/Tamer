package playground.anhorni.locationchoice.cs.depr.filters;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import playground.anhorni.locationchoice.cs.helper.ChoiceSet;

public class SampleDrawerFixedSizeTravelCosts extends SampleDrawer {

    int maxSizeOfChoiceSets = 1;

    boolean crowFly = true;

    private static final Logger log = Logger.getLogger(SampleDrawerFixedSizeTravelCosts.class);

    public SampleDrawerFixedSizeTravelCosts(int maxSizeOfChoiceSets, boolean crowFly) {
        this.maxSizeOfChoiceSets = maxSizeOfChoiceSets;
        this.crowFly = crowFly;
    }

    public void drawSample(List<ChoiceSet> choiceSets) {
        log.info("Sample choice sets to max. size : " + this.maxSizeOfChoiceSets);
        Iterator<ChoiceSet> choiceSets_it = choiceSets.iterator();
        while (choiceSets_it.hasNext()) {
            ChoiceSet choiceSet = choiceSets_it.next();
            while (choiceSet.choiceSetSize() > this.maxSizeOfChoiceSets) {
            }
        }
    }
}
