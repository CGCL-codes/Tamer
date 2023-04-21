package consciouscode.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import junit.framework.TestCase;

public class StringsTest extends TestCase {

    public static final String AUTO_SUITES = "common";

    public void testJoin() {
        String[] split = { "alpha", "beta", "delta" };
        Iterator<String> iter = Arrays.asList(split).iterator();
        String joined = Strings.join(iter, "|");
        assertEquals("alpha|beta|delta", joined);
    }

    public void testEmptyJoin() {
        String[] split = {};
        Iterator<String> iter = Arrays.asList(split).iterator();
        String joined = Strings.join(iter, "|");
        assertEquals("", joined);
    }

    public void testBasicSplitAndCleanup() {
        String joined = "alpha,beta,delta";
        ArrayList<String> split = Strings.splitAndCleanup(joined, ",");
        assertEquals(3, split.size());
        assertEquals("alpha", split.get(0));
        assertEquals("beta", split.get(1));
        assertEquals("delta", split.get(2));
    }

    public void testMessySplitAndCleanup() {
        String joined = "alpha, beta, ,,   delta ,gamma";
        ArrayList<String> split = Strings.splitAndCleanup(joined, ",");
        assertEquals(4, split.size());
        assertEquals("alpha", split.get(0));
        assertEquals("beta", split.get(1));
        assertEquals("delta", split.get(2));
        assertEquals("gamma", split.get(3));
    }

    public void testEmptySplitAndCleanup() {
        String empty = "";
        ArrayList<String> split = Strings.splitAndCleanup(empty, ",");
        assertEquals(0, split.size());
    }

    public void testParseInt() {
        assertEquals(-23, Strings.parseInt(null, -23));
        assertEquals(-23, Strings.parseInt("yadda", -23));
        assertEquals(-23, Strings.parseInt("", -23));
        assertEquals(-23, Strings.parseInt(" ", -23));
        assertEquals(-23, Strings.parseInt("-2x3", -23));
        assertEquals(-23, Strings.parseInt(" 12", -23));
        assertEquals(-23, Strings.parseInt("12 ", -23));
        assertEquals(-23, Strings.parseInt("12x", -23));
        assertEquals(-23, Strings.parseInt("+19", -23));
        assertEquals(0, Strings.parseInt("0", -23));
        assertEquals(19, Strings.parseInt("19", -23));
        assertEquals(-19, Strings.parseInt("-19", -23));
    }
}
