package com.google.gwt.user.client.rpc.impl;

import com.google.gwt.http.client.Request;

/**
 * A {@link Request} that is already canceled at the moment it is created.
 */
public class FailedRequest extends Request {

    public FailedRequest() {
        super();
    }
}
