package net.sf.mxlosgi.disco.impl;

import net.sf.mxlosgi.core.ServerTimeoutException;
import net.sf.mxlosgi.core.StanzaCollector;
import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.XmppException;
import net.sf.mxlosgi.core.filter.PacketIDFilter;
import net.sf.mxlosgi.disco.DiscoItemsManager;
import net.sf.mxlosgi.disco.DiscoItemsPacketExtension;
import net.sf.mxlosgi.xmpp.Iq;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.XmlStanza;

/**
 * @author noah
 *
 */
public class DiscoItemsManagerImpl implements DiscoItemsManager {

    public DiscoItemsManagerImpl() {
    }

    @Override
    public DiscoItemsPacketExtension getDiscoItems(XmppConnection connection, JID to) throws XmppException {
        return getDiscoItems(connection, to, null);
    }

    @Override
    public DiscoItemsPacketExtension getDiscoItems(XmppConnection connection, JID to, String node) throws XmppException {
        Iq iq = new Iq(Iq.Type.get);
        iq.setTo(to);
        DiscoItemsPacketExtension queryDiscoItems = new DiscoItemsPacketExtension();
        queryDiscoItems.setNode(node);
        iq.addExtension(queryDiscoItems);
        StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
        connection.sendStanza(iq);
        XmlStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
        collector.cancel();
        if (stanza == null) {
            throw new ServerTimeoutException("no response from server");
        } else {
            Iq iqResponse = (Iq) stanza;
            Iq.Type type = iqResponse.getType();
            if (type == Iq.Type.result) {
                DiscoItemsPacketExtension discoInfoResponse = (DiscoItemsPacketExtension) iqResponse.getExtension(DiscoItemsPacketExtension.ELEMENTNAME, DiscoItemsPacketExtension.NAMESPACE);
                return discoInfoResponse;
            } else if (type == Iq.Type.error) {
                throw new XmppException(iqResponse.getError());
            }
        }
        return null;
    }
}
