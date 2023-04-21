package org.xaware.testing.util;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import com.meterware.pseudoserver.PseudoServlet;
import com.meterware.pseudoserver.WebResource;

/**
 * This is a simple servlet that will echo back to the requestor the message sent to it
 * 
 * @author hcurtis
 */
public class GenericEchoServlet extends PseudoServlet {

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String CONTENT_ID = "Content-ID";

    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    public static final String CONTENT_XFR_ENCODE = "Content-Transfer-Encoding";

    public static final String CONTENT_LOCATION = "Content-Location";

    public static final String SOAPACTION = "SOAPAction";

    public static final String MULTIPART = "multipart/";

    public static final String HOST = "Host";

    protected static final String headers[] = { CONTENT_TYPE, CONTENT_LENGTH, "boundary", "User-Agent", HOST, SOAPACTION, CONTENT_DISPOSITION, CONTENT_XFR_ENCODE };

    protected Map<String, String> headerMap = null;

    protected String msgBody = null;

    protected Log log;

    private final List<MessagePart> msgBlocks = new ArrayList<MessagePart>();

    private String boundary = null;

    /**
     * Constructor
     */
    public GenericEchoServlet(final Log logger) {
        super();
        log = logger;
    }

    @Override
    public WebResource getPostResponse() throws IOException {
        msgBody = new String(getBody());
        log.debug("Request Message:" + msgBody);
        fetchHeaders();
        return createResponse();
    }

    /**
     * Validate the boundary string is provided, segment the message blocks into separate entries in the msgBlocks
     * ArrayList, then invoke the createResponse method to generate the response to the originating message.
     * 
     * @return Returns the response to the request as a WebResource
     */
    private void prepareMultiPartMessageBlocks() {
        final String contentType = headerMap.get(CONTENT_TYPE);
        int boundaryPos = contentType.indexOf("boundary=");
        if (boundaryPos > 0) {
            boundaryPos += 9;
            boundary = contentType.substring(boundaryPos);
            final int boundaryEndPos = boundary.indexOf(";");
            if (boundaryEndPos > 0) {
                boundary = boundary.substring(0, boundaryEndPos).trim();
            }
            if (boundary.startsWith("\"") && boundary.endsWith("\"")) {
                boundary = boundary.substring(1, boundary.length() - 1);
            }
            prepareMessageBlocks("--" + boundary);
        } else {
            final String errMsg = "Didn't find 'boudary' definition in Content-Type:" + contentType;
            log.error(errMsg);
            junit.framework.Assert.fail(errMsg);
        }
    }

    /**
     * Segment the message blocks using the boundary string provided in the originating header. Each section of the
     * originating request is stored in a separate array entry.
     * 
     * @param sentinel -
     *            The character string that separates each section of the multipart SOAP message
     */
    private void prepareMessageBlocks(final String sentinel) {
        log.debug("Sentinel string, \"" + sentinel + "\" separating each message part");
        String endSentinel = "";
        if (!msgBody.startsWith(sentinel)) {
            final String errorMsg = "Multipart message doesn't start with boundary string, \"" + sentinel + "\"";
            log.error(errorMsg);
            junit.framework.Assert.fail(errorMsg);
        }
        msgBody = msgBody.substring(sentinel.length());
        if (msgBody.startsWith("\r\n")) {
            msgBody = msgBody.substring(2);
        }
        do {
            final int pos = msgBody.indexOf(sentinel);
            if (pos > 0) {
                msgBlocks.add(new MessagePart(msgBody.substring(0, pos)));
            } else {
                final String errMsg = "No closing boundary for message block " + (msgBlocks.size() + 1);
                log.error(errMsg);
                log.info("remaining message string:" + msgBody);
                junit.framework.Assert.fail(errMsg);
            }
            msgBody = msgBody.substring(pos + sentinel.length());
            if (msgBody.startsWith("\r\n")) {
                msgBody = msgBody.substring(2);
            }
            if (msgBody.length() >= 2) {
                endSentinel = msgBody.substring(0, 2);
            }
        } while (!endSentinel.equals("--"));
        log.debug("There are " + msgBlocks.size() + " message blocks in this SOAP message");
    }

    /**
     * Put the simple message into the msgBlocks, then call the createResponse to prepare the WebResource response
     * 
     * @return Returns the response to the request as a WebResource
     */
    protected WebResource prepareStdEchoMessage() {
        final WebResource resource = new WebResource(msgBody, headerMap.get(CONTENT_TYPE));
        resource.addHeader("connection: close");
        setHeader(CONTENT_LENGTH, String.valueOf(msgBody.length()));
        pushHeaders(resource);
        return resource;
    }

    /**
     * Put the simple message into the msgBlocks, then call the createResponse to prepare the WebResource response
     * 
     * @return Returns the response to the request as a WebResource
     */
    protected WebResource prepareMultiPartEchoMessage() {
        final StringBuffer msg = new StringBuffer(msgBody.length());
        final Iterator iter = msgBlocks.iterator();
        while (iter.hasNext()) {
            final MessagePart mp = (MessagePart) iter.next();
            msg.append("\r\n--").append(boundary).append("\r\n").append(mp.assembleMessageBlock());
        }
        msg.append("\r\n--").append(boundary).append("--");
        final WebResource resource = new WebResource(msg.toString(), headerMap.get(CONTENT_TYPE));
        resource.addHeader("connection: close");
        setHeader(CONTENT_LENGTH, String.valueOf(msg.length()));
        pushHeaders(resource);
        return resource;
    }

