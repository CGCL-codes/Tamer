package org.datanucleus.store.mapped.expression;

import org.datanucleus.store.mapped.expression.MatchExpressionParser;
import junit.framework.TestCase;

/**
 * Series of tests for the String "matches" function.
 * @version $Revision: 1.4 $
 */
public class MatchExpressionParserTest extends TestCase {

    public void testParsePattern() {
        String str = "test.*tete.yyy\\.dede\\\\--";
        MatchExpressionParser parser = new MatchExpressionParser(str, 'Z', 'A', '\\');
        String parsed = parser.parsePattern();
        String str_correct = "testZteteAyyy.dede\\\\\\\\--";
        assertTrue("Parsing mised expression gave erroneous string : " + parsed + " but should have been " + str_correct, parsed.equals(str_correct));
        MatchExpressionParser parser2 = new MatchExpressionParser("", 'Z', 'A', '\\');
        assertEquals("", parser2.parsePattern());
        str = ".output.";
        MatchExpressionParser parser3 = new MatchExpressionParser(".putput.", 'Z', 'A', '\\');
        parsed = parser3.parsePattern();
        str_correct = "AputputA";
        assertEquals(str_correct, parsed);
        str = "\"slash\\city\"";
        MatchExpressionParser parser4 = new MatchExpressionParser(str, 'Z', 'A', '\\');
        parsed = parser4.parsePattern();
        str_correct = "\"slash\\\\city\"";
        assertEquals(str_correct, parsed);
    }
}
