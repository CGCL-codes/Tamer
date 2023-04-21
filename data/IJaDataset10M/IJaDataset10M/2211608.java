package com.beanstalktech.common.server;

import java.net.*;

/**
 * Defines an HTTP Server request
 * 
 * @author Beanstalk Technologies LLC
 * @version 1.0 5/24/2002
 * @since Beanstalk V2.2
 * @see com.beanstalktech.common.server.ServerAdapterManager
 */
public class HTTPServerRequest {

    protected URL m_URL;

    protected String m_method;

    protected byte[] m_requestBytes;

    protected String m_encodingMethod;

    protected String m_contentType;

    /**
     * Creates a request and initializes all properties
     * <P>
     * @param uRL URL for the request
     * @param method Method to specify in the HTTP header
     * @param requestBytes The actual request contents
     * @param encodingMethod Encoding method for the request byte array
     */
    public HTTPServerRequest(URL uRL, String method, byte[] requestBytes, String encodingMethod, String contentType) {
        m_URL = uRL;
        m_method = method;
        m_requestBytes = requestBytes;
        m_encodingMethod = encodingMethod;
        m_contentType = contentType;
    }

    /**
     * Sets the URL of the request
     *
     * @param url The URL
     */
    public void setURL(URL uRL) {
        m_URL = uRL;
    }

    /**
     * Gets the URL of the request
     *
     * @return The URL
     */
    public URL getURL() {
        return m_URL;
    }

    /**
     * Gets the HTTP method
     *
     * @return The HTTP action
     */
    public String getMethod() {
        return m_method;
    }

    /**
     * Gets the HTTP request payload
     *
     * @return The payloaed
     */
    public byte[] getRequestBytes() {
        return m_requestBytes;
    }

    /**
     * Gets the length of the request
     *
     * @return The length of the soap request
     */
    public int getRequestBytesLength() {
        return m_requestBytes.length;
    }

    /**
     * Gets the HTTP content type
     *
     * @return The HTTP content type
     */
    public String getContentType() {
        return m_contentType;
    }
}
