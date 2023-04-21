package org.apache.http.nio.reactor;

/**
 * SessionBufferStatus interface is intended to query the status of session 
 * I/O buffers. 
 * 
 * 
 * @version $Revision: 744545 $
 *
 * @since 4.0
 */
public interface SessionBufferStatus {

    /**
     * Determines if the session input buffer contains data.
     * 
     * @return <code>true</code> if the session input buffer contains data,
     *   <code>false</code> otherwise.
     */
    boolean hasBufferedInput();

    /**
     * Determines if the session output buffer contains data.
     * 
     * @return <code>true</code> if the session output buffer contains data,
     *   <code>false</code> otherwise.
     */
    boolean hasBufferedOutput();
}
