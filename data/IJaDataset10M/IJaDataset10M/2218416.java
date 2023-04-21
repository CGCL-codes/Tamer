package org.dyndns.fichtner.jarrunner;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

@SuppressWarnings("nls")
public class ClassUtilTest {

    @Test
    public void testString() {
        assertEquals("java.lang.String", ClassUtil.getClassname(String.class));
        assertEquals("java.lang.String[]", ClassUtil.getClassname(String[].class));
        assertEquals("java.lang.String[][]", ClassUtil.getClassname(String[][].class));
        assertEquals("java.lang.String[][][]", ClassUtil.getClassname(String[][][].class));
    }
}
