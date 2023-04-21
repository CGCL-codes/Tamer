package net.sf.mxlosgi.mxlosgidiscobundle;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgimainbundle.XMPPException;
import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 *
 */
public interface DiscoItemsManager {

    /**
	 * 
	 * @param connection
	 * @param to
	 * @return
	 * @throws XMPPException
	 */
    public DiscoItemsPacketExtension getDiscoItems(XMPPConnection connection, JID to) throws XMPPException;

    /**
	 * 
	 * @param connection
	 * @param to
	 * @param node
	 * @return
	 * @throws XMPPException
	 */
    public DiscoItemsPacketExtension getDiscoItems(XMPPConnection connection, JID to, String node) throws XMPPException;
}
