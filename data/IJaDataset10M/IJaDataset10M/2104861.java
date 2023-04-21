package org.codecompany.jeha.test.handler;

import org.codecompany.jeha.core.DefaultHandler;
import org.codecompany.jeha.core.Handler;
import org.codecompany.jeha.core.HandlerUtil;
import org.codecompany.jeha.test.base.ExceptionGenerator;
import org.testng.Assert;

public abstract class HandlerTest extends ExceptionGenerator {

    protected Object[] checkDefaultHandlerAndGetParams() {
        Handler handler = HandlerUtil.currentHandler();
        Assert.assertTrue(handler instanceof DefaultHandler);
        DefaultHandler defaultHandler = (DefaultHandler) handler;
        return defaultHandler.getParameters();
    }
}
