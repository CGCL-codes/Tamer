package org.skunk.dav.client;

import java.util.Map;

/**
 *
 */
public interface DAVRequest {

    public Map getRequestHeaders();

    public void setRequestHeaders(Map headers);

    public DAVMethodName getRequestMethodName();

    public String getRequestURL();

    public byte[] getRequestBody();

    public void setRequestBody(byte[] body);
}
