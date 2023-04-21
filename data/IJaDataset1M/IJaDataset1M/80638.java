package org.archive.util.jmx;

import javax.management.openmbean.OpenMBeanOperationInfoSupport;

public class OpenMBeanInvocationManagerTest extends MBeanTestCase {

    public void test() {
        OpenMBeanInvocationManager m = new OpenMBeanInvocationManager();
        OpenMBeanOperationInfoSupport info = createInfo();
        m.addMBeanOperation(new SimpleReflectingMBeanOperation(this, info));
        try {
            String test = (String) m.invoke(info.getName(), new Object[0], new String[0]);
            assertEquals("test", test);
        } catch (Exception e) {
            assertFalse(true);
            e.printStackTrace();
        }
    }
}
