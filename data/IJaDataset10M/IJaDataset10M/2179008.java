package org.progeeks.junit;

import java.util.*;
import org.progeeks.junit.ContextualTestCase.*;
import org.progeeks.util.TemplateExpressionProcessor.TemplateExpression;
import org.progeeks.util.beans.BeanConfigurator;
import junit.framework.*;

/**
 * Tests of the ContextualTestCase class.
 *
 * @version		$Revision: 1.6 $
 * @author		Paul Wisneskey
 */
public class ContextualTestCaseTests extends TestCase {

    public void testNoArgConstructor() {
        ContextualTestCase testCase = new ConcreteContextualTestCase();
        assertNotNull(testCase.getTestContext());
        assertTrue(testCase.getTestContext() instanceof DefaultTestContext);
        assertNotNull(testCase.toString());
    }

    public void testNameArgConstructor() {
        ContextualTestCase testCase = new ConcreteContextualTestCase("Test");
        assertNotNull(testCase.getTestContext());
        assertTrue(testCase.getTestContext() instanceof DefaultTestContext);
    }

    public void testSetGetTestContext() {
        ContextualTestCase testCase = new ConcreteContextualTestCase("Test");
        SimpleTestContext testContext = new SimpleTestContext();
        testCase.setTestContext(testContext);
        assertTrue(testContext == testCase.getTestContext());
        testCase.setTestContext(null);
        assertNull(testCase.getTestContext());
    }

    public void testSetGetTestContextFactory() {
        ContextualTestCase testCase = new ConcreteContextualTestCase("Test");
        BeanConfigurator configurator = new BeanConfigurator();
        testCase.setTestContextFactory(configurator);
        assertTrue(configurator == testCase.getTestContextFactory());
        testCase.setTestContextFactory(null);
        assertNull(testCase.getTestContextFactory());
    }

    public void testSetGetContextTests() {
        ContextualTestCase testCase = new ConcreteContextualTestCase("Test");
        EqualsTest test = new EqualsTest();
        test.setExpected("Bogus");
        test.setExpression("${testString}");
        List contextTests = Collections.singletonList(test);
        testCase.setContextTests(contextTests);
        assertEquals(contextTests, testCase.getContextTests());
        testCase.setContextTests(testCase.getContextTests());
        assertEquals(contextTests, testCase.getContextTests());
    }

    public void testSetUpNoFixture() {
        ContextualTestCase testCase = new ConcreteContextualTestCase();
        try {
            testCase.setUp();
        } catch (Exception e) {
            fail("Caught exception during setUp().");
        }
    }

    public void testSetUpWithFixture() {
        ContextualTestCase testCase = new ConcreteContextualTestCase();
        SimpleTestFixture fixture = new SimpleTestFixture();
        testCase.getTestContext().setTestFixture(fixture);
        try {
            testCase.setUp();
        } catch (Exception e) {
            fail("Caught exception during setUp().");
        }
        assertTrue(fixture.setUpInvoked);
    }

    public void testSetUpNoContext() {
        ContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setTestContext(null);
        boolean caughtException = false;
        try {
            testCase.setUp();
        } catch (Exception e) {
            caughtException = true;
        }
        assertTrue("Failed to catch expected exception.", caughtException);
    }

    public void testSetUpFactory() throws Exception {
        BeanConfigurator factory = new BeanConfigurator(SimpleTestContext.class);
        ContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setTestContext(null);
        testCase.setTestContextFactory(factory);
        testCase.setUp();
        assertNotNull(testCase.getTestContext());
    }

    public void testTearDownNoFixture() {
        ContextualTestCase testCase = new ConcreteContextualTestCase();
        try {
            testCase.tearDown();
        } catch (Exception e) {
            fail("Caught exception during tearDown().");
        }
    }

    public void testTearDownWithFixture() {
        ContextualTestCase testCase = new ConcreteContextualTestCase();
        SimpleTestFixture fixture = new SimpleTestFixture();
        testCase.getTestContext().setTestFixture(fixture);
        try {
            testCase.tearDown();
        } catch (Exception e) {
            fail("Caught exception during tearDown().");
        }
        assertTrue(fixture.tearDownInvoked);
    }

    public void testTearDownNoContext() {
        ContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setTestContext(null);
        boolean caughtException = false;
        try {
            testCase.tearDown();
        } catch (Exception e) {
            caughtException = true;
        }
        assertTrue("Failed to catch expected exception.", caughtException);
    }

    public void testAssertEqualsTemplate() throws Exception {
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setTestContext(new SimpleTestContext());
        testCase.setUp();
        testCase.testAssertEqualsTemplate();
        testCase.tearDown();
    }

