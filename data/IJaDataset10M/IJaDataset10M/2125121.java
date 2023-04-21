package uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.factory.sentencebody.roleinclusion;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.roleinclusion.IParsedGeneralRoleInclusion;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.base.BaseTestPartFactory;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.impl.sentencebody.roleinclusion.TestParsedGeneralRoleInclusion;

/**
 * Creates a {@link TestParsedGeneralRoleInclusion} based on a
 * {@link IParsedGeneralRoleInclusion}
 * 
 * @author rdenaux
 * 
 */
public class TestParsedGeneralRoleInclusionFactory extends BaseTestPartFactory<IParsedGeneralRoleInclusion> {

    public TestParsedGeneralRoleInclusionFactory() {
        super(IParsedGeneralRoleInclusion.class);
    }

    @Override
    protected ITestParsedPart<IParsedGeneralRoleInclusion> doConvertToTestPart(IParsedGeneralRoleInclusion part) {
        return new TestParsedGeneralRoleInclusion(part.getBroadRelation(), part.getIncludedRole(), part.getRoleConfirm(), part.getKeyphrases());
    }
}
