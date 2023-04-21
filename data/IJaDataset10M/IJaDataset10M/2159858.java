package uk.co.ordnancesurvey.rabbitparser.testkit.testpart.impl.entity;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedEntityCandidate;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedInstance;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedInstanceCandidate;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.entity.ParsedInstance;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestPartMatcher;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.TestPartMatcher;

/**
 * This class provides an implementation of an {@link IParsedInstance} that can
 * be used in tests.
 * 
 * @author rdenaux
 * 
 */
public class TestParsedInstance extends ParsedInstance implements ITestParsedPart<IParsedInstance> {

    private static final long serialVersionUID = 2864933332324087353L;

    public TestParsedInstance(IParsedInstanceCandidate candidate) {
        super(candidate);
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

    public boolean hasSameContents(IParsedInstance parsedPart) {
        boolean result = parsedPart != null;
        if (result) {
            result = matcher.hasSameContent(getEntityCandidate(), parsedPart.getEntityCandidate(), IParsedEntityCandidate.class) && matcher.hasSameContent(getDisambiguator(), parsedPart.getDisambiguator(), IParsedEntityCandidate.class);
        }
        return result;
    }

    public boolean testsClass(Class<?> clazz) {
        return IParsedInstance.class.equals(clazz);
    }
}
