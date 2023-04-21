package org.jcyclone.ext.http;

import org.jcyclone.core.event.BufferElement;
import org.jcyclone.core.queue.*;
import org.jcyclone.ext.asocket.ATcpConnection;
import org.jcyclone.ext.asocket.ATcpInPacket;
import java.io.IOException;

/**
 * This class represents a single HTTP connection. When an httpServer
 * receives a connection, an httpConnection is pushed to the user.
 * To send HTTP responses to a client, you can enqueue an httpResponse
 * object on the corresponding httpConnection.
 *
 * @author Matt Welsh
 * @see HttpRequest
 * @see HttpResponse
 */
public class HttpConnection extends SimpleSink implements HttpConst, IElement {

    private static final boolean DEBUG = false;

    private ATcpConnection tcpconn;

    private HttpServer hs;

    private ISink compQ;

    private HttpPacketReader hpr;

    /**
	 * Can be used by applications to associate an arbitrary data object
	 * with this connection.
	 */
    public Object userTag;

    /**
	 * Package-internal: Create an httpConnection with the given TCP
	 * connection and completion queue.
	 */
    HttpConnection(ATcpConnection tcpconn, HttpServer hs, ISink compQ) {
        this.tcpconn = tcpconn;
        this.hs = hs;
        this.compQ = compQ;
        this.hpr = new HttpPacketReader(this, compQ);
        compQ.enqueueLossy(this);
    }

    /**
	 * Package-internal: Parse the data contained in the given TCP packet.
	 */
    void parsePacket(ATcpInPacket pkt) throws IOException {
        hpr.parsePacket(pkt);
    }

    /**
	 * Return the ATcpConnection associated with this connection.
	 */
    public ATcpConnection getConnection() {
        return tcpconn;
    }

    public String toString() {
        return "httpConnection [conn=" + tcpconn + "]";
    }

    /**
	 * Enqueue outgoing data on this connection. The 'element' must be
	 * of type httpResponder.
	 */
    public void enqueue(IElement element) throws SinkException {
        if (DEBUG) System.err.println("httpConnection.enqueue: " + element);
        HttpResponder resp = (HttpResponder) element;
        HttpResponse packet = resp.getResponse();
        BufferElement bufarr[] = packet.getBuffers(resp.sendHeader());
        tcpconn.enqueue_many(bufarr);
    }

    /**
	 * Enqueue outgoing data on this connection. The 'element' must be
	 * of type httpResponder.
	 */
    public boolean enqueueLossy(IElement element) {
        if (DEBUG) System.err.println("httpConnection.enqueue_lossy: " + element);
        HttpResponder resp = (HttpResponder) element;
        HttpResponse packet = resp.getResponse();
        BufferElement bufarr[] = packet.getBuffers(resp.sendHeader());
        try {
            tcpconn.enqueue_many(bufarr);
        } catch (SinkException se) {
            return false;
        }
        return true;
    }

    /**
	 * Enqueue outgoing data on this connection. Each item in the
	 * elements array must be of type httpResponse.
	 */
    public void enqueue_many(IElement elements[]) throws SinkException {
        for (int i = 0; i < elements.length; i++) {
            enqueue(elements[i]);
        }
    }

    /**
	 * Return the number of outgoing packets waiting to be sent.
	 */
    public int size() {
        return tcpconn.size();
    }

    /**
	 * Close the connection.
	 */
    public void close(final ISink compQ) throws SinkClosedException {
        hs.cleanupConnection(this);
        tcpconn.close(new SimpleSink() {

            public void enqueue(IElement qel) throws SinkException {
                compQ.enqueue(new SinkClosedEvent(HttpConnection.this));
            }
        });
    }

    /**
	 * Flush the connection; a SinkFlushedEvent will be pushed to the
	 * user when all packets have drained.
	 */
    public void flush(ISink compQ) throws SinkClosedException {
        tcpconn.flush(compQ);
    }

    public Object enqueue_prepare(IElement enqueueMe[]) throws SinkException {
        return tcpconn.enqueue_prepare(enqueueMe);
    }

    public void enqueue_commit(Object key) {
        tcpconn.enqueue_commit(key);
    }

    public void enqueue_abort(Object key) {
        tcpconn.enqueue_abort(key);
    }
}
