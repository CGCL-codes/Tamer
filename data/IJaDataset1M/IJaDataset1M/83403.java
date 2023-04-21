package uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.object;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedObjectList;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.base.BaseTestPartFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.impl.object.TestObjectList;

/**
 * Converts a {@link IParsedObjectList} into a {@link TestObjectList}.
 * 
 * @author rdenaux
 * 
 */
public class TestObjectListFactory extends BaseTestPartFactory<IParsedObjectList> {

    public TestObjectListFactory() {
        super(IParsedObjectList.class);
    }

    @Override
    protected ITestParsedPart<IParsedObjectList> doConvertToTestPart(IParsedObjectList part) {
        return new TestObjectList(part.getNumberOfMoreOf(), part.getHeadElement(), part.getTailBodies());
    }
}
