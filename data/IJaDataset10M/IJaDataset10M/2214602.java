package fitlibrary.traverse.workflow.special;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import fitlibrary.flow.GlobalActionScope;
import fitlibrary.runtime.RuntimeContextInternal;
import fitlibrary.special.DoAction;
import fitlibrary.tableProxy.CellProxy;

@RunWith(JMock.class)
public class TestContains {

    Mockery context = new Mockery();

    DoAction action = context.mock(DoAction.class);

    CellProxy cellProxy = context.mock(CellProxy.class);

    RuntimeContextInternal runtime = context.mock(RuntimeContextInternal.class);

    GlobalActionScope globalActionScope = new GlobalActionScope();

    void actual(final Object value) throws Exception {
        context.checking(new Expectations() {

            {
                oneOf(action).run();
                will(returnValue(value));
            }
        });
    }

    @Before
    public void injectRuntime() {
        globalActionScope.setRuntimeContext(runtime);
        context.checking(new Expectations() {

            {
                allowing(action).cellAt(1);
                will(returnValue(cellProxy));
                allowing(runtime).getTimeout("becomes", 1000);
                will(returnValue(5));
            }
        });
    }

    @Test
    public void failsWithNullResult() throws Exception {
        context.checking(new Expectations() {

            {
                oneOf(cellProxy).fail("result is null");
            }
        });
        actual(null);
        globalActionScope.contains(action, "a");
    }

    @Test
    public void failsWithNullExpectation() throws Exception {
        context.checking(new Expectations() {

            {
                oneOf(cellProxy).fail("expected is null");
            }
        });
        globalActionScope.contains(action, null);
    }

    @Test
    public void passes() throws Exception {
        context.checking(new Expectations() {

            {
                oneOf(cellProxy).pass();
            }
        });
        actual("abc");
        globalActionScope.contains(action, "a");
    }

    @Test
    public void fails() throws Exception {
        context.checking(new Expectations() {

            {
                oneOf(cellProxy).fail("abc");
            }
        });
        actual("abc");
        globalActionScope.contains(action, "Z");
    }

    @Test
    public void doesNotContainFailsWithNullResult() throws Exception {
        context.checking(new Expectations() {

            {
                oneOf(cellProxy).fail("result is null");
            }
        });
        actual(null);
        globalActionScope.doesNotContain(action, "a");
    }

    @Test
    public void doesNotContainFailsWithNullExpectation() throws Exception {
        context.checking(new Expectations() {

            {
                oneOf(cellProxy).fail("expected is null");
            }
        });
        globalActionScope.doesNotContain(action, null);
    }

    @Test
    public void doesNotContainFails() throws Exception {
        context.checking(new Expectations() {

            {
                oneOf(cellProxy).fail("abc");
            }
        });
        actual("abc");
        globalActionScope.doesNotContain(action, "a");
    }

    @Test
    public void doesNotContainPasses() throws Exception {
        context.checking(new Expectations() {

            {
                oneOf(cellProxy).pass();
            }
        });
        actual("abc");
        globalActionScope.doesNotContain(action, "Z");
    }

    @Test
    public void eventuallyFailsWithNullResult() throws Exception {
        context.checking(new Expectations() {

            {
                atLeast(1).of(action).run();
                will(returnValue(null));
                oneOf(cellProxy).fail("result is null");
            }
        });
        globalActionScope.eventuallyContains(action, "a");
    }

    @Test
    public void eventuallyFailsWithNullExpectation() throws Exception {
        context.checking(new Expectations() {

            {
                allowing(action).cellAt(1);
                will(returnValue(cellProxy));
                oneOf(cellProxy).fail("expected is null");
            }
        });
        globalActionScope.eventuallyContains(action, null);
    }

    @Test
    public void eventuallyPasses() throws Exception {
        context.checking(new Expectations() {

            {
                oneOf(cellProxy).pass();
            }
        });
        actual("abc");
        globalActionScope.eventuallyContains(action, "a");
    }

    @Test
    public void eventuallyPassesAfterFailing() throws Exception {
        context.checking(new Expectations() {

            {
                oneOf(action).run();
                will(returnValue("ZZ"));
                oneOf(action).run();
                will(returnValue("abc"));
                oneOf(cellProxy).pass();
            }
        });
        globalActionScope.eventuallyContains(action, "a");
    }

    @Test
    public void eventuallyFails() throws Exception {
        context.checking(new Expectations() {

            {
                atLeast(1).of(action).run();
                will(returnValue("abc"));
                oneOf(cellProxy).fail("abc");
            }
        });
        globalActionScope.eventuallyContains(action, "Z");
    }
}
