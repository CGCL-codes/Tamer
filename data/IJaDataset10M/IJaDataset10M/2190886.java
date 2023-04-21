package fitlibrary.flow;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import fitlibrary.dynamicVariable.DynamicVariables;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.runtime.RuntimeContextInternal;
import fitlibrary.traverse.workflow.StopWatch;

@RunWith(JMock.class)
public class TestGlobalMethodScope {

    final Mockery context = new Mockery();

    final RuntimeContextInternal runtimeContext = context.mock(RuntimeContextInternal.class);

    final DynamicVariables dynamicVariables = context.mock(DynamicVariables.class);

    final GlobalActionScope globalScope = new GlobalActionScope();

    @Before
    public void regularAllowing() {
        globalScope.setRuntimeContext(runtimeContext);
        context.checking(new Expectations() {

            {
                allowing(runtimeContext).getDynamicVariables();
                will(returnValue(dynamicVariables));
            }
        });
    }

    @Test
    public void getBecomesTimeout() {
        context.checking(new Expectations() {

            {
                oneOf(runtimeContext).getTimeout(GlobalActionScope.BECOMES_TIMEOUT, 1000);
                will(returnValue(1000));
            }
        });
        assertThat(globalScope.becomesTimeout(), is(1000));
    }

    @Test
    public void setBecomesTimeout() {
        context.checking(new Expectations() {

            {
                oneOf(runtimeContext).putTimeout(GlobalActionScope.BECOMES_TIMEOUT, 5000);
            }
        });
        globalScope.becomesTimeout(5000);
    }

    @Test
    public void getTimeout() {
        final String var = "exists";
        context.checking(new Expectations() {

            {
                oneOf(runtimeContext).getTimeout(var, 1000);
                will(returnValue(1000));
            }
        });
        assertThat(globalScope.getTimeout(var), is(1000));
    }

    @Test
    public void putTimeout() {
        final String var = "exists";
        context.checking(new Expectations() {

            {
                oneOf(runtimeContext).putTimeout(var, 5000);
            }
        });
        globalScope.putTimeout(var, 5000);
    }

    @Test
    public void dynamicVariableDoesNotExist() {
        final String var = "unknown---???";
        context.checking(new Expectations() {

            {
                oneOf(dynamicVariables).get(var);
                will(returnValue(null));
            }
        });
        assertThat(globalScope.getDynamicVariable(var), is((Object) null));
    }

    @Test
    public void dynamicVariableExists() {
        final String var = "exists";
        context.checking(new Expectations() {

            {
                oneOf(dynamicVariables).get(var);
                will(returnValue("a"));
            }
        });
        assertThat(globalScope.getDynamicVariable(var), is((Object) "a"));
    }

    @Test
    public void setDynamicVariable() {
        final String var = "exists";
        context.checking(new Expectations() {

            {
                oneOf(dynamicVariables).put(var, "a");
            }
        });
        globalScope.setDynamicVariable(var, "a");
    }

    @Test
    public void clearDynamicVariable() {
        context.checking(new Expectations() {

            {
                oneOf(dynamicVariables).clearAll();
            }
        });
        globalScope.clearDynamicVariables();
    }

    @Test(expected = FitLibraryException.class)
    public void theStopWatchNotStarted() {
        context.checking(new Expectations() {

            {
                oneOf(dynamicVariables).get(GlobalActionScope.STOP_WATCH);
                will(returnValue(null));
            }
        });
        globalScope.stopWatch();
    }

    @Test
    public void startAndstopStopWatch() {
        context.checking(new Expectations() {

            {
                oneOf(dynamicVariables).put(with(GlobalActionScope.STOP_WATCH), with(aNonNull(StopWatch.class)));
                oneOf(dynamicVariables).get(GlobalActionScope.STOP_WATCH);
                will(returnValue(new StopWatch()));
            }
        });
        globalScope.startStopWatch();
        assertThat(globalScope.stopWatch(), not(greaterThan(5L)));
    }
}
