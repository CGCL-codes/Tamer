package pl.rzarajczyk.utils.junit;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import pl.rzarajczyk.utils.system.os.OperatingSystemType;

/**
 *
 * @author Rafal
 */
@Ignore
public class OsAwareTestRunnerTest {

    private OsAwareTestRunner runner;

    @Before
    public void setUp() throws InitializationError {
        runner = new OsAwareTestRunner(TestClass.class);
    }

    @Test
    public void testOnMac() throws NoSuchMethodException {
        runner.setCurrentOs(OperatingSystemType.MAC);
        List<Method> methods = Collections.singletonList(TestClass.class.getMethod("mac"));
        Assert.assertEquals(methods, runner.computeTestMethods());
    }

    @Test
    public void testOnWin() throws NoSuchMethodException {
        runner.setCurrentOs(OperatingSystemType.WINDOWS);
        List<Method> methods = Collections.singletonList(TestClass.class.getMethod("win"));
        Assert.assertEquals(methods, runner.computeTestMethods());
    }

    @Test
    public void testOnUnix() throws NoSuchMethodException {
        runner.setCurrentOs(OperatingSystemType.UNIX);
        List<Method> methods = Collections.emptyList();
        Assert.assertEquals(methods, runner.computeTestMethods());
    }

    @Test
    public void testOnUnknown() throws NoSuchMethodException {
        runner.setCurrentOs(OperatingSystemType.UNKNOWN);
        List<Method> methods = Collections.emptyList();
        Assert.assertEquals(methods, runner.computeTestMethods());
    }

    @RunOnlyOn({ OperatingSystemType.MAC, OperatingSystemType.UNIX, OperatingSystemType.WINDOWS })
    static class TestClass {

        @RunOnlyOn(OperatingSystemType.MAC)
        @Test
        public void mac() {
        }

        @RunOnlyOn(OperatingSystemType.WINDOWS)
        @Test
        public void win() {
        }
    }
}
