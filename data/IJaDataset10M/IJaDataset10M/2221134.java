package com.kenmccrary.jtella;

import com.kenmccrary.jtella.util.Log;

/** 
 *  Session that can be used to respond to Query messages and receive
 *  Push messages
 *
 */
public class FileServerSession {

    private Router router;

    private MessageReceiver receiver;

    /**
	 *  Constructs the <code>FileServerSession</code>, not visible
	 *  to application
	 *
	 */
    FileServerSession(Router router, MessageReceiver receiver) {
        this.router = router;
        this.receiver = receiver;
        router.addSearchMessageReceiver(receiver);
        router.addPushMessageReceiver(receiver);
    }

    /**
	 *  Closes the session
	 *
	 */
    public void close() {
        router.removeSearchMessageReceiver(receiver);
        router.removePushMessageReceiver(receiver);
    }

    /**
	 *  An application should call <code>queryHit</code> to indicate that
	 *  a search query is satisfied
	 *
	 *  @param request search message which has a hit
	 *  @param response hit information
	 */
    public void queryHit(SearchMessage request, SearchReplyMessage response) {
        NodeConnection connection = router.getQuerySource(request);
        try {
            if (null != connection) {
                connection.prioritySend(response);
            } else {
                Log.getLog().logDebug("Null connection for query: \n" + request.toString());
            }
        } catch (Exception e) {
            Log.getLog().log(e);
        }
    }
}
