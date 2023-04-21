package de.objectinc.samsha.ui;

import de.objectinc.samsha.core.ComClient;
import de.objectinc.samsha.ui.adapter.ProfileUIAdapter;
import de.objectinc.samsha.ui.information.IConnectionUIListener;
import de.objectinc.samsha.ui.information.IMessageUIListener;
import de.objectinc.samsha.ui.information.IPresenceUIListener;
import de.objectinc.samsha.ui.information.ISearchUIListener;
import de.objectinc.samsha.ui.information.IUserlistUIListener;
import de.objectinc.samsha.xmpp.ui.profile.ProfileUIAdapterFactory;

public abstract class SamshaUIBuilder {

    protected ProfileUIAdapterFactory profileUIAapterFactory;

    protected ProfileUIAdapter profileUIAdapter;

    protected ComClient client = null;

    public SamshaUIBuilder(ComClient client) {
        this.client = client;
    }

    public abstract void buildProfileUI();

    public abstract IConnectionUIListener buildConnectionUI();

    public abstract IPresenceUIListener buildPresenceUI();

    public abstract IUserlistUIListener buildUserlistUI();

    public abstract IMessageUIListener buildMessageUI();

    public abstract ISearchUIListener buildSearchUI();
}
