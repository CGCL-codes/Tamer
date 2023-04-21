package ch.unifr.nio.framework.transform;

import ch.unifr.nio.framework.FrameworkTools;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * the base class for a HTTP proxy forwarder
 *
 * @author Ronny Standtke <Ronny.Standtke@gmx.net>
 */
public abstract class AbstractHttpProxyForwarder extends AbstractForwarder<ByteBuffer, ByteBuffer> {

    private static final Logger LOGGER = Logger.getLogger(AbstractHttpProxyForwarder.class.getName());

    /**
     * the different HTTP parsing states
     */
    protected enum HttpState {

        /**
         * the header is parsed
         */
        HEADER, /**
         * body data is forwarded
         */
        BODY, /**
         * a tunnel is established
         */
        TUNNEL
    }

    /**
     * the current HTTP parsing state
     */
    protected HttpState state = HttpState.HEADER;

    /**
     * the supported HTTP methods
     */
    protected enum HttpMethod {

        /**
         * unknown or not implemented method
         */
        NOT_IMPLEMENTED, /**
         * HTTP HEAD
         */
        HEAD, /**
         * HTTP GET
         */
        GET, /**
         * HTTP POST
         */
        POST, /**
         * HTTP CONNECT
         */
        CONNECT
    }

    /**
     * the current HTTP method
     */
    protected HttpMethod httpMethod;

    /**
     * the HTTP version of the current message
     */
    protected String httpVersion;

    /**
     * if the connection of the current message is persistent (*MUST* default to
     * true!)
     */
    protected boolean persistent = true;

    private final String viaPseudonym;

    /**
     * the known variants of body length declaration
     */
    protected enum LengthIndicator {

        /**
         * the body length is unknown
         */
        UNKNOWN, /**
         * a content-length header field was given
         */
        CONTENT_LENGTH, /**
         * the body is transferred in chunks
         */
        CHUNKED
    }

    /**
     * the length indicator of the current message
     */
    protected LengthIndicator lengthIndicator;

    private enum ChunkedState {

        /**
         * the chunk size has to be determined
         */
        SIZE, /**
         * the chunk data has to be forwarded
         */
        DATA, /**
         * the chunk footer has to be parsed
         */
        FOOTER
    }

    private ChunkedState chunkedState;

    private long remainingBodyLength;

    private boolean viaUpdated;

    private int chunkSize;

    private ByteBuffer buffer;

    /**
     * creates a new AbstractHttpProxyForwarder
     *
     * @param viaPseudonym the pseudonym used in the "Via" general-header field
     */
    public AbstractHttpProxyForwarder(String viaPseudonym) {
        this.viaPseudonym = viaPseudonym;
    }

    /**
     * parses a HTTP header
     *
     * @param header the header to parse
     * @throws IOException if an I/O exception occurs
     */
    protected abstract void parseHeader(String header) throws IOException;

    /**
     * tries to filter a header line
     *
     * @param lowerCaseHeaderLine the lower case version of the header line to
     * check
     * @return
     * <code>true</code> if the line must be filtered,
     * <code>false</code> otherwise
     */
    protected abstract boolean filterHeaderLine(String lowerCaseHeaderLine);

    /**
     * forwards data
     *
     * @param data
     * @param size the amount of data to forward
     */
    protected abstract void forwardData(ByteBuffer data, int size);

    @Override
    public void forward(ByteBuffer input) throws IOException {
        buffer = FrameworkTools.append(false, buffer, input);
        processBuffer();
        buffer.compact();
        buffer.flip();
    }

    /**
     * processes any buffered data
     *
     * @throws IOException if an I/O exception occurs
     */
    public void processBuffer() throws IOException {
        LOGGER.log(Level.FINE, "current state: {0}", state);
        switch(state) {
            case HEADER:
                readHeader();
                break;
            case BODY:
                readBody();
                break;
            case TUNNEL:
                if (buffer.hasRemaining()) {
                    forwardData(buffer, buffer.remaining());
                }
                break;
            default:
                LOGGER.log(Level.WARNING, "unknown state {0}", state);
        }
    }

    /**
     * splits a header into tokens and removes empty lines
     *
     * @param header the header
     * @param regex the regular expression used for splitting
     * @return the header tokens
     */
    protected String[] splitHeader(String header, String regex) {
        String[] headerTokens = header.split(regex);
        int i = 0;
        while (headerTokens[i].isEmpty()) {
            i++;
        }
        if (i != 0) {
            int trimmedLength = headerTokens.length - i;
            String[] trimmedTokens = new String[trimmedLength];
            System.arraycopy(headerTokens, i, trimmedTokens, 0, trimmedLength);
            headerTokens = trimmedTokens;
        }
        return headerTokens;
    }

