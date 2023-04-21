package org.waveprotocol.wave.federation.xmpp;

/**
 * Namespace definitions for the XMPP package.
 *
 * @author thorogood@google.com (Sam Thorogood)
 */
final class XmppNamespace {

    static final String NAMESPACE_XMPP_RECEIPTS = "urn:xmpp:receipts";

    static final String NAMESPACE_DISCO_INFO = "http://jabber.org/protocol/disco#info";

    static final String NAMESPACE_DISCO_ITEMS = "http://jabber.org/protocol/disco#items";

    static final String NAMESPACE_PUBSUB = "http://jabber.org/protocol/pubsub";

    static final String NAMESPACE_PUBSUB_EVENT = "http://jabber.org/protocol/pubsub#event";

    static final String NAMESPACE_WAVE_SERVER = "http://waveprotocol.org/protocol/0.2/waveserver";

    /**
   * Uninstantiable class.
   */
    private XmppNamespace() {
    }
}
