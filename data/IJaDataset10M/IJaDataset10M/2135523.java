package uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.impl.sentencebody.conceptassertion;

import java.util.List;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.complexobject.IParsedComplexRelationshipPhrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.conceptassertion.ParsedClosureAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedClosureAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.subject.IParsedSubject;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestPartMatcher;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.TestPartMatcher;

/**
 * Adds test methods for {@link ParsedClosureAssertion}.
 * 
 * @author rdenaux.
 * 
 */
public class TestParsedClosureAssertion extends ParsedClosureAssertion implements ITestParsedPart<IParsedClosureAssertion> {

    private static final long serialVersionUID = 4997293275167837008L;

    private ITestPartMatcher matcher = new TestPartMatcher();

    public void setMatcher(ITestPartMatcher aMatcher) {
        assert aMatcher != null;
        matcher = aMatcher;
    }

    public TestParsedClosureAssertion(IParsedSubject subject, IParsedComplexRelationshipPhrase acrp, List<IParsedKeyphrase> keyphrases) {
        super(subject, acrp, keyphrases);
    }

    public boolean hasSameContents(IParsedClosureAssertion parsedPart) {
        return matcher.getAsTestPart(getSubject(), IParsedSubject.class).hasSameContents(parsedPart.getSubject()) && matcher.getAsTestPart(getComplexRelationshipPhrase(), IParsedComplexRelationshipPhrase.class).hasSameContents(parsedPart.getComplexRelationshipPhrase());
    }

    public boolean testsClass(Class<?> clazz) {
        return IParsedClosureAssertion.class.equals(clazz);
    }
}
