package uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.impl.sentencebody.conceptassertion;

import java.util.List;
import java.util.logging.Logger;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.compoundrelationship.IParsedDefinitionPhraseList;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.conceptassertion.ParsedConceptDefinition;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedConceptDefinition;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.subject.IParsedSubject;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestPartMatcher;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.TestPartMatcher;

/**
 * Adds test methods for {@link ParsedConceptDefinition}
 * 
 * @author rdenaux
 * 
 */
public class TestParsedConceptDefinition extends ParsedConceptDefinition implements ITestParsedPart<IParsedConceptDefinition> {

    private static final long serialVersionUID = 1764889545123659301L;

    private static final Logger log = Logger.getLogger(TestParsedConceptDefinition.class.getName());

    public TestParsedConceptDefinition(IParsedSubject subject, IParsedDefinitionPhraseList defList, List<IParsedKeyphrase> keyphrases) {
        super(subject, defList, keyphrases);
    }

    private ITestPartMatcher matcher = new TestPartMatcher();

    public void setMatcher(ITestPartMatcher aMatcher) {
        assert aMatcher != null;
        matcher = aMatcher;
    }

    public boolean hasSameContents(IParsedConceptDefinition parsedPart) {
        boolean subjectEqual = matcher.getAsTestPart(getSubject(), IParsedSubject.class).hasSameContents(parsedPart.getSubject());
        boolean defPhraseListEqual = matcher.hasSameContent(getDefinitionPhraseList(), parsedPart.getDefinitionPhraseList(), IParsedDefinitionPhraseList.class);
        log.fine("Comparing " + getAsString() + "\n with " + parsedPart.getAsString() + "\n subjectEqual " + subjectEqual + "\n defPhraseListEqual " + defPhraseListEqual);
        return subjectEqual && defPhraseListEqual;
    }

    public boolean testsClass(Class<?> clazz) {
        return IParsedConceptDefinition.class.equals(clazz);
    }
}
