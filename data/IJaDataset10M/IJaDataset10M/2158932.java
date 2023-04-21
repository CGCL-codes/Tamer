package uk.co.ordnancesurvey.rabbitparser.testkit.testpart.impl.entity.synonym;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.synonym.IParsedSynonym;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.synonym.IParsedSynonymListBody;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.entity.synonym.ParsedSynonymListBody;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestPartMatcher;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.TestPartMatcher;

/**
 * Adds test methods for {@link IParsedSynonymListBody}.
 * 
 * @author rdenaux
 * 
 */
public class TestParsedSynonymListBody extends ParsedSynonymListBody implements ITestParsedPart<IParsedSynonymListBody> {

    private static final long serialVersionUID = -1368133244446026174L;

    public TestParsedSynonymListBody(IParsedKeyphrase keyphrase, IParsedSynonym listPart) {
        super(keyphrase, listPart);
    }

    private ITestPartMatcher matcher = new TestPartMatcher();

    public void setMatcher(ITestPartMatcher aMatcher) {
        assert aMatcher != null;
        matcher = aMatcher;
    }

    public boolean hasSameContents(IParsedSynonymListBody parsedPart) {
        return matcher.getAsTestPart(getListElement(), IParsedSynonym.class).hasSameContents(parsedPart.getListElement());
    }

    public boolean testsClass(Class<?> clazz) {
        return IParsedSynonymListBody.class.equals(clazz);
    }
}