    /**
     * parses the header lines
     *
     * @param headerLines the header lines
     * @param offset the offset where to start parsing
     * @param newHeader the new header to create
     */
    protected void parseHeaderLines(String[] headerLines, int offset, StringBuilder newHeader) {
        persistent = true;
        viaUpdated = false;
        lengthIndicator = LengthIndicator.UNKNOWN;
        for (int i = offset; i < headerLines.length; i++) {
            String headerLine = headerLines[i];
            String lowerCaseHeaderLine = headerLine.toLowerCase();
            if (!filterHeaderLine(lowerCaseHeaderLine)) {
                parseHeaderLine(headerLine, lowerCaseHeaderLine, newHeader);
            }
        }
        if (!viaUpdated) {
            newHeader.append("Via: ");
            newHeader.append(httpVersion);
            newHeader.append(' ');
            newHeader.append(viaPseudonym);
            newHeader.append("\r\n");
        }
    }

    /**
     * gets called whenever a message was completely processes
     */
    protected void nextMessage() {
        state = HttpState.HEADER;
    }

    private void readHeader() throws IOException {
        String header = getHeader();
        if (header == null) {
            return;
        }
        LOGGER.log(Level.FINEST, "header:\n{0}", header);
        parseHeader(header);
    }

    private void readBody() throws IOException {
        LOGGER.log(Level.FINE, "lengthIndicator: {0}", lengthIndicator);
        switch(lengthIndicator) {
            case CHUNKED:
                readChunkedBody();
                break;
            case CONTENT_LENGTH:
                readContentLengthBody();
                break;
            case UNKNOWN:
                forwardData(buffer, buffer.remaining());
                break;
            default:
                LOGGER.log(Level.WARNING, "Unknown lengthIndicator {0}", lengthIndicator);
        }
    }

    private void parseHeaderLine(String headerLine, String lowerCaseHeaderLine, StringBuilder stringBuilder) {
        if (lowerCaseHeaderLine.equals("transfer-encoding: chunked")) {
            state = HttpState.BODY;
            lengthIndicator = LengthIndicator.CHUNKED;
            chunkedState = ChunkedState.SIZE;
            stringBuilder.append(headerLine);
            stringBuilder.append("\r\n");
        } else if (lowerCaseHeaderLine.equals("connection: close")) {
            persistent = false;
        } else if (lowerCaseHeaderLine.startsWith("connection:")) {
        } else if (lowerCaseHeaderLine.startsWith("keep-alive")) {
        } else if (lowerCaseHeaderLine.startsWith("content-length")) {
            remainingBodyLength = getContentLength(headerLine);
            if (remainingBodyLength != 0) {
                state = HttpState.BODY;
                lengthIndicator = LengthIndicator.CONTENT_LENGTH;
            }
            stringBuilder.append(headerLine);
            stringBuilder.append("\r\n");
        } else if (lowerCaseHeaderLine.startsWith("content-") || lowerCaseHeaderLine.startsWith("transfer-encoding")) {
            state = HttpState.BODY;
            stringBuilder.append(headerLine);
            stringBuilder.append("\r\n");
        } else if (lowerCaseHeaderLine.startsWith("via:")) {
            stringBuilder.append(headerLine);
            stringBuilder.append(", ");
            stringBuilder.append(httpVersion);
            stringBuilder.append(' ');
            stringBuilder.append(viaPseudonym);
            stringBuilder.append("\r\n");
            viaUpdated = true;
        } else {
            stringBuilder.append(headerLine);
            stringBuilder.append("\r\n");
        }
    }

