package net.sf.transmorgify;

import java.lang.reflect.InvocationTargetException;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link Transmorgify} class.
 * Since there are still problems with "jar-hell" with conflicting
 * versions of cglib for EasyMock and the latest version of ASM library
 * we can't use class mocking of JUnit classes so we have to create our own
 * stubs.
 * @author dkatzel-home
 *
 */
public class TestTransmorgify {

    private static class TransmorgifyTestDouble extends Transmorgify {

        public TransmorgifyTestDouble(JUnitCore junitCore, String testClass) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            super(junitCore, testClass);
        }

        @Override
        protected Class<?> getLoadedClass(String testClass) throws ClassNotFoundException {
            return null;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidClassNameHasSpaceShouldThrowException() throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Result result = createSuccessfulResult();
        JUnitCore mockCore = createMockCore(result);
        new TransmorgifyTestDouble(mockCore, "invalid class");
    }

    @Test
    public void validClassNameWith$() throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Result result = createSuccessfulResult();
        JUnitCore mockCore = createMockCore(result);
        Transmorgify sut = new TransmorgifyTestDouble(mockCore, "package.myClass$innerClass");
        assertTrue(sut.didTestsStillPass());
        assertEquals(0, sut.getNumberOfFailedTests());
    }

    @Test
    public void allUnitTestsPass() throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Result result = createSuccessfulResult();
        JUnitCore mockCore = createMockCore(result);
        Transmorgify sut = new TransmorgifyTestDouble(mockCore, this.getClass().getName());
        assertTrue(sut.didTestsStillPass());
        assertEquals(0, sut.getNumberOfFailedTests());
    }

    @Test
    public void someTestsFail() throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Result result = createFailedResult(10);
        JUnitCore mockCore = createMockCore(result);
        Transmorgify sut = new TransmorgifyTestDouble(mockCore, this.getClass().getName());
        assertFalse(sut.didTestsStillPass());
        assertEquals(10, sut.getNumberOfFailedTests());
    }

    @Test
    public void somekindOfExceptionIsThrown() {
        RuntimeException expectedException = new RuntimeException("expected");
        JUnitCore mockCore = createMockCoreThatThrowsExceptionOnRun(expectedException);
        try {
            new TransmorgifyTestDouble(mockCore, this.getClass().getName());
            fail("should throw exception");
        } catch (Exception e) {
            assertEquals(expectedException, e.getCause());
        }
    }

    protected JUnitCore createMockCoreThatThrowsExceptionOnRun(final RuntimeException e) {
        return new JUnitCore() {

            @Override
            public Result run(Class<?>... classes) {
                throw e;
            }
        };
    }

    protected JUnitCore createMockCore(final Result result) {
        return new JUnitCore() {

            @Override
            public Result run(Class<?>... classes) {
                return result;
            }
        };
    }

    protected Result createFailedResult(final int numberOfFailures) {
        return new Result() {

            @Override
            public int getFailureCount() {
                return numberOfFailures;
            }

            @Override
            public boolean wasSuccessful() {
                return false;
            }
        };
    }

    protected Result createSuccessfulResult() {
        return new Result() {

            @Override
            public int getFailureCount() {
                return 0;
            }

            @Override
            public boolean wasSuccessful() {
                return true;
            }
        };
    }
}