    public void testAssertSameTemplate() throws Exception {
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setTestContext(new SimpleTestContext());
        testCase.setUp();
        testCase.testAssertSameTemplate();
        testCase.tearDown();
    }

    public void testAssertNotSameTemplate() throws Exception {
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setTestContext(new SimpleTestContext());
        testCase.setUp();
        testCase.testAssertNotSameTemplate();
        testCase.tearDown();
    }

    public void testAssertNullTemplate() throws Exception {
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setTestContext(new SimpleTestContext());
        testCase.setUp();
        testCase.testAssertNullTemplate();
        testCase.tearDown();
    }

    public void testAssertNotNullTemplate() throws Exception {
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setTestContext(new SimpleTestContext());
        testCase.setUp();
        testCase.testAssertNotNullTemplate();
        testCase.tearDown();
    }

    public void testAssertFalseTemplate() throws Exception {
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setTestContext(new SimpleTestContext());
        testCase.setUp();
        testCase.testAssertFalseTemplate();
        testCase.tearDown();
    }

    public void testAssertTrueTemplate() throws Exception {
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setTestContext(new SimpleTestContext());
        testCase.setUp();
        testCase.testAssertTrueTemplate();
        testCase.tearDown();
    }

    public void testContextTestGetSetMessage() {
        ContextTest test = new ContextTest() {

            public void test() {
            }
        };
        test.setMessage("Testing");
        assertEquals("Testing", test.getMessage());
    }

    public void testContextTestGetSetExpression() {
        TemplateExpression expression = new TemplateExpression("${testString}");
        ContextTest test = new ContextTest() {

            public void test() {
            }
        };
        test.setExpression(expression);
        assertEquals(expression, test.getExpression());
    }

    public void testEqualsContextTest() throws Throwable {
        EqualsTest test = new EqualsTest();
        test.setExpected("Test");
        test.setExpression("${testString}");
        assertEquals("Test", test.getExpected());
        assertNotNull(test.toString());
        List contextTests = Collections.singletonList(test);
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setName("testDummyTest");
        testCase.setTestContext(new SimpleTestContext());
        testCase.setContextTests(contextTests);
        testCase.setUp();
        testCase.runTest();
        testCase.tearDown();
    }

    public void testSameContextTest() throws Throwable {
        SameTest test = new SameTest();
        test.setExpected(SimpleTestContext.TEST_STRING);
        test.setExpression("${testString}");
        assertEquals("Test", test.getExpected());
        assertNotNull(test.toString());
        List contextTests = Collections.singletonList(test);
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setName("testDummyTest");
        testCase.setTestContext(new SimpleTestContext());
        testCase.setContextTests(contextTests);
        testCase.setUp();
        testCase.runTest();
        testCase.tearDown();
    }

    public void testNotSameContextTest() throws Throwable {
        NotSameTest test = new NotSameTest();
        test.setExpected(new String("Test"));
        test.setExpression("${testString}");
        assertEquals("Test", test.getExpected());
        assertNotNull(test.toString());
        List contextTests = Collections.singletonList(test);
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setName("testDummyTest");
        testCase.setTestContext(new SimpleTestContext());
        testCase.setContextTests(contextTests);
        testCase.setUp();
        testCase.runTest();
        testCase.tearDown();
    }

    public void testNullContextTest() throws Throwable {
        NullTest test = new NullTest();
        test.setExpression("${testNull}");
        assertNotNull(test.toString());
        List contextTests = Collections.singletonList(test);
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setName("testDummyTest");
        testCase.setTestContext(new SimpleTestContext());
        testCase.setContextTests(contextTests);
        testCase.setUp();
        testCase.runTest();
        testCase.tearDown();
    }

    public void testNotNullContextTest() throws Throwable {
        NotNullTest test = new NotNullTest();
        test.setExpression("${testString}");
        assertNotNull(test.toString());
        List contextTests = Collections.singletonList(test);
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setName("testDummyTest");
        testCase.setTestContext(new SimpleTestContext());
        testCase.setContextTests(contextTests);
        testCase.setUp();
        testCase.runTest();
        testCase.tearDown();
    }

    public void testFalseContextTest() throws Throwable {
        FalseTest test = new FalseTest();
        test.setExpression("${testFalse}");
        assertNotNull(test.toString());
        List contextTests = Collections.singletonList(test);
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setName("testDummyTest");
        testCase.setTestContext(new SimpleTestContext());
        testCase.setContextTests(contextTests);
        testCase.setUp();
        testCase.runTest();
        testCase.tearDown();
    }

    public void testTrueContextTest() throws Throwable {
        TrueTest test = new TrueTest();
        test.setExpression("${testTrue}");
        assertNotNull(test.toString());
        List contextTests = Collections.singletonList(test);
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setName("testDummyTest");
        testCase.setTestContext(new SimpleTestContext());
        testCase.setContextTests(contextTests);
        testCase.setUp();
        testCase.runTest();
        testCase.tearDown();
    }

