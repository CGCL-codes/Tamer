package jmri.jmrix.easydcc.easydccmon;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class EasyDccMonActionTest extends TestCase {

    public void testCreate() {
        EasyDccMonAction a = new EasyDccMonAction();
        Assert.assertNotNull("exists", a);
    }

    public EasyDccMonActionTest(String s) {
        super(s);
    }

    public static void main(String[] args) {
        String[] testCaseName = { EasyDccMonActionTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(EasyDccMonActionTest.class);
        return suite;
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EasyDccMonActionTest.class.getName());
}
