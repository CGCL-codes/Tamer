package com.sitechasia.webx.components.jmx.util;

import junit.framework.TestCase;
import com.sitechasia.webx.components.jmx.JMXRuntimeException;
import com.sitechasia.webx.components.jmx.util.MBeanSwither;
import com.sitechasia.webx.components.jmx.util.MBeanUtil;

public class MBeanUtilTest extends TestCase {

    public void testAddCallback() {
        MBeanUtil util = new MBeanUtil(new EchoClass());
        MBeanSwither switcher = new MBeanSwither();
        switcher.setSwitcher(true);
        EchoInterface obj = (EchoInterface) util.addCallback(switcher).getMBean();
        assertNotNull(obj.echo("test string"));
        switcher.setSwitcher(false);
        try {
            obj.echo("test string");
            fail("未产生异常");
        } catch (JMXRuntimeException e) {
        }
    }
}
