package org.coinjema.util;

import java.net.URL;
import java.util.Enumeration;
import java.util.Random;
import junit.framework.TestCase;

/**
 * @author mstover
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DataFlowTest extends TestCase {

    /**
    * @param arg0
    */
    public DataFlowTest(String arg0) {
        super(arg0);
    }

    public void testDataFlow() throws Exception {
        try {
            final DataFlowCell cell1 = new DataFlowCell();
            final StringBuffer buf = new StringBuffer("1");
            Thread thread1 = new Thread() {

                public void run() {
                    buf.append(cell1.getValue());
                }
            };
            thread1.start();
            try {
                Thread.sleep((long) (new Random().nextFloat() * 2000));
            } catch (Exception e) {
            }
            buf.append("2");
            cell1.setValue("3");
            Thread.sleep(500);
            assertEquals("123", buf.toString());
        } catch (Exception e) {
            System.out.println("hey, error " + e);
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testClassLoader() throws Exception {
        System.out.println(Thread.currentThread().getContextClassLoader().getResource("org/aspectj"));
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("");
        while (urls.hasMoreElements()) {
            URL u = urls.nextElement();
            System.out.println(u);
        }
    }
}
