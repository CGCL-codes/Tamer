package name.fraser.neil.plaintext;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Test harness for {@link Match}.
 * 
 * @author klaus_halfmann at sourceforge.net
 * @author fraser@google.com (Neil Fraser)
 */
public class MatchTest extends TestCase {

    @Test
    public void testBoring() {
        assertNotNull(new Match());
    }

    @Test
    public void testMainExceptions() {
        try {
            Match.main(null, null, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            Match.main("", null, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        try {
            Match.main(null, "", -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        String searchString = "Somw String that shall not include the Text found below";
        String longPattern = "A very long pattern that will surely be longer than Integer.SIZE";
        try {
            Match.main(searchString, longPattern, 64);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void testMain() {
        assertEquals("match_main: Equality.", 0, Match.main("abcdef", "abcdef", 1000));
        assertEquals("match_main: Null text.", -1, Match.main("", "abcdef", 1));
        assertEquals("match_main: Null pattern.", 3, Match.main("abcdef", "", 3));
        assertEquals("match_main: Exact match.", 3, Match.main("abcdef", "de", 3));
        assertEquals("match_main: Beyond end match.", 3, Match.main("abcdef", "defy", 4));
        assertEquals("match_main: Oversized pattern.", 0, Match.main("abcdef", "abcdefy", 0));
        Bitap.setMatchThreshold(0.7f);
        assertEquals("match_main: Complex match.", 4, Match.main("I am the very model of a modern major general.", " that berry ", 5));
        Bitap.setMatchThreshold(Bitap.DEFAULT_MATCH_THRESHOLD);
        try {
            Match.main(null, null, 0);
            fail("match_main: Null inputs.");
        } catch (IllegalArgumentException ex) {
        }
    }
}
