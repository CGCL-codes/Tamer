package com.sxi.cometd.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.wicketstuff.push.IChannelService;
import org.wicketstuff.push.cometd.CometdBehavior;
import org.wicketstuff.push.examples.application.WicketCometdSession;
import com.sxi.cometd.CometdAppBehavior;
import com.sxi.cometd.behavior.RedirectUrlBehavior;
import com.sxi.cometd.core.CometdBasePage;
import com.sxi.cometd.pages.list.ListOverridePage;
import com.sxi.cometd.pages.utils.LoggedUsers;

/**
 * @author Emmanuel Nollase - emanux
 * created 2009 7 21 - 16:48:18
 */
public class DigiBasePage extends CometdBasePage {

    private final String user;

    public DigiBasePage(PageParameters parameters) {
        super(parameters);
        add(HeaderContributor.forJavaScript(CometdAppBehavior.class, "js/cometd-app.js"));
        add(HeaderContributor.forJavaScript(CometdAppBehavior.class, "js/cometd-wicket.js"));
        add(new RedirectUrlBehavior());
        user = WicketCometdSession.get().getCometUser();
        add(new Label("user", user));
        LoggedUsers.printLoggedUsers();
        add(new Link("txn") {

            @Override
            public void onClick() {
                setResponsePage(DigiTransactionPage.class);
            }
        });
        add(new Link("listovrd") {

            @Override
            public void onClick() {
                setResponsePage(ListOverridePage.class);
            }
        });
    }

    @Override
    protected IChannelService getChannelService() {
        return getCometdService();
    }

    public String getUser() {
        return user;
    }
}
