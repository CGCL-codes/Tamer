package test.de.sicari.util;

import static org.junit.Assert.*;
import org.junit.Test;
import de.sicari.util.WildcardedCharSequence;

/**
 * This is the <i>JUnit</i> test for the {@link WildcardedCharSequence} class.
 *
 * @author Matthias Pressfreund
 * @version $Id: WildcardedCharSequenceTest.java 204 2007-07-11 19:26:55Z jpeters $
 */
public class WildcardedCharSequenceTest {

    protected String[][] tests_ = new String[][] { { "", "" }, { "", "?" }, { "*?", "*" }, { "*?", "?*" }, { "?*", "*?" }, { "**", "?*?" }, { "**?**???***", "*?*?*?*?" }, { "?", "a" }, { "?", "ab" }, { "a?", "?a" }, { "a*", "a" }, { "a?c", "a*c" }, { "a*c", "abbbc" }, { "a*a*?z", "aa?bcz" } };

    @Test
    public void testImplies() {
        Boolean[] results = new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE };
        int cnt = 0;
        for (String[] test : tests_) {
            assertEquals("WildcardedCharSequence('" + test[0] + "')" + ".implies(WildcardedCharSequence('" + test[1] + "')", results[cnt++], Boolean.valueOf(new WildcardedCharSequence(test[0]).implies(new WildcardedCharSequence(test[1]))));
        }
    }

    @Test
    public void testMatch() {
        Boolean[] results = new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
        int cnt = 0;
        for (String[] test : tests_) {
            assertEquals("WildcardedCharSequence('" + test[0] + "')" + ".match('" + test[1] + "')", results[cnt++], Boolean.valueOf(new WildcardedCharSequence(test[0]).match(test[1])));
        }
    }
}
