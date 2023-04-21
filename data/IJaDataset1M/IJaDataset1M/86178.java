package ircam.jmax.fts;

import java.io.*;
import java.net.*;

/**
 * The socket connection.
 * Implement a specialed connection using a socket (client side)
 * for communicating with FTS
 */
class FtsSocketStream extends FtsStream {

    String host;

    int port;

    Socket socket;

    InputStream in_stream = null;

    OutputStream out_stream = null;

    FtsSocketStream(String host, int port) {
        super(host + ":" + port);
        this.host = host;
        this.port = port;
        try {
            socket = new Socket(host, port);
            out_stream = new BufferedOutputStream(socket.getOutputStream(), 1024);
            in_stream = new BufferedInputStream(socket.getInputStream(), 1024);
        } catch (UnknownHostException e) {
            System.out.println("Don't know about host " + host + ":" + port);
        } catch (IOException e) {
            System.out.println("Couldn't get I/O for the connection to " + host + ":" + port);
        }
    }

    /** Close the connection and shutdown the server */
    void close() {
        try {
            in_stream.close();
            out_stream.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("I/O failed on closing the connection to " + host + ":" + port);
        }
    }

    /** Check if the connection is opened */
    boolean isOpen() {
        return (in_stream != null) && (out_stream != null);
    }

    /** Method to send a char; since we can use datagram sockets or other
    means I/O is not necessarly done thru streams */
    protected void write(int data) throws java.io.IOException {
        out_stream.write(data);
    }

    /** Method to receive a char; since we can use datagram sockets or other
    means I/O is not necessarly done thru streams */
    protected int read() throws java.io.IOException, FtsQuittedException {
        int c;
        c = in_stream.read();
        if (c == -1) throw new FtsQuittedException();
        return c;
    }

    /** Method to Ask for an explicit output flush ; since we
    can use datagram sockets or other means I/O is not necessarly done
    thru streams */
    void flush() throws java.io.IOException {
        out_stream.flush();
    }
}
