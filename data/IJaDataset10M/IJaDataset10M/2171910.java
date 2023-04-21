package com.yubuild.coreman.web.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GZIPResponseWrapper extends HttpServletResponseWrapper {

    private final transient Log log;

    protected HttpServletResponse origResponse;

    protected ServletOutputStream stream;

    protected PrintWriter writer;

    protected int error;

    public GZIPResponseWrapper(HttpServletResponse response) {
        super(response);
        log = LogFactory.getLog(com.yubuild.coreman.web.filter.GZIPResponseWrapper.class);
        origResponse = null;
        stream = null;
        writer = null;
        error = 0;
        origResponse = response;
    }

    public ServletOutputStream createOutputStream() throws IOException {
        return new GZIPResponseStream(origResponse);
    }

    public void finishResponse() {
        try {
            if (writer != null) writer.close(); else if (stream != null) stream.close();
        } catch (IOException ioexception) {
        }
    }

    public void flushBuffer() throws IOException {
        if (stream != null) stream.flush();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) throw new IllegalStateException("getWriter() has already been called!");
        if (stream == null) stream = createOutputStream();
        return stream;
    }

    public PrintWriter getWriter() throws IOException {
        if (origResponse != null && origResponse.isCommitted()) return super.getWriter();
        if (writer != null) return writer;
        if (stream != null) {
            throw new IllegalStateException("getOutputStream() has already been called!");
        } else {
            stream = createOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(stream, origResponse.getCharacterEncoding()));
            return writer;
        }
    }

    public void setContentLength(int i) {
    }

    public void sendError(int error, String message) throws IOException {
        super.sendError(error, message);
        this.error = error;
        if (log.isDebugEnabled()) log.debug("sending error: " + error + " [" + message + "]");
    }
}
