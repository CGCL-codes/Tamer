package com.fujitsu.arcon.gateway;

/**
 * A connection to a Vsite
 *
 * @author Sven van den Berghe, fujitsu
 *
 * @version $Id: VsiteConnection.java,v 1.2 2004/06/06 18:41:53 svenvdb Exp $
 *
 **/
public interface VsiteConnection {

    /**
     * Process a request from a client.
     *
     * @param oos An ObjectOutputStream opened on the socket from the client.
     *            Nothing is written to this by the Gateway but as the Gateway
     *            opens this and there may be some state set up by it it is
     *            passed on. (I do not think that this is really necessary).
     *
     * @param ois An ObjectInputStream opened on the socket from the client.
     *            The Gateway will have read a UPL header object from this stream.
     *            As this will have created some state on the client it is passed
     *            on to handling code. This is necessary and also means that the
     *            client must write a UPL object onto the stream.
     *
     * @param socket The socket from the client
     *
     * @param sr The UPL header object from the client with the Consignor field
     *           set using the public certificate presented by the client.
     *
     **/
    public void processRequest(java.io.ObjectOutputStream oos, java.io.ObjectInputStream ois, java.net.Socket socket, org.unicore.upl.ServerRequest sr) throws Exception;

    /**
     * Close the connection and any sockets, streams
     * that it may have open.
     *
     **/
    public void close();

    /**
     * Return the name of the Vsite that this is
     * connected to.
     *
     **/
    public String getTarget();
}
