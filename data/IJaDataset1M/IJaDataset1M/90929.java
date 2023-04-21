package org.yajul.net.http.client;

import org.apache.log4j.Logger;
import org.yajul.io.*;
import org.yajul.net.http.*;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * A substitute for java.net.URLConnection that gives more control over the sockets and HTTP headers.
 */
public class HTTPConnection extends HTTPSocketWrapper {

    /** A logger for this class. **/
    private static Logger log = Logger.getLogger(HTTPConnection.class);

    /** The default socket timeout. */
    public static final int DEFAULT_SOCKET_TIMEOUT = 10000;

    private HTTPClient client;

    private ByteCountingInputStream in;

    private ByteCountingOutputStream out;

    private ByteArrayOutputStream outTrace;

    private ByteArrayOutputStream inTrace;

    /** The state of the connection is unknown. */
    public static final int UNKNOWN = 0;

    /** The connection has been initialized. */
    public static final int INITIALIZED = 1;

    /** The connection is open. */
    public static final int OPEN = 2;

    /** The connection is ready to send the request headers. */
    public static final int REQUEST_HEADERS = 3;

    /** The connection is ready to send the request body. */
    public static final int REQUEST_BODY = 4;

    /** The connection is ready to read the response headers. */
    public static final int RESPONSE_HEADERS = 5;

    /** The connection is ready to read the body. */
    public static final int RESPONSE_BODY = 6;

    /** The connection has been closed. */
    public static final int CLOSED = 99;

    private int state;

    private static final int RESPONSE_BUFFER_SIZE = 4096;

    /**
     * Initializes the state.
     */
    protected void initialize(HTTPClient client, String protocol, String host, int port) {
        this.client = client;
        if (this.client == null) throw new RuntimeException("An instance of HTTPClient is required!");
        super.initialize(protocol, host, port);
        state = INITIALIZED;
        setProxyInfo(this.client.getProxyInfo());
        log.info("HTTPConnection initialized.  " + getProtocol() + "://" + getHost() + ":" + getPort());
    }

    /**
     * Creates an HTTP Connection, given the client, protocol, host and port.
     * @param client A reference to the HTTPClient object.
     * @param protocol Protocol string (i.e. 'http' or 'https')
     * @param host The host name or IP address.
     * @param port The port number.
     */
    public HTTPConnection(HTTPClient client, String protocol, String host, int port) {
        initialize(client, protocol, host, port);
    }

    /**
     * Opens the socket to the HTTP server.
     * @param protocol Protocol to use.
     * @param host Host name or IP address.
     * @param port Port number.
     * @exception java.net.SocketException Thrown if the socket parameters cannot be set.
     * @exception java.io.IOException Thrown if there was a problem creating the socket.
     */
    public void open(String protocol, String host, int port) throws java.net.SocketException, java.io.IOException {
        initialize(client, protocol, host, port);
        super.open();
        getSocket().setSoTimeout(client.getSocketTimeout());
        state = OPEN;
    }

    /** Returns the input stream. */
    public synchronized InputStream getInputStream() {
        return in;
    }

    /** Returns the output stream. */
    public synchronized OutputStream getOutputStream() {
        return out;
    }

    /**
     * Wraps the superclass method for adding buffers to the input and output streams.
     */
    public void addStreamBuffers() {
        InputStream in = super.getInputStream();
        OutputStream out = super.getOutputStream();
        if (log.isDebugEnabled()) {
            log.debug("Adding stream debug traces...");
            outTrace = new ByteArrayOutputStream();
            out = new TeeOutputStream(out, outTrace);
            inTrace = new ByteArrayOutputStream();
            in = new EchoInputStream(in, inTrace);
        }
        this.in = new ByteCountingInputStream(in);
        this.out = new ByteCountingOutputStream(out);
    }

