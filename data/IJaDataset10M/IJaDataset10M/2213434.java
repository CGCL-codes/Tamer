package org.jwebsocket.plugins.streaming;

import org.apache.log4j.Logger;
import org.jwebsocket.api.WebSocketConnector;
import org.jwebsocket.logging.Logging;
import org.jwebsocket.server.TokenServer;
import org.jwebsocket.token.Token;

/**
 * implements a stream with a queue of token instances. In addition to the
 * <tt>BaseStream</tt> the <tt>TokenStream</tt> also maintains a reference
 * to a <tt>TokenServer</tt> instance. Unlike the <tt>BaseStream</tt> which has
 * no control or limitation regarding the objects in the Queue, the
 * <tt>TokenStream</tt> allow tokens in the queue only. To support the various
 * sub protocols the TokenStream does not send the queued tokens directly to
 * the client but via the <tt>TokenServer</tt>. The <tt>TokenServer</tt> knows
 * about the used sub protocols of the clients and can decide wether to format
 * them as JSON, CSV or XML. Thus application streams usually are descend from
 * <tt>TokenStream</tt>.
 * @author aschulze
 */
public class TokenStream extends BaseStream {

    private static Logger mLog = Logging.getLogger(TokenStream.class);

    private TokenServer mServer = null;

    /**
	 * creates a new instance of the TokenStream. In Addition to the
	 * <tt>BaseStream</tt> the <tt>TokenStream</tt> also maintains a reference
	 * to a <tt>TokenServer</tt> instance.
	 * @param aStreamID
	 * @param aServer
	 */
    public TokenStream(String aStreamID, TokenServer aServer) {
        super(aStreamID);
        mServer = aServer;
    }

    @Override
    protected void processConnector(WebSocketConnector aConnector, Object aObject) {
        try {
            getServer().sendToken(aConnector, (Token) aObject);
        } catch (Exception lEx) {
            mLog.error("(processConnector) " + lEx.getClass().getSimpleName() + ": " + lEx.getMessage());
        }
    }

    /**
	 * returns the referenced <tt>TokenServer</tt> instance.
	 * @return the server
	 */
    public TokenServer getServer() {
        return mServer;
    }
}
