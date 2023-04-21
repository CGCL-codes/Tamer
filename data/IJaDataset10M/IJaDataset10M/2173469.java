package pcgen.persistence.lst.prereq;

import pcgen.core.prereq.Prerequisite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PreLanguageParserTest extends TestCase {

    public static void main(String args[]) {
        junit.swingui.TestRunner.run(PreLanguageParserTest.class);
    }

    /**
	 * @return Test
	 */
    public static Test suite() {
        return new TestSuite(PreLanguageParserTest.class);
    }

    /**
	 * @throws Exception
	 */
    public void test1LanguageOf2() throws Exception {
        PreLanguageParser parser = new PreLanguageParser();
        Prerequisite prereq = parser.parse("LANG", "1,Dwarven,Elven", false, false);
        System.out.println(prereq);
        assertEquals("<prereq operator=\"gteq\" operand=\"1\" >\n" + "<prereq kind=\"lang\" count-multiples=\"true\" key=\"Dwarven\" operator=\"eq\" operand=\"1\" >\n" + "</prereq>\n" + "<prereq kind=\"lang\" count-multiples=\"true\" key=\"Elven\" operator=\"eq\" operand=\"1\" >\n" + "</prereq>\n" + "</prereq>\n", prereq.toString());
    }

    /**
	 * @throws Exception
	 */
    public void testNot1LanguageOf2() throws Exception {
        PreLanguageParser parser = new PreLanguageParser();
        Prerequisite prereq = parser.parse("LANG", "1,Dwarven,Elven", true, false);
        System.out.println(prereq);
        assertEquals("<prereq operator=\"lt\" operand=\"1\" >\n" + "<prereq kind=\"lang\" count-multiples=\"true\" key=\"Dwarven\" operator=\"eq\" operand=\"1\" >\n" + "</prereq>\n" + "<prereq kind=\"lang\" count-multiples=\"true\" key=\"Elven\" operator=\"eq\" operand=\"1\" >\n" + "</prereq>\n" + "</prereq>\n", prereq.toString());
    }

    /**
	 * @throws Exception
	 */
    public void test2LanguageOfAny() throws Exception {
        PreLanguageParser parser = new PreLanguageParser();
        Prerequisite prereq = parser.parse("LANG", "2,ANY", false, false);
        System.out.println(prereq);
        assertEquals("<prereq kind=\"lang\" count-multiples=\"true\" key=\"ANY\" operator=\"gteq\" operand=\"2\" >\n" + "</prereq>\n", prereq.toString());
    }

    /**
	 * @throws Exception
	 */
    public void testNot2LanguageOfAny() throws Exception {
        PreLanguageParser parser = new PreLanguageParser();
        Prerequisite prereq = parser.parse("LANG", "2,ANY", true, false);
        System.out.println(prereq);
        assertEquals("<prereq kind=\"lang\" count-multiples=\"true\" key=\"ANY\" operator=\"lt\" operand=\"2\" >\n" + "</prereq>\n", prereq.toString());
    }
}
