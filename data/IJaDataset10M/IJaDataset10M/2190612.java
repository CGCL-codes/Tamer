package uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.factory.compoundrelationship;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.compoundrelationship.IParsedCompoundRelationshipBody;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.base.BaseTestPartFactory;
import uk.co.ordnancesurvey.rabbitparser.testinfrastructure.testpart.impl.compoundrelationship.TestParsedCompoundRelationshipBody;

/**
 * Converts a {@link IParsedCompoundRelationshipBody} into a
 * {@link TestParsedCompoundRelationshipBody}.
 * 
 * @author rdenaux
 * 
 */
public class TestParsedCompoundRelationshipBodyFactory extends BaseTestPartFactory<IParsedCompoundRelationshipBody> {

    public TestParsedCompoundRelationshipBodyFactory() {
        super(IParsedCompoundRelationshipBody.class);
    }

    @Override
    protected ITestParsedPart<IParsedCompoundRelationshipBody> doConvertToTestPart(IParsedCompoundRelationshipBody part) {
        return new TestParsedCompoundRelationshipBody(part.getListSeparatorKeyphrase(), part.getListElement());
    }
}
