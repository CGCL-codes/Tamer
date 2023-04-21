package de.dgrid.bisgrid.services.proxy.redirect;

import de.dgrid.bisgrid.services.proxy.redirect.util.ByteArrayBuffer;
import de.dgrid.bisgrid.services.proxy.redirect.ui.IOSystemForServer;
import de.dgrid.bisgrid.services.proxy.redirect.util.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.apache.log4j.Logger;

/**
 *
 * @author dmeister
 */
public class Response {

    private static Logger log = Logger.getLogger(Response.class);

    private final InputStream fromServer;

    private final OutputStream toClient;

    private final String targetHost;

    private IOSystemForServer output = null;

    private final String requestId;

    private final URL requestTarget;

    private HttpResponseHeader header;

    private final String requestMethod;

    /**
     * Create a new response handler to handle
     * the data that the server wants to send
     * to the client.
     *
     */
    protected Response(InputStream fromServer, OutputStream toClient, String targetHost, URL requestTarget, String requestMethod, String requestId) {
        this.fromServer = fromServer;
        this.toClient = toClient;
        this.targetHost = targetHost;
        this.output = new IOSystemForServer(HttpRedirect.Redirector);
        this.requestId = requestId;
        this.requestTarget = requestTarget;
        this.requestMethod = requestMethod;
        this.header = null;
    }

    /**
     * ...to be able to use this as a thread...
     */
    public void run() {
        doRun();
    }

    /**
     * @return true, if the caller should keep the
     *         connections to the client alive.
     *         false, if the connection shall be closed.
     *
     *          Currently, the method will allways return "false".
     */
    public boolean doRun() {
        boolean result;
        this.output.startNewRequest(this.targetHost);
        HttpRedirect.setThreadId(this.requestId);
        do {
            header = new HttpResponseHeader(this.requestTarget, this.requestMethod);
            result = read();
        } while (result == true);
        return result;
    }

    /**
     * Read data from the server and pass it
     * to the client.
     *
     * @return true, if the caller should keep the
     *         connections to the client alive.
     *         false, if the connection shall be closed.
     */
    private boolean read() {
        log.debug("HttpResponse::doRun>>>");
        ByteArrayBuffer inputBuffer = new ByteArrayBuffer(HttpRedirect.BUFFER_SIZE);
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
        int readResult = 0;
        boolean result = true;
        try {
            readResult = inputBuffer.readHttpHeader(fromServer);
            if (readResult < 0) {
                log.error("Error while reading HTTP header from server. ReadResult=" + String.valueOf(readResult) + ". Buffer content=" + inputBuffer.toString());
                return false;
            }
            header.readHeader(inputBuffer);
            if (header.getStatusCode() != 100) {
                result = false;
            } else {
                log.info("HTTP 100 received.");
            }
            this.output.displayHttpHeader(inputBuffer.getProcessed());
            String headerText = header.createHttpData(false);
            log.debug("Response Header Text:\n" + headerText);
            byte[] HeaderTextAsArray = headerText.getBytes();
            outputBuffer.write(HeaderTextAsArray, 0, HeaderTextAsArray.length);
            if (header.isBodyAllowed()) {
                int bodyLength = processBody(inputBuffer, outputBuffer);
                if (bodyLength == -1) {
                    result = false;
                }
            } else {
                outputBuffer.writeTo(toClient);
                toClient.flush();
            }
            if (header.isConnectionClose()) {
                result = false;
            }
        } catch (IOException e) {
            log.error("IOException while reading from client. " + e);
            result = false;
        } catch (HttpException e) {
            HttpRedirect.Redirector.Out.displayLine("\tProtocol error by client:" + e.getMessage());
            result = false;
        } finally {
        }
        log.debug("HttpResponse::doRun<<<");
        this.output.flushDisplay();
        return result;
    }

