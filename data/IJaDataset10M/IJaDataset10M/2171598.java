package org.dom4j;

import junit.textui.TestRunner;
import org.dom4j.io.SAXReader;

/**
 * A test harness for validation when using SAXReader
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.3 $
 */
public class ValidationTest extends AbstractTestCase {

    public static void main(String[] args) {
        TestRunner.run(ValidationTest.class);
    }

    public void testValidation() throws Exception {
        try {
            SAXReader reader = new SAXReader(true);
            reader.read("test");
            fail();
        } catch (DocumentException e) {
        }
    }
}
