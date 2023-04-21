package com.google.web.bindery.requestfactory.server;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryExceptionPropagationTest;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.SimpleRequestFactory;

/**
 * JRE version of {@link RequestFactoryExceptionPropagationTest}.
 */
public class RequestFactoryExceptionPropagationJreTest extends RequestFactoryExceptionPropagationTest {

    @Override
    public String getModuleName() {
        return null;
    }

    @Override
    protected SimpleRequestFactory createFactory() {
        return RequestFactoryJreTest.createInProcess(SimpleRequestFactory.class);
    }

    @Override
    protected void fireContextAndCatch(RequestContext context, Receiver<Void> receiver, GWT.UncaughtExceptionHandler exceptionHandler) {
        try {
            if (receiver == null) {
                context.fire();
            } else {
                context.fire(receiver);
            }
        } catch (Throwable e) {
            exceptionHandler.onUncaughtException(e);
        }
    }
}
