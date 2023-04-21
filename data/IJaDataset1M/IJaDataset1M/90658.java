package com.google.gwt.user.client.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ResponseTextHandler;

/**
 * Native implementation associated with
 * {@link com.google.gwt.user.client.HTTPRequest}.
 */
public class HTTPRequestImpl {

    public boolean asyncGet(String url, ResponseTextHandler handler) {
        return asyncGet(null, null, url, handler);
    }

    public boolean asyncGet(String user, String pwd, String url, ResponseTextHandler handler) {
        return asyncGetImpl(user, pwd, url, handler);
    }

    public boolean asyncPost(String url, String postData, ResponseTextHandler handler) {
        return asyncPost(null, null, url, postData, handler);
    }

    public boolean asyncPost(String user, String pwd, String url, String postData, ResponseTextHandler handler) {
        return asyncPostImpl(user, pwd, url, postData, handler);
    }

    public JavaScriptObject createXmlHTTPRequest() {
        return doCreateXmlHTTPRequest();
    }

    /**
   * All the supported browsers except for IE instantiate it as shown.
   */
    protected native JavaScriptObject doCreateXmlHTTPRequest();

    private native boolean asyncGetImpl(String user, String pwd, String url, ResponseTextHandler handler);

    private native boolean asyncPostImpl(String user, String pwd, String url, String postData, ResponseTextHandler handler);
}
