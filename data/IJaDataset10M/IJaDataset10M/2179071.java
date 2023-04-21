package net.sf.stump.api.web;

import org.apache.wicket.markup.html.pages.AccessDeniedPage;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * @author Ville Peurala
 */
public class CustomWebApplication extends WebApplication {

    @SuppressWarnings("unchecked")
    @Override
    public Class getHomePage() {
        return AccessDeniedPage.class;
    }
}
