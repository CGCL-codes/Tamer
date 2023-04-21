package uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.impl.sentencebody.relationassertion;

import java.util.List;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.relationassertion.ParsedEquivalenceRelationAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedEquivalenceRelationAssertion;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestPartMatcher;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.TestPartMatcher;

/**
 * Provides test methods for {@link IParsedEquivalenceRelationAssertion}s
 * 
 * @author rdenaux
 * 
 */
public class TestParsedEquivalenceRelationAssertion extends ParsedEquivalenceRelationAssertion implements ITestParsedPart<IParsedEquivalenceRelationAssertion> {

    private static final long serialVersionUID = -2095398657974777002L;

    public TestParsedEquivalenceRelationAssertion(IParsedRelation modRel, IParsedRelation equivRel, List<IParsedKeyphrase> keyphrases) {
        super(modRel, equivRel, keyphrases);
    }

    private ITestPartMatcher matcher = new TestPartMatcher();

    public void setMatcher(ITestPartMatcher aMatcher) {
        assert aMatcher != null;
        matcher = aMatcher;
    }

    public boolean hasSameContents(IParsedEquivalenceRelationAssertion parsedPart) {
        return matcher.hasSameContent(getModifiedRelation(), parsedPart.getModifiedRelation(), IParsedRelation.class) && matcher.hasSameContent(getEquivalentRelation(), parsedPart.getEquivalentRelation(), IParsedRelation.class);
    }

    public boolean testsClass(Class<?> clazz) {
        return IParsedEquivalenceRelationAssertion.class.equals(clazz);
    }
}
