package uk.co.ordnancesurvey.rabbitparser.gate.jape.definitionphrasefinder;

import org.junit.Test;
import uk.co.ordnancesurvey.rabbitparser.IRabbitParsedResult;
import uk.co.ordnancesurvey.rabbitparser.gate.jape.BaseSingleAnnotationJapeFinderTest;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.compoundrelationship.IParsedDefinitionPhrase;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.impl.compoundrelationship.TestParsedDefinitionPhrase;

/**
 * Tests the {@link JapeCB_DefinitionPhrasePattern} class.
 * 
 * @author rdenaux
 * 
 */
public class JapeCB_DefinitionPhrasePatternTest extends BaseSingleAnnotationJapeFinderTest<IParsedDefinitionPhrase> {

    public JapeCB_DefinitionPhrasePatternTest() {
        super(IParsedDefinitionPhrase.class);
    }

    @Test
    public void testFindCompoundRelationshipOk() throws Exception {
        final IRabbitParsedResult result = rabbitParser.parse("has purpose a Purpose");
        TestParsedDefinitionPhrase expected = createTestDefinitionPhrase(createTestCompoundRelationship(createTestComplexParsedRelationshipPhrase(createExpectedRelation("has", "purpose"), createExpectedObjectWithIndefiniteArticle("Purpose"))));
        assertFoundAnnotation(rabbitParser, expected);
    }

    @Test
    public void testFindNegatedCompoudnRelationshipOk() throws Exception {
        final IRabbitParsedResult result = rabbitParser.parse("does not have purpose a Purpose");
        TestParsedDefinitionPhrase expected = createTestDefinitionPhrase(createTestNegatedCompoundRelationship(createTestCompoundRelationship(createTestComplexParsedRelationshipPhrase(createExpectedRelation("have", "purpose"), createExpectedObjectWithIndefiniteArticle("Purpose")))));
        assertFoundAnnotation(rabbitParser, expected);
    }

    @Test
    public void testFindSubsumptionPhraseOk() throws Exception {
        final IRabbitParsedResult result = rabbitParser.parse("is a kind of River");
        TestParsedDefinitionPhrase expected = createTestDefinitionPhrase(createTestSubsumptionPhrase(createExpectedConcept("River")));
        assertFoundAnnotation(rabbitParser, expected);
    }
}
