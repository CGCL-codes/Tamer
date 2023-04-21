package org.octaedr.httpudp;

/**
 * Request line.
 */
public class RequestLine implements Consts {

    /** Request method. */
    private final String method;

    /** Request URI. */
    private final String uri;

    /** HTTP protocol version. */
    private final String httpVersion;

    /**
     * Constructs request line.
     * 
     * @param method
     *            Request method.
     * @param uri
     *            Request URI.
     * @param httpVersion
     *            HTTP protocol version.
     */
    public RequestLine(final String method, final String uri, final String httpVersion) {
        if (method == null) {
            throw new IllegalArgumentException("Method may not be null");
        }
        if (uri == null) {
            throw new IllegalArgumentException("URI may not be null");
        }
        if (httpVersion == null) {
            throw new IllegalArgumentException("HTTP version may not be null");
        }
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    /**
     * Returns request method.
     * 
     * @return Request method.
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * Returns request URI.
     * 
     * @return Request URI.
     */
    public String getURI() {
        return this.uri;
    }

    /**
     * Returns HTTP protocol version used.
     * 
     * @return HTTP protocol version.
     */
    public String getHttpVersion() {
        return this.httpVersion;
    }

    /**
     * Parses given string as request line.
     * 
     * @param firstLine
     *            First message line to be parsed.
     * 
     * @return Request line object if line was valid representation of request
     *         line, null otherwise.
     */
    static RequestLine parseRequestLine(final String firstLine) {
        String[] parts = firstLine.split(" ", 3);
        if (parts.length != 3) {
            return null;
        }
        parts[0] = parts[0].trim();
        parts[1] = parts[1].trim();
        if (parts[0].length() == 0 || parts[1].length() == 0) {
            return null;
        }
        if (!HTTP_VERSION__1_0.equals(parts[2]) && !HTTP_VERSION__1_1.equals(parts[2])) {
            return null;
        }
        return new RequestLine(parts[0], parts[1], parts[2]);
    }

    void write(final ByteArrayOutputStream outputStream) {
        outputStream.write(this.method);
        outputStream.writeSP();
        outputStream.write(this.uri);
        outputStream.writeSP();
        outputStream.write(this.httpVersion);
        outputStream.writeCRLF();
    }

    public void write(StringBuilder builder) {
        builder.append(this.method);
        builder.append(' ');
        builder.append(this.uri);
        builder.append(' ');
        builder.append(this.httpVersion);
        builder.append("\r\n");
    }
}
