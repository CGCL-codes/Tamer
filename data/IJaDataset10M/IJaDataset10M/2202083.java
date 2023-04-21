package org.arasso.application.pages.admin;

import org.arasso.application.pages.MainPage;
import wicket.Component;
import wicket.MarkupContainer;
import wicket.markup.html.WebPage;
import wicket.markup.html.border.Border;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import wicket.model.Model;

/**
 *
 * @author fdiotalevi
 */
public abstract class AdminPage extends WebPage {

    public AdminPage() {
        add(new BookmarkablePageLink("insert", InsertFeed.class));
        add(new BookmarkablePageLink("list", ListFeeds.class));
    }
}
