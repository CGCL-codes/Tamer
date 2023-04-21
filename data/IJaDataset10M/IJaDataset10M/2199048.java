package utilities.aspects4selenium;

import java.io.File;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import utilities.aspects4selenium.sut.ActionSeleniumCase;
import com.thoughtworks.selenium.Selenium;

public class ActionsExceptionHandlerTest {

    Mockery context = new Mockery();

    private ActionSeleniumCase actions;

    private Selenium selenium;

    void mocking(final String method) {
        selenium = context.mock(Selenium.class, method);
        final String dir = System.getProperty("user.dir") + File.separator;
        context.checking(new Expectations() {

            {
                String filename = dir + "ActionSeleniumCase-" + method + ".png";
                one(selenium).captureEntirePageScreenshot(filename);
            }
        });
        actions = new ActionSeleniumCase(selenium);
    }

    @Test
    public void actionCatchException() throws Exception {
        mocking("actionCatchException");
        actions.actionCatchException();
    }

    @Test(expected = Exception.class)
    public void actionWithArgsAndReturnValueThrowsRuntimeException() throws Exception {
        mocking("actionWithArgsAndReturnValueThrowsRuntimeException");
        actions.actionWithArgsAndReturnValueThrowsRuntimeException(0);
        context.assertIsSatisfied();
    }

    @Test(expected = Exception.class)
    public void actionThrowsRuntimeException() throws Exception {
        mocking("actionThrowsRuntimeException");
        actions.actionThrowsRuntimeException();
        context.assertIsSatisfied();
    }

    @Test(expected = Exception.class)
    public void actionCatchAndThrowsException() throws Exception {
        mocking("actionCatchAndThrowsException");
        actions.actionCatchAndThrowsException();
        context.assertIsSatisfied();
    }

    @Test(expected = Exception.class)
    public void actionCatchExceptionAndThrowsRuntimeException() throws Exception {
        mocking("actionCatchExceptionAndThrowsRuntimeException");
        actions.actionCatchExceptionAndThrowsRuntimeException();
        context.assertIsSatisfied();
    }

    @Test(expected = Exception.class)
    public void actionThrowsUnknownException() throws Exception {
        mocking("actionThrowsUnknownException");
        actions.actionThrowsUnknownException();
        context.assertIsSatisfied();
    }

    @Test(expected = Exception.class)
    public void actionThrowsUnknownRuntimeException() throws Exception {
        mocking("actionThrowsUnknownRuntimeException");
        actions.actionThrowsUnknownRuntimeException();
        context.assertIsSatisfied();
    }
}
