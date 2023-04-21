package org.jcyclone.ext.asocket;

/**
 * ATcpListenFailedEvent objects will be passed to the ISink associated
 * with an ATcpServerSocket when an attempt to create that server socket
 * fails.
 *
 * @author Matt Welsh
 * @see ATcpServerSocket
 */
public class ATcpListenFailedEvent extends ASocketErrorEvent {

    public ATcpServerSocket theSocket;

    public ATcpListenFailedEvent(ATcpServerSocket sock, String message) {
        super(message);
        theSocket = sock;
    }

    /**
	 * Return the ATcpServerSocket for which the listen failed.
	 */
    public ATcpServerSocket getSocket() {
        return theSocket;
    }

    public String toString() {
        return "ATcpListenFailedEvent: " + getMessage();
    }
}
