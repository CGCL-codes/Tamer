package uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.impl.sentencebody.instanceassertion;

import java.util.List;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedInstance;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.instanceassertion.ParsedSameInstances;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.instanceassertion.IParsedSameInstances;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestPartMatcher;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.TestPartMatcher;

/**
 * Provides test methods for {@link IParsedSameInstances}
 * 
 * @author rdenaux
 * 
 */
public class TestParsedSameInstances extends ParsedSameInstances implements ITestParsedPart<IParsedSameInstances> {

    private static final long serialVersionUID = 6482204665382790893L;

    public TestParsedSameInstances(IParsedInstance first, IParsedInstance second, List<IParsedKeyphrase> keyphrases) {
        super(first, second, keyphrases);
    }

    private ITestPartMatcher matcher = new TestPartMatcher();

    public void setMatcher(ITestPartMatcher aMatcher) {
        assert aMatcher != null;
        matcher = aMatcher;
    }

    public boolean hasSameContents(IParsedSameInstances parsedPart) {
        return matcher.hasSameContent(getFirst(), parsedPart.getFirst(), IParsedInstance.class) && matcher.hasSameContent(getSecond(), parsedPart.getSecond(), IParsedInstance.class);
    }

    public boolean testsClass(Class<?> clazz) {
        return IParsedSameInstances.class.equals(clazz);
    }
}
