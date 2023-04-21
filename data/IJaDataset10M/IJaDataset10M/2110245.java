package com.faunos.util.net.http;

import java.nio.ByteBuffer;
import java.util.List;
import com.faunos.util.io.ImmutableBuffer;

/**
 * Simple 404 Not Found response.
 *
 * @author Babak Farhang
 */
public class NotFoundStage extends ResponseHeaderStage {

    private static final ImmutableBuffer htmlPage = CannedResponses.createBuffer("<html>" + "<head>" + "<title>404 - Not Found</title>" + "</head>" + "<body>" + "<h2>404 - Not Found</h2>" + "</body>" + "</html>");

    private final boolean head;

    /**
     * Creates a new instance.
     * 
     * @param close
     *        if <tt>true</tt>, then a "Connection: close" header is
     *        included.
     * @param head
     *        if <tt>true</tt>, then this is the response to an HTTP HEAD, and
     *        no content (HTTP entity) will be written.
     */
    public NotFoundStage(boolean close, boolean head) {
        setStatusNotFound404();
        setContentTypeHtml();
        setContentLength(htmlPage.size());
        if (close) setConnectionClose();
        this.head = head;
    }

    /**
     * Adds a plain vanilla "not-found" page.
     */
    @Override
    protected void addContent(List<ByteBuffer> response) {
        if (!head) response.add(htmlPage.buffer());
    }
}
