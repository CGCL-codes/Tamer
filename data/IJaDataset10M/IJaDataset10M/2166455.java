package org.jxul.swing;

import junit.framework.TestCase;
import org.jxul.XulBuilder;
import org.jxul.XulBuilderFactory;

/**
 * @author Will Etson
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XulXsltTest extends TestCase {

    public void testTransform() throws Exception {
        XulBuilderFactory factory = (XulBuilderFactory) XulBuilderFactory.newInstance();
        XulBuilder builder = (XulBuilder) factory.newDocumentBuilder();
        builder.parse("chrome:///XulXsltTest.xul");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
