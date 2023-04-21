package com.jabberwookie;

import java.util.Hashtable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.jabberwookie.ns.jabber.Chunk;
import com.jabberwookie.UnrecognizedChunkListener;
import com.ssttr.crypto.SHA1;
import com.ssttr.util.processor.Processor;

/**
 * Implements the required methods to establish a connection to a
 * Jabber server as a component.  This allows you to create new
 * services for the server. 
 * @author  smeiners
 */
public class Component2Server extends Stream implements UnrecognizedChunkListener {

    private static final String STREAM_OPENER = "<stream:stream xmlns:stream='http://etherx.jabber.org/streams' " + "xmlns='jabber:component:accept' to='";

    private static final String STREAM_CLOSER = "</stream:stream>";

    private String secret = "";

    private boolean authenticated = false;

    /** Creates a new instance of Component2Server.  Since this class
     * doesn't understand what a Socket is you'll have to open
     * one youself and then pass in the input and output stream.
     * @param secret The secret used to authenticate yourself to
     * the Jabber server. */
    public Component2Server(InputStream in, OutputStream out, String secret) {
        super(in, out);
        _init(secret);
    }

    /** Creates a new instance of Component2Server.  Since this class
     * doesn't understand what a Socket is you'll have to open
     * one youself and then pass in the input and output stream.
     * Please see the documentation for
     * {@link Stream#Stream(InputStream,OutputStream,Processor)}
     * and {@link Stream#setProcessor} for more information about
     * the Processor.
     * @param secret The secret used to authenticate yourself to
     * the Jabber server.
     */
    public Component2Server(InputStream in, OutputStream out, String secret, Processor processor) {
        super(in, out, processor);
        _init(secret);
    }

    private void _init(String secret) {
        unListener = this;
        this.secret = secret;
    }

    /**
     * Since Jabber components are set to receive data from specific
     * namespaces only this overrides the default
     * {@link Stream#setUnrecogizedChunkListener} to do nothing.
     */
    public void setUnrecogizedChunkListener(UnrecognizedChunkListener listener) {
    }

    /**
     * Opens the stream and attempts to authenticate itself.
     */
    public boolean open(String serverName, int timeoutSecs) throws IOException {
        if (DEBUG) System.out.println("SEND: " + (STREAM_OPENER + serverName + "'>"));
        if (ns == null) ns = new Namespaces();
        out.write((STREAM_OPENER + serverName + "'>").getBytes());
        out.flush();
        long stopTime = System.currentTimeMillis() + timeoutSecs * 1000;
        while (!connected && !authenticated && System.currentTimeMillis() < stopTime) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException x) {
            }
        }
        return connected && authenticated;
    }

    /**
     * Closes the stream.
     */
    public synchronized void close() {
        try {
            if (DEBUG) System.out.println("SEND: " + (STREAM_CLOSER));
            if (out != null) {
                out.write(STREAM_CLOSER.getBytes());
                out.flush();
            }
        } catch (IOException x) {
        }
        if (in != null) try {
            in.close();
        } catch (IOException x) {
        }
        if (out != null) try {
            out.close();
        } catch (IOException x) {
        }
        connected = false;
        authenticated = false;
        in = null;
        out = null;
    }

    public void docStart(String tag, Hashtable attrs) {
        super.docStart(tag, attrs);
        SHA1 md = new SHA1();
        String resp = getConnectionId() + secret;
        if (DEBUG) System.out.println("Making hash from: '" + resp + "'");
        md.computeDigest(resp.getBytes());
        String hash = md.getDigestHex();
        Chunk hs = new Chunk("handshake");
        hs.setValue(hash);
        try {
            send(hs);
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    /** From UnrecognizedChunkListener, needed for authentication. */
    public void incomingChunk(Chunk chunk) {
        if (chunk.getName().equals("handshake")) authenticated = true;
    }
}
