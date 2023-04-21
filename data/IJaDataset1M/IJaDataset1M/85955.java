package org.coinjema.util;

import junit.framework.TestCase;
import org.coinjema.nontest.BasicObject;

/**
 * @author mstover
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class NormalConstructionTest extends TestCase {

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public NormalConstructionTest() {
    }

    /**
    * @param arg0
    */
    public NormalConstructionTest(String arg0) {
        super(arg0);
    }

    public void testNormalConstruction() throws Exception {
        for (int i = 0; i < 1000; i++) {
            assertEquals(BasicObject.class, new BasicObject().getClass());
            BasicObject period = new BasicObject();
            period.setPaths(new String[] { "path1", "path2" });
            assertEquals(BasicObject.class, period.getClass());
            assertEquals("path2", period.getPaths()[1]);
            period = new BasicObject();
            period.setPaths(new String[] { "path1", "path2" });
            assertEquals(BasicObject.class, period.getClass());
            assertEquals("path1", period.getPaths()[0]);
        }
    }
}
