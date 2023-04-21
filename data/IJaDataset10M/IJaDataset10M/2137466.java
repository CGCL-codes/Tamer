package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import org.apache.http.protocol.HTTP;

/**
 * A self contained, repeatable entity that obtains its content from 
 * a {@link String}.
 *
 *
 * @version $Revision: 744523 $
 * 
 * @since 4.0
 */
public class StringEntity extends AbstractHttpEntity implements Cloneable {

    protected final byte[] content;

    public StringEntity(final String s, String charset) throws UnsupportedEncodingException {
        super();
        if (s == null) {
            throw new IllegalArgumentException("Source string may not be null");
        }
        if (charset == null) {
            charset = HTTP.DEFAULT_CONTENT_CHARSET;
        }
        this.content = s.getBytes(charset);
        setContentType(HTTP.PLAIN_TEXT_TYPE + HTTP.CHARSET_PARAM + charset);
    }

    public StringEntity(final String s) throws UnsupportedEncodingException {
        this(s, null);
    }

    public boolean isRepeatable() {
        return true;
    }

    public long getContentLength() {
        return this.content.length;
    }

    public InputStream getContent() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    public void writeTo(final OutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        outstream.write(this.content);
        outstream.flush();
    }

    /**
     * Tells that this entity is not streaming.
     *
     * @return <code>false</code>
     */
    public boolean isStreaming() {
        return false;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
