package org.apache.coyote.http11;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.coyote.ActionCode;
import org.apache.coyote.ActionHook;
import org.apache.coyote.Adapter;
import org.apache.coyote.Request;
import org.apache.coyote.RequestInfo;
import org.apache.coyote.Response;
import org.apache.coyote.http11.filters.BufferedInputFilter;
import org.apache.coyote.http11.filters.ChunkedInputFilter;
import org.apache.coyote.http11.filters.ChunkedOutputFilter;
import org.apache.coyote.http11.filters.GzipOutputFilter;
import org.apache.coyote.http11.filters.IdentityInputFilter;
import org.apache.coyote.http11.filters.IdentityOutputFilter;
import org.apache.coyote.http11.filters.SavedRequestInputFilter;
import org.apache.coyote.http11.filters.VoidInputFilter;
import org.apache.coyote.http11.filters.VoidOutputFilter;
import org.apache.tomcat.util.buf.Ascii;
import org.apache.tomcat.util.buf.ByteChunk;
import org.apache.tomcat.util.buf.HexUtils;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.FastHttpDateFormat;
import org.apache.tomcat.util.http.MimeHeaders;
import org.apache.tomcat.util.net.JIoEndpoint;
import org.apache.tomcat.util.net.SSLSupport;
import org.apache.tomcat.util.res.StringManager;

/**
 * Processes HTTP requests.
 *
 * @author Remy Maucherat
 */
public class Http11Processor implements ActionHook {

    /**
     * Logger.
     */
    protected static org.apache.juli.logging.Log log = org.apache.juli.logging.LogFactory.getLog(Http11Processor.class);

    /**
     * The string manager for this package.
     */
    protected static StringManager sm = StringManager.getManager(Constants.Package);

