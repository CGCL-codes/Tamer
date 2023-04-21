package uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.impl.sentencebody.relationassertion;

import java.util.List;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.relationassertion.ParsedLiteralRelationAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedLiteralModifier;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedLiteralRelationAssertion;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestPartMatcher;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.TestPartMatcher;

/**
 * Provides methods for testing {@link IParsedLiteralRelationAssertion}
 * 
 * @author rdenaux
 * 
 */
public class TestParsedLiteralRelationAssertion extends ParsedLiteralRelationAssertion implements ITestParsedPart<IParsedLiteralRelationAssertion> {

    private static final long serialVersionUID = -3683351905431045951L;

    private ITestPartMatcher matcher = new TestPartMatcher();

    public void setMatcher(ITestPartMatcher aMatcher) {
        assert aMatcher != null;
        matcher = aMatcher;
    }

    public TestParsedLiteralRelationAssertion(IParsedRelation modRel, IParsedLiteralModifier litMod, List<IParsedKeyphrase> keyphrases) {
        super(modRel, litMod, keyphrases);
    }

    public boolean hasSameContents(IParsedLiteralRelationAssertion parsedPart) {
        return matcher.hasSameContent(getModifiedRelation(), parsedPart.getModifiedRelation(), IParsedRelation.class) && matcher.hasSameContent(getLiteralModifier(), parsedPart.getLiteralModifier(), IParsedLiteralModifier.class);
    }

    public boolean testsClass(Class<?> clazz) {
        return IParsedLiteralRelationAssertion.class.equals(clazz);
    }
}
