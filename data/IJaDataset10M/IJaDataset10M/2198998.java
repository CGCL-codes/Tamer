package com.google.gwt.user.client.impl;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Internet Explorer 6 implementation of
 * {@link com.google.gwt.user.client.impl.HttpRequestImpl}.
 */
class HTTPRequestImplIE6 extends HTTPRequestImpl {

    protected native JavaScriptObject doCreateXmlHTTPRequest();
}
