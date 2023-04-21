package net.sf.clairv.p2p;

/**
 * Message handler interface.
 * 
 * @author qiuyin
 *
 */
public interface RequestHandler {

    /**
	 * Handles an incoming request message and wraps the result as another
	 * message.
	 * @param request the request object
	 * @return a new <tt>Message</tt> object which contains the result;
	 * null if this request message does not require a response
	 */
    public ResponseMessage handleRequest(OverlayRequest request);
}
