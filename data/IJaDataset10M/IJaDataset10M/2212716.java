package com.worldware.mail.pop3;

import java.io.*;
import java.util.Vector;
import com.worldware.misc.*;

/** This is a pop3 client implementation that uses streams to connect to the remote server */
public abstract class POP3ClientAdaptStream extends POP3ClientAdapt {

    protected DataInputStream inStream;

    protected DataOutputStream outStream;

    /** Only for use by subclasses for factory class
	 */
    protected POP3ClientAdaptStream() {
        super();
    }

    public POP3ClientAdaptStream(String host) {
        this(host, POP3.defaultPort);
    }

    public POP3ClientAdaptStream(String host, int port) {
        super(host, port);
    }

    /** Connects to the POP3 server,and opens input and output streams.
	 * the interface will be ready to log on after this call */
    public abstract void connect() throws IOException;

    /** Shuts down the connection immediately. You should call this if you get an exception.*/
    public boolean close() throws IOException {
        boolean rc = true;
        if (inStream != null) {
            try {
                inStream.close();
            } catch (IOException e) {
                Log.error("POP3ClientAdaptStream.close: error closing socket input stream" + e);
                e.printStackTrace();
                rc = false;
            } finally {
                inStream = null;
            }
        }
        if (outStream != null) {
            try {
                outStream.close();
            } catch (IOException e) {
                Log.error("POP3ClientAdaptStream.close: error closing socket output stream" + e);
                e.printStackTrace();
                rc = false;
            } finally {
                outStream = null;
            }
        }
        return rc;
    }

    /** Reads one line. response DOES NOT includes CR/LF termination */
    public String readResponse() throws IOException {
        String response = inStream.readLine();
        if (response == null) {
            Log.error("T" + Thread.currentThread().getName() + ": POP3ClientAdaptStream: null return from inStrean.readLine()");
            throw new IOException("POP3ClientAdaptStream: null return from inStrean.readLine()");
        }
        return response;
    }

    /** Reads one line. response Includes CR/LF termination */
    public String readResponseXXXX() throws IOException {
        String response = this.readResponse();
        return POP3.adjustLineEnd(response);
    }

    /** Conveys a command from the client to the server
	 * @param s The command line to send from the client to the server
	 */
    void fromClient(String s) throws IOException {
        outStream.writeBytes(s);
    }
}
