package jmri.jmrix.srcp;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import jmri.jmrix.srcp.parser.SRCPClientParser;
import jmri.jmrix.srcp.parser.ParseException;
import java.io.StringReader;

/**
 * SRCPReplyTest.java
 *
 * Description:	tests for the jmri.jmrix.srcp.SRCPReply class
 *
 * @author	Bob Jacobsen
 * @version $Revision: 19890 $
 */
public class SRCPReplyTest extends TestCase {

    public void testCtor() {
        SRCPReply m = new SRCPReply();
        Assert.assertNotNull(m);
    }

    public void testStringCtor() {
        String s = "100 OK REASON GOES HERE\n\r";
        SRCPReply m = new SRCPReply(s);
        Assert.assertNotNull(m);
        Assert.assertTrue("String Constructor Correct", s.equals(m.toString()));
    }

    public void testParserCtor() {
        String s = "100 OK REASON GOES HERE\n\r";
        SRCPClientParser p = new SRCPClientParser(new StringReader(s));
        SRCPReply m = null;
        try {
            m = new SRCPReply(p.inforesponse());
        } catch (ParseException pe) {
        }
        Assert.assertNotNull(m);
        Assert.assertTrue("Parser Constructor Correct", s.equals("" + m));
    }

    public SRCPReplyTest(String s) {
        super(s);
    }

    public static void main(String[] args) {
        String[] testCaseName = { "-noloading", SRCPReplyTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SRCPReplyTest.class);
        return suite;
    }

    @Override
    protected void setUp() {
        apps.tests.Log4JFixture.setUp();
    }

    @Override
    protected void tearDown() {
        apps.tests.Log4JFixture.tearDown();
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SRCPReplyTest.class.getName());
}
