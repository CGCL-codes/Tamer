package org.fpse.download;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fpse.server.utils.OutputUtils;

/**
 * Created on Dec 30, 2006 7:00:42 PM by Ajay
 */
public class CharsetAwareInputStream extends FilterInputStream {

    private static final Log LOG = LogFactory.getLog(CharsetAwareInputStream.class);

    private final String m_charset;

    private boolean m_closed;

    private Throwable m_trace;

    protected CharsetAwareInputStream(InputStream in, String charset) {
        super(in);
        m_charset = charset;
        m_closed = false;
        m_trace = new Throwable();
    }

    public String getCharset() {
        return m_charset;
    }

    public Reader getReader() throws UnsupportedEncodingException {
        String charset = getCharset();
        if (null == charset) charset = OutputUtils.DEFAULT_CHARSET;
        return new InputStreamReader(this, charset);
    }

    @Override
    public void close() throws IOException {
        super.close();
        m_closed = true;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (!m_closed) {
            close();
            LOG.error("Memory leak", m_trace);
        }
    }
}
