package com.bitgate.util.services.protocol;

import com.bitgate.util.services.WorkerContext;

/**
 * For request handlers to work properly, the name of the class <B>MUST BE IN ALL CAPS.</B>  No exceptions.  This is so
 * the <code>Services</code> class understands how to load in your handler.
 *
 * @version $Id: //depot/nuklees/util/services/protocol/RequestHandler.java#6 $
 * @author Kenji Hollis &kt;kenji@nuklees.com&gt;
 */
public interface RequestHandler {

    /**
     * This is the "GET" request handler for the protocol you wish to implement.
     *
     * @param ww Worker class calling this instance.
     * @param request The actual incoming request.
     * @param docRoot Document Root directory.
     * @param defaultRoot Default Root directory.
     * @param requestHost The request host.
     */
    void handleGetRequest(WorkerContext ww, String request, String docRoot, String defaultRoot, String requestHost);

    /**
     * This is the "HEAD" request handler for the protocol you wish to implement.
     *
     * @param ww Worker class calling this instance.
     * @param request The actual incoming request.
     * @param docRoot Document Root directory.
     * @param defaultRoot Default Root directory.
     * @param requestHost The request host.
     */
    void handleHeadRequest(WorkerContext ww, String request, String docRoot, String defaultRoot, String requestHost);

    /**
     * This is the "POST" request handler for the protocol you wish to implement.
     *
     * @param ww Worker class calling this instance.
     * @param request The actual incoming request.
     * @param postContent The data that was posted.
     * @param docRoot Document Root directory.
     * @param defaultRoot Default Root directory.
     * @param requestHost The request host.
     */
    void handlePostRequest(WorkerContext ww, String request, String postContent, String docRoot, String defaultRoot, String requestHost);

    /**
     * This is the request handler for the protocol you wish to implement, which handles requests other than those
     * listed above (ie. GET/HEAD/POST.)  This is useful for other protocols that you wish to implement and create
     * clients for.  Especially for streaming audio (such as ShoutCast, or Winamp) or other streaming media types.
     *
     * @param ww Worker class calling this instance.
     * @param request The actual incoming request.
     * @param postContent The data that was posted.
     * @param docRoot Document Root directory.
     * @param defaultRoot Default Root directory.
     * @param requestHost The request host.
     */
    void handleUnknownRequest(WorkerContext ww, String request, String postContent, String docRoot, String defaultRoot, String requestHost);

    /**
     * Stateless/Stateful mode connection - keep connection open for additional requests (one after another) or
     * close connection after request.
     *
     * @param ww Worker class calling this instance.
     * @return boolean true if stateful (keeps connection open), false otherwise.
     */
    boolean isStateful(WorkerContext ww);
}
