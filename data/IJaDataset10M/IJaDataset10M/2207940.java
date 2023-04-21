package net.sf.jzeno.servletfilter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.log4j.Logger;

/**
 * Class that wraps a HttpServletResponse so that it can be GZIPped.
 * It does this conditionally, use setCompression before your first write to select compressed/uncompressed.
 */
public class GZIPResponseWrapper extends HttpServletResponseWrapper {

    private static Logger log = Logger.getLogger(GZIPResponseWrapper.class);

    protected HttpServletResponse origResponse;

    protected ServletOutputStream stream;

    protected PrintWriter writer;

    protected boolean compression = false;

    public GZIPResponseWrapper(HttpServletResponse httpservletresponse) {
        super(httpservletresponse);
        origResponse = null;
        stream = null;
        writer = null;
        origResponse = httpservletresponse;
    }

    public ServletOutputStream createOutputStream() throws IOException {
        if (isCompression()) {
            return new GZIPResponseStream(origResponse);
        } else {
            return origResponse.getOutputStream();
        }
    }

    public void finishResponse() {
        try {
            if (writer != null) writer.flush(); else if (stream != null) stream.flush();
        } catch (IOException ioexception) {
        }
    }

    public void flushBuffer() throws IOException {
        try {
            stream.flush();
        } catch (IOException e) {
        }
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) throw new IllegalStateException("getWriter() has already been called!");
        if (stream == null) stream = createOutputStream();
        return stream;
    }

    public PrintWriter getWriter() throws IOException {
        if (writer != null) return writer;
        if (stream != null) {
            throw new IllegalStateException("getOutputStream() has already been called!");
        } else {
            stream = createOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(stream, "UTF-8"));
            return writer;
        }
    }

    public void setContentLength(int i) {
    }

    public boolean isCompression() {
        return compression;
    }

    public void setCompression(boolean compression) {
        log.info("compression = <" + compression + ">");
        this.compression = compression;
    }
}
