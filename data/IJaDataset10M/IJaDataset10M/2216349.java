package net.sourceforge.freejava.combina;

import static net.sourceforge.freejava.collection.array.AbstractArrayWrapper.wrap;
import static org.junit.Assert.assertArrayEquals;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import net.sourceforge.freejava.closure.alt.Proc1;
import net.sourceforge.freejava.collection.primitive.IntegerCollection;
import net.sourceforge.freejava.primitive.IntMath;
import org.junit.Test;

public class PermutationTest extends TestCase {

    int count = 0;

    static void debugf(String format, Object... args) {
        String mesg = String.format(format, args);
        System.out.println(mesg);
    }

    @Test
    public void testIterate() {
        final String orig = "654321";
        final char[] array = orig.toCharArray();
        final Set<String> instances = new HashSet<String>();
        Permutation.iterate(wrap(array), new Proc1<char[]>() {

            @Override
            public void exec(char[] inst) {
                count++;
                String s = new String(inst);
                assertEquals("iter-" + count, orig.length(), inst.length);
                instances.add(s);
                int pos = s.indexOf('1');
                s = s.replaceAll("[^1]", "0");
                int bin = Integer.parseInt(s, 2);
                debugf("%8d - %8d - %8d", count, pos, bin);
            }
        });
        int expectedCount = IntMath.fac(orig.length()).intValue();
        assert expectedCount == 720;
        assertEquals("iterates", expectedCount, count);
        assertEquals("unified", expectedCount, instances.size());
        assertEquals("modified", orig, new String(array));
    }

    static class TestPermEval {

        char[] src;

        char[] dst;

        public TestPermEval(char[] src) {
            this.src = src;
            this.dst = new char[src.length];
        }

        String eval(int ord) {
            Permutation.perm(ord, wrap(src), wrap(dst));
            return String.valueOf(dst);
        }

