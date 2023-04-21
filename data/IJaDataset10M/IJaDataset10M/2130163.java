package org.octaedr.httpudp;

import java.net.InetAddress;

/**
 * Response message.
 */
public class Response extends Message {

    /** Status line. */
    private final StatusLine statusLine;

    /**
     * Constructs response message.
     * 
     * @param statusLine
     *            Response status line.
     */
    public Response(final StatusLine statusLine) {
        super();
        if (statusLine == null) {
            throw new IllegalArgumentException("Status line may not be null");
        }
        this.statusLine = statusLine;
    }

    /**
     * Constructs response message.
     * 
     * @param statusLine
     *            Response status line.
     * @param httpVersion
     *            HTTP protocol version.
     * @param statusCode
     *            Response status line.
     * @param reasonPhrase
     *            Response reason phrase.
     */
    public Response(final String httpVersion, final int statusCode, final String reasonPhrase) {
        this(new StatusLine(httpVersion, statusCode, reasonPhrase));
    }

    /**
     * Constructs response message.
     * 
     * @param statusLine
     *            Response status line.
     * @param sourceHost
     *            Message source host address.
     * @param sourcePort
     *            Message source host port.
     */
    Response(final StatusLine statusLine, final InetAddress sourceHost, final int sourcePort) {
        super(sourceHost, sourcePort);
        if (statusLine == null) {
            throw new IllegalArgumentException("Status line may not be null");
        }
        this.statusLine = statusLine;
    }

    /**
     * Constructs response message from another response.
     * 
     * @param response
     *            The source of response data.
     */
    public Response(final Response response) {
        super(response);
        this.statusLine = response.statusLine;
    }

    /**
     * Returns message status line.
     * 
     * @return Status line.
     */
    public StatusLine getStatusLine() {
        return this.statusLine;
    }

    void write(final ByteArrayOutputStream outputStream) {
        this.statusLine.write(outputStream);
        super.write(outputStream);
    }

    void write(final StringBuilder builder, final boolean includeBody, final boolean hexEncodeBody) {
        this.statusLine.write(builder);
        super.write(builder, includeBody, hexEncodeBody);
    }
}
