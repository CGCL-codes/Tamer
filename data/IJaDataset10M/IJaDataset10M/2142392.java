package triebag.tries;

import junit.framework.TestCase;
import java.util.Arrays;
import java.util.Random;
import static triebag.tries.CompactStringTrie.*;

public class StringTrieTest extends TestCase {

    public void testOnlineCompactTrieTest() {
        assertEquals(0, getSplitPoint("foo".toCharArray(), 0, "bar".toCharArray()));
        assertEquals(0, getSplitPoint("foo".toCharArray(), 0, "ar".toCharArray()));
        assertEquals(0, getSplitPoint("b".toCharArray(), 0, "x".toCharArray()));
        assertEquals(0, getSplitPoint("fo".toCharArray(), 0, "barx".toCharArray()));
        assertEquals(1, getSplitPoint("foo".toCharArray(), 0, "far".toCharArray()));
        assertEquals(1, getSplitPoint("far".toCharArray(), 0, "foo".toCharArray()));
        assertEquals(1, getSplitPoint("fo".toCharArray(), 0, "fxy".toCharArray()));
        assertEquals(1, getSplitPoint("f".toCharArray(), 0, "f".toCharArray()));
        assertEquals(3, getSplitPoint("foo".toCharArray(), 0, "foo".toCharArray()));
        assertEquals(3, getSplitPoint("foo".toCharArray(), 0, "foobar".toCharArray()));
        assertEquals(3, getSplitPoint("fool".toCharArray(), 0, "foo".toCharArray()));
        assertEquals(3, getSplitPoint("xyzfoo".toCharArray(), 3, "foo".toCharArray()));
        assertEquals(2, getSplitPoint("foo".toCharArray(), 1, "oobar".toCharArray()));
        assertEquals(3, getSplitPoint("bool".toCharArray(), 1, "ool".toCharArray()));
    }

    public void testSimpleInsertion() {
        StringTrie ost = new StringTrie();
        ost.add("foo");
        ost.add("foobar");
        ost.add("foobarjazz");
        assertEquals("#:(f)|foo:(b)*|bar:(j)*|jazz:()*|", ost.toFlatString());
    }

    public void testSimpleInsertionDifferentOrder() {
        StringTrie ost = new StringTrie();
        ost.add("foobarjazz");
        ost.add("foobar");
        ost.add("foo");
        assertEquals("#:(f)|foo:(b)*|bar:(j)*|jazz:()*|", ost.toFlatString());
    }

    public void testSimpleInsertionSeperate() {
        StringTrie ost = new StringTrie();
        ost.add("foo");
        ost.add("bar");
        ost.add("jazz");
        assertEquals("#:(bfj)|bar:()*|foo:()*|jazz:()*|", ost.toFlatString());
    }

    public void testSimpleInsertionSimpleSplit() {
        StringTrie ost = new StringTrie();
        ost.add("foobar");
        ost.add("foo");
        assertEquals("#:(f)|foo:(b)*|bar:()*|", ost.toFlatString());
    }

    public void testSimpleInsertionComplexSplit() {
        StringTrie ost = new StringTrie();
        ost.add("foobar");
        ost.add("foz");
        assertEquals("#:(f)|fo:(oz)|obar:()*|z:()*|", ost.toFlatString());
    }

    public void testInsertionOfAllCombinationsResultsSameTrie() {
        StringTrie ost = new StringTrie();
        String[] testStr = { "aba", "abc", "a", "ab", "abba", "aaa", "baa" };
        for (String s : testStr) {
            ost.add(s);
        }
        System.out.println(ost.getInfo());
        for (int i = 0; i < testStr.length; i++) {
            StringTrie newOst = new StringTrie();
            for (int j = i; j < i + testStr.length; j++) {
                String s = testStr[j % testStr.length];
                newOst.add(s);
            }
            assertEquals(ost.toFlatString(), newOst.toFlatString());
        }
    }

    public void testSimpleInsertionComplexSplit6() {
        StringTrie ost = new StringTrie();
        String[] testStr = { "blahfoobar", "blahfoojazz", "blahjazz", "blahfoo" };
        for (String s : testStr) {
            ost.add(s);
        }
        assertEquals("#:(b)|blah:(fj)|foo:(bj)*|bar:()*|jazz:()*|jazz:()*|", ost.toFlatString());
    }

    public void testrandomInsertionsInAllCombinationsResultsTheSameTrie() {
        StringTrie ost = new StringTrie();
        String[] testStrArray = new String[100];
        for (int i = 0; i < testStrArray.length; i++) {
            testStrArray[i] = createRandomStringFrom("0123456789", 10);
        }
        System.out.println(Arrays.toString(testStrArray));
        for (String s : testStrArray) {
            ost.add(s);
        }
        System.out.println(ost.toDeepString());
        System.out.println(ost.toFlatString());
        for (int i = 0; i < testStrArray.length; i++) {
            StringTrie newOst = new StringTrie();
            for (int j = i; j < i + testStrArray.length; j++) {
                String s = testStrArray[j % testStrArray.length];
                newOst.add(s);
            }
            assertEquals(ost.toFlatString(), newOst.toFlatString());
        }
    }

    private static Random r = new Random(1);

    private String createRandomStringFrom(String s, int maxSize) {
        char[] c = new char[r.nextInt(maxSize) + 1];
        char[] chars = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            c[i] = chars[r.nextInt(chars.length)];
        }
        return new String(c);
    }
}