        void o(int ord, String expected) {
            Permutation.perm(ord, wrap(src), wrap(dst));
            String actual = String.valueOf(dst);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testPerm1() {
        TestPermEval d = new TestPermEval("1".toCharArray());
        d.o(0, "1");
    }

    @Test
    public void testPerm2() {
        TestPermEval d = new TestPermEval("12".toCharArray());
        d.o(0, "12");
        d.o(1, "21");
    }

    @Test
    public void testPerm3() {
        TestPermEval d = new TestPermEval("123".toCharArray());
        d.o(0 * 2 + 0, "123");
        d.o(0 * 2 + 1, "132");
        d.o(1 * 2 + 0, "213");
        d.o(1 * 2 + 1, "231");
        d.o(2 * 2 + 0, "312");
        d.o(2 * 2 + 1, "321");
        d.o(3 * 6 + 0 * 2 + 0, "123");
        d.o(3 * 6 + 0 * 2 + 1, "132");
        d.o(3 * 6 + 1 * 2 + 0, "213");
        d.o(3 * 6 + 1 * 2 + 1, "231");
        d.o(3 * 6 + 2 * 2 + 0, "312");
        d.o(3 * 6 + 2 * 2 + 1, "321");
    }

    @Test
    public void testPerm4() {
        TestPermEval d = new TestPermEval("1234".toCharArray());
        d.o(0 * 6 + 0 * 2 + 0, "1234");
        d.o(0 * 6 + 0 * 2 + 1, "1243");
        d.o(0 * 6 + 1 * 2 + 0, "1324");
        d.o(0 * 6 + 1 * 2 + 1, "1342");
        d.o(0 * 6 + 2 * 2 + 0, "1423");
        d.o(0 * 6 + 2 * 2 + 1, "1432");
        d.o(1 * 6 + 0 * 2 + 0, "2134");
        d.o(1 * 6 + 0 * 2 + 1, "2143");
        d.o(1 * 6 + 1 * 2 + 0, "2314");
        d.o(1 * 6 + 1 * 2 + 1, "2341");
        d.o(1 * 6 + 2 * 2 + 0, "2413");
        d.o(1 * 6 + 2 * 2 + 1, "2431");
        d.o(2 * 6 + 0 * 2 + 0, "3124");
        d.o(2 * 6 + 0 * 2 + 1, "3142");
        d.o(2 * 6 + 1 * 2 + 0, "3214");
        d.o(2 * 6 + 1 * 2 + 1, "3241");
        d.o(2 * 6 + 2 * 2 + 0, "3412");
        d.o(2 * 6 + 2 * 2 + 1, "3421");
        d.o(3 * 6 + 0 * 2 + 0, "4123");
        d.o(3 * 6 + 0 * 2 + 1, "4132");
        d.o(3 * 6 + 1 * 2 + 0, "4213");
        d.o(3 * 6 + 1 * 2 + 1, "4231");
        d.o(3 * 6 + 2 * 2 + 0, "4312");
        d.o(3 * 6 + 2 * 2 + 1, "4321");
    }

    @Test
    public void testPerm5() throws Throwable {
        TestPermEval d = new TestPermEval("12345".toCharArray());
        int last = 0;
        for (int i = 0; i < 120; i++) {
            String perm = (String) d.eval(i);
            int num = Integer.parseInt(perm);
            assertTrue("increase", num > last);
            last = num;
        }
    }

    static class TestPermOrd {

        char[] src;

        public TestPermOrd(char[] src) {
            this.src = src;
        }

        public void o(String input, int expected) {
            char[] dst = input.toCharArray();
            int actual = Permutation.ord(wrap(src), wrap(dst));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testOrd1() {
        TestPermOrd d = new TestPermOrd("1".toCharArray());
        d.o("1", 0);
    }

    @Test
    public void testOrd2() {
        TestPermOrd d = new TestPermOrd("12".toCharArray());
        d.o("12", 0);
        d.o("21", 1);
    }

    @Test
    public void testOrd3() {
        TestPermOrd d = new TestPermOrd("123".toCharArray());
        d.o("123", 0 * 2 + 0);
        d.o("132", 0 * 2 + 1);
        d.o("213", 1 * 2 + 0);
        d.o("231", 1 * 2 + 1);
        d.o("312", 2 * 2 + 0);
        d.o("321", 2 * 2 + 1);
    }

    @Test
    public void testOrd4() {
        TestPermOrd d = new TestPermOrd("1234".toCharArray());
        d.o("1234", 0 * 6 + 0 * 2 + 0);
        d.o("1243", 0 * 6 + 0 * 2 + 1);
        d.o("1324", 0 * 6 + 1 * 2 + 0);
        d.o("1342", 0 * 6 + 1 * 2 + 1);
        d.o("1423", 0 * 6 + 2 * 2 + 0);
        d.o("1432", 0 * 6 + 2 * 2 + 1);
        d.o("2134", 1 * 6 + 0 * 2 + 0);
        d.o("2143", 1 * 6 + 0 * 2 + 1);
        d.o("2314", 1 * 6 + 1 * 2 + 0);
        d.o("2341", 1 * 6 + 1 * 2 + 1);
        d.o("2413", 1 * 6 + 2 * 2 + 0);
        d.o("2431", 1 * 6 + 2 * 2 + 1);
        d.o("3124", 2 * 6 + 0 * 2 + 0);
        d.o("3142", 2 * 6 + 0 * 2 + 1);
        d.o("3214", 2 * 6 + 1 * 2 + 0);
        d.o("3241", 2 * 6 + 1 * 2 + 1);
        d.o("3412", 2 * 6 + 2 * 2 + 0);
        d.o("3421", 2 * 6 + 2 * 2 + 1);
        d.o("4123", 3 * 6 + 0 * 2 + 0);
        d.o("4132", 3 * 6 + 0 * 2 + 1);
        d.o("4213", 3 * 6 + 1 * 2 + 0);
        d.o("4231", 3 * 6 + 1 * 2 + 1);
        d.o("4312", 3 * 6 + 2 * 2 + 0);
        d.o("4321", 3 * 6 + 2 * 2 + 1);
    }

    @Test
    public void testIterOrd() {
        final char[] src = "1234".toCharArray();
        final List<Integer> ords = new ArrayList<Integer>();
        Permutation.iterate(wrap(src), new Proc1<char[]>() {

            @Override
            public void exec(char[] inst) {
                int ord = Permutation.ord(wrap(src), wrap(inst));
                ords.add(ord);
            }
        });
        int[] expecteds = { 0, 1, 3, 2, 4, 5, 9, 8, 10, 11, 6, 7, 16, 17, 12, 13, 15, 14, 18, 19, 21, 20, 22, 23 };
        int[] actuals = IntegerCollection.toArray(ords);
        assertArrayEquals(expecteds, actuals);
    }
}