    public Http11Processor(int headerBufferSize, JIoEndpoint endpoint) {
        this.endpoint = endpoint;
        request = new Request();
        inputBuffer = new InternalInputBuffer(request, headerBufferSize);
        request.setInputBuffer(inputBuffer);
        response = new Response();
        response.setHook(this);
        outputBuffer = new InternalOutputBuffer(response, headerBufferSize);
        response.setOutputBuffer(outputBuffer);
        request.setResponse(response);
        initializeFilters();
        int foo = HexUtils.DEC[0];
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
     * Input.
     */
    protected InternalInputBuffer inputBuffer = null;

    /**
     * Output.
     */
    protected InternalOutputBuffer outputBuffer = null;

    /**
     * State flag.
     */
    protected boolean started = false;

    /**
     * Error flag.
     */
    protected boolean error = false;

    /**
     * Keep-alive.
     */
    protected boolean keepAlive = true;

    /**
     * HTTP/1.1 flag.
     */
    protected boolean http11 = true;

    /**
     * HTTP/0.9 flag.
     */
    protected boolean http09 = false;

    /**
     * Content delimitator for the request (if false, the connection will
     * be closed at the end of the request).
     */
    protected boolean contentDelimitation = true;

    /**
     * Is there an expectation ?
     */
    protected boolean expectation = false;

    /**
     * List of restricted user agents.
     */
    protected Pattern[] restrictedUserAgents = null;

    /**
     * Maximum number of Keep-Alive requests to honor.
     */
    protected int maxKeepAliveRequests = -1;

    /**
     * The number of seconds Tomcat will wait for a subsequent request
     * before closing the connection.
     */
    protected int keepAliveTimeout = -1;

    /**
     * SSL information.
     */
    protected SSLSupport sslSupport;

    /**
     * Socket associated with the current connection.
     */
    protected Socket socket;

    /**
     * Remote Address associated with the current connection.
     */
    protected String remoteAddr = null;

    /**
     * Remote Host associated with the current connection.
     */
    protected String remoteHost = null;

    /**
     * Local Host associated with the current connection.
     */
    protected String localName = null;

    /**
     * Local port to which the socket is connected
     */
    protected int localPort = -1;

    /**
     * Remote port to which the socket is connected
     */
    protected int remotePort = -1;

    /**
     * The local Host address.
     */
    protected String localAddr = null;

    /**
     * Maximum timeout on uploads. 5 minutes as in Apache HTTPD server.
     */
    protected int timeout = 300000;

    /**
     * Flag to disable setting a different time-out on uploads.
     */
    protected boolean disableUploadTimeout = false;

    /**
     * Allowed compression level.
     */
    protected int compressionLevel = 0;

    /**
     * Minimum contentsize to make compression.
     */
    protected int compressionMinSize = 2048;

    /**
     * Socket buffering.
     */
    protected int socketBuffer = -1;

    /**
     * Max saved post size.
     */
    protected int maxSavePostSize = 4 * 1024;

    /**
     * List of user agents to not use gzip with
     */
    protected Pattern noCompressionUserAgents[] = null;

    /**
     * List of MIMES which could be gzipped
     */
    protected String[] compressableMimeTypes = { "text/html", "text/xml", "text/plain" };

    /**
     * Host name (used to avoid useless B2C conversion on the host name).
     */
    protected char[] hostNameC = new char[0];

    /**
     * Associated endpoint.
     */
    protected JIoEndpoint endpoint;

    /**
     * Allow a customized the server header for the tin-foil hat folks.
     */
    protected String server = null;

    /**
     * Return compression level.
     */
    public String getCompression() {
        switch(compressionLevel) {
            case 0:
                return "off";
            case 1:
                return "on";
            case 2:
                return "force";
        }
        return "off";
    }

    /**
     * Set compression level.
     */
    public void setCompression(String compression) {
        if (compression.equals("on")) {
            this.compressionLevel = 1;
        } else if (compression.equals("force")) {
            this.compressionLevel = 2;
        } else if (compression.equals("off")) {
            this.compressionLevel = 0;
        } else {
            try {
                compressionMinSize = Integer.parseInt(compression);
                this.compressionLevel = 1;
            } catch (Exception e) {
                this.compressionLevel = 0;
            }
        }
    }

    /**
     * Set Minimum size to trigger compression.
     */
    public void setCompressionMinSize(int compressionMinSize) {
        this.compressionMinSize = compressionMinSize;
    }

    /**
     * Add user-agent for which gzip compression didn't works
     * The user agent String given will be exactly matched
     * to the user-agent header submitted by the client.
     *
     * @param userAgent user-agent string
     */
    public void addNoCompressionUserAgent(String userAgent) {
        try {
            Pattern nRule = Pattern.compile(userAgent);
            noCompressionUserAgents = addREArray(noCompressionUserAgents, nRule);
        } catch (PatternSyntaxException pse) {
            log.error(sm.getString("http11processor.regexp.error", userAgent), pse);
        }
    }

    /**
     * Set no compression user agent list (this method is best when used with
     * a large number of connectors, where it would be better to have all of
     * them referenced a single array).
     */
    public void setNoCompressionUserAgents(Pattern[] noCompressionUserAgents) {
        this.noCompressionUserAgents = noCompressionUserAgents;
    }

    /**
     * Set no compression user agent list.
     * List contains users agents separated by ',' :
     *
     * ie: "gorilla,desesplorer,tigrus"
     */
    public void setNoCompressionUserAgents(String noCompressionUserAgents) {
        if (noCompressionUserAgents != null) {
            StringTokenizer st = new StringTokenizer(noCompressionUserAgents, ",");
            while (st.hasMoreTokens()) {
                addNoCompressionUserAgent(st.nextToken().trim());
            }
        }
    }

    /**
     * Add a mime-type which will be compressable
     * The mime-type String will be exactly matched
     * in the response mime-type header .
     *
     * @param mimeType mime-type string
     */
    public void addCompressableMimeType(String mimeType) {
        compressableMimeTypes = addStringArray(compressableMimeTypes, mimeType);
    }

    /**
     * Set compressable mime-type list (this method is best when used with
     * a large number of connectors, where it would be better to have all of
     * them referenced a single array).
     */
    public void setCompressableMimeTypes(String[] compressableMimeTypes) {
        this.compressableMimeTypes = compressableMimeTypes;
    }

    /**
     * Set compressable mime-type list
     * List contains users agents separated by ',' :
     *
     * ie: "text/html,text/xml,text/plain"
     */
    public void setCompressableMimeTypes(String compressableMimeTypes) {
        if (compressableMimeTypes != null) {
            this.compressableMimeTypes = null;
            StringTokenizer st = new StringTokenizer(compressableMimeTypes, ",");
            while (st.hasMoreTokens()) {
                addCompressableMimeType(st.nextToken().trim());
            }
        }
    }

    /**
     * Return the list of restricted user agents.
     */
    public String[] findCompressableMimeTypes() {
        return (compressableMimeTypes);
    }

    /**
     * Add input or output filter.
     *
     * @param className class name of the filter
     */
    protected void addFilter(String className) {
        try {
            Class clazz = Class.forName(className);
            Object obj = clazz.newInstance();
            if (obj instanceof InputFilter) {
                inputBuffer.addFilter((InputFilter) obj);
            } else if (obj instanceof OutputFilter) {
                outputBuffer.addFilter((OutputFilter) obj);
            } else {
                log.warn(sm.getString("http11processor.filter.unknown", className));
            }
        } catch (Exception e) {
            log.error(sm.getString("http11processor.filter.error", className), e);
        }
    }

    /**
     * General use method
     *
     * @param sArray the StringArray
     * @param value string
     */
    private String[] addStringArray(String sArray[], String value) {
        String[] result = null;
        if (sArray == null) {
            result = new String[1];
            result[0] = value;
        } else {
            result = new String[sArray.length + 1];
            for (int i = 0; i < sArray.length; i++) result[i] = sArray[i];
            result[sArray.length] = value;
        }
        return result;
    }

    /**
     * General use method
     *
     * @param rArray the REArray
     * @param value Obj
     */
    private Pattern[] addREArray(Pattern rArray[], Pattern value) {
        Pattern[] result = null;
        if (rArray == null) {
            result = new Pattern[1];
            result[0] = value;
        } else {
            result = new Pattern[rArray.length + 1];
            for (int i = 0; i < rArray.length; i++) result[i] = rArray[i];
            result[rArray.length] = value;
        }
        return result;
    }

    /**
     * General use method
     *
     * @param sArray the StringArray
     * @param value string
     */
    private boolean inStringArray(String sArray[], String value) {
        for (int i = 0; i < sArray.length; i++) {
            if (sArray[i].equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if any entry in the string array starts with the specified value
     *
     * @param sArray the StringArray
     * @param value string
     */
    private boolean startsWithStringArray(String sArray[], String value) {
        if (value == null) return false;
        for (int i = 0; i < sArray.length; i++) {
            if (value.startsWith(sArray[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add restricted user-agent (which will downgrade the connector
     * to HTTP/1.0 mode). The user agent String given will be matched
     * via regexp to the user-agent header submitted by the client.
     *
     * @param userAgent user-agent string
     */
    public void addRestrictedUserAgent(String userAgent) {
        try {
            Pattern nRule = Pattern.compile(userAgent);
            restrictedUserAgents = addREArray(restrictedUserAgents, nRule);
        } catch (PatternSyntaxException pse) {
            log.error(sm.getString("http11processor.regexp.error", userAgent), pse);
        }
    }

    /**
     * Set restricted user agent list (this method is best when used with
     * a large number of connectors, where it would be better to have all of
     * them referenced a single array).
     */
    public void setRestrictedUserAgents(Pattern[] restrictedUserAgents) {
        this.restrictedUserAgents = restrictedUserAgents;
    }

    /**
     * Set restricted user agent list (which will downgrade the connector
     * to HTTP/1.0 mode). List contains users agents separated by ',' :
     *
     * ie: "gorilla,desesplorer,tigrus"
     */
    public void setRestrictedUserAgents(String restrictedUserAgents) {
        if (restrictedUserAgents != null) {
            StringTokenizer st = new StringTokenizer(restrictedUserAgents, ",");
            while (st.hasMoreTokens()) {
                addRestrictedUserAgent(st.nextToken().trim());
            }
        }
    }

    /**
     * Return the list of restricted user agents.
     */
    public String[] findRestrictedUserAgents() {
        String[] sarr = new String[restrictedUserAgents.length];
        for (int i = 0; i < restrictedUserAgents.length; i++) sarr[i] = restrictedUserAgents[i].toString();
        return (sarr);
    }

    /**
     * Set the maximum number of Keep-Alive requests to honor.
     * This is to safeguard from DoS attacks.  Setting to a negative
     * value disables the check.
     */
    public void setMaxKeepAliveRequests(int mkar) {
        maxKeepAliveRequests = mkar;
    }

    /**
     * Return the number of Keep-Alive requests that we will honor.
     */
    public int getMaxKeepAliveRequests() {
        return maxKeepAliveRequests;
    }

    /**
     * Set the Keep-Alive timeout.
     */
    public void setKeepAliveTimeout(int timeout) {
        keepAliveTimeout = timeout;
    }

    /**
     * Return the number Keep-Alive timeout.
     */
    public int getKeepAliveTimeout() {
        return keepAliveTimeout;
    }

    /**
     * Set the maximum size of a POST which will be buffered in SSL mode.
     */
    public void setMaxSavePostSize(int msps) {
        maxSavePostSize = msps;
    }

    /**
     * Return the maximum size of a POST which will be buffered in SSL mode.
     */
    public int getMaxSavePostSize() {
        return maxSavePostSize;
    }

    /**
     * Set the SSL information for this HTTP connection.
     */
    public void setSSLSupport(SSLSupport sslSupport) {
        this.sslSupport = sslSupport;
    }

    /**
     * Set the flag to control upload time-outs.
     */
    public void setDisableUploadTimeout(boolean isDisabled) {
        disableUploadTimeout = isDisabled;
    }

    /**
     * Get the flag that controls upload time-outs.
     */
    public boolean getDisableUploadTimeout() {
        return disableUploadTimeout;
    }

    /**
     * Set the socket buffer flag.
     */
    public void setSocketBuffer(int socketBuffer) {
        this.socketBuffer = socketBuffer;
        outputBuffer.setSocketBuffer(socketBuffer);
    }

    /**
     * Get the socket buffer flag.
     */
    public int getSocketBuffer() {
        return socketBuffer;
    }

    /**
     * Set the upload timeout.
     */
    public void setTimeout(int timeouts) {
        timeout = timeouts;
    }

    /**
     * Get the upload timeout.
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Set the server header name.
     */
    public void setServer(String server) {
        if (server == null || server.equals("")) {
            this.server = null;
        } else {
            this.server = server;
        }
    }

    /**
     * Get the server header name.
     */
    public String getServer() {
        return server;
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
     * @param input stream from which the HTTP requests will be read
     * @param output stream which will be used to output the HTTP
     * responses
     * @throws IOException error during an I/O operation
     */
    public void process(Socket socket) throws IOException {
        RequestInfo rp = request.getRequestProcessor();
        rp.setStage(org.apache.coyote.Constants.STAGE_PARSE);
        remoteAddr = null;
        remoteHost = null;
        localAddr = null;
        localName = null;
        remotePort = -1;
        localPort = -1;
        this.socket = socket;
        inputBuffer.setInputStream(socket.getInputStream());
        outputBuffer.setOutputStream(socket.getOutputStream());
        error = false;
        keepAlive = true;
        int keepAliveLeft = maxKeepAliveRequests;
        int soTimeout = socket.getSoTimeout();
        int oldSoTimeout = soTimeout;
        int threadRatio = (endpoint.getCurrentThreadsBusy() * 100) / endpoint.getMaxThreads();
        if (threadRatio > 75) {
            keepAliveLeft = 1;
        }
        if (soTimeout != oldSoTimeout) {
            try {
                socket.setSoTimeout(soTimeout);
            } catch (Throwable t) {
                log.debug(sm.getString("http11processor.socket.timeout"), t);
                error = true;
            }
        }
        boolean keptAlive = false;
        while (started && !error && keepAlive) {
            try {
                if (!disableUploadTimeout && keptAlive) {
                    if (keepAliveTimeout > 0) {
                        socket.setSoTimeout(keepAliveTimeout);
                    } else if (soTimeout > 0) {
                        socket.setSoTimeout(soTimeout);
                    }
                }
                inputBuffer.parseRequestLine();
                request.setStartTime(System.currentTimeMillis());
                keptAlive = true;
                if (!disableUploadTimeout) {
                    socket.setSoTimeout(timeout);
                }
                inputBuffer.parseHeaders();
            } catch (IOException e) {
                error = true;
                break;
            } catch (Throwable t) {
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("http11processor.header.parse"), t);
                }
                response.setStatus(400);
                error = true;
            }
            rp.setStage(org.apache.coyote.Constants.STAGE_PREPARE);
            try {
                prepareRequest();
            } catch (Throwable t) {
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("http11processor.request.prepare"), t);
                }
                response.setStatus(400);
                error = true;
            }
            if (maxKeepAliveRequests > 0 && --keepAliveLeft == 0) keepAlive = false;
            if (!error) {
                try {
                    rp.setStage(org.apache.coyote.Constants.STAGE_SERVICE);
                    adapter.service(request, response);
                    if (keepAlive && !error) {
                        error = response.getErrorException() != null || statusDropsConnection(response.getStatus());
                    }
                } catch (InterruptedIOException e) {
                    error = true;
                } catch (Throwable t) {
                    log.error(sm.getString("http11processor.request.process"), t);
                    response.setStatus(500);
                    error = true;
                }
            }
            try {
                rp.setStage(org.apache.coyote.Constants.STAGE_ENDINPUT);
                inputBuffer.endRequest();
            } catch (IOException e) {
                error = true;
            } catch (Throwable t) {
                log.error(sm.getString("http11processor.request.finish"), t);
                response.setStatus(500);
                error = true;
            }
            try {
                rp.setStage(org.apache.coyote.Constants.STAGE_ENDOUTPUT);
                outputBuffer.endRequest();
            } catch (IOException e) {
                error = true;
            } catch (Throwable t) {
                log.error(sm.getString("http11processor.response.finish"), t);
                error = true;
            }
            if (error) {
                response.setStatus(500);
            }
            request.updateCounters();
            rp.setStage(org.apache.coyote.Constants.STAGE_KEEPALIVE);
            inputBuffer.nextRequest();
            outputBuffer.nextRequest();
        }
        rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);
        inputBuffer.recycle();
        outputBuffer.recycle();
        sslSupport = null;
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
            prepareResponse();
            try {
                outputBuffer.commit();
            } catch (IOException e) {
                error = true;
            }
        } else if (actionCode == ActionCode.ACTION_ACK) {
            if ((response.isCommitted()) || !expectation) return;
            inputBuffer.setSwallowInput(true);
            try {
                outputBuffer.sendAck();
            } catch (IOException e) {
                error = true;
            }
        } else if (actionCode == ActionCode.ACTION_CLIENT_FLUSH) {
            try {
                outputBuffer.flush();
            } catch (IOException e) {
                error = true;
                response.setErrorException(e);
            }
        } else if (actionCode == ActionCode.ACTION_CLOSE) {
            try {
                outputBuffer.endRequest();
            } catch (IOException e) {
                error = true;
            }
        } else if (actionCode == ActionCode.ACTION_RESET) {
            outputBuffer.reset();
        } else if (actionCode == ActionCode.ACTION_CUSTOM) {
        } else if (actionCode == ActionCode.ACTION_START) {
            started = true;
        } else if (actionCode == ActionCode.ACTION_STOP) {
            started = false;
        } else if (actionCode == ActionCode.ACTION_REQ_SSL_ATTRIBUTE) {
            try {
                if (sslSupport != null) {
                    Object sslO = sslSupport.getCipherSuite();
                    if (sslO != null) request.setAttribute(SSLSupport.CIPHER_SUITE_KEY, sslO);
                    sslO = sslSupport.getPeerCertificateChain(false);
                    if (sslO != null) request.setAttribute(SSLSupport.CERTIFICATE_KEY, sslO);
                    sslO = sslSupport.getKeySize();
                    if (sslO != null) request.setAttribute(SSLSupport.KEY_SIZE_KEY, sslO);
                    sslO = sslSupport.getSessionId();
                    if (sslO != null) request.setAttribute(SSLSupport.SESSION_ID_KEY, sslO);
                }
            } catch (Exception e) {
                log.warn(sm.getString("http11processor.socket.ssl"), e);
            }
        } else if (actionCode == ActionCode.ACTION_REQ_HOST_ADDR_ATTRIBUTE) {
            if ((remoteAddr == null) && (socket != null)) {
                InetAddress inetAddr = socket.getInetAddress();
                if (inetAddr != null) {
                    remoteAddr = inetAddr.getHostAddress();
                }
            }
            request.remoteAddr().setString(remoteAddr);
        } else if (actionCode == ActionCode.ACTION_REQ_LOCAL_NAME_ATTRIBUTE) {
            if ((localName == null) && (socket != null)) {
                InetAddress inetAddr = socket.getLocalAddress();
                if (inetAddr != null) {
                    localName = inetAddr.getHostName();
                }
            }
            request.localName().setString(localName);
        } else if (actionCode == ActionCode.ACTION_REQ_HOST_ATTRIBUTE) {
            if ((remoteHost == null) && (socket != null)) {
                InetAddress inetAddr = socket.getInetAddress();
                if (inetAddr != null) {
                    remoteHost = inetAddr.getHostName();
                }
                if (remoteHost == null) {
                    if (remoteAddr != null) {
                        remoteHost = remoteAddr;
                    } else {
                        request.remoteHost().recycle();
                    }
                }
            }
            request.remoteHost().setString(remoteHost);
        } else if (actionCode == ActionCode.ACTION_REQ_LOCAL_ADDR_ATTRIBUTE) {
            if (localAddr == null) localAddr = socket.getLocalAddress().getHostAddress();
            request.localAddr().setString(localAddr);
        } else if (actionCode == ActionCode.ACTION_REQ_REMOTEPORT_ATTRIBUTE) {
            if ((remotePort == -1) && (socket != null)) {
                remotePort = socket.getPort();
            }
            request.setRemotePort(remotePort);
        } else if (actionCode == ActionCode.ACTION_REQ_LOCALPORT_ATTRIBUTE) {
            if ((localPort == -1) && (socket != null)) {
                localPort = socket.getLocalPort();
            }
            request.setLocalPort(localPort);
        } else if (actionCode == ActionCode.ACTION_REQ_SSL_CERTIFICATE) {
            if (sslSupport != null) {
                InputFilter[] inputFilters = inputBuffer.getFilters();
                ((BufferedInputFilter) inputFilters[Constants.BUFFERED_FILTER]).setLimit(maxSavePostSize);
                inputBuffer.addActiveFilter(inputFilters[Constants.BUFFERED_FILTER]);
                try {
                    Object sslO = sslSupport.getPeerCertificateChain(true);
                    if (sslO != null) {
                        request.setAttribute(SSLSupport.CERTIFICATE_KEY, sslO);
                    }
                } catch (Exception e) {
                    log.warn(sm.getString("http11processor.socket.ssl"), e);
                }
            }
        } else if (actionCode == ActionCode.ACTION_REQ_SET_BODY_REPLAY) {
            ByteChunk body = (ByteChunk) param;
            InputFilter savedBody = new SavedRequestInputFilter(body);
            savedBody.setRequest(request);
            InternalInputBuffer internalBuffer = (InternalInputBuffer) request.getInputBuffer();
            internalBuffer.addActiveFilter(savedBody);
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
        http11 = true;
        http09 = false;
        contentDelimitation = false;
        expectation = false;
        if (sslSupport != null) {
            request.scheme().setString("https");
        }
        MessageBytes protocolMB = request.protocol();
        if (protocolMB.equals(Constants.HTTP_11)) {
            http11 = true;
            protocolMB.setString(Constants.HTTP_11);
        } else if (protocolMB.equals(Constants.HTTP_10)) {
            http11 = false;
            keepAlive = false;
            protocolMB.setString(Constants.HTTP_10);
        } else if (protocolMB.equals("")) {
            http09 = true;
            http11 = false;
            keepAlive = false;
        } else {
            http11 = false;
            error = true;
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("http11processor.request.prepare") + " Unsupported HTTP version \"" + protocolMB + "\"");
            }
            response.setStatus(505);
        }
        MessageBytes methodMB = request.method();
        if (methodMB.equals(Constants.GET)) {
            methodMB.setString(Constants.GET);
        } else if (methodMB.equals(Constants.POST)) {
            methodMB.setString(Constants.POST);
        }
        MimeHeaders headers = request.getMimeHeaders();
        MessageBytes connectionValueMB = headers.getValue("connection");
        if (connectionValueMB != null) {
            ByteChunk connectionValueBC = connectionValueMB.getByteChunk();
            if (findBytes(connectionValueBC, Constants.CLOSE_BYTES) != -1) {
                keepAlive = false;
            } else if (findBytes(connectionValueBC, Constants.KEEPALIVE_BYTES) != -1) {
                keepAlive = true;
            }
        }
        MessageBytes expectMB = null;
        if (http11) expectMB = headers.getValue("expect");
        if ((expectMB != null) && (expectMB.indexOfIgnoreCase("100-continue", 0) != -1)) {
            inputBuffer.setSwallowInput(false);
            expectation = true;
        }
        if ((restrictedUserAgents != null) && ((http11) || (keepAlive))) {
            MessageBytes userAgentValueMB = headers.getValue("user-agent");
            if (userAgentValueMB != null) {
                String userAgentValue = userAgentValueMB.toString();
                for (int i = 0; i < restrictedUserAgents.length; i++) {
                    if (restrictedUserAgents[i].matcher(userAgentValue).matches()) {
                        http11 = false;
                        keepAlive = false;
                        break;
                    }
                }
            }
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
        InputFilter[] inputFilters = inputBuffer.getFilters();
        MessageBytes transferEncodingValueMB = null;
        if (http11) transferEncodingValueMB = headers.getValue("transfer-encoding");
        if (transferEncodingValueMB != null) {
            String transferEncodingValue = transferEncodingValueMB.toString();
            int startPos = 0;
            int commaPos = transferEncodingValue.indexOf(',');
            String encodingName = null;
            while (commaPos != -1) {
                encodingName = transferEncodingValue.substring(startPos, commaPos).toLowerCase().trim();
                if (!addInputFilter(inputFilters, encodingName)) {
                    error = true;
                    response.setStatus(501);
                }
                startPos = commaPos + 1;
                commaPos = transferEncodingValue.indexOf(',', startPos);
            }
            encodingName = transferEncodingValue.substring(startPos).toLowerCase().trim();
            if (!addInputFilter(inputFilters, encodingName)) {
                error = true;
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("http11processor.request.prepare") + " Unsupported transfer encoding \"" + encodingName + "\"");
                }
                response.setStatus(501);
            }
        }
        long contentLength = request.getContentLengthLong();
        if (contentLength >= 0 && !contentDelimitation) {
            inputBuffer.addActiveFilter(inputFilters[Constants.IDENTITY_FILTER]);
            contentDelimitation = true;
        }
        MessageBytes valueMB = headers.getValue("host");
        if (http11 && (valueMB == null)) {
            error = true;
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("http11processor.request.prepare") + " host header missing");
            }
            response.setStatus(400);
        }
        parseHost(valueMB);
        if (!contentDelimitation) {
            inputBuffer.addActiveFilter(inputFilters[Constants.VOID_FILTER]);
            contentDelimitation = true;
        }
    }

