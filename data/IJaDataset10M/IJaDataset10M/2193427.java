package uk.co.ordnancesurvey.rabbitparser.testkit.testpart.impl.sentencebody.entitydeclaration;

import java.util.List;
import java.util.logging.Logger;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.synonym.IParsedSynonymList;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.entitydeclaration.ParsedConceptDeclaration;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.objectmodifier.IParsedArticle;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedConceptDeclaration;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.subject.IParsedSubject;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestPartMatcher;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.TestPartMatcher;

/**
 * Provides methods for testing {@link IParsedConceptDeclaration}s.
 * 
 * @author rdenaux
 * 
 */
public class TestParsedConceptDeclaration extends ParsedConceptDeclaration implements ITestParsedPart<IParsedConceptDeclaration> {

    private static final long serialVersionUID = 5208455948781312128L;

    private static final Logger log = Logger.getLogger(TestParsedConceptDeclaration.class.getName());

    public TestParsedConceptDeclaration(IParsedSubject subject, IParsedArticle article, List<IParsedKeyphrase> ps) {
        super(subject, article, ps);
    }

    private ITestPartMatcher matcher = new TestPartMatcher();

    public void setMatcher(ITestPartMatcher aMatcher) {
        assert aMatcher != null;
        matcher = aMatcher;
    }

    public boolean hasSameContents(IParsedConceptDeclaration parsedPart) {
        boolean conceptEqual = matcher.getAsTestPart(getSubject(), IParsedSubject.class).hasSameContents(parsedPart.getSubject());
        boolean secondaryEqual = (isSecondary() == parsedPart.isSecondary());
        boolean pluralEqual = matcher.hasSameContent(getPlural(), parsedPart.getPlural(), IParsedConcept.class);
        boolean synonymsEqual = matcher.hasSameContent(getSynonyms(), getSynonyms(), IParsedSynonymList.class);
        log.info(getAsString() + "\nis equal to \n" + parsedPart + "\n" + conceptEqual + ", " + secondaryEqual + ", " + pluralEqual + ", " + synonymsEqual);
        return conceptEqual && secondaryEqual && pluralEqual && synonymsEqual;
    }

    public boolean testsClass(Class<?> clazz) {
        return IParsedConceptDeclaration.class.equals(clazz);
    }
}
