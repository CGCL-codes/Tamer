package com.volantis.xml.jdom;

import java.lang.reflect.Method;

/**
 * Checks that the SimpleVisitor returns the correct value from its nextHeader
 * methods.
 */
public class SimpleVisitorTestCase extends VisitorTestAbstract {

    /**
     * This test uses reflection to ensure that the {@link SimpleVisitor}
     * implements all nextHeader methods to return {@link Visitor.Action#CONTINUE
     * CONTINUE}.
     */
    public void testVisit() throws Exception {
        Method[] methods = Visitor.class.getMethods();
        Visitor visitor = new SimpleVisitor();
        Object[] args = { null };
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.getName().equals("nextHeader") && (method.getParameterTypes().length == 1)) {
                Object result = method.invoke(visitor, args);
                assertTrue("result of call to " + method + " not a Visitor.Action", result instanceof Visitor.Action);
                assertSame(method + " didn't return expected value", Visitor.Action.CONTINUE, result);
            }
        }
    }
}
