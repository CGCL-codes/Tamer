package concreteTests.irisStratified;

import org.wsml.reasoner.api.LPReasoner;
import abstractTests.lp.AbstractDataTypes5CaseSensitivity;

public class DataTypes5CaseSensitivityTest extends AbstractDataTypes5CaseSensitivity {

    public LPReasoner getLPReasoner() {
        return Reasoner.get();
    }
}
