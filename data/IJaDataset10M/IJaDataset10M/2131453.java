package org.apache.coyote.ajp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.apache.coyote.ActionCode;
import org.apache.coyote.ActionHook;
import org.apache.coyote.Adapter;
import org.apache.coyote.InputBuffer;
import org.apache.coyote.OutputBuffer;
import org.apache.coyote.Request;
import org.apache.coyote.RequestInfo;
import org.apache.coyote.Response;
import org.apache.tomcat.jni.Socket;
import org.apache.tomcat.jni.Status;
import org.apache.tomcat.util.buf.ByteChunk;
import org.apache.tomcat.util.buf.HexUtils;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.HttpMessages;
import org.apache.tomcat.util.http.MimeHeaders;
import org.apache.tomcat.util.net.AprEndpoint;
import org.apache.tomcat.util.res.StringManager;

/**
 * Processes HTTP requests.
 *
 * @author Remy Maucherat
 * @author Henri Gomez
 * @author Dan Milstein
 * @author Keith Wannamaker
 * @author Kevin Seguin
 * @author Costin Manolache
 * @author Bill Barker
 */
public class AjpAprProcessor implements ActionHook {

    /**
     * Logger.
     */
    protected static org.apache.juli.logging.Log log = org.apache.juli.logging.LogFactory.getLog(AjpAprProcessor.class);

    /**
     * The string manager for this package.
     */
    protected static StringManager sm = StringManager.getManager(Constants.Package);

    public AjpAprProcessor(int packetSize, AprEndpoint endpoint) {
        this.endpoint = endpoint;
        request = new Request();
        request.setInputBuffer(new SocketInputBuffer());
        response = new Response();
        response.setHook(this);
        response.setOutputBuffer(new SocketOutputBuffer());
        request.setResponse(response);
        requestHeaderMessage = new AjpMessage(packetSize);
        responseHeaderMessage = new AjpMessage(packetSize);
        bodyMessage = new AjpMessage(packetSize);
        inputBuffer = ByteBuffer.allocateDirect(packetSize * 2);
        inputBuffer.limit(0);
        outputBuffer = ByteBuffer.allocateDirect(packetSize * 2);
        int foo = HexUtils.DEC[0];
        HttpMessages.getMessage(200);
    }

    /**
     * Associated adapter.
     */
    protected Adapter adapter = null;

    /**
     * Request object.
     */
    protected Request request = null;

    /**
     * Response object.
     */
    protected Response response = null;

    /**
     * Header message. Note that this header is merely the one used during the
     * processing of the first message of a "request", so it might not be a request
     * header. It will stay unchanged during the processing of the whole request.
     */
    protected AjpMessage requestHeaderMessage = null;

    /**
     * Message used for response header composition.
     */
    protected AjpMessage responseHeaderMessage = null;

    /**
     * Body message.
     */
    protected AjpMessage bodyMessage = null;

    /**
     * Body message.
     */
    protected MessageBytes bodyBytes = MessageBytes.newInstance();

    /**
     * State flag.
     */
    protected boolean started = false;

    /**
     * Error flag.
     */
    protected boolean error = false;

    /**
     * Socket associated with the current connection.
     */
    protected long socket;

    /**
     * Host name (used to avoid useless B2C conversion on the host name).
     */
    protected char[] hostNameC = new char[0];

    /**
     * Associated endpoint.
     */
    protected AprEndpoint endpoint;

    /**
     * Temp message bytes used for processing.
     */
    protected MessageBytes tmpMB = MessageBytes.newInstance();

    /**
     * Byte chunk for certs.
     */
    protected MessageBytes certificates = MessageBytes.newInstance();

    /**
     * End of stream flag.
     */
    protected boolean endOfStream = false;

    /**
     * Body empty flag.
     */
    protected boolean empty = true;

    /**
     * First read.
     */
    protected boolean first = true;

    /**
     * Replay read.
     */
    protected boolean replay = false;

    /**
     * Finished response.
     */
    protected boolean finished = false;

    /**
     * Direct buffer used for output.
     */
    protected ByteBuffer outputBuffer = null;

    /**
     * Direct buffer used for input.
     */
    protected ByteBuffer inputBuffer = null;