    /**
     * Parse host.
     */
    public void parseHost(MessageBytes valueMB) {
        if (valueMB == null || valueMB.isNull()) {
            request.setServerPort(socket.getLocalPort());
            InetAddress localAddress = socket.getLocalAddress();
            request.serverName().setString(localAddress.getHostName());
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
            if (sslSupport == null) {
                request.setServerPort(80);
            } else {
                request.setServerPort(443);
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
     * Check for compression
     */
    private boolean isCompressable() {
        MessageBytes acceptEncodingMB = request.getMimeHeaders().getValue("accept-encoding");
        if ((acceptEncodingMB == null) || (acceptEncodingMB.indexOf("gzip") == -1)) return false;
        MessageBytes contentEncodingMB = response.getMimeHeaders().getValue("Content-Encoding");
        if ((contentEncodingMB != null) && (contentEncodingMB.indexOf("gzip") != -1)) return false;
        if (compressionLevel == 2) return true;
        if (noCompressionUserAgents != null) {
            MessageBytes userAgentValueMB = request.getMimeHeaders().getValue("user-agent");
            if (userAgentValueMB != null) {
                String userAgentValue = userAgentValueMB.toString();
                for (int i = 0; i < noCompressionUserAgents.length; i++) if (noCompressionUserAgents[i].matcher(userAgentValue).matches()) return false;
            }
        }
        long contentLength = response.getContentLengthLong();
        if ((contentLength == -1) || (contentLength > compressionMinSize)) {
            if (compressableMimeTypes != null) {
                return (startsWithStringArray(compressableMimeTypes, response.getContentType()));
            }
        }
        return false;
    }

    /**
     * When committing the response, we have to validate the set of headers, as
     * well as setup the response filters.
     */
    protected void prepareResponse() {
        boolean entityBody = true;
        contentDelimitation = false;
        OutputFilter[] outputFilters = outputBuffer.getFilters();
        if (http09 == true) {
            outputBuffer.addActiveFilter(outputFilters[Constants.IDENTITY_FILTER]);
            return;
        }
        int statusCode = response.getStatus();
        if ((statusCode == 204) || (statusCode == 205) || (statusCode == 304)) {
            outputBuffer.addActiveFilter(outputFilters[Constants.VOID_FILTER]);
            entityBody = false;
            contentDelimitation = true;
        }
        MessageBytes methodMB = request.method();
        if (methodMB.equals("HEAD")) {
            outputBuffer.addActiveFilter(outputFilters[Constants.VOID_FILTER]);
            contentDelimitation = true;
        }
        boolean useCompression = false;
        if (entityBody && (compressionLevel > 0)) {
            useCompression = isCompressable();
            if (useCompression) {
                response.setContentLength(-1);
            }
        }
        MimeHeaders headers = response.getMimeHeaders();
        if (!entityBody) {
            response.setContentLength(-1);
        } else {
            String contentType = response.getContentType();
            if (contentType != null) {
                headers.setValue("Content-Type").setString(contentType);
            }
            String contentLanguage = response.getContentLanguage();
            if (contentLanguage != null) {
                headers.setValue("Content-Language").setString(contentLanguage);
            }
        }
        long contentLength = response.getContentLengthLong();
        if (contentLength != -1) {
            headers.setValue("Content-Length").setLong(contentLength);
            outputBuffer.addActiveFilter(outputFilters[Constants.IDENTITY_FILTER]);
            contentDelimitation = true;
        } else {
            if (entityBody && http11 && keepAlive) {
                outputBuffer.addActiveFilter(outputFilters[Constants.CHUNKED_FILTER]);
                contentDelimitation = true;
                headers.addValue(Constants.TRANSFERENCODING).setString(Constants.CHUNKED);
            } else {
                outputBuffer.addActiveFilter(outputFilters[Constants.IDENTITY_FILTER]);
            }
        }
        if (useCompression) {
            outputBuffer.addActiveFilter(outputFilters[Constants.GZIP_FILTER]);
            headers.setValue("Content-Encoding").setString("gzip");
            headers.setValue("Vary").setString("Accept-Encoding");
        }
        String date = null;
        if (org.apache.coyote.Constants.IS_SECURITY_ENABLED) {
            date = (String) AccessController.doPrivileged(new PrivilegedAction() {

                public Object run() {
                    return FastHttpDateFormat.getCurrentDate();
                }
            });
        } else {
            date = FastHttpDateFormat.getCurrentDate();
        }
        headers.setValue("Date").setString(date);
        if ((entityBody) && (!contentDelimitation)) {
            keepAlive = false;
        }
        keepAlive = keepAlive && !statusDropsConnection(statusCode);
        if (!keepAlive) {
            headers.addValue(Constants.CONNECTION).setString(Constants.CLOSE);
        } else if (!http11 && !error) {
            headers.addValue(Constants.CONNECTION).setString(Constants.KEEPALIVE);
        }
        outputBuffer.sendStatus();
        if (server != null) {
            headers.setValue("Server").setString(server);
        } else {
            outputBuffer.write(Constants.SERVER_BYTES);
        }
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            outputBuffer.sendHeader(headers.getName(i), headers.getValue(i));
        }
        outputBuffer.endHeaders();
    }

    /**
     * Initialize standard input and output filters.
     */
    protected void initializeFilters() {
        inputBuffer.addFilter(new IdentityInputFilter());
        outputBuffer.addFilter(new IdentityOutputFilter());
        inputBuffer.addFilter(new ChunkedInputFilter());
        outputBuffer.addFilter(new ChunkedOutputFilter());
        inputBuffer.addFilter(new VoidInputFilter());
        outputBuffer.addFilter(new VoidOutputFilter());
        inputBuffer.addFilter(new BufferedInputFilter());
        outputBuffer.addFilter(new GzipOutputFilter());
    }

    /**
     * Add an input filter to the current request.
     *
     * @return false if the encoding was not found (which would mean it is
     * unsupported)
     */
    protected boolean addInputFilter(InputFilter[] inputFilters, String encodingName) {
        if (encodingName.equals("identity")) {
        } else if (encodingName.equals("chunked")) {
            inputBuffer.addActiveFilter(inputFilters[Constants.CHUNKED_FILTER]);
            contentDelimitation = true;
        } else {
            for (int i = 2; i < inputFilters.length; i++) {
                if (inputFilters[i].getEncodingName().toString().equals(encodingName)) {
                    inputBuffer.addActiveFilter(inputFilters[i]);
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Specialized utility method: find a sequence of lower case bytes inside
     * a ByteChunk.
     */
    protected int findBytes(ByteChunk bc, byte[] b) {
        byte first = b[0];
        byte[] buff = bc.getBuffer();
        int start = bc.getStart();
        int end = bc.getEnd();
        int srcEnd = b.length;
        for (int i = start; i <= (end - srcEnd); i++) {
            if (Ascii.toLower(buff[i]) != first) continue;
            int myPos = i + 1;
            for (int srcPos = 1; srcPos < srcEnd; ) {
                if (Ascii.toLower(buff[myPos++]) != b[srcPos++]) break;
                if (srcPos == srcEnd) return i - start;
            }
        }
        return -1;
    }

    /**
     * Determine if we must drop the connection because of the HTTP status
     * code.  Use the same list of codes as Apache/httpd.
     */
    protected boolean statusDropsConnection(int status) {
        return status == 400 || status == 408 || status == 411 || status == 413 || status == 414 || status == 500 || status == 503 || status == 501;
    }
}
