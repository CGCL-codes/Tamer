package org.apache.commons.httpclient.methods;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.NameValuePair;

/**
 * A RequestEntity that contains a String.
 * 
 * @since 3.0
 */
public class StringRequestEntity implements RequestEntity {

    /** The content */
    private byte[] content;

    /** The charset */
    private String charset;

    /** The content type (i.e. text/html; charset=EUC-JP). */
    private String contentType;

    /**
     * <p>Creates a new entity with the given content. This constructor 
     * will use the default platform charset to convert the content string 
     * and will provide no content type.</p>
     *  
     * <p>This constructor may be deprecated or changed to use the 
     * default HTTP content charset (ISO-8859-1) in the release 3.1</p>
     * 
     * <p>It is strongly recommended to use 
     * {@link #StringRequestEntity(String, String, String)} constructor 
     * instead.</p>
     * 
     * @see #StringRequestEntity(String, String, String)
     * 
     * @param content The content to set.
     */
    public StringRequestEntity(String content) {
        super();
        if (content == null) {
            throw new IllegalArgumentException("The content cannot be null");
        }
        this.contentType = null;
        this.charset = null;
        this.content = content.getBytes();
    }

    /**
     * Creates a new entity with the given content, content type, and charset.
     *  
     * @param content The content to set.
     * @param contentType The type of the content, or <code>null</code>.  The value retured 
     *   by {@link #getContentType()}.  If this content type contains a charset and the charset
     *   parameter is null, the content's type charset will be used.
     * @param charset The charset of the content, or <code>null</code>.  Used to convert the 
     *   content to bytes.  If the content type does not contain a charset and charset is not null,
     *   then the charset will be appended to the content type.
     */
    public StringRequestEntity(String content, String contentType, String charset) throws UnsupportedEncodingException {
        super();
        if (content == null) {
            throw new IllegalArgumentException("The content cannot be null");
        }
        this.contentType = contentType;
        this.charset = charset;
        if (contentType != null) {
            HeaderElement[] values = HeaderElement.parseElements(contentType);
            NameValuePair charsetPair = null;
            for (int i = 0; i < values.length; i++) {
                if ((charsetPair = values[i].getParameterByName("charset")) != null) {
                    break;
                }
            }
            if (charset == null && charsetPair != null) {
                this.charset = charsetPair.getValue();
            } else if (charset != null && charsetPair == null) {
                this.contentType = contentType + "; charset=" + charset;
            }
        }
        if (this.charset != null) {
            this.content = content.getBytes(this.charset);
        } else {
            this.content = content.getBytes();
        }
    }

    public String getContentType() {
        return contentType;
    }

    /**
     * @return <code>true</code>
     */
    public boolean isRepeatable() {
        return true;
    }

    public void writeRequest(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        out.write(this.content);
        out.flush();
    }

    /**
     * @return The length of the content.
     */
    public long getContentLength() {
        return this.content.length;
    }

    /**
     * @return Returns the content.
     */
    public String getContent() {
        if (this.charset != null) {
            try {
                return new String(this.content, this.charset);
            } catch (UnsupportedEncodingException e) {
                return new String(this.content);
            }
        } else {
            return new String(this.content);
        }
    }

    /**
     * @return Returns the charset used to convert the content to bytes. <code>null</code> if
     * no charset as been specified.
     */
    public String getCharset() {
        return charset;
    }
}
