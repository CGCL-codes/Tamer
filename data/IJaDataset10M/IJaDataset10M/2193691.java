package uk.co.ordnancesurvey.rabbitparser.gate.jape.conceptassertionfinder;

import org.junit.Test;
import uk.co.ordnancesurvey.rabbitparser.IRabbitParsedResult;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.impl.sentencebody.TestParsedConceptAssertion;

/**
 * Tests the {@link JapeCB_ClosureAssertion} class
 * 
 * @author rdenaux
 * 
 */
public class JapeCB_ClosureAssertionTest extends BaseConceptAssertionCBTest {

    @Test
    public void testClosureOk() throws Exception {
        final IRabbitParsedResult result = rabbitParser.parse("Every Basin only is connected to a Channel or a Pipe");
        TestParsedConceptAssertion expected = createTestAssertion(createTestClosureAssertion(createExpectedConcept("Basin"), createTestComplexParsedRelationshipPhrase(createExpectedRelation("is", "connected", "to"), createTestObjectList(createTestParsedObject(createExpectedConcept("Channel"), createTestArticle("a"), null), createTestListBody(createTestParsedObject(createExpectedConcept("Pipe"), createTestArticle("a"), null))))));
        assertFoundAnnotation(rabbitParser, expected);
    }

    @Test
    public void parseClosure2Ok() throws Exception {
        final IRabbitParsedResult result = rabbitParser.parse("Every Mill Stream only flows in a Mill Race");
        TestParsedConceptAssertion expected = createTestAssertion(createTestClosureAssertion(createExpectedConcept("Mill", "Stream"), createTestComplexParsedRelationshipPhrase(createExpectedRelation("flows", "in"), createExpectedObjectWithIndefiniteArticle("Mill", "Race"))));
        assertFoundAnnotation(rabbitParser, expected);
    }
}
