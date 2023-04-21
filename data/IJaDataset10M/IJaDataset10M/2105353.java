package com.googlecode.ascrblr.api.util;

import java.io.IOException;
import java.net.HttpURLConnection;

public class AuthenticationException extends ServiceException {

    private static final long serialVersionUID = -6495869951894339873L;

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(HttpURLConnection httpConn) throws IOException {
        super(httpConn);
    }
}
