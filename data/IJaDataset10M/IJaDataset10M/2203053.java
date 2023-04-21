package uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.factory.sentencebody;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedSentenceBody;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.base.BaseTestPartFactory;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.impl.sentencebody.TestParsedSentenceBody;

/**
 * Creates a {@link TestParsedSentenceBody} based on a
 * {@link IParsedSentenceBody}.
 * 
 * @author rdenaux
 * 
 */
public class TestParsedSentenceBodyFactory extends BaseTestPartFactory<IParsedSentenceBody> {

    public TestParsedSentenceBodyFactory() {
        super(IParsedSentenceBody.class);
    }

    @Override
    protected ITestParsedPart<IParsedSentenceBody> doConvertToTestPart(IParsedSentenceBody part) {
        return new TestParsedSentenceBody(part);
    }
}
