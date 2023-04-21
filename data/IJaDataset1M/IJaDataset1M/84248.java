package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.objectmodifier;

import static org.junit.Assert.*;
import org.junit.Test;
import uk.co.ordnancesurvey.rabbitparser.RabbitKeyphraseType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.objectmodifier.ParsedThatPostModifier;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedRelationshipPhrase;
import uk.co.ordnancesurvey.rabbitparser.testkit.RabbitParserTestObjectFactory;

/**
 * Tests the {@link ParsedThatPostModifier} class.
 * 
 * @author rdenaux.
 * 
 */
public class ParsedThatPostModifierTest {

    private ParsedThatPostModifier testObj;

    @Test
    public void createOk() {
        IParsedKeyphrase kp = createThatKeyphrase();
        IParsedRelationshipPhrase rp = createTestRelationshipPhrase();
        testObj = new ParsedThatPostModifier(kp, rp);
        assertEquals(kp, testObj.getThatKeyphrase());
        assertEquals(rp, testObj.getRelationshipPhrase());
    }

    @Test(expected = AssertionError.class)
    public void createWithNullKeyphraseFail() {
        new ParsedThatPostModifier(null, createTestRelationshipPhrase());
    }

    @Test(expected = AssertionError.class)
    public void createWithNullRelphraseFail() {
        new ParsedThatPostModifier(createThatKeyphrase(), null);
    }

    @Test(expected = AssertionError.class)
    public void createWithWrongKeyphraseFail() {
        new ParsedThatPostModifier(RabbitParserTestObjectFactory.createTestKeyphrase(RabbitKeyphraseType.And), createTestRelationshipPhrase());
    }

    private IParsedRelationshipPhrase createTestRelationshipPhrase() {
        return RabbitParserTestObjectFactory.createTestRelationshipPhrase();
    }

    private IParsedKeyphrase createThatKeyphrase() {
        return RabbitParserTestObjectFactory.createTestKeyphrase(RabbitKeyphraseType.That);
    }
}
