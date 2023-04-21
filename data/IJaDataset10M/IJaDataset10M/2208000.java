package uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.object;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedRelationshipPhrase;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.base.BaseTestPartFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.impl.object.TestParsedRelationshipPhrase;

/**
 * Converts a {@link IParsedRelationshipPhrase} into a
 * {@link TestParsedRelationshipPhrase}.
 * 
 * @author rdenaux
 * 
 */
public class TestParsedRelationshpPhraseFactory extends BaseTestPartFactory<IParsedRelationshipPhrase> {

    public TestParsedRelationshpPhraseFactory() {
        super(IParsedRelationshipPhrase.class);
    }

    @Override
    protected ITestParsedPart<IParsedRelationshipPhrase> doConvertToTestPart(IParsedRelationshipPhrase part) {
        return new TestParsedRelationshipPhrase(part.getRelation(), part.getCompoundObject());
    }
}
