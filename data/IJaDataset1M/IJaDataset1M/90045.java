package concreteTests.irisStratified;

import org.wsml.reasoner.api.LPReasoner;
import abstractTests.lp.AbstractConsistent1Example1;

public class Consistent1Example1Test extends AbstractConsistent1Example1 {

    public LPReasoner getLPReasoner() {
        return Reasoner.get();
    }
}
