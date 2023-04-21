package tigerunit.framework;

import junit.framework.TestCase;
import java.util.Properties;
import java.util.Arrays;
import java.lang.reflect.Method;
import java.io.File;
import tigerunit.util.Executor;
import tigerunit.util.ConfigurableClassLoader;
import tigerunit.framework.mock.MockTestCase;

public class TestSessionTest extends TestCase {

    public TestSessionTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.setUp();
        if (TestSession.getInstance().isBegun()) {
            TestSession.getInstance().end();
        }
    }

    public void testCreate() throws Throwable {
        Properties oldProperties = System.getProperties();
        MockExecutor executor = new MockExecutor();
        TestSession session = TestSession.getInstance();
        session.setExecutor(executor);
        session.setClasspath(null);
        session.setSystemProperties(null);
        session.begin();
        assertFalse(session.isFork());
        assertEquals(Thread.currentThread().getContextClassLoader(), session.getClassLoader());
        assertEquals(oldProperties, System.getProperties());
        session.execute(null, null);
        assertTrue(executor.executed);
    }

    public void testCustomSysProperties() {
        Properties oldProperties = System.getProperties();
        MockExecutor executor = new MockExecutor();
        Properties customProps = new Properties();
        customProps.put("foo", "bar");
        TestSession session = TestSession.getInstance();
        session.setExecutor(executor);
        session.setClasspath(null);
        session.setSystemProperties(customProps);
        session.begin();
        Properties modifiedProps = System.getProperties();
        assertEquals("bar", modifiedProps.get("foo"));
        assertFalse(oldProperties.equals(modifiedProps));
        session.end();
        assertEquals(System.getProperties(), oldProperties);
    }

    public void testCustomClasspath() {
        MockExecutor executor = new MockExecutor();
        TestSession session = TestSession.getInstance();
        session.setExecutor(executor);
        session.setClasspath(Arrays.asList(new String[] { "." }));
        session.setSystemProperties(null);
        session.begin();
        assertTrue(session.getClassLoader() instanceof ConfigurableClassLoader);
        ConfigurableClassLoader cl = (ConfigurableClassLoader) session.getClassLoader();
        assertEquals(new File(".").getAbsolutePath(), cl.getClasspath());
    }

    public void testFork() throws Throwable {
        TestSession session = TestSession.getInstance();
        session.setFork(true);
        session.begin();
        Method m = MockTestCase.class.getMethod("testSuccess", new Class[0]);
        session.execute(m, null);
    }

    private class MockExecutor implements Executor {

        boolean executed = false;

        public void execute(Method method, Object target) throws Throwable {
            executed = true;
        }

        public void reset() {
        }
    }
}
