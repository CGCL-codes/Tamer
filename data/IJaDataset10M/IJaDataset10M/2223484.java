package org.apache.http.nio.reactor;

/**
 * SessionRequestCallback interface can be used to get notifications of 
 * completion of session requests asynchronously without having to wait 
 * for it, blocking the current thread of execution. 
 *
 *
 * @version $Revision: 744545 $
 *
 * @since 4.0
 */
public interface SessionRequestCallback {

    /**
     * Triggered on successful completion of a {@link SessionRequest}. 
     * The {@link SessionRequest#getSession()} method can now be used to obtain 
     * the new I/O session. 
     *  
     * @param request session request.
     */
    void completed(SessionRequest request);

    /**
     * Triggered on unsuccessful completion a {@link SessionRequest}. 
     * The {@link SessionRequest#getException()} method can now be used to 
     * obtain the cause of the error. 
     *  
     * @param request session request.
     */
    void failed(SessionRequest request);

    /**
     * Triggered if a {@link SessionRequest} times out. 
     *  
     * @param request session request.
     */
    void timeout(SessionRequest request);

    /**
     * Triggered on cancellation of a {@link SessionRequest}. 
     *  
     * @param request session request.
     */
    void cancelled(SessionRequest request);
}
