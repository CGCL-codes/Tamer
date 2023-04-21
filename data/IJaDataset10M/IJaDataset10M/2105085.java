package net.sf.anole.test;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TestAnole
 * @author Fengbo Tse (fengbotse@hotmail.com)
 * @version $Revision: 1.1 $ $ Date: Jul 24, 2003 2:09:20 PM GMT+8$
 */
public class TestAnole extends TestCase {

    protected static Log log = LogFactory.getLog("net.sf.anole.test");

    /**
     * No-arg constructor to enable serialization. This method
     * is not intended to be used by mere mortals without calling setName().
     */
    public TestAnole() {
        addTest(getClass());
    }

    public TestAnole(String testName) {
        super(testName);
        addTest(getClass());
    }

    private static TestSuite suite = new TestSuite();

    public static Test suite() {
        return suite;
    }

    public static void addTest(Class clazz) {
        suite.addTestSuite(clazz);
    }

    /**
     * This allows the tests to run as a standalone application.
     */
    public static void main(String args[]) {
        String[] testCaseName = { TestAnole.class.getName() };
        int type = 0;
        if (args.length > 0) {
            if (args[0].startsWith("-a") || args[0].startsWith("-A")) {
                type = 1;
            } else if (args[0].startsWith("-s") || args[0].startsWith("-S")) {
                type = 2;
            }
        }
        switch(type) {
            case 1:
                junit.awtui.TestRunner.main(testCaseName);
                break;
            case 2:
                junit.swingui.TestRunner.main(testCaseName);
                break;
            default:
                junit.textui.TestRunner.main(testCaseName);
        }
    }
}