    /**
     * This method has the responsibility to compose the response to the received request. The default implementation
     * will simply echo the originating message.
     * 
     * @return Returns the response to the request as a WebResource
     */
    protected WebResource createResponse() {
        WebResource resource = null;
        final String msgType = headerMap.get(CONTENT_TYPE);
        junit.framework.Assert.assertNotNull("Content-Type not detected", msgType);
        if (msgType.startsWith(MULTIPART)) {
            prepareMultiPartMessageBlocks();
            resource = prepareMultiPartEchoMessage();
        } else {
            resource = prepareStdEchoMessage();
        }
        return resource;
    }

    /**
     * Fetch all the headers from the orginating message and store them in a HashMap. There is a public access to the
     * HashMap of the detected headers
     */
    protected void fetchHeaders() {
        headerMap = new HashMap<String, String>();
        for (int i = 0; i < headers.length; i++) {
            final String headerValue = getHeader(headers[i]);
            if ((headerValue != null) && (headerValue.length() > 0)) {
                headerMap.put(headers[i], headerValue);
            }
        }
    }

    /**
     * Push the headers stored in the headerMap into the supplied WebResource
     * 
     * @param resource -
     *            The WebResource the headers will be added to
     */
    public void pushHeaders(final WebResource resource) {
        final Iterator hIter = headerMap.keySet().iterator();
        while (hIter.hasNext()) {
            final String key = (String) hIter.next();
            final String value = headerMap.get(key);
            resource.addHeader(key + ": " + value);
        }
    }

    public void setHeader(final String key, final String value) {
        headerMap.put(key, value);
    }

    /**
     * Accessor to the headers of the originating request
     * 
     * @return
     */
    public Map<String, String> getHeaderMap() {
        return Collections.unmodifiableMap(headerMap);
    }

    /**
     * Accessor to the received message blocks
     */
    public List<MessagePart> getMsgBlocks() {
        return msgBlocks;
    }

    /**
     * Provides any dump facility of the generated response message
     * 
     * @param resource -
     *            The WebResource that contains the response
     */
    public void dumpWebResource(final WebResource resource) {
        log.debug("Servlet Response: " + resource.toString());
    }

    /**
     * This object holds each message of a multi-part message
     */
    public class MessagePart {

        private final Map<String, String> headers = new HashMap<String, String>();

        private String msg;

        public MessagePart(final String msgPart) {
            msg = parseOutHeaders(msgPart);
        }

        /**
         * Pull out the headers from the message block
         * 
         * @param m
         * @return
         */
        private String parseOutHeaders(final String m) {
            final StringBuffer msgBuf = new StringBuffer(m.length());
            final CharArrayReader reader = new CharArrayReader(m.toCharArray());
            final BufferedReader bufReader = new BufferedReader(reader);
            boolean headerData = false;
            do {
                String headerLine;
                try {
                    headerLine = bufReader.readLine();
                    headerData = headerLine.length() > 0;
                    if (headerData) {
                        final int p = headerLine.indexOf(":");
                        if (p > 0) {
                            final String name = headerLine.substring(0, p);
                            final String value = headerLine.substring(p + 1).trim();
                            headers.put(name, value);
                        }
                    }
                } catch (final IOException e) {
                    log.error("Exception reading message headers", e);
                    headerData = false;
                }
            } while (headerData);
            String messageLine = null;
            do {
                try {
                    messageLine = bufReader.readLine();
                } catch (final IOException e) {
                    log.error("Exception reading message data", e);
                    messageLine = null;
                }
                if (messageLine != null) {
                    msgBuf.append(messageLine).append("\n");
                }
            } while (messageLine != null);
            return msgBuf.toString();
        }

        /**
         * Assemble the message block into a message section to be grafted into the multi-part message
         * 
         * @return
         */
        public String assembleMessageBlock() {
            final StringBuffer msgBlock = new StringBuffer(msg.length());
            final Iterator iter = headers.keySet().iterator();
            while (iter.hasNext()) {
                final String name = (String) iter.next();
                final String value = headers.get(name);
                if (value != null && value.length() > 0) {
                    msgBlock.append(name).append(": ").append(value).append("\n");
                }
            }
            msgBlock.append("\n").append(msg);
            return msgBlock.toString();
        }

        /**
         * @return Returns the headers.
         */
        public Map<String, String> getHeaders() {
            return Collections.unmodifiableMap(headers);
        }

        /**
         * Get the header value specified
         * 
         * @param name -
         *            The name of the desired header
         * @return Returns the value of the header or null if the header doesn't exist.
         */
        public String getHeader(final String name) {
            return headers.get(name);
        }

        /**
         * Mutator for the header specified. If the header value doesn't exist it will be added to the list. If it does
         * exist, the new value will replace the old one.
         * 
         * @param name -
         *            The name of the header
         * @param value -
         *            The value of the header
         */
        public void addHeader(final String name, final String value) {
            headers.put(name, value);
        }

        /**
         * @return Returns the msg.
         */
        public String getMsg() {
            return msg;
        }

        /**
         * @param msg
         *            The msg to set.
         */
        public void setMsg(final String msg) {
            this.msg = msg;
        }
    }
}
