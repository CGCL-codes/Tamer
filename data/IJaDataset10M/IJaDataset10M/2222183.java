package sfeir.gwt.ergosoom.server.guice;

import sfeir.gwt.ergosoom.server.ClientApiServiceImpl;
import sfeir.gwt.ergosoom.server.ImportServlet;
import sfeir.gwt.ergosoom.server.PictureServlet;
import sfeir.gwt.ergosoom.server.VcardProfile;
import sfeir.gwt.ergosoom.server.ViewProfile;
import sfeir.gwt.ergosoom.server.service.ProfileService;
import sfeir.gwt.ergosoom.server.service.ProfileServiceImpl;
import sfeir.gwt.ergosoom.server.util.GuicedServerServlet;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;

public class GuiceServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {

            @Override
            protected void configureServlets() {
                serve("/ergosoom/client", "/editprofile/client", "/ergosoomviewprofile/client").with(ClientApiServiceImpl.class);
                serve("/gwtergosoom/import").with(ImportServlet.class);
                serve("/picture/*").with(PictureServlet.class);
                serve("/_logout").with(ViewProfile.class);
                serve("/vcard/*").with(VcardProfile.class);
                serve("/rest/*").with(GuicedServerServlet.class);
                bind(ProfileService.class).toProvider(new Provider<ProfileService>() {

                    @Override
                    public ProfileService get() {
                        return new ProfileServiceImpl();
                    }
                }).in(ServletScopes.REQUEST);
            }
        });
    }
}