    private String getHeader() {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "searching for a header in {0}:\n{1}", new Object[] { buffer, FrameworkTools.toHex(buffer) });
        }
        int standardHeaderSize = offsetOf(buffer, "\r\n\r\n".getBytes());
        int fuckedUpHeaderSize = offsetOf(buffer, "\n\n".getBytes());
        int headerSize;
        if (standardHeaderSize == -1) {
            if (fuckedUpHeaderSize == -1) {
                return null;
            } else {
                headerSize = fuckedUpHeaderSize + 2;
            }
        } else {
            if (fuckedUpHeaderSize == -1) {
                headerSize = standardHeaderSize + 4;
            } else {
                if (standardHeaderSize > fuckedUpHeaderSize) {
                    headerSize = fuckedUpHeaderSize + 2;
                } else {
                    headerSize = standardHeaderSize + 4;
                }
            }
        }
        byte[] headerBytes = new byte[headerSize];
        buffer.get(headerBytes);
        String header = null;
        try {
            header = new String(headerBytes, "ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            LOGGER.log(Level.WARNING, "could not encode header", ex);
        }
        return header;
    }

    private static int offsetOf(ByteBuffer buffer, byte[] values) {
        int position = buffer.position();
        int valuesLength = values.length;
        int stopSearch = buffer.limit() - valuesLength + 1;
        for (int i = position; i < stopSearch; i++) {
            boolean match = true;
            for (int j = 0; j < valuesLength; j++) {
                if (buffer.get(i + j) != values[j]) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return i - position;
            }
        }
        return -1;
    }

    private long getContentLength(String contentLengthHeader) {
        int colonIndex = contentLengthHeader.indexOf(':');
        StringBuilder stringBuilder = new StringBuilder(contentLengthHeader.substring(colonIndex + 1).trim());
        int apostropheIndex = stringBuilder.indexOf("'");
        while (apostropheIndex != -1) {
            stringBuilder.deleteCharAt(apostropheIndex);
            apostropheIndex = stringBuilder.indexOf("'");
        }
        int semicolonIndex = stringBuilder.indexOf(";");
        String contentLengthString;
        if (semicolonIndex == -1) {
            contentLengthString = stringBuilder.toString();
        } else {
            contentLengthString = stringBuilder.substring(0, semicolonIndex);
        }
        try {
            long contentLength = Long.parseLong(contentLengthString);
            return contentLength;
        } catch (NumberFormatException ex) {
            LOGGER.log(Level.WARNING, "invalid length value: \"" + contentLengthString + '\"', ex);
        }
        return 0;
    }

    private void readContentLengthBody() throws IOException {
        int bodySize = (int) Math.min(buffer.remaining(), remainingBodyLength);
        forwardData(buffer, bodySize);
        remainingBodyLength -= bodySize;
        if (remainingBodyLength == 0) {
            nextMessage();
            processBuffer();
        }
    }

    private void readChunkedBody() throws IOException {
        switch(chunkedState) {
            case SIZE:
                determineChunkSize();
                break;
            case DATA:
                passChunkData();
                break;
            case FOOTER:
                passChunkFooter();
                break;
            default:
                LOGGER.log(Level.WARNING, "Unknown chunkedState {0}", chunkedState);
        }
    }

    private void determineChunkSize() throws IOException {
        int crlfOffset = offsetOf(buffer, "\r\n".getBytes());
        LOGGER.log(Level.FINE, "crlfOffset = {0}", crlfOffset);
        if (crlfOffset == -1) {
            return;
        }
        byte[] chunkSizeBytes = new byte[crlfOffset + 2];
        buffer.get(chunkSizeBytes);
        String chunkSizeString = new String(chunkSizeBytes);
        int semicolonIndex = chunkSizeString.indexOf(';');
        boolean sizeParsed = false;
        try {
            String sizeString;
            if (semicolonIndex == -1) {
                sizeString = chunkSizeString.substring(0, chunkSizeString.length() - 2);
            } else {
                sizeString = chunkSizeString.substring(0, semicolonIndex);
            }
            sizeString = sizeString.trim();
            chunkSize = Integer.parseInt(sizeString, 16) + 2;
            sizeParsed = true;
            LOGGER.log(Level.FINEST, "chunkSize (plus CRLF) = {0}", chunkSize);
        } catch (NumberFormatException ex) {
            LOGGER.log(Level.WARNING, "chunk size could not be parsed: " + chunkSizeString, ex);
        }
        ByteBuffer tmpBuffer = ByteBuffer.wrap(chunkSizeString.getBytes());
        forwardData(tmpBuffer, tmpBuffer.remaining());
        if (sizeParsed) {
            if (chunkSize == 2) {
                chunkedState = ChunkedState.FOOTER;
                passChunkFooter();
            } else {
                chunkedState = ChunkedState.DATA;
                passChunkData();
            }
        } else {
            lengthIndicator = LengthIndicator.UNKNOWN;
            readBody();
        }
    }

    private void passChunkData() throws IOException {
        int bytesToWrite = Math.min(buffer.remaining(), chunkSize);
        LOGGER.log(Level.FINEST, "bytesToWrite = {0}", bytesToWrite);
        if (bytesToWrite == 0) {
            return;
        }
        forwardData(buffer, bytesToWrite);
        chunkSize -= bytesToWrite;
        if (chunkSize == 0) {
            chunkedState = ChunkedState.SIZE;
            determineChunkSize();
        }
    }

    private void passChunkFooter() throws IOException {
        int crlfOffset = offsetOf(buffer, "\r\n".getBytes());
        LOGGER.log(Level.FINE, "crlfOffset = {0}", crlfOffset);
        if (crlfOffset == -1) {
            return;
        }
        forwardData(buffer, crlfOffset + 2);
        nextMessage();
        processBuffer();
    }
}
