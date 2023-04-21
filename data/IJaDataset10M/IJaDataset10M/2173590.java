package uk.co.ordnancesurvey.rabbitparser.gate.jape.relationshipphrasefinder;

import org.junit.Test;
import uk.co.ordnancesurvey.rabbitparser.IRabbitParsedResult;
import uk.co.ordnancesurvey.rabbitparser.gate.jape.BaseSingleAnnotationJapeFinderTest;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.objectmodifier.ParsedNumber;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedRelationshipPhrase;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.impl.object.TestParsedRelationshipPhrase;

/**
 * Tests the {@link JapeCB_RelationshipPhrasePattern}.
 * 
 * @author rdenaux
 * 
 */
public class JapeCB_RelationshipPhrasePatternTest extends BaseSingleAnnotationJapeFinderTest<IParsedRelationshipPhrase> {

    public JapeCB_RelationshipPhrasePatternTest() {
        super(IParsedRelationshipPhrase.class);
    }

    @Test
    public void testSimplePhraseOk() throws Exception {
        final IRabbitParsedResult result = rabbitParser.parse("has purpose a Purpose");
        TestParsedRelationshipPhrase expected = createTestParsedRelationshipPhrase(createExpectedRelation("has", "purpose"), createExpectedObjectWithIndefiniteArticle("Purpose"));
        assertFoundAnnotation(rabbitParser, expected);
    }

    @Test
    public void testHasCurrentACurrentOk() throws Exception {
        final IRabbitParsedResult result = rabbitParser.parse("has current a Current");
        TestParsedRelationshipPhrase expected = createTestParsedRelationshipPhrase(createExpectedRelation("has", "current"), createExpectedObjectWithIndefiniteArticle("Current"));
        assertFoundAnnotation(rabbitParser, expected);
    }

    @Test
    public void testComplexVerbOk() throws Exception {
        final IRabbitParsedResult result = rabbitParser.parse("is produced by a String");
        TestParsedRelationshipPhrase expected = createTestParsedRelationshipPhrase(createExpectedRelation("is", "produced", "by"), createExpectedObjectWithIndefiniteArticle("String"));
        assertFoundAnnotation(rabbitParser, expected);
    }

    @Test
    public void testPhraseWithObjectListOk() throws Exception {
        final IRabbitParsedResult result = rabbitParser.parse("has purpose one or more of Car Parking, Bike Parking or Skating");
        TestParsedRelationshipPhrase expected = createTestParsedRelationshipPhrase(createExpectedRelation("has", "purpose"), createTestObjectList(new ParsedNumber(1), createExpectedObject("Car", "Parking"), createTestListBody(createExpectedObject("Bike", "Parking")), createTestListBody(createExpectedObject("Skating"))));
        assertFoundAnnotation(rabbitParser, expected);
    }

    @Test
    public void testPhraseWithObjectAndListOk() throws Exception {
        final IRabbitParsedResult result = rabbitParser.parse("has purpose Car Parking and Bike Parking and Skating");
        TestParsedRelationshipPhrase expected = createTestParsedRelationshipPhrase(createExpectedRelation("has", "purpose"), createTestObjectAndList(createExpectedObject("Car", "Parking"), createTestAndListBody(createExpectedObject("Bike", "Parking")), createTestAndListBody(createExpectedObject("Skating"))));
        TestParsedRelationshipPhrase ambiguous = createTestParsedRelationshipPhrase(createExpectedRelation("has"), createTestObjectAndList(createExpectedObject("purpose", "Car", "Parking"), createTestAndListBody(createExpectedObject("Bike", "Parking")), createTestAndListBody(createExpectedObject("Skating"))));
        assertFoundAnnotation(rabbitParser, expected);
        assertFoundAnnotation(rabbitParser, ambiguous);
    }

    @Test
    public void testPhraseIsAKindOfPlaceNotFoundOk() throws Exception {
        final IRabbitParsedResult result = rabbitParser.parse("is a kind of Place");
        TestParsedRelationshipPhrase expected = createTestParsedRelationshipPhrase(createExpectedRelation("is"), createTestParsedObject(createExpectedConcept("kind"), createTestArticle("a"), createTestPrepositionModifier("of", "Place")));
        assertNotFoundByTestedJapeCB(rabbitParser, expected);
    }
}
