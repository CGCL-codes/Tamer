package uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.datavalue;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedFreeText;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.ITestParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.base.BaseTestPartFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.impl.datavalue.TestParsedFreeText;

public class TestParsedFreeTextFactory extends BaseTestPartFactory<IParsedFreeText> {

    public TestParsedFreeTextFactory() {
        super(IParsedFreeText.class);
    }

    @Override
    protected ITestParsedPart<IParsedFreeText> doConvertToTestPart(IParsedFreeText part) {
        return new TestParsedFreeText(part.getOpening(), part.getText(), part.getClosing());
    }
}