    /**
     * This method processes the body of a
     * HTTP response.
     *
     * There are two ways the method determins the length
     * of the message body: <ul>
     * <li> the method checks for the value of "content-length" attribute
     * <li> the method reads data until the server closes the connection.
     * </ul>
     *
     * @return -1 == connection closed
     *  >=0 length of the body
     */
    private int processBody(ByteArrayBuffer inputBuffer, ByteArrayOutputStream outputBuffer) throws IOException, HttpException {
        int messageLength = this.header.getMessageLength();
        int result;
        log.debug("processBody::MessageLength=" + String.valueOf(messageLength));
        if (messageLength == -1) {
            String transferEncoding = header.getAttributeValue(HttpConstants.TransferEncoding);
            if ((transferEncoding != null) && (transferEncoding.equalsIgnoreCase(HttpConstants.Chunked))) {
                result = processChunkedBody(inputBuffer, outputBuffer);
            } else {
                processBodyUntilClosed(inputBuffer, outputBuffer);
                result = -1;
            }
        } else {
            processBodyWithLength(inputBuffer, outputBuffer, messageLength);
            result = messageLength;
        }
        return result;
    }

    /**
     * Process a HTTP body that comes in a number
     * of chunks. Each chunk comes with a length prefix.
     *
     * The method works like this: <ul>
     * <li> it reads the first chunk using the InputBuffer.readChunk method.
     *
     * <li> the "chunk" is then appended to the "OutputBuffer" (the output
     *      buffer then contains the HTTP header and the first chunk)
     *
     * <li> The content of the output buffer is send to the client (browser)
     *
     * <li> The method uses InputBuffer.readChunk to get the next chunk
     * </ul>
     * @return Length of the body
     */
    private int processChunkedBody(ByteArrayBuffer inputBuffer, ByteArrayOutputStream outputBuffer) throws IOException, HttpException {
        log.debug("processChunkedBody");
        int readResult;
        int bodyLength = 0;
        output.flushDisplay();
        while ((readResult = inputBuffer.readChunk(this.fromServer)) >= 0) {
            inputBuffer.writeTo(outputBuffer);
            bodyLength += readResult;
            byte[] outputData = outputBuffer.toByteArray();
            outputBuffer.reset();
            if (log.isDebugEnabled()) {
                log.debug("Sending data to client:");
                log.debug(Util.toHex(outputData));
                log.debug("<<<");
            }
            output.displayHttpBody(outputData, 0, outputData.length - 1);
            toClient.write(outputData, 0, outputData.length);
            toClient.flush();
            if (readResult == 0) {
                break;
            }
        }
        return bodyLength;
    }

    /**
     *Read data from the HTTP server and send it to the client
     *(browser) until the server closes the connection.
     */
    private void processBodyUntilClosed(ByteArrayBuffer InputBuffer, ByteArrayOutputStream OutputBuffer) throws IOException {
        log.debug("processBodyUntilClosed");
        do {
            ByteArrayBuffer RemainingPart = InputBuffer.getRemaining();
            this.output.displayHttpBody(RemainingPart);
            RemainingPart.writeTo(OutputBuffer);
            byte[] OutputData = OutputBuffer.toByteArray();
            OutputBuffer.reset();
            this.output.flushDisplay();
            if (log.isDebugEnabled()) {
                log.debug("Sending data to client:");
                log.debug(Util.toHex(OutputData));
                log.debug("<<<");
            }
            try {
                toClient.write(OutputData, 0, OutputData.length);
                toClient.flush();
            } catch (IOException E) {
                log.warn("Failed to write to client: " + E.toString());
                break;
            }
        } while (InputBuffer.read(fromServer) >= 0);
    }

    /**
     *Process the body of a HTTP message. The length of the body is
     *specified in the HTTP header using the "content-length" attribut.
     */
    private void processBodyWithLength(ByteArrayBuffer InputBuffer, ByteArrayOutputStream OutputBuffer, int MessageLength) throws IOException {
        int BytesInBuffer = InputBuffer.readExact(fromServer, MessageLength);
        if (BytesInBuffer > 0) {
            this.output.displayHttpBody(InputBuffer);
            InputBuffer.writeTo(OutputBuffer);
        }
        byte[] OutputData = OutputBuffer.toByteArray();
        OutputBuffer.reset();
        this.output.flushDisplay();
        if (log.isDebugEnabled()) {
            log.debug("Sending data to client:");
            log.debug(Util.toHex(OutputData));
            log.debug("<<<");
        }
        toClient.write(OutputData, 0, OutputData.length);
        toClient.flush();
    }

    /**
     * Get access to the header. Used for testing.
     * @return this.Header
     */
    public HttpResponseHeader getHeader() {
        return this.header;
    }
}
