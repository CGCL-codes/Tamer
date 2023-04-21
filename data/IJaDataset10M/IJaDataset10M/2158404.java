package org.apache.harmony.beans.tests.java.beans;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.Statement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import junit.framework.TestCase;

/**
 * Test some customized persistence delegate, not including the AWT and SWING
 * ones.
 */
public class CustomizedPersistenceDelegateTest extends TestCase {

    private MockEncoder enc = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        enc = new MockEncoder();
    }

    public void testStringPD() {
        enc.writeObject("");
        assertEquals("", enc.get(""));
    }

    public void testArrayPD() {
        Integer[] ia = new Integer[] { new Integer(1), new Integer(11100) };
        enc.writeObject(ia);
        assertTrue(Arrays.equals(ia, (Integer[]) enc.get(ia)));
    }

    public void testClassPD() {
        enc.writeObject(Integer.class);
        assertSame(Integer.class, enc.get(Integer.class));
        enc.writeObject(String.class);
        assertSame(String.class, enc.get(String.class));
        enc.writeObject(Class.class);
        assertSame(Class.class, enc.get(Class.class));
    }

    public void testProxyPD() throws Exception {
        MyInterface o = (MyInterface) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[] { MyInterface.class }, new MyHandler(1));
        PersistenceDelegate pd = new PersistenceDelegate() {

            @Override
            public Expression instantiate(Object o, Encoder e) {
                return new Expression(o, ClassLoader.class, "getSystemClassLoader", null);
            }
        };
        enc.setPersistenceDelegate(ClassLoader.getSystemClassLoader().getClass(), pd);
        enc.writeObject(o);
        MyInterface o2 = (MyInterface) enc.get(o);
        assertEquals(o.getProp(), o2.getProp());
    }

    public void testPrimitivePD() {
        int[] ia = new int[] { 1, 2 };
        enc.writeObject(ia);
        assertTrue(Arrays.equals(ia, (int[]) enc.get(ia)));
        ia = new int[0];
        enc.writeObject(ia);
        assertTrue(Arrays.equals(ia, (int[]) enc.get(ia)));
        short[] sa = new short[] { 1, 2 };
        enc.writeObject(sa);
        assertTrue(Arrays.equals(sa, (short[]) enc.get(sa)));
        sa = new short[0];
        enc.writeObject(sa);
        assertTrue(Arrays.equals(sa, (short[]) enc.get(sa)));
        long[] la = new long[] { 1, 2 };
        enc.writeObject(la);
        assertTrue(Arrays.equals(la, (long[]) enc.get(la)));
        la = new long[0];
        enc.writeObject(la);
        assertTrue(Arrays.equals(la, (long[]) enc.get(la)));
        float[] fa = new float[] { 1, 2 };
        enc.writeObject(fa);
        assertTrue(Arrays.equals(fa, (float[]) enc.get(fa)));
        fa = new float[0];
        enc.writeObject(fa);
        assertTrue(Arrays.equals(fa, (float[]) enc.get(fa)));
        double[] da = new double[] { -1, 2.0 };
        enc.writeObject(da);
        assertTrue(Arrays.equals(da, (double[]) enc.get(da)));
        da = new double[0];
        enc.writeObject(da);
        assertTrue(Arrays.equals(da, (double[]) enc.get(da)));
        boolean[] ba = new boolean[] { true, false };
        enc.writeObject(ba);
        assertTrue(Arrays.equals(ba, (boolean[]) enc.get(ba)));
        ba = new boolean[0];
        enc.writeObject(ba);
        assertTrue(Arrays.equals(ba, (boolean[]) enc.get(ba)));
        char[] ca = new char[] { 'c', 'b' };
        enc.writeObject(ca);
        assertTrue(Arrays.equals(ca, (char[]) enc.get(ca)));
        ca = new char[0];
        enc.writeObject(ca);
        assertTrue(Arrays.equals(ca, (char[]) enc.get(ca)));
        byte[] bba = new byte[] { 112, 12 };
        enc.writeObject(bba);
        assertTrue(Arrays.equals(bba, (byte[]) enc.get(bba)));
        bba = new byte[0];
        enc.writeObject(bba);
        assertTrue(Arrays.equals(bba, (byte[]) enc.get(bba)));
    }

    public void testMethodPD() throws Exception {
        Method m = Object.class.getMethod("getClass", (Class[]) null);
        enc.writeObject(m);
        assertEquals(m, enc.get(m));
    }

    public void testFieldPD() throws Exception {
        Field f = MyHandler.class.getField("i");
        enc.writeObject(f);
        assertEquals(f, enc.get(f));
    }

    static class MockEncoder extends Encoder {

        @Override
        public void writeObject(Object o) {
            super.writeObject(o);
        }

        @Override
        public PersistenceDelegate getPersistenceDelegate(Class<?> type) {
            return super.getPersistenceDelegate(type);
        }

        @Override
        public void writeExpression(Expression oldExp) {
            super.writeExpression(oldExp);
        }

        @Override
        public void writeStatement(Statement oldStat) {
            if (oldStat.getMethodName().equals("add")) {
                new Throwable().printStackTrace();
            }
            super.writeStatement(oldStat);
        }

        @Override
        public Object get(Object o) {
            return super.get(o);
        }
    }

    public static interface MyInterface {

        void setProp(int i);

        int getProp();
    }

    public static class MyHandler implements InvocationHandler {

        public int i;

        public MyHandler() {
            this.i = 0;
        }

        public MyHandler(int i) {
            this.i = i;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("setProp")) {
                this.i = ((Integer) args[0]).intValue();
                return null;
            } else if (method.getName().equals("getProp")) {
                return new Integer(i);
            } else if (method.getName().equals("hashCode")) {
                return Integer.valueOf(this.hashCode() + 113);
            } else if (method.getName().equals("equals") && args.length == 1) {
                return Boolean.valueOf(proxy.getClass().getName().equals(args[0].getClass().getName()));
            }
            return null;
        }

        public void setI(int i) {
            this.i = i;
        }

        public int getI() {
            return this.i;
        }
    }

    public static class MockBean {

        String str;

        public MockBean() {
            this.str = "";
        }

        public String getStr() {
            return str;
        }

        public void addStr(String s) {
            str += s;
        }

        public MockBean side() {
            str += "side";
            return new MockBean();
        }

        public MockBean side2() {
            str += "side2";
            return new MockBean();
        }

        @Override
        public String toString() {
            return str;
        }
    }
}
