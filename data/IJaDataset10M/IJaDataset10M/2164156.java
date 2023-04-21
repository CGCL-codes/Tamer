package genj.gedcom;

import junit.framework.TestCase;

/**
 * Test gedcom TagPaths
 */
public class TagPathTest extends TestCase {

    /**
   * Test path matches that include selectors
   */
    public void testPathMatches() {
        testPathMatch("FOO", "FOO", true);
        testPathMatch("FOO#0", "FOO", true);
        testPathMatch("FOO#1", "FOO", true);
        testPathMatch("FOO", "FOOO", false);
        testPathMatch("FOO#0", "FO", false);
        testPathMatch("FOO#1", "FOOOO", false);
        testPathMatch("FOO", "FOO", 0, true);
        testPathMatch("FOO#0", "FOO", 0, true);
        testPathMatch("FOO#0", "FOO", 1, false);
        testPathMatch("FOO#1", "FOO", 0, false);
        testPathMatch("FOO#1", "FOO", 1, true);
        testPathMatch("FOO", "BAR", 0, false);
        testPathMatch("FOO", "FOOO", 0, false);
        testPathMatch("FOO#0", "FO", 0, false);
        testPathMatch("FOO#1", "FOOO", 1, false);
    }

    /**
   * test helper
   */
    private void testPathMatch(String path, String tag, boolean result) {
        assertEquals(path + ".equals(0," + tag + ")", new TagPath(path).equals(0, tag), result);
    }

    /**
   * test helper
   */
    private void testPathMatch(String path, String tag, int selector, boolean result) {
        TagPath tp = new TagPath(path);
        assertEquals(path + ".match(0," + tag + ") && " + path + ".match(0," + selector + ")", tp.equals(0, tag) && tp.equals(0, selector), result);
    }
}
