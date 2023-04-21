package uk.co.ordnancesurvey.rabbitparser.gate.jape.relationshipdeclaration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.co.ordnancesurvey.rabbitparser.DeclarativeSentenceType;
import uk.co.ordnancesurvey.rabbitparser.IRabbitParsedResult;
import uk.co.ordnancesurvey.rabbitparser.gate.jape.BaseTestJapeCB_DeclarativeSentence;
import uk.co.ordnancesurvey.rabbitparser.gate.testinfrastructure.TestRabbitParser;
import uk.co.ordnancesurvey.rabbitparser.gate.testinfrastructure.TestRabbitParserFactory;

/**
 * Tests the JapeCB_RelationDomainSentence class
 * 
 * @author rdenaux
 * 
 */
public class JapeCB_RelationDomainSentenceTest extends BaseTestJapeCB_DeclarativeSentence {

    private static TestRabbitParser rabbitParser;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        rabbitParser = TestRabbitParserFactory.getTestRabbitParser();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Override
    protected DeclarativeSentenceType getExpectedDeclarativeSentenceType() {
        return DeclarativeSentenceType.RELATION_DOMAIN;
    }

    @Test
    public void testGetSentenceType() {
        JapeCB_RelationDomainSentence testObj = new JapeCB_RelationDomainSentence();
        DeclarativeSentenceType actual = testObj.getSentenceType();
        DeclarativeSentenceType expected = getExpectedDeclarativeSentenceType();
        assertEquals(expected, actual);
    }

    /**
	 * Mini integration test
	 * 
	 * @throws Exception
	 */
    @Test
    public void parseRelationDomainSentenceOkSingle() throws Exception {
        IRabbitParsedResult result = rabbitParser.parse("Is capital city can only have a city as a subject.");
        assertContains(result, getExpectedDeclarativeSentenceType());
    }

    /**
	 * Mini integration test
	 * 
	 * @throws Exception
	 */
    @Test
    public void parseRelationDomainSentenceOkMultiple() throws Exception {
        IRabbitParsedResult result = rabbitParser.parse("Occurs in relationship can only have summer or autumn or winter or spring as a subject.");
        assertContains(result, getExpectedDeclarativeSentenceType());
    }
}
