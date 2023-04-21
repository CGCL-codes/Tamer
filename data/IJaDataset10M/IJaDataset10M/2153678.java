package org.softmed.rest.server.core.handler;

import org.softmed.rest.config.Handler;

public interface IHandlerSolution {

    public void solve(Handler handler, HandlerProcessParameters parameters) throws Throwable;

    public boolean isContinueProcessing();
}
