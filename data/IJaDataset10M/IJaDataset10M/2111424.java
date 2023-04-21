package org.codecompany.jeha.test.handler;

import static org.codecompany.jeha.test.base.CustomHandler.MSG_ARITHMETIC_EXCEPTION;
import static org.codecompany.jeha.test.base.CustomHandler.MSG_OUT_OF_BOUNDS_EXCEPTION;
import org.codecompany.jeha.ExceptionHandler;
import org.codecompany.jeha.core.Handler;
import org.codecompany.jeha.core.HandlerUtil;
import org.codecompany.jeha.test.base.CustomHandler;
import org.codecompany.jeha.test.base.CustomHandlerMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CustomHandlerTest extends HandlerTest {

    @Test
    @ExceptionHandler(mapper = CustomHandlerMapper.class)
    public void mapperSpecifiedWithCustomMethodHandler() {
        try {
            System.out.println(10 / 0);
        } catch (ArithmeticException e) {
            Throwable t = HandlerUtil.handle(e);
            Assert.assertEquals(t.getMessage(), MSG_ARITHMETIC_EXCEPTION);
            Handler handler = HandlerUtil.currentHandler();
            Assert.assertTrue(handler instanceof CustomHandler);
        }
    }

    @Test
    @ExceptionHandler
    public void noMapperSpecified() {
        try {
            System.out.println(new int[] { 1, 2 }[10]);
        } catch (IndexOutOfBoundsException e) {
            Throwable t = HandlerUtil.handle(e);
            Assert.assertEquals(t, e);
            Object[] params = checkDefaultHandlerAndGetParams();
            Assert.assertTrue(params.length == 0);
        }
    }

    @Test
    @ExceptionHandler(handler = CustomHandler.class)
    public void handlerSpecifiedWithSpecificMethodHandler() {
        try {
            System.out.println(new int[] { 1, 2 }[10]);
        } catch (IndexOutOfBoundsException e) {
            Throwable t = HandlerUtil.handle(e);
            Assert.assertEquals(t.getMessage(), MSG_OUT_OF_BOUNDS_EXCEPTION);
            Handler handler = HandlerUtil.currentHandler();
            Assert.assertTrue(handler instanceof CustomHandler);
        }
    }
}
