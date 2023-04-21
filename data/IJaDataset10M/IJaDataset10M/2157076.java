package uk.co.ordnancesurvey.rabbitparser.gate.jape.instanceassertionfinder;

import uk.co.ordnancesurvey.rabbitparser.gate.jape.BaseSingleAnnotationJapeFinderTest;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedInstanceAssertion;

/**
 * Base class for all classes testing instance assertion call backs.
 * @author rdenaux
 *
 */
public abstract class BaseInstanceAssertionCBTest extends BaseSingleAnnotationJapeFinderTest<IParsedInstanceAssertion> {

    public BaseInstanceAssertionCBTest() {
        super(IParsedInstanceAssertion.class);
    }
}
