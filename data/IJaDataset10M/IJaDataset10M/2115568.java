package uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.factory.sentencebody.instanceassertion;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.instanceassertion.IParsedSameInstances;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.base.BaseTestPartFactory;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.impl.sentencebody.instanceassertion.TestParsedSameInstances;

/**
 * Creates a {@link TestParsedSameInstances} based on
 * {@link IParsedSameInstances}.
 * 
 * @author rdenaux
 * 
 */
public class TestParsedSameInstancesFactory extends BaseTestPartFactory<IParsedSameInstances> {

    public TestParsedSameInstancesFactory() {
        super(IParsedSameInstances.class);
    }

    @Override
    protected ITestParsedPart<IParsedSameInstances> doConvertToTestPart(IParsedSameInstances part) {
        return new TestParsedSameInstances(part.getFirst(), part.getSecond(), part.getKeyphrases());
    }
}
