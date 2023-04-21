package org.nextmock.model.basic;

import org.nextmock.model.basic.BasicMockableMethod;
import junit.framework.TestCase;

public class BasicMockableMethodTest extends TestCase {

    public void testNotNullList() {
        BasicMockableMethod t = new BasicMockableMethod();
        assertNotNull(t.getArguments());
        assertNotNull(t.getExceptions());
    }

    public void testEquals() {
        BasicMockableMethod m1 = new BasicMockableMethod();
        BasicMockableMethod m2 = new BasicMockableMethod();
        BasicMockableMethod m3 = new BasicMockableMethod();
        m1.setName("methodName");
        m2.setName("methodName");
        m3.setName("methodName");
        BasicMethodArgument arg1 = new BasicMethodArgument();
        m1.addArgument(arg1);
        m2.addArgument(arg1);
        m3.addArgument(arg1);
        m3.addArgument(arg1);
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
        assertFalse(m1.equals(m3));
    }
}
