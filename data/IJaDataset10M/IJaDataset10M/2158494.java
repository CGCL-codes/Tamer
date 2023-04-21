package com.mycila.testing.core;

import com.mycila.testing.JDKLogging;
import com.mycila.testing.MycilaTestingException;
import com.mycila.testing.core.annot.MycilaPlugins;
import static com.mycila.testing.core.api.Cache.*;
import com.mycila.testing.core.api.Execution;
import com.mycila.testing.core.api.Step;
import com.mycila.testing.core.api.TestContext;
import com.mycila.testing.core.introspect.Introspector;
import com.mycila.testing.ea.Code;
import static com.mycila.testing.ea.ExtendedAssert.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@MycilaPlugins(UNSHARED)
public final class MycilaTest {

    static {
        JDKLogging.init();
    }

    @Test
    public void test_context() throws Exception {
        Introspector introspector = new Introspector(this);
        TestContext context = mock(TestContext.class);
        when(context.introspector()).thenReturn(introspector);
        assertThrow(MycilaTestingException.class).containingMessage("No Global Test Context available for test com.mycila.testing.core.MycilaTest#").whenRunning(new Code() {

            public void run() throws Throwable {
                Mycila.context(MycilaTest.this);
            }
        });
        Mycila.registerContext(context);
        assertEquals(Mycila.context(this), context);
        Mycila.unsetContext(context);
        assertThrow(MycilaTestingException.class).containingMessage("No Global Test Context available for test com.mycila.testing.core.MycilaTest#").whenRunning(new Code() {

            public void run() throws Throwable {
                Mycila.context(MycilaTest.this);
            }
        });
    }

    @Test
    public void test_execution() throws Exception {
        Introspector introspector = new Introspector(this);
        TestContext context = mock(TestContext.class);
        final Execution execution = mock(Execution.class);
        when(context.introspector()).thenReturn(introspector);
        when(execution.context()).thenReturn(context);
        when(execution.step()).thenReturn(Step.BEFORE);
        when(execution.method()).thenReturn(getClass().getDeclaredMethod("test_execution"));
        assertThrow(IllegalStateException.class).containingMessage("No Execution context bound to local thread !").whenRunning(new Code() {

            public void run() throws Throwable {
                Mycila.currentExecution();
            }
        });
        Mycila.registerCurrentExecution(execution);
        assertEquals(Mycila.currentExecution(), execution);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {

            public void run() {
                assertEquals(Mycila.currentExecution(), execution);
            }
        }).get();
        executorService.shutdown();
        Mycila.unsetCurrentExecution();
        assertThrow(IllegalStateException.class).containingMessage("No Execution context bound to local thread !").whenRunning(new Code() {

            public void run() throws Throwable {
                Mycila.currentExecution();
            }
        });
    }
}
