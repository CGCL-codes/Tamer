package org.doit.muffin;

/** Determines whether a filters wants to process a request itself, rather than let that request be sent to a server.
 *
 * Classes which want to process requests should implement this interface and also the HttpRelay interface.
 *
 * Muffin will call your <B>wantRequest</B> method for each request. If <B>wantRequest</B> returns true,
 * then Muffin will call your filter's sendRequest and recvReply (part of HttpRelay) for this request instead of 
 * sending the request to a server. If <B>wantRequest</B> returns false, Muffin will send the request to a server as 
 * usual.
 *
 * Generally the request is intended for an HTTP server.
 *
 * @author Mark Boyns
 */
public interface HttpFilter extends HttpRelay, Filter {

    /** Returns whether this filter wants to process this request itself.
     * @param request Request
     * @return Whether this filter wants to process the request itself.
     */
    public boolean wantRequest(Request request);
}
