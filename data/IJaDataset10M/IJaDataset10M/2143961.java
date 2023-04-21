package org.blueoxygen.cimande.wicket;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.blueoxygen.cimande.wicket.session.CimandeWebSession;
import org.blueoxygen.cimande.wicket.signin.SignInPage;
import org.blueoxygen.cimande.wicket.main.MainPage;

/**
 * @author edward.yakop@gmail.com
 * @since 1.3.0
 */
public final class CimandeWicketApplication extends AuthenticatedWebApplication {

    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return CimandeWebSession.class;
    }

    @Override
    protected void init() {
        super.init();
        SpringComponentInjector listener = new SpringComponentInjector(this);
        addComponentInstantiationListener(listener);
    }

    protected Class<? extends WebPage> getSignInPageClass() {
        return SignInPage.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new CimandeWebSession(request);
    }

    public Class getHomePage() {
        return MainPage.class;
    }
}