    public void testFailsContextTest() throws Throwable {
        EqualsTest test = new EqualsTest();
        test.setExpected("Bogus");
        test.setExpression("${testString}");
        assertNotNull(test.toString());
        List contextTests = Collections.singletonList(test);
        ConcreteContextualTestCase testCase = new ConcreteContextualTestCase();
        testCase.setName("testDummyTest");
        testCase.setTestContext(new SimpleTestContext());
        testCase.setContextTests(contextTests);
        testCase.setUp();
        boolean caughtException = false;
        try {
            testCase.runTest();
        } catch (AssertionFailedError e) {
            caughtException = true;
        }
        testCase.tearDown();
        assertTrue("Failed to catch expected exception.", caughtException);
    }

    /**
     * Concrete implementation of the ContextualTestCase we can test with.
     */
    public static class ConcreteContextualTestCase extends ContextualTestCase {

        public ConcreteContextualTestCase() {
            super();
        }

        public ConcreteContextualTestCase(String name) {
            super(name);
        }

        public void testDummyTest() {
        }

        public void testAssertEqualsTemplate() {
            assertEquals("Test", new TemplateExpression("${testString}"));
            assertEquals("Some message.", "Test", new TemplateExpression("${testString}"));
            assertEquals(new Integer(6294), new TemplateExpression("${testInteger}"));
            assertEquals("Some message", new Integer(6294), new TemplateExpression("${testInteger}"));
        }

        public void testAssertSameTemplate() {
            SimpleTestContext context = (SimpleTestContext) getTestContext();
            assertSame(context.getTestString(), new TemplateExpression("${testString}"));
            assertSame("Some message.", context.getTestString(), new TemplateExpression("${testString}"));
            assertSame(context.getTestInteger(), new TemplateExpression("${testInteger}"));
            assertSame("Some message", context.getTestInteger(), new TemplateExpression("${testInteger}"));
        }

        public void testAssertNotSameTemplate() {
            assertNotSame(new String("Test"), new TemplateExpression("${testString}"));
            assertNotSame("Some message.", new String("Test"), new TemplateExpression("${testString}"));
            assertNotSame(new Integer(6294), new TemplateExpression("${testInteger}"));
            assertNotSame("Some message", new Integer(6294), new TemplateExpression("${testInteger}"));
        }

        public void testAssertNullTemplate() {
            assertNull(new TemplateExpression("${testNull}"));
            assertNull("Some message.", new TemplateExpression("${testNull}"));
        }

        public void testAssertNotNullTemplate() {
            assertNotNull(new TemplateExpression("${testString}"));
            assertNotNull("Some message.", new TemplateExpression("${testString}"));
        }

        public void testAssertFalseTemplate() {
            assertFalse(new TemplateExpression("${testFalse}"));
            assertFalse("Some message.", new TemplateExpression("${testFalse}"));
            assertFalse(new TemplateExpression("${testFalseString}"));
            assertFalse("Some message.", new TemplateExpression("${testFalseString}"));
        }

        public void testAssertTrueTemplate() {
            assertTrue(new TemplateExpression("${testTrue}"));
            assertTrue("Some message.", new TemplateExpression("${testTrue}"));
            assertTrue(new TemplateExpression("${testTrueString}"));
            assertTrue("Some message.", new TemplateExpression("${testTrueString}"));
        }
    }

    /**
     * Basic test context implementation to use for testing.
     */
    public static class SimpleTestContext extends DefaultTestContext {

        public static final String TEST_STRING = "Test";

        public static final Integer TEST_INTEGER = new Integer(6294);

        public SimpleTestContext() {
        }

        public String getTestString() {
            return TEST_STRING;
        }

        public Integer getTestInteger() {
            return TEST_INTEGER;
        }

        public Object getTestNull() {
            return null;
        }

        public boolean getTestFalse() {
            return false;
        }

        public String getTestFalseString() {
            return Boolean.FALSE.toString();
        }

        public boolean getTestTrue() {
            return true;
        }

        public String getTestTrueString() {
            return Boolean.TRUE.toString();
        }
    }

    /**
     * Basic test fixture that remembers if its setUp() and tearDown()
     * methods were invoked.
     */
    public static class SimpleTestFixture implements TestFixture {

        protected boolean setUpInvoked = false;

        protected boolean tearDownInvoked = false;

        public void setUp(TestContext context) throws Exception {
            setUpInvoked = true;
        }

        public void tearDown(TestContext context) throws Exception {
            tearDownInvoked = true;
        }
    }
}
