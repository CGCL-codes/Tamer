package com.dasberg.gwt.guice;

import com.dasberg.gwt.dispatch.Dispatcher;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test case for DispatcherModule.
 * @author mischa
 */
public class DispatcherModuleTest {

    @Inject
    private Dispatcher dispatcher;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new DispatcherModule());
        injector.injectMembers(this);
    }

    @Test
    public void shouldHaveOneHandler() {
        assertEquals(1, dispatcher.getHandlers().size());
    }
}
