package net.sourceforge.freejava.jvm.stack;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

public class CallerTest extends TestCase {

    @Test
    public void test_neg2_SunReflect() {
        assertEquals("sun.reflect.Reflection", Caller.getCallerClass(-2).getName());
    }

    @Test
    public void test_neg1_JvmhackCaller() {
        assertEquals(Caller.class, Caller.getCallerClass(-1));
    }

    @Test
    public void test_0_ThisTestMethod() {
        assertEquals(CallerTest.class, Caller.getCallerClass(0));
    }

    @Ignore
    @Test
    public void test_1_TestManagerLikeJUnitAndAll() {
        String tester = Caller.getCallerClass(1).getName().toLowerCase();
        assertTrue(tester.contains("junitx") || tester.contains("test"));
    }

    @Test
    public void testStackTrace() {
        StackTraceElement stack = Caller.stackTrace(0);
        Class<?> clazz = getClass();
        assertEquals(clazz.getName(), stack.getClassName());
        assertEquals("testStackTrace", stack.getMethodName());
    }

    static class NestRunnable implements Runnable {

        Runnable nestedRunnable;

        public NestRunnable(Runnable nestedRunnable) {
            this.nestedRunnable = nestedRunnable;
        }

        @Override
        public void run() {
            nestedRunnable.run();
        }
    }

    @Test
    public void testCallerLibImpl() throws Exception {
        Class<?> callerSelf = Caller.getCallerClass(-1);
        assertEquals(Caller.class, callerSelf);
    }

    @Test
    public void testCallerSelf() throws Exception {
        Class<?> callerSelf = Caller.getCallerClass(0);
        assertEquals(getClass(), callerSelf);
    }

    public void testNest1() throws Exception {
        NestRunnable outer = new NestRunnable(new Runnable() {

            @Override
            public void run() {
                assertEquals(NestRunnable.class, Caller.getCallerClass(1));
            }
        });
        outer.run();
    }
}
