package uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.impl.entity.synonym;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedConceptCandidate;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.synonym.IParsedSynonym;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.entity.ParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestPartMatcher;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.TestPartMatcher;

/**
 * 
 * @author rdenaux
 * 
 */
public class TestParsedSynonymBasedOnConcept extends ParsedConcept implements ITestParsedPart<IParsedSynonym>, IParsedSynonym {

    private static final long serialVersionUID = 6145472333887051405L;

    private ITestPartMatcher matcher = new TestPartMatcher();

    public void setMatcher(ITestPartMatcher aMatcher) {
        assert aMatcher != null;
        matcher = aMatcher;
    }

    public TestParsedSynonymBasedOnConcept(IParsedConcept aConcept) {
        super((IParsedConceptCandidate) aConcept.getEntityCandidate());
    }

    public boolean hasSameContents(IParsedSynonym parsedPart) {
        return matcher.getAsTestPart(getSynonymValue(), IParsedConcept.class).hasSameContents(parsedPart.getSynonymValue()) && parsedPart.getPlural() == null;
    }

    public boolean testsClass(Class<?> clazz) {
        return IParsedSynonym.class.equals(clazz);
    }
}
