package concreteTests.irisWellFounded;

import org.wsml.reasoner.api.LPReasoner;
import abstractTests.lp.AbstractViolation7MaxCardinality;

public class Violation7MaxCardinalityTest extends AbstractViolation7MaxCardinality {

    public LPReasoner getLPReasoner() {
        return Reasoner.get();
    }
}
