package net.sf.mxlosgi.mxlosgisearchbundle.impl;

import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoManager;
import net.sf.mxlosgi.mxlosgidiscobundle.DiscoInfoPacketExtension;
import net.sf.mxlosgi.mxlosgimainbundle.ServerTimeoutException;
import net.sf.mxlosgi.mxlosgimainbundle.StanzaCollector;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgimainbundle.filter.PacketIDFilter;
import net.sf.mxlosgi.mxlosgisearchbundle.SearchExtension;
import net.sf.mxlosgi.mxlosgisearchbundle.SearchManager;
import net.sf.mxlosgi.mxlosgixmppbundle.IQ;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public class SearchManagerImpl implements SearchManager {

    private DiscoInfoManager discoInfoManager;

    /**
	 * 
	 * @param discoInfoManager
	 */
    public SearchManagerImpl(DiscoInfoManager discoInfoManager) {
        this.discoInfoManager = discoInfoManager;
    }

    @Override
    public SearchExtension getSearchExtension(XMPPConnection connection, JID serverJID) throws XMPPException {
        IQ iq = new IQ(IQ.Type.get);
        iq.setTo(serverJID);
        SearchExtension searchExtension = new SearchExtension();
        iq.addExtension(searchExtension);
        StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
        connection.sendStanza(iq);
        XMLStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
        collector.cancel();
        if (stanza == null) {
            throw new ServerTimeoutException("remote server no response");
        }
        if (stanza instanceof IQ) {
            IQ iqResponse = (IQ) stanza;
            IQ.Type type = iqResponse.getType();
            if (type == IQ.Type.result) {
                return (SearchExtension) iqResponse.getExtension(SearchExtension.ELEMENTNAME, SearchExtension.NAMESPACE);
            } else if (type == IQ.Type.error) {
                throw new XMPPException(iqResponse.getError());
            }
        }
        return null;
    }

    @Override
    public boolean isSupportSearch(XMPPConnection connection, JID serverJID) throws XMPPException {
        DiscoInfoPacketExtension discoInfo = discoInfoManager.getDiscoInfo(connection, serverJID);
        for (DiscoInfoPacketExtension.Feature feature : discoInfo.getFeatures()) {
            if ("jabber:iq:search".equals(feature.getFeature())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public SearchExtension search(XMPPConnection connection, SearchExtension extension, JID serverJID) throws XMPPException {
        IQ iq = new IQ(IQ.Type.set);
        iq.setTo(serverJID);
        iq.addExtension(extension);
        StanzaCollector collector = connection.createStanzaCollector(new PacketIDFilter(iq.getStanzaID()));
        connection.sendStanza(iq);
        XMLStanza stanza = collector.nextResult(connection.getConnectionConfig().getResponseStanzaTimeout());
        collector.cancel();
        if (stanza == null) {
            throw new ServerTimeoutException("remote server no response");
        }
        if (stanza instanceof IQ) {
            IQ iqResponse = (IQ) stanza;
            IQ.Type type = iqResponse.getType();
            if (type == IQ.Type.result) {
                return (SearchExtension) iqResponse.getExtension(SearchExtension.ELEMENTNAME, SearchExtension.NAMESPACE);
            } else if (type == IQ.Type.error) {
                throw new XMPPException(iqResponse.getError());
            }
        }
        return null;
    }
}
