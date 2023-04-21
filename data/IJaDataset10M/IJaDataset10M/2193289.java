package uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.conceptassertion;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedPositiveAssertion;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.base.BaseTestPartFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.impl.sentencebody.conceptassertion.TestParsedPositiveAssertion;

/**
 * Converts {@link IParsedPositiveAssertion} into
 * {@link TestParsedPositiveAssertion}
 * 
 * @author rdenaux
 * 
 */
public class TestParsedPositiveAssertionFactory extends BaseTestPartFactory<IParsedPositiveAssertion> {

    public TestParsedPositiveAssertionFactory() {
        super(IParsedPositiveAssertion.class);
    }

    @Override
    protected ITestParsedPart<IParsedPositiveAssertion> doConvertToTestPart(IParsedPositiveAssertion part) {
        return new TestParsedPositiveAssertion(part.getSubject(), part.getCompoundRelationship());
    }
}
