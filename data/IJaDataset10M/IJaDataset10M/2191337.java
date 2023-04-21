package portal.presentation;

import hambo.app.util.CookieUtil;
import hambo.app.base.StaticPortalPage;

public class country_fr extends StaticPortalPage {

    public country_fr() {
        super("country_fr", false);
    }

    protected void processHooks() {
        CookieUtil.setCountryCookie(comms.response.getHttpServletResponse(), "fr");
        if (!getContext().isUserLoggedIn()) {
            getContext().setSessionAttribute("country", "France");
            getContext().setSessionAttribute("language", "French");
        }
    }

    public void processPage() {
    }
}
