package pcgen.persistence.lst.prereq;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;
import pcgen.core.prereq.Prerequisite;

/**
 * @author wardc
 *
 */
public class PreVariableParserTest extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(PreVariableParserTest.class);
    }

    /**
	 * @return Test
	 */
    public static Test suite() {
        return new TestSuite(PreVariableParserTest.class);
    }

    /**
	 * @throws Exception
	 */
    public void testNotEqual() throws Exception {
        PreVariableParser parser = new PreVariableParser();
        Prerequisite prereq = parser.parse("VARNEQ", "Enraged,1", false, false);
        assertEquals("<prereq kind=\"var\" key=\"Enraged\" operator=\"neq\" operand=\"1\" >\n</prereq>\n", prereq.toString());
    }
}
