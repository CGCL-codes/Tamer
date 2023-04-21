package fitlibrary.traverse.workflow.special;

import java.math.BigDecimal;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import fitlibrary.exception.IgnoredException;
import fitlibrary.flow.GlobalActionScope;
import fitlibrary.special.DoAction;

@RunWith(JMock.class)
public class TestShowAfterAs {

    Mockery context = new Mockery();

    DoAction action = context.mock(DoAction.class);

    GlobalActionScope globalActionScope = new GlobalActionScope();

    @Test
    public void textIsShown() throws Exception {
        context.checking(new Expectations() {

            {
                one(action).run();
                will(returnValue("nz"));
                one(action).showAfterAs("zz", "nz");
            }
        });
        globalActionScope.showAfterAs("zz", action);
    }

    @Test
    public void objectIsShown() throws Exception {
        final BigDecimal number = new BigDecimal(4444444);
        context.checking(new Expectations() {

            {
                one(action).run();
                will(returnValue(number));
                one(action).showAfterAs("yy", number);
            }
        });
        globalActionScope.showAfterAs("yy", action);
    }

    @Test
    public void nothingShownWithNullResult() throws Exception {
        context.checking(new Expectations() {

            {
                one(action).run();
                will(returnValue(null));
            }
        });
        globalActionScope.showAfterAs("zz", action);
    }

    @Test(expected = Exception.class)
    public void exceptionIsPassedOn() throws Exception {
        context.checking(new Expectations() {

            {
                one(action).run();
                will(throwException(new IgnoredException()));
            }
        });
        globalActionScope.showAfterAs("zz", action);
    }
}
