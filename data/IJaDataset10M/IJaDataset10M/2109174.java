package org.unitils.mock.core;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import static org.unitils.mock.MockUnitils.notNull;
import static org.unitils.mock.MockUnitils.refEq;
import static org.unitils.mock.core.ArgumentMatcherPositionFinder.getArgumentMatcherIndexes;
import static org.unitils.reflectionassert.ReflectionAssert.assertRefEquals;
import static org.unitils.util.ReflectionUtils.getMethod;
import java.lang.reflect.Method;
import static java.util.Arrays.asList;
import java.util.List;

/**
 * Tests the finding of the argument matchers in a proxy method invocation.
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 * @author Kenny Claes
 */
@SuppressWarnings({ "UnusedDeclaration" })
public class ArgumentMatcherPositionFinderTest extends UnitilsJUnit4 {

    private int invocationLineNr = 153;

    private int noMatcherInvocationLineNr = 155;

    private int doubleInvocationLineNr = 157;

    private int staticInvocationLineNr = 159;

    private int noArgumentsInvocationLineNr = 161;

    private Method proxyMethod;

    private Method staticProxyMethod;

    private Method noArgumentsProxyMethod;

    /**
     * Initializes the proxy methods
     */
    @Before
    public void setUp() {
        proxyMethod = getMethod(TestProxy.class, "someMethod", false, String.class, String.class, String.class);
        staticProxyMethod = getMethod(TestProxy.class, "someStaticMethod", true, Integer.TYPE, Integer.TYPE);
        noArgumentsProxyMethod = getMethod(TestProxy.class, "someMethod", false);
    }

    /**
     * Test finding matchers for a proxy method invocation with the first and third arguments being an argument matcher.
     */
    @Test
    public void testGetArgumentMatcherIndexes() {
        List<Integer> result = getArgumentMatcherIndexes(TestClass.class, "test", proxyMethod, invocationLineNr, 1);
        assertRefEquals(asList(0, 2), result);
    }

    /**
     * Test finding matchers for a proxy method invocation without argument matcher.
     */
    @Test
    public void testGetArgumentMatcherIndexes_noArgumentMatchers() {
        List<Integer> result = getArgumentMatcherIndexes(TestClass.class, "test", proxyMethod, noMatcherInvocationLineNr, 1);
        assertTrue(result.isEmpty());
    }

    /**
     * Test finding matchers for a proxy method that has no arguments.
     */
    @Test
    public void testGetArgumentMatcherIndexes_noArguments() {
        List<Integer> result = getArgumentMatcherIndexes(TestClass.class, "test", noArgumentsProxyMethod, noArgumentsInvocationLineNr, 1);
        assertTrue(result.isEmpty());
    }

    /**
     * Test finding matchers for two method invocations on the same line. The index determines which one.
     */
    @Test
    public void testGetArgumentMatcherIndexes_twoInvocationsOnSameLine() {
        List<Integer> firstInvocationResult = getArgumentMatcherIndexes(TestClass.class, "test", proxyMethod, doubleInvocationLineNr, 1);
        List<Integer> secondInvocationResult = getArgumentMatcherIndexes(TestClass.class, "test", proxyMethod, doubleInvocationLineNr, 2);
        assertRefEquals(asList(0), firstInvocationResult);
        assertRefEquals(asList(2), secondInvocationResult);
    }

    /**
     * Test finding matchers for a static proxy method invocation.
     */
    @Test
    public void testGetArgumentMatcherIndexes_staticInvocation() {
        List<Integer> result = getArgumentMatcherIndexes(TestClass.class, "test", staticProxyMethod, staticInvocationLineNr, 1);
        assertRefEquals(asList(0), result);
    }

    /**
     * Test for trying to find matchers on a non-existing method.
     */
    @Test
    public void testGetArgumentMatcherIndexes_wrongMethodName() {
        List<Integer> result = getArgumentMatcherIndexes(TestClass.class, "xxxx", proxyMethod, invocationLineNr, 1);
        assertNull(result);
    }

    /**
     * Test for trying to find matchers on a wrong line.
     */
    @Test
    public void testGetArgumentMatcherIndexes_wrongLineNr() {
        List<Integer> result = getArgumentMatcherIndexes(TestClass.class, "test", proxyMethod, invocationLineNr, 9999);
        assertNull(result);
    }

    /**
     * Test class with 5 proxy method invocations.
     */
    public static class TestClass {

        private TestProxy testProxy = new TestProxy();

        public void test() {
            testProxy.someMethod(notNull(String.class), "aValue", notNull(String.class));
            testProxy.someMethod("aValue", "aValue", "aValue");
            testProxy.someMethod(notNull(String.class), "aValue", "aValue");
            testProxy.someMethod("aValue", "aValue", notNull(String.class));
            TestProxy.someStaticMethod(refEq(1), 33);
            testProxy.someMethod();
        }
    }

    /**
     * Simulates a proxy
     */
    public static class TestProxy {

        public void someMethod(String value1, String value2, String value3) {
        }

        public static void someStaticMethod(int value1, int value2) {
        }

        public void someMethod() {
        }
    }
}
