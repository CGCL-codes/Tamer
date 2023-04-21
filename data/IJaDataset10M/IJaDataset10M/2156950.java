package org.jxul.framework;

import junit.framework.TestCase;
import org.jxul.XulBuilder;
import org.jxul.XulBuilderFactory;
import org.jxul.XulDocument;
import org.jxul.XulLabel;

/**
 * @author Will Etson
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class XulLabelImplTest extends TestCase {

    XulDocument doc;

    protected void setUp() throws Exception {
        XulBuilderFactory factory = (XulBuilderFactory) XulBuilderFactory.newInstance();
        XulBuilder builder = (XulBuilder) factory.newDocumentBuilder();
        doc = (XulDocument) builder.parse("chrome:///XulLabelTest.xul");
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
        XulLabel label = (XulLabel) doc.getElementById("label1");
        XulLabelImplTest.assertNotNull(label);
        System.out.println(label.getId());
    }
}
