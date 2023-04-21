package concreteTests.irisWellFounded;

import org.wsml.reasoner.api.LPReasoner;
import abstractTests.lp.AbstractDataTypes8DateComparison;

public class DataTypes8DateComparisonTest extends AbstractDataTypes8DateComparison {

    public LPReasoner getLPReasoner() {
        return Reasoner.get();
    }
}
