package org.jcyclone.ext.asocket;

import org.jcyclone.core.queue.IElement;
import org.jcyclone.core.queue.ISink;

/**
 * Request to initiate read events on a UDP socket.
 */
public class AUdpStartReadRequest extends ASocketRequest implements IElement {

    AUdpSocket sock;

    ISink compQ;

    int readClogTries;

    public AUdpStartReadRequest(AUdpSocket sock, ISink compQ, int readClogTries) {
        this.sock = sock;
        this.compQ = compQ;
        this.readClogTries = readClogTries;
    }
}
