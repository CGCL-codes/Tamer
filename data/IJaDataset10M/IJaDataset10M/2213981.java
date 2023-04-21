package openvend.xml;

import java.util.Map;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;
import openvend.main.OvLog;
import openvend.test.A_OvTestCase;
import org.apache.commons.logging.Log;

/**
 * @author Thomas Weckert
 * @version $Revision: 1.2 $
 */
public class OvXmlProcessingInstructionTestCase extends A_OvTestCase {

    private static Log log = OvLog.getLog(OvXmlProcessingInstructionTestCase.class);

    /**
     * Default JUnit constructor.<p>
     * 
     * @param arg0 JUnit parameters
     */
    public OvXmlProcessingInstructionTestCase(String arg0) {
        super(arg0);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.setName(OvXmlProcessingInstructionTestCase.class.getName());
        suite.addTest(new OvXmlProcessingInstructionTestCase("testParseXmlPi1"));
        suite.addTest(new OvXmlProcessingInstructionTestCase("testParseXmlPi2"));
        suite.addTest(new OvXmlProcessingInstructionTestCase("testParseXmlPi3"));
        suite.addTest(new OvXmlProcessingInstructionTestCase("testParseXmlPi4"));
        suite.addTest(new OvXmlProcessingInstructionTestCase("testParseXmlPi5"));
        return suite;
    }

    public void testParseXmlPi1() {
        try {
            echo("Test to parse XML processing instructions (1)...");
            String dataStr = "dummy=\"123\" test0\n\ntype=\"text/html\" href=\"#style\" charset=\"UTF-8\"\n\ttitle=\"It's so easy!\"\n  test1\n\ttest2";
            OvXmlProcessingInstruction pi = new OvXmlProcessingInstruction("xml-stylesheet", dataStr);
            Map params = pi.getParams();
            Set meta = pi.getMetaData();
            assertTrue(params.containsKey("dummy") && "123".equals(params.get("dummy")));
            assertTrue(params.containsKey("type") && "text/html".equals(params.get("type")));
            assertTrue(params.containsKey("charset") && "UTF-8".equals(params.get("charset")));
            assertTrue(params.containsKey("title") && "It's so easy!".equals(params.get("title")));
            assertTrue(meta.contains("test0") && meta.contains("test1") && meta.contains("test2"));
        } catch (Throwable t) {
            fail(t.getMessage());
            if (log.isErrorEnabled()) {
                log.error(t.getMessage(), t);
            }
        }
    }

    public void testParseXmlPi2() {
        try {
            echo("Test to parse XML processing instructions (2)...");
            String dataStr = "dummy=\"123\" test0\n\ntype =\"text/html\" href=\"#style\" charset=\"UTF-8\"\n\ttitle=\"It's so easy!\" test1 test2";
            boolean hasException = false;
            try {
                new OvXmlProcessingInstruction("xml-stylesheet", dataStr);
            } catch (Exception e) {
                hasException = true;
            }
            assertTrue(hasException);
        } catch (Throwable t) {
            fail(t.getMessage());
            if (log.isErrorEnabled()) {
                log.error(t.getMessage(), t);
            }
        }
    }

    public void testParseXmlPi3() {
        try {
            echo("Test to parse XML processing instructions (3)...");
            String dataStr = "dummy=\"123\" test0\n\ntype=  \"text/html\" href=\"#style\" charset=\"UTF-8\"\n\ttitle=\"It's so easy!\" test1 test2";
            boolean hasException = false;
            try {
                new OvXmlProcessingInstruction("xml-stylesheet", dataStr);
            } catch (Exception e) {
                hasException = true;
            }
            assertTrue(hasException);
        } catch (Throwable t) {
            fail(t.getMessage());
            if (log.isErrorEnabled()) {
                log.error(t.getMessage(), t);
            }
        }
    }

    public void testParseXmlPi4() {
        try {
            echo("Test to parse XML processing instructions (4)...");
            String dataStr = "dummy=\"123\" test0\n\ntype=\"text/html\"href=\"#style\" charset=\"UTF-8\"\n\ttitle=\"It's so easy!\" test1 test2";
            boolean hasException = false;
            try {
                new OvXmlProcessingInstruction("xml-stylesheet", dataStr);
            } catch (Exception e) {
                hasException = true;
            }
            assertTrue(hasException);
        } catch (Throwable t) {
            fail(t.getMessage());
            if (log.isErrorEnabled()) {
                log.error(t.getMessage(), t);
            }
        }
    }

    public void testParseXmlPi5() {
        try {
            echo("Test to parse XML processing instructions (5)...");
            String dataStr = "dummy=\"123\" test0\n\ntype=\"text/html\" href=\"#style\" charset=\"UTF-8\"\n\ttitle=\" \n It's\nso\t\teasy!  \"\n  test1\n\ttest2";
            OvXmlProcessingInstruction pi = new OvXmlProcessingInstruction("xml-stylesheet", dataStr);
            Map params = pi.getParams();
            Set meta = pi.getMetaData();
            assertTrue(params.containsKey("dummy") && "123".equals(params.get("dummy")));
            assertTrue(params.containsKey("type") && "text/html".equals(params.get("type")));
            assertTrue(params.containsKey("charset") && "UTF-8".equals(params.get("charset")));
            assertTrue(params.containsKey("title") && " \n It's\nso\t\teasy!  ".equals(params.get("title")));
            assertTrue(meta.contains("test0") && meta.contains("test1") && meta.contains("test2"));
        } catch (Throwable t) {
            fail(t.getMessage());
            if (log.isErrorEnabled()) {
                log.error(t.getMessage(), t);
            }
        }
    }
}
