package concreteTests.irisWellFounded;

import org.wsml.reasoner.api.LPReasoner;
import abstractTests.lp.AbstractAttribute6InheritingCardinalityConstraints;

public class Attribute6InheritingCardinalityConstraintsTest extends AbstractAttribute6InheritingCardinalityConstraints {

    public LPReasoner getLPReasoner() {
        return Reasoner.get();
    }
}
