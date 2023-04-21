package org.jxul.swing;

import junit.framework.TestCase;
import org.jxul.XulBuilder;
import org.jxul.XulBuilderFactory;
import org.jxul.XulDocument;
import org.jxul.XulWindow;

/**
 * @author Will Etson
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class XulSvgTest extends TestCase {

    XulDocument doc;

    protected void setUp() throws Exception {
        XulBuilderFactory factory = (XulBuilderFactory) XulBuilderFactory.newInstance();
        XulBuilder builder = (XulBuilder) factory.newDocumentBuilder();
        doc = (XulDocument) builder.parse("chrome:///XulSvgTest.xul");
        Thread.sleep(5000);
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetById() {
        XulWindow window = (XulWindow) doc.getElementById("windowId");
        XulSvgTest.assertNotNull(window);
        System.out.println(window.getTitle());
        window.setCursor("wait");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testNavigation() {
        XulWindow window = (XulWindow) doc.getDocumentElement();
        XulSvgTest.assertNotNull(window);
        System.out.println(window.getTitle());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