    /**
     * Direct buffer used for sending right away a get body message.
     */
    protected static final ByteBuffer getBodyMessageBuffer;

    /**
     * Direct buffer used for sending right away a pong message.
     */
    protected static final ByteBuffer pongMessageBuffer;

    /**
     * End message array.
     */
    protected static final byte[] endMessageArray;

    /**
     * Direct buffer used for sending explicit flush message.
     */
    protected static final ByteBuffer flushMessageBuffer;

    static {
        AjpMessage getBodyMessage = new AjpMessage(16);
        getBodyMessage.reset();
        getBodyMessage.appendByte(Constants.JK_AJP13_GET_BODY_CHUNK);
        getBodyMessage.appendInt(Constants.MAX_READ_SIZE);
        getBodyMessage.end();
        getBodyMessageBuffer = ByteBuffer.allocateDirect(getBodyMessage.getLen());
        getBodyMessageBuffer.put(getBodyMessage.getBuffer(), 0, getBodyMessage.getLen());
        AjpMessage pongMessage = new AjpMessage(16);
        pongMessage.reset();
        pongMessage.appendByte(Constants.JK_AJP13_CPONG_REPLY);
        pongMessage.end();
        pongMessageBuffer = ByteBuffer.allocateDirect(pongMessage.getLen());
        pongMessageBuffer.put(pongMessage.getBuffer(), 0, pongMessage.getLen());
        AjpMessage endMessage = new AjpMessage(16);
        endMessage.reset();
        endMessage.appendByte(Constants.JK_AJP13_END_RESPONSE);
        endMessage.appendByte(1);
        endMessage.end();
        endMessageArray = new byte[endMessage.getLen()];
        System.arraycopy(endMessage.getBuffer(), 0, endMessageArray, 0, endMessage.getLen());
        AjpMessage flushMessage = new AjpMessage(16);
        flushMessage.reset();
        flushMessage.appendByte(Constants.JK_AJP13_SEND_BODY_CHUNK);
        flushMessage.appendInt(0);
        flushMessage.appendByte(0);
        flushMessage.end();
        flushMessageBuffer = ByteBuffer.allocateDirect(flushMessage.getLen());
        flushMessageBuffer.put(flushMessage.getBuffer(), 0, flushMessage.getLen());
    }

    /**
     * Use Tomcat authentication ?
     */
    protected boolean tomcatAuthentication = true;

    public boolean getTomcatAuthentication() {
        return tomcatAuthentication;
    }

    public void setTomcatAuthentication(boolean tomcatAuthentication) {
        this.tomcatAuthentication = tomcatAuthentication;
    }

    /**
     * Required secret.
     */
    protected String requiredSecret = null;

    public void setRequiredSecret(String requiredSecret) {
        this.requiredSecret = requiredSecret;
    }

