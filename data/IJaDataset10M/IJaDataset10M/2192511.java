package com.volantis.mps.bms;

import java.net.URL;

/**
 * <p>Interface representing a Message.</p>
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and
 * source levels.</strong></p>
 *
 * @volantis-api-include-in InternalAPI
 */
public interface Message {

    /**
     * Set the URL where the message can be obtained from. This URL is expected
     * to return XDIME?
     *
     * @param url URL - may not be null.
     */
    void setURL(URL url);

    /**
     * Return the URL that contains the XDIME resource. May not be null.
     *
     * @return a URL.
     */
    URL getURL();

    /**
     * Set the subject of this Message - may be null.
     *
     * @param subject the message subject, may be null.
     */
    void setSubject(String subject);

    /**
     * Return the subject of the message.
     *
     * @return a String - may be null.
     */
    String getSubject();

    /**
     * Set the character encoding of this Message. Supported character
     * encoding sets can be found at:
     * <ul>
     * <li><a href="http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html" title="Java 5 Supported Encodings">
     * http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html</a></li>
     * <li><a href="http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html" title="Java 4 Supported Encodings">
     * http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html</a></li>
     * <li><a href="http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html" title="Java 3 Supported Encodings">
     * http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html</a></li>
     * </ul>
     *
     * @param characterEncoding the character encoding name - not null.
     */
    void setCharacterEncoding(String characterEncoding);

    /**
     * Return the character-encoding of the message. Supported character
     * encoding sets can be found at:
     * <ul>
     * <li><a href="http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html" title="Java 5 Supported Encodings">
     * http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html</a></li>
     * <li><a href="http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html" title="Java 4 Supported Encodings">
     * http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html</a></li>
     * <li><a href="http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html" title="Java 3 Supported Encodings">
     * http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html</a></li>
     * </ul>
     *
     * @return the character encoding of the message.
     */
    String getCharacterEncoding();

    /**
     * Sets the content that is to be sent.
     *
     * @param content the content to be sent.
     */
    void setContent(String content);

    /**
     * Returns the contents of the message that will be sent.
     * @return the contents of the message that will be sent.
     */
    String getContent();
}
