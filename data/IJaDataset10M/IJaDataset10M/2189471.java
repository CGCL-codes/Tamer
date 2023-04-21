package checker.semantic;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import reader.antlr.SAFParser;
import utils.TestUtils;
import fighter.Fighter;
import fighter.Personality;
import fighter.checker.SemanticChecker;
import fighter.messages.Message;

public class CheckerTests {

    private SAFParser parser;

    private Fighter fighter;

    private List<Message> messages;

    public void setUp(String inputFile) throws RecognitionException {
        parser = TestUtils.getParserForFile(inputFile);
        fighter = parser.fighter();
        messages = SemanticChecker.checkFighter(fighter);
    }

    @Test
    public void persoanlityTest() throws RecognitionException {
        String inputFile = "semanticTest.txt";
        setUp(inputFile);
        assertEquals("Semantic", fighter.getName());
        Personality personality = fighter.getPersonality();
        assertEquals(7, personality.getKickPower());
        assertEquals(0, personality.getKickReach());
        assertEquals(5, personality.getPunchPower());
        assertEquals(12, personality.getPunchReach());
        assertEquals("WARNING: Action block_high apears multiple times in choose", messages.get(0).toString().trim());
        assertEquals("ERROR: Kick Reach exceeds lower limit", messages.get(1).toString().trim());
        assertEquals("ERROR: Punch Reach exceeds uper limit", messages.get(2).toString().trim());
    }
}
