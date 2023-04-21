package org.zkoss.lang;

import junit.framework.*;
import java.util.*;
import java.lang.reflect.*;

public class InvokeTest extends TestCase {

    public static final int LOOP = 1000000;

    private A a = new A();

    private final Map map = new HashMap();

    public InvokeTest(String name) {
        super(name);
        map.put(a, "a1");
        map.put(new A(), "a2");
        map.put(new A(), "a3");
    }

    public static Test suite() {
        return new TestSuite(InvokeTest.class);
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void testDirectSpeed() throws Exception {
        for (int j = LOOP; --j >= 0; ) a.f();
    }

    public void testInterfaceSpeed() throws Exception {
        I i = a;
        for (int j = LOOP; --j >= 0; ) i.f();
    }

    public void testReflectSpeed() throws Exception {
        Method m = I.class.getMethod("f", null);
        for (int j = LOOP; --j >= 0; ) m.invoke(a, null);
    }

    public void testProxySpeed() throws Exception {
        final I i = (I) Proxy.newProxyInstance(A.class.getClassLoader(), new Class[] { I.class }, new Invoker(a));
        for (int j = LOOP; --j >= 0; ) i.f();
    }

    public void testHashSpeed() throws Exception {
        for (int j = LOOP; --j >= 0; ) map.get(a);
    }

    public void testIdentitySpeed() throws Exception {
        for (int j = LOOP; --j >= 0; ) System.identityHashCode(a);
    }

    public static interface I {

        public void f();
    }

    static class A implements I {

        A() {
        }

        public void f() {
        }
    }

    static class Invoker implements InvocationHandler {

        I _i;

        public Invoker(I i) {
            _i = i;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(_i, args);
        }
    }
}
