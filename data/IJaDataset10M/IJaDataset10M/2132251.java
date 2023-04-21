package com.google.web.bindery.requestfactory.server;

import com.google.web.bindery.requestfactory.gwt.client.FindServiceTest;
import com.google.web.bindery.requestfactory.shared.SimpleRequestFactory;

/**
 * Run the FindService tests in-process.
 */
public class FindServiceJreTest extends FindServiceTest {

    @Override
    public String getModuleName() {
        return null;
    }

    @Override
    protected SimpleRequestFactory createFactory() {
        return RequestFactoryJreTest.createInProcess(SimpleRequestFactory.class);
    }
}
