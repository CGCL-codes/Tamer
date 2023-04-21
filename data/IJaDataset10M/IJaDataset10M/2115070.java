package hampi.tests.gramgren;

import hampi.constraints.Regexp;
import hampi.grammars.Grammar;
import hampi.grammars.apps.GrammarStringBounder;
import hampi.grammars.parser.Parser;
import hampi.utils.PigeonHoleDistributor;
import java.util.*;
import junit.framework.TestCase;

public class BoundingTests extends TestCase {

    public void testMultiCharTerminals() throws Exception {
        Grammar g = new Parser(GrammarTests.DIR + "test_multiCharTerminal.txt").parse();
        GrammarStringBounder gsb = new GrammarStringBounder();
        Regexp boundedRegexp = gsb.getBoundedRegexp(g, "program", 3, false);
        System.out.println(boundedRegexp);
        assertTrue(!boundedRegexp.matches("AB__"));
        assertTrue(boundedRegexp.matches("AB_"));
    }

    public void test1() throws Exception {
        Grammar g = new Parser(GrammarTests.DIR + "test_generate_parent.txt").parse();
        GrammarStringBounder gsb = new GrammarStringBounder();
        Regexp boundedRegexp = gsb.getBoundedRegexp(g, "expr", 2, false);
        assertTrue(boundedRegexp.matches("()"));
        assertTrue(!boundedRegexp.matches("(())"));
    }

    public void test2() throws Exception {
        Grammar g = new Parser(GrammarTests.DIR + "test_generate_parent.txt").parse();
        GrammarStringBounder gsb = new GrammarStringBounder();
        Regexp boundedRegexp = gsb.getBoundedRegexp(g, "expr", 3, false);
        assertNull(boundedRegexp);
    }

    public void test3() throws Exception {
        Grammar g = new Parser(GrammarTests.DIR + "test_generate_parent.txt").parse();
        GrammarStringBounder gsb = new GrammarStringBounder();
        Regexp boundedRegexp = gsb.getBoundedRegexp(g, "expr", 4, false);
        System.out.println(boundedRegexp);
        assertTrue(!boundedRegexp.matches("()"));
        assertTrue(boundedRegexp.matches("(())"));
        assertTrue(boundedRegexp.matches("()()"));
        assertTrue(!boundedRegexp.matches("(((("));
    }

    public void test4() throws Exception {
        Grammar g = new Parser(GrammarTests.DIR + "test_generate_parent.txt").parse();
        GrammarStringBounder gsb = new GrammarStringBounder();
        Regexp boundedRegexp = gsb.getBoundedRegexp(g, "expr", 5, false);
        assertNull(boundedRegexp);
    }

    public void test5() throws Exception {
        Grammar g = new Parser(GrammarTests.DIR + "test_generate_parent.txt").parse();
        GrammarStringBounder gsb = new GrammarStringBounder();
        Regexp boundedRegexp = gsb.getBoundedRegexp(g, "expr", 150, false);
        assertNotNull(boundedRegexp);
    }

    public void test6() throws Exception {
        Grammar g = new Parser(GrammarTests.DIR + "test_arithm.txt").parse();
        GrammarStringBounder gsb = new GrammarStringBounder();
        int bound = 40;
        Regexp boundedRegexp = gsb.getBoundedRegexp(g, "S", bound, false);
        assertNotNull(boundedRegexp);
    }

    public void testSmallSQL() throws Exception {
        Grammar g = new Parser(GrammarTests.DIR + "small_sql.txt").parse();
        GrammarStringBounder gsb = new GrammarStringBounder();
        Regexp boundedRegexp = gsb.getBoundedRegexp(g, "Select", 20, true);
        assertNotNull(boundedRegexp);
    }

    public void testEcmascript() throws Exception {
        Grammar g = new Parser(GrammarTests.DIR + "ecmascript.txt").parse();
        GrammarStringBounder gsb = new GrammarStringBounder();
        Regexp boundedRegexp = gsb.getBoundedRegexp(g, "FunctionDeclaration", 27, true);
        assertNotNull(boundedRegexp);
    }

    public void testTinySQL() throws Exception {
        Grammar g = new Parser(GrammarTests.DIR + "tiny_SQL.txt").parse();
        GrammarStringBounder gsb = new GrammarStringBounder();
        Regexp boundedRegexp = gsb.getBoundedRegexp(g, "SelectStmt", 26, false);
        assertNotNull(boundedRegexp);
        assertNotNull(boundedRegexp);
    }

    public void testDistro1() throws Exception {
        int pigeons = 20;
        int holes = 5;
        PigeonHoleDistributor phd = new PigeonHoleDistributor();
        Set<List<Integer>> distribute = phd.distribute(holes, pigeons, false);
        assertTrue(!distribute.isEmpty());
        for (List<Integer> list : distribute) {
            assertEquals(pigeons, sum(list));
        }
    }

    public void testUselessProductionCycle1() throws Exception {
        Grammar g = new Parser(GrammarTests.DIR + "testUselessProductionCycle1.txt").parse();
        GrammarStringBounder gsb = new GrammarStringBounder();
        Regexp boundedRegexp = gsb.getBoundedRegexp(g, "S", 1, false);
        assertNotNull(boundedRegexp);
    }

    public void testUselessProductionCycle2() throws Exception {
        Grammar g = new Parser(GrammarTests.DIR + "testUselessProductionCycle2.txt").parse();
        GrammarStringBounder gsb = new GrammarStringBounder();
        Regexp boundedRegexp = gsb.getBoundedRegexp(g, "S", 3, false);
        assertNotNull(boundedRegexp);
    }

    private static int sum(List<Integer> list) {
        int sum = 0;
        for (Integer i : list) {
            sum += i;
        }
        return sum;
    }
}
