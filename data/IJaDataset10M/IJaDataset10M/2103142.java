package org.cast.isi.page;

import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.cast.cwm.CwmSession;
import org.cast.cwm.data.Role;
import org.cast.isi.ISIApplication;

@AuthorizeInstantiation("RESEARCHER")
public class AdminHome extends org.cast.cwm.admin.AdminHome {

    public AdminHome(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected List<Component> homePageComponents() {
        List<Component> list = super.homePageComponents();
        if (CwmSession.get().getUser().getRole().equals(Role.RESEARCHER)) {
            list.add(new BookmarkablePageLink<ISIStandardPage>("link", ISIApplication.get().getTocPageClass(Role.TEACHER)).add(new Label("label", "Teacher Interface")));
        }
        return list;
    }
}
