package org.jxul.swing;

import junit.framework.TestCase;
import org.jxul.XulBuilder;
import org.jxul.XulBuilderFactory;
import org.jxul.XulDocument;
import org.jxul.XulProgressMeter;

/**
 * @author Will Etson
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class XulRadioTest extends TestCase {

    XulDocument doc;

    protected void setUp() throws Exception {
        XulBuilderFactory factory = (XulBuilderFactory) XulBuilderFactory.newInstance();
        XulBuilder builder = (XulBuilder) factory.newDocumentBuilder();
        doc = (XulDocument) builder.parse("chrome:///XulRadioTest.xul");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetById() {
        try {
            XulProgressMeter pm = (XulProgressMeter) doc.getElementById("prog-meter");
            assertNotNull(pm);
            System.out.println(pm.getId());
            Thread.sleep(1000);
            pm.setMode("determined");
            for (int i = 0; i < 100; i += 5) {
                Thread.sleep(1000);
                pm.setValue(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
