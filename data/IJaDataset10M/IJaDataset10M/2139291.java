package org.atricore.idbus.capabilities.openid.ui.internal;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.pages.AccessDeniedPage;
import org.apache.wicket.markup.html.pages.PageExpiredErrorPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.atricore.idbus.capabilities.openid.ui.page.OpenIDLoginPage;

/**
 * Entry point for the Wicket-based OpenID front-end.
 */
public class OpenIDUIApplication extends WebApplication {

    public OpenIDUIApplication() {
        super();
    }

    @Override
    protected void init() {
        super.init();
        mountBookmarkablePage("/login", OpenIDLoginPage.class);
        mountBookmarkablePage("/error/401", AccessDeniedPage.class);
        mountBookmarkablePage("/error/404", PageExpiredErrorPage.class);
        getApplicationSettings().setAccessDeniedPage(AccessDeniedPage.class);
        getApplicationSettings().setPageExpiredErrorPage(PageExpiredErrorPage.class);
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<OpenIDLoginPage> getHomePage() {
        return OpenIDLoginPage.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new OpenIDWebSession(request);
    }
}
