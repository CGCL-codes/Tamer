package com.anzsoft.client.XMPP.mandioca;

import com.anzsoft.client.XMPP.XmppListener;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppPresence;
import com.anzsoft.client.XMPP.XmppPresenceListener;

class PresenceRouter extends Router implements XmppPresenceListener {

    public void onPresenceReceived(final XmppPresence presence) {
        filterIncoming(presence, new XmppPacketHandler() {

            public void handle(final XmppPacket message, final XmppListener listener) {
                ((XmppPresenceListener) listener).onPresenceReceived((XmppPresence) message);
            }
        });
    }

    public void onPresenceSent(final XmppPresence presence) {
        filterOutcoming(presence, new XmppPacketHandler() {

            public void handle(final XmppPacket message, final XmppListener listener) {
                ((XmppPresenceListener) listener).onPresenceSent((XmppPresence) message);
            }
        });
    }

    public void add(final XmppPresenceListener listener, final XmppPacketFilter filter) {
        addListener(listener, filter);
    }
}
