package org.jxul.swing;

import junit.framework.TestCase;
import org.jxul.XulBuilder;
import org.jxul.XulBuilderFactory;
import org.jxul.XulDocument;
import org.jxul.XulTextBox;

/**
 * @author Will Etson
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class XulTextBoxTest extends TestCase {

    XulDocument doc;

    protected void setUp() throws Exception {
        XulBuilderFactory factory = (XulBuilderFactory) XulBuilderFactory.newInstance();
        XulBuilder builder = (XulBuilder) factory.newDocumentBuilder();
        XulDocument doc = (XulDocument) builder.parse("chrome:///XulTextBoxTest.xul");
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
        XulTextBox pm = (XulTextBox) doc.getElementById("textbox1");
        XulTextBoxTest.assertNotNull(pm);
        System.out.println(pm.getId());
    }
}
