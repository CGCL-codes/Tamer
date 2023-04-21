package net.simpleframework.example;

import net.simpleframework.organization.OrganizationApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;

public class MyOrganizationApplicationModule extends OrganizationApplicationModule {

    @Override
    public String getLoginUrl(final PageRequestResponse requestResponse) {
        return "/system/login.jsp";
    }
}
