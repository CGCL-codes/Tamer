package org.lopatka.idonc.web;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.lopatka.idonc.web.page.HomePage;
import org.lopatka.idonc.web.page.LoginPage;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class IdoncApplication extends AuthenticatedWebApplication {

    private static final long serialVersionUID = 1L;

    /** Relevant locales wrapped in a list. */
    public static final List<Locale> LOCALES = Arrays.asList(new Locale[] { Locale.ENGLISH, new Locale("pl", "PL") });

    @Override
    protected void init() {
        super.init();
        addComponentInstantiationListener(new SpringComponentInjector(this, context()));
        getMarkupSettings().setCompressWhitespace(true);
        getMarkupSettings().setStripComments(true);
        getMarkupSettings().setStripWicketTags(true);
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
    }

    public ApplicationContext context() {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }

    @Override
    public Class<? extends HomePage> getHomePage() {
        return HomePage.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new IdoncSession(request);
    }

    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return IdoncSession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return LoginPage.class;
    }
}
