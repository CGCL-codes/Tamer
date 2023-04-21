package com.genia.toolbox.web.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import com.genia.toolbox.basics.exception.technical.TechnicalIOException;

/**
 * This class wrapps a <code>HttpServletResponse</code> and allow to retrieve
 * the content of the response body.
 */
public class QueryableHttpServletResponse extends HttpServletResponseWrapper {

    /**
   * the <code>ServletOutputStream</code> that gathers data. It wrapps
   * <code>baOut</code>.
   */
    private transient ServletOutputStream dsOut;

    /**
   * the <code>ByteArrayOutputStream</code> that gathers data.
   */
    private transient ByteArrayOutputStream baOut;

    /**
   * the <code>ServletOutputStream</code> that gathers data. It wrapps
   * <code>dsOut</code>.
   */
    private transient PrintWriter printWriter;

    /**
   * Constructor.
   * 
   * @param response
   *          the <code>HttpServletResponse</code> wrapped.
   */
    public QueryableHttpServletResponse(final HttpServletResponse response) {
        super(response);
    }

    /**
   * Flush all the streams used by this class.
   * 
   * @throws IOException
   *           when an I/O exception occurred
   */
    @Override
    public void flushBuffer() throws IOException {
        if (baOut != null) {
            if (printWriter != null) {
                printWriter.flush();
            }
            dsOut.flush();
            baOut.flush();
        }
    }

    /**
   * Return <code>0</code> as this implementation does not provide any buffer.
   * 
   * @return 0
   */
    @Override
    public int getBufferSize() {
        return 0;
    }

    /**
   * Return the data written to this response.
   * 
   * @return the data written to this response
   * @throws TechnicalIOException
   *           when an I/O exception occurred
   */
    public byte[] getOutput() throws TechnicalIOException {
        initStreams();
        try {
            flushBuffer();
        } catch (final IOException e) {
            throw new TechnicalIOException(e);
        }
        return baOut.toByteArray();
    }

    /**
   * Returns a <code>ServletOutputStream</code> suitable for writing binary
   * data in the response.
   * 
   * @return a <code>ServletOutputStream</code> for writing binary data
   */
    @Override
    public ServletOutputStream getOutputStream() {
        initStreams();
        return dsOut;
    }

    /**
   * Returns a <code>PrintWriter</code> object that can send character text to
   * the client.
   * 
   * @return a <code>PrintWriter</code> object that can return character data
   *         to the client
   */
    @Override
    public PrintWriter getWriter() {
        if (printWriter == null) {
            initStreams();
            printWriter = new PrintWriter(new OutputStreamWriter(dsOut, Charset.forName(getCharacterEncoding())));
        }
        return printWriter;
    }

    /**
   * Clears any data that exists in the buffer as well as the status code and
   * headers.
   */
    @Override
    public void reset() {
        resetBuffer();
    }

    /**
   * Clears the content of the underlying buffer in the response without
   * clearing headers or status code.
   */
    @Override
    public void resetBuffer() {
        reInitStreams();
    }

    /**
   * Initialize the different streams used by this class.
   */
    private void initStreams() {
        if (baOut == null) {
            baOut = new ByteArrayOutputStream();
            dsOut = new DumbServletOutputStream(baOut);
        }
    }

    /**
   * Reinitialize the different streams used by this class.
   */
    private void reInitStreams() {
        baOut = null;
        dsOut = null;
        printWriter = null;
    }
}
