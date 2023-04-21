package org.qedeq.kernel.log;

import java.net.URL;

/**
 * Log event listener. Here one can listen to high level application events.
 *
 * @version $Revision: 1.1 $
 * @author  Michael Meyling
 */
public interface LogListener {

    /**
     * Log message.
     *
     * @param   text    Message.
     */
    public void logMessage(String text);

    /**
     * Log request.
     *
     * @param   text    Request.
     */
    public void logRequest(String text);

    /**
     * Log successful reply for an request.
     *
     * @param   text    Reply.
     */
    public void logSuccessfulReply(String text);

    /**
     * Log failure reply for an request.
     *
     * @param   text        Reply.
     * @param   description Reason for reply.
     */
    public void logFailureReply(String text, String description);

    /**
     * Log message state for URL.
     *
     * @param   text    Message state.
     * @param   url     URL.
     */
    public void logMessageState(String text, URL url);

    /**
     * Log failure state for URL.
     *
     * @param   text    Failure state.
     * @param   url     URL.
     * @param   description Reason.
     */
    public void logFailureState(String text, URL url, String description);

    /**
     * Log successful state for URL.
     *
     * @param   text    State.
     * @param   url     URL.
     */
    public void logSuccessfulState(String text, URL url);
}
