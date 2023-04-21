package org.jxul.framework;

import junit.framework.TestCase;
import org.jxul.XulBuilder;
import org.jxul.XulBuilderFactory;
import org.jxul.XulButton;
import org.jxul.XulDocument;

/**
 * @author Will Etson
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class HtmlAppletImplTest extends TestCase {

    XulDocument doc;

    /**
	 * @see TestCase#setUp()
	 */
    protected void setUp() throws Exception {
        XulBuilderFactory factory = (XulBuilderFactory) XulBuilderFactory.newInstance();
        XulBuilder builder = (XulBuilder) factory.newDocumentBuilder();
        doc = (XulDocument) builder.parse("chrome:///HtmlAppletTest.xul");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetById() {
    }
}
