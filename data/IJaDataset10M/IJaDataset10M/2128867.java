package astcentric.editor.swing.dialog;

import junit.framework.TestCase;

public class WildcardFilterTest extends TestCase {

    public void testEmptyPattern() {
        pass("", "");
        pass("", "a");
    }

    public void testConstantPattern() {
        pass("a", "a");
        pass("a", "A");
        pass("A", "a");
        pass("A", "A");
        passNot("a", "affe");
        passNot("a", "Kaffee");
        passNot("affe", "a");
    }

    public void testStar() {
        pass("*", "");
        pass("*", "a");
        pass("*", "affe");
        pass("*", "*");
    }

    public void testStartConstant() {
        pass("*t", "t");
        pass("*t", "Test");
        pass("*t", "test");
        passNot("*t", "ti");
        passNot("*t", "iti");
        pass("*te", "te");
        pass("*te", "ate");
        pass("*te", "ate");
        pass("*te", "ateate");
        passNot("*te", "a");
        passNot("*te", "atea");
    }

    public void testConstantStar() {
        pass("t*", "t");
        pass("t*", "test");
        pass("t*", "tester");
        passNot("t*", "a");
        passNot("t*", "at");
        passNot("t*", "ate");
        pass("af*", "af");
        pass("af*", "affe");
        pass("af*", "affenkaffee");
        passNot("af*", "a");
        passNot("af*", "f");
        passNot("af*", "kaffee");
    }

    public void testStarConstant() {
        pass("*t*", "t");
        pass("*t*", "t");
        pass("*t*", "Test");
        pass("*t*", "Tester");
        pass("*t*", "test");
        pass("*t*", "tester");
        passNot("*t*", "");
        passNot("*t*", "a");
        passNot("*t*", "affe");
    }

    public void testConstantStarConstant() {
        pass("a*e", "ae");
        pass("a*e", "affe");
        pass("a*e", "affebande");
        passNot("a*e", "e");
        passNot("a*e", "a");
        passNot("a*e", "af");
        passNot("a*e", "fe");
        passNot("a*e", "affen");
        pass("a*ea", "aea");
        pass("a*ea", "area");
        pass("a*ea", "aeaea");
        pass("a*ea", "area in area");
        passNot("a*ea", "areas");
        passNot("a*ea", "aean");
        passNot("a*ea", "in area");
    }

    public void testStarConstantStarConstant() {
        pass("*a*e", "ae");
        pass("*a*e", "are");
        pass("*a*e", "tae");
        pass("*a*e", "tare");
        pass("*a*e", "tare ware");
        passNot("*a*e", "a");
        passNot("*a*e", "e");
        passNot("*a*e", "aeaet");
        passNot("*a*e", "teaer");
        passNot("*a*e", "aaet");
        passNot("*a*e", "are?");
        pass("*a*a", "aa");
        pass("*a*a", "aaa");
        pass("*a*a", "taa");
        pass("*a*a", "tata");
        pass("*a*a", "taatata");
        passNot("*a*a", "");
        passNot("*a*a", "a");
        passNot("*a*a", "ra");
        passNot("*a*a", "raarar");
    }

    private void pass(String pattern, String string) {
        assertTrue("'" + string + "' expected to match '" + pattern + "'", new WildcardFilter(pattern).pass(string));
    }

    private void passNot(String pattern, String string) {
        assertFalse("'" + string + "' expected not match '" + pattern + "'", new WildcardFilter(pattern).pass(string));
    }
}
