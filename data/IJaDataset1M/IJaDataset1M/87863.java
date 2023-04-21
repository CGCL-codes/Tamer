package org.testing.server;

import com.google.inject.servlet.ServletModule;
import org.testing.client.SampleGWTService;

/**
 *
 * @author Praca
 */
public class MyServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/org.testing.Main/GWT.rpc").with(GuiceRemoteServiceServlet.class);
        bind(SampleGWTService.class).to(SampleGWTServiceImpl.class);
        bind(CustomService.class).to(CustomServiceImpl.class);
    }
}
