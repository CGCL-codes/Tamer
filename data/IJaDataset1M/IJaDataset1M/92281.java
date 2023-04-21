package org.wintersleep.usermgmt.wicket.base;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.PageLink;
import net.databinder.components.hibernate.PageSourceLink;

public class ActionsPanel extends Panel {

    public ActionsPanel(String wicketid, PageSourceLink showLink, IPageLink editLink, IPageLink deleteLink) {
        super(wicketid);
        add(showLink);
        add(new PageLink("editLink", editLink));
        add(new PageLink("deleteLink", deleteLink));
    }
}
