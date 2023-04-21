package org.socialresume.server.guice;

import org.socialresume.server.DispatchServletModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class MyGuiceServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new DispatchServletModule(), new ServerModule());
    }
}
