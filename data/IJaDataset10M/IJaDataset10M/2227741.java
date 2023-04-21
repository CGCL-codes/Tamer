package org.isurf.dataintegrator.admin;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.isurf.dataintegrator.templates.TemplateAdmin;

public class WelcomeAdmin extends TemplateAdmin {

    @SuppressWarnings("unchecked")
    public WelcomeAdmin(PageParameters params) {
        super(params);
        add(new Label("welcome", "Admin Area."));
        add(new BookmarkablePageLink("pageLinkUserAdd", CreateUser.class));
        add(new BookmarkablePageLink("pageLinkControlAccess", Access.class));
    }

    @Override
    public String getPageTitle() {
        return "Admin Area";
    }
}
