package uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.impl.entity.synonym;

import java.util.List;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.synonym.IParsedSynonym;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.synonym.IParsedSynonymList;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.synonym.IParsedSynonymListBody;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.entity.synonym.ParsedSynonymList;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestPartMatcher;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.TestPartMatcher;

/**
 * Provides test methods for {@link IParsedSynonymList}
 * 
 * @author rdenaux
 * 
 */
public class TestParsedSynonymList extends ParsedSynonymList implements ITestParsedPart<IParsedSynonymList> {

    private static final long serialVersionUID = 8424857322464020238L;

    public TestParsedSynonymList(IParsedSynonym headObject, List<IParsedSynonymListBody> tailObjects) {
        super(headObject, tailObjects);
    }

    private ITestPartMatcher matcher = new TestPartMatcher();

    public void setMatcher(ITestPartMatcher aMatcher) {
        assert aMatcher != null;
        matcher = aMatcher;
    }

    public boolean hasSameContents(IParsedSynonymList parsedPart) {
        return matcher.getAsTestPart(getHeadElement(), IParsedSynonym.class).hasSameContents(parsedPart.getHeadElement()) && matcher.hasSameContent(getTailBodies(), parsedPart.getTailBodies(), IParsedSynonymListBody.class);
    }

    public boolean testsClass(Class<?> clazz) {
        return IParsedSynonymList.class.equals(clazz);
    }
}
