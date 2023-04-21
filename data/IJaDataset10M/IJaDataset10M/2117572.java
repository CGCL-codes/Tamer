package concreteTests.mins;

import org.wsml.reasoner.api.LPReasoner;
import abstractTests.lp.AbstractFlight1Example1;

public class Flight1Example1Test extends AbstractFlight1Example1 {

    public LPReasoner getLPReasoner() {
        return Reasoner.get();
    }
}
