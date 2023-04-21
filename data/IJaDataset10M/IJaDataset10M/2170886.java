package uk.co.ordnancesurvey.rabbitparser.testkit.testpart.impl.sentencebody.roleinclusion;

import java.util.List;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.roleinclusion.ParsedComplexRoleInclusion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.roleinclusion.IParsedComplexRoleInclusion;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestPartMatcher;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.TestPartMatcher;

/**
 * Provides methods for testing {@link IParsedComplexRoleInclusion}s
 * 
 * @author rdenaux
 * 
 */
public class TestParsedComplexRoleInclusion extends ParsedComplexRoleInclusion implements ITestParsedPart<IParsedComplexRoleInclusion> {

    private static final long serialVersionUID = -8321612798448032838L;

    private ITestPartMatcher matcher = new TestPartMatcher();

    public void setMatcher(ITestPartMatcher aMatcher) {
        assert aMatcher != null;
        matcher = aMatcher;
    }

    public TestParsedComplexRoleInclusion(IParsedRelation broadRel, IParsedRelation indludedRel, IParsedConcept concept, IParsedRelation confRel, IParsedConcept confConcept, List<IParsedKeyphrase> kpList) {
        super(broadRel, indludedRel, concept, confRel, confConcept, kpList);
    }

    public boolean hasSameContents(IParsedComplexRoleInclusion parsedPart) {
        return matcher.hasSameContent(getBroadRelation(), parsedPart.getBroadRelation(), IParsedRelation.class) && matcher.hasSameContent(getIncludedRole(), parsedPart.getIncludedRole(), IParsedRelation.class) && matcher.hasSameContent(getRoleConfirm(), parsedPart.getRoleConfirm(), IParsedRelation.class) && matcher.hasSameContent(getConcept(), parsedPart.getConcept(), IParsedConcept.class) && matcher.hasSameContent(getConceptConfirm(), parsedPart.getConceptConfirm(), IParsedConcept.class);
    }

    public boolean testsClass(Class<?> clazz) {
        return IParsedComplexRoleInclusion.class.equals(clazz);
    }
}