    /**
     * Sends the request to the server.  Returns the
     * server's response in Message.
     * @param req The HTTP request.
     * @exception java.io.IOException
     * @exception java.lang.Exception
     * @return The HTTP response with the input stream positioned at the beginning of the message body.
     */
    public synchronized Message send(RequestHeader req, InputStream content) throws IOException, Exception {
        long start = System.currentTimeMillis();
        open();
        long openTime = System.currentTimeMillis() - start;
        try {
            resetTraceStreams();
            OutputStream os = getOutputStream();
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
            state = REQUEST_HEADERS;
            req.writeRequestLine(out);
            req.writeStandardHeaders(out);
            String proxyAuth = getBasicProxyAuthorization();
            if (proxyAuth != null && req.containsHeader(HeaderConstants.PROXY_AUTH)) {
                HTTPHeader h = new HTTPHeader(HeaderConstants.PROXY_AUTH, proxyAuth);
                h.write(out);
            }
            req.finishHeaders(out);
            out.flush();
            state = REQUEST_BODY;
            if (content != null) StreamCopier.unsyncCopy(content, os, StreamCopier.DEFAULT_BUFFER_SIZE);
            os.flush();
            logDetails("Resquest", req, outTrace);
            state = RESPONSE_HEADERS;
            log.info(getSocketHost() + ":" + getSocketPort() + " < " + req.getMethod() + " " + req.getRequestURI() + " " + this.out.getByteCount() + " bytes written.");
            start = System.currentTimeMillis();
            HTTPInputStream r = new HTTPInputStream(this.getInputStream(), RESPONSE_BUFFER_SIZE);
            if (log.isDebugEnabled()) log.debug("Waiting for response from " + getSocketHost() + ":" + getSocketPort() + " ...");
            r.waitUntilReady();
            long responseTime = System.currentTimeMillis() - start;
            if (log.isDebugEnabled()) log.debug("Reading response from " + getSocketHost() + ":" + getSocketPort() + " (" + responseTime + "ms)");
            ResponseHeader h = new ResponseHeader();
            h.read(r);
            Message res = new Message(r, h);
            state = RESPONSE_BODY;
            if (log.isDebugEnabled()) {
                log.debug("Open time      = " + responseTime);
                log.debug("Response time  = " + openTime);
            }
            MessageHeader header = res.getHeader();
            log.info(getSocketHost() + ":" + getSocketPort() + " > " + header.getStartLine() + " (" + responseTime + "ms) Content-length: " + header.getContentLength());
            logDetails("Response", header, inTrace);
            return res;
        } catch (Exception e) {
            log.error(e, e);
            throw e;
        }
    }

    private void logDetails(String type, GenericMessageHeader header, ByteArrayOutputStream baos) {
        if (log.isDebugEnabled()) log.debug("" + type + ":\n--- " + type + " Headers ---\n" + header.getStartLine() + "\n" + header.getHeadersAsString() + "--- End of " + type + " Headers ---");
        if (baos != null) {
            byte[] buf = baos.toByteArray();
            if (log.isDebugEnabled()) log.debug("\n--- " + type + " Stream ---\n" + HexDumpOutputStream.toHexString(buf, buf.length) + "\n--- End of " + type + " Stream ---");
            baos.reset();
        }
    }

    private void resetTraceStreams() {
        if (outTrace != null) outTrace.reset();
        if (inTrace != null) inTrace.reset();
    }

    /**
     * Closes the connection to the server.
     * @noinspection EmptyCatchBlock
     */
    public void close() {
        if (!isConnected()) return;
        if (log.isDebugEnabled()) log.debug("Closing HTTPConnection...");
        if (outTrace != null) try {
            outTrace.close();
        } catch (Exception ignore) {
        }
        outTrace = null;
        if (out != null) try {
            out.close();
        } catch (Exception ignore) {
        }
        out = null;
        if (inTrace != null) {
            try {
                inTrace.close();
            } catch (Exception ignore) {
            }
        }
        inTrace = null;
        if (in != null) try {
            in.close();
        } catch (Exception ignore) {
        }
        in = null;
        super.close();
        state = CLOSED;
    }

    /**
     * Returns the number of bytes sent so far in the connection.
     */
    public int getBytesSent() {
        return out.getByteCount();
    }

    /**
     * Returns the number of bytes received so far for the connection.
     */
    public int getBytesReceived() {
        return in.getByteCount();
    }

    /**
     * Returns the HTTPClient that is using the connecion.
     */
    public HTTPClient getClient() {
        return client;
    }

    public int getState() {
        return state;
    }
}