    /** Get the request associated with this processor.
     *
     * @return The request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Process pipelined HTTP requests using the specified input and output
     * streams.
     *
     * @throws IOException error during an I/O operation
     */
    public boolean process(long socket) throws IOException {
        RequestInfo rp = request.getRequestProcessor();
        rp.setStage(org.apache.coyote.Constants.STAGE_PARSE);
        this.socket = socket;
        Socket.setrbb(this.socket, inputBuffer);
        Socket.setsbb(this.socket, outputBuffer);
        error = false;
        boolean openSocket = true;
        boolean keptAlive = false;
        while (started && !error) {
            try {
                if (!readMessage(requestHeaderMessage, true, keptAlive)) {
                    rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);
                    break;
                }
                int type = requestHeaderMessage.getByte();
                if (type == Constants.JK_AJP13_CPING_REQUEST) {
                    if (Socket.sendb(socket, pongMessageBuffer, 0, pongMessageBuffer.position()) < 0) {
                        error = true;
                    }
                    continue;
                } else if (type != Constants.JK_AJP13_FORWARD_REQUEST) {
                    if (log.isDebugEnabled()) {
                        log.debug("Unexpected message: " + type);
                    }
                    continue;
                }
                keptAlive = true;
                request.setStartTime(System.currentTimeMillis());
            } catch (IOException e) {
                error = true;
                break;
            } catch (Throwable t) {
                log.debug(sm.getString("ajpprocessor.header.error"), t);
                response.setStatus(400);
                error = true;
            }
            rp.setStage(org.apache.coyote.Constants.STAGE_PREPARE);
            try {
                prepareRequest();
            } catch (Throwable t) {
                log.debug(sm.getString("ajpprocessor.request.prepare"), t);
                response.setStatus(400);
                error = true;
            }
            if (!error) {
                try {
                    rp.setStage(org.apache.coyote.Constants.STAGE_SERVICE);
                    adapter.service(request, response);
                } catch (InterruptedIOException e) {
                    error = true;
                } catch (Throwable t) {
                    log.error(sm.getString("ajpprocessor.request.process"), t);
                    response.setStatus(500);
                    error = true;
                }
            }
            if (!finished) {
                try {
                    finish();
                } catch (Throwable t) {
                    error = true;
                }
            }
            if (error) {
                response.setStatus(500);
            }
            request.updateCounters();
            rp.setStage(org.apache.coyote.Constants.STAGE_KEEPALIVE);
            recycle();
        }
        if (!error) {
            endpoint.getPoller().add(socket);
        } else {
            openSocket = false;
        }
        rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);
        recycle();
        return openSocket;
    }

    /**
     * Send an action to the connector.
     *
     * @param actionCode Type of the action
     * @param param Action parameter
     */
    public void action(ActionCode actionCode, Object param) {
        if (actionCode == ActionCode.ACTION_COMMIT) {
            if (response.isCommitted()) return;
            try {
                prepareResponse();
            } catch (IOException e) {
                error = true;
            }
        } else if (actionCode == ActionCode.ACTION_CLIENT_FLUSH) {
            if (!response.isCommitted()) {
                try {
                    prepareResponse();
                } catch (IOException e) {
                    error = true;
                    return;
                }
            }
            try {
                flush();
                if (Socket.sendb(socket, flushMessageBuffer, 0, flushMessageBuffer.position()) < 0) {
                    error = true;
                }
            } catch (IOException e) {
                error = true;
            }
        } else if (actionCode == ActionCode.ACTION_CLOSE) {
            try {
                finish();
            } catch (IOException e) {
                error = true;
            }
        } else if (actionCode == ActionCode.ACTION_START) {
            started = true;
        } else if (actionCode == ActionCode.ACTION_STOP) {
            started = false;
        } else if (actionCode == ActionCode.ACTION_REQ_SSL_ATTRIBUTE) {
            if (!certificates.isNull()) {
                ByteChunk certData = certificates.getByteChunk();
                X509Certificate jsseCerts[] = null;
                ByteArrayInputStream bais = new ByteArrayInputStream(certData.getBytes(), certData.getStart(), certData.getLength());
                try {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    X509Certificate cert = (X509Certificate) cf.generateCertificate(bais);
                    jsseCerts = new X509Certificate[1];
                    jsseCerts[0] = cert;
                    request.setAttribute(AprEndpoint.CERTIFICATE_KEY, jsseCerts);
                } catch (java.security.cert.CertificateException e) {
                    log.error(sm.getString("ajpprocessor.certs.fail"), e);
                    return;
                }
            }
        } else if (actionCode == ActionCode.ACTION_REQ_HOST_ATTRIBUTE) {
            if (request.remoteHost().isNull()) {
                try {
                    request.remoteHost().setString(InetAddress.getByName(request.remoteAddr().toString()).getHostName());
                } catch (IOException iex) {
                }
            }
        } else if (actionCode == ActionCode.ACTION_REQ_LOCAL_ADDR_ATTRIBUTE) {
            request.localAddr().setString(request.localName().toString());
        } else if (actionCode == ActionCode.ACTION_REQ_SET_BODY_REPLAY) {
            ByteChunk bc = (ByteChunk) param;
            int length = bc.getLength();
            bodyBytes.setBytes(bc.getBytes(), bc.getStart(), length);
            request.setContentLength(length);
            first = false;
            empty = false;
            replay = true;
        }
    }

    /**
     * Set the associated adapter.
     *
     * @param adapter the new adapter
     */
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Get the associated adapter.
     *
     * @return the associated adapter
     */
    public Adapter getAdapter() {
        return adapter;
    }

    /**
     * After reading the request headers, we have to setup the request filters.
     */
    protected void prepareRequest() {
        byte methodCode = requestHeaderMessage.getByte();
        if (methodCode != Constants.SC_M_JK_STORED) {
            String methodName = Constants.methodTransArray[(int) methodCode - 1];
            request.method().setString(methodName);
        }
        requestHeaderMessage.getBytes(request.protocol());
        requestHeaderMessage.getBytes(request.requestURI());
        requestHeaderMessage.getBytes(request.remoteAddr());
        requestHeaderMessage.getBytes(request.remoteHost());
        requestHeaderMessage.getBytes(request.localName());
        request.setLocalPort(requestHeaderMessage.getInt());
        boolean isSSL = requestHeaderMessage.getByte() != 0;
        if (isSSL) {
            request.scheme().setString("https");
        }
        MimeHeaders headers = request.getMimeHeaders();
        int hCount = requestHeaderMessage.getInt();
        for (int i = 0; i < hCount; i++) {
            String hName = null;
            int isc = requestHeaderMessage.peekInt();
            int hId = isc & 0xFF;
            MessageBytes vMB = null;
            isc &= 0xFF00;
            if (0xA000 == isc) {
                requestHeaderMessage.getInt();
                hName = Constants.headerTransArray[hId - 1];
                vMB = headers.addValue(hName);
            } else {
                hId = -1;
                requestHeaderMessage.getBytes(tmpMB);
                ByteChunk bc = tmpMB.getByteChunk();
                vMB = headers.addValue(bc.getBuffer(), bc.getStart(), bc.getLength());
            }
            requestHeaderMessage.getBytes(vMB);
            if (hId == Constants.SC_REQ_CONTENT_LENGTH || (hId == -1 && tmpMB.equalsIgnoreCase("Content-Length"))) {
                long cl = vMB.getLong();
                if (cl < Integer.MAX_VALUE) request.setContentLength((int) cl);
            } else if (hId == Constants.SC_REQ_CONTENT_TYPE || (hId == -1 && tmpMB.equalsIgnoreCase("Content-Type"))) {
                ByteChunk bchunk = vMB.getByteChunk();
                request.contentType().setBytes(bchunk.getBytes(), bchunk.getOffset(), bchunk.getLength());
            }
        }
        boolean secret = false;
        byte attributeCode;
        while ((attributeCode = requestHeaderMessage.getByte()) != Constants.SC_A_ARE_DONE) {
            switch(attributeCode) {
                case Constants.SC_A_REQ_ATTRIBUTE:
                    requestHeaderMessage.getBytes(tmpMB);
                    String n = tmpMB.toString();
                    requestHeaderMessage.getBytes(tmpMB);
                    String v = tmpMB.toString();
                    request.setAttribute(n, v);
                    break;
                case Constants.SC_A_CONTEXT:
                    requestHeaderMessage.getBytes(tmpMB);
                    break;
                case Constants.SC_A_SERVLET_PATH:
                    requestHeaderMessage.getBytes(tmpMB);
                    break;
                case Constants.SC_A_REMOTE_USER:
                    if (tomcatAuthentication) {
                        requestHeaderMessage.getBytes(tmpMB);
                    } else {
                        requestHeaderMessage.getBytes(request.getRemoteUser());
                    }
                    break;
                case Constants.SC_A_AUTH_TYPE:
                    if (tomcatAuthentication) {
                        requestHeaderMessage.getBytes(tmpMB);
                    } else {
                        requestHeaderMessage.getBytes(request.getAuthType());
                    }
                    break;
                case Constants.SC_A_QUERY_STRING:
                    requestHeaderMessage.getBytes(request.queryString());
                    break;
                case Constants.SC_A_JVM_ROUTE:
                    requestHeaderMessage.getBytes(request.instanceId());
                    break;
                case Constants.SC_A_SSL_CERT:
                    request.scheme().setString("https");
                    requestHeaderMessage.getBytes(certificates);
                    break;
                case Constants.SC_A_SSL_CIPHER:
                    request.scheme().setString("https");
                    requestHeaderMessage.getBytes(tmpMB);
                    request.setAttribute(AprEndpoint.CIPHER_SUITE_KEY, tmpMB.toString());
                    break;
                case Constants.SC_A_SSL_SESSION:
                    request.scheme().setString("https");
                    requestHeaderMessage.getBytes(tmpMB);
                    request.setAttribute(AprEndpoint.SESSION_ID_KEY, tmpMB.toString());
                    break;
                case Constants.SC_A_SSL_KEY_SIZE:
                    request.setAttribute(AprEndpoint.KEY_SIZE_KEY, new Integer(requestHeaderMessage.getInt()));
                    break;
                case Constants.SC_A_STORED_METHOD:
                    requestHeaderMessage.getBytes(request.method());
                    break;
                case Constants.SC_A_SECRET:
                    requestHeaderMessage.getBytes(tmpMB);
                    if (requiredSecret != null) {
                        secret = true;
                        if (!tmpMB.equals(requiredSecret)) {
                            response.setStatus(403);
                            error = true;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        if ((requiredSecret != null) && !secret) {
            response.setStatus(403);
            error = true;
        }
        ByteChunk uriBC = request.requestURI().getByteChunk();
        if (uriBC.startsWithIgnoreCase("http", 0)) {
            int pos = uriBC.indexOf("://", 0, 3, 4);
            int uriBCStart = uriBC.getStart();
            int slashPos = -1;
            if (pos != -1) {
                byte[] uriB = uriBC.getBytes();
                slashPos = uriBC.indexOf('/', pos + 3);
                if (slashPos == -1) {
                    slashPos = uriBC.getLength();
                    request.requestURI().setBytes(uriB, uriBCStart + pos + 1, 1);
                } else {
                    request.requestURI().setBytes(uriB, uriBCStart + slashPos, uriBC.getLength() - slashPos);
                }
                MessageBytes hostMB = headers.setValue("host");
                hostMB.setBytes(uriB, uriBCStart + pos + 3, slashPos - pos - 3);
            }
        }
        MessageBytes valueMB = request.getMimeHeaders().getValue("host");
        parseHost(valueMB);
    }

    /**
     * Parse host.
     */
    public void parseHost(MessageBytes valueMB) {
        if (valueMB == null || (valueMB != null && valueMB.isNull())) {
            request.setServerPort(endpoint.getPort());
            return;
        }
        ByteChunk valueBC = valueMB.getByteChunk();
        byte[] valueB = valueBC.getBytes();
        int valueL = valueBC.getLength();
        int valueS = valueBC.getStart();
        int colonPos = -1;
        if (hostNameC.length < valueL) {
            hostNameC = new char[valueL];
        }
        boolean ipv6 = (valueB[valueS] == '[');
        boolean bracketClosed = false;
        for (int i = 0; i < valueL; i++) {
            char b = (char) valueB[i + valueS];
            hostNameC[i] = b;
            if (b == ']') {
                bracketClosed = true;
            } else if (b == ':') {
                if (!ipv6 || bracketClosed) {
                    colonPos = i;
                    break;
                }
            }
        }
        if (colonPos < 0) {
            if (request.scheme().equalsIgnoreCase("https")) {
                request.setServerPort(443);
            } else {
                request.setServerPort(80);
            }
            request.serverName().setChars(hostNameC, 0, valueL);
        } else {
            request.serverName().setChars(hostNameC, 0, colonPos);
            int port = 0;
            int mult = 1;
            for (int i = valueL - 1; i > colonPos; i--) {
                int charValue = HexUtils.DEC[(int) valueB[i + valueS]];
                if (charValue == -1) {
                    error = true;
                    response.setStatus(400);
                    break;
                }
                port = port + (charValue * mult);
                mult = 10 * mult;
            }
            request.setServerPort(port);
        }
    }

    /**
     * When committing the response, we have to validate the set of headers, as
     * well as setup the response filters.
     */
    protected void prepareResponse() throws IOException {
        response.setCommitted(true);
        responseHeaderMessage.reset();
        responseHeaderMessage.appendByte(Constants.JK_AJP13_SEND_HEADERS);
        responseHeaderMessage.appendInt(response.getStatus());
        String message = null;
        if (org.apache.coyote.Constants.USE_CUSTOM_STATUS_MSG_IN_HEADER) {
            message = response.getMessage();
        }
        if (message == null) {
            message = HttpMessages.getMessage(response.getStatus());
        } else {
            message = message.replace('\n', ' ').replace('\r', ' ');
        }
        tmpMB.setString(message);
        responseHeaderMessage.appendBytes(tmpMB);
        MimeHeaders headers = response.getMimeHeaders();
        String contentType = response.getContentType();
        if (contentType != null) {
            headers.setValue("Content-Type").setString(contentType);
        }
        String contentLanguage = response.getContentLanguage();
        if (contentLanguage != null) {
            headers.setValue("Content-Language").setString(contentLanguage);
        }
        long contentLength = response.getContentLengthLong();
        if (contentLength >= 0) {
            headers.setValue("Content-Length").setLong(contentLength);
        }
        int numHeaders = headers.size();
        responseHeaderMessage.appendInt(numHeaders);
        for (int i = 0; i < numHeaders; i++) {
            MessageBytes hN = headers.getName(i);
            int hC = Constants.getResponseAjpIndex(hN.toString());
            if (hC > 0) {
                responseHeaderMessage.appendInt(hC);
            } else {
                responseHeaderMessage.appendBytes(hN);
            }
            MessageBytes hV = headers.getValue(i);
            responseHeaderMessage.appendBytes(hV);
        }
        responseHeaderMessage.end();
        outputBuffer.put(responseHeaderMessage.getBuffer(), 0, responseHeaderMessage.getLen());
    }

    /**
     * Finish AJP response.
     */
    protected void finish() throws IOException {
        if (!response.isCommitted()) {
            try {
                prepareResponse();
            } catch (IOException e) {
                error = true;
            }
        }
        if (finished) return;
        finished = true;
        if (outputBuffer.position() + endMessageArray.length > outputBuffer.capacity()) {
            flush();
        }
        outputBuffer.put(endMessageArray);
        flush();
    }

    /**
     * Read at least the specified amount of bytes, and place them
     * in the input buffer.
     */
    protected boolean read(int n) throws IOException {
        if (inputBuffer.capacity() - inputBuffer.limit() <= n - inputBuffer.remaining()) {
            inputBuffer.compact();
            inputBuffer.limit(inputBuffer.position());
            inputBuffer.position(0);
        }
        int nRead;
        while (inputBuffer.remaining() < n) {
            nRead = Socket.recvbb(socket, inputBuffer.limit(), inputBuffer.capacity() - inputBuffer.limit());
            if (nRead > 0) {
                inputBuffer.limit(inputBuffer.limit() + nRead);
            } else {
                throw new IOException(sm.getString("ajpprotocol.failedread"));
            }
        }
        return true;
    }

    /**
     * Read at least the specified amount of bytes, and place them
     * in the input buffer.
     */
    protected boolean readt(int n, boolean useAvailableData) throws IOException {
        if (useAvailableData && inputBuffer.remaining() == 0) {
            return false;
        }
        if (inputBuffer.capacity() - inputBuffer.limit() <= n - inputBuffer.remaining()) {
            inputBuffer.compact();
            inputBuffer.limit(inputBuffer.position());
            inputBuffer.position(0);
        }
        int nRead;
        while (inputBuffer.remaining() < n) {
            nRead = Socket.recvbb(socket, inputBuffer.limit(), inputBuffer.capacity() - inputBuffer.limit());
            if (nRead > 0) {
                inputBuffer.limit(inputBuffer.limit() + nRead);
            } else {
                if ((-nRead) == Status.ETIMEDOUT || (-nRead) == Status.TIMEUP) {
                    return false;
                } else {
                    throw new IOException(sm.getString("ajpprotocol.failedread"));
                }
            }
        }
        return true;
    }

    /** Receive a chunk of data. Called to implement the
     *  'special' packet in ajp13 and to receive the data
     *  after we send a GET_BODY packet
     */
    public boolean receive() throws IOException {
        first = false;
        bodyMessage.reset();
        readMessage(bodyMessage, false, false);
        if (bodyMessage.getLen() == 0) {
            return false;
        }
        int blen = bodyMessage.peekInt();
        if (blen == 0) {
            return false;
        }
        bodyMessage.getBytes(bodyBytes);
        empty = false;
        return true;
    }

    /**
     * Get more request body data from the web server and store it in the
     * internal buffer.
     *
     * @return true if there is more data, false if not.
     */
    private boolean refillReadBuffer() throws IOException {
        if (replay) {
            endOfStream = true;
        }
        if (endOfStream) {
            return false;
        }
        Socket.sendb(socket, getBodyMessageBuffer, 0, getBodyMessageBuffer.position());
        boolean moreData = receive();
        if (!moreData) {
            endOfStream = true;
        }
        return moreData;
    }

    /**
     * Read an AJP message.
     *
     * @param first is true if the message is the first in the request, which
     *        will cause a short duration blocking read
     * @return true if the message has been read, false if the short read
     *         didn't return anything
     * @throws IOException any other failure, including incomplete reads
     */
    protected boolean readMessage(AjpMessage message, boolean first, boolean useAvailableData) throws IOException {
        byte[] buf = message.getBuffer();
        int headerLength = message.getHeaderLength();
        if (first) {
            if (!readt(headerLength, useAvailableData)) {
                return false;
            }
        } else {
            read(headerLength);
        }
        inputBuffer.get(message.getBuffer(), 0, headerLength);
        message.processHeader();
        read(message.getLen());
        inputBuffer.get(message.getBuffer(), headerLength, message.getLen());
        return true;
    }

    /**
     * Recycle the processor.
     */
    public void recycle() {
        first = true;
        endOfStream = false;
        empty = true;
        replay = false;
        finished = false;
        request.recycle();
        response.recycle();
        certificates.recycle();
        inputBuffer.clear();
        inputBuffer.limit(0);
        outputBuffer.clear();
    }

    /**
     * Callback to write data from the buffer.
     */
    protected void flush() throws IOException {
        if (outputBuffer.position() > 0) {
            if (Socket.sendbb(socket, 0, outputBuffer.position()) < 0) {
                throw new IOException(sm.getString("ajpprocessor.failedsend"));
            }
            outputBuffer.clear();
        }
    }

    /**
     * This class is an input buffer which will read its data from an input
     * stream.
     */
    protected class SocketInputBuffer implements InputBuffer {

        /**
         * Read bytes into the specified chunk.
         */
        public int doRead(ByteChunk chunk, Request req) throws IOException {
            if (endOfStream) {
                return -1;
            }
            if (first && req.getContentLengthLong() > 0) {
                if (!receive()) {
                    return 0;
                }
            } else if (empty) {
                if (!refillReadBuffer()) {
                    return -1;
                }
            }
            ByteChunk bc = bodyBytes.getByteChunk();
            chunk.setBytes(bc.getBuffer(), bc.getStart(), bc.getLength());
            empty = true;
            return chunk.getLength();
        }
    }

    /**
     * This class is an output buffer which will write data to an output
     * stream.
     */
    protected class SocketOutputBuffer implements OutputBuffer {

        /**
         * Write chunk.
         */
        public int doWrite(ByteChunk chunk, Response res) throws IOException {
            if (!response.isCommitted()) {
                try {
                    prepareResponse();
                } catch (IOException e) {
                    error = true;
                }
            }
            int len = chunk.getLength();
            int chunkSize = Constants.MAX_SEND_SIZE;
            int off = 0;
            while (len > 0) {
                int thisTime = len;
                if (thisTime > chunkSize) {
                    thisTime = chunkSize;
                }
                len -= thisTime;
                if (outputBuffer.position() + thisTime + Constants.H_SIZE + 4 > outputBuffer.capacity()) {
                    flush();
                }
                outputBuffer.put((byte) 0x41);
                outputBuffer.put((byte) 0x42);
                outputBuffer.putShort((short) (thisTime + 4));
                outputBuffer.put(Constants.JK_AJP13_SEND_BODY_CHUNK);
                outputBuffer.putShort((short) thisTime);
                outputBuffer.put(chunk.getBytes(), chunk.getOffset() + off, thisTime);
                outputBuffer.put((byte) 0x00);
                off += thisTime;
            }
            return chunk.getLength();
        }
    }
}
