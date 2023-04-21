package org.unitils;

import org.junit.*;
import static org.unitils.TracingTestListener.TestInvocation.TEST_BEFORE_CLASS;
import static org.unitils.TracingTestListener.TestInvocation.TEST_SET_UP;
import static org.unitils.TracingTestListener.TestInvocation.TEST_METHOD;
import static org.unitils.TracingTestListener.TestInvocation.TEST_TEAR_DOWN;
import static org.unitils.TracingTestListener.TestInvocation.TEST_AFTER_CLASS;

/**
 * JUnit 4 test class containing 2 test methods. This test test-class is used in the {@link JUnitUnitilsInvocationTest} tests.
 *
 * @author Tim Ducheyne
 */
public class UnitilsJUnit4Test_TestClass2 extends UnitilsJUnit4TestBase {

    @BeforeClass
    public static void beforeClass() {
        registerTestInvocation(TEST_BEFORE_CLASS, UnitilsJUnit4Test_TestClass2.class, null);
    }

    @AfterClass
    public static void afterClass() {
        registerTestInvocation(TEST_AFTER_CLASS, UnitilsJUnit4Test_TestClass2.class, null);
    }

    @Before
    public void setUp() {
        registerTestInvocation(TEST_SET_UP, this.getClass(), null);
    }

    @After
    public void tearDown() {
        registerTestInvocation(TEST_TEAR_DOWN, this.getClass(), null);
    }

    @Test
    public void test1() {
        registerTestInvocation(TEST_METHOD, this.getClass(), "test1");
    }

    @Test
    public void test2() {
        registerTestInvocation(TEST_METHOD, this.getClass(), "test2");
    }
}
