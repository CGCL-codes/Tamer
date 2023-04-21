package uk.co.ordnancesurvey.rabbitparser.testkit.testpart.impl.entity;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedEntityCandidate;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelationshipCandidate;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.entity.ParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestPartMatcher;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.TestPartMatcher;

/**
 * This class of {@link ParsedRelation}s are used in tests.
 * 
 * @author rdenaux
 * 
 */
public class TestParsedRelation extends ParsedRelation implements ITestParsedPart<IParsedRelation> {

    private static final long serialVersionUID = -2208318069918978470L;

    public TestParsedRelation(IParsedRelationshipCandidate aCandidate) {
        super(aCandidate);
    }

    public TestParsedRelation(IParsedRelation aParsedRelation) {
        this((IParsedRelationshipCandidate) aParsedRelation.getEntityCandidates().get(0));
        setOntologyEntity(aParsedRelation.getOntologyEntity());
    }

    private ITestPartMatcher matcher = new TestPartMatcher();

    public void setMatcher(ITestPartMatcher aMatcher) {
        assert aMatcher != null;
        matcher = aMatcher;
    }

    public void setIsDefinedInOntology(boolean aValue) {
        if (aValue) {
            setOntologyEntity("A ontology entity value");
        } else {
            setOntologyEntity(null);
        }
    }

    public boolean hasSameContents(IParsedRelation aOther) {
        boolean result = aOther != null;
        if (result) {
            result = matcher.hasSameContent(getEntityCandidates(), aOther.getEntityCandidates(), IParsedEntityCandidate.class);
        }
        return result;
    }

    public boolean testsClass(Class<?> clazz) {
        return IParsedRelation.class.equals(clazz);
    }
}
