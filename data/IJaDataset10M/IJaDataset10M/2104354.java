package concreteTests.kaon2;

import org.wsml.reasoner.api.LPReasoner;
import abstractTests.lp.AbstractViolation8UserConstraint;

public class Violation8UserConstraintTest extends AbstractViolation8UserConstraint {

    public LPReasoner getLPReasoner() {
        return Reasoner.get();
    }
}
