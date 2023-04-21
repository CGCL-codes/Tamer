package uk.co.ordnancesurvey.rabbitparser.testkit.result;

import uk.co.ordnancesurvey.rabbitparser.result.BaseRabbitParsedResult;
import uk.co.ordnancesurvey.rabbitparser.result.BaseRabbitParsedResultWithIteratorAndDisambiguator;

/**
 * This parsed result extends the {@link BaseRabbitParsedResult} and is here for
 * test purposes. Because it extends an actual implementation, this test class
 * should only be used when other simpler test implementations are not good
 * enough.
 * 
 * @author rdenaux
 * 
 */
public class TestRabbitParsedResult extends BaseRabbitParsedResultWithIteratorAndDisambiguator {

    private static final long serialVersionUID = 5333237887327609097L;

    /**
	 * Uses a rendering for this parsed result to mock a parsed result that has
	 * been retrieved from a parsed string and for which spans have been
	 * assigned.
	 */
    public void mockParsedSpanAndParsedString() {
        TestParsedResultSpanAssigner spanAssigner = new TestParsedResultSpanAssigner();
        String rendering = spanAssigner.render(this);
        setParsedString(rendering);
    }
}
