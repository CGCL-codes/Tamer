package concreteTests.irisWellFounded;

import org.wsml.reasoner.api.LPReasoner;
import abstractTests.lp.AbstractFunctionSymbols2Example;

public class FunctionSymbols2ExampleTest extends AbstractFunctionSymbols2Example {

    public LPReasoner getLPReasoner() {
        return Reasoner.get();
    }
}
