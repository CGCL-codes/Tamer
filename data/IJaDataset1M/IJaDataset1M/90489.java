package uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.imports;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.imports.IParsedUseMultipleOntologiesSentence;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.base.BaseTestPartFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.impl.sentencebody.imports.TestUseMultipleOntologiesSentence;

/**
 * Creates a test version for {@link IParsedUseMultipleOntologiesSentence}
 * 
 * @author rdenaux
 * 
 */
public class TestParsedUseMultipleOntologiesSentenceFactory extends BaseTestPartFactory<IParsedUseMultipleOntologiesSentence> {

    public TestParsedUseMultipleOntologiesSentenceFactory() {
        super(IParsedUseMultipleOntologiesSentence.class);
    }

    @Override
    protected ITestParsedPart<IParsedUseMultipleOntologiesSentence> doConvertToTestPart(IParsedUseMultipleOntologiesSentence part) {
        return new TestUseMultipleOntologiesSentence(part.getUrlReferences(), part.getKeyphrases());
    }
}
