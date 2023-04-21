package securus.client.send;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 *
 * @author e.dovgopoliy
 */
public class TestFormSend extends TestCase {

    public TestFormSend(String name) {
        super(name);
    }

    public void testEmails() {
        assertTrue(FormSend.checkEmails(FormSend.parseStringToList("   e.dovgopoliy@levi9.com , d.posternak@levi9.com  , ")));
        assertTrue(FormSend.checkEmails(FormSend.parseStringToList("qwe@qwe.com")));
        assertTrue(FormSend.combineListToString(FormSend.parseStringToList("   e.dovgopoliy@levi9.com , d.posternak@levi9.com  , ")).equals("e.dovgopoliy@levi9.com,d.posternak@levi9.com"));
    }

    public static void main(String args[]) {
        TestRunner runner = new TestRunner();
        TestSuite suite = new TestSuite();
        suite.addTest(new TestFormSend("testEmails"));
        runner.doRun(suite);
    }
}
