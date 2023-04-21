package net.sourceforge.pharos.security;

import net.sourceforge.pharos.constants.Constants;
import net.sourceforge.pharos.exception.ApplicationException;
import net.sourceforge.pharos.web.Response;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author kaushikr
 *
 */
final class CompressedResponse extends HttpServletResponseWrapper {

    private final HttpServletResponse response;

    private HttpServletRequest request;

    private PrintWriter writer;

    private ServletOutputStream stream;

    /**
	 * Constructor for CompressedResponse. 
	 * @param response
	 */
    public CompressedResponse(final HttpServletResponse response) {
        super(response);
        this.response = response;
    }

    /**
	 * Constructor for CompressedResponse. 
	 * @param response
	 * @param request
	 */
    public CompressedResponse(final HttpServletResponse response, final HttpServletRequest request) {
        super(response);
        this.response = response;
        this.request = request;
    }

    /**
	 * @throws IOException
	 */
    public void close() throws IOException {
        if (null != this.writer) {
            this.writer.close();
        }
        if (null != this.stream) {
            this.stream.close();
        }
    }

    /**
	 * @see javax.servlet.ServletResponseWrapper#flushBuffer()
	 */
    @Override
    public void flushBuffer() throws IOException {
        if (null != this.writer) {
            this.writer.flush();
        }
        if (null != this.stream) {
            this.stream.flush();
        }
    }

    /**
	 * @see javax.servlet.ServletResponseWrapper#getOutputStream()
	 */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (null != this.writer) {
            throw new ApplicationException("getWriter() has already been called!");
        }
        createCompressedStream();
        return this.stream;
    }

    /**
	 * @throws IOException
	 */
    private void createCompressedStream() throws IOException {
        if (null == this.stream) {
            if (null == this.request) {
                this.stream = this.response.getOutputStream();
            } else {
                final Response res = (Response) this.request.getAttribute(Constants.ACTION_RESPONSE);
                if (null == res) {
                    this.stream = this.response.getOutputStream();
                } else {
                    if (res.isForCompression()) {
                        this.stream = new CompressedResponseStream(response);
                    } else {
                        this.stream = this.response.getOutputStream();
                    }
                }
            }
        }
    }

    /**
	 * @see javax.servlet.ServletResponseWrapper#getWriter()
	 */
    @Override
    public PrintWriter getWriter() throws IOException {
        if (null != this.writer) {
            return this.writer;
        }
        if (null != this.stream) {
            throw new ApplicationException("getOutputStream() has already been called!");
        }
        createCompressedStream();
        this.writer = createPrintWriter(response, stream);
        return this.writer;
    }

    /**
	 * @param response
	 * @param stream
	 * @return
	 * @throws UnsupportedEncodingException
	 */
    private static PrintWriter createPrintWriter(final HttpServletResponse response, final OutputStream stream) {
        final String encoding = response.getCharacterEncoding();
        if (null == encoding || 0 == encoding.length()) {
            return new PrintWriter(stream);
        }
        try {
            return new PrintWriter(new OutputStreamWriter(stream, encoding));
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationException(e);
        }
    }
}
