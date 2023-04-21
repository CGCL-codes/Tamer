package org.jxul.swing;

import junit.framework.TestCase;
import org.jxul.XulBuilder;
import org.jxul.XulBuilderFactory;
import org.jxul.XulDocument;
import org.jxul.XulListItem;

/**
 * @author Will Etson
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class XulListItemTest extends TestCase {

    XulDocument doc;

    protected void setUp() throws Exception {
        XulBuilderFactory factory = (XulBuilderFactory) XulBuilderFactory.newInstance();
        XulBuilder builder = (XulBuilder) factory.newDocumentBuilder();
        doc = (XulDocument) builder.parse("chrome:///XulListItemTest.xul");
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetById() {
        XulListItem listItem = (XulListItem) doc.getElementById("li1");
        XulListItemTest.assertNotNull(listItem);
        System.out.println(listItem.getValue());
    }
}
