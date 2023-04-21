package jmri.jmrix.jmriclient;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * JMRIClientReplyTest.java
 *
 * Description:	    tests for the jmri.jmrix.jmriclient.JMRIClientReply class
 * @author			Bob Jacobsen
 * @version         $Revision: 17977 $
 */
public class JMRIClientReplyTest extends TestCase {

    public void testCtor() {
        JMRIClientReply m = new JMRIClientReply();
        Assert.assertNotNull(m);
    }

    public JMRIClientReplyTest(String s) {
        super(s);
    }

    public static void main(String[] args) {
        String[] testCaseName = { "-noloading", JMRIClientReplyTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(JMRIClientReplyTest.class);
        return suite;
    }

    protected void setUp() {
        apps.tests.Log4JFixture.setUp();
    }

    protected void tearDown() {
        apps.tests.Log4JFixture.tearDown();
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JMRIClientReplyTest.class.getName());
}
