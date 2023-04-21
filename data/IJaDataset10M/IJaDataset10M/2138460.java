package net.sf.mxlosgi.mxlosgimainbundle.interceptor;

import net.sf.mxlosgi.mxlosgimainbundle.XMPPConnection;
import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * @author noah
 *
 */
public interface StanzaSendInterceptor {

    /**
	 * @param connection
	 * @param stanza
	 */
    public boolean interceptSendStanza(XMPPConnection connection, XMLStanza stanza);
}
