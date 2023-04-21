package fitlibrary.traverse.workflow.special;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.lang.reflect.InvocationTargetException;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import fitlibrary.exception.IgnoredException;
import fitlibrary.exception.parse.BadNumberException;
import fitlibrary.flow.GlobalActionScope;
import fitlibrary.special.DoAction;

@RunWith(JMock.class)
public class TestEnsure {

    Mockery context = new Mockery();

    DoAction action = context.mock(DoAction.class);

    GlobalActionScope globalActionScope = new GlobalActionScope();

    @Test
    public void passesWithNullResult() throws Exception {
        context.checking(new Expectations() {

            {
                one(action).run();
                will(returnValue(null));
            }
        });
        assertThat(globalActionScope.ensure(action), is(true));
    }

    @Test
    public void passesWithTrueResult() throws Exception {
        context.checking(new Expectations() {

            {
                one(action).run();
                will(returnValue(true));
            }
        });
        assertThat(globalActionScope.ensure(action), is(true));
    }

    @Test
    public void failsWithFalseResult() throws Exception {
        context.checking(new Expectations() {

            {
                one(action).run();
                will(returnValue(false));
            }
        });
        assertThat(globalActionScope.ensure(action), is(false));
    }

    @Test(expected = IgnoredException.class)
    public void ignoredWithIgnoredException() throws Exception {
        context.checking(new Expectations() {

            {
                one(action).run();
                will(throwException(new IgnoredException()));
            }
        });
        assertThat(globalActionScope.ensure(action), is(false));
    }

    @Test(expected = BadNumberException.class)
    public void errorWithException() throws Exception {
        context.checking(new Expectations() {

            {
                one(action).run();
                will(throwException(new BadNumberException()));
            }
        });
        assertThat(globalActionScope.ensure(action), is(false));
    }

    @Test(expected = InvocationTargetException.class)
    public void errorWithEmbeddedException() throws Exception {
        context.checking(new Expectations() {

            {
                one(action).run();
                will(throwException(new InvocationTargetException(new BadNumberException())));
            }
        });
        assertThat(globalActionScope.ensure(action), is(false));
    }
}
