package org.skunk.dav.client;

import java.util.Map;

public interface DAVResponse {

    public Map getResponseHeaders();

    public void setResponseHeaders(Map m);

    public byte[] getResponseBody();

    public void setResponseBody(byte[] body);

    public int getStatus();

    public void setStatus(int status);
}
