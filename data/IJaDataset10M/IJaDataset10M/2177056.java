package org.apache.http.nio.reactor;

import java.io.IOException;

/**
 * HttpCore NIO is based on the Reactor pattern as described by Doug Lea. 
 * The purpose of I/O reactors is to react to I/O events and to dispatch event 
 * notifications to individual I/O sessions. The main idea of I/O reactor 
 * pattern is to break away from the one thread per connection model imposed 
 * by the classic blocking I/O model. 
 * <p>
 * The IOReactor interface represents an abstract object implementing 
 * the Reactor pattern. 
 * <p>
 * I/O reactors usually employ a small number of dispatch threads (often as 
 * few as one) to dispatch I/O event notifications to a much greater number 
 * (often as many as several thousands) of I/O sessions or connections. It is 
 * generally recommended to have one dispatch thread per CPU core.
 *
 *
 * @version $Revision: 744545 $
 *
 * @since 4.0
 */
public interface IOReactor {

    /**
     * Returns the current status of the reactor.
     *  
     * @return reactor status.
     */
    IOReactorStatus getStatus();

    /**
     * Starts the reactor and initiates the dispatch of I/O event notifications
     * to the given {@link IOEventDispatch}.
     * 
     * @param eventDispatch the I/O event dispatch.
     * @throws IOException in case of an I/O error.
     */
    void execute(IOEventDispatch eventDispatch) throws IOException;

    /**
     * Initiates shutdown of the reactor and blocks approximately for the given
     * period of time in milliseconds waiting for the reactor to terminate all
     * active connections, to shut down itself and to release system resources 
     * it currently holds. 
     * 
     * @param waitMs wait time in milliseconds.
     * @throws IOException in case of an I/O error.
     */
    void shutdown(long waitMs) throws IOException;

    /**
     * Initiates shutdown of the reactor and blocks for a default period of 
     * time waiting for the reactor to terminate all active connections, to shut 
     * down itself and to release system resources it currently holds. It is 
     * up to individual implementations to decide for how long this method can
     * remain blocked.  
     * 
     * @throws IOException in case of an I/O error.
     */
    void shutdown() throws IOException;
}
